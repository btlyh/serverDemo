package com.cambrian.dfhm.player;

import com.cambrian.common.actor.Actor;
import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.object.Sample;

/**
 * 类说明：角色类
 * 
 * @version 1.0
 * @author
 */
public class Player extends Sample implements Actor
{
	/** 玩家ID */
	private long userId;

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	/** 登录后执行 */
	public void initLogin()
	{
		// TODO Auto-generated method stub
		
	}
	/** 初始化玩家 */
	public void init()
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void bytesWrite(ByteBuffer data)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void dbBytesWrite(ByteBuffer data)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void dbBytesRead(ByteBuffer data)
	{
		// TODO Auto-generated method stub
	}
	/* inner class */
}
