//数据库接口工厂类
package com.mr.clock.dao;

public class DAOFactory
{
    public static DAO getDAO()
    {
        return new DAOMySQLImpl(); //返回基于MySQL的实现类对象
    }
}