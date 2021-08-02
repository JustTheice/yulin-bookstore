package com.bookcity.dao;

import com.bookcity.entity.User;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserDAO extends BaseDAO<User>{
    //增
    public int add(Connection conn, User user){
        return update(conn, "insert into user " +
                "(username,password,avatar,balance,context,isAdmin) values " +
                "(?,?,?,?,?,?)", user.getUsername(),user.getPassword(),user.getAvatar(),
                user.getBalance(), user.getContext(), user.getIsAdmin());
    }

    //删
    public int removeById(Connection conn, int userId){
        return update(conn, "delete from user where id=?", userId);
    }

    //改
    public int update(Connection conn, User user){
        return update(conn, "update user set username=?, password=?, " +
                "avatar=?, balance=?, context=?, isAdmin=? where id=?", user.getUsername(),
                user.getPassword(),user.getAvatar(),user.getBalance(), user.getContext(),
                user.getIsAdmin(), user.getId());
    }



    //查
    public User getUserById(Connection conn, int id){
        String sql = "select * from user where id=?";
        return query(conn, sql, id);
    }

    public List<User> getUsers(Connection conn){
        String sql = "select * from user";
        return queryAll(conn, sql);
    }

    public User getUserByName(Connection conn, String username){
        String sql = "select * from user where username=?";
        return query(conn, sql, username);
    }
}
