package com.cambrian.gc.notice;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.dfhm.SendCommand;
import com.cambrian.game.Session;

/**
 * 类说明：卡牌修改推送
 * @author：LazyBear
 * 
 */
public class CardUpdateNotice extends SendCommand
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
		data.writeInt(Integer.parseInt(objs[3].toString()));
		data.writeInt(Integer.parseInt(objs[4].toString()));
		data.writeInt(Integer.parseInt(objs[5].toString()));
		data.writeInt(Integer.parseInt(objs[6].toString()));
		data.writeInt(Integer.parseInt(objs[7].toString()));
		data.writeInt(Integer.parseInt(objs[8].toString()));
		connect.send(noticePort,data);
	}
}
