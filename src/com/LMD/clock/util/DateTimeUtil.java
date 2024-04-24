//工具类——日期时间工具
package com.LMD.clock.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class DateTimeUtil
{
    //获取当前日期、时间字符串
    public static String timeNow()
    {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
    public static String dateNow()
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
    public static String dateTimeNow()
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    //获取由当前年、月、日、时、分、秒数字所组成的数组
    public static Integer[] now()
    {
        Integer now[]=new Integer[6];
        Calendar c=Calendar.getInstance(); //日历对象
        now[0]=c.get(Calendar.YEAR);
        now[1]=c.get(Calendar.MONTH)+1; //注意在Calendar日历类中一月份是用数字0表示的，所以要在月份结果后面+1
        now[2]=c.get(Calendar.DAY_OF_MONTH);
        now[3]=c.get(Calendar.HOUR_OF_DAY);
        now[4]=c.get(Calendar.MINUTE);
        now[5]=c.get(Calendar.SECOND);
        return now;
    }

    //获取指定月份的总天数
    public static int getLastDay(int year,int month)
    {
        Calendar c=Calendar.getInstance(); //日历对象
        c.set(Calendar.YEAR,year); //指定年
        c.set(Calendar.MONTH,month-1); //指定月
        return c.getActualMaximum(Calendar.DAY_OF_MONTH); //返回这月的最后一天
    }

    //将字符串转换为Date对象
    public static Date dateOf(String datetime)throws ParseException
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetime);
    }

    //按照指定年、月、日和时间创建Date对象
    public static Date dateOf(int year,int month,int day,String time)throws ParseException
    { //%02d表示长度为2，当位数不足2时，用0左补位的整数
        String datetime=String.format("%4d-%02d-%02d %s",year,month,day,time);
        return dateOf(datetime);
    }

    //校验时间字符串是否符合HH:mm:ss格式
    public static boolean checkTimeStr(String time)
    {
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        try
        {
            sdf.parse(time);
            return true;
        }
        catch(ParseException e)
        { //发生异常则表示字符串格式错误
            return false;
        }
    }
}