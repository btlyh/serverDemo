package com.cambrian;

import com.cambrian.common.log.Logger;
import com.cambrian.common.net.ProxyDataHandler;
import com.cambrian.common.net.URL;
import com.cambrian.common.net.client.ClientConnectManager;

/**
 * ��˵����
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class ClientLoadContext
{

	/* static fields */
	/** ��־ */
	private static Logger log=Logger.getLogger(ClientLoadContext.class);
	/** ������ */
	public static ClientLoadContext loadContext=new ClientLoadContext();

	/* static methods */

	/* fields */

	/* ͨ�� */
	/** �ͻ�����Ϣ������ */
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

	/** ��ʼ�� */
	public void init()
	{
		// TODO Auto-generated method stub
		if(log.isInfoEnabled()) log.info(" ,init");
		initConnectProducer();

		initGameClient();
	}

	/** ��ʼ���������������ṩ�� */
	public void initConnectProducer()
	{
		if(log.isInfoEnabled()) log.info(" ,initConnectProducer");
		URL url=new URL("tcp://118.112.253.214:20501");
		ClientConnectManager.getConnect(url);
	}

	/** ��ʼ����Ϸ�ͻ��� */
	private void initGameClient()
	{
		if(log.isInfoEnabled()) log.info(" ,initGameClient");

		if(log.isInfoEnabled()) log.info(" ,initGameClient ok");

	}
	/* methods */

	/* common methods */

	/* inner class */

}
