package com.cambrian.game.ds;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.Command;
import com.cambrian.common.net.NioTcpConnect;

/**
 * 类说明: 账号注册
 * 
 * @author：LazyBear
 */
public class RegistCommand extends Command
{

	/* static fields */

	/* static methods */

	/* fields */
	/** 服务器数据对象 */
	DataServer ds;

	/* constructors */

	/* properties */
	public void setDS(DataServer ds)
	{
		this.ds=ds;
	}
	/* init start */

	/* methods */
	@Override
	public void handle(NioTcpConnect connect,ByteBuffer data)
	{
		int regType=data.readInt();
		String info=data.readUTF();
		String backInfo=ds.regist(regType,info);
		if(backInfo!=null)
		{
			data.clear();
			data.writeUTF(backInfo);
		}
	}
}
