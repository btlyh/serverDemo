package com.cambrian.game.util;

import com.cambrian.common.net.ByteBuffer;

/**
 * ��˵����
 * 
 * @version 2013-4-23
 * @author HYZ (huangyz1988@qq.com)
 */
public interface BytesWriter
{

	/** �ֽ�д�� */
	public abstract void bytesWrite(Object paramObject,
		ByteBuffer paramByteBuffer);
}