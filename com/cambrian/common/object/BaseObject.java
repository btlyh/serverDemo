/**
 * 
 */
package com.cambrian.common.object;

import com.cambrian.common.net.ByteBuffer;

/**
 * ��˵������������
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class BaseObject implements Serializable
{

	/* static fields */

	/* static methods */

	/* fields */
	/** Ψһid */
	public int uuid;
	/** ����id */
	public int id;
	/** ���� */
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
