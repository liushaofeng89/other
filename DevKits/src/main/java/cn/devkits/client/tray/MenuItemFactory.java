package cn.devkits.client.tray;

import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.Toolkit;

import cn.devkits.client.util.DKNetworkUtil;

public class MenuItemFactory
{
    public static void createClipboardItem(Menu parentItem, MenuItemEnum itemType)
    {
        MenuItem menuItem = null;
        switch (itemType)
        {
            case USER_NAME:
                String userName = System.getProperty("user.name");

                menuItem = new MenuItem("User Name: " + userName);
                menuItem.addActionListener(new TrayItemClipboardListener(userName));
                break;
            case OS_NAME:
                String osName = System.getProperty("os.name") + " (" + System.getProperty("os.version") + ")";

                menuItem = new MenuItem("OS Name: " + osName);
                menuItem.addActionListener(new TrayItemClipboardListener(osName));
                break;
            case OS_ARCH:
                String osArch = System.getProperty("os.arch");

                menuItem = new MenuItem("OS ARCH: " + osArch);
                menuItem.addActionListener(new TrayItemClipboardListener(osArch));
                break;
            case CPU_ENDIAN:
                String endian = System.getProperty("sun.cpu.endian");

                menuItem = new MenuItem("CPU Endian: " + endian);
                menuItem.addActionListener(new TrayItemClipboardListener(endian));
                break;
            case SCREEN_SIZE:
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                String screenLabel = (int) screenSize.getWidth() + "*" + (int) screenSize.getHeight() + "(" + Toolkit.getDefaultToolkit().getScreenResolution() + "dpi)";
                
                menuItem = new MenuItem("Screen Size: " + screenLabel);
                menuItem.addActionListener(new TrayItemClipboardListener(screenLabel));
                break;
            case IP:
                String internetIp = DKNetworkUtil.getInternetIp();

                menuItem = new MenuItem(itemType.toString() + ": " + internetIp);
                menuItem.addActionListener(new TrayItemClipboardListener(internetIp));
                break;
            case MAC:
                String mac = DKNetworkUtil.getMac();

                menuItem = new MenuItem(itemType.toString() + ": " + mac);
                menuItem.addActionListener(new TrayItemClipboardListener(mac));
                break;

            default:
                break;
        }
        parentItem.add(menuItem);
    }

    public static void createComputeItem(Menu parentItem, MenuItemEnum itemType)
    {
        MenuItem menuItem = null;
        switch (itemType)
        {
            case MD5:
                menuItem = new MenuItem("MD5");
                menuItem.addActionListener(new TrayItemMD5Listener());
                break;

            default:
                break;
        }
        parentItem.add(menuItem);
    }
}
