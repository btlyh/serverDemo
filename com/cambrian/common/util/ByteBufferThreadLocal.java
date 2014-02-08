/** */
package com.cambrian.common.util;

import com.cambrian.common.net.ByteBuffer;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 2013-4-26
 * @author HYZ (huangyz1988@qq.com)
 */
@SuppressWarnings("rawtypes")
public final class ByteBufferThreadLocal extends ThreadLocal
{

	private static final ThreadLocal instance=new ByteBufferThreadLocal();

	public static ByteBuffer getByteBuffer()
	{
		return ((ByteBuffer)instance.get());
	}

	protected Object initialValue()
	{
		return new ByteBuffer();
	}
}