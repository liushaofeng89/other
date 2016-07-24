
public class Constant 
{
	
	//主窗体标题
	public static final String TITLE="属性文件比较修改器";
	
	//比较文件按钮TEXT
	public static final String BTN_COMPAREFIlE = "开始比较";
	
	//取消按钮TEXT
	public static final String BTN_CANCEL = "取消";
	
	//浏览按钮TEXT
	public static final String BTN_BROWSE = "浏览...";
	
	//基准文件标签TEXT
	public static final String LABEL_BASEFILE = "基准文件:";
	
	//扩展文件标签TEXT
	public static final String LABEL_EXPANDFILE = "扩展文件:";
	
	//按钮宽度
	public static final int BTN_WIDTH = 13;
	
	//按钮高度
	public static final int BTN_HEIGHT = 20;
	
	//主窗体宽度
	public static final int FRAME_WIDTH = 400;
	
	//主窗体高度
	public static final int FRAME_HEIGHT = 320;
	
	//文件路径域的列数
	public static final int TEXTFIELD_COLUMNS = 5;
	
	//拷贝内容文本域宽度
	public static final int TEXTAREA_WIDTH = 10;
	
	//拷贝内容文本域宽度
	public static final int TEXTAREA_HEIGHT = 35;
	
	//文本域初始文字
	public static final String TEXTAREA_TEXT_DEFAULT = "拷贝已经准备就绪……";
	
	//文件不需要更新时文本域初始文字
	public static final String TEXTAREA_TEXT = "文件不需要拷贝……";
	
	//屏幕宽度
	public static int SCR_WIDTH = (int)new Utils().getScreenSize().getWidth();
	
	//屏幕高度
	public static int SCR_HEIGHT = (int)new Utils().getScreenSize().getHeight();
	
		
	/*
	 * 构造方法
	 */
	public Constant()
	{
		
	}
}
