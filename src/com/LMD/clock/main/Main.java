//启动类
package com.LMD.clock.main;

import com.LMD.clock.frame.MainFrame;
import com.LMD.clock.frame.MainPanel;

public class Main
{
    public static void main(String[] args)
    {
        MainFrame mf=new MainFrame(); //创建主窗体
        mf.setPanel(new MainPanel(mf)); //主窗体加载主面板
        mf.setVisible(true); //显示主窗体
    }
}