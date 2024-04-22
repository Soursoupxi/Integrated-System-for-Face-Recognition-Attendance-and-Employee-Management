//服务类——人脸识别服务
package com.mr.clock.service;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.mr.clock.session.Session;

import javax.naming.ConfigurationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class FaceEngineService
{
    private static String appId =null;
    private static String sdkKey=null;
    private static FaceEngine faceEngine=null; //人脸识别引擎
    private static String ENGINE_PATH="libs_ArcFace/WIN64"; //算法库文件夹地址
    private static final String CONFIG_FILE="src/com/mr/clock/config/ArcFace.properties"; //配置文件地址
    static
    {
        Properties prop=new Properties(); //配置文件解析类
        File config=new File(CONFIG_FILE); //配置文件的文件对象
        try
        {
            if(!config.exists())
            { //若配置文件不存在
                throw new FileNotFoundException("缺少文件："+config.getAbsolutePath());
            }
            prop.load(new FileInputStream(config)); //加载配置文件
            appId=prop.getProperty("app_id"); //获取指定字段值
            sdkKey=prop.getProperty("sdk_key");
            if(appId==null || sdkKey==null)
            { //若配置文件中获取不到这两个字段
                throw new ConfigurationException("lib_ArcFace.properties文件缺少配置信息");
            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(ConfigurationException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        File path=new File(ENGINE_PATH); //算法库文件夹
        faceEngine=new FaceEngine(path.getAbsolutePath()); //人脸识别引擎
        //激活引擎，首次激活需要联网
        int errorCode=faceEngine.activeOnline(appId,sdkKey);
        if(errorCode!=ErrorInfo.MOK.getValue() && errorCode!=ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue())
        {
            System.out.println("ERROR:ArcFace引擎激活失败，请检查激活码是否填写错误，或重新联网激活");
        }
        EngineConfiguration egConfig=new EngineConfiguration(); //引擎配置
        egConfig.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE); //单张图片模式
        egConfig.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT); //检测所有角度
        egConfig.setDetectFaceMaxNum(1); //检测最多人脸数
        egConfig.setDetectFaceScaleVal(16); //设置人脸相对于所在图片的长边的占比
        FunctionConfiguration funcConfig=new FunctionConfiguration(); //功能配置
        funcConfig.setSupportFaceDetect(true); //支持人脸检测
        funcConfig.setSupportFaceRecognition(true); //支持人脸识别
        egConfig.setFunctionConfiguration(funcConfig); //引擎使用此功能配置
        errorCode=faceEngine.init(egConfig); //初始化引擎
        if(errorCode!=ErrorInfo.MOK.getValue())
        {
            System.err.println("ERROR:ArcFace引擎初始化失败");
        }
    }

    /*
    获取一张人脸的面部特征
    @param img 人脸照片
    @return
     */
    public static FaceFeature getFaceFeature(BufferedImage img)
    {
        if(img==null)
        {
            throw new NullPointerException("人脸图像为null");
        }
        //创建一个和原图像大小相同的临时图像，临时图像类型为普通BRG图像
        BufferedImage face=new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_BGR);
        face.setData(img.getData()); //临时图像使用原图像中的数据
        ImageInfo imgInfo=ImageFactory.bufferedImage2ImageInfo(face); //采集图像信息
        List<FaceInfo> faceInfoList=new ArrayList<FaceInfo>(); //人脸信息列表
        //从图像信息中采集人脸信息
        faceEngine.detectFaces(imgInfo.getImageData(),imgInfo.getWidth(),imgInfo.getHeight(),imgInfo.getImageFormat(),faceInfoList);
        if(faceInfoList.isEmpty())
        { //若人脸信息是空的
            return null;
        }
        FaceFeature faceFeature=new FaceFeature(); //人脸特征
        //从人脸信息中采集人脸特征
        faceEngine.extractFaceFeature(imgInfo.getImageData(),imgInfo.getWidth(),imgInfo.getHeight(),imgInfo.getImageFormat(),faceInfoList.get(0),faceFeature);
        return faceFeature; //采集之后的人脸特征
    }

    /*
    加载所有面部特征
     */
    public static void loadAllFaceFeature()
    {
        Set<String> keys= Session.IMAGE_MAP.keySet(); //获取所有人脸图片对应的特征码集合
        for(String code:keys)
        { //遍历所有特征码
            BufferedImage image=Session.IMAGE_MAP.get(code); //取出一张人脸图片
            FaceFeature faceFeature=getFaceFeature(image); //获取该人脸图片的人脸特征对象
            Session.FACE_FEATURE_MAP.put(code,faceFeature); //将人脸特征对象保存至全局会话中
        }
    }

    /*
    从人脸特征库中检测人脸
    @param 目标人脸特征
    @return 该人脸特征对应的特征码
    */
    public static String detectFace(FaceFeature targetFaceFeature)
    {
        if(targetFaceFeature==null)
        {
            return null;
        }
        //获取所有人脸特征对应的特征码集合
        Set<String> keys=Session.FACE_FEATURE_MAP.keySet();
        double score=0; //匹配最高得分
        String resultCode=null; ///评分对应的特征码
        for(String code:keys)
        { //遍历所有特征码
            FaceFeature sourceFaceFeature=Session.FACE_FEATURE_MAP.get(code); //取出一个人脸特征对象
            FaceSimilar faceSimilar=new FaceSimilar(); //特征对比对象
            faceEngine.compareFaceFeature(targetFaceFeature,sourceFaceFeature,faceSimilar);
            if(faceSimilar.getScore()>score)
            { //若得分大于当前最高得分
                score=faceSimilar.getScore(); //重新记录当前最高得分
                resultCode=code; //记录最高得分的特征码
            }
        }
        if(score>0.9)
        { //若最高得分大于0.9，则认为找到匹配人脸
            return resultCode; //返回人脸对应的特征码
        }
        return null;
    }

    /*
    释放资源
     */
    public static void dispost()
    {
        faceEngine.unInit(); //引擎卸载
    }
}