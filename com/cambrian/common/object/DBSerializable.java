package com.cambrian.common.object;

import com.cambrian.common.net.ByteBuffer;

/**
 * ��˵�����������û����л�
 * 
 * @version 1.0
 * @date 2013-6-8
 * @author maxw<woldits@qq.com>
 */
public interface DBSerializable
{

	/** ���л� */
	void dbBytesWrite(ByteBuffer data);
	/** �����л� */
	void dbBytesRead(ByteBuffer data);
}
