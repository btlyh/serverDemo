package com.cambrian.gc.notice;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.dfhm.SendCommand;
import com.cambrian.game.Session;

/**
 * 类说明： 道具更新
 * @author：LazyBear
 * 
 */
public class ProUpdateNotice extends SendCommand
{

	/* static fields */

	/* static methods */

	/* fields */
	/** 消息通知端口*/
	short noticePort;
	/* constructors */

	/* properties */
	public void setPort(short noticePort)
	{
		this.noticePort = noticePort;
	}
	/* init start */

	/* methods */
	@Override
	public void send(Session session,Object[] objs)
	{
		NioTcpConnect connect = session.getConnect();
		if(connect==null)
		{
			return;
		}
		ByteBuffer data = new ByteBuffer();
		data.writeInt(Integer.parseInt(objs[0].toString()));
		data.writeInt(Integer.parseInt(objs[1].toString()));
		data.writeInt(Integer.parseInt(objs[2].toString()));
		connect.send(noticePort,data);
	}
}
