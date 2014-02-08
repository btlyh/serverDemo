/** */
package com.cambrian.game.util;

import com.cambrian.common.net.ByteBuffer;

/**
 * 类说明：
 * 
 * @version 2013-4-23
 * @author HYZ (huangyz1988@qq.com)
 */
public interface BytesReader
{

	/** 字节读取 */
	public abstract Object bytesRead(ByteBuffer paramByteBuffer);
}
