/**
 * 
 */
package com.cambrian.common.object;

import com.cambrian.common.net.ByteBuffer;

/**
 * 类说明：基础对象
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class BaseObject implements Serializable
{

	/* static fields */

	/* static methods */

	/* fields */
	/** 唯一id */
	public int uuid;
	/** 类型id */
	public int id;
	/** 种类 */
	public int type;

	/* constructors */

	/* properties */

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id=id;
	}
	/* init start */

	/* methods */

	/*
	 * (non-Javadoc)
	 * @see
	 * com.main.common.object.Serializable#writeObject(com.main.common.net
	 * .ChannelBuffer)
	 */
	public void bytesWrite(ByteBuffer data)
	{
		data.writeInt(type);
		data.writeInt(id);
		data.writeInt(uuid);
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * com.main.common.object.Serializable#readObject(com.main.common.net
	 * .ChannelBuffer)
	 */
	public void bytesRead(ByteBuffer data)
	{
		this.type=data.readInt();
		this.id=data.readInt();
		this.uuid=data.readInt();
	}
	/* common methods */

	/* inner class */

}
