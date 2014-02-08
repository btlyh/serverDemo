package com.cambrian.game.ds;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.Command;
import com.cambrian.common.net.NioTcpConnect;

/**
 * ÀàËµÃ÷£º
 * @author£ºLazyBear
 *
 */
public class GetRandomNameCommand extends Command
{
	DataServer ds;

	public void setDS(DataServer ds)
	{
		this.ds=ds;
	}
	
	@Override
	public void handle(NioTcpConnect connect,ByteBuffer data)
	{
		data.clear();
		String nickName = ds.getRandomName();
		data.writeUTF(nickName);
	}
}
