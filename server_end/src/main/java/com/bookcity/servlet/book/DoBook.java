package com.bookcity.servlet.book;


import com.alibaba.fastjson.JSON;
import com.bookcity.dao.BookDAO;
import com.bookcity.dao.CollectionDAO;
import com.bookcity.dao.DruidUtil;
import com.bookcity.dao.UserDAO;
import com.bookcity.entity.Book;
import com.bookcity.entity.User;
import com.bookcity.service.ResponseObj;
import com.bookcity.utils.FileUtil;
import com.bookcity.utils.TokenManager;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DoBook extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = null;
        Book book = null;
        Connection conn = null;
        BookDAO bd = new BookDAO();

        try {
            String token = req.getHeader("token");
            out = resp.getWriter();
            conn = DruidUtil.getConnection();
            String action = req.getParameter("action");

            User user = TokenManager.find(token);
            if(user.getIsAdmin() == 0){
                out.write(JSON.toJSONString(new ResponseObj(2, "您不是管理员", null)));
                return;
            }

            if(action.equals("add") || action.equals("edit")){

                //初始化文件解析
                String relativeDirPath = "/upload/book";
                String realDirPath = this.getServletContext().getRealPath(relativeDirPath);
                File uploadDir = new File(realDirPath);

                if (!uploadDir.exists()){
                    uploadDir.mkdirs();
                }
                //1、创建DiskFileItemFactory对象，该对象是对上传文件的约束
                DiskFileItemFactory factory = new DiskFileItemFactory();
                //通过这个工厂设置一个缓冲区，当上传的文件大于这个缓冲区的时候，将他放到临时文件中
                factory.setSizeThreshold(1024*1024*10); //缓冲区大小为10M
                factory.setRepository(uploadDir);//临时文件保存的目录，需要一个File
                //解析表单
                List<FileItem> fileItems = null;

                //2、ServletFileUpload，上传管理器
                ServletFileUpload uploadManager = new ServletFileUpload(factory);
                fileItems = uploadManager.parseRequest(req);

                Class clazz = Class.forName("com.bookcity.entity.Book");
                book = (Book) clazz.newInstance();
                for (FileItem fileItem : fileItems) {
                    if (fileItem.isFormField()){ //普通表单内容
                        String name = fileItem.getFieldName();
                        String value = fileItem.getString("UTF-8");

                        Field nameField = clazz.getDeclaredField(name);
                        nameField.setAccessible(true);
                        System.out.println(name + ":" + value);
                        switch (name){
                            case "bookName":
                                nameField.set(book, value);
                                break;
                            case "price":
                                nameField.set(book, Double.parseDouble(value));
                                break;
                            case "surplus":
                                nameField.set(book, Integer.parseInt(value));
                                break;
                            case "id":
                                nameField.set(book, Integer.parseInt(value));
                                break;
                        }
                    }else { //文件内容
                        String uploadFileName = fileItem.getName();
                        if (uploadFileName.trim().equals("") || uploadFileName == null) {
                            continue;
                        }
                        //拼接完整文件路径
                        String fileSuffix = uploadFileName.substring(uploadFileName.lastIndexOf("."));

                        String relativeFilePath = relativeDirPath + '/' + book.getBookName() + fileSuffix;
                        String realFilePath = realDirPath + '/' + book.getBookName() + fileSuffix;
                        //上传文件
                        File realPathFile = new File(realFilePath);
                        InputStream is = fileItem.getInputStream();
                        FileOutputStream fos = new FileOutputStream(realPathFile);
                        byte[] buffer = new byte[1024 * 1024];
                        int len = 0;
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }

                        fos.close();
                        is.close();

                        book.setBookCover(relativeFilePath);
                    }
                }

                //如果书名存在，并且这个书的ID不是自己
                Book existBook = bd.getBookByName(conn, book.getBookName());
                if(existBook!=null && book.getId()!=existBook.getId()){
                    out.write(JSON.toJSONString(new ResponseObj(1, "书名已存在", null)));
                    return;
                }
                //更新数据库
                if(action.equals("add")){
                    Book addedBook = bd.add(conn, book);
                    out.write(JSON.toJSONString(new ResponseObj(0, "添加成功", addedBook)));
                } else {
                    Book updatedBook = bd.update(conn, book);
                    out.write(JSON.toJSONString(new ResponseObj(0, "修改成功", updatedBook)));
                }
            } else if (action.equals("delete")){ //删除
                String bookId = req.getParameter("bookId");
                //删除其封面
                Book deleteBook = bd.getBookById(conn, Integer.parseInt(bookId));
                System.out.println("deleteBook:" + deleteBook);
                if(deleteBook.getBookCover().length() > 0){
                    FileUtil.deleteFile(new File(this.getServletContext().getRealPath(deleteBook.getBookCover())));
                }
                //数据库中删除
                bd.removeById(conn, Integer.parseInt(bookId));
                out.write(JSON.toJSONString(new ResponseObj(0, "删除成功", null)));
                //清除collection表中的收藏信息
                CollectionDAO cd = new CollectionDAO();
                cd.removeAllByBookId(conn, deleteBook.getId());
            }


        } catch (Exception e) {
            e.printStackTrace();
            out.write(JSON.toJSONString(new ResponseObj(5, "服务器繁忙", null)));
        }  finally {
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
