import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * 创建无线wifi创建按钮监听
 * @author songshengxi
 * @version 1.0.0 2013-12-1
 */
public class BtnListener extends MouseAdapter
{
    private WifiParaInputWindow window;

    private String cmd;
    private static String startWifiCmd;

    /**
     * 构造方法
     * @param wifiParaInputWindow
     */
    public BtnListener(WifiParaInputWindow wifiParaInputWindow)
    {
        this.window = wifiParaInputWindow;
        this.startWifiCmd = "netsh wlan start hostednetwork";
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

        JButton btn = (JButton) e.getSource();
        // 点击的是"创建wifi"按钮还是"取消按钮"
        if (btn.getText().equals("创建wifi"))
        {
            okBtnClick();
        }
        else
        {
            cancelBtnClick();
        }

    }

    private void cancelBtnClick()
    {
        System.exit(0);
    }

    private void okBtnClick()
    {
        String ssid = window.getSsidText().getText();
        String pwd = window.getPwdText().getText();

        if (ssid.isEmpty() || pwd.isEmpty())
        {
            JOptionPane.showMessageDialog(window, "wifi名字与密码不能为空!!!", "创建wifi建错误", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            cmd = "netsh wlan set hostednetwork mode=allow ssid=" + ssid + "key=" + pwd;
            startWifi(cmd);
        }

    }

    private void startWifi(String cmd)
    {
        if (cmd == null || cmd.isEmpty())
        {
            JOptionPane.showMessageDialog(window, "wifi创建失败!!!", "创建wifi建错误", JOptionPane.ERROR_MESSAGE);
        }
        try
        {
            Runtime.getRuntime().exec(cmd);
            Runtime.getRuntime().exec(startWifiCmd);
            JOptionPane.showMessageDialog(window, "wifi创建成功!!!", "创wifi成功", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(window, "wifi创建失败!!!", "创建wifi建错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
