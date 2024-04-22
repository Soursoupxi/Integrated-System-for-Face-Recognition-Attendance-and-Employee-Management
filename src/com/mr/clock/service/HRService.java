//服务类——人事服务
package com.mr.clock.service;

import com.mr.clock.dao.DAO;
import com.mr.clock.dao.DAOFactory;
import com.mr.clock.pojo.Administrator;
import com.mr.clock.pojo.Employee;
import com.mr.clock.pojo.WorkTime;
import com.mr.clock.session.Session;
import com.mr.clock.util.DateTimeUtil;

import java.awt.image.BufferedImage;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class HRService
{
    private static final String CLOCK_IN="I"; //正常上班打卡标记
    private static final String CLOCK_OUT="O"; //正常下班打卡标记
    private static final String LATE="L"; //迟到标记
    private static final String LEFT_EARLY="E"; //早退标记
    private static final String ABSENT="A"; //缺席标记
    private static DAO dao= DAOFactory.getDAO(); //数据库接口

    /*
    加载所有员工
     */
    public static void loadAllEmp()
    {
        Session.EMP_SET.clear(); //全局会话先清空所有员工
        Session.EMP_SET.addAll(dao.getAllEmp()); //重新从数据库中加载所有员工的对象集合
    }

    /*
    管理员用户登入
    @param username
    @param password
    @return 是否成功登入
     */
    public static boolean userLogin(String username,String password)
    {
        Administrator admin=new Administrator(username,password); //创建管理员对象
        if(dao.adminLogin(admin))
        { //若数据库中可以查到相关管理员用户名和密码
            Session.admin=admin; //将登入的管理员设为全局会话中的管理员
            return true; //登入失败
        }
        else
        {
            return false; //登入失败
        }
    }

    /*
    添加新员工
    @param name 员工姓名
    @param face 员工人脸照片
    @return 添加成功的员工对象
     */
    public static Employee addEmp(String name,BufferedImage face)
    {
        String code=UUID.randomUUID().toString().replace("-",""); //通过UUID随机生成该员工的特征码
        Employee emp=new Employee(null,name,code); //创建新员工对象
        dao.addEmp(emp); //向数据库插入该员工数据
        emp=dao.getEmp(code); //重新获取已分配员工编号的员工对象
        Session.EMP_SET.add(emp); //新员工加入全局员工列表中
        return emp; //返回新员工对象
    }

    /*
    添加新员工
    @param id 员工编号
    @param name 员工姓名
    @param face 员工人脸照片
    @return 添加成功的员工对象
     */
    public static Employee addEmp(Integer id,String name,BufferedImage face)
    {
        String code=UUID.randomUUID().toString().replace("-",""); //通过UUID随机生成该员工的特征码
        Employee emp=new Employee(id,name,code); //创建新员工对象
        dao.addEmp(emp); //向数据库插入该员工对象
        emp=dao.getEmp(code); //重新获取已分配员工编号的员工对象
        Session.EMP_SET.add(emp);
        return emp;
    }

    /*
    删除员工
    @param id 员工编号
     */
    public static void deleteEmp(int id)
    {
        Employee emp=getEmp(id); //根据编号获取该员工对象
        if(emp!=null)
        {
            Session.EMP_SET.remove(emp); //从员工列表中删除
        }
        dao.deleteEmp(id); //在数据库中删除该员工信息
        dao.deleteClockinRecord(id); //在数据库中删除该员工所有打卡记录
        ImageService.deleteFaceImage(emp.getCode()); //删除该员工人脸照片文件
        Session.FACE_FEATURE_MAP.remove(emp.getCode()); //删除该员工人脸特征
        Session.RECORD_MAP.remove(emp.getId()); //删除该员工打卡记录
    }

    /*
    删除员工
    @param code 员工特征码
     */
    public static void deleteEmp(String code)
    {
        Employee emp=getEmp(code);
        deleteEmp(emp.getId());
    }

    /*
    获取员工对象
    @param id 员工编号
    @return
     */
    public static Employee getEmp(int id)
    {
        for(Employee emp:Session.EMP_SET)
        { //遍历所有员工
            if(emp.getId().equals(id))
            { //若编号一致
                return emp; //返回该员工
            }
        }
        return null;
    }

    /*
    获取员工对象
    @param code 员工特征码
    @return
     */
    public static Employee getEmp(String code)
    {
        for(Employee emp:Session.EMP_SET)
        {
            if(emp.getCode().equals(code))
            {
                return emp;
            }
        }
        return null;
    }

    /*
    添加打卡记录
    @param emp 员工对象
     */
    public static void addClockinRecord(Employee emp)
    {
        Date now=new Date(); //当前时间
        dao.addClockinRecord(emp.getId(),now); //为该员工添加当前时间的打卡记录
        if(!Session.RECORD_MAP.containsKey(emp.getId()))
        { //若全局会话中没有该员工的打卡记录
            Session.RECORD_MAP.put(emp.getId(),new HashSet<>()); //为该员工添加空记录
        }
        Session.RECORD_MAP.get(emp.getId()).add(now); //在该员工的打卡记录中添加新的打卡时间
    }

    /*
    加载所有人的打卡记录
     */
    public static void loadAllClockinRecord()
    {
        String[][] record=dao.getAllClockinRecord(); //从数据库中获取打卡记录数据
        if(record==null)
        {
            System.err.println("表中无打卡数据");
            return;
        }
        for(int i=0,length=record.length;i<length;++i)
        { //遍历所有打卡记录
            String[] rowR=record[i]; //获取第i行记录
            Integer id=Integer.valueOf(rowR[0]); //获取员工编号
            if(!Session.RECORD_MAP.containsKey(id))
            { //若全局会话中没有该员工的打卡记录
                Session.RECORD_MAP.put(id,new HashSet<>()); //为该员工添加空记录
            }
            try
            {
                Date recordDate=DateTimeUtil.dateOf(rowR[1]); //日期时间字符串转为日期对象
                Session.RECORD_MAP.get(id).add(recordDate); //在该员工的打卡记录中添加新的打卡时间
            }
            catch(ParseException e)
            {
                e.printStackTrace();
            }
        }
    }

    /*
    加载作息时间
     */
    public static void loadWorkTime()
    {
        if(dao.getWorkTime()!=null)
        {
            Session.worktime=dao.getWorkTime(); //从数据库中获取作息时间，并赋值给全局会话
        }
    }

    /*
    更新作息时间
    @param time 新的作息时间
     */
    public static void updateWorkTime(WorkTime time)
    {
        dao.updateWorkTime(time); //更新数据库中的作息时间
        Session.worktime=time; //更新全局会话中的作息时间
    }

    /*
    获取某一天所有员工的打卡数据，键为员工对象，值为员工考勤记录。打卡记录用拼接起来的标识符描述打卡情况
    @param year,month,day
    @return 键值对
     */
    private static Map<Employee,String> getOneDayRecordData(int year,int month,int day)
    {
        Map<Employee,String> record=new HashMap<>(); //键为员工对象，值为考勤记录
        //各时间点对象
        Date zeroTime=null,noonTime=null,lastTime=null,workTime=null,closingTime=null;
        try
        {
            zeroTime=DateTimeUtil.dateOf(year,month,day,"00:00:00"); //零时
            noonTime=DateTimeUtil.dateOf(year,month,day,"12:00:00"); //正午
            lastTime=DateTimeUtil.dateOf(year,month,day,"23:59:59"); //一天中最后一秒
            WorkTime wt=Session.worktime; //获取当天作息时间
            workTime=DateTimeUtil.dateOf(year,month,day,wt.getStart()); //上班时间
            closingTime=DateTimeUtil.dateOf(year,month,day,wt.getEnd()); //下班时间
        }
        catch(ParseException e1)
        {
            e1.printStackTrace();
        }

        for(Employee emp:Session.EMP_SET)
        { //遍历所有员工
            String report=""; //该员工的考勤记录，初始为空
            if(Session.RECORD_MAP.containsKey(emp.getId()))
            { //若所有打卡记录可以找到该员工
                boolean isAbsent=true; //默认为缺勤状态
                //获取该员工的所有打卡记录
                Set<Date> clockinSet=Session.RECORD_MAP.get(emp.getId());
                for(Date r:clockinSet)
                { //若员工再此时期内有打卡记录
                    if(r.after(zeroTime) && r.before(lastTime))
                    {
                        isAbsent=false; //不缺勤
                        if(r.before(workTime) || r.equals(workTime))
                        { //上班前打卡
                            report+=CLOCK_IN; //追加上班正常打卡标记
                        }
                        if(r.after(closingTime) || r.equals(closingTime))
                        { //下班后打卡
                            report+=CLOCK_OUT; //追加下班正常打卡标记
                        }
                        if(r.after(workTime) && r.before(noonTime))
                        { //上班后，正午前打卡
                            report+=LATE; //追加迟到标记
                        }
                        if(r.after(noonTime) && r.before(closingTime))
                        { //正午后，下班前打卡
                            report+=LEFT_EARLY; //追加早退标记
                        }
                    }
                }
                if(isAbsent)
                { //该员工在此日期没有打卡记录
                    report=ABSENT; //指定为缺勤标记
                }
            }
            else
            { //在所有打卡记录里都没有该员工
                report=ABSENT; //指定为缺勤标记
            }
            record.put(emp,report); //保存该员工的考勤记录
        }
        return record;
    }

    /*
    获得日报报表
    @param year,month,day
    @return 完整的日报字符串
     */
    public static String getDayReport(int year,int month,int day)
    {
        Set<String> lateSet=new HashSet<>(); //迟到名单
        Set<String> leftSet=new HashSet<>(); //早退名单
        Set<String> absentSet=new HashSet<>(); //缺勤名单
        Set<String> noClockinSet=new HashSet<>(); //上班未打卡名单
        Set<String> noClockoutSet=new HashSet<>(); //下班未打卡名单
        //获取这一天所有员工的打卡数据
        Map<Employee,String> record=HRService.getOneDayRecordData(year,month,day);
        for(Employee emp:record.keySet())
        { //遍历所有员工
            String oneRecord=record.get(emp); //获取该员工的考勤记录
            if(oneRecord.contains(ABSENT))
            { //若有缺勤标记
                absentSet.add(emp.getName());
            }
            else
            {
                if (oneRecord.contains(LATE) && !oneRecord.contains(CLOCK_IN))
                { //若有迟到标记，并且没有正常上班打卡标记
                    lateSet.add(emp.getName());
                }
                if (oneRecord.contains(LEFT_EARLY) && !oneRecord.contains(CLOCK_OUT))
                { //若有早退标记，并且没有正常下班打卡标记
                    leftSet.add(emp.getName());
                }
                if (!oneRecord.contains(CLOCK_IN) && !oneRecord.contains(LATE))
                { //若有正常下班打卡标记或早退标记，但既没有正常上班打卡标记也没有迟到标记
                    noClockinSet.add(emp.getName());
                }
                if (!oneRecord.contains(CLOCK_OUT) && !oneRecord.contains(LEFT_EARLY))
                { //若有正常上班打卡标记或迟到标记，但既没有正常下班打卡标记也没有早退标记
                    noClockoutSet.add(emp.getName());
                }
            }
        }

        StringBuilder report=new StringBuilder(); //报表字符串
        int count=Session.EMP_SET.size(); //获取员工人数
        //拼接报表内容
        report.append("---- "+year+"年"+month+"月"+day+"日 ----\n");
        report.append("应到人数："+count+"\n");
        report.append("缺勤人数："+absentSet.size()+"\n");
        report.append("缺勤名单：");
        if(absentSet.isEmpty())
        {
            report.append("（空）\n");
        }
        else
        { //创建缺勤名单的遍历对象
            Iterator<String> iter=absentSet.iterator();
            while(iter.hasNext())
            { //遍历名单
                report.append(iter.next()+" "); //在报表上添加缺席员工的名字
            }
            report.append("\n");
        }

        report.append("迟到人数："+lateSet.size()+"\n");
        report.append("迟到名单：");
        {
            if(lateSet.isEmpty())
            {
                report.append("（空）\n");
            }
            else
            { //创建迟到名单的遍历对象
                Iterator<String> iter=lateSet.iterator();
                while(iter.hasNext())
                { //遍历名单
                    report.append(iter.next()+" "); //在报表中添加迟到员工的名字
                }
                report.append("\n");
            }
        }

        report.append("早退人数："+leftSet.size()+"\n");
        report.append("早退名单：");
        {
            if(leftSet.isEmpty())
            {
                report.append("（空）\n");
            }
            else
            {
                Iterator<String> iter=leftSet.iterator(); //创建早退名单的遍历对象
                while(iter.hasNext())
                { //遍历名单
                    report.append(iter.next()+" "); //在报表中添加早退员工的名字
                }
                report.append("\n");
            }
        }

        report.append("上班未打卡人数："+noClockinSet.size()+"\n");
        report.append("上班未打卡名单：");
        {
            if(noClockinSet.isEmpty())
            {
                report.append("（空）\n");
            }
            else
            {
                Iterator<String> iter=noClockinSet.iterator();
                while(iter.hasNext())
                {
                    report.append(iter.next()+" ");
                }
                report.append("\n");
            }
        }

        report.append("下班未打卡人数："+noClockoutSet.size()+"\n");
        report.append("下班未打卡名单：");
        {
            if(noClockoutSet.isEmpty())
            {
                report.append("（空）\n");
            }
            else
            {
                Iterator<String> iter=noClockoutSet.iterator();
                while(iter.hasNext())
                {
                    report.append(iter.next()+" ");
                }
                report.append("\n");
            }
        }

        return report.toString();
    }

    /*
    获取月报数据。二维数组第1列为员工编号，第2列为员工姓名，第3列值最后一列为year年month月1日至最后一日的打卡情况
    @param year,month
    @return
     */
    public static String[][] getMonthReport(int year,int month)
    {
        int lastDay=DateTimeUtil.getLastDay(year,month); //此月最大天数
        int count=Session.EMP_SET.size(); //员工总人数
        //报表数据键值对，键为员工对象，值为该员工从第一天至最后一天的考勤标记列表
        Map<Employee,ArrayList<String>> reportCollection=new HashMap<>();
        for(int day=1;day<=lastDay;++day)
        {
            Map<Employee,String> recordOneDay=HRService.getOneDayRecordData(year,month,day);
            for(Employee emp:recordOneDay.keySet())
            { //遍历所有员工
                if(!reportCollection.containsKey(emp))
                { //为该员工添加空列表，列表长度为最大天数
                    reportCollection.put(emp,new ArrayList<>(lastDay));
                }
                //向该员工的打卡记录列表中添加这一天的考勤标记
                reportCollection.get(emp).add(recordOneDay.get(emp));
            }
        }

        //报表数据数组，行数为员工人数，列数为最大天数+2(因为第1列是员工编号，第2列是员工姓名)
        String report[][]=new String[count][lastDay+2];
        int row=0;
        //遍历报表数据键值对中的每一位员工
        for(Employee emp:reportCollection.keySet())
        {
            report[row][0]= String.valueOf(emp.getId()); //第1列为员工编号
            report[row][1]=emp.getName(); //第2列为员工姓名
            ArrayList<String> list=reportCollection.get(emp); //获取该员工考勤标记列表
            for(int i=0,length=list.size();i<length;++i)
            { //遍历每一个考勤标记
                report[row][i+2]=""; //从第2列开始，默认值为空字符串
                String record=list.get(i); //获取此列的考勤标记
                if(record.contains(ABSENT))
                { //若存在缺勤标记
                    report[row][i+2]="[缺勤]"; //该列标记为缺勤
                }
                //如果存在正常上班打卡记录和正常下班打卡记录
                else if(record.contains(CLOCK_IN) && record.contains(CLOCK_OUT))
                { //若是全勤
                    report[row][i+2]=""; //该列标记为空字符串
                }
                else
                {
                    if(record.contains(LATE) && !record.contains(CLOCK_IN))
                    { //若有迟到记录，无正常上班打卡记录
                        report[row][i+2]+="[迟到]";
                    }
                    if(record.contains(LEFT_EARLY) && !record.contains(CLOCK_OUT))
                    { //若有早退记录，无正常下班打卡记录
                        report[row][i+2]+="[早退]";
                    }
                    if(!record.contains(LATE) && !record.contains(CLOCK_IN))
                    {
                        report[row][i+2]+="[上班未打卡]";
                    }
                    if(!record.contains(LEFT_EARLY) && !record.contains(CLOCK_OUT))
                    {
                        report[row][i+2]+="[下班未打卡]";
                    }
                }
            }
            ++row; //二维数组的行索引递增
        }
        return report;
    }

    /*
    获取年报数据。二维数组第一列为员工名称，第二列值最后一列为year年1月至12月的各种打卡情况的次数
    @param year
    @return
    */
    public static String[][] getYearReport(int year)
    {
        String[][] monthReport,yearReport;
        int row=0; //row表示第row位员工
        int count=Session.EMP_SET.size();
        yearReport=new String[count][12+2]; //要插入员工编号与员工姓名，所以列数加2
        HashMap<String,Integer> tagCounts=new HashMap<>(); //用于记录各种标签(迟到、早退等等)的次数

        for(Employee emp:Session.EMP_SET)
        { //遍历所有员工
            yearReport[row][0]= String.valueOf(emp.getId());
            yearReport[row++][1]=emp.getName();
        }
        for(int month=1;month<=12;++month)
        {
            monthReport=getMonthReport(year,month); //调用获取月报数据方法，用于统计年报中每月的各种打卡情况的数量

            for(row=0;row<count;++row)
            {
                yearReport[row][month+1]=""; //去除初始的"null"内容
                for(int day=2;day<monthReport[0].length;++day)
                {
                    //在正则表达式中，使用方括号作为普通字符，需要进行转义，即写成\[和\]。同样地，为了在正则表达式中使用反斜杠\作为普通字符，也需要进行转义，即写成\\
                    String[] tags=monthReport[row][day].split("\\]\\[");
                    for(String tag:tags)
                    {
                        tag=tag.replace("[","").replace("]",""); //去除首尾的方括号
                        if(!tag.isEmpty())
                        { //getOrDefault(tag, 0)用于获取tag对应的值，如果 tag在tagCounts中不存在，那么返回默认值 0，这样避免了空指针异常
                            tagCounts.put(tag,tagCounts.getOrDefault(tag,0)+1);
                        }
                    }
                }
                StringBuilder monthInfo=new StringBuilder();
                for(String tag:tagCounts.keySet())
                {
                    int tagNum=tagCounts.get(tag);
                    monthInfo.append(tag).append("：").append(tagNum).append("次；");
                }
                yearReport[row][month+1]= String.valueOf(monthInfo);
                tagCounts.clear();
            }
        }
        return yearReport;
    }
}