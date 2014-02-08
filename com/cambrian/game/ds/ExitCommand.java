/** */
package com.cambrian.game.ds;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.Command;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.game.Session;

/**
 * 类说明：退出命令
 * 
 * @version 2013-5-6
 * @author HYZ (huangyz1988@qq.com)
 */
public class ExitCommand extends Command
{

	/** 数据服务 */
	DataServer ds;

	/** 设置数据服务 */
	public void setDS(DataServer ds)
	{
		this.ds=ds;
	}
	@Override
	public void handle(NioTcpConnect connect,ByteBuffer data)
	{
		data.clear();
		Session session=connect.getSession();
		ds.exit(session,data);
	}

}
