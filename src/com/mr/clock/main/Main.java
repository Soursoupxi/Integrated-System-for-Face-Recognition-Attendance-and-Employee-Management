//启动类
package com.mr.clock.main;

import com.mr.clock.frame.MainFrame;
import com.mr.clock.frame.MainPanel;

public class Main
{
    public static void main(String[] args)
    {
        MainFrame mf=new MainFrame(); //创建主窗体
        mf.setPanel(new MainPanel(mf)); //主窗体加载主面板
        mf.setVisible(true); //显示主窗体
    }
}