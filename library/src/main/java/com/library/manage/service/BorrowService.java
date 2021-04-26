package com.library.manage.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.manage.entity.AdminUserRole;
import com.library.manage.entity.Book;
import com.library.manage.entity.Borrow;
import com.library.manage.entity.User;
import com.library.manage.mapper.BookMapper;
import com.library.manage.mapper.BorrowMapper;
import com.library.manage.model.vo.QueryBorrowListVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class BorrowService {
    //状态 -- 未还书   借阅类型 -- 预约借书
    private final static String ORDER_TYPE = "1";
    //状态 -- 已还书  借阅类型 -- 普通借书
    private final static String NORMAL_TYPE = "0";
    //状态 -- 已预约
    private final static String YUYUE_TYPE = "2";
    //状态 -- 预约超时
    private final static String OVERTIME_TYPE = "3";
    //角色 -- 学生
    private final static int STUDENT_ROLE = 10;
    @Autowired
    private AdminUserRoleService adminUserRoleService;
    @Autowired
    private BorrowMapper borrowmapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private UserService userService;

    /**
     * 新增借阅记录
     *
     * @param bookid   书id
     * @param bookname 书名
     * @param type     借阅类型（借阅，预约借阅）
     * @return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public int saveBorrow(Integer bookid, String bookname, String type, String mailAccount) {
        if (StringUtils.isNotBlank(bookname) && StringUtils.isNotBlank(type) && null != bookid) {
            Borrow borrow = new Borrow();
            borrow.setBookname(bookname);
            borrow.setBookid(bookid);
            borrow.setType(type);
//            获取用户名
            Subject subject = SecurityUtils.getSubject();
            String username = subject.getPrincipal().toString();
            borrow.setUsername(username);
//            判断用户等级
            Integer level = userService.findByUsername(username).getLevel();
            if (level > 20) {
                return 2;
            }
            try {
                Book books = bookMapper.selectOne(Wrappers.<Book>lambdaQuery()
                        .eq(Book::getId, bookid)
                        .between(level >= 10, Book::getCid, 1, 4));
                if (null != books) {
                    Book book = new Book();
//                    在库不为0
                    if (books.getStock() > 0 && books.getStock() <= books.getSum()) {
                        if (NORMAL_TYPE.equals(type)) {
//                            普通借书
                            borrow.setStatus(ORDER_TYPE);
                        } else if (ORDER_TYPE.equals(type)) {
//                            预约借书
                            borrow.setStatus(YUYUE_TYPE);
                            borrow.setMail(mailAccount);
                        }
                        book.setStock(books.getStock() - 1);
                        bookMapper.update(book, Wrappers.<Book>lambdaQuery().eq(Book::getId, bookid));
                    } else if (books.getStock() == 0) {
                        if (NORMAL_TYPE.equals(type)) {
                            return 3;
                        } else if (ORDER_TYPE.equals(type)) {
                            borrow.setStatus(YUYUE_TYPE);
                            borrow.setMail(mailAccount);
                        }
                    }
                }
                level = borrowmapper.insert(borrow);
                if (ORDER_TYPE.equals(type)) {
                    this.order(mailAccount, ORDER_TYPE);
                }
                return level;
            } catch (Exception e) {
                log.error("保存出错");
            }
        }
        return 0;
    }

    /**
     * 查询借阅记录
     */
    public Page<Borrow> getBorrowList(QueryBorrowListVO vo) {
        Subject subject = SecurityUtils.getSubject();
        String name = subject.getPrincipal().toString();
        Page<Borrow> page = new Page<>(vo.getCurrent(), vo.getSize());
        IPage<Borrow> borrowIPage = borrowmapper.selectPage(page, Wrappers.<Borrow>lambdaQuery()
                .like(StringUtils.isNotBlank(vo.getUsername()), Borrow::getUsername, vo.getUsername())
                .like(StringUtils.isNotBlank(vo.getBookname()), Borrow::getBookname, vo.getBookname())
                .eq(userService.getUserRole(name) == STUDENT_ROLE, Borrow::getUsername, name)
                .eq(StringUtils.isNotBlank(vo.getStatus()), Borrow::getStatus, vo.getStatus())
                .eq(StringUtils.isNotBlank(vo.getType()), Borrow::getType, vo.getType())
                .orderByAsc(Borrow::getBorrowTime));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        page.setRecords(borrowIPage.getRecords().stream().peek(m -> {
            //计算是否需要罚款，还书时间为空，且状态为未还  则用借书时间-当前查看时间，一天1块
            this.upBorrow(df, m);
            if (ORDER_TYPE.equals(m.getType())) {
                m.setType("预约借书");
            } else if (NORMAL_TYPE.equals(m.getType())) {
                m.setType("普通借书");
            }
        }).collect(Collectors.toList()));
        return page;
    }

    private void upBorrow(DateFormat df, Borrow m) {
        if (m.getReturnTime() == null) {
            long time = 0;
            try {
                time = DateUtil.between(df.parse(m.getBorrowTime()), DateUtil.date(), DateUnit.DAY);
            } catch (ParseException e) {
                log.error("时间处理出错！");
            }
            if (time > 60 && ORDER_TYPE.equals(m.getStatus())) {
                m.setMoney((int) (time - 60));
            }
            //如果预约的时间超过一个星期
            if (time > 7 && YUYUE_TYPE.equals(m.getStatus())) {
                m.setStatus(OVERTIME_TYPE);
                Book book = bookMapper.selectOne(Wrappers.<Book>lambdaQuery().eq(Book::getId, m.getBookid()));
                if (null != book && book.getStock() < book.getSum()) {
                    book.setStock(book.getStock() + 1);
                    bookMapper.updateById(book);
                }
            }
            borrowmapper.updateById(m);
        }
    }

    /**
     * 还书或交罚款或取预约书
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public int updateBorrow(String username, String status, Integer money, Integer bookid, Integer id) {
        if (ORDER_TYPE.equals(status) && null != money) {
            return 2;
        }
        Borrow books = borrowmapper.selectOne(Wrappers.<Borrow>lambdaQuery()
                .eq(Borrow::getBookid, bookid)
                .eq(Borrow::getId, id)
                .eq(Borrow::getUsername, username));
        if (null != books) {
            Borrow borrow = new Borrow();
            //0已还书
            // 1未还书  传入的status为1时，更新状态为已还书0
            // 2已预约  更新状态为1 未还书
            // 3预约超时
            if (ORDER_TYPE.equals(status) && null == money) {
                borrow.setStatus(NORMAL_TYPE);
                Book book = bookMapper.selectOne(Wrappers.<Book>lambdaQuery().eq(Book::getId, bookid));
                if (null != book) {
                    //有人还书过后，如果书籍的在库数量为0 ，查询是否有预约借书的，如果有，则发邮件通知该读者
                    if (book.getStock() == 0) {
                        Borrow borrow1 = borrowmapper.selectOne(Wrappers.<Borrow>lambdaQuery()
                                .eq(Borrow::getBookid, bookid)
                                .eq(Borrow::getStatus, YUYUE_TYPE)
                                .eq(Borrow::getType, ORDER_TYPE)
                                .orderByDesc(Borrow::getBorrowTime));
                        if (borrow1 != null && borrow1.getMail() != null) {
                            order(borrow1.getMail(), YUYUE_TYPE);
                        } else {
                            book.setStock(book.getStock() + 1);
                            bookMapper.updateById(book);
                        }
                    } else if (book.getStock() < book.getSum() && book.getStock() > 0) {
                        //库存>在库数>0
                        book.setStock(book.getStock() + 1);
                        bookMapper.updateById(book);
                    }
                }
                borrow.setReturnTime(DateUtil.now());
            } else if (YUYUE_TYPE.equals(status)) {
                borrow.setStatus(ORDER_TYPE);
                borrow.setBorrowTime(DateUtil.now());
            } else if (null != money) {
                borrow.setMoney(0);
            }
            return borrowmapper.update(borrow, Wrappers.<Borrow>lambdaUpdate()
                    .eq(Borrow::getUsername, username)
                    .eq(Borrow::getId, id));
        } else {
            return 0;
        }
    }

    /**
     * 发送预约成功邮件
     */
    @Async("asyncPool")
    public void order(String mailAccount, String type) {
        MailAccount account = new MailAccount();
        account.setHost("smtp.qq.com");
        account.setPort(25);
        account.setAuth(true);
        account.setFrom("1747749585@qq.com");
        account.setUser("1747749585@qq.com");
        account.setPass("iaeclrbqwiljchdb"); //密码
        if (ORDER_TYPE.equals(type)) {
            MailUtil.send(account, mailAccount, "图书馆", "预约借书成功，请关注您所需要的图书。\n邮件来自图书馆", false);
        } else if (YUYUE_TYPE.equals(type)) {
            MailUtil.send(account, mailAccount, "图书馆", "您所预约的图书现在可以借阅了，请尽快来借阅。\n邮件来自图书馆", false);
        }
    }

    /**
     * 查询借阅是否超时  定时任务  每个月1号凌晨1点
     */
    @Async("asyncPool")
    @Scheduled(cron = "0 0 1 1 * ?")
    public void queryStatus() {
        List<AdminUserRole> role = adminUserRoleService.getAll();
        for (AdminUserRole role1 : role) {
            String userName = userService.getUserName(role1.getUid());
            Borrow borrow = borrowmapper.selectOne(Wrappers.<Borrow>lambdaQuery()
                    .eq(Borrow::getUsername, userName)
                    .eq(Borrow::getStatus, "1")
                    .orderByAsc(Borrow::getBorrowTime));
            if (null != borrow && borrow.getReturnTime() == null && ORDER_TYPE.equals(borrow.getStatus())) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long time = 0;
                try {
                    time = DateUtil.between(df.parse(borrow.getBorrowTime()), DateUtil.date(), DateUnit.DAY);
                } catch (ParseException e) {
                    log.error("定时任务错误{}", e.toString());
                    e.printStackTrace();
                }
                if (time > 90) {
                    User user = new User();
                    user.setUsername(userName);
                    userService.upUser(user);
                }
            }
        }
    }
}
