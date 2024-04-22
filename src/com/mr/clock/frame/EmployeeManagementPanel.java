//窗体类——员工管理面板
package com.mr.clock.frame;

import com.mr.clock.pojo.Employee;
import com.mr.clock.service.HRService;
import com.mr.clock.session.Session;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

public class EmployeeManagementPanel extends JPanel
{
    private static final File FACE_DIR=new File("src/faces"); //存放员工照片文件的文件夹
    private static final String SUFFIX="png"; //图像文件的默认格式
    private MainFrame parent; //主窗体
    private JTable table; //员工信息表格
    private DefaultTableModel model; //表格的数据模型
    private JButton addBtn; //录入新员工按钮
    private JButton deleteBtn; //删除员工按钮
    private JButton backBtn; //返回按钮
    private JTextField searchField; //搜索框
    private JComboBox<String> searchType; //搜索方式下拉列表框
    private JButton searchBtn; //搜索按钮

    public EmployeeManagementPanel(MainFrame parent)
    {
        this.parent=parent;
        init(); //组件初始化
        addListener(); //为组件添加监听
    }

    //组件初始化
    private void init()
    {
        parent.setTitle("员工管理"); //修改标题
        addBtn=new JButton("录入新员工");
        deleteBtn=new JButton("删除员工");
        backBtn=new JButton("返回");

        model=new DefaultTableModel();

        String columnName[]={"员工编号","员工名称","员工照片"}; //表格列头
        int count=Session.EMP_SET.size(); //员工人数
        Object value[][]=new Object[count][3]; //表格展示的员工编号与姓名

        //创建员工结合的迭代器
        Iterator<Employee> iter=Session.EMP_SET.iterator();
        for(int i=0;iter.hasNext();++i)
        { //遍历所有员工
            Employee emp=iter.next(); //获取一个员工
            value[i][0]=String.valueOf(emp.getId()); //第一列为员工编号
            value[i][1]=emp.getName(); //第二列为员工姓名
            value[i][2]=Session.IMAGE_MAP.get(emp.getCode());
        }

        model=new DefaultTableModel(value,columnName); //创建表格的数据模型

        table=new EmpTable(model); //创建表格
        table.setRowHeight(100);

        // 创建自定义的TableCellRenderer对象
        TableCellRenderer renderer=new PhotoRenderer();
        // 设置第3列的渲染器为自定义的TableCellRenderer对象
        table.getColumnModel().getColumn(2).setCellRenderer(renderer);

        JScrollPane scroll=new JScrollPane(table); //表格放入滚动面板

        setLayout(new BorderLayout()); //采用边界布局
        add(scroll,BorderLayout.CENTER); //滚动面板中部

        JPanel bottom=new JPanel(); //底部面板
        bottom.add(addBtn); //添加底部组件
        bottom.add(deleteBtn);
        bottom.add(backBtn);
        add(bottom,BorderLayout.SOUTH);

        JPanel top=new JPanel(); //顶部面板
        searchField=new JTextField("此处输入文本可进行搜索",15);
        searchType=new JComboBox<>(new String[]{"员工编号","员工姓名"});
        searchBtn=new JButton("搜索");
        top.add(searchField);
        top.add(searchType);
        top.add(searchBtn);
        add(top,BorderLayout.NORTH);
    }

    private class EmpTable extends JTable
    {
        public EmpTable(TableModel tm)
        {
            super(tm);
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //只能单选
        }

        @Override
        public boolean isCellEditable(int row,int column)
        {
            return false; //表格不可编辑
        }

        @Override
        public Class<?> getColumnClass(int col)
        {
            if(col==2)
            {
                return BufferedImage.class;
            }
            return super.getColumnClass(col);
        }

        @Override
        public TableCellRenderer getDefaultRenderer(Class<?> columnClass)
        {
            DefaultTableCellRenderer cr=(DefaultTableCellRenderer)super.getDefaultRenderer(columnClass); //获取单元格渲染对象
            cr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER); //表格文字居中显示
            return cr;
        }
    }

    public static class ImageScaler
    { //图片拉伸
        public static BufferedImage scaledImage(BufferedImage source,int targetWidth,int targetHeight)
        { //图片拉伸方法
            BufferedImage scaledImage=new BufferedImage(targetWidth,targetHeight,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d=scaledImage.createGraphics();

            //计算水平和垂直方向上的缩放比例
            double scaleX=(double)targetWidth/source.getWidth();
            double scaleY=(double)targetHeight/source.getHeight();

            //仿射变换类AffineTransform类表示2D仿射变换，它执行从2D坐标到其他2D坐标的线性映射，保留了线的“直线性”和“平行性”
            AffineTransform transform=AffineTransform.getScaleInstance(scaleX,scaleY);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR); //设置渲染提示，指定图形的插值算法为双线性插值
            g2d.drawImage(source,transform, null); //将源图像根据指定的仿射变换对象transform进行缩放，并绘制到目标图像上。

            g2d.dispose(); //释放g2d使用的系统资源
            return scaledImage;
        }
    }

    private class PhotoRenderer extends JLabel implements TableCellRenderer
    {
        public PhotoRenderer()
        {
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            //将value值（即BufferedImage对象）渲染成照片
            BufferedImage image=(BufferedImage)value;
            BufferedImage scaledImage=ImageScaler.scaledImage(image,100,100); //照片缩小成100×100
            ImageIcon icon=new ImageIcon(scaledImage);
            setIcon(icon);
            return this;
        }
    }

    //为组件添加监听
    private void addListener()
    {
        addBtn.addActionListener(new ActionListener()
        { //录入新员工按钮事件
            public void actionPerformed(ActionEvent e)
            { //主窗体切换到添加新员工面板
                parent.setPanel(new AddEmployeePanel(parent));
            }
        });
        deleteBtn.addActionListener(new ActionListener()
        { //删除员工按钮事件
            public void actionPerformed(ActionEvent e)
            {
                int selectRow=table.getSelectedRow(); //获取表格选中的行索引
                if(selectRow!=-1)
                { //若有行被选中
                    int deleteCode= JOptionPane.showConfirmDialog(parent,"确定删除该员工？","提示！",JOptionPane.YES_NO_OPTION);
                    if(deleteCode==JOptionPane.YES_OPTION)
                    { //若用户选择确定
                        String id=(String)model.getValueAt(selectRow,0); //获取选中员工的编号
                        HRService.deleteEmp(Integer.parseInt(id)); //删除此员工
                        model.removeRow(selectRow); //删除此行
                    }
                }
            }
        });
        backBtn.addActionListener(new ActionListener()
        { //返回按钮事件
            public void actionPerformed(ActionEvent e)
            {
                parent.setPanel(new MainPanel(parent));
            }
        });

        searchBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                String text = searchField.getText(); //获取搜索框输入的内容
                String type = (String) searchType.getSelectedItem(); //获取选中的搜索方式
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
                        table.setRowSelectionInterval(i,i); //选中i行column列的表格
                        table.scrollRectToVisible(table.getCellRect(i,column,true)); //滚动至i行column列所在表格的位置
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
}