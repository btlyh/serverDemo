/** */
package com.cambrian.common.object;

import com.cambrian.common.net.ByteBuffer;

/**
 * ��˵��������ģ��
 * 
 * @version 2013-4-12
 * @author HYZ (huangyz1988@qq.com)
 */
public class Sample implements Cloneable, Serializable, DBSerializable {

	/** ģ�幤�� */
	public static SampleFactory factory = new SampleFactory();

	/** ��ȡģ�幤�� */
	public static SampleFactory getFactory() {
		return factory;
	}

	/** ����ģ�幤�� */
	public static void setFactory(SampleFactory factory) {
		Sample.factory = factory;
	}

	/** Ψһid */
	int uid;
	/** ģ��sid */
	int sid;
	/** Դ */
	Object source;

	/** ��ȡsid */
	public int getSid() {
		return sid;
	}

	/** ����sid */
	public void setSid(int sid) {
		this.sid = sid;
	}

	/** ��ȡΨһid */
	public int getId() {
		return ((uid != 0) ? uid : getSid());
	}

	/** ��ȡԴ */
	public Object getSource() {
		return source;
	}

	/** ����Դ */
	public void setSource(Object source) {
		this.source = source;
	}

	/** ��id */
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

	/** ����һЩ���飨��һ������ģ���������ݣ�����������Ҫ��һ������������Ҫ��д�÷��� */
	public Sample dosome() {
		return this;
	}

	/** �������л�Ϊ�ֽڻ��� */
	public void bytesWrite(ByteBuffer data) {
		data.writeShort(sid);
	}

	/** �ֽڻ��淴���л�Ϊ���� */
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
		// ���ﲻ��ȡsid�����ⲿ��ȡ
		uid=data.readInt();
	}

	/** ���󸱱� */
	public Object copy(Object obj) {
		return obj;
	}

	/** ��¡ */
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