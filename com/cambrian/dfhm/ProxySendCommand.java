package com.cambrian.dfhm;

import com.cambrian.common.log.Logger;
import com.cambrian.game.Session;
import com.cambrian.game.ds.DataServer;

/**
 * ��˵����ͨ��-������Ϣ������
 * 
 * @version 1.0
 * @date 2013-6-7
 * @author maxw<woldits@qq.com>
 */
public class ProxySendCommand
{
	/* static fields */
	/**  ��־ */
	private static final Logger log=Logger.getLogger(ProxySendCommand.class);
	/**  */
	public static ProxySendCommand proxy=new ProxySendCommand();

	/* static methods */
	
	/* fields */
	/** ���ݷ����� */
	DataServer dataServer;
	/** ���ʹ����� */
	SendCommand[] handlers=new SendCommand[0xffff];

	/* constructors */

	/* properties */
	/**
	 * @param dataServer the dataServer to set
	 */
	public void setDataServer(DataServer dataServer)
	{
		this.dataServer=dataServer;
	}
	
	/* init start */

	/* methods */
	/** ��ȡָ�����ʹ����� */
	public SendCommand getPort(int id)
	{
		return this.handlers[id];
	}
	/** ����ָ�����ʹ����� */
	public void setPort(int id,SendCommand handler)
	{
		if(log.isDebugEnabled())
			log.debug(",setSendCommand ,id="+id+",handler="+handler);
		this.handlers[id]=handler;
	}
	/** �ַ� */
	public void handle(int uuid,long userid,Object[] objs)
	{
		Session session=dataServer.getSessionMap().get(userid+"");
		handle(uuid,session,objs);
	}
	/** �ַ� */
	public void handle(int uuid,Session session,Object[] objs)
	{
		SendCommand command=getPort(uuid);
		if(log.isDebugEnabled())
			log.debug(",uuid="+uuid+",command="+command);
		if(command==null) return;
		command.send(session,objs);
	}
	/* common methods */

	/* inner class */
}
