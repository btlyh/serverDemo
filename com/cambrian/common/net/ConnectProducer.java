/** */
package com.cambrian.common.net;

import com.cambrian.common.log.Logger;
import com.cambrian.common.net.client.ClientConnectManager;
import com.cambrian.common.timer.TimerCenter;
import com.cambrian.common.timer.TimerEvent;
import com.cambrian.common.timer.TimerListener;

/**
 * ��˵���������ṩ�����ͻ������ӷ���ˣ�
 * 
 * @version 2013-5-9
 * @author HYZ (huangyz1988@qq.com)
 */
public class ConnectProducer implements TimerListener
{

	/** ��־ */
	private static final Logger log=Logger.getLogger(ConnectProducer.class);

	/** ������� */
	public static NioTcpConnect getConnect(URL url)
	{
		return ClientConnectManager.getConnect(url);
	}

	/** �����Ƿ�� */
	private boolean openning;
	/** URL */
	URL url;
	/** ���Ӷ��� */
	NioTcpConnect connect;
	/** ��������ʱ�� */
	TimerEvent connectEvent=new TimerEvent(this,"collate",3000);

	/** ���URL */
	public URL getURL()
	{
		return url;
	}
	/** ����URL */
	public void setURL(URL url)
	{
		this.url=url;
	}
	/** ������� */
	public NioTcpConnect getConnect()
	{
		return connect;
	}

	/** ��ʱ����ʼ */
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