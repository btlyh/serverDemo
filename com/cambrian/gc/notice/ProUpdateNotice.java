package com.cambrian.gc.notice;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.dfhm.SendCommand;
import com.cambrian.game.Session;

/**
 * ��˵���� ���߸���
 * @author��LazyBear
 * 
 */
public class ProUpdateNotice extends SendCommand
{

	/* static fields */

	/* static methods */

	/* fields */
	/** ��Ϣ֪ͨ�˿�*/
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
