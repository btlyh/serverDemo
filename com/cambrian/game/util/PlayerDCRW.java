package com.cambrian.game.util;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.dfhm.player.Player;

/**
 * ��˵����������ݳ־����л�
 * 
 * @version 2013-5-10
 * @author HYZ (huangyz1988@qq.com)
 */
public class PlayerDCRW implements BytesRW
{

	/** ������ݷ����л� */
	public Object bytesRead(ByteBuffer data)
	{
		Player p=new Player();
		System.out.println("---------------�յ�����---------------");
		p.dbBytesRead(data);
		return p;
	}

	/** ����������л� */
	public void bytesWrite(Object obj,ByteBuffer data)
	{
		Player p=(Player)obj;
		p.dbBytesWrite(data);
	}

}
