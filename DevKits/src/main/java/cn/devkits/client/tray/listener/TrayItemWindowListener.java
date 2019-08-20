package cn.devkits.client.tray.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import cn.devkits.client.tray.window.ServerPortsFrame;

public class TrayItemWindowListener implements ActionListener
{

    @Override
    public void actionPerformed(ActionEvent e)
    {
        ServerPortsFrame serverPortsWindow = new ServerPortsFrame();
        serverPortsWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverPortsWindow.setVisible(true);
    }

}
