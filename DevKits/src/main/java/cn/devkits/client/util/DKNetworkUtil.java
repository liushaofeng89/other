package cn.devkits.client.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DKNetworkUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DKNetworkUtil.class);

    /**
     * 获得内网IP
     * @return 内网IP
     */
    public static String getIntranetIp()
    {
        try
        {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e)
        {
            LOGGER.error("get local host address error!");
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得外网IP
     * @return 外网IP
     */
    public static String getInternetIp()
    {
        try
        {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            Enumeration<InetAddress> addrs;
            while (networks.hasMoreElements())
            {
                addrs = networks.nextElement().getInetAddresses();
                while (addrs.hasMoreElements())
                {
                    ip = addrs.nextElement();
                    if (ip != null && ip instanceof Inet4Address && ip.isSiteLocalAddress() && !ip.getHostAddress().equals(getIntranetIp()))
                    {
                        return ip.getHostAddress();
                    }
                }
            }

            // 如果没有外网IP，就返回内网IP
            return getIntranetIp();
        } catch (Exception e)
        {
            LOGGER.error("get internet ip address error!");
            throw new RuntimeException(e);
        }
    }

    public static String getMac()
    {
        Enumeration<NetworkInterface> el;
        String macs = "";
        try
        {
            el = NetworkInterface.getNetworkInterfaces();
            while (el.hasMoreElements())
            {
                byte[] mac = el.nextElement().getHardwareAddress();
                if (mac == null)
                    continue;
                macs = hexByte(mac[0]) + "-" + hexByte(mac[1]) + "-" + hexByte(mac[2]) + "-" + hexByte(mac[3]) + "-" + hexByte(mac[4]) + "-" + hexByte(mac[5]);
            }
        } catch (SocketException e1)
        {
            LOGGER.error("get mac address error!");
        }
        return macs;
    }

    public static String hexByte(byte b)
    {
        String s = "000000" + Integer.toHexString(b);
        return s.substring(s.length() - 2).toUpperCase();
    }

}