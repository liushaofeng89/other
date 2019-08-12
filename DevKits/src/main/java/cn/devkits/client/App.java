package cn.devkits.client;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args)
    {
        if (SystemTray.isSupported())
        {
            try
            {
                TrayIcon trayIcon = new TrayIcon(ImageIO.read(App.class.getClassLoader().getResource("20.png")));
                trayIcon.setImageAutoSize(true);
                // 添加工具提示文本
                trayIcon.setToolTip("本地连接\r\n速度：100.0 Mbps\r\n状态：已连接上");

                initContextMenu(trayIcon);
                initDbClick(trayIcon);
                initNotice(trayIcon);

                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e)
            {
                logger.error("初始化托盘功能失败！");
            } catch (IOException e)
            {
                logger.error("托盘图标加载失败！");
            }
        } else
        {
            logger.error("系统不支持托盘菜单！");
        }
    }

    private static void initContextMenu(TrayIcon trayIcon)
    {
        // 创建弹出菜单
        PopupMenu popupMenu = new PopupMenu();
        popupMenu.add(new MenuItem("禁用(D)"));
        popupMenu.add(new MenuItem("状态(S)"));
        popupMenu.add(new MenuItem("修复(P)"));
        popupMenu.addSeparator();
        popupMenu.add(new MenuItem("更改 Windows 防火墙设置(C)"));
        popupMenu.addSeparator();
        MenuItem quit = new MenuItem("退出");
        quit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        popupMenu.add(quit);

        // 为托盘图标加弹出菜弹
        trayIcon.setPopupMenu(popupMenu);
    }

    private static void initDbClick(TrayIcon trayIcon)
    {
        trayIcon.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                // 判断是否双击了鼠标
                if (e.getClickCount() == 2)
                {
                    JOptionPane.showMessageDialog(null, "待开放");
                }
            }
        });
    }

    private static void initNotice(TrayIcon trayIcon)
    {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new WinNoticeTask(trayIcon), 500, 1000 * 60 * 30);// 半小时执行一次
    }
}
