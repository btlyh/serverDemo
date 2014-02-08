/** */
package com.cambrian.game.cc;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.Command;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.game.Session;

/**
 * 类说明：认证-维持活动
 * 
 * @version 1.0
 * @date 2013-5-29
 * @author HYZ (huangyz1988@qq.com)
 */
public class ActiveCommand extends Command
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
		cc.active(sid,session);
	}

}
