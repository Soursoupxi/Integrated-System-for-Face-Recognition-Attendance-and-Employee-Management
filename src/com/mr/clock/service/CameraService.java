//服务类——摄像头服务
package com.mr.clock.service;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class CameraService
{
    private static final Webcam WEBCAM=Webcam.getDefault(); //摄像头对象

    /*
    开启摄像头
    @return 是否成功开启
     */
    public static boolean startCamera()
    {
        if(WEBCAM==null)
        { //若计算机没有连接摄像头
            return false;
        }
        WEBCAM.setViewSize(new Dimension(640,480)); //摄像头采用默认640×480宽高
        return WEBCAM.open(); //开启摄像头，返回是否成功开启
    }

    /*
    摄像头是否开启
    @return
     */
    public static boolean cameraIsOpen()
    {
        if(WEBCAM==null)
        { //若计算机没有连接摄像头
            return false;
        }
        return WEBCAM.isOpen();
    }

    /*
    获取摄像头画面面板
    @return
     */
    public static JPanel getCameraPanel()
    {
        WebcamPanel panel=new WebcamPanel(WEBCAM); //摄像头画面面板
        panel.setMirrored(true); //开启镜像
        return panel;
    }

    /*
    获取摄像头捕获的帧画面
    @return 原始大小帧画面
     */
    public static BufferedImage getCameraFrame()
    {
        return WEBCAM.getImage(); //获取当前帧画面
    }

    /*
    释放摄像头资源
     */
    public static void releaseCamera()
    {
        if(WEBCAM!=null)
        {
            WEBCAM.close(); //关闭摄像头
        }
    }
}