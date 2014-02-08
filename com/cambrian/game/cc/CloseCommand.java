package com.cambrian.game.cc;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.Command;
import com.cambrian.common.net.NioTcpConnect;

/**
 * ��˵�����ر�CC���̽ӿ�
 * 
 * @version 2013-5-8
 * @author HYZ (huangyz1988@qq.com)
 */
public class CloseCommand extends Command
{

	/** �˺�,���� */
	String[] info;
	/** ��֤���� */
	CertifyCenter cc;

	/** �����ʺ����� */
	public void setIDPW(String[] info)
	{
		this.info=info;
	}

	/** ������֤���� */
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