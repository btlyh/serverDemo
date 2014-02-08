/**
 * 
 */
package com.cambrian.common.timer;

import com.cambrian.common.log.Logger;
import com.cambrian.common.thread.ThreadKit;

/**
 * ��˵������ʱ�����ġ�һ�����ڷ������ˡ� �ṩ���뼶���뼶��ʱ�������Ӽ���ʱ������������Ӷ�ʱ����ִ�������
 * �����ʱ���̱߳��������������л�������������������Ӧ��鲢ȥ������ ���ӡ�̶߳�ջ�������߳̽������ں������̡߳�
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */

public final class TimerCenter implements Runnable
{

	/* static fields */
	/** Ĭ�ϵĺ��뼶���뼶��ʱ�������Ӽ���ʱ��������ʱ�� */
	public static final int MILLIS_TIME=10,SECOND_TIME=200,MINUTE_TIME=4000;
	/** Ĭ�ϵĺ��뼶���뼶��ʱ�������Ӽ���ʱ���ĳ�ʱʱ�� */
	public static final int MILLIS_TIMEOUT=500,SECOND_TIMEOUT=10000,
					MINUTE_TIMEOUT=200000;
	/** Ĭ�ϵļ��ʱ�� */
	public static final int COLLATE_TIME=1000;

	/** ��ǰ�Ķ�ʱ������ */
	private static TimerCenter center=new TimerCenter();

	/** ��־��¼ */
	private static final Logger log=Logger.getLogger(TimerCenter.class);

	/* static methods */
	/** ��õ�ǰ�Ķ�ʱ������ */
	public static TimerCenter getInstance()
	{
		return center;
	}
	/** ��ú��뼶��ʱ�� */
	public static Timer getMillisTimer()
	{
		return center.getMillisThread().timer;
	}
	/** ����뼶��ʱ�� */
	public static Timer getSecondTimer()
	{
		return center.getSecondThread().timer;
	}
	/** ��÷��Ӽ���ʱ�� */
	public static Timer getMinuteTimer()
	{
		return center.getMinuteThread().timer;
	}

	/* fields */
	/** ���뼶��ʱ���߳� */
	TimerThread millisThread;
	/** �뼶��ʱ���߳� */
	TimerThread secondThread;
	/** ���Ӽ���ʱ���߳� */
	TimerThread minuteThread;

	/** ����̵߳Ĺ���ʱ���� */
	private int runTime=COLLATE_TIME;
	/** ����߳� */
	private Thread run;

	/* constructors */
	/** ���췽�� */
	TimerCenter()
	{
		run=new Thread(this);
		run.setName(run.getName()+"@"+getClass().getName()+"@"+hashCode()
			+"/"+runTime);
		run.setDaemon(true);
		run.start();
	}
	/* properties */
	/** ��ú��뼶��ʱ���߳� */
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
	/** ����뼶��ʱ���߳� */
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
	/** ��÷��Ӽ���ʱ���߳� */
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
	/** ������ */
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
	/** ѭ���������� */
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