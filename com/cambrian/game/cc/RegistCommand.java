/** */
package com.cambrian.game.cc;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.Command;
import com.cambrian.common.net.NioTcpConnect;

/**
 * ÀàËµÃ÷£º×¢²á
 * 
 * @version 2013-5-10
 * @author HYZ (huangyz1988@qq.com)
 */
public class RegistCommand extends Command
{

	CertifyCenter cc;

	public void setCC(CertifyCenter cc)
	{
		this.cc=cc;
	}

	@Override
	public void handle(NioTcpConnect connect,ByteBuffer data)
	{
		DBAccess dba=cc.getDBAccess();
		String str=data.readUTF();
		int serverId=data.readInt();
		data.clear();
		data.writeBoolean(dba.register(str,serverId));
	}

}
