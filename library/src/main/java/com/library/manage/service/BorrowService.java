package com.library.manage.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.manage.VO.QueryBorrowListVO;
import com.library.manage.entity.Book;
import com.library.manage.entity.Borrow;
import com.library.manage.entity.User;
import com.library.manage.mapper.BookMapper;
import com.library.manage.mapper.BorrowMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;


@Service
@Slf4j
public class BorrowService {
    @Autowired
    private BorrowMapper borrowmapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private UserService userService;

    private final static String ORDER_TYPE = "1";
    private final static String NORMAL_TYPE = "0";
    private final static String YUYUE_TYPE = "2";
    private final static int STUDENT_ROLE = 10;


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
            Subject subject = SecurityUtils.getSubject();
            String username = subject.getPrincipal().toString();
            borrow.setUsername(username);
            Integer level = userService.findByUsername(username).getLevel();
            if (level > 20) {
                return 2;
            }
            try {
                Book books = bookMapper.selectOne(Wrappers.<Book>lambdaQuery().eq(Book::getId, bookid).between(level >= 10, Book::getCid, 1, 4));
                if (null != books) {
                    Book book = new Book();
                    if (ORDER_TYPE.equals(type)) {
                        borrow.setStatus(YUYUE_TYPE);
                    }else if (NORMAL_TYPE.equals(type)){
                        borrow.setStatus(ORDER_TYPE);
                    }
                    if (books.getStock() > 0) {
                        book.setStock(books.getStock() - 1);
                    } else {
                        if (ORDER_TYPE.equals(type)) {
                            borrow.setStatus(YUYUE_TYPE);
                        }else {
                            return 0;
                        }
                    }
                    bookMapper.update(book, Wrappers.<Book>lambdaQuery().eq(Book::getId, bookid));
                }
                if (ORDER_TYPE.equals(type)) {
                    this.order(mailAccount);
                }
                return borrowmapper.insert(borrow);
            } catch (Exception e) {
                log.error("保存出错");
                e.printStackTrace();
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
        IPage<Borrow> borrowIPage = borrowmapper.selectPage(page,
                Wrappers.<Borrow>lambdaQuery()
                        .like(StringUtils.isNotBlank(vo.getUsername()), Borrow::getUsername, vo.getUsername())
                        .like(StringUtils.isNotBlank(vo.getBookname()), Borrow::getBookname, vo.getBookname())
                        .eq(userService.getUserRole(name) == STUDENT_ROLE, Borrow::getUsername, name)
                        .eq(StringUtils.isNotBlank(vo.getStatus()), Borrow::getStatus, vo.getStatus())
                        .eq(StringUtils.isNotBlank(vo.getType()), Borrow::getType, vo.getType())
                        .orderByAsc(Borrow::getBorrowTime));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        page.setRecords(borrowIPage.getRecords().stream().peek(m -> {
            //计算是否需要罚款，还书时间为空，且状态为未还  则用借书时间-当前查看时间，一天1块
            if (m.getReturnTime() == null && ORDER_TYPE.equals(m.getStatus())) {
                long time = 0;
                try {
                    time = DateUtil.between(df.parse(m.getBorrowTime()), DateUtil.date(), DateUnit.DAY);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (time > 60) {
                    m.setMoney((int) (time - 60));
                    borrowmapper.updateById(m);
                }
            }
            if (ORDER_TYPE.equals(m.getType())) {
                m.setType("预约借书");
            } else if (NORMAL_TYPE.equals(m.getType())) {
                m.setType("普通借书");
            }
        }).collect(Collectors.toList()));
        return page;
    }

    /**
     * 还书或交罚款或取预约书
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public int updateBorrow(String username, String status, Integer money, Integer bookid, Integer id) {
        Borrow books = borrowmapper.selectOne(Wrappers.<Borrow>lambdaQuery()
                .eq(Borrow::getBookid, bookid)
                .eq(Borrow::getId, id)
                .eq(Borrow::getUsername, username));
        if (null != books) {
            Borrow borrow = new Borrow();
            //0已还书
            // 1未还书  传入的status为1时，更新状态为已还书0
            // 2已预约  更新状态为1 未还书
            if (ORDER_TYPE.equals(status)) {
                borrow.setStatus(NORMAL_TYPE);
                Book book = bookMapper.selectOne(Wrappers.<Book>lambdaQuery().eq(Book::getId, bookid));
                if (null != book) {
                    book.setStock(book.getStock() + 1);
                    bookMapper.updateById(book);
                }
                borrow.setReturnTime(DateUtil.now());
            } else if (YUYUE_TYPE.equals(status)) {
                borrow.setStatus(ORDER_TYPE);
                borrow.setBorrowTime(DateUtil.now());
            }
            if (null != money) {
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
    private void order(String mailAccount) {
        MailAccount account = new MailAccount();
        account.setHost("smtp.qq.com");
        account.setPort(25);
        account.setAuth(true);
        account.setFrom("1747749585@qq.com");
        account.setUser("1747749585@qq.com");
        account.setPass("iaeclrbqwiljchdb"); //密码
        MailUtil.send(account, mailAccount, "图书馆", "预约借书成功，请关注您所需要的图书。\n邮件来自图书馆", false);
    }

    @Async("asyncPool")
    public void queryStatus(String userName) {
        int role = userService.getUserRole(userName);
        if (STUDENT_ROLE == role) {
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
                    e.printStackTrace();
                }
                if (time > 60) {
                    User user = new User();
                    user.setUsername(userName);
                    userService.upUser(user);
                }
            }
        }
    }
}
