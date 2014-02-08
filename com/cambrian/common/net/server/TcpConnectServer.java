/**
 * 
 */
package com.cambrian.common.net.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.cambrian.common.log.Logger;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.common.net.ProxyDataHandler;
import com.cambrian.common.net.URL;
import com.cambrian.common.thread.TaskPoolExecutor;
import com.cambrian.common.util.ChangeListenerList;

/**
 * 类说明：异步TCP连接服务
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class TcpConnectServer extends ChangeListenerList implements Runnable
{

	/* static fields */
	/** 日志 */
	private static Logger log=Logger.getLogger(TcpConnectServer.class);

	public static final String PROTOCOL="tcp";

	/* static methods */

	/* fields */
	InetAddress address;
	int port;
	boolean active;
	ProxyDataHandler handler;
	TaskPoolExecutor executor=new TaskPoolExecutor();
	boolean serialExecute;
	int sendBufferSize=131072;

	int rBufferSize=2048;
	int wBufferSize=2048;
	int maxDataLength=129024;
	int dataBufferSize=32;

	private Selector selector;
	private ServerSocketChannel serverChannel;
	Thread run;

	/* constructors */

	/* properties */
	public InetAddress getAddress()
	{
		return this.address;
	}

	public int getPort()
	{
		return this.port;
	}

	public boolean isActive()
	{
		return this.active;
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
	/* init start */

	/* methods */

	public void open(String host,int port)
	{
		if(active)
			throw new IllegalStateException(this+" open, Server is active");
		try
		{
			if(host==null)
				address=InetAddress.getLocalHost();
			else
				address=InetAddress.getByName(host);
			this.port=port;
			selector=Selector.open();
			serverChannel=ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			serverChannel.socket().setSoTimeout(0);
			InetSocketAddress inetAddress=new InetSocketAddress(address,port);
			serverChannel.socket().bind(inetAddress);
			serverChannel.register(selector,16);
		}
		catch(Exception e)
		{
			if(log.isWarnEnabled())
				log.warn("open error, localAddress="+host+", port="+port,e);
			throw new RuntimeException(this+" open, localAddress="+host
				+", port="+port+", "+e.toString());
		}
		active=true;
		run=new Thread(this);
		// String str=(this.address!=null)?this.address.getHostAddress():"";
		run.setName(run.getName()+"@"+super.getClass().getName()+"@"
			+super.hashCode()+"/"+address.getHostAddress()+":"+port);
		run.start();
		if(log.isInfoEnabled()) log.info("open, "+this);
	}

	public void run()
	{
		while(active)
		{
			try
			{
				if(selector.select()>0)
				{
					Set<?> localSet=selector.selectedKeys();
					Iterator<?> iterator=localSet.iterator();
					while(iterator.hasNext())
					{
						SelectionKey skey=(SelectionKey)iterator.next();
						iterator.remove();
						int i=skey.readyOps();
						if((i&0x10)==16)
						{
							create();
						}
						else
						{
							if((i&0x1)!=1) continue;
							NioTcpConnect connect=(NioTcpConnect)skey
								.attachment();
							connect.receive();
						}
					}
				}
			}
			catch(Exception localException)
			{
				if(log.isWarnEnabled())
					log.warn("run error, "+this,localException);
			}
		}
	}

	protected void create()
	{
		SocketChannel socketChannel=null;
		try
		{
			socketChannel=serverChannel.accept();
			Socket socket=socketChannel.socket();
			if(log.isDebugEnabled()) log.debug("create, "+socket);
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
			connect.setSocketChannel(socketChannel);
			SelectionKey skey=socketChannel.register(selector,1);
			skey.attach(connect);
			connect.open(new URL("tcp",socket.getInetAddress()
				.getHostAddress(),socket.getPort(),null));
			if(log.isDebugEnabled()) log.debug("create ok, "+connect);
		}
		catch(Exception e)
		{
			try
			{
				if(socketChannel!=null) socketChannel.close();
			}
			catch(Exception e1)
			{
			}
			if(log.isWarnEnabled()) log.warn("create error, "+this,e);
		}
	}

	public void close()
	{
		active=false;
		if(run!=null) run.interrupt();
		run=null;
		try
		{
			if(selector!=null) selector.close();
			selector=null;
			if(serverChannel!=null) serverChannel.close();
			serverChannel=null;
		}
		catch(IOException localIOException)
		{
		}
		if(log.isInfoEnabled()) log.info("close, "+this);
	}
	/* common methods */

	public String toString()
	{
		String str=(address!=null)?address.getHostAddress():"";
		return super.toString()+"["+str+":"+port+", active="+active
			+", executor="+executor+", serialExecute="+serialExecute
			+", sendBufferSize="+sendBufferSize+", rBufferSize="+rBufferSize
			+", wBufferSize="+wBufferSize+", maxDataLength="+maxDataLength
			+", dataBufferSize="+dataBufferSize+"]";
	}
	/* inner class */

}
