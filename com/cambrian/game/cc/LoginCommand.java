/** */
package com.cambrian.game.cc;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.Command;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.game.Session;

/**
 * 类说明：获取账号信息
 * 
 * @version 2013-5-9
 * @author HYZ (huangyz1988@qq.com)
 */
public class LoginCommand extends Command
{

	CertifyCenter cc;

	public void setCC(CertifyCenter cc)
	{
		this.cc=cc;
	}

	@Override
	public void handle(NioTcpConnect connect,ByteBuffer data)
	{
		System.err.println("cc.loginCommand ---------");
		System.err.println("host ==="+connect.getURL().getHost());
		String sid=data.readUTF();
		Session session=connect.getSession();
		session=cc.login(sid,connect.getURL().getHost(),session);
		data.clear();
		data.writeUTF(session.getId());
		data.writeUTF(session.getReference().toString());
	}

}
