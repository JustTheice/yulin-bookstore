package com.bookcity.servlet.book;

import com.alibaba.fastjson.JSON;
import com.bookcity.dao.BookDAO;
import com.bookcity.dao.DruidUtil;
import com.bookcity.entity.Book;
import com.bookcity.service.ResponseObj;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *      获取书籍信息
 */
public class GetBooks extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = null;
        Connection conn = null;
        try {
            out = resp.getWriter();

            conn = DruidUtil.getConnection();
            BookDAO bd = new BookDAO();
            List<Book> books = bd.getBooks(conn);

            out.write(JSON.toJSONString(new ResponseObj(0, "查找成功", books)));
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
