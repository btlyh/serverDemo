/**
 * 
 */
package com.cambrian.common.net;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;

import com.cambrian.common.codec.CRC32;
import com.cambrian.common.log.Logger;
import com.cambrian.common.thread.TaskPoolExecutor;
import com.cambrian.common.util.ArrayDeque;
import com.cambrian.common.util.ByteKit;
import com.cambrian.common.util.ChangeListener;
import com.cambrian.common.util.TimeKit;
import com.cambrian.game.Session;

/**
 * 类说明：异步TCP连接
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class NioTcpConnect implements Runnable
{

	/* static fields */
	/** 日志 */
	private static Logger log=Logger.getLogger(NioTcpConnect.class);
	public static final int SEND_BUFFER_SIZE=131072;
	public static final int R_BUFFER_SIZE=2048;
	public static final int W_BUFFER_SIZE=2048;
	public static final int MAX_DATA_LENGTH=129024;
	public static final int DATA_BUFFER_SIZE=32;
	private static final String err1=NioTcpConnect.class.getName()
		+" open, UnresolvedAddressException";
	private static final String err2=NioTcpConnect.class.getName()
		+" open, NoRouteToHostException";
	private static final String err3=NioTcpConnect.class.getName()
		+" open, ConnectException";
	private static final String err4=NioTcpConnect.class.getName()
		+" open, open fail";

	public static final byte[] NULL=new byte[0];
	public static final int TIMEOUT=180000;
	public static final int OPEN_CHANGED=1;
	public static final int CLOSE_CHANGED=2;
	/* static methods */

	/* fields */
	URL url;
	String localAddress;
	int localPort;
	volatile boolean active;
	long startTime;
	long activeTime;
	int ping=-1;
	int pingCode;
	long pingTime;
	int timeout=180000;
	Session session;
	/** 收到的消息分发 */
	ProxyDataHandler proxy;
	ChangeListener listener;

	private int sendBufferSize=131072;
	private int rBufferSize=2048;
	private int wBufferSize=2048;
	private int maxDataLength=129024;
	private int dataBufferSize=32;
	private TaskPoolExecutor executor;
	private boolean serialExecute=true;
	private SocketChannel channel;
	private java.nio.ByteBuffer reader;
	private java.nio.ByteBuffer writer;
	private ArrayDeque deque;
	private ByteBuffer data;
	private byte[] head;
	private int headIndex;
	private boolean running;

	/** 协议版本号 */
	byte version=0x10;
	/** 流水号 */
	int uid;

	/* constructors */

	/* properties */
	public URL getURL()
	{
		return url;
	}

	public String getLocalAddress()
	{
		return localAddress;
	}

	public int getLocalPort()
	{
		return localPort;
	}

	public boolean isActive()
	{
		return active;
	}

	public long getStartTime()
	{
		return startTime;
	}

	public long getActiveTime()
	{
		return activeTime;
	}

	public int getPing()
	{
		return ping;
	}

	public void setPing(int paramInt)
	{
		this.ping=paramInt;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int paramInt)
	{
		this.timeout=paramInt;
	}

	public Session getSession()
	{
		return session;
	}

	public void setSession(Session session)
	{
		this.session=session;
	}

	public void setTransmitHandler(ProxyDataHandler paramTransmitHandler)
	{
		this.proxy=paramTransmitHandler;
	}

	public ChangeListener getChangeListener()
	{
		return listener;
	}

	public void setChangeListener(ChangeListener paramChangeListener)
	{
		this.listener=paramChangeListener;
	}

	public int getPingCode()
	{
		return pingCode;
	}

	public long getPingTime()
	{
		return pingTime;
	}

	public void setPingCodeTime(int paramInt,long paramLong)
	{
		this.pingCode=paramInt;
		this.pingTime=paramLong;
	}
	public int getSendBufferSize()
	{
		return sendBufferSize;
	}

	public void setSendBufferSize(int paramInt)
	{
		this.sendBufferSize=paramInt;
		if(channel==null) return;
		try
		{
			channel.socket().setSendBufferSize(this.sendBufferSize);
		}
		catch(IOException localIOException)
		{
		}
	}

	public int getRBufferSize()
	{
		return rBufferSize;
	}

	public void setRBufferSize(int paramInt)
	{
		this.rBufferSize=paramInt;
	}

	public int getWBufferSize()
	{
		return wBufferSize;
	}

	public void setWBufferSize(int paramInt)
	{
		this.wBufferSize=paramInt;
	}

	public int getMaxDataLength()
	{
		return maxDataLength;
	}

	public void setMaxDataLength(int paramInt)
	{
		this.maxDataLength=paramInt;
	}

	public int getDataBufferSize()
	{
		return dataBufferSize;
	}

	public void setDataBufferSize(int paramInt)
	{
		this.dataBufferSize=paramInt;
	}

	public TaskPoolExecutor getExecutor()
	{
		return executor;
	}

	public void setExecutor(TaskPoolExecutor paramExecutor)
	{
		this.executor=paramExecutor;
	}

	public boolean isSerialExecute()
	{
		return serialExecute;
	}

	public void setSerialExecute(boolean paramBoolean)
	{
		this.serialExecute=paramBoolean;
	}

	public SocketChannel getSocketChannel()
	{
		return channel;
	}

	public void setSocketChannel(SocketChannel socketChannel)
		throws IOException
	{
		channel=socketChannel;
		socketChannel.configureBlocking(false);
		Socket socket=socketChannel.socket();
		socket.setTcpNoDelay(false);
		socket.setSendBufferSize(sendBufferSize);
		localAddress=socket.getLocalAddress().getHostAddress();
		localPort=socket.getLocalPort();
	}

	protected java.nio.ByteBuffer getReader()
	{
		return reader;
	}

	protected java.nio.ByteBuffer getWriter()
	{
		return writer;
	}
	/* init start */

	/* methods */
	public void open(URL url)
	{
		if(log.isInfoEnabled()) log.info("open="+url);
		try
		{
			if(active)
				throw new IllegalStateException(this
					+" open, connect is active");
			if(url==null)
				throw new IllegalArgumentException(this+" open, null url");
			this.url=url;
			if(channel==null)
				setSocketChannel(SocketChannel.open(new InetSocketAddress(
					url.getHost(),url.getPort())));
		}
		catch(UnresolvedAddressException e1)
		{
			if(log.isDebugEnabled())
				log.debug("open error, "+url.getString(),e1);
			throw new DataAccessException(420,err1,null,url.getString());
		}
		catch(NoRouteToHostException e2)
		{
			if(log.isDebugEnabled())
				log.debug("open error, "+url.getString(),e2);
			throw new DataAccessException(420,err2,null,url.getString());
		}
		catch(ConnectException e3)
		{
			if(log.isDebugEnabled())
				log.debug("open error, "+url.getString(),e3);
			throw new DataAccessException(420,err3,null,url.getString());
		}
		catch(Exception e4)
		{
			if(log.isDebugEnabled())
				log.debug("open error, "+url.getString(),e4);
			throw new DataAccessException(420,err4,null,url.getString());
		}
		reader=java.nio.ByteBuffer.allocateDirect(rBufferSize);
		writer=java.nio.ByteBuffer.allocateDirect(wBufferSize);
		deque=new ArrayDeque(dataBufferSize);
		head=new byte[4];
		active=true;
		startTime=System.currentTimeMillis();
		activeTime=startTime;
		if(log.isDebugEnabled()) log.debug("open, "+this);
		if(listener!=null) listener.change(this,OPEN_CHANGED);
		// System.err.println(Thread.currentThread().getName());
	}

	/** 消息的分发处理 */
	public void receive(ByteBuffer data)
	{
		activeTime=TimeKit.nowTimeMills();

		if(session!=null) session.setActiveTime(activeTime);
		try
		{
			proxy.transmit(this,data);
		}
		catch(Exception e)
		{
			if(log.isWarnEnabled()) log.warn("receive, "+this,e);
		}
	}

	public void close()
	{
		synchronized(this)
		{
			if(!active) return;
			active=false;
		}
		if(log.isDebugEnabled()) log.debug("close, "+this);
		if(listener!=null) listener.change(this,CLOSE_CHANGED);
		try
		{
			if(channel!=null)
			{
				channel.close();
				channel=null;
			}
		}
		catch(Exception e)
		{
		}
	}

	/** 获取流水号 */
	public synchronized int getUid()
	{
		return ++uid;
	}

	/** 创建不用返回的消息头 */
	private ByteBuffer creatHead(int cmd,ByteBuffer data)
	{
		ByteBuffer head=new ByteBuffer();
		head.writeInt(16+data.length());// 4 总长度
		head.writeByte(12);// 1 头长度
		head.writeByte(version);// 1 版本
		head.writeInt(getUid());// 4 连接流水号
		head.writeShort(cmd);// 2 命令号
		head.writeInt(CRC32.getValue(data.toArray()));// 4
		return head;
	}
	/** 创建需要返回的消息头 */
	private ByteBuffer creatReturnHead(int cmd,int recmd,int uuid,
		ByteBuffer data)
	{
		ByteBuffer head=new ByteBuffer();
		head.writeInt(22+data.length());// 4 总长度
		head.writeByte(18);// 1 头长度
		head.writeByte(version);// 1 版本
		head.writeInt(getUid());// 4 连接流水号
		head.writeShort(cmd);// 2 命令号
		head.writeInt(CRC32.getValue(data.toArray()));// 4
		head.writeShort(recmd); // 2
		head.writeInt(uuid);// 4
		return head;
	}

	/** 发送消息（不需要返回消息） */
	public void send(int cmd,ByteBuffer data)
	{
		if(log.isDebugEnabled())
			log.debug(",send command="+cmd+",len="+(16+data.length())
				+", connect="+toString());
		if(cmd==0) return;
		ByteBuffer head=creatHead(cmd,data);
		head.write(data.getArray(),data.offset,data.length());
		send(head.getArray(),head.offset,head.length());
	}
	/** 发送消息 */
	public void send(ByteBuffer data)
	{
		send(data.getArray(),data.offset,data.length());
	}
	/** 发送数据（需要有返回消息） */
	public void send(int cmd,int recmd,int uuid,ByteBuffer data)
	{
		if(log.isDebugEnabled())
			log.debug(",send command="+cmd+",len="+(15+data.length())
				+", connect="+toString());
		if(cmd==0) return;
		ByteBuffer head=creatReturnHead(cmd,recmd,uuid,data);
		head.write(data.getArray(),data.offset,data.length());
		send(head.getArray(),head.offset,head.length());
	}
	/** 发送返回出去的消息 */
	public void send(int recmd,int reuuid,ByteBuffer data)
	{
		if(log.isDebugEnabled())
			log.debug(",send ,version="+version+",reuuid="+reuuid
				+",recommand="+recmd);
		if(recmd==0) return;
		ByteBuffer head=creatHead(recmd,data);
		head.write(data.getArray(),data.offset,data.length());
		send(head.getArray(),head.offset(),head.length());
	}

	/** 发送数据 */
	public synchronized void send(byte[] bytes,int scr,int len)
	{
		if(!isActive()) return;
		if(len<=0) return;
		if(len>getMaxDataLength())
			throw new IllegalArgumentException(this+" send, data overflow:"
				+len);
		try
		{
			// writer.putInt(len+4);
			// int capacity=getWBufferSize()-4;
			send(getWBufferSize(),bytes,scr,len);
		}
		catch(Exception e)
		{
			if(log.isDebugEnabled())
				log.debug("send error, len="+len+" "+this,e);
			close();
		}
	}

	/** local */
	protected void send(int capacity,byte[] bytes,int scr,int len)
		throws IOException
	{
		int i=0;
		while(capacity<=len)
		{
			writer.put(bytes,scr,capacity);
			i+=send(i);
			scr+=capacity;
			len-=capacity;
			capacity=wBufferSize;
		}
		if(len<=0) return;
		writer.put(bytes,scr,len);
		send(i);
	}

	/** 发送数据 */
	public synchronized void send(byte[] bytes1,int scr1,int len1,
		byte[] bytes2,int scr2,int len2)
	{
		if(!isActive()) return;
		int len=len1+len2;
		if(len<=0) return;
		if(len>getMaxDataLength())
			throw new IllegalArgumentException(this+" send, data overflow:"
				+len1+","+len2);
		try
		{
			// writer.putInt(len+4);
			// int capacity=getWBufferSize()-4;
			send(getWBufferSize(),bytes1,scr1,len1,bytes2,scr2,len2);
		}
		catch(Exception e)
		{
			if(log.isDebugEnabled())
				log.debug("send error, len="+len1+","+len2+" "+this,e);
			close();
		}
	}
	/** local */
	protected void send(int capacity,byte[] bytes1,int scr1,int len1,
		byte[] bytes2,int scr2,int len2) throws IOException
	{
		int i=0;
		while(capacity<=len1)
		{
			writer.put(bytes1,scr1,capacity);
			i+=send(i);
			scr1+=capacity;
			len1-=capacity;
			capacity=wBufferSize;
		}
		if(len1>0)
		{
			writer.put(bytes1,scr1,len1);
			capacity=wBufferSize-writer.position();
		}
		while(capacity<=len2)
		{
			writer.put(bytes2,scr2,capacity);
			i+=send(i);
			scr2+=capacity;
			len2-=capacity;
			capacity=wBufferSize;
		}
		if(len2>0) writer.put(bytes2,scr2,len2);
		if(writer.position()<=0) return;
		send(i);
	}
	/** local */
	private int send(int paramInt) throws IOException
	{
		int i=writer.position();
		writer.flip();
		int j=channel.write(writer);
		paramInt+=j;
		if(j<i)
			throw new IOException(this+" send, buffer overflow:"+paramInt);
		writer.clear();
		return j;
	}

	/** 连接的数据接收方法 */
	public void receive()
	{
		int r=0;
		try
		{
			while(channel!=null)
			{
				r=channel.read(reader);
				if(r<0)
					throw new IOException(this+" receive, channel read over");
				if(r==0) break;
				reader.flip();
				while(readBuffer()>0)
					;
				reader.clear();
			}
		}
		catch(Exception e)
		{
			if(log.isDebugEnabled()) log.debug("receive error, "+this,e);
			close();
		}
	}
	/** 从块中读取数据，返回块中还可读取的长度 */
	protected int readBuffer() throws IOException
	{
		if(data==null)
		{
			createData();
			if(data==null) return 0;
		}
		int len1=reader.limit()-reader.position();
		if(len1==0) return 0;
		int top=data.top();
		int len2=data.capacity()-data.top();
		if(len2>len1)
		{
			reader.get(data.getArray(),top,len1);
			data.setTop(top+len1);
			return 0;
		}
		reader.get(data.getArray(),top,len2);
		data.setTop(top+len2);
		pushData(data);
		data=null;
		return len1-len2;
	}
	/** 创建字节缓存对象，有时需要二次读取才能获得字节缓存的长度 */
	private void createData() throws IOException
	{
		if(headIndex==0)
		{
			if(reader.position()==reader.limit()) return;
			head[headIndex++]=reader.get();
		}
		int n=4;// getHeadLength(head,headIndex);
		while(headIndex<n)
		{
			if(reader.position()==reader.limit()) return;
			head[headIndex++]=reader.get();
		}
		headIndex=0;
		data=createDataByHead(head,n);
	}
	// /** 根据当前的数据头获得头信息的长度 */
	// protected int getHeadLength(byte[] head,int headIndex)
	// {
	// return ByteKit.getReadLength(head[0]);
	// }
	/** 根据头信息创建字节缓存对象 */
	protected ByteBuffer createDataByHead(byte[] head,int length)
		throws IOException
	{
		int len=ByteKit.readInt(head,0)-4;
		if(len>maxDataLength)
			throw new IOException(this+" createDataByHead, data overflow:"
				+len);
		return new ByteBuffer(len);
	}
	/** 取出新消息 */
	protected Object popData()
	{
		synchronized(deque)
		{
			if(!deque.isEmpty()) return deque.remove();
			running=false;
			return null;
		}
	}
	/** 加入新消息 */
	protected void pushData(Object data) throws IOException
	{
		synchronized(deque)
		{
			if(deque.isFull())
				throw new IOException(this+" pushData, deque is full");
			deque.add(data);
			if(running&&serialExecute) return;
			running=true;
		}
		executor.execute(this);// 这里调用run方法
	}
	/** 运行方法，执行器处理消息 */
	public void run()
	{
		Object data;
		while((data=popData())!=null)
			receive((ByteBuffer)data);
	}

	/* common methods */
	public String toString()
	{
		return super.toString()+"["+url+", localAddress="+localAddress
			+", localPort="+localPort+", active="+active+", startTime="
			+startTime+", activeTime="+activeTime+", ping="+ping
			+", timeout="+timeout+"]";
	}
	/* inner class */

}
