import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JTextField;

public class Utils
{
    public static Dimension dms;

    /*
     * 构造方法
     */
    public Utils()
    {

    }

    /*
     * 获取屏幕分辨率
     */
    public Dimension getScreenSize()
    {

        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        dms = new Dimension((int) width, (int) height);
        return dms;
    }

    /*
     * 给文本框赋值
     */
    public boolean setValues(JTextField jtf, String value)
    {
        jtf.setText(value);
        return true;
    }

}
