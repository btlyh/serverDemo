package com.cambrian.common.actor;

import com.cambrian.common.log.Logger;

/**
 * 类说明：角色模式-功能处理分发器
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class ProxyActorProcess
{

	/* static fields */
	/** 日志 */
	private static final Logger log=Logger
		.getLogger(ProxyActorProcess.class);
	/**  */
	public static ProxyActorProcess proxy=new ProxyActorProcess();

	/* static methods */
	/** 分发 */
	public static boolean handle(int uuid,Actor actor,Object value)
	{
		Process process=proxy.getProcess(uuid);
		if(log.isDebugEnabled())
			log.debug(",uuid="+uuid+",process="+process);
		if(process==null) return false;
		return process.handle(actor,value);
	}

	/* fields */
	/** 功能处理器 */
	Process[] handlers=new Process[0xffff];

	/* constructors */

	/* properties */

	/* init start */

	/* methods */
	/** 获取指定功能处理器 */
	public Process getProcess(int id)
	{
		return this.handlers[id];
	}
	/** 设置指定功能处理器 */
	public void setProcess(int id,Process handler)
	{
		if(log.isDebugEnabled())
			log.debug(",setProcess ,id="+id+",handler="+handler);
		this.handlers[id]=handler;
	}

	/* common methods */

	/* inner class */
}
