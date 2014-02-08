package com.cambrian.game.cc;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.Command;
import com.cambrian.common.net.NioTcpConnect;

/**
 * ��˵�����ر�CC��������
 * 
 * @author��LazyBear
 */
public class ShutDownCommand extends Command
{

	/* static fields */

	/* static methods */

	/* fields */
	CertifyCenter cc;

	/* constructors */

	/* properties */
	public void setCC(CertifyCenter cc)
	{
		this.cc=cc;
	}
	/* init start */

	/* methods */
	@Override
	public void handle(NioTcpConnect connect,ByteBuffer data)
	{
		System.err.println("shut down now !!!");
		System.exit(0);
	}

}
