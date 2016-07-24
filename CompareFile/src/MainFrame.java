import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author liushaofeng
 */
public class MainFrame extends JFrame
{

    private static final long serialVersionUID = 8783743950584656446L;

    private JFrame jframe;

    // 上部分容器
    private JPanel upPanel;

    // 中间容器
    private JPanel middlePanel;

    // 下部分容器
    private JPanel bottomPanel;

    // 基本属性文件浏览标签
    private JLabel baseFileLabel;

    // 扩展属性文件浏览标签
    private JLabel expandFileLabel;

    // 属性文件浏览窗口
    private JFileChooser browseDialog;

    // 基本属性文件浏览按钮
    private JButton baseFileBrowseBtn;

    // 扩展属性文件浏览按钮
    private JButton expandFileBrowseBtn;

    // 基本属性文件地址文本域
    private JTextField baseFileTextField;

    // 扩展属性文件地址文本域
    private JTextField expandFileTextField;

    // 滚动条容器
    private JScrollPane scrollPane;

    // 现实拷贝详情
    private JTextArea copiedContent;

    // 比较按钮
    private JButton compareFileBtn;

    // 取消按钮
    private JButton cancelBtn;

    private Properties baseFileProperties;
    private Properties expandFileProperties;

    // 工具类
    private Utils util;

    // map的键
    private String key;

    // map的值
    private String value;

    /**
     * MainFrame的构造方法
     */
    public MainFrame()
    {
        this.jframe = new JFrame();
        this.upPanel = new JPanel();
        this.middlePanel = new JPanel();
        this.bottomPanel = new JPanel();
        this.copiedContent = new JTextArea(Constant.TEXTAREA_TEXT_DEFAULT, Constant.TEXTAREA_WIDTH,
            Constant.TEXTAREA_HEIGHT);
        this.copiedContent.setEditable(false);
        this.scrollPane = new JScrollPane(copiedContent);
        this.baseFileBrowseBtn = new JButton(Constant.BTN_BROWSE);
        this.expandFileBrowseBtn = new JButton(Constant.BTN_BROWSE);
        this.browseDialog = new JFileChooser();
        this.baseFileLabel = new JLabel(Constant.LABEL_BASEFILE);
        this.expandFileLabel = new JLabel(Constant.LABEL_EXPANDFILE);
        this.baseFileTextField = new JTextField(Constant.TEXTFIELD_COLUMNS);
        this.expandFileTextField = new JTextField(Constant.TEXTFIELD_COLUMNS);
        this.compareFileBtn = new JButton(Constant.BTN_COMPAREFIlE);
        this.cancelBtn = new JButton(Constant.BTN_CANCEL);

        this.util = new Utils();

        this.baseFileProperties = new Properties();
        this.expandFileProperties = new Properties();
    }

    /**
     * 设置按钮监听事件
     */
    private void addHandler()
    {
        BrowseFileMonitor bfm = new BrowseFileMonitor();
        CompareAndCancelMonitor cacm = new CompareAndCancelMonitor();

        // 给按钮添加监听事件
        baseFileBrowseBtn.setActionCommand("baseFileBtn");
        expandFileBrowseBtn.setActionCommand("expandFileBtn");
        compareFileBtn.setActionCommand("compareFileBtn");
        cancelBtn.setActionCommand("cancelBtn");
        baseFileBrowseBtn.addActionListener(bfm);
        expandFileBrowseBtn.addActionListener(bfm);
        compareFileBtn.addActionListener(cacm);
        cancelBtn.addActionListener(cacm);

    }

    /**
     * 设置窗体的布局和样式
     */
    public void doLayout()
    {
        // 设置窗体位置
        jframe = new JFrame(Constant.TITLE);
        jframe.setLayout(new BorderLayout());
        jframe.setBounds((Constant.SCR_WIDTH - Constant.FRAME_WIDTH) / 2,
            (Constant.SCR_HEIGHT - Constant.FRAME_HEIGHT) / 2, Constant.FRAME_WIDTH,
            Constant.FRAME_HEIGHT);

        // 设置样式
        jframe.setUndecorated(true);
        jframe.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);

        // 文本域设置自动换行
        copiedContent.setLineWrap(true);

        // 设置文件路径框不可编辑
        baseFileTextField.setEditable(false);
        expandFileTextField.setEditable(false);

        // 设置智能获取properties为扩展名的文件
        FileNameExtensionFilter filter = new FileNameExtensionFilter("properties格式的文件",
            "properties");
        browseDialog.setFileFilter(filter);

        // 设置upPanel布局
        GroupLayout ulayout = new GroupLayout(upPanel);
        upPanel.setLayout(ulayout);

        ulayout.setAutoCreateGaps(true);
        ulayout.setAutoCreateContainerGaps(true);

        GroupLayout.SequentialGroup uhGroup = ulayout.createSequentialGroup();

        uhGroup.addGroup(ulayout.createParallelGroup(Alignment.CENTER).addComponent(baseFileLabel)
            .addComponent(expandFileLabel));

        uhGroup.addGroup(ulayout.createParallelGroup().addComponent(baseFileTextField)
            .addComponent(expandFileTextField));
        ulayout.setHorizontalGroup(uhGroup);

        uhGroup.addGroup(ulayout.createParallelGroup().addComponent(baseFileBrowseBtn)
            .addComponent(expandFileBrowseBtn));
        ulayout.setHorizontalGroup(uhGroup);

        GroupLayout.SequentialGroup uvGroup = ulayout.createSequentialGroup();

        uvGroup.addGroup(ulayout.createParallelGroup(Alignment.CENTER).addComponent(baseFileLabel)
            .addComponent(baseFileTextField).addComponent(baseFileBrowseBtn));
        uvGroup.addGroup(ulayout.createParallelGroup(Alignment.CENTER)
            .addComponent(expandFileLabel).addComponent(expandFileTextField).addComponent(
                expandFileBrowseBtn));
        ulayout.setVerticalGroup(uvGroup);

        // 设置中间容器
        middlePanel.setLayout(new FlowLayout());
        middlePanel.add(scrollPane);

        // 设置bottomPanel布局
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(compareFileBtn);
        bottomPanel.add(cancelBtn);

        jframe.add(upPanel, BorderLayout.NORTH);
        jframe.add(middlePanel, BorderLayout.CENTER);
        jframe.add(bottomPanel, BorderLayout.SOUTH);
        jframe.setVisible(true);

    }

    /**
     * 程序启动入口
     */
    public static void main(String[] args)
    {
        MainFrame mf = new MainFrame();
        mf.doLayout();
        mf.addHandler();
    }

    /**
     * 给浏览按钮添加事件监听,根据Buttom的ActionCommand不同来获取对应的分支操作
     */
    class BrowseFileMonitor implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {

            int returnVal = browseDialog.showOpenDialog(upPanel);
            Utils util = new Utils();
            if ("baseFileBtn".equals(e.getActionCommand()))
            {
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    util.setValues(baseFileTextField, browseDialog.getSelectedFile()
                        .getAbsolutePath());
                }
            }
            else if ("expandFileBtn".equals(e.getActionCommand()))
            {
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    util.setValues(expandFileTextField, browseDialog.getSelectedFile()
                        .getAbsolutePath());
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "系统错误！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 给比较和取消按钮添加事件监听,根据Buttom的ActionCommand不同来获取对应的分支操作
     */
    class CompareAndCancelMonitor implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if ("compareFileBtn".equals(e.getActionCommand()))
            {
                // 避免文件路径为空的情况
                if (baseFileTextField.getText().length() > 0
                    && expandFileTextField.getText().length() > 0)
                {
                    DoFileThread dofilethread = new DoFileThread();
                    dofilethread.start();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "文件路径不能有空！", "错误",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            else if ("cancelBtn".equals(e.getActionCommand()))
            {
                System.exit(0);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "系统错误！", "错误", JOptionPane.ERROR_MESSAGE);
            }

        }

    }

    /**
     * 比较两个文件的异同，以基准文件为准
     * @param path0：基准文件路径
     * @param path1：扩展文件路径
     */
    public void compareFile(String path0, String path1)
    {
        Map<String, String> baseFileMap = new HashMap<String, String>();
        Map<String, String> expandFileMap = new HashMap<String, String>();

        try
        {
            // 读取配置文件
            baseFileProperties.load(new FileReader(path0));
            expandFileProperties.load(new FileReader(path1));
        }
        catch (FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, "对不起，文件路径不正确！", "错误", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "系统IO异常！", "错误", JOptionPane.ERROR_MESSAGE);
        }

        // 遍历存放基准文件
        Set<Object> baseSet = baseFileProperties.keySet();
        for (Object object : baseSet)
        {
            String baseKey = object.toString();
            String baseValue = new String(baseFileProperties.getProperty(baseKey));
            baseFileMap.put(baseKey, baseValue);
        }

        // 遍历存放扩展文件
        Set<Object> expandSet = expandFileProperties.keySet();
        for (Object object : expandSet)
        {
            String expandKey = object.toString();
            String expandValue = new String(expandFileProperties.getProperty(expandKey));
            expandFileMap.put(expandKey, expandValue);
        }

        Set<String> set = baseFileMap.keySet();
        Iterator<String> it = set.iterator();
        int i = 0, j = 0;
        while (it.hasNext())
        {
            j++;
            key = it.next();
            value = baseFileMap.get(key);
            // 如果扩展文件包含这个键
            if (expandFileMap.containsKey(key))
            {
                // 在键相等的请款下判断值是否也相等
                if (expandFileMap.get(key).equals(value))
                {
                    i++;
                }
                else
                {
                    // 去掉原文件中的键值，添加新的键值
                }
                // 判断是否键值对是否全等
                if (i == set.size())
                {
                    copiedContent.setText(Constant.TEXTAREA_TEXT);
                }
            }
            else
            {
                writeToFile(path1, key, value, j, set.size());
            }
        }
    }

    /**
     * 数据写回扩展配置文件
     * @param path:写回文件的路径
     * @param key：写回的键
     * @param value：写回的值
     * @param k：遍历了第K个
     * @param total：需要遍历总的个数
     */
    public void writeToFile(String path, String key, String value, int k, int total)
    {
        // 将不匹配的内容更新到扩展文件中去
        try
        {
            FileWriter writer = new FileWriter(path, true);
            writer.write("\n" + key + "=" + value);
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        showInfo(key, value, k, total);
    }

    /**
     * 将要添加的数据显示到界面中去
     * @param str0:写回的键
     * @param str1:写回的值
     * @param i：遍历了第K个
     * @param total：需要遍历总的个数
     */
    public void showInfo(String str0, String str1, int i, int total)
    {
        // 如果是最后一个
        if (i == total)
        {
            copiedContent.setText("完成复制:" + str0 + "=" + str1 + "\n" + copiedContent.getText());
            JOptionPane.showMessageDialog(null, "配置文件已经更新完毕！", "更新成功",
                JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            copiedContent.setText("完成复制:" + str0 + "=" + str1 + "\n" + copiedContent.getText());
        }
    }

    /**
     * 创建一个线程来比较文件并显示到文本域中去
     */
    public class DoFileThread extends Thread
    {
        @Override
        public void run()
        {
            compareFile(baseFileTextField.getText(), expandFileTextField.getText());
        }
    }
}
