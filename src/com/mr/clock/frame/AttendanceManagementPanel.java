//面板类——考勤报表面板类
package com.mr.clock.frame;

import com.mr.clock.pojo.WorkTime;
import com.mr.clock.service.HRService;
import com.mr.clock.session.Session;
import com.mr.clock.util.DateTimeUtil;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AttendanceManagementPanel extends JPanel
{
    private MainFrame parent; //主窗体

    private JToggleButton dayRecordBtn; //日报按钮
    private JToggleButton monthRecordBtn; //月报按钮
    private JToggleButton yearRecordBtn; //年报按钮
    private JToggleButton worktimeBtn; //作息时间设置按钮
    private JButton back; //返回按钮
    private JButton flushD,flushM,flushY; //分别在日报、月报、年报面板中的刷新按钮
    private JPanel centerPanel; //中央面板
    private CardLayout card; //中央面板使用的卡片和布局

    private JPanel dayRecordPanel; //日报面板
    private JTextArea area; //日报面板里的文本域
    private JComboBox<Integer> yearComboBoxD,monthComboBoxD,dayComboBoxD; //日报面板中的年、月、日下拉列表
    private DefaultComboBoxModel<Integer> yearModelD,monthModelD,dayModelD; //年、月、日下拉列表使用的数据模型

    private JPanel monthRecordPanel; //月报面板
    private JTable tableM; //月报面板里的表格
    private DefaultTableModel modelM; //表格的数据模型
    private JComboBox<Integer> yearComboBoxM,monthComboBoxM; //月报面板里的年、月下拉列表
    private DefaultComboBoxModel<Integer> yearModelM,monthModelM; //年、月下拉列表使用的数据模型

    private JPanel yearRecordPanel; //年报面板
    private JTable tableY; //年报面板里的表格
    private DefaultTableModel modelY; //表格的数据模型
    private JComboBox<Integer> yearComboBoxY; //年表面板里的年下拉列表
    private DefaultComboBoxModel<Integer> yearModelY; //年报面板下拉列表使用的数据模型

    private JPanel worktimePanel; //作息时间面板
    private JTextField hourS,minuteS,secondS; //上班时间的时、分、秒文本框
    private JTextField hourE,minuteE,secondE; //下班时间的时、分、秒文本框
    private JButton updateWorktime; //替换作息时间按钮

    private JTextField searchFieldM; //搜索框
    private JTextField searchFieldY; //搜索框
    private JComboBox<String> searchTypeM; //搜索方式下拉列表框
    private JComboBox<String> searchTypeY; //搜索方式下拉列表框
    private JButton searchBtnM; //搜索按钮
    private JButton searchBtnY; //搜索按钮

    public AttendanceManagementPanel(MainFrame parent)
    {
        this.parent = parent;
        init();// 组件初始化
        addListener();// 为组件添加监听
    }

    //组件初始化
    private void init()
    {
        WorkTime workTime=Session.worktime; //获取当前作息时间
        parent.setTitle("考勤报表（上班时间："+workTime.getStart()+"，下班时间："+workTime.getEnd()+"）"); //修改主窗体标题

        dayRecordBtn=new JToggleButton("日报");
        dayRecordBtn.setSelected(true); //日报按钮处于选中状态
        monthRecordBtn=new JToggleButton("月报");
        yearRecordBtn=new JToggleButton("年报");
        worktimeBtn=new JToggleButton("作息时间设置");
        ButtonGroup buttonGroup=new ButtonGroup(); //按钮组，保证几个按钮中只有一个处于选中状态
        buttonGroup.add(dayRecordBtn);
        buttonGroup.add(monthRecordBtn);
        buttonGroup.add(yearRecordBtn);
        buttonGroup.add(worktimeBtn);

        back=new JButton("返回");
        flushD=new JButton("刷新日报报表");
        flushM=new JButton("刷新月报报表");
        flushY=new JButton("刷新年报报表");

        ComboBoxInit(); //下拉列表初始化
        dayRecordInit(); //日报面板初始化
        monthRecordInit(); //月报面板初始化
        yearRecordInit(); //年报面板初始化
        worktimeInit(); //作息时间面板初始化

        card=new CardLayout(); //卡片布局
        centerPanel=new JPanel(card); //中部面板采用卡片布局
        centerPanel.add("day",dayRecordPanel); //day标签为日报面板
        centerPanel.add("month",monthRecordPanel); //month标签为月报面板
        centerPanel.add("year",yearRecordPanel); //year标签为年报面板
        centerPanel.add("worktime",worktimePanel); //worktime标签为作息时间面板

        JPanel bottom=new JPanel(); //底部面板
        bottom.add(dayRecordBtn); //添加底部的组件
        bottom.add(monthRecordBtn);
        bottom.add(yearRecordBtn);
        bottom.add(worktimeBtn);
        bottom.add(back);

        setLayout(new BorderLayout()); //采用边界布局
        add(centerPanel,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
    }

    //为组件添加监听
    private void addListener()
    {
        dayRecordBtn.addActionListener(new ActionListener()
        { //日报按钮事件
            @Override
            public void actionPerformed(ActionEvent e)
            { //卡片布局切换至日报面板
                card.show(centerPanel,"day");
            }
        });

        monthRecordBtn.addActionListener(new ActionListener()
        { //月报按钮事件
            @Override
            public void actionPerformed(ActionEvent e)
            { //卡片布局切换至月报面板
                card.show(centerPanel,"month");
            }
        });

        yearRecordBtn.addActionListener(new ActionListener()
        { //年报按钮事件
            @Override
            public void actionPerformed(ActionEvent e)
            { //卡片布局切换至年报面板
                card.show(centerPanel,"year");
            }
        });

        worktimeBtn.addActionListener(new ActionListener()
        { //作息时间设置按钮事件
            @Override
            public void actionPerformed(ActionEvent e)
            { //卡片布局切换至作息时间面板
                card.show(centerPanel,"worktime");
            }
        });

        back.addActionListener(new ActionListener()
        { //返回按钮事件
            @Override
            public void actionPerformed(ActionEvent e)
            { //主窗体切换至主面板
                parent.setPanel(new MainPanel(parent));
            }
        });

        flushD.addActionListener(new ActionListener()
        { //日报面板刷新按钮事件
            @Override
            public void actionPerformed(ActionEvent e)
            {
                updateDayRecord(); //更新日报
            }
        });

        flushM.addActionListener(new ActionListener()
        { //月报面板刷新按钮事件
            @Override
            public void actionPerformed(ActionEvent e)
            {
                updateMonthRecord(); //更新月报
            }
        });

        flushY.addActionListener(new ActionListener()
        { //年报面板刷新按钮事件
            @Override
            public void actionPerformed(ActionEvent e)
            {
                updateYearRecord(); //更新年报
            }
        });

        updateWorktime.addActionListener(new ActionListener()
        { //替换作息时间按钮事件
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String hs=hourS.getText().trim(); //上班的小时
                String ms=minuteS.getText().trim(); //上班的分钟
                String ss=secondS.getText().trim(); //上班的秒
                String he=hourE.getText().trim(); //下班的小时
                String me=minuteE.getText().trim(); //下班的分钟
                String se=secondE.getText().trim(); //下班的秒

                boolean check=true; //时间校验成功标志
                String startInput=hs+":"+ms+":"+ss; //拼接上班时间
                String endInput=he+":"+me+":"+se; //拼接下班时间
                if(!DateTimeUtil.checkTimeStr(startInput))
                { //若上班时间不是正确格式
                    check=false; //校验失败
                    JOptionPane.showMessageDialog(parent,"上班时间的格式不正确");
                }
                if(!DateTimeUtil.checkTimeStr(endInput))
                { //若下班时间不是正确格式
                    check=false; //校验失败
                    JOptionPane.showMessageDialog(parent,"下班时间的格式不正确");
                }
                if(check)
                { //若校验成功，弹出选择对话框并记录用户选择
                    int confirmation=JOptionPane.showConfirmDialog(parent,"确定做出以下设置？\n上班时间："+startInput+"\n下班时间："+endInput,"提示！",JOptionPane.YES_NO_OPTION);
                    if(confirmation==JOptionPane.YES_OPTION)
                    {
                        WorkTime input=new WorkTime(startInput,endInput);
                        HRService.updateWorkTime(input); //更新作息时间
                        parent.setTitle("考勤报表（上班时间："+startInput+"，下班时间："+endInput+"）"); //修改标题
                    }
                }
            }
        });

        ActionListener dayD_Listener=new ActionListener()
        { //日报面板中的日期下拉列表使用的监听对象
            @Override
            public void actionPerformed(ActionEvent e)
            {
                updateDayRecord(); //更新日报
            }
        };
        dayComboBoxD.addActionListener(dayD_Listener); //添加监听

        ActionListener yearD_monthD_Listener=new ActionListener()
        { //日报面板中的年、月下拉列表使用的监听对象
            @Override
            public void actionPerformed(ActionEvent e)
            { //删除日期下拉列表使用的监听对象，防止日期改变后自动触发监听
                dayComboBoxD.removeActionListener(dayD_Listener);
                updateDayModel(); //更新日下拉列表中的天数
                updateDayRecord(); //更新日报
                dayComboBoxD.addActionListener(dayD_Listener); //重新为日期下拉列表添加监听对象
            }
        };

        yearComboBoxD.addActionListener(yearD_monthD_Listener); //添加监听
        monthComboBoxD.addActionListener(yearD_monthD_Listener);

        ActionListener yearM_monthM_Listener=new ActionListener()
        { //月报面板中的年、月下拉列表使用的监听对象
            @Override
            public void actionPerformed(ActionEvent e)
            {
                updateMonthRecord(); //更新月报
            }
        };

        yearComboBoxM.addActionListener(yearM_monthM_Listener); //添加监听
        monthComboBoxM.addActionListener(yearM_monthM_Listener);

        ActionListener yearY_Listener=new ActionListener()
        { //年报面板中的年下拉列表使用的监听对象
            @Override
            public void actionPerformed(ActionEvent e)
            {
                updateYearRecord(); //更新年报
            }
        };

        yearComboBoxY.addActionListener(yearY_Listener); //添加监听

        searchBtnM.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                DefaultTableModel model = (DefaultTableModel) tableM.getModel();
                String text = searchFieldM.getText(); //获取搜索框输入的内容
                String type = (String) searchTypeM.getSelectedItem(); //获取选中的搜索方式
                int column=0; //搜索列的编号
                if (type.equals("员工编号"))
                {
                    column=0;
                }
                else if(type.equals("员工姓名"))
                {
                    column=1;
                }
                boolean found=false;

                for(int i=0;i<model.getRowCount();++i)
                {
                    String value=(String)model.getValueAt(i,column);
                    if(value!=null && value.equals(text))
                    { //若搜索到匹配项，则选中该行并滚动到可见区域
                        tableM.setRowSelectionInterval(i,i); //选中i行column列的表格
                        tableM.scrollRectToVisible(tableM.getCellRect(i,column,true)); //滚动至i行column列所在表格的位置
                        found=true;
                        break;
                    }
                }
                if(!found)
                { //若未找到匹配项，弹出提示窗口
                    JOptionPane.showMessageDialog(null,"未找到匹配项","搜索结果",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        searchBtnY.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                DefaultTableModel model = (DefaultTableModel) tableY.getModel();
                String text = searchFieldY.getText(); //获取搜索框输入的内容
                String type = (String) searchTypeY.getSelectedItem(); //获取选中的搜索方式
                int column=0; //搜索列的编号
                if (type.equals("员工编号"))
                {
                    column=0;
                }
                else if(type.equals("员工姓名"))
                {
                    column=1;
                }
                boolean found=false;

                for(int i=0;i<model.getRowCount();++i)
                {
                    String value=(String)model.getValueAt(i,column);
                    if(value!=null && value.equals(text))
                    { //若搜索到匹配项，则选中该行并滚动到可见区域
                        tableY.setRowSelectionInterval(i,i); //选中i行column列的表格
                        tableY.scrollRectToVisible(tableY.getCellRect(i,column,true)); //滚动至i行column列所在表格的位置
                        found=true;
                        break;
                    }
                }
                if(!found)
                { //若未找到匹配项，弹出提示窗口
                    JOptionPane.showMessageDialog(null,"未找到匹配项","搜索结果",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    //作息时间初始化
    private void worktimeInit()
    {
        WorkTime worktime=Session.worktime; //获取当前的作息时间
        //将上班时间和下班时间分割成时、分、秒数组
        String startTime[]=worktime.getStart().split(":");
        String endTime[]=worktime.getEnd().split(":");

        Font labelFont=new Font("黑体",Font.BOLD,20); //字体

        JPanel top=new JPanel(); //顶部面板

        JLabel startLabel=new JLabel("上班时间："); //文本标签
        startLabel.setFont(labelFont); //使用指定字体
        top.add(startLabel);

        hourS=new JTextField(3); //上班时间的时输入框，长度为3
        hourS.setText(startTime[0]); //默认值为当前上班时间的小时
        top.add(hourS);

        JLabel colon1=new JLabel(":");
        colon1.setFont(labelFont);
        top.add(colon1);

        minuteS=new JTextField(3); //上班时间的分输入框
        minuteS.setText(startTime[1]); //默认值为当前上班时间的分钟
        top.add(minuteS);

        JLabel colon2=new JLabel("：");
        colon2.setFont(labelFont);
        top.add(colon2);

        secondS=new JTextField(3); //上班时间的秒输入框
        secondS.setText(startTime[2]); //默认值为当前上班时间的秒
        top.add(secondS);

        JPanel bottom=new JPanel(); //底部面板

        JLabel endLabel=new JLabel("下班时间：");
        endLabel.setFont(labelFont);
        bottom.add(endLabel);

        hourE=new JTextField(3); //下班时间的时输入框
        hourE.setText(endTime[0]); //默认值为当前下班时间的时
        bottom.add(hourE);

        JLabel colon3=new JLabel(":");
        colon3.setFont(labelFont);
        bottom.add(colon3);

        minuteE=new JTextField(3); //下班时间的分输入框
        minuteE.setText(endTime[1]); //默认值为当前下班时间的分
        bottom.add(minuteE);

        JLabel colon4=new JLabel(":");
        colon4.setFont(labelFont);
        bottom.add(colon4);

        secondE=new JTextField(3); //下班时间的秒输入框
        secondE.setText(endTime[2]); //默认值为当前下班时间的秒
        bottom.add(secondE);

        worktimePanel=new JPanel();
        worktimePanel.setLayout(null); //作息时间面板采用null布局

        JPanel center=new JPanel(); //作息面板中央显示的面板
        center.setLayout(new GridLayout(2,1)); //采用2行1列的网格布局
        center.add(top); //第1行放顶部面板
        center.add(bottom); //第2行底部面板

        center.setBounds(100,60,400,150); //设置面板的坐标与宽高
        worktimePanel.add(center);

        updateWorktime=new JButton("替换作息时间");
        updateWorktime.setFont(new Font("黑体",Font.BOLD,20));
        updateWorktime.setBounds(220,235,170,55); //按钮的坐标与宽高
        worktimePanel.add(updateWorktime);
    }

    //日报面板初始化
    private void dayRecordInit()
    {
        area=new JTextArea();
        area.setEditable(false); //文本域不可编辑
        area.setFont(new Font("宋体",Font.BOLD,24));
        JScrollPane scroll=new JScrollPane(area); //文本域放到滚动面板中

        dayRecordPanel=new JPanel();
        dayRecordPanel.setLayout(new BorderLayout()); //日报面板采用边界布局
        dayRecordPanel.add(scroll,BorderLayout.CENTER); //滚动面板在中部显示

        JPanel top=new JPanel(); //顶部面板
        top.setLayout(new FlowLayout()); //采用流布局
        top.add(yearComboBoxD); //年下拉列表
        top.add(new JLabel("年")); //文本标签
        top.add(monthComboBoxD); //月下拉列表
        top.add(new JLabel("月"));
        top.add(dayComboBoxD); //日下拉列表
        top.add(new JLabel("日"));
        top.add(flushD); //日报面板的刷新按钮
        dayRecordPanel.add(top,BorderLayout.NORTH);

        updateDayRecord(); //更新日报
    }

    //月报面板初始化
    private void monthRecordInit()
    {
        JPanel top=new JPanel(); //顶部面板
        top.add(yearComboBoxM); //年下拉面板
        top.add(new JLabel("年")); //文本标签
        top.add(monthComboBoxM); //月下拉列表
        top.add(new JLabel("月"));
        top.add(flushM); //刷新按钮

        monthRecordPanel=new JPanel();
        monthRecordPanel.setLayout(new BorderLayout()); //月报面板采用边界布局
        monthRecordPanel.add(top,BorderLayout.NORTH);

        modelM=new DefaultTableModel(); //表格数据模型
        tableM=new JTable(modelM); //表格采用数据模型
        tableM.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //关闭自动调整宽度
        tableM.setDefaultEditor(Object.class,null); //表格不可编辑
        JScrollPane tableScroll=new JScrollPane(tableM); //表格放入滚动面板
        monthRecordPanel.add(tableScroll,BorderLayout.CENTER);

        searchFieldM=new JTextField("此处输入文本可进行搜索",15);
        searchTypeM=new JComboBox<>(new String[]{"员工编号","员工姓名"});
        searchBtnM=new JButton("搜索");

        top.add(searchFieldM);
        top.add(searchTypeM);
        top.add(searchBtnM);

        updateMonthRecord(); //更新月报
    }

    //年报面板初始化
    private void yearRecordInit()
    {
        JPanel top=new JPanel(); //顶部面板
        top.add(yearComboBoxY); //年下拉面板
        top.add(new JLabel("年")); //文本标签
        top.add(flushY); //刷新按钮

        yearRecordPanel=new JPanel();
        yearRecordPanel.setLayout(new BorderLayout()); //年报面板采用边界布局
        yearRecordPanel.add(top,BorderLayout.NORTH);

        modelY=new DefaultTableModel(); //表格数据模型
        tableY=new JTable(modelY); //表格采用数据模型
        tableY.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //关闭自动调整宽度
        tableY.setDefaultEditor(Object.class,null); //表格不可编辑
        JScrollPane tableScroll=new JScrollPane(tableY); //表格放入滚动面板
        yearRecordPanel.add(tableScroll,BorderLayout.CENTER);

        searchFieldY=new JTextField("此处输入文本可进行搜索",15);
        searchTypeY=new JComboBox<>(new String[]{"员工编号","员工姓名"});
        searchBtnY=new JButton("搜索");

        top.add(searchFieldY);
        top.add(searchTypeY);
        top.add(searchBtnY);

        updateYearRecord(); //更新年报
    }

    //下拉列表初始化
    private void ComboBoxInit()
    {
        //下拉列表的数据模型初始化
        yearModelD=new DefaultComboBoxModel<>();
        monthModelD=new DefaultComboBoxModel<>();
        dayModelD=new DefaultComboBoxModel<>();
        yearModelM=new DefaultComboBoxModel<>();
        monthModelM=new DefaultComboBoxModel<>();
        yearModelY=new DefaultComboBoxModel<>();

        Integer now[]=DateTimeUtil.now(); //获取当前时间的年、月、日、时、分、秒

        for(int i=now[0]-10;i<=now[0]+10;++i)
        { //获取当前时间前后十年的年份，添加到年下拉列表的数据模型中
            yearModelD.addElement(i);
            yearModelM.addElement(i);
            yearModelY.addElement(i);
        }
        yearComboBoxD=new JComboBox<>(yearModelD); //日报的年下拉列表
        yearComboBoxD.setSelectedItem(now[0]); //默认选中本年
        yearComboBoxM=new JComboBox<>(yearModelM); //月报的年下拉列表
        yearComboBoxM.setSelectedItem(now[0]);
        yearComboBoxY=new JComboBox<>(yearModelD); //年报的年下拉列表
        yearComboBoxY.setSelectedItem(now[0]);

        for(int i=1;i<=12;++i)
        { //遍历12个月，添加到月下拉列表的数据模型中
            monthModelD.addElement(i);
            monthModelM.addElement(i);
        }
        monthComboBoxD=new JComboBox<>(monthModelD); //日报的月下拉列表
        monthComboBoxD.setSelectedItem(now[1]); //默认选中本月
        monthComboBoxM=new JComboBox<>(monthModelM); //月报的月下拉列表
        monthComboBoxM.setSelectedItem(now[1]);

        updateDayModel(); //更新日下拉列表中的天数
        dayComboBoxD=new JComboBox<>(dayModelD); //日报的日下拉列表
        dayComboBoxD.setSelectedItem(now[2]); //默认选中本日
    }

    //更新日下拉列表
    private void updateDayModel()
    {
        int year=(int)yearComboBoxD.getSelectedItem(); //获取年下拉列表选中的值
        int month=(int)monthComboBoxD.getSelectedItem(); //获取月下拉列表选中的值
        int lastDay=DateTimeUtil.getLastDay(year,month); //获取选中月的最大天数
        dayModelD.removeAllElements(); //清除已有元素
        for(int i=1;i<=lastDay;++i)
        {
            dayModelD.addElement(i); //将每一天都添加到日下拉列表数据模型中
        }
    }

    //更新日报
    private void updateDayRecord()
    {
        //获取日报面板中选中的年、月、日
        int year=(int)yearComboBoxD.getSelectedItem();
        int month=(int)monthComboBoxD.getSelectedItem();
        int day=(int)dayComboBoxD.getSelectedItem();
        String report=HRService.getDayReport(year,month,day); //获取日报报表
        area.setText(report); //日报报表覆盖到文本域中
    }

    //更新月报
    private void updateMonthRecord()
    {
        //获取月报面板中选中的年、月
        int year=(int)yearComboBoxM.getSelectedItem();
        int month=(int)monthComboBoxM.getSelectedItem();
        int lastDay=DateTimeUtil.getLastDay(year,month); //此月最大天数

        String columnHeader[]=new String[lastDay+2]; //表格列头
        columnHeader[0]="员工编号";
        columnHeader[1]="员工姓名";
        for(int day=1;day<=lastDay;++day)
        {
            columnHeader[day+1]=year+"年"+month+"月"+day+"日";
        }

        //获取月报数据
        String[][] values=HRService.getMonthReport(year,month);
        modelM.setDataVector(values,columnHeader); //将数据和列头放入表格数据模型中
        int columnCount=tableM.getColumnCount(); //获取表格的所有列数
        for(int i=1;i<columnCount;++i)
        { //遍历每一列。从第2列开始，每一列都设为100宽度
            tableM.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
    }

    //更新年报
    private void updateYearRecord()
    {
        //获取年报面板中选中的年
        int year=(int)yearComboBoxY.getSelectedItem();

        String columnHeader[]=new String[12+2]; //表格列头
        columnHeader[0]="员工编号";
        columnHeader[1]="员工姓名";
        for(int month=1;month<=12;++month)
        {
            columnHeader[month+1]=year+"年"+month+"月";
        }

        //获取年报数据
        String[][] values=HRService.getYearReport(year);
        modelY.setDataVector(values,columnHeader); //将数据和列头放入表格数据模型中
        int columnCount=tableY.getColumnCount(); //获取表格的所有列数
        for(int i=1;i<columnCount;++i)
        { //遍历每一列。从第2列开始，每一列都设为200宽度
            tableY.getColumnModel().getColumn(i).setPreferredWidth(200);
        }
    }
}