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


    //改

    //查
    public List<Collection> getCollections(Connection conn, int userId){
        return queryAll(conn, "select * from collection where userId=?", userId);
    }
}
