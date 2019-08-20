package cn.devkits.client.util;

public class DKStringUtil
{
    /**
     * IP address check
     * @param str the string need to check
     * @return is IP or not
     */
    public static boolean ipCheck(String str)
    {
        if (str == null || str.trim().isEmpty())
        {
            return false;
        }
        return str.matches("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");
    }
    
    public static void main(String[] args)
    {
       System.out.println(ipCheck("2.1.2.3")); 
    }
}
