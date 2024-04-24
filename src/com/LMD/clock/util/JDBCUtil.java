//工具类——数据库连接工具类
package com.LMD.clock.util;


import javax.naming.ConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtil
{
    private static String driver_name; //驱动类
    private static String username;
    private static String password;
    private static String url; //数据库地址
    private static Connection con=null; //数据库连接
    private static final String CONFIG_FILE="src/com/LMD/clock/config/jdbc.properties";

    static
    {   //初始化
        Properties pro=new Properties(); //配置文件解析类
        try
        {
            File config=new File(CONFIG_FILE); //配置文件的文件对象
            if(!config.exists())
            {
                throw new FileNotFoundException("缺少文件："+config.getAbsolutePath()+"，请重新创建该文件。");
            }
            pro.load(new FileInputStream(config)); //加载配置文件
            driver_name=pro.getProperty("driver_name"); //获取指定字段值
            username=pro.getProperty("username","");
            password=pro.getProperty("password","");
            url=pro.getProperty("url");
            if(driver_name==null||url==null)
            {
                throw new ConfigurationException(config.getAbsolutePath()+"文件缺少配置信息，请写入配置信息。");
            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(ConfigurationException e)
        {
            System.out.println("配置文件获取的内容:[driver_name="+driver_name+"],[username="+username+"],[password="+password+"],[url="+url+"]");
            e.printStackTrace();
        }
    }

    /*
    获取数据库连接。根据jdbc.properties中的配置信息返回对应的Connection对象
    @return 已连接数据库的Connection对象
    */
    public static Connection getConnection()
    {
        try
        {
            if(con==null || con.isClosed())
            {
                Class.forName(driver_name); //加载驱动类
                con=DriverManager.getConnection(url,username,password); //根据URL、账号、密码获取数据库连接
            }
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return con;
    }

    /*
    关闭ResultSet
    @param rSet
     */
    public static void close(ResultSet rSet)
    {
        try
        {
            if(rSet!=null)
            {
                rSet.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
    关闭Statement
    @param stmt
     */
    public static void close(Statement stmt)
    {
        try
        {
            if(stmt!=null)
            {
                stmt.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
    关闭PreparedStatment
    @param pStmt
     */
    public static void close(PreparedStatement pStmt)
    {
        try
        {
            if(pStmt!=null)
            {
                pStmt.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
    关闭数据库接口
    @param pStmt
    @param rSet
     */
    public static void close(Statement stmt,PreparedStatement pStmt,ResultSet rSet)
    {
        close(rSet);
        close(pStmt);
        close(stmt);
    }

    /*
    关闭Connection
     */
    public static void closeConnection()
    {
        if(con!=null)
        {
            try
            {
                con.close();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
}