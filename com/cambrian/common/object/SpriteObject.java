/**
 * 
 */
package com.cambrian.common.object;

import com.cambrian.common.net.ByteBuffer;

/**
 * ��˵�����������
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class SpriteObject extends SpaceObject
{

	/* static fields */

	/* static methods */

	/* fields */
	/** ״̬ */
	public int state;
	/** �ٶ� */
	public int speedX,speedY,speedZ;
	/** ��Ϊ���� */
	public int action;
	/**  */
	public long lasttime;

	/* constructors */

	/* properties */

	/* init start */

	/* methods */
	/*
	 * (non-Javadoc)
	 * @see com.main.common.object.SpaceObject#run(long)
	 */
	@Override
	public void run(long time)
	{
		// TODO Auto-generated method stub
		super.run(time);
		int space=(int)(time-lasttime);
		this.x+=speedX*space;
		this.y+=speedY*space;
		this.z+=speedZ*space;
		this.lasttime=time;
	}
	/* common methods */
	/*
	 * (non-Javadoc)
	 * @see
	 * com.main.common.object.SpaceObject#writeObject(com.main.common.net
	 * .ChannelBuffer)
	 */
	@Override
	public void bytesWrite(ByteBuffer data)
	{
		super.bytesWrite(data);
		data.writeInt(state);
		data.writeInt(speedX);
		data.writeInt(speedY);
		data.writeInt(speedZ);
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * com.main.common.object.SpaceObject#readObject(com.main.common.net.
	 * ChannelBuffer)
	 */
	@Override
	public void bytesRead(ByteBuffer data)
	{
		super.bytesRead(data);
		this.state=data.readInt();
		this.speedX=data.readInt();
		this.speedY=data.readInt();
		this.speedZ=data.readInt();
	}
	/* inner class */

}
