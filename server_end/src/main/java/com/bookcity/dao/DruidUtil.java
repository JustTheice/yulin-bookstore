package com.bookcity.dao;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DruidUtil {
    static DataSource dds = null;

    // 获取连接
    public static Connection getConnection() throws Exception{
        if(dds == null){
            InputStream is = DruidUtil.class.getClassLoader().getResourceAsStream("druid.properties");
            Properties prop = new Properties();
            prop.load(is);

            dds = DruidDataSourceFactory.createDataSource(prop);
        }

        return dds.getConnection();
    }

    //关闭连接
    public static void closeResources(Connection conn, Statement stm){

        if(stm != null) {
            try {
                stm.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

//    @Test
//    public void test1(){
//        ArrayList al = new ArrayList();
//        System.out.println(JSON.toJSON(new ResponseObj(5, "服务器繁忙", null)));
//        System.out.println(new ResponseObj(5, "服务器繁忙", null));
//    }
}
