package com.bookcity.servlet.user;

import com.alibaba.fastjson.JSON;
import com.bookcity.dao.BookDAO;
import com.bookcity.dao.CollectionDAO;
import com.bookcity.dao.DruidUtil;
import com.bookcity.entity.Book;
import com.bookcity.entity.Collection;
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
import java.util.ArrayList;
import java.util.List;

public class GetCollections extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = null;
        Connection conn = null;
        try {
            String token = req.getHeader("token");
            out = resp.getWriter();

            User user = TokenManager.find(token);

            //查询用户的收藏列表
            conn = DruidUtil.getConnection();
            CollectionDAO cd = new CollectionDAO();
            BookDAO bd = new BookDAO();
            List<Collection> collections = cd.getCollections(conn, user.getId());
            ArrayList<Book> bookList = new ArrayList<>();

            for(Collection item:collections){
                Book bookItem = bd.getBookById(conn, item.getBookId());
                bookList.add(bookItem);
            }
            out.write(JSON.toJSONString(new ResponseObj(0, "查找成功", bookList)));
        } catch (Exception e) {
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
