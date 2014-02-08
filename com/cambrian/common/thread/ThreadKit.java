/**
 * 
 */
package com.cambrian.common.thread;

import java.util.Iterator;
import java.util.Map;

import com.cambrian.common.log.Logger;
import com.cambrian.common.net.CharBuffer;

public final class ThreadKit
{

	public static final String toString=ThreadKit.class.getName();
	public static final int THREAD_TOSTRING_SIZE=1024;
	public static final String STACKTRACEELEMENT_PREFIX="at ";
	static final Logger log=Logger.getLogger(ThreadKit.class);

	public static String toString(Thread paramThread)
	{
		CharBuffer localCharBuffer=new CharBuffer(THREAD_TOSTRING_SIZE);
		localCharBuffer.append(paramThread.toString()).append('\n');
		StackTraceElement[] arrayOfStackTraceElement=paramThread
			.getStackTrace();
		for(int i=0;i<arrayOfStackTraceElement.length;i++)
			localCharBuffer.append('\t').append("at ")
				.append(arrayOfStackTraceElement[i]).append('\n');
		return localCharBuffer.getString();
	}

	public static void logAllStackTraces()
	{
		Map<?,?> localMap=Thread.getAllStackTraces();
		CharBuffer localCharBuffer=new CharBuffer(THREAD_TOSTRING_SIZE
			*localMap.size());
		localCharBuffer.append("logAllStackTraces,").append('\n');

		Iterator<?> localIterator=localMap.entrySet().iterator();
		while(localIterator.hasNext())
		{
			Map.Entry<?,?> localEntry=(Map.Entry<?,?>)localIterator.next();
			localCharBuffer.append(localEntry.getKey().toString()).append(
				'\n');
			StackTraceElement[] arrayOfStackTraceElement=(StackTraceElement[])localEntry
				.getValue();
			for(int i=0;i<arrayOfStackTraceElement.length;i++)
				localCharBuffer.append('\t').append("at ")
					.append(arrayOfStackTraceElement[i]).append('\n');
		}
		log.warn(localCharBuffer.getString());
	}

	public static void delay(int paramInt)
	{
		try
		{
			Thread.sleep(paramInt);
		}
		catch(InterruptedException localInterruptedException)
		{
		}
	}
}