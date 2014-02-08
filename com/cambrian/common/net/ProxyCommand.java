package com.cambrian.common.net;

import com.cambrian.common.log.Logger;

/**
 * ��˵����
 * 
 * @version 2013-5-14
 * @author HYZ (huangyz1988@qq.com)
 */
public class ProxyCommand extends Command
{

	private static final Logger log=Logger.getLogger(ProxyCommand.class);

	int port;
	ConnectProducer cp;

	/** ���ô���˿ں������ṩ�� */
	public void setProxy(int port,ConnectProducer cp)
	{
		this.port=port;
		this.cp=cp;
	}

	/** ���� */
	@Override
	public void handle(NioTcpConnect connect,ByteBuffer data)
	{
		NioTcpConnect c=cp.getConnect();
		if(c==null||(!c.isActive()))
		{
			if(log.isWarnEnabled())
				log.warn("Proxy: access error, port="+port+" Connect="+c);
		}
		proxyAccess(c,data);
	}

	/** ������� */
	public void proxyAccess(NioTcpConnect c,ByteBuffer data)
	{
		try
		{
			ByteBuffer bb=null;
			bb=DataAccessHandler.getInstance().access(c,port,data);//CC��֤
			String sid=bb.readUTF();
			data.clear();
			data.writeUTF(sid);
		}
		catch(DataAccessException e)
		{
			if(log.isWarnEnabled())
				log.warn("accessProxy error, port="+port+", connect="+c,e);
			throw e;
		}
		catch(Exception e)
		{
			if(log.isWarnEnabled())
				log.warn("accessProxy error, port="+port+", connect="+c,e);
			throw new DataAccessException(
				DataAccessException.SERVER_INTERNAL_ERROR,
				"Proxy: internal error");
		}
	}
}
