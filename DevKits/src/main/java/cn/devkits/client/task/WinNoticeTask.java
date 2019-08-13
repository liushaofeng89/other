package cn.devkits.client.task;

import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.util.TimerTask;

public class WinNoticeTask extends TimerTask
{
    private TrayIcon trayIcon;
    private String title;
    private String content;

    public WinNoticeTask(TrayIcon trayIcon)
    {
        this.trayIcon = trayIcon;
        this.title = "重要提醒";
        this.content = "让我们健康、高效、快乐的工作...";
    }

    @Override
    public void run()
    {
        trayIcon.displayMessage(title, content, MessageType.INFO);
    }
}
