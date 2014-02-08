package com.cambrian.common.actor;

import com.cambrian.common.log.Logger;

/**
 * ��˵������ɫģʽ-���ܴ���ַ���
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class ProxyActorProcess
{

	/* static fields */
	/** ��־ */
	private static final Logger log=Logger
		.getLogger(ProxyActorProcess.class);
	/**  */
	public static ProxyActorProcess proxy=new ProxyActorProcess();

	/* static methods */
	/** �ַ� */
	public static boolean handle(int uuid,Actor actor,Object value)
	{
		Process process=proxy.getProcess(uuid);
		if(log.isDebugEnabled())
			log.debug(",uuid="+uuid+",process="+process);
		if(process==null) return false;
		return process.handle(actor,value);
	}

	/* fields */
	/** ���ܴ����� */
	Process[] handlers=new Process[0xffff];

	/* constructors */

	/* properties */

	/* init start */

	/* methods */
	/** ��ȡָ�����ܴ����� */
	public Process getProcess(int id)
	{
		return this.handlers[id];
	}
	/** ����ָ�����ܴ����� */
	public void setProcess(int id,Process handler)
	{
		if(log.isDebugEnabled())
			log.debug(",setProcess ,id="+id+",handler="+handler);
		this.handlers[id]=handler;
	}

	/* common methods */

	/* inner class */
}
