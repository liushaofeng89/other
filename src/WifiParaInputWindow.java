import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

/**
 * 无线wifi名称、密码设置界面
 * @author songshengxi
 * @version 1.0.0 2013-12-1
 */
public class WifiParaInputWindow extends Component
{
    private static final long serialVersionUID = 1L;
    private JFrame jframe;
    private JPanel upPanel;
    private JPanel bottomPanel;

    private JLabel ssidLable;
    private JLabel pwdLable;
    private JLabel authorLable;

    private JTextField ssidText;
    private JTextField pwdText;

    private JButton okBtn;
    private JButton cancelBtn;

    private double screenWidth;
    private double screenHeight;

    /** 程序窗体大小 */
    private int WIDTH = 350;
    private int HEIGHT = 200;

    private BtnListener listener;

    /**
     * 构造方法
     */
    public WifiParaInputWindow()
    {
        this.jframe = new JFrame();
        this.upPanel = new JPanel();
        this.bottomPanel = new JPanel();
        this.ssidText = new JTextField();
        this.pwdText = new JTextField();
        this.ssidLable = new JLabel();
        this.ssidLable.setText("无线名称：");
        this.pwdLable = new JLabel();
        this.pwdLable.setText("无线密码：");
        this.okBtn = new JButton();
        this.okBtn.setText("创建wifi");
        this.cancelBtn = new JButton();
        this.cancelBtn.setText("取消");
        this.authorLable = new JLabel();
        this.authorLable.setText("   作者：宋胜曦");

        // 获取屏幕尺寸
        this.screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        this.screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        this.listener = new BtnListener(this);
        // 设置关闭窗体
        this.jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * 绘制界面
     */
    public void createContent()
    {
        jframe.setTitle("无线wifi创建工具");

        jframe.setLayout(new BorderLayout());
        jframe.setBounds((int) (screenWidth - WIDTH) / 2, (int) (screenHeight - HEIGHT) / 2, WIDTH, HEIGHT);

        // 设置样式java默认窗体样式
        // jframe.setUndecorated(true);
        // jframe.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);

        // 设置upPanel布局
        GroupLayout ulayout = new GroupLayout(upPanel);
        upPanel.setLayout(ulayout);

        ulayout.setAutoCreateGaps(true);
        ulayout.setAutoCreateContainerGaps(true);

        GroupLayout.SequentialGroup uhGroup = ulayout.createSequentialGroup();

        uhGroup.addGroup(ulayout.createParallelGroup(Alignment.CENTER).addComponent(ssidLable).addComponent(pwdLable));

        uhGroup.addGroup(ulayout.createParallelGroup().addComponent(ssidText).addComponent(pwdText));
        ulayout.setHorizontalGroup(uhGroup);

        GroupLayout.SequentialGroup uvGroup = ulayout.createSequentialGroup();

        uvGroup.addGroup(ulayout.createParallelGroup(Alignment.CENTER).addComponent(ssidLable).addComponent(ssidText));
        uvGroup.addGroup(ulayout.createParallelGroup(Alignment.CENTER).addComponent(pwdLable).addComponent(pwdText));
        ulayout.setVerticalGroup(uvGroup);

        // 设置bottomPanel布局
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(okBtn);
        bottomPanel.add(cancelBtn);

        jframe.add(upPanel, BorderLayout.NORTH);
        jframe.add(bottomPanel, BorderLayout.SOUTH);
        jframe.add(authorLable);
        jframe.setVisible(true);

        initListener();
    }

    /**
     * 创建监听
     */
    private void initListener()
    {
        okBtn.addMouseListener(listener);
        cancelBtn.addMouseListener(listener);
    }

    public JTextField getSsidText()
    {
        return ssidText;
    }

    public JTextField getPwdText()
    {
        return pwdText;
    }

    public BtnListener getListener()
    {
        return listener;
    }

}
