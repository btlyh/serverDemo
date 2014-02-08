/**
 * 
 */
package com.cambrian.common.timer;

import com.cambrian.common.log.Logger;
import com.cambrian.common.thread.ThreadKit;

/**
 * 类说明：定时器中心。一般用于服务器端。 提供毫秒级、秒级定时器、分钟级定时器。并负责监视定时器的执行情况。
 * 如果定时器线程崩溃、阻塞或运行缓慢（表明有重型任务，应检查并去除）， 则打印线程堆栈，并在线程结束或超期后重启线程。
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */

public final class TimerCenter implements Runnable
{

	/* static fields */
	/** 默认的毫秒级、秒级定时器、分钟级定时器的运行时间 */
	public static final int MILLIS_TIME=10,SECOND_TIME=200,MINUTE_TIME=4000;
	/** 默认的毫秒级、秒级定时器、分钟级定时器的超时时间 */
	public static final int MILLIS_TIMEOUT=500,SECOND_TIMEOUT=10000,
					MINUTE_TIMEOUT=200000;
	/** 默认的监控时间 */
	public static final int COLLATE_TIME=1000;

	/** 当前的定时器中心 */
	private static TimerCenter center=new TimerCenter();

	/** 日志记录 */
	private static final Logger log=Logger.getLogger(TimerCenter.class);

	/* static methods */
	/** 获得当前的定时器中心 */
	public static TimerCenter getInstance()
	{
		return center;
	}
	/** 获得毫秒级定时器 */
	public static Timer getMillisTimer()
	{
		return center.getMillisThread().timer;
	}
	/** 获得秒级定时器 */
	public static Timer getSecondTimer()
	{
		return center.getSecondThread().timer;
	}
	/** 获得分钟级定时器 */
	public static Timer getMinuteTimer()
	{
		return center.getMinuteThread().timer;
	}

	/* fields */
	/** 毫秒级定时器线程 */
	TimerThread millisThread;
	/** 秒级定时器线程 */
	TimerThread secondThread;
	/** 分钟级定时器线程 */
	TimerThread minuteThread;

	/** 监控线程的工作时间间隔 */
	private int runTime=COLLATE_TIME;
	/** 监控线程 */
	private Thread run;

	/* constructors */
	/** 构造方法 */
	TimerCenter()
	{
		run=new Thread(this);
		run.setName(run.getName()+"@"+getClass().getName()+"@"+hashCode()
			+"/"+runTime);
		run.setDaemon(true);
		run.start();
	}
	/* properties */
	/** 获得毫秒级定时器线程 */
	public synchronized TimerThread getMillisThread()
	{
		if(millisThread==null)
		{
			millisThread=new TimerThread(new Timer(),MILLIS_TIME,
				MILLIS_TIMEOUT);
			millisThread.start();
		}
		return millisThread;
	}
	/** 获得秒级定时器线程 */
	public synchronized TimerThread getSecondThread()
	{
		if(secondThread==null)
		{
			secondThread=new TimerThread(new Timer(),SECOND_TIME,
				SECOND_TIMEOUT);
			secondThread.start();
		}
		return secondThread;
	}
	/** 获得分钟级定时器线程 */
	public synchronized TimerThread getMinuteThread()
	{
		if(minuteThread==null)
		{
			minuteThread=new TimerThread(new Timer(),MINUTE_TIME,
				MINUTE_TIMEOUT);
			minuteThread.start();
		}
		return minuteThread;
	}
	/* methods */
	/** 整理方法 */
	public synchronized void collate(long time)
	{
		if(millisThread!=null
			&&(millisThread.isTimeout(time)||!millisThread.isAlive()))
		{
			if(log.isWarnEnabled())
				log.warn("collate, millisThread timeout, "
					+ThreadKit.toString(millisThread));
			millisThread.close();
			millisThread=new TimerThread(millisThread);
			millisThread.start();
			if(log.isWarnEnabled())
				log.warn("collate, millisThread start, "+millisThread);
		}
		if(secondThread!=null
			&&(secondThread.isTimeout(time)||!secondThread.isAlive()))
		{
			if(log.isWarnEnabled())
				log.warn("collate, secondThread timeout, "
					+ThreadKit.toString(secondThread));
			secondThread.close();
			secondThread=new TimerThread(secondThread);
			secondThread.start();
			if(log.isWarnEnabled())
				log.warn("collate, secondThread start, "+secondThread);
		}
		if(minuteThread!=null
			&&(minuteThread.isTimeout(time)||!minuteThread.isAlive()))
		{
			if(log.isWarnEnabled())
				log.warn("collate, minuteThread timeout, "
					+ThreadKit.toString(minuteThread));
			minuteThread.close();
			minuteThread=new TimerThread(minuteThread);
			minuteThread.start();
			if(log.isWarnEnabled())
				log.warn("collate, minuteThread start, "+minuteThread);
		}
	}
	/** 循环监听方法 */
	public void run()
	{
		while(true)
		{
			collate(System.currentTimeMillis());
			try
			{
				Thread.sleep(runTime);
			}
			catch(InterruptedException e)
			{
			}
		}
	}
}