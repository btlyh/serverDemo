/**
 * 
 */
package com.cambrian.common.net;

import com.cambrian.common.log.Logger;

/**
 * ��˵����
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public abstract class Command// implements TransmitHandler
{

	/* static fields */
	public static final Logger log=Logger.getLogger(Command.class);

	/** ������:�ɹ�,���� */
	public static final short OK=0,ERROR=100;
	/* static methods */

	/* fields */
	/** ��������id */
	protected short retCommandId;

	/* constructors */

	/* properties */

	/* init start */

	/* methods */
	/** �����޷�����Ϣ */
	public void transmit(NioTcpConnect connect,ByteBuffer data)
	{
		// if(!checkConnect(connect))
		// {
		// data.clear();
		// }
		// else
		// {
		// try
		// {
		handle(connect,data);
		// }
		// catch(Exception e)
		// {
		// data.clear();
		// data.writeShort(ERROR);
		// data.writeUTF(e.getMessage());
		// }
		// }
	}
	/** �����޷�����Ϣ */
	public void transmit(int recmd,int reuuid,NioTcpConnect connect,
		ByteBuffer data)
	{
		if(log.isDebugEnabled())
			log.debug("transmit, recmd="+recmd+" reuuid="+reuuid);
		System.err.println("transmit, recmd="+recmd+" reuuid="+reuuid);
		// if(checkConnect(connect))
		// {
		 try
		 {
		handle(connect,data);
		ByteBuffer bb=new ByteBuffer();
		bb.writeInt(reuuid);
		bb.writeShort(OK);
		bb.write(data.getArray(),data.offset,data.length());
		data=bb;
		 }
		 catch(Exception e)
		 {
		 e.printStackTrace();// �����Ҵ�
		 data.clear();
		 data.writeInt(reuuid);
		 data.writeShort(ERROR);
		 data.writeUTF(e.getMessage());
		 }
		//		 }
		// else
		// {
		// data.clear();
		// data.writeInt(reuuid);
		// data.writeShort(ERROR);
		// data.writeUTF("connect error");
		// }
		connect.send((short)recmd,reuuid,data);
	}
	/** ���� */
	public abstract void handle(NioTcpConnect connect,ByteBuffer data);

	/* common methods */

	/* inner class */

}
