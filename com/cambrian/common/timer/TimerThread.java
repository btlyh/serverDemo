/**
 * 
 */
package com.cambrian.common.timer;

/**
 * ��˵������ʱ���̣߳��������ж�ʱ����
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */

/** ��˵������ʱ���߳� */
public class TimerThread extends Thread
{

	/* fields */
	/** ��ʱ�� */
	Timer timer;
	/** ����ʱ�� */
	int runTime;
	/** ��ʱʱ�� */
	int timeout;

	/** ���־ */
	volatile boolean active;

	/** ���п�ʼʱ�� */
	long runStartTime;
	/** ���н���ʱ�� */
	long runEndTime;

	/* constructors */
	/** ��ָ���Ķ�ʱ��������ʱ��ͳ�ʱʱ�乹��һ����ʱ���߳� */
	TimerThread(Timer timer,int runTime,int timeout)
	{
		this.timer=timer;
		this.runTime=runTime;
		this.timeout=timeout;
		runEndTime=runStartTime=System.currentTimeMillis();
		active=true;
		setName(getName()+"@"+getClass().getName()+"@"+hashCode()+"/"
			+runTime);
		setDaemon(true);
	}
	/** ���ƹ��췽�� */
	TimerThread(TimerThread thread)
	{
		this.timer=thread.timer;
		this.runTime=thread.runTime;
		this.timeout=thread.timeout;
		runEndTime=runStartTime=System.currentTimeMillis();
		active=true;
		setName(getName()+"@"+getClass().getName()+"@"+hashCode()+"/"
			+runTime);
		setDaemon(true);
	}
	/* properties */
	/** �ж��߳��Ƿ� */
	public boolean isActive()
	{
		return active;
	}
	/** ����̵߳�����ʱ�� */
	public int getRunTime()
	{
		return runTime;
	}
	/** �����̵߳�����ʱ�� */
	public void setRunTime(int time)
	{
		runTime=time;
	}
	/** ����̵߳ĳ�ʱʱ�� */
	public int getTimeout()
	{
		return timeout;
	}
	/** �����̵߳ĳ�ʱʱ�� */
	public void setTimeout(int timeout)
	{
		this.timeout=timeout;
	}
	/** ����̵߳����п�ʼʱ�� */
	public long getRunStartTime()
	{
		return runStartTime;
	}
	/** ����̵߳����н���ʱ�� */
	public long getRunEndTime()
	{
		return runEndTime;
	}
	/* methods */
	/** �ж��Ƿ�ʱ */
	public boolean isTimeout(long time)
	{
		return time-runStartTime>timeout;
	}
	/** ���з��� */
	public void run()
	{
		long time;
		TimerEvent[] temp;
		TimerEvent ev;
		int i;
		while(active)
		{
			try
			{
				time=System.currentTimeMillis();
				runStartTime=time;
				temp=timer.getArray();
				for(i=temp.length-1;active&&i>=0;i--)
				{
					ev=temp[i];
					if(ev.count<=0)
						timer.remove(ev);
					else if(time>=ev.nextTime) ev.fire(time);
				}
				runEndTime=System.currentTimeMillis();
				Thread.sleep(runTime);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	/** �رշ��� */
	public void close()
	{
		active=false;
	}
	/* common methods */
	public String toString()
	{
		return super.toString()+"[active="+active+", runTime="+runTime
			+", timeout="+timeout+", runStartTime="+runStartTime
			+", runEndTime="+runEndTime+"]";
	}

}