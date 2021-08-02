package com.bookcity.servlet.user;

import com.alibaba.fastjson.JSON;
import com.bookcity.service.ResponseObj;
import com.bookcity.utils.TokenManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Logout extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = null;
        try {
            out = resp.getWriter();

            String token = req.getHeader("token");
            TokenManager.delete(token);

            out.write(JSON.toJSONString(new ResponseObj(0, "退出成功", null)));
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
