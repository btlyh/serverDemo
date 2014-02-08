/**
 * 
 */
package com.cambrian.common.thread;

import com.cambrian.common.util.ArrayList;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class TaskPoolExecutor
{

	/* static fields */

	public static final int INIT_SIZE=2;
	public static final int MAX_SIZE=100;
	public static final int TIMEOUT=180000;
	public static final int COLLATE_TIME=60000;

	/* static methods */

	/* fields */

	int initSize;
	int maxSize;
	ArrayList pool;
	int timeout;
	boolean active;
	int collateTime;
	private long lastCollateTime;

	/* constructors */
	public TaskPoolExecutor()
	{
		this(2,100);
	}

	public TaskPoolExecutor(int paramInt1,int paramInt2)
	{
		this.initSize=paramInt1;
		this.maxSize=paramInt2;
		this.pool=new ArrayList(paramInt2);
		for(int i=0;i<paramInt1;++i)
			this.pool.add(new TaskQueueExecutor());
		this.timeout=180000;
		this.active=true;
		this.collateTime=60000;
		this.lastCollateTime=System.currentTimeMillis();
	}
	/* properties */
	public boolean isActive()
	{
		return this.active;
	}

	public synchronized int size()
	{
		return this.pool.size();
	}

	public int getInitSize()
	{
		return this.initSize;
	}

	public int getMaxSize()
	{
		return this.maxSize;
	}

	public synchronized int getRunningCount()
	{
		int i=0;
		for(int j=this.pool.size()-1;j>=0;--j)
		{
			if(!(((TaskQueueExecutor)this.pool.get(j)).isRunning()))
				continue;
			++i;
		}
		return i;
	}

	public int getTimeout()
	{
		return this.timeout;
	}

	public void setTimeout(int paramInt)
	{
		this.timeout=paramInt;
	}

	public int getCollateTime()
	{
		return this.collateTime;
	}

	public void setCollateTime(int paramInt)
	{
		this.collateTime=paramInt;
	}
	/* init start */

	/* methods */

	private TaskQueueExecutor addExecutor()
	{
		TaskQueueExecutor localTaskQueueExecutor=new TaskQueueExecutor();
		this.pool.add(localTaskQueueExecutor);
		return localTaskQueueExecutor;
	}

	private synchronized TaskQueueExecutor getExecutor()
	{
		if(!(this.active))
		{
			throw new IllegalStateException(this+" getExecutor, is stopped");
		}
		TaskQueueExecutor localObject=null;
		int i=2147483647;
		int j=0;

		for(int k=this.pool.size()-1;k>=0;--k)
		{
			TaskQueueExecutor localTaskQueueExecutor=(TaskQueueExecutor)this.pool
				.get(k);
			if(localTaskQueueExecutor.isActive())
			{
				if(!(localTaskQueueExecutor.isRunning()))
					return localTaskQueueExecutor;
				if(!(localTaskQueueExecutor.isFull()))
				{
					j=localTaskQueueExecutor.getCount();
					if(i>j)
					{
						i=j;
						localObject=localTaskQueueExecutor;
					}
				}
			}
			else
			{
				this.pool.removeAt(k);
			}
		}
		if(this.pool.size()<this.maxSize) return addExecutor();
		if(localObject!=null) return localObject;
		throw new IllegalStateException(this+" getExecutor, pool is full");
	}

	public void execute(Runnable paramRunnable)
	{
		getExecutor().execute(paramRunnable);
		long time=System.currentTimeMillis();
		if(time-this.lastCollateTime<this.collateTime) return;
		collate(time);
		this.lastCollateTime=time;
	}

	public synchronized void collate(long paramLong)
	{
		paramLong-=this.timeout;

		for(int i=this.pool.size()-1;i>=0;--i)
		{
			TaskQueueExecutor localTaskQueueExecutor=(TaskQueueExecutor)this.pool
				.get(i);
			if((localTaskQueueExecutor.isAlive())
				&&(localTaskQueueExecutor.isActive()))
			{
				if(localTaskQueueExecutor.isRunning()) continue;
				if(paramLong<localTaskQueueExecutor.getLastTime())
				{
					continue;
				}
			}
			localTaskQueueExecutor.stopTask();
			this.pool.removeAt(i);
		}
	}

	public synchronized void stop()
	{
		this.active=false;
		for(int i=this.pool.size()-1;i>=0;--i)
			((TaskQueueExecutor)this.pool.get(i)).stopTask();
		this.pool.clear();
	}
	/* common methods */

	public String toString()
	{
		return super.toString()+"[active="+this.active+", run="
			+getRunningCount()+", size="+size()+", maxSize="+this.maxSize
			+"]";
	}
	/* inner class */

}
