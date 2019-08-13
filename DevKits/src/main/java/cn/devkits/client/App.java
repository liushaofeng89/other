package cn.devkits.client;

import java.awt.AWTException;
import java.awt.Menu;
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

import cn.devkits.client.task.WinNoticeTask;
import cn.devkits.client.tray.MenuItemFactory;
import cn.devkits.client.tray.MenuItemEnum;

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
                trayIcon.setToolTip("开发工具包\r\n官网：www.devkits.cn");

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
        PopupMenu popupMenu = new PopupMenu();

        Menu myComputerItem = new Menu("我的电脑");

        MenuItemFactory.createItem(myComputerItem, MenuItemEnum.USER_NAME);
        MenuItemFactory.createItem(myComputerItem, MenuItemEnum.OS_NAME);
        MenuItemFactory.createItem(myComputerItem, MenuItemEnum.OS_ARCH);
        MenuItemFactory.createItem(myComputerItem, MenuItemEnum.CPU_ENDIAN);
        MenuItemFactory.createItem(myComputerItem, MenuItemEnum.IP);
        MenuItemFactory.createItem(myComputerItem, MenuItemEnum.MAC);

        popupMenu.add(myComputerItem);

        popupMenu.addSeparator();
        popupMenu.add(new MenuItem("关于..."));
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
