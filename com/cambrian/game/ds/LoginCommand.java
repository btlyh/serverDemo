/** */
package com.cambrian.game.ds;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.Command;
import com.cambrian.common.net.NioTcpConnect;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 2013-4-27
 * @author HYZ (huangyz1988@qq.com)
 */
public class LoginCommand extends Command
{

	DataServer ds;

	public void setDS(DataServer ds)
	{
		this.ds=ds;
	}

	@Override
	public void handle(NioTcpConnect connect,ByteBuffer data)
	{
		String sid=data.readUTF();
		boolean renew=false;
		if(data.length()>0) renew=data.readBoolean();
		ds.login(sid,renew,connect);
	}

}
