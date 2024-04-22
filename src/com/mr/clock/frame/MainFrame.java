//窗体类——主窗体
package com.mr.clock.frame;

import com.mr.clock.session.Session;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//主窗体
public class MainFrame extends JFrame
{
    public MainFrame()
    {
        Session.init(); //全局会话初始化
        addListener(); //添加监听
        setSize(640, 480); //窗体宽高
        setDefaultCloseOperation(EXIT_ON_CLOSE); //单击“关闭”，释放窗体资源并关闭程序
        Toolkit tool = Toolkit.getDefaultToolkit(); //创建系统默认组件工具包
        Dimension dms = tool.getScreenSize(); //获取屏幕尺寸，赋给坐标对象
        setLocation((dms.width - getWidth()) / 2, (dms.height - getHeight()) / 2); //让主窗体在屏幕中间显示
    }

    //添加组件监听
    private void addListener()
    {
        addWindowFocusListener(new WindowAdapter()
        { //添加窗体事件监听
            @Override
            public void windowClosing(WindowEvent e)
            { //弹出选择对话框，并记录用户做出的选择
                int closeCode=JOptionPane.showConfirmDialog(MainFrame.this,"是否退出程序？","提示！",JOptionPane.YES_NO_OPTION);
                if(closeCode==JOptionPane.YES_OPTION)
                { //若用户选择“是”
                    Session.dispose(); //释放全局资源
                    System.exit(0); //正常关闭程序
                }
            }
        });
    }

    /*
    更换主容器中的面板
    @param panel 更换的面板
     */
    public void setPanel(JPanel panel)
    {
        Container ctn=getContentPane(); //获取主容器对象
        ctn.removeAll(); //删除容器中所有面板
        ctn.add(panel); //容器添加面板
        ctn.validate(); //容器重新验证所有组件
    }
}