//基于MySQL的数据库接口实现类
package com.LMD.clock.dao;

import com.LMD.clock.pojo.Employee;
import com.LMD.clock.util.JDBCUtil;
import com.LMD.clock.pojo.Administrator;
import com.LMD.clock.pojo.WorkTime;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import java.util.Iterator;

public class DAOMySQLImpl implements DAO
{
    Connection con = null;
    Statement stmt = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    @Override
    public Set<Employee> getAllEmp()
    {
        Set<Employee> set = new HashSet<Employee>(); //全体员工合集
        String sql = "SELECT id,name,code FROM t_emp"; //待执行的SQL语句
        con = JDBCUtil.getConnection();
        try
        {
            stmt = con.createStatement(); //创建执行SQL语句的接口
            rSet = stmt.executeQuery(sql);
            while (rSet.next())
            {
                int id = rSet.getInt("id");
                String name = rSet.getString("name");
                String code = rSet.getString("code");
                Employee emp = new Employee(id, name, code);
                set.add(emp);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            JDBCUtil.close(stmt, pStmt, rSet); //关闭数据库接口对象
        }
        return set;
    }

    @Override
    public Employee getEmp(int id)
    {
        String sql = "SELECT name,code FROM t_emp WHERE id=?"; //待执行的SQL语句
        con = JDBCUtil.getConnection(); //获取数据库连接
        try
        {
            pStmt = con.prepareStatement(sql); //创建执行SQL语句的接口
            pStmt.setInt(1, id); //将SQL语句中第一个?改为员工编号的值
            rSet = pStmt.executeQuery(); //执行SQL语句
            if (rSet.next())
            { //若有查询结果
                String name = rSet.getString("name"); //获取name字段的值
                String code = rSet.getString("code"); //获取code字段的值
                Employee emp = new Employee(id, name, code); //将id,name,code这3个值封装成员工对象
                return emp; //返回此员工对象
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            JDBCUtil.close(stmt, pStmt, rSet); //关闭数据库连接对象
        }
        return null; //无此员工则返回null
    }

    @Override
    public Employee getEmp(String code)
    {
        String sql = "SELECT id,name,code FROM t_emp WHERE code=?";
        con = JDBCUtil.getConnection();
        try
        {
            pStmt = con.prepareStatement(sql);
            pStmt.setString(1, code);
            rSet = pStmt.executeQuery();
            if (rSet.next())
            {
                int id = rSet.getInt("id");
                String name = rSet.getString("name");
                Employee emp = new Employee(id, name, code);
                return emp;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            JDBCUtil.close(stmt, pStmt, rSet);
        }
        return null;
    }

    @Override
    public void addEmp(Employee emp)
    {
        String sql = "INSERT INTO t_emp(name,code) VALUES(?,?)";
        con = JDBCUtil.getConnection();
        try
        {
            pStmt = con.prepareStatement(sql);
            pStmt.setString(1, emp.getName());
            pStmt.setString(2, emp.getCode());
            pStmt.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            JDBCUtil.close(stmt, pStmt, rSet);
        }
    }

    @Override
    public void deleteEmp(Integer id)
    {
        String sql = "DELETE FROM t_emp WHERE id=?";
        con = JDBCUtil.getConnection();
        try
        {
            pStmt = con.prepareStatement(sql);
            pStmt.setInt(1, id);
            pStmt.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            JDBCUtil.close(stmt, pStmt, rSet);
        }
    }

    @Override
    public void deleteEmp(String code)
    {
        String sql = "DELETE FROM t_emp WHERE code=?";
        con = JDBCUtil.getConnection();
        try
        {
            pStmt = con.prepareStatement(sql);
            pStmt.setString(1, code);
            pStmt.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            JDBCUtil.close(stmt, pStmt, rSet);
        }
    }

    @Override
    public WorkTime getWorkTime()
    {
        String sql = "SELECT start,end FROM t_worktime";
        con = JDBCUtil.getConnection();
        try
        {
            stmt = con.createStatement();
            rSet = stmt.executeQuery(sql);
            if (rSet.next())
            {
                String start = rSet.getString("start");
                String end = rSet.getString("end");
                return new WorkTime(start, end);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            JDBCUtil.close(stmt, pStmt, rSet);
        }
        return null;
    }

    @Override
    public void updateWorkTime(WorkTime time)
    {
        String sql = "UPDATE t_worktime SET start=?,end=?";
        con = JDBCUtil.getConnection();
        try
        {
            pStmt = con.prepareStatement(sql);
            pStmt.setString(1, time.getStart());
            pStmt.setString(2, time.getEnd());
            pStmt.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            JDBCUtil.close(stmt, pStmt, rSet);
        }
    }

    @Override
    public void addClockinRecord(int empId, Date now)
    { //日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(now); //将日期转为字符串
        String sql = "INSERT INTO t_clockin_record(emp_id,clockin_time) VALUES(?,?)";
        con = JDBCUtil.getConnection();
        try
        {
            pStmt = con.prepareStatement(sql);
            pStmt.setInt(1, empId);
            pStmt.setString(2, time);
            pStmt.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            JDBCUtil.close(stmt, pStmt, rSet); //关闭数据库接口对象
        }
    }

    @Override
    public void deleteClockinRecord(int empId)
    {
        String sql = "DELETE FROM t_clockin_record WHERE emp_id=?";
        con = JDBCUtil.getConnection();
        try
        {
            pStmt = con.prepareStatement(sql);
            pStmt.setInt(1, empId);
            pStmt.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            JDBCUtil.close(stmt, pStmt, rSet); //关闭数据库接口对象
        }
    }

    @Override
    public boolean adminLogin(Administrator admin)
    {
        String sql = "SELECT id FROM t_admin WHERE username=? AND PASSWORD=?";
        con = JDBCUtil.getConnection();
        try
        {
            pStmt = con.prepareStatement(sql);
            pStmt.setString(1, admin.getUsername());
            pStmt.setString(2, admin.getPassword());
            rSet = pStmt.executeQuery();
            return rSet.next(); //若有查询结构则返回true
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            JDBCUtil.close(stmt, pStmt, rSet);
        }
        return false; //无查询结果则返回false
    }

    @Override
    public String[][] getAllClockinRecord()
    { //保存查询数据的集合。因为不确定行数，所以使用集合而不是二维数组
        HashSet<String[]> set=new HashSet<>();
        String sql="SELECT emp_id,clockin_time FROM t_clockin_record";
        con=JDBCUtil.getConnection();
        try
        {
            stmt=con.createStatement();
            rSet=stmt.executeQuery(sql);
            while(rSet.next())
            {
                String emp_id=rSet.getString("emp_id");
                String clockin_time=rSet.getString("clockin_time");
                //直接将查询的两个结果以字符串数组的形式放到集合中
                set.add(new String[]{emp_id,clockin_time});
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            JDBCUtil.close(stmt,pStmt,rSet);
        }
        if(set.isEmpty())
        { //若集合为空，表示表中没有任何打卡数据
            return null;
        }
        else
        { //创建二维数组作为返回结果，数组行数为集合元素个数，列数为2
            String result[][]=new String[set.size()][2];
            Iterator<String[]> iter=set.iterator(); //创建集合迭代器
            for(int i=0;iter.hasNext();++i)
            { //迭代集合，同时让i递增
                result[i]=iter.next(); //集合中的每一个元素都作为数组的每一行数据
            }
            return result;
        }
    }
}
