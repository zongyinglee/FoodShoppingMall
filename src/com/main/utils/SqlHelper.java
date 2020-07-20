package com.main.utils;

import java.sql.*;
import java.util.List;


public class SqlHelper {
    //连接对象
    private static Connection con = null;
    //处理SQL语句，替代Statement，防止SQL注入
    private static PreparedStatement ps = null;
    //数据查询结果集
    private static ResultSet rs = null;
    //用于执行SQL存储过程
    private static CallableStatement cs = null;

    public static Connection getCon() {
        return con;
    }

    public static PreparedStatement getPs() {
        return ps;
    }

    public static CallableStatement getCs() {
        return cs;
    }

    //数据库地址
    private static String url="";
    //用户名
    private static String username="";
    //数据库驱动
    private static String driver="";
    //密码
    private static String password="";

    //加载驱动
    static{
        try {
            //从DBInfo文件中读取数据库配置信息
            url= DBInfo.url;
            username= DBInfo.username;
            driver= DBInfo.driver;
            password= Base64Util.decode(DBInfo.password);
            //注册数据库驱动
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Connection getConnection(){
        try {
            con = DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return con;
    }


    public static ResultSet doQuery(String sql,String parameters[]) {
        try {
            //获取数据库连接对象
            con = getConnection();
            //准备sql语句
            ps = con.prepareStatement(sql);
            //为sql语句的占位符?赋值
            if (parameters != null && !parameters.equals("")) {
                for(int i=0;i<parameters.length;i++){
                    ps.setString(i+1,parameters[i]);
                }
            }
            //执行sql语句
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <T> List<T> doListQuery(String sql, String parameters[], Class<T> bean) {
        try {
            //获取数据库连接对象
            con = getConnection();
            //准备sql语句
            ps = con.prepareStatement(sql);
            //为sql语句的占位符?赋值
            if (parameters != null && !parameters.equals("")) {
                for(int i=0;i<parameters.length;i++){
                    ps.setString(i+1,parameters[i]);
                }
            }
            //执行sql语句
            rs = ps.executeQuery();
            //封装到Bean集合中
            List<T> list = BeanUtil.getList(bean,rs);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            //关闭数据库连接
            close();
        }
    }


    public static <T> T doObjQuery(String sql,String parameters[],Class<T> bean) {
        try {
            //获取数据库连接对象
            con = getConnection();
            //准备sql语句
            ps = con.prepareStatement(sql);
            //为sql语句的占位符?赋值
            if (parameters != null && !parameters.equals("")) {
                for(int i=0;i<parameters.length;i++){
                    ps.setString(i+1,parameters[i]);
                }
            }
            //执行sql语句
            rs = ps.executeQuery();
            //封装到Bean中
            T t = BeanUtil.getObj(bean,rs);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            //关闭数据库连接
            close();
        }
    }


    public static int doUpdate(String sql,String parameters[]) {
        try {
            //获取数据库连接对象
            con = getConnection();
            //准备sql语句
            ps = con.prepareStatement(sql);
            //为sql语句的占位符?赋值
            if (parameters != null && !parameters.equals("")) {
                for(int i=0;i<parameters.length;i++){
                    ps.setString(i+1,parameters[i]);
                }
            }
            //执行sql语句
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            close();
        }
    }

    public static void close() {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            rs = null;
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ps = null;
        }
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            con = null;
        }
    }
}
