package com.cambrian.common.net.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

import com.cambrian.common.log.Logger;
import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.ConnectArray;
import com.cambrian.common.net.DataAccessException;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.common.net.ProxyDataHandler;
import com.cambrian.common.net.TransmitHandler;
import com.cambrian.common.net.URL;
import com.cambrian.common.thread.TaskPoolExecutor;
import com.cambrian.common.thread.ThreadKit;
import com.cambrian.common.util.ChangeListenerList;

/**
 * 类说明：连接工厂
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class ClientConnectManager extends ChangeListenerList implements
	Runnable
{

	/* static fields */
	/** 日志 */
	private static Logger log=Logger.getLogger(ClientConnectManager.class);
	protected static ClientConnectManager factory=new ClientConnectManager();

	public static final int RUN_TIME=20;
	public static final int PING_TIME=20000;
	public static final int COLLATE_TIME=30000;

	private static final String err2=ClientConnectManager.class.getName()
		+" openTcpConnect, register error";

	/* static methods */

	public static ClientConnectManager getFactory()
	{
		return factory;
	}
	public static NioTcpConnect checkConnect(URL paramURL)
	{
		if(factory==null)
		{
			NioTcpConnect localConnect=new NioTcpConnect();
			localConnect.open(paramURL);
			return localConnect;
		}
		return factory.checkInstance(paramURL);
	}

	public static NioTcpConnect getConnect(URL paramURL)
	{
		if(factory==null)
		{
			NioTcpConnect localConnect=new NioTcpConnect();
			localConnect.open(paramURL);
			return localConnect;
		}
		return factory.getInstance(paramURL);
	}

	public static NioTcpConnect openConnect(URL paramURL)
	{
		if(factory==null)
		{
			NioTcpConnect localConnect=new NioTcpConnect();
			localConnect.open(paramURL);
			return localConnect;
		}
		return factory.openTcpConnect(paramURL);
	}

	/* fields */
	ConnectArray connectArray=new ConnectArray();
	ProxyDataHandler handler;
	TaskPoolExecutor executor=new TaskPoolExecutor();
	boolean serialExecute;
	int sendBufferSize=131072;
	int rBufferSize=2048;
	int wBufferSize=2048;
	int maxDataLength=129024;
	int dataBufferSize=32;
	int runTime=20;
	int pingTime=20000;
	int collateTime=30000;
	Selector selector;
	long lastPingTime;
	long lastCollateTime;

	/* constructors */

	/* properties */
	public int size()
	{
		return this.connectArray.size();
	}

	public NioTcpConnect[] getConnects()
	{
		return this.connectArray.getArray();
	}

	public void setTransmitHandler(ProxyDataHandler paramTransmitHandler)
	{
		this.handler=paramTransmitHandler;
	}

	public TaskPoolExecutor getExecutor()
	{
		return this.executor;
	}

	public void setExecutor(TaskPoolExecutor paramExecutor)
	{
		this.executor=paramExecutor;
	}

	public boolean isSerialExecute()
	{
		return this.serialExecute;
	}

	public void setSerialExecute(boolean paramBoolean)
	{
		this.serialExecute=paramBoolean;
	}

	public int getSendBufferSize()
	{
		return this.sendBufferSize;
	}

	public void setSendBufferSize(int paramInt)
	{
		this.sendBufferSize=paramInt;
	}

	public int getRBufferSize()
	{
		return this.rBufferSize;
	}

	public void setRBufferSize(int paramInt)
	{
		this.rBufferSize=paramInt;
	}

	public int getWBufferSize()
	{
		return this.wBufferSize;
	}

	public void setWBufferSize(int paramInt)
	{
		this.wBufferSize=paramInt;
	}

	public int getMaxDataLength()
	{
		return this.maxDataLength;
	}

	public void setMaxDataLength(int paramInt)
	{
		this.maxDataLength=paramInt;
	}

	public int getDataBufferSize()
	{
		return this.dataBufferSize;
	}

	public void setDataBufferSize(int paramInt)
	{
		this.dataBufferSize=paramInt;
	}

	public int getRunTime()
	{
		return this.runTime;
	}

	public void setRunTime(int paramInt)
	{
		this.runTime=paramInt;
	}

	public int getPingTime()
	{
		return this.pingTime;
	}

	public void setPingTime(int paramInt)
	{
		this.pingTime=paramInt;
	}

	public int getCollateTime()
	{
		return this.collateTime;
	}

	public void setCollateTime(int paramInt)
	{
		this.collateTime=paramInt;
	}
	/* init start */

	/* methods */
	public NioTcpConnect checkInstance(URL paramURL)
	{
		NioTcpConnect[] arrayOfConnect=this.connectArray.getArray();
		for(int i=arrayOfConnect.length-1;i>=0;--i)
		{
			NioTcpConnect localConnect=arrayOfConnect[i];
			if(!(localConnect.isActive()))
			{
				this.connectArray.remove(localConnect);
				if(log.isDebugEnabled())
					log.debug("checkInstance, connect close, "+localConnect);
			}
			else
			{
				URL localURL=localConnect.getURL();
				if(localURL.getProtocol().equals(paramURL.getProtocol()))
					if(localURL.getHost().equals(paramURL.getHost()))
						if(localURL.getPort()==paramURL.getPort())
							if((!("http".equals(localURL.getProtocol())))
								||(localURL.getFilePath().equals(paramURL
									.getFilePath()))) return localConnect;
			}
		}
		return null;
	}

	public NioTcpConnect getInstance(URL paramURL)
	{
		NioTcpConnect localConnect=checkInstance(paramURL);
		if(localConnect!=null) return localConnect;
		localConnect=openTcpConnect(paramURL);
		this.connectArray.add(localConnect);
		return localConnect;
	}

	public NioTcpConnect openTcpConnect(URL url)
	{
		if(log.isInfoEnabled()) log.info("openInstance="+url);
		NioTcpConnect connect=new NioTcpConnect();
		connect.setTransmitHandler(handler);
		connect.setChangeListener(this);
		connect.setExecutor(executor);
		connect.setSerialExecute(serialExecute);
		connect.setSendBufferSize(sendBufferSize);
		connect.setRBufferSize(rBufferSize);
		connect.setWBufferSize(wBufferSize);
		connect.setMaxDataLength(maxDataLength);
		connect.setDataBufferSize(dataBufferSize);
		connect.open(url);
		try
		{
			if(selector==null) selector=Selector.open();
			SelectionKey skey=connect.getSocketChannel()
				.register(selector,1);
			skey.attach(connect);
		}
		catch(Exception exception)
		{
			if(log.isWarnEnabled())
				log.warn("openTcpConnect error, register fail, "+connect,
					exception);
			connect.close();
			throw new DataAccessException(420,err2,null,url.getString());
		}
		return connect;
	}

	public void loop()
	{
		if(log.isInfoEnabled()) log.info("loop, "+this);
		while(true)
		{
			run();
			ThreadKit.delay(runTime);
		}
	}

	public void run()
	{
		listen();
		long time=System.currentTimeMillis();
		if((pingTime>0)&&(time-lastPingTime>pingTime))
		{
			heart(time);
			lastPingTime=time;
		}
		if((collateTime>0)&&(time-lastCollateTime>collateTime))
		{
			collate(time);
			lastCollateTime=time;
		}
	}
	/** 一直等到消息 */
	public void listen()
	{
		try
		{
			if((selector==null)||(selector.selectNow()<=0)) return;
			if(log.isTraceEnabled()) log.trace("listen, "+this);
			Set<?> set=selector.selectedKeys();
			Iterator<?> iterator=set.iterator();
			while(iterator.hasNext())
			{
				SelectionKey selectionKey=(SelectionKey)iterator.next();
				iterator.remove();
				int i=selectionKey.readyOps();
				if((i&0x1)==0) continue;
				NioTcpConnect connect=(NioTcpConnect)selectionKey
					.attachment();
				connect.receive();
			}
		}
		catch(ClosedSelectorException e)
		{
			if(log.isWarnEnabled())
				log.warn("listen error, selector close, "+this,e);
			close();
		}
		catch(InterruptedIOException e2)
		{
		}
		catch(Exception e3)
		{
			if(log.isWarnEnabled()) log.warn("listen error, "+this,e3);
		}
	}
	/** 心跳 */
	public void heart(long time)
	{
		int t=(int)time;
		NioTcpConnect[] connects=getConnects();
		if(log.isDebugEnabled())
			log.debug(",heart ,time="+time+",size="+connects.length);
		for(int i=connects.length-1;i>=0;--i)
		{
			NioTcpConnect connect=connects[i];
			if(connect.isActive())
			{
				ByteBuffer buffer=new ByteBuffer();
				buffer.writeLong(time);
				connect.send(TransmitHandler.CMD_HEART,buffer);
				connect.setPingCodeTime(t,time);
			}
		}
	}
	/** 整理 */
	public void collate(long time)
	{
		NioTcpConnect[] connects=connectArray.getArray();
		for(int i=connects.length-1;i>=0;--i)
		{
			NioTcpConnect connect=connects[i];
			if(connect.isActive())
			{
				if(time<connect.getTimeout()+connect.getActiveTime())
					continue;
				connect.close();
			}
			if(log.isDebugEnabled())
				log.debug("collate, connect timeout, "+connect);
			connectArray.remove(connect);
		}
	}
	/** 关闭所有连接 */
	public void close()
	{
		NioTcpConnect[] connects=connectArray.getArray();
		connectArray.clear();
		for(int i=connects.length-1;i>=0;--i)
			connects[i].close();
		try
		{
			if(selector!=null) selector.close();
			selector=null;
		}
		catch(IOException e)
		{
		}
		if(log.isInfoEnabled())
			log.info("close, size="+connects.length+" "+this);
	}

	/* common methods */
	public String toString()
	{
		return super.toString()+"[size="+connectArray.size()+", executor="
			+executor+", serialExecute="+serialExecute+", runTime="+runTime
			+", pingTime="+pingTime+", collateTime="+collateTime+"]";
	}
	/* inner class */

}
