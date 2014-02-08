/**
 * 
 */
package com.cambrian.common.timer;

import com.cambrian.common.log.Logger;

/**
 * ��˵������ʱ���¼�
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */

public class TimerEvent
{

	/* static fields */
	/** ��������ѭ������ */
	public static final int INFINITE_CYCLE=0x7fffffff;

	/** ��־��¼ */
	private static final Logger log=Logger.getLogger(TimerEvent.class);

	/* fields */
	/** ��ʱ�¼������� */
	TimerListener listener;
	/** �¼��������� */
	Object parameter;
	/** ��ʱʱ�� */
	int intervalTime;
	/** ��ʱ���� */
	int count;
	/** ��ʼ�ӳ�ʱ�� */
	int initTime;
	/** ����ʱ�䶨ʱ���̹߳�����ʱ������ڶ�ʱʱ���� */
	boolean absolute;
	/** ��ʼʱ�� */
	long startTime;
	/** ��ǰ���е�ʱ�� */
	long currentTime;
	/** ��һ�����е�ʱ�� */
	long nextTime;
	/* constructors */
	/** ����һ����ʱ�¼�����Ĭ��Ϊ�޳�ʼ�ӳ�ʱ�䡢����ѭ�������ʱ�䶨ʱ */
	public TimerEvent(TimerListener listener,Object parameter,
		int intervalTime)
	{
		this(listener,parameter,intervalTime,INFINITE_CYCLE,0,false);
	}
	/** ����һ����ʱ�¼�����Ĭ��Ϊ�޳�ʼ�ӳ�ʱ�䡢����ѭ�� */
	public TimerEvent(TimerListener listener,Object parameter,
		int intervalTime,boolean absolute)
	{
		this(listener,parameter,intervalTime,INFINITE_CYCLE,0,absolute);
	}
	/** ����һ����ʱ�¼�����Ĭ��Ϊ�޳�ʼ�ӳ�ʱ�䣬���ʱ�䶨ʱ */
	public TimerEvent(TimerListener listener,Object parameter,
		int intervalTime,int count)
	{
		this(listener,parameter,intervalTime,count,0,false);
	}
	/** ����һ����ʱ�¼�����Ĭ��Ϊ�޳�ʼ�ӳ�ʱ�� */
	public TimerEvent(TimerListener listener,Object parameter,
		int intervalTime,int count,boolean absolute)
	{
		this(listener,parameter,intervalTime,count,0,absolute);
	}
	/** ����һ����ʱ�¼�����Ĭ��Ϊ���ʱ�䶨ʱ */
	public TimerEvent(TimerListener listener,Object parameter,
		int intervalTime,int count,int initTime)
	{
		this(listener,parameter,intervalTime,count,initTime,false);
	}
	/**
	 * ����һ����ʱԴ�¼�����
	 * 
	 * @param listener ��ʱ�¼�������
	 * @param parameter �¼���������
	 * @param intervalTime ��ʱʱ��
	 * @param count ��ʱ����
	 * @param initTime ��ʼ�ӳ�ʱ��
	 * @param absolute ����ʱ�䶨ʱ���̹߳�����ʱ������ڶ�ʱʱ����
	 */
	public TimerEvent(TimerListener listener,Object parameter,
		int intervalTime,int count,int initTime,boolean absolute)
	{
		this.listener=listener;
		this.parameter=parameter;
		this.intervalTime=intervalTime;
		this.count=count;
		this.initTime=initTime;
		this.absolute=absolute;
	}
	/* properties */
	/** ��ö�ʱ�¼������� */
	public TimerListener getTimerListener()
	{
		return listener;
	}
	/** ����¼��������� */
	public Object getParameter()
	{
		return parameter;
	}
	/** �����¼��������� */
	public void setParameter(Object parameter)
	{
		this.parameter=parameter;
	}
	/** ��ö�ʱʱ�� */
	public int getIntervalTime()
	{
		return intervalTime;
	}
	/** ���ö�ʱʱ�� */
	public void setIntervalTime(int time)
	{
		intervalTime=time;
	}
	/** ��ö�ʱ���� */
	public int getCount()
	{
		return count;
	}
	/** ���ö�ʱ���� */
	public void setCount(int count)
	{
		this.count=count;
	}
	/** ��ö�ʱ����ʼ��ʱʱ�� */
	public int getInitTime()
	{
		return initTime;
	}
	/** �ж��Ƿ�Ϊ����ʱ�䶨ʱ */
	public boolean isAbsolute()
	{
		return absolute;
	}
	/** ���þ��Ի����ʱ�䶨ʱ */
	public void setAbsolute(boolean b)
	{
		absolute=b;
	}
	/** �����ʼʱ�� */
	public long getStartTime()
	{
		return startTime;
	}
	/** ��õ�ǰ���е�ʱ�� */
	public long getCurrentTime()
	{
		return currentTime;
	}
	/** �����һ�����е�ʱ�� */
	public long getNextTime()
	{
		return nextTime;
	}
	/** ������һ�����е�ʱ�� */
	public void setNextTime(long time)
	{
		nextTime=time;
	}
	/* methods */
	/** ��ʼ������ */
	public void init()
	{
		startTime=System.currentTimeMillis();
		nextTime=startTime+initTime;
	}
	/** ������ʱ�¼���֪ͨ��ʱ�¼������������ö�ʱ��������һ�ε�����ʱ�� */
	void fire(long currentTime)
	{
		if(count!=INFINITE_CYCLE) count--;
		this.currentTime=currentTime;
		nextTime=absolute?(nextTime+intervalTime):(currentTime+intervalTime);
		try
		{
			listener.onTimer(this);
		}
		catch(Throwable e)
		{
			if(log.isWarnEnabled()) log.warn("fire error, "+toString(),e);
		}
	}
	/* common methods */
	public String toString()
	{
		return super.toString()+"[listener="+listener+", parameter="
			+parameter+", intervalTime="+intervalTime+", count="+count
			+", initTime="+initTime+", absolute="+absolute+"]";
	}
}