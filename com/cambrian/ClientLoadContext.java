package com.cambrian;

import com.cambrian.common.log.Logger;
import com.cambrian.common.net.ProxyDataHandler;
import com.cambrian.common.net.URL;
import com.cambrian.common.net.client.ClientConnectManager;

/**
 * 类说明：
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class ClientLoadContext
{

	/* static fields */
	/** 日志 */
	private static Logger log=Logger.getLogger(ClientLoadContext.class);
	/** 加载器 */
	public static ClientLoadContext loadContext=new ClientLoadContext();

	/* static methods */

	/* fields */

	/* 通信 */
	/** 客户端信息处理器 */
	public ProxyDataHandler clientService;

	/* constructors */

	public ClientLoadContext()
	{
		// TODO Auto-generated constructor stub
	}

	/* properties */

	public void setClientService(ProxyDataHandler clientService)
	{
		this.clientService=clientService;
	}

	/* init start */

	/** 初始化 */
	public void init()
	{
		// TODO Auto-generated method stub
		if(log.isInfoEnabled()) log.info(" ,init");
		initConnectProducer();

		initGameClient();
	}

	/** 初始化无阻塞的连接提供器 */
	public void initConnectProducer()
	{
		if(log.isInfoEnabled()) log.info(" ,initConnectProducer");
		URL url=new URL("tcp://118.112.253.214:20501");
		ClientConnectManager.getConnect(url);
	}

	/** 初始化游戏客户端 */
	private void initGameClient()
	{
		if(log.isInfoEnabled()) log.info(" ,initGameClient");

		if(log.isInfoEnabled()) log.info(" ,initGameClient ok");

	}
	/* methods */

	/* common methods */

	/* inner class */

}
