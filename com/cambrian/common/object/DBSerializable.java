package com.cambrian.common.object;

import com.cambrian.common.net.ByteBuffer;

/**
 * 类说明：数据永久化序列化
 * 
 * @version 1.0
 * @date 2013-6-8
 * @author maxw<woldits@qq.com>
 */
public interface DBSerializable
{

	/** 序列化 */
	void dbBytesWrite(ByteBuffer data);
	/** 反序列化 */
	void dbBytesRead(ByteBuffer data);
}
