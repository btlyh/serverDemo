package com.cambrian.game;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.Command;
import com.cambrian.common.net.DataAccessException;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.dfhm.Lang;

/**
 * 类说明：
 * 
 * @version 1.0
 * @date 2013-6-8
 * @author maxw<woldits@qq.com>
 */
public abstract class SessionCommand extends Command
{

	/*
	 * (non-Javadoc)
	 * @see com.cambrian.common.net.Command#handle(com.cambrian.common.net.
	 * NioTcpConnect, com.cambrian.common.net.ByteBuffer)
	 */
	@Override
	public void handle(NioTcpConnect connect,ByteBuffer data)
	{
		Session session=connect.getSession();
		if(session==null) throw new DataAccessException(601,Lang.F9000_SDE);
		handle(session,data);
	}
	/** 处理 */
	public abstract void handle(Session session,ByteBuffer data);
}
