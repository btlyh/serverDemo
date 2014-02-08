package com.cambrian.gc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.cambrian.game.ds.DataServer;
import com.cambrian.gc.notice.BaseUpdateNotice;
import com.cambrian.gc.notice.CardUpdateNotice;
import com.cambrian.gc.notice.ProUpdateNotice;

/**
 * ��˵����������Ϸ�������޸�
 * 
 * @author��LazyBear
 */
public class GameControl extends Thread
{

	/* static fields */
	private static final int SERVER_PORT=10000;
	/* static methods */

	/* fields */
	/** ���ݹ������� */
	DataServer dataServer;
	/** �������Ը������� */
	BaseUpdateNotice bun;
	/** �������Ը������� */
	ProUpdateNotice pun;
	/** �����޸ĸ������� */
	CardUpdateNotice cun;

	/* constructors */
	public GameControl()
	{
		start();
	}
	/* properties */
	public void setDS(DataServer dataServer)
	{
		this.dataServer=dataServer;
	}
	public void setBun(BaseUpdateNotice bun)
	{
		this.bun=bun;
	}
	public void setPun(ProUpdateNotice pun)
	{
		this.pun=pun;
	}
	public void setCun(CardUpdateNotice cun)
	{
		this.cun=cun;
	}
	/* init start */

	/* methods */
	public void run()
	{
		try
		{
			ServerSocket ss=new ServerSocket(SERVER_PORT);
			while(true)
			{
				Socket socket=ss.accept();
				new CreateServerThread(socket,dataServer,bun,pun,cun);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

}
