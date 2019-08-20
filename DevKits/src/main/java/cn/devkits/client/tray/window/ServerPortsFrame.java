package cn.devkits.client.tray.window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cn.devkits.client.util.DKNetworkUtil;

public class ServerPortsFrame extends JFrame
{
    private static final long serialVersionUID = -6406148296636175804L;

    private static final int WINDOW_SIZE_WIDTH = 600;
    private static final int WINDOW_SIZE_HEIGHT = 400;
    private static final int PC_MAX_PORT = 65535;

    private JTextField addressInputField;

    private JButton searchBtn;

    private JTextArea userConsole;

    public ServerPortsFrame()
    {
        super("Server Ports Check");
        super.setSize(WINDOW_SIZE_WIDTH, WINDOW_SIZE_HEIGHT);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        super.setLocation((screenSize.width - WINDOW_SIZE_WIDTH) / 2, (screenSize.height - WINDOW_SIZE_HEIGHT) / 2);
    }

    @Override
    protected JRootPane createRootPane()
    {
        JRootPane jRootPane = new JRootPane();

        jRootPane.setLayout(new BorderLayout());

        JPanel northPane = new JPanel();
        northPane.setLayout(new BorderLayout());

        createInputTextField(northPane);
        createSearchBtn(northPane);

        this.userConsole = new JTextArea();
        userConsole.setEditable(false);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        scrollPane.setViewportView(userConsole);

        jRootPane.add(northPane, BorderLayout.NORTH);
        jRootPane.add(scrollPane, BorderLayout.CENTER);
        
        return jRootPane;
    }

    private void createSearchBtn(JPanel northPanel)
    {
        this.searchBtn = new JButton("查询");
        searchBtn.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                userConsole.append("start to check port on server " + addressInputField.getText() + " ..." + System.getProperty("line.separator"));
                startCheck(addressInputField.getText());
            }
        });
        northPanel.add(searchBtn, BorderLayout.EAST);
    }

    private void startCheck(final String address)
    {
        for (int port = 1; port <= PC_MAX_PORT; port++)
        {
            if (DKNetworkUtil.socketReachable(address, port))
            {
                userConsole.append("The port " + port + " is listening..." + System.getProperty("line.separator"));
            } else
            {
                userConsole.append("The address " + address + " with port " + port + " can not be reached!" + System.getProperty("line.separator"));
            }
            userConsole.update(userConsole.getGraphics());
            userConsole.setCaretPosition(userConsole.getText().length() - 1);
        }
    }

    private void createInputTextField(JPanel northPanel)
    {
        final String defaultText = "input domain or ip address please...";

        this.addressInputField = new JTextField(20);
        addressInputField.setText(defaultText);
        addressInputField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                JTextField textField = (JTextField) e.getSource();
                if (defaultText.equals(textField.getText()))
                {
                    textField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                JTextField textField = (JTextField) e.getSource();
                if (textField.getText().trim().isEmpty())
                {
                    textField.setText(defaultText);
                }
            }
        });
        addressInputField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                JTextField textField = (JTextField) e.getSource();
                // 判断按下的键是否是回车键
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    userConsole.append("start to check port on server " + textField.getText() + " ..." + System.getProperty("line.separator"));
                    startCheck(textField.getText());
                }
            }
        });

        northPanel.add(addressInputField, BorderLayout.CENTER);
    }

    @Override
    public boolean isResizable()
    {
        return false;
    }

}
