package com.bookcity.dao;

import com.bookcity.entity.Collection;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseDAO<T> {
    private Class<T> clazz;
    QueryRunner runner = new QueryRunner();

    //当子类构造对象时，会自动调用父类的静态代码快，此时的this是子类对象
    {
        //获取父类泛型参数
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) genericSuperclass;

        clazz = (Class<T>) paramType.getActualTypeArguments()[0];

    }



    //增删改
    public int update(Connection conn, String sql, Object ...args){
        int count = 0;
        try {
            count = runner.update(conn, sql, args);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }

    public int updateById(Connection conn, int id, HashMap option){
        String updateStr = "";
        int idx = 0;
        Set entrys = option.entrySet();

        for(Object item:entrys){
            Map.Entry entry = (Map.Entry) item;
            updateStr += entry.getKey() + "=\"" +entry.getValue() + '"';
            idx++;
            if(idx != entrys.size()){
                updateStr += ",";
            }
        }

        String className = clazz.getName().toLowerCase();

        String sql = "update " + className.substring(className.lastIndexOf('.')+1) + " set " + updateStr + " where id=?";
        System.out.println(sql);
        return update(conn, sql, id);
    }

    //查
    public T query(Connection conn, String sql, Object ...args){
        try {
            return runner.query(conn, sql, new BeanHandler<T>(clazz), args);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    //查多个
    public List<T> queryAll(Connection conn, String sql, Object ...args){
        try {
            return runner.query(conn, sql, new BeanListHandler<T>(clazz), args);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


}