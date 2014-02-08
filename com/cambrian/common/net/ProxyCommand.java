package com.cambrian.common.net;

import com.cambrian.common.log.Logger;

/**
 * 类说明：
 * 
 * @version 2013-5-14
 * @author HYZ (huangyz1988@qq.com)
 */
public class ProxyCommand extends Command
{

	private static final Logger log=Logger.getLogger(ProxyCommand.class);

	int port;
	ConnectProducer cp;

	/** 设置代理端口和连接提供器 */
	public void setProxy(int port,ConnectProducer cp)
	{
		this.port=port;
		this.cp=cp;
	}

	/** 处理 */
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

	/** 代理访问 */
	public void proxyAccess(NioTcpConnect c,ByteBuffer data)
	{
		try
		{
			ByteBuffer bb=null;
			bb=DataAccessHandler.getInstance().access(c,port,data);//CC认证
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
