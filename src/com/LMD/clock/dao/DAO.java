//数据库接口
package com.LMD.clock.dao;

import com.LMD.clock.pojo.Employee;
import com.LMD.clock.pojo.Administrator;
import com.LMD.clock.pojo.WorkTime;

import java.util.Date;
import java.util.Set;

public interface DAO
{
    /*
    获取所有员工
    @return 所有员工对象集合
    */
    public Set<Employee> getAllEmp();

    /*
    根据员工编号获取员工对象
    @param id 员工编号
    @return 具体员工对象
     */
    public Employee getEmp(int id);

    /*
    根据特征码获取员工对象
    @param code 特征码
    @return 具体员工对象
     */
    public Employee getEmp(String code);

    /*
    添加新员工
    @param e 新员工对象
     */
    public void addEmp(Employee e);

    /*
    删除指定员工
    @param id 员工编号
     */
    public void deleteEmp(Integer id);

    /*
    删除指定员工
    @param code 员工特征码
     */
    public void deleteEmp(String code);

    /*
    获取作息时间
    @return 作息时间对象
     */
    public WorkTime getWorkTime();

    /*
    更新作息时间
    @param time
     */
    public void updateWorkTime(WorkTime time);

    /*
    指定员工添加打卡记录
    @param empId 员工编号
    @param now 打卡日期
     */
    public void addClockinRecord(int empId,Date now);

    /*
    '删除指定员工所有打卡记录
    @param empId 员工编号
     */
    public void deleteClockinRecord(int empId);

    /*
    获取所有员工的打卡记录
    @return 左索引记录员工编号，右索引记录打卡日期
     */
    public String[][] getAllClockinRecord();

    /*
    验证管理员登入
    @param admin 管理员账号
    @return 若账号密码正确，则返回true，否则返回false
     */
    public boolean adminLogin(Administrator admin);
}