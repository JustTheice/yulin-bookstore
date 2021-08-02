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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

public class Close extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = null;
        Connection conn = null;
        try {
            String token = req.getHeader("token");
            out = resp.getWriter();

            User user = TokenManager.find(token);
            //注销用户
            conn = DruidUtil.getConnection();
            UserDAO ud = new UserDAO();
            ud.removeById(conn, user.getId());
            TokenManager.delete(token);

            out.write(JSON.toJSONString(new ResponseObj(0, "注销成功", null)));
        } catch (Exception e) {
            e.printStackTrace();
            out.write(JSON.toJSONString(new ResponseObj(5, "服务器繁忙", null)));
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
