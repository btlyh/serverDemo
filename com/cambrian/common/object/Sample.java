/** */
package com.cambrian.common.object;

import com.cambrian.common.net.ByteBuffer;

/**
 * 类说明：对象模板
 * 
 * @version 2013-4-12
 * @author HYZ (huangyz1988@qq.com)
 */
public class Sample implements Cloneable, Serializable, DBSerializable {

	/** 模板工厂 */
	public static SampleFactory factory = new SampleFactory();

	/** 获取模板工厂 */
	public static SampleFactory getFactory() {
		return factory;
	}

	/** 设置模板工厂 */
	public static void setFactory(SampleFactory factory) {
		Sample.factory = factory;
	}

	/** 唯一id */
	int uid;
	/** 模板sid */
	int sid;
	/** 源 */
	Object source;

	/** 获取sid */
	public int getSid() {
		return sid;
	}

	/** 设置sid */
	public void setSid(int sid) {
		this.sid = sid;
	}

	/** 获取唯一id */
	public int getId() {
		return ((uid != 0) ? uid : getSid());
	}

	/** 获取源 */
	public Object getSource() {
		return source;
	}

	/** 设置源 */
	public void setSource(Object source) {
		this.source = source;
	}

	/** 绑定id */
	public boolean bindUid(int id) {
		if (this.uid != 0)
			return false;
		this.uid = id;
		return true;
	}
	
	public int getUId()
	{
		return uid;
	}

	/** 处理一些事情（进一步解析模板配置数据），子类若需要进一步解析数据需要重写该方法 */
	public Sample dosome() {
		return this;
	}

	/** 对象序列化为字节缓存 */
	public void bytesWrite(ByteBuffer data) {
		data.writeShort(sid);
	}

	/** 字节缓存反序列化为对象 */
	public void bytesRead(ByteBuffer data) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cambrian.common.object.DBSerializable#dbBytesWrite(com.cambrian.common
	 * .net.ByteBuffer)
	 */

	public void dbBytesWrite(ByteBuffer data) {
		data.writeShort(sid);
		data.writeInt(uid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cambrian.common.object.DBSerializable#dbBytesRead(com.cambrian.common
	 * .net.ByteBuffer)
	 */

	public void dbBytesRead(ByteBuffer data) {
		// 这里不读取sid，在外部读取
		uid=data.readInt();
	}

	/** 对象副本 */
	public Object copy(Object obj) {
		return obj;
	}

	/** 克隆 */
	public Object clone() {
		try {
			return copy(super.clone());
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(super.getClass().getName()
					+ " clone, sid=" + this.sid, e);
		}
	}

//	public String toString() {
//		return "Sample: sid=\"" + sid + "\" uid=\"" + getId() + "\"";
//	}
}