package com.cambrian.common.object;

import com.cambrian.common.net.ByteBuffer;

/**
 * ��˵�����������л�
 * 
 * @version 1.0
 * @date 2013-6-8
 * @author maxw<woldits@qq.com>
 */
public interface Serializable
{

	/** ���л� */
	void bytesWrite(ByteBuffer data);
	/** �����л� */
	void bytesRead(ByteBuffer data);
}
