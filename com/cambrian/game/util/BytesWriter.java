package com.cambrian.game.util;

import com.cambrian.common.net.ByteBuffer;

/**
 * 类说明：
 * 
 * @version 2013-4-23
 * @author HYZ (huangyz1988@qq.com)
 */
public interface BytesWriter
{

	/** 字节写入 */
	public abstract void bytesWrite(Object paramObject,
		ByteBuffer paramByteBuffer);
}