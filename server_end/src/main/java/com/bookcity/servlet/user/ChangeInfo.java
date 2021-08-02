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
import java.util.HashMap;

public class ChangeInfo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = null;
        Connection conn = null;
        try {
            String token = req.getHeader("token");
            String action = req.getParameter("action");
            out = resp.getWriter();

            HashMap updateMap = new HashMap();
            UserDAO ud = new UserDAO();
            conn = DruidUtil.getConnection();
            User user = TokenManager.find(token);
            System.out.println("find token:"+token);
            System.out.println("find user:"+user);

            switch (action){
                case "base": //修改基本信息:
                    updateMap.put("username", req.getParameter("username"));
                    updateMap.put("context", req.getParameter("context"));
                    break;
                case "pwd": //修改密码
                    String oldPwd = req.getParameter("oldPwd");
                    String newPwd = req.getParameter("newPwd");
                    if(!oldPwd.equals(user.getPassword())){
                        out.write(JSON.toJSONString(new ResponseObj(2, "旧密码错误", null)));
                        return;
                    }
                    updateMap.put("password", newPwd);
                    break;
                case "pay": //充值
                    Double payMoney = Double.parseDouble(req.getParameter("payMoney"));
                    updateMap.put("balance", user.getBalance() + payMoney);
                    user.setBalance(user.getBalance() + payMoney);
                    TokenManager.update(token, user);
                    break;
            }

            ud.updateById(conn, user.getId(), updateMap);
            out.write(JSON.toJSONString(new ResponseObj(0, "成功", null)));
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
}
