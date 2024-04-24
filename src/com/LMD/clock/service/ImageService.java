//服务类——图像文件服务
package com.LMD.clock.service;

import com.LMD.clock.session.Session;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ImageService
{
    private static final File FACE_DIR=new File("src/faces"); //存放员工照片文件的文件夹
    private static final String SUFFIX="png"; //图像文件的默认格式

    /*
    加载所有人脸图像文件
    @return
     */
    public static Map<String,BufferedImage> loadAllImage()
    {
        if(!FACE_DIR.exists())
        {
            System.err.println("src\\faces\\人脸图像文件夹丢失！");
            return null;
        }
        File faces[]=FACE_DIR.listFiles(); //获取文件夹下的所有文件
        for(File file:faces)
        { //遍历所有文件
            try
            {
                BufferedImage img=ImageIO.read(file); //创建该图像文件的BufferedImage对象
                String fileName=file.getName(); //文件名
                String code=fileName.substring(0,fileName.indexOf('.')); //截取文件名，去掉后缀名
                Session.IMAGE_MAP.put(code,img); //将人脸图像添加到全局会话中
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
    保存人脸图像文件
    @param image 员工人脸图像
    @param code 员工特征码
     */
    public static void saveFaceImage(BufferedImage image,String code)
    {
        try
        { //将图像按照SUFFIX格式写入文件夹中
            ImageIO.write(image,SUFFIX,new File(FACE_DIR,code+"."+SUFFIX));
            Session.IMAGE_MAP.put(code,image); //将人脸图像添加到全局会话中
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
    删除人脸图像文件
    @param code 员工特征码
     */
    public static void deleteFaceImage(String code)
    {
        Session.IMAGE_MAP.remove(code); //在全局会话中删除该员工的图像
        File image = new File(FACE_DIR, code + "." + SUFFIX); //创建该员工人脸图像文件对象
        if(image.exists())
        {
            image.delete(); //删除文件
            System.out.println(image.getAbsolutePath()+" ---已删除"); //提示删除文件成功
        }
    }
}