package com.cambrian.common.util;

import com.cambrian.common.net.CharBuffer;

@SuppressWarnings("rawtypes")
public final class CharBufferThreadLocal extends ThreadLocal
{

	private static final ThreadLocal instance=new CharBufferThreadLocal();

	public static CharBuffer getCharBuffer()
	{
		return (CharBuffer)instance.get();
	}

	protected Object initialValue()
	{
		return new CharBuffer();
	}
}
