package com.cambrian.dfhm;

import com.cambrian.game.Session;

/**
 * 类说明：通信-发送信息
 * 
 * @version 1.0
 * @date 2013-6-7
 * @author maxw<woldits@qq.com>
 */
public abstract class SendCommand
{

	/* static fields */

	/* static methods */

	/* fields */
	/** 命令号  */
	public int id;
	
	/* constructors */

	/* properties */

	/* init start */

	/* methods */
	/**  */
	public abstract void send(Session session,Object[] objs);
	
	/* common methods */

	/* inner class */
}
