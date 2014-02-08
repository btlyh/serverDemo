package com.cambrian.game.ds;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.common.net.ProxyCommand;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 2013-5-14
 * @author HYZ (huangyz1988@qq.com)
 */
public class CLLCommand extends ProxyCommand
{

	DataServer ds;

	public void setDS(DataServer ds)
	{
		this.ds=ds;
	}

	@Override
	public void handle(NioTcpConnect connect,ByteBuffer data)
	{
		System.err.println("ds.cllCommand -----------");
		String nickName = data.readUTF();
		int offset = data.offset();
		String info = data.readUTF();
		data.setOffset(offset);
		String[] infos = info.split(":");
		super.handle(connect,data);
		String sid = data.readUTF();
		ds.login(sid, false, connect);
		ds.load(connect.getSession(),data,nickName,infos[0]);
	}
	
}
