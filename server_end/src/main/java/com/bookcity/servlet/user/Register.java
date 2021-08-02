package com.bookcity.servlet.user;

import com.alibaba.fastjson.JSON;
import com.bookcity.dao.DruidUtil;
import com.bookcity.dao.UserDAO;
import com.bookcity.entity.User;
import com.bookcity.service.ResponseObj;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public class Register extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = null;
        Connection conn = null;

        try {
            out = resp.getWriter();

            Map<String, String[]> parameterMap = req.getParameterMap();

            conn = DruidUtil.getConnection();
            UserDAO ud = new UserDAO();

            //判断用户名是否存在
            if(ud.getUserByName(conn, parameterMap.get("username")[0]) != null){
                out.write(JSON.toJSONString(new ResponseObj(1, "用户名已存在", null)));
                return;
            }
            //创造user实例
            Class<User> clazz = (Class<User>) Class.forName("com.bookcity.entity.User");
            User user = clazz.newInstance();

            Set<Map.Entry<String, String[]>> entries = parameterMap.entrySet();
            for (Map.Entry<String,String[]> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue()[0];
                Field field = clazz.getDeclaredField(key);
                field.setAccessible(true);
                field.set(user, value);
            }

            ud.add(conn, user);
            out.write(JSON.toJSONString(new ResponseObj(0, "注册成功", user)));
        } catch (Exception e) {
            System.out.println(11111);
            out.write(JSON.toJSONString(new ResponseObj(5, "服务器繁忙", null)));
            e.printStackTrace();
        } finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            out.close();
        }

    }
}
