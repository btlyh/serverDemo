/** */
package com.cambrian.game.cc;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.Command;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.game.Session;

/**
 * 类说明：认证-获取sid
 * 
 * @version 2013-5-9
 * @author HYZ (huangyz1988@qq.com)
 */
public class CertifyCommand extends Command
{

	CertifyCenter cc;

	public void setCC(CertifyCenter cc)
	{
		this.cc=cc;
	}

	@Override
	public void handle(NioTcpConnect connect,ByteBuffer data)
	{
		String info = data.readUTF();
		System.out.println("info:"+info);
		System.err.println("CertifyCommand --------------");
		System.err.println("info ==="+info);
		Session session = connect.getSession();
		session = cc.certify(info, connect.getURL().getHost(), session);
		String sid = (String)session.getSource();
		data.clear();
		data.writeUTF(sid);
	}
}
