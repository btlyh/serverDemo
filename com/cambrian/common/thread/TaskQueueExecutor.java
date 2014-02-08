/**
 * 
 */
package com.cambrian.common.thread;

import com.cambrian.common.log.Logger;
import com.cambrian.common.util.ArrayQueue;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class TaskQueueExecutor extends Thread
{

	/* static fields */

	public static final int CAPACITY=255;
	public static final int PRIORITY=5;
	static final Logger log=Logger.getLogger(TaskQueueExecutor.class);
	static int count=0;
	/* static methods */

	/* fields */
	private ArrayQueue queue;
	private boolean active;
	boolean running;
	private long lastTime;

	/* constructors */
	public TaskQueueExecutor()
	{
		this(CAPACITY,PRIORITY);
	}

	public TaskQueueExecutor(int paramInt)
	{
		this(paramInt,PRIORITY);
	}

	public TaskQueueExecutor(int paramInt1,int paramInt2)
	{
		this.queue=new ArrayQueue(paramInt1);
		setPriority(paramInt2);
		this.active=true;
		setName(getName()+"@"+super.getClass().getName()+"@"
			+super.hashCode()+"-"+(count++)+"/"+paramInt1);
		start();
	}
	/* properties */

	/* init start */

	/* methods */

	public synchronized boolean isActive()
	{
		return this.active;
	}

	public synchronized boolean isRunning()
	{
		return this.running;
	}

	public synchronized boolean isSleeping()
	{
		return ((this.active)&&(!(this.running)));
	}

	public synchronized long getLastTime()
	{
		return this.lastTime;
	}

	public synchronized int getCapacity()
	{
		return this.queue.capacity();
	}

	public synchronized int getCount()
	{
		return this.queue.size();
	}

	public synchronized boolean isFull()
	{
		return this.queue.isFull();
	}

	public void execute(Runnable paramRunnable)
	{
		pushTask(paramRunnable);
		threadWake();
	}

	public void executeEndTask()
	{
		execute(new Runnable()
		{

			public void run()
			{
				TaskQueueExecutor.this.stopTask();
			}
		});
	}

	void runTask(Runnable paramRunnable)
	{
		try
		{
			paramRunnable.run();
		}
		catch(Throwable localThrowable)
		{
			if(log.isWarnEnabled())
				log.warn("runTask error, "+paramRunnable,localThrowable);
		}
		this.lastTime=System.currentTimeMillis();
	}

	synchronized Runnable popTask()
	{
		if(this.queue.isEmpty())
		{
			this.running=false;
			return null;
		}
		return ((Runnable)this.queue.remove());
	}

	private synchronized void pushTask(Runnable paramRunnable)
	{
		if(!(this.active))
			throw new IllegalStateException(this
				+" pushTask, has already stopped");
		if(this.queue.isFull())
			throw new IllegalStateException(this
				+" pushTask, task queue is full");
		this.queue.add(paramRunnable);
		this.running=true;
	}

	private void threadWake()
	{
		synchronized(this.queue)
		{
			this.queue.notify();
		}
	}

	void threadWait()
	{
		synchronized(this.queue)
		{
			while(isSleeping())
			{
				try
				{
					this.queue.wait();
				}
				catch(InterruptedException localInterruptedException)
				{
				}
			}
		}
	}

	public void run()
	{
		while(isActive())
		{
			Runnable localRunnable=popTask();
			if(localRunnable!=null)
				runTask(localRunnable);
			else
				threadWait();
		}
		if(!(log.isInfoEnabled())) return;
		log.info("run over, "+this);
	}

	public synchronized void stopTask()
	{
		if(log.isDebugEnabled()) log.debug("stop, "+this);
		this.active=false;
		this.queue.clear();
		if((this.running)&&(log.isWarnEnabled()))
			log.warn("stop, thread running, "+ThreadKit.toString(this));
		if(!(isAlive())) return;
		interrupt();
	}

	/* common methods */

	public String toString()
	{
		return super.toString()+"[ active="+this.active+", running="
			+this.running+", count="+this.queue.size()+"]";
	}
	/* inner class */

}
