//窗体类——主菜单面板
package com.LMD.clock.frame;

import com.LMD.clock.pojo.Employee;
import com.LMD.clock.service.CameraService;
import com.LMD.clock.service.HRService;
import com.LMD.clock.session.Session;
import com.LMD.clock.util.DateTimeUtil;
import com.LMD.clock.service.FaceEngineService;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class MainPanel extends JPanel
{
    private MainFrame parent; //主窗体
    private JToggleButton clockin; //打卡按钮
    private JButton attendance; //考勤按钮
    private JButton employee; //员工按钮
    private JButton aboutAuthor; //关于作者按钮
    private JTextArea area; //提示信息文本域
    private DetectFaceThread dfThread; //人脸识别线程
    private JPanel center; //中部面板

    public MainPanel(MainFrame parent)
    {
        this.parent=parent;
        init(); //组件初始化
        addListener(); //为组件添加监听
    }

    //组件初始化
    private void init()
    {
        parent.setTitle("集人脸识别打卡、考勤报表、员工管理功能的跨平台可离线运行系统");

        center=new JPanel(); //中部面板
        center.setLayout(null); //采用null布局

        area=new JTextArea();
        area.setEditable(false); //文本域不可编辑
        area.setFont(new Font("黑体",Font.BOLD,18)); //设置字体
        JScrollPane scroll=new JScrollPane(area); //文本域放入滚动面板
        scroll.setBounds(0,0,275,380); //滚动面板的坐标与宽高
        center.add(scroll);

        clockin=new JToggleButton("打  卡");
        clockin.setFont(new Font("黑体",Font.BOLD,40)); //设置字体
        clockin.setBounds(330,300,240,70); //打卡按钮的坐标与宽高
        center.add(clockin);

        JPanel blackPanel=new JPanel(); //纯黑面板
        blackPanel.setBounds(286,16,320,240); //纯黑面板的坐标与宽高
        blackPanel.setBackground(Color.BLACK); //黑色背景
        center.add(blackPanel);

        setLayout(new BorderLayout()); //主面板采用边界布局
        add(center,BorderLayout.CENTER);

        JPanel bottom=new JPanel(); //底部面板
        attendance=new JButton("考勤报表");
        employee=new JButton("员工管理");
        aboutAuthor=new JButton("关于作者");
        bottom.add(attendance); //添加底部组件
        bottom.add(employee);
        bottom.add(aboutAuthor);
        add(bottom,BorderLayout.SOUTH);
    }

    //为组件添加监听
    private void addListener()
    {
        attendance.addActionListener(new ActionListener()
        { //考勤按钮事件
           @Override
           public void actionPerformed(ActionEvent e)
           {
               if(Session.admin==null)
               { //若无管理员登入，则创建登入对话框
                    LoginDialog ld=new LoginDialog(parent);
                    ld.setVisible(true); //展示登入对话框
               }
               if(Session.admin!=null)
               { //若管理员已登入，则创建考勤报表面板
                    AttendanceManagementPanel amp=new AttendanceManagementPanel(parent);
                    parent.setPanel(amp); //主面板切换至考勤面板
                    releaseCamera(); //释放摄像头资源
               }
           }
        });

        employee.addActionListener(new ActionListener()
        { //员工按钮事件
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(Session.admin==null)
                { //若无管理员登入，则创建登入对话框
                    LoginDialog ld=new LoginDialog(parent);
                    ld.setVisible(true); //展示登入对话框
                }
                if(Session.admin!=null)
                { //若管理员已登入，则创建员工管理面板
                    EmployeeManagementPanel emp=new EmployeeManagementPanel(parent);
                    parent.setPanel(emp); //主窗体切换至员工管理面板
                    releaseCamera(); //释放摄像头资源
                }
            }
        });

        clockin.addActionListener(new ActionListener()
        { //打卡按钮事件
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(clockin.isSelected())
                { //若打卡按钮是选中状态
                    area.append("正在开启摄像头，请稍后……\n"); //文本域添加提示信息
                    clockin.setEnabled(false); //打卡按钮不可用
                    clockin.setText("关闭摄像头"); //更改打卡按钮的文本
                    Thread cameraThread=new Thread()
                    { //创建启动摄像头的临时线程
                        public void run()
                        {
                            if(CameraService.startCamera())
                            { //若摄像头可以正常开启
                                area.append("请面向摄像头打卡。\n"); //添加提示
                                clockin.setEnabled(true); //打卡按钮可用
                                JPanel cameraPanel=CameraService.getCameraPanel(); //获取摄像头画面面板
                                cameraPanel.setBounds(286,16,320,240); //设置面板的坐标与宽高
                                center.add(cameraPanel); //放到中部面板中
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(parent,"未检测到摄像头！"); //弹出提示
                                releaseCamera(); //释放摄像头资源
                                return; //停止方法
                            }
                        }
                    };
                    cameraThread.start(); //启动临时线程
                    dfThread=new DetectFaceThread(); //创建人脸识别线程
                    dfThread.start(); //启动人脸识别线程
                }
                else
                { //若打卡按钮不是选中状态
                    releaseCamera(); //释放摄像头资源
                }
            }
        });

        aboutAuthor.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(parent,"邮箱：scard713@gmail.com \n gitHub主页： \n github.com/QKenRu \n","关于作者",JOptionPane.DEFAULT_OPTION);
            }
        });
    }

    /*
    释放摄像头及面板中的一些资源
     */
    private void releaseCamera()
    {
        CameraService.releaseCamera(); //释放摄像头
        area.append("摄像头已关闭。\n"); //添加提示信息
        if(dfThread!=null)
        { //若人脸识别线程被创建
            dfThread.stopThread(); //停止线程
        }
        clockin.setText("打  卡"); //更改打卡按钮的文本
        clockin.setSelected(false); //打卡按钮变为未选中状态
        clockin.setEnabled(true); //打卡按钮可用
    }

    /*
    人脸识别线程
     */
    private class DetectFaceThread extends Thread
    {
        boolean work=true; //人脸识别线程是否继续扫描image

        @Override
        public void run()
        {
            while(work)
            {
                if(CameraService.cameraIsOpen())
                { //若摄像头已开启
                    BufferedImage frame=CameraService.getCameraFrame(); //获取摄像头的当前帧
                    if(frame!=null)
                    { //若可以获得有效帧
                        //获取当前帧中出现的人脸对应的特征码
                        String code=FaceEngineService.detectFace(FaceEngineService.getFaceFeature(frame));
                        if(code!=null)
                        { //若特征码不为null，表明画面中存在某员工的人脸
                            Employee emp= HRService.getEmp(code); //根据特征码获取员工对象
                            HRService.addClockinRecord(emp); //为此员工添加打卡记录
                            area.append("\n"+ DateTimeUtil.dateTimeNow()+"\n");
                            area.append(emp.getName()+"打卡成功。\n\n");
                            releaseCamera(); //释放摄像头
                        }
                    }
                }
            }
        }

        public synchronized void stopThread()
        { //停止人脸识别线程
            work=false;
        }
    }
}