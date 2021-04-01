package com.library.manage.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.manage.entity.Book;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMapper extends BaseMapper<Book> {
    @Select("SELECT *,c.name AS categoryName FROM book AS b,category AS c WHERE b.cid=c.id" +
            "title LIKE CONCAT('%',#{keyWords},'%') OR author LIKE CONCAT('%',#{keyWords},'%')")
    List<Book> searchList(String keyWords);

    @Select("<script>SELECT b.*,c.name AS categoryName FROM book AS b,category AS c WHERE b.cid=c.id" +
            "<if test='null != keyWords'>AND (title LIKE CONCAT('%',#{keyWords},'%') OR author LIKE CONCAT('%',#{keyWords},'%')) </if>" +
            "<if test='null != cid'>AND cid = #{cid}</if>" +
            "<if test='null != level'>AND cid BETWEEN 1 AND 4 </if> " +
            "ORDER BY b.id DESC</script>")
    List<Book> getBookList(Page<Book> page, String keyWords, Integer cid, Integer level);
}
