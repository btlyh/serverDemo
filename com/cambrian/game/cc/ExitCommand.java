/** */
package com.cambrian.game.cc;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.Command;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.game.Session;

/**
 * 类说明：退出
 * 
 * @version 2013-5-9
 * @author HYZ (huangyz1988@qq.com)
 */
public class ExitCommand extends Command
{

	CertifyCenter cc;

	public void setCC(CertifyCenter cc)
	{
		this.cc=cc;
	}
	@Override
	public void handle(NioTcpConnect connect,ByteBuffer data)
	{
		String sid=data.readUTF();
		Session session=connect.getSession();
		cc.exit(sid,session);
	}

}
