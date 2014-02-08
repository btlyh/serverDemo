/**
 * 
 */
package com.cambrian.common.object;

import com.cambrian.common.net.ByteBuffer;

/**
 * 类说明：空间对象
 * 
 * @version 1.0
 * @author
 */
public class SpaceObject extends BaseObject
{

	/* static fields */

	/* static methods */

	/* fields */
	/** 位置 */
	public int x,y,z;
	/** 大小 */
	public int length,width,height;

	/* constructors */

	/* properties */

	/* init start */

	/* methods */
	/** 指定点是否在范围内 */
	/**  */
	public boolean isIn(int x)
	{
		return this.x+width>=x&&this.x<=x;
	}
	/**  */
	public boolean isIn(int x,int y)
	{
		return (this.x+width>=x&&this.x<=x)&&(this.y+length>=x&&this.y<=y);
	}
	/**  */
	public boolean isIn(int x,int y,int z)
	{
		return (this.x+width>=x&&this.x<=x)&&(this.y+length>=x&&this.y<=y)
			&&(this.z+height>=x&&this.z<=z);
	}
	/**  */
	public boolean isCrossLine(SpaceObject object)
	{
		return isIn(object.x)||isIn(object.x+object.width)||object.isIn(x)
			||object.isIn(x+width);
	}
	/**  */
	public boolean isCrossSurface(SpaceObject object)
	{
		return isIn(object.x,object.y)
			||isIn(object.x,object.y+object.length)
			||isIn(object.x+object.width,object.y)
			||isIn(object.x+object.width,object.y+object.length)
			||object.isIn(x,y)||object.isIn(x,y+length)
			||object.isIn(x+width,y)||object.isIn(x+width,y+length);
	}
	/** 驱动方法 */
	public void run(long time)
	{

	}
	/* common methods */
	/*
	 * (non-Javadoc)
	 * @see com.main.common.object.BaseObject#writeObject(com.main.common.
	 * net.ChannelBuffer)
	 */
	@Override
	public void bytesWrite(ByteBuffer data)
	{
		super.bytesWrite(data);
		data.writeInt(x);
		data.writeInt(y);
		data.writeInt(z);
		data.writeInt(length);
		data.writeInt(width);
		data.writeInt(height);
	}
	/*
	 * (non-Javadoc)
	 * @see com.main.common.object.BaseObject#readObject(com.main.common.net
	 * .ChannelBuffer)
	 */
	@Override
	public void bytesRead(ByteBuffer data)
	{
		super.bytesRead(data);
		this.x=data.readInt();
		this.y=data.readInt();
		this.z=data.readInt();
		this.length=data.readInt();
		this.width=data.readInt();
		this.height=data.readInt();
	}
	/* inner class */

}
