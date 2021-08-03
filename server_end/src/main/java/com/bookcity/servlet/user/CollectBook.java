package com.bookcity.servlet.user;

import com.alibaba.fastjson.JSON;
import com.bookcity.dao.BookDAO;
import com.bookcity.dao.CollectionDAO;
import com.bookcity.dao.DruidUtil;
import com.bookcity.dao.UserDAO;
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
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectBook extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = null;
        Connection conn = null;
        try {
            String action = req.getParameter("action");
            String bookId = req.getParameter("bookId");
            String token = req.getHeader("token");
            out = resp.getWriter();
            User user = TokenManager.find(token);

            conn = DruidUtil.getConnection();
            CollectionDAO cd = new CollectionDAO();
            if(action.equals("add")){ //添加收藏


                Class clazz = Class.forName("com.bookcity.entity.Collection");
                Field fUserId = clazz.getDeclaredField("userId");
                Field fBookId = clazz.getDeclaredField("bookId");
                Collection collection = (Collection)clazz.newInstance();
                fUserId.setAccessible(true);
                fBookId.setAccessible(true);
                fUserId.set(collection, user.getId());
                fBookId.set(collection, Integer.parseInt(bookId));

                //判断是否收藏过
                if(cd.hasCollectied(conn, user.getId(), Integer.parseInt(bookId))){
                    out.write(JSON.toJSONString(new ResponseObj(2, "您已收藏", null)));
                    return;
                }
                cd.add(conn, collection);
                out.write(JSON.toJSONString(new ResponseObj(0, "添加成功", null)));
            } else if(action.equals("remove")){ //移除收藏
                if(bookId == null){ //移除全部
                    cd.removeAllByUserId(conn, user.getId());
                    out.write(JSON.toJSONString(new ResponseObj(0, "移除成功", null)));
                } else {//移除单个
                    cd.removeOne(conn, user.getId(), Integer.parseInt(bookId));
                    out.write(JSON.toJSONString(new ResponseObj(0, "移除成功", null)));
                }
            } else if(action.equals("buy")){ //购买
                UserDAO ud = new UserDAO();
                BookDAO bd = new BookDAO();
                conn.setAutoCommit(false);
                Double restBalance;
                Double totalPrice = 0.0;

                if(bookId == null){//购买全部
                    //查询数据库计算余额
                    List<Collection> collections = cd.getCollections(conn, user.getId());
                    ArrayList<Book> books = new ArrayList<>();
                    for(Collection collectItem : collections){
                        Book book = bd.getBookById(conn, collectItem.getBookId());
                        //判断图书数量是否足够
                        if(book.getSurplus() <= 0){
                            out.write(JSON.toJSONString(new ResponseObj(4, "《" + book.getBookName() + "》数量不够了", null)));
                            return;
                        }
                        totalPrice += book.getPrice();
                        books.add(book);
                    }

                    restBalance = user.getBalance() - totalPrice;
                    if(restBalance < 0){
                        out.write(JSON.toJSONString(new ResponseObj(3, "余额不足", null)));
                        return;
                    }

                    //余额足够，进行操作逻辑
                    cd.removeAllByUserId(conn, user.getId());
                    HashMap updateMap = new HashMap();
                    updateMap.put("balance", restBalance);
                    user.setBalance(restBalance);
//                    TokenManager.update(token, );
                    ud.updateById(conn, user.getId(), updateMap);
                    for(Book book : books){
                        book.setSurplus(book.getSurplus() - 1);
                        bd.update(conn, book);
                    }
                    conn.commit();
                } else { //购买单个
                    Book book = bd.getBookById(conn, Integer.parseInt(bookId));
                    if(book.getSurplus() <= 0){
                        out.write(JSON.toJSONString(new ResponseObj(4, "《" + book.getBookName() + "》数量不够了", null)));
                        return;
                    }
                    restBalance = user.getBalance() - book.getPrice();
                    if(restBalance < 0){
                        out.write(JSON.toJSONString(new ResponseObj(3, "余额不足", null)));
                        return;
                    }
                    //余额足够，进行更新逻辑
                    cd.removeOne(conn, user.getId(), Integer.parseInt(bookId));
                    user.setBalance(restBalance);
                    HashMap updateMap = new HashMap();
                    updateMap.put("balance", restBalance);
                    ud.updateById(conn, user.getId(), updateMap);
                    book.setSurplus(book.getSurplus() - 1);
                    bd.update(conn, book);
                    System.out.println(TokenManager.find(token));
                    conn.commit();
                }

                out.write(JSON.toJSONString(new ResponseObj(0, "购买成功", user)));
            }

        } catch (Exception e) {
            out.write(JSON.toJSONString(new ResponseObj(5, "服务器繁忙", null)));
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
