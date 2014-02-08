/**
 * 
 */
package com.cambrian.common.net;

import com.cambrian.common.log.Logger;

/**
 * 类说明：
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public abstract class Command// implements TransmitHandler
{

	/* static fields */
	public static final Logger log=Logger.getLogger(Command.class);

	/** 错误码:成功,错误 */
	public static final short OK=0,ERROR=100;
	/* static methods */

	/* fields */
	/** 返回命令id */
	protected short retCommandId;

	/* constructors */

	/* properties */

	/* init start */

	/* methods */
	/** 处理无返回消息 */
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
	/** 处理无返回消息 */
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
		 e.printStackTrace();// 方便找错
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
	/** 处理 */
	public abstract void handle(NioTcpConnect connect,ByteBuffer data);

	/* common methods */

	/* inner class */

}
