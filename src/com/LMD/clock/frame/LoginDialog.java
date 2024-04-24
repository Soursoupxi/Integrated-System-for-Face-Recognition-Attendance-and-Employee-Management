//窗体类——登入对话框
package com.LMD.clock.frame;

import com.LMD.clock.service.HRService;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends JDialog
{
    private JTextField usernameField=null; //用户名文本框
    private JPasswordField passwordField=null; //密码输入框
    private JButton loginBtn=null; //登入按钮
    private JButton cancelBtn=null; //取消按钮
    private final int WIDTH=300,HEIGHT=150; //对话框的宽高

    public LoginDialog(Frame owner)
    {
        super(owner,"管理员登入",true); //阻塞主窗体
        setSize(WIDTH,HEIGHT); //设置宽高
        //在主窗体中央显示
        setLocation(owner.getX()+(owner.getWidth()-WIDTH)/2,owner.getY()+(owner.getHeight()-HEIGHT)/2);
        init(); //组件初始化
        addListener(); //为组件添加监听
    }

    //组件初始化
    private void init()
    {
        JLabel usernameLabel=new JLabel("用 户 名：",JLabel.CENTER);
        JLabel passwordLabel=new JLabel("密    码：",JLabel.CENTER);
        usernameField=new JTextField();
        passwordField=new JPasswordField();
        loginBtn=new JButton("登 入");
        cancelBtn=new JButton("取 消");

        Container ctn=getContentPane();
        ctn.setLayout(new GridLayout(3,2)); //3行2列的网络布局
        ctn.add(usernameLabel);
        ctn.add(usernameField);
        ctn.add(passwordLabel);
        ctn.add(passwordField);
        ctn.add(loginBtn);
        ctn.add(cancelBtn);
    }

    //为组件添加监听
    private void addListener()
    {
        cancelBtn.addActionListener(new ActionListener()
        { //取消按钮事件
            @Override
            public void actionPerformed(ActionEvent e)
            {
                LoginDialog.this.dispose(); //销毁登入对话框
            }
        });

        loginBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            { //登入按钮事件
                String username=usernameField.getText().trim(); //获取用户输入的用户名，trim()方法用于删除字符串的头尾空白符
                String password=new String(passwordField.getPassword()); //获取用户输入的密码
                boolean result= HRService.userLogin(username,password);
                if(result)
                { //若正确
                    LoginDialog.this.dispose(); //销毁登入对话框
                }
                else
                {
                    JOptionPane.showMessageDialog(LoginDialog.this,"用户名或密码错误！");
                }
            }
        });

        //密码输入框敲击回车事件
        passwordField.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                loginBtn.doClick(); //触发登入按钮点击事件
            }
        });

        //用户名文本框敲击回车事件
        usernameField.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                passwordField.grabFocus(); //密码输入框获取光标
            }
        });
    }
}