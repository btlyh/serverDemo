/** */
package com.cambrian.common.net;

import com.cambrian.common.log.Logger;
import com.cambrian.common.net.client.ClientConnectManager;
import com.cambrian.common.timer.TimerCenter;
import com.cambrian.common.timer.TimerEvent;
import com.cambrian.common.timer.TimerListener;

/**
 * 类说明：连接提供器（客户端连接服务端）
 * 
 * @version 2013-5-9
 * @author HYZ (huangyz1988@qq.com)
 */
public class ConnectProducer implements TimerListener
{

	/** 日志 */
	private static final Logger log=Logger.getLogger(ConnectProducer.class);

	/** 获得连接 */
	public static NioTcpConnect getConnect(URL url)
	{
		return ClientConnectManager.getConnect(url);
	}

	/** 连接是否打开 */
	private boolean openning;
	/** URL */
	URL url;
	/** 连接对象 */
	NioTcpConnect connect;
	/** 连接整理定时器 */
	TimerEvent connectEvent=new TimerEvent(this,"collate",3000);

	/** 获得URL */
	public URL getURL()
	{
		return url;
	}
	/** 设置URL */
	public void setURL(URL url)
	{
		this.url=url;
	}
	/** 获得连接 */
	public NioTcpConnect getConnect()
	{
		return connect;
	}

	/** 定时器开始 */
	public void timerStart()
	{
		TimerCenter.getMinuteTimer().add(connectEvent);
	}

	public void onTimer(TimerEvent ev)
	{
		if(ev!=connectEvent) return;
		collate();
	}

	public void collate()
	{
		synchronized(this)
		{
			if(openning) return;
			openning=true;
		}
		try
		{
			if(connect==null||!connect.isActive())
			{
				connect=getConnect(url);
				if(log.isDebugEnabled())
					log.debug("getConnect, connect="+connect);
			}
		}
		catch(Exception e)
		{
			if(!log.isWarnEnabled()) log.warn("collate error, url="+url);
		}
		finally
		{
			openning=false;
		}

	}
}