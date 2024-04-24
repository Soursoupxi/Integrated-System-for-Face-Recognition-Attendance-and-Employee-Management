package com.LMD.clock.session;

import com.LMD.clock.pojo.Administrator;
import com.LMD.clock.pojo.Employee;
import com.LMD.clock.pojo.WorkTime;
import com.LMD.clock.service.CameraService;
import com.LMD.clock.service.HRService;
import com.LMD.clock.util.JDBCUtil;
import com.arcsoft.face.FaceFeature;
import com.LMD.clock.service.FaceEngineService;
import com.LMD.clock.service.ImageService;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Session
{
    //当前已登入的管理员
    public static Administrator admin=null;

    //当前作息时间
    public static WorkTime worktime=new WorkTime("09:00:00","17:00:00"); //若数据库无作息时间，则默认作息时间为此

    //全部员工
    public static final HashSet<Employee> EMP_SET=new HashSet<>();

    //全部人脸特征
    public static final HashMap<String,FaceFeature> FACE_FEATURE_MAP=new HashMap<>();

    //全部人脸图像
    public static final HashMap<String,BufferedImage> IMAGE_MAP=new HashMap<>();

    //全部打卡记录
    public static final HashMap<Integer,Set<Date>> RECORD_MAP=new HashMap<>();

    //初始化全局资源
    public static void init()
    {
        ImageService.loadAllImage(); //加载所有人脸图像文件
        HRService.loadWorkTime(); //加载作息时间
        HRService.loadAllEmp(); //加载所有员工
        HRService.loadAllClockinRecord(); //加载所有打卡记录
        FaceEngineService.loadAllFaceFeature(); //加载所有人脸特征
    }

    //释放全局资源
    public static void dispose()
    {
        FaceEngineService.dispost(); //释放人脸识别引擎
        CameraService.releaseCamera(); //释放摄像头
        JDBCUtil.closeConnection(); //关闭数据库连接
    }
}