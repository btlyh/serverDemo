/** */
package com.cambrian.game.ds;

import com.cambrian.common.log.Logger;
import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.ConnectProducer;
import com.cambrian.common.net.DataAccessException;
import com.cambrian.common.net.DataAccessHandler;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.common.util.ByteBufferThreadLocal;
import com.cambrian.dfhm.GlobalConst;
import com.cambrian.dfhm.Lang;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 2013-5-9
 * @author HYZ (huangyz1988@qq.com)
 */
public class DirectDSCCAccess implements DSCCAccess
{

	private static final Logger log=Logger.getLogger(DirectDSCCAccess.class);
	public static final String err=DirectDSCCAccess.class.getName()
		+" getConnect, connect fail";

	ConnectProducer cp;
	int loadPort=GlobalConst.CC_LOAD_PORT;
	int exitPort=GlobalConst.CC_EXIT_PORT;
	int activePort=GlobalConst.CC_ACTIVE_PORT;
	int shutDownPort=GlobalConst.CC_SHUTDOWN_PORT;
	int registPort=GlobalConst.CC_REGIST_PORT;

	public ConnectProducer getConnectProducer()
	{
		return this.cp;
	}

	public void setConnectProducer(ConnectProducer cp)
	{
		this.cp=cp;
	}

	public int getLoadPort()
	{
		return this.loadPort;
	}

	public void setLoadPort(int port)
	{
		this.loadPort=port;
	}

	public int getActivePort()
	{
		return this.activePort;
	}

	public void setActivePort(int port)
	{
		this.activePort=port;
	}

	public int getExitPort()
	{
		return this.exitPort;
	}

	public void setExitPort(int port)
	{
		this.exitPort=port;
	}

	public boolean canAccess()
	{
		NioTcpConnect c=(cp!=null)?cp.getConnect():null;
		if((c==null)||(!(c.isActive())))
		{
			if(log.isWarnEnabled())
				log.warn("canAccess error, connect="+c,null);
			return false;
		}
		try
		{
			ByteBuffer bb=new ByteBuffer();
			bb.writeInt(222);
			c.send((short)11,bb);
			return true;
		}
		catch(Exception e)
		{
			if(log.isWarnEnabled())
				log.warn("canAccess error, connect="+c,e);
		}
		return false;
	}

	public String[] load(String sid,String address)
	{
		NioTcpConnect c=cp.getConnect();
		if(c==null||!c.isActive())
		{
			if(log.isWarnEnabled())
				log.warn("canAccess error, connect="+c,null);
			throw new DataAccessException(420,"connect error");
		}
		ByteBuffer bb=ByteBufferThreadLocal.getByteBuffer();
		bb.clear();
		bb.writeUTF(sid);
		bb.writeUTF(address);
		bb=DataAccessHandler.getInstance().access(c,loadPort,bb);
		String[] infos=new String[2];
		infos[0]=bb.readUTF();
		infos[1]=bb.readUTF();
		return infos;
	}

	public void active(String sid)
	{
		NioTcpConnect c=cp.getConnect();
		if((c==null)||(!(c.isActive())))
		{
			if(log.isWarnEnabled())
				log.warn("canAccess error, connect="+c,null);
			throw new DataAccessException(420,"connect error");
		}
		ByteBuffer bb=ByteBufferThreadLocal.getByteBuffer();
		bb.clear();
		bb.writeUTF(sid);
		c.send((short)activePort,bb);
	}

	public void exit(String sid)
	{
		NioTcpConnect c=cp.getConnect();
		if((c==null)||(!(c.isActive())))
		{
			if(log.isWarnEnabled())
				log.warn("canAccess error, connect="+c,null);
			throw new DataAccessException(420,"connect error");
		}
		ByteBuffer bb=ByteBufferThreadLocal.getByteBuffer();
		bb.clear();
		bb.writeUTF(sid);
		c.send((short)exitPort,bb);
	}

	public void shutDown()
	{
		NioTcpConnect c=cp.getConnect();
		if((c==null)||(!(c.isActive())))
		{
			if(log.isWarnEnabled())
				log.warn("canAccess error, connect="+c,null);
			throw new DataAccessException(420,"connect error");
		}
		ByteBuffer bb=ByteBufferThreadLocal.getByteBuffer();
		bb.clear();
		c.send((short)shutDownPort,bb);
	}

	public void regist(String info,int serverID)
	{
		NioTcpConnect c=cp.getConnect();
		if((c==null)||(!(c.isActive())))
		{
			if(log.isWarnEnabled())
				log.warn("canAccess error, connect="+c,null);
			throw new DataAccessException(420,"connect error");
		}
		ByteBuffer bb=ByteBufferThreadLocal.getByteBuffer();
		bb.clear();
		bb.writeUTF(info);
		bb.writeInt(serverID);
		bb=DataAccessHandler.getInstance().access(c,registPort,bb);
		if(!bb.readBoolean())
		{
			throw new DataAccessException(601,Lang.F618_REG_EXIST);
		}
	}
}