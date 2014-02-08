package com.cambrian.common.net;

import com.cambrian.common.thread.ArrayThreadLocal;
import com.cambrian.common.timer.Timer;
import com.cambrian.common.timer.TimerEvent;
import com.cambrian.common.timer.TimerListener;
import com.cambrian.common.util.ByteKit;

/**
 * 类说明：数据访问处理器
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class DataAccessHandler extends Command implements DataAccess,
	TimerListener
{

	/* static fields */
	private static final String err1=DataAccessHandler.class.getName()
		+" getServicePort, invalid port";
	private static final String err2=DataAccessHandler.class.getName()
		+" access, timeout";
	private static final String err3=DataAccessHandler.class.getName()
		+" access, io error";
	private static final String err4=DataAccessHandler.class.getName()
		+" parseData, server data error";
	/** 空结果对象 */
	public static final Object NONE=new Object();
	/** 无返回结果对象 */
	public static final Object VOID=new Object();
	public static final int TIMEOUT=10000;
	public static final int ACCESS_OK=1;
	public static final int ACCESS_ERROR=-1;
	public static final int ACCESS_EXCEPTION=0;
	private static int plusId;
	private static int minusId;
	/** 条目ID */
	private static int entryId;
	/** 递增id同步锁（保证同时只有一个线程获取ID） */
	private static Object plusLock=new Object();
	/** 递减id同步锁（保证同时只有一个线程获取ID） */
	private static Object minusLock=new Object();
	/** 访问处理器 */
	private static DataAccessHandler handler=new DataAccessHandler();

	/* static methods */
	public static DataAccessHandler getInstance()
	{
		return handler;
	}
	/** 获得一个新条目ID */
	public static synchronized int newId()
	{
		return (entryId++);
	}
	/** 递增ID */
	static int newPlusId()
	{
		synchronized(plusLock)
		{
			if(++plusId<=0) plusId=1;
			return plusId;
		}
	}
	/** 递减ID */
	static int newMinusId()
	{
		synchronized(minusLock)
		{
			if(--minusId>=0) minusId=-1;
			return minusId;
		}
	}

	/* fields */
	/** 线程访问器 */
	ThreadAccessHandler threadAccess=new ThreadAccessHandler();
	/** 访问返回端口 */
	int accessReturnPort=4;
	/** 超时时间 */
	int timeout=10000;

	/* constructors */
	/* properties */
	public int getAccessReturnPort()
	{
		return this.accessReturnPort;
	}
	public void setAccessReturnPort(int port)
	{
		this.accessReturnPort=port;
	}
	public int getTimeout()
	{
		return this.timeout;
	}
	public void setTimeout(int time)
	{
		this.timeout=time;
	}
	/** 获得服务端口 */
	int getServicePort(URL url)
	{
		String str=url.getFileQuery();
		if((str==null)||(str.length()==0))
			throw new DataAccessException(410,err1,null,url.getString());
		return Integer.parseInt(str);
	}
	/* init start */
	/* methods */
	/** 通讯处理 */
	public void transmit(URL url,ByteBuffer data)
	{
		NioTcpConnect connect=ConnectFactory.getConnect(url);
		byte[] bytes=new byte[2];
		ByteKit.writeShort((short)getServicePort(url),bytes,0);
		connect.send(bytes,0,2,data.getArray(),data.offset(),data.length());
	}

	/** 通讯处理 */
	public void handle(NioTcpConnect connect,ByteBuffer data)
	{
		int uuid=data.readInt();
		notify(uuid,data);
	}
	/** 通知 */
	void notify(int id,ByteBuffer data)
	{
		if(id>0)
			threadAccess.notify(id,data);
		else
			accessCall(id,data);
	}

	public ByteBuffer access(URL url,ByteBuffer data)
	{
		NioTcpConnect connect=ConnectFactory.getConnect(url);
		return access(connect,getServicePort(url),data,timeout);
	}

	public ByteBuffer access(URL url,ByteBuffer data,int timeout)
	{
		NioTcpConnect connect=ConnectFactory.getConnect(url);
		return access(connect,getServicePort(url),data,timeout);
	}

	public ByteBuffer access(NioTcpConnect connect,int port,ByteBuffer data)
	{
		return access(connect,port,data,timeout);
	}

	public ByteBuffer access(NioTcpConnect connect,int port,ByteBuffer data,
		int timeout)
	{
		if(connect==null)
			throw new IllegalArgumentException(super.getClass().getName()
				+" access, null connect");
		Object obj=threadAccess.access(new Entry(connect,port,data),timeout);
		DataAccessException exception=null;
		if(obj!=NONE)
		{
			ByteBuffer result=(ByteBuffer)obj;
			exception=parseData(result);
			if(exception==null) return result;
		}
		throw resetException(exception,connect,port);
	}

	public void access(NioTcpConnect connect,int command,ByteBuffer data,
		ActionListener listener,Object paramter,Timer timer)
	{
		access(connect,command,data,listener,paramter,timer,timeout);
	}

	public void access(NioTcpConnect connect,int command,ByteBuffer data,
		ActionListener listener,Object paramter,Timer timer,int time)
	{
		if(connect==null)
			throw new IllegalArgumentException(getClass().getName()
				+" access, null connect");
		Entry entry=new Entry(newMinusId(),new ActionEvent(connect,-1,
			paramter,listener),connect,command,null);
		threadAccess.addEntry(entry);
		accessSend(connect,command,entry.getId(),data);
		timer.add(new TimerEvent(this,entry,time,1,time));
	}

	public void accessCall(int id,ByteBuffer data)
	{
		Entry entry=(Entry)threadAccess.removeEntry(id);
		if(entry==null) return;
		ActionEvent enent=(ActionEvent)entry.getResult();
		ActionListener listener=(ActionListener)enent.parameter;
		DataAccessException exception=parseData(data);
		if(exception!=null)
		{
			enent.type=0;
			enent.parameter=resetException(exception,entry.connect,
				entry.port);
		}
		else
		{
			enent.type=1;
			enent.parameter=data;
		}
		listener.action(enent);
	}

	public DataAccessException resetException(DataAccessException exception,
		NioTcpConnect connect,int port)
	{
		if(exception==null)
		{
			if(connect.isActive())
				exception=new DataAccessException(440,err2);
			else
				exception=new DataAccessException(420,err3);
		}
		if(connect.getURL().getFile().indexOf(63)<0)
			exception.setAddress(connect.getURL().getString()+"?"+port);
		else
			exception.setAddress(connect.getURL().getString());
		return exception;
	}

	public void onTimer(TimerEvent event)
	{
		Entry entry=(Entry)event.getParameter();
		if(!threadAccess.removeEntry(entry)) return;
		ActionEvent aevent=(ActionEvent)entry.getResult();
		ActionListener listener=(ActionListener)aevent.parameter;
		aevent.parameter=resetException(null,entry.connect,entry.port);
		aevent.type=0;
		listener.action(aevent);
	}

	public void accessSend(NioTcpConnect connect,int port,int id,
		ByteBuffer data)
	{
		byte[] bytes=ArrayThreadLocal.getByteArray(8);
		ByteKit.writeShort((short)port,bytes,0);// 0,1字节
		ByteKit.writeShort((short)accessReturnPort,bytes,2);// 2,3字节
		ByteKit.writeInt(id,bytes,4);// 4,5,6,7,8字节
		if((data!=null)&&(data.length()>0))
			connect.send(bytes,0,8,data.getArray(),data.offset(),
				data.length());
		else
			connect.send(bytes,0,bytes.length);
	}
	/** 解析数据 */
	DataAccessException parseData(ByteBuffer data)
	{
		try
		{
			int i=data.readUnsignedShort();
			if(i==Command.OK) return null;
			String str=data.readUTF();
			String[] strs=null;
			if(data.length()>0)
			{
				int j=data.readUnsignedByte();
				if(j>0)
				{
					strs=new String[j];
					for(int k=0;k<j;++k)
						strs[k]=data.readUTF();
				}
			}
			return new DataAccessException(/* i */0,str,strs,null);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
		}
		return new DataAccessException(
			DataAccessException.CLIENT_SDATA_ERROR,err4);
	}

	/***
	 * 类说明： 数据访问条目
	 * 
	 * @version 2013-5-14
	 * @author HYZ (huangyz1988@qq.com)
	 */
	class Entry
	{

		/** 条目ID */
		int id;
		/** 访问端口 */
		int port;
		/** 访问连接 */
		NioTcpConnect connect;
		/** 参数数据 */
		ByteBuffer data;
		/** 访问结果 */
		Object result;

		/* constructors */
		public Entry()
		{
			this(newId(),NONE);
		}
		public Entry(Object result)
		{
			this(newId(),result);
		}
		public Entry(int id,Object result)
		{
			this.id=id;
			this.result=result;
		}
		Entry(NioTcpConnect connect,int port,ByteBuffer data)
		{
			// TODO 这里检查下
			// this(DataAccessHandler.this, DataAccessHandler.newPlusId(),
			// NONE, paramConnect, paramInt, paramByteBuffer);
			this(newPlusId(),NONE,connect,port,data);
		}
		public Entry(int id,Object result,NioTcpConnect connect,int port,
			ByteBuffer data)
		{
			this(id,result);
			this.connect=connect;
			this.port=port;
			this.data=data;
		}
		/* properties */
		public int getId()
		{
			return this.id;
		}
		public Object getResult()
		{
			return this.result;
		}
		/* init start */
		/* methods */
		public void access()
		{
			// DataAccessHandler.this.accessSend(connect,port,getId(),data);
			// connect.send((short)port,data);
			System.out.println("======Entry access id="+id);
			// ByteBuffer bb=new ByteBuffer();
			connect.send((short)port,(short)accessReturnPort,id,data);
		}
		/* common methods */
		/* inner class */

	}
	/* common methods */
	/* inner class */

}
