package com.cambrian.game.util;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.dfhm.player.Player;

/**
 * 类说明：玩家数据持久序列化
 * 
 * @version 2013-5-10
 * @author HYZ (huangyz1988@qq.com)
 */
public class PlayerDCRW implements BytesRW
{

	/** 玩家数据反序列化 */
	public Object bytesRead(ByteBuffer data)
	{
		Player p=new Player();
		System.out.println("---------------收到数据---------------");
		p.dbBytesRead(data);
		return p;
	}

	/** 玩家数据序列化 */
	public void bytesWrite(Object obj,ByteBuffer data)
	{
		Player p=(Player)obj;
		p.dbBytesWrite(data);
	}

}
