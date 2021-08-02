package com.bookcity.servlet.user;

import com.alibaba.fastjson.JSON;
import com.bookcity.entity.User;
import com.bookcity.service.ResponseObj;
import com.bookcity.utils.TokenManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AutoLogin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = null;
        try {
            String token = req.getHeader("token");
            out = resp.getWriter();

            //去tokens仓库中寻找用户信息
            User user = TokenManager.find(token);
            if(user != null){
                out.write(JSON.toJSONString(new ResponseObj(0, "登陆成功", user)));
            } else {
                out.write(JSON.toJSONString(new ResponseObj(2, "没有用户信息", null)));
            }
        } catch (IOException e) {
            e.printStackTrace();
            out.write(JSON.toJSONString(new ResponseObj(5, "服务器繁忙", null)));
        } finally {
            out.close();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
