//窗体类——录入新员工面板
package com.LMD.clock.frame;

import com.LMD.clock.pojo.Employee;
import com.LMD.clock.service.CameraService;
import com.LMD.clock.service.FaceEngineService;
import com.LMD.clock.service.HRService;
import com.LMD.clock.service.ImageService;
import com.LMD.clock.session.Session;
import com.arcsoft.face.FaceFeature;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class AddEmployeePanel extends JPanel
{
    private MainFrame parent; //主窗体
    private JLabel message; //提示信息
    private JTextField nameField; //姓名文本框
    private JTextField idField; //编号文本框
    private JButton submitBtn; //提交按钮
    private JButton backBtn; //返回按钮
    private JPanel centerPl; //中部面板

    public AddEmployeePanel(MainFrame parent)
    {
        this.parent=parent;
        init(); //组件初始化
        addListener(); //为组件添加监听
    }

    //组件初始化
    private void init()
    {
        parent.setTitle("录入新员工"); //修改主窗体标题
        JLabel idLabel=new JLabel("员工编号：",JLabel.RIGHT);
        JLabel nameLabel=new JLabel("员工名称：",JLabel.RIGHT); //在标签组件中将文本右对齐
        idField=new JTextField("(自动创建编号)",15);
        nameField=new JTextField(15); //文本框宽度为15
        submitBtn=new JButton("拍照并录入");
        backBtn=new JButton("返回");

        setLayout(new BorderLayout()); //采用边界布局

        JPanel bottomPl=new JPanel(); //底部组件
        bottomPl.add(idLabel);
        bottomPl.add(idField);
        bottomPl.add(nameLabel); //添加底部组件
        bottomPl.add(nameField);
        bottomPl.add(submitBtn);
        bottomPl.add(backBtn);
        add(bottomPl,BorderLayout.SOUTH);

        centerPl=new JPanel(); //中部面板
        centerPl.setLayout(null); //采用null布局

        message=new JLabel("正在打开摄像头……");
        message.setFont(new Font("黑体",Font.BOLD,40)); //设置字体
        message.setBounds((640-400)/2,20,400,50); //设置组件的坐标与宽高
        centerPl.add(message);

        JPanel blackPl=new JPanel(); //黑色面板
        blackPl.setBackground(Color.BLACK); //背景色为黑色
        blackPl.setBounds(150,75,320,240); //设置组件的坐标与宽高
        centerPl.add(blackPl);

        add(centerPl,BorderLayout.CENTER);

        //启动摄像头线程
        Thread cameraThread=new Thread()
        { //摄像头启动线程
            public void run()
            {
                if(CameraService.startCamera())
                { //若摄像头成功开启
                    message.setText("请正面面向摄像头"); //更换提示信息
                    JPanel cameraPanel=CameraService.getCameraPanel(); //获取摄像头画面面板
                    cameraPanel.setBounds(150,75,320,240); //设置面板的坐标与宽高
                    centerPl.add(cameraPanel); //放到中部面板
                }
                else
                { //弹出提示
                    JOptionPane.showMessageDialog(parent,"未检测到摄像头");
                    backBtn.doClick(); //触发“返回”按钮的单击事件
                }
            }
        };
        cameraThread.start(); //开启线程
    }

    //为组件添加监听
    private void addListener()
    {
        submitBtn.addActionListener(new ActionListener()
        { //提交按钮事件
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String name=nameField.getText().trim(); //获取文本框中的名字
                String id=idField.getText().trim();
                if(name==null || "".equals(name))
                { //若员工姓名为空
                    JOptionPane.showMessageDialog(parent,"员工姓名不能为空！");
                    return; //中断方法
                }
                if(id==null || "".equals(id))
                { //若员工编号为空
                    JOptionPane.showMessageDialog(parent,"员工编号不能为空！");
                    return;
                }
                if(!CameraService.cameraIsOpen())
                { //若摄像头未开启
                    JOptionPane.showMessageDialog(parent,"摄像头尚未开启，请稍后。");
                    return;
                }
                BufferedImage image=CameraService.getCameraFrame(); //获取当前摄像头捕捉的帧
                FaceFeature ff= FaceEngineService.getFaceFeature(image); //获取此图像中人脸的面部特征
                if(ff==null)
                {
                    JOptionPane.showMessageDialog(parent,"未检测到有效人脸信息。");
                    return;
                }
                Employee emp=null;
                if("(自动创建编号)".equals(id))
                {
                    emp= HRService.addEmp(name,image);
                    JOptionPane.showMessageDialog(parent,"新员工创建成功，其员工编号为"+emp.getId()+"。");
                }
                else
                {
                    if(!id.matches("^[0-9]+$")) //^表示匹配字符串开头，$表示匹配字符串结尾
                    {
                        JOptionPane.showMessageDialog(parent,"员工编号存在非法字符，请重新输入，输入格式为0~9的任意长度的数字串，或输入“(自动创建编号)”以创建新的员工。");
                        return;
                    }
                    for(Employee emp2: Session.EMP_SET)
                    { //遍历所有员工
                        if(emp2.getId().equals(Integer.parseInt(id)))
                        { //若编号一致
                            JOptionPane.showMessageDialog(parent,"此员工编号已被占用，请重新输入不同的员工编号，或输入“(自动创建编号)”以创建新的员工。");
                            return;
                        }
                    }
                    emp=HRService.addEmp(Integer.parseInt(id),name,image);
                    JOptionPane.showMessageDialog(parent,"员工添加成功！"); //弹出提示框
                }
                ImageService.saveFaceImage(image,emp.getCode()); //保存员工照片文件
                Session.FACE_FEATURE_MAP.put(emp.getCode(),ff); //全局会话记录此人面部特征
                backBtn.doClick(); //触发“返回”按钮的单击事件
            }
        });

        backBtn.addActionListener(new ActionListener()
        { //返回按钮事件
            @Override
            public void actionPerformed(ActionEvent e)
            {
                CameraService.releaseCamera(); //释放摄像头
                parent.setPanel(new EmployeeManagementPanel(parent)); //主窗体切换到员工管理面板
            }
        });
    }
}