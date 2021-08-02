package com.bookcity.servlet.user;

import com.alibaba.fastjson.JSON;
import com.bookcity.dao.DruidUtil;
import com.bookcity.dao.UserDAO;
import com.bookcity.entity.User;
import com.bookcity.service.ResponseObj;
import com.bookcity.utils.TokenManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class Login extends HttpServlet {
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

            User user;
            String token = req.getHeader("token");//同步token

            if((user = TokenManager.find(token)) == null) { //如果没有token，从数据库中查找
                System.out.println("无token");
                String username = req.getParameter("username");
                String password = req.getParameter("password");

                conn = DruidUtil.getConnection();
                UserDAO ud = new UserDAO();

                user = ud.getUserByName(conn, username);
                if(user == null){
                    out.write(JSON.toJSONString(new ResponseObj(2, "用户名不存在", null)));
                    return;
                }
                if(!password.equals(user.getPassword())){
                    out.write(JSON.toJSONString(new ResponseObj(1, "密码错误", null)));
                    return;
                }

                // 新建token
                token = TokenManager.createToken(user);
            }

            System.out.println(user);

            HashMap resMap = new HashMap();
            resMap.put("user", user);
            resMap.put("token", token);

            out.write(JSON.toJSONString(new ResponseObj(0, "登陆成功", resMap)));
        } catch (Exception e) {
            out.write(JSON.toJSONString(new ResponseObj(5, "服务器繁忙", null)));
            e.printStackTrace();
        } finally {
            try {
                if(conn != null)
                    conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            out.close();
        }


    }
}
