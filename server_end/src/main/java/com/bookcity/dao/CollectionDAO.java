package com.bookcity.dao;

import com.bookcity.entity.Collection;

import java.sql.Connection;
import java.util.List;

public class CollectionDAO extends BaseDAO<Collection> {
    //增
    public int add(Connection conn, Collection collection) {
        String sql = "insert into collection (userId, bookId) values (?,?)";
        return update(conn, sql, collection.getUserId(), collection.getBookId());
    }

    //删
    public int removeById(Connection conn, int id){
        return update(conn, "delete from collection where id=?", id);
    }

    public int removeAllByUserId(Connection conn, int userId){
        return update(conn, "delete from collection where userId=?", userId);
    }

    public int removeAllByBookId(Connection conn, int bookId){
        return update(conn, "delete from collection where bookId=?", bookId);
    }

    public int removeOne(Connection conn, int userId, int bookId){
        return update(conn, "delete from collection where userId=? and bookId=?", userId, bookId);
    }

    //改

    //查
    public List<Collection> getCollections(Connection conn, int userId){
        return queryAll(conn, "select * from collection where userId=?", userId);
    }

    public boolean hasCollectied(Connection conn, int userId, int bookId){
        Collection ret = query(conn, "select id from collection where (userId=? and bookId=?)", userId, bookId);
        return ret!=null ? true : false;
    }
}
