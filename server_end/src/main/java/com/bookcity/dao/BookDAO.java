package com.bookcity.dao;

import com.bookcity.entity.Book;
import com.bookcity.entity.User;

import java.sql.Connection;
import java.util.List;

public class BookDAO extends BaseDAO<Book>{
    //增
    public Book add(Connection coon, Book book){
        String sql = "insert into book (bookName, bookCover, surplus, price) values (?,?,?,?)";
        update(coon, sql, book.getBookName(), book.getBookCover(), book.getSurplus(), book.getPrice());
        return getBookByName(coon, book.getBookName());
    }

    //删
    public int removeById(Connection conn, int id){
        return update(conn, "delete from book where id=?", id);
    }

    //改
    public Book update(Connection conn, Book book){
        String sql = "update book set bookName=?, bookCover=?, surplus=?, price=? where id=?";
        update(conn, sql, book.getBookName(),book.getBookCover(),
                book.getSurplus(),book.getPrice(),book.getId());
        return getBookById(conn, book.getId());
    }

    //查
    public Book getBookById(Connection conn, int id){
        String sql = "select * from book where id=?";
        return query(conn, sql, id);
    }
    public Book getBookByName(Connection conn, String bookName){
        String sql = "select * from book where bookName=?";
        return query(conn, sql, bookName);
    }

//    public List<Book> getBooksById(Connection conn, int id){
//        String sql = "select * from book where id=?";
//        return queryAll(conn, sql, id);
//    }

    public List<Book> getBooks(Connection conn){
        String sql = "select * from book";
        return queryAll(conn, sql);
    }
}
