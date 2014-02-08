package com.cambrian.game.util;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.dfhm.player.Player;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 2013-5-14
 * @author HYZ (huangyz1988@qq.com)
 */
public class PlayerWriter implements BytesRW
{

	public Object bytesRead(ByteBuffer data)
	{
		return null;
	}

	public void bytesWrite(Object obj,ByteBuffer data)
	{
		Player p=(Player)obj;
		p.bytesWrite(data);
		p.initLogin();
	}

}
