package com.cambrian.common.object;

import com.cambrian.common.net.ByteBuffer;

/**
 * 类说明：数据序列化
 * 
 * @version 1.0
 * @date 2013-6-8
 * @author maxw<woldits@qq.com>
 */
public interface Serializable
{

	/** 序列化 */
	void bytesWrite(ByteBuffer data);
	/** 反序列化 */
	void bytesRead(ByteBuffer data);
}
