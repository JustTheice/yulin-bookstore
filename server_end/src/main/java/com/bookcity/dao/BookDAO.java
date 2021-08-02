package com.bookcity.dao;

import com.bookcity.entity.Book;
import com.bookcity.entity.User;

import java.sql.Connection;
import java.util.List;

public class BookDAO extends BaseDAO<Book>{
    //增
    public int add(Connection coon, Book book){
        String sql = "insert into book (bookName, bookCover, surplus, price) values (?,?,?,?)";
        return update(coon, sql, book.getBookName(),book.getBookCover(),book.getSurplus(),book.getPrice());
    }

    //删
    public int removeById(Connection conn, int id){
        return update(conn, "delete from book where id=?", id);
    }

    //改
    public int update(Connection conn, Book book){
        String sql = "update book set bookName=?, bookCover=?, surplus=?, price=?";
        return update(conn, sql, book.getBookName(),book.getBookCover(),
                book.getSurplus(),book.getPrice());
    }

    //查
    public Book getUserById(Connection conn, int id){
        String sql = "select * from book where id=?";
        return query(conn, sql, id);
    }

    public List<Book> getUsers(Connection conn){
        String sql = "select * from book";
        return queryAll(conn, sql);
    }
}
