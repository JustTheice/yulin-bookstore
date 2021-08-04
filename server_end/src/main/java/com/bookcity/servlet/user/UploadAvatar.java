package com.bookcity.servlet.user;

import com.alibaba.fastjson.JSON;
import com.bookcity.dao.DruidUtil;
import com.bookcity.dao.UserDAO;
import com.bookcity.entity.User;
import com.bookcity.service.ResponseObj;
import com.bookcity.utils.TokenManager;
import jdk.nashorn.internal.parser.Token;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class UploadAvatar extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //判断用户上传的文件是普通表单还是带文件的表单，如果是普通文件直接返回
        if (!ServletFileUpload.isMultipartContent(req)) {
            return;
        }

        String token = req.getHeader("token");
        PrintWriter out = resp.getWriter();

        //创建文件上传保存的路路径
//        HttpSession session = req.getSession();
//        User user = (User)session.getAttribute("user");

        Connection conn = null;
        try {
            conn = null;
            conn = DruidUtil.getConnection();


            //获取用户信息
            User user = TokenManager.find(token);

            String relativeDirPath = "/upload/avatar/" + user.getId();
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

            //2、ServletFileUpload，上传管理器
            ServletFileUpload upload = new ServletFileUpload(factory);
            //处理乱码问题
//            upload.setHeaderEncoding("utf-8");
            //设置单个文件的最大值
            upload.setFileSizeMax(1024*1024*100);
            //设置总共能够上传文件的大小
            upload.setSizeMax(1024 * 1024 * 100);

            //3、处理上传的文件
            String msg = "";
            //解析前端请求，生成一个FileItem对象（表单中的输入项）
            List<FileItem> fileItems = null;
            fileItems = upload.parseRequest(req);

            for (FileItem fileItem : fileItems) {
                if (fileItem.isFormField()){ //普通表单内容
                    String name = fileItem.getFieldName();
                    String value = fileItem.getString("UTF-8");
                }else { //文件内容
                    //****************************处理文件****************************
                    //拿到文件名字
                    String uploadFileName = fileItem.getName();
                    if (uploadFileName.trim().equals("") || uploadFileName == null) {
                        continue;
                    }
                    //获得上传的文件名
                    String fileName = uploadFileName.substring(uploadFileName.lastIndexOf("/") + 1);

                    //拼接完整文件路径
                    String relativeFilePath = relativeDirPath + '/' + fileName;
                    String realFilePath = realDirPath + '/' + fileName;

                    //****************************处理文件完毕****************************

                    //上传文件
                    File realPathFile = new File(realFilePath);
                    //获得文件上传的流
                    InputStream inputStream = fileItem.getInputStream();
                    //创建一个文件输出流
                    //realPath是真实的文件夹
                    FileOutputStream fos = new FileOutputStream(realPathFile);
                    //创建一个缓冲区
                    byte[] buffer = new byte[1024 * 1024];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }

                    //更新数据库
                    user.setAvatar(relativeFilePath);
                    UserDAO ud = new UserDAO();

                    HashMap updateMap = new HashMap();
                    updateMap.put("avatar", user.getAvatar());
                    ud.updateById(conn, user.getId(), updateMap);

                    //返回新的用户信息
                    out.write(JSON.toJSONString(new ResponseObj(0, "上传成功", user)));

                    //关闭流
                    fos.close();
                    inputStream.close();
                    fileItem.delete();//上传成功，清除临时文件
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write(JSON.toJSONString(new ResponseObj(5, "服务器繁忙", e)));
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
