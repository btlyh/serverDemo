package com.cambrian.game.cc;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.Command;
import com.cambrian.common.net.NioTcpConnect;

/**
 * 类说明：关闭CC进程接口
 * 
 * @version 2013-5-8
 * @author HYZ (huangyz1988@qq.com)
 */
public class CloseCommand extends Command
{

	/** 账号,密码 */
	String[] info;
	/** 认证中心 */
	CertifyCenter cc;

	/** 设置帐号密码 */
	public void setIDPW(String[] info)
	{
		this.info=info;
	}

	/** 设置认证中心 */
	public void setCC(CertifyCenter cc)
	{
		this.cc=cc;
	}

	@Override
	public void handle(NioTcpConnect connect,ByteBuffer data)
	{
		String id=data.readUTF();
		String pw=data.readUTF();
		if(info[0].equals(id)&&info[1].equals(pw))
		{
			cc.close();
		}
	}
}