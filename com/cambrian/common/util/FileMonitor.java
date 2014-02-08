package com.cambrian.common.util;

import com.cambrian.common.log.Logger;
import com.cambrian.common.timer.TimerCenter;
import com.cambrian.common.timer.TimerEvent;
import com.cambrian.common.timer.TimerListener;

/**
 * 类说明：文件监视器。 监视文件是否被修改，及目录中是否有文件的增加、删除和修改。
 * 
 * @version 2013-5-9
 * @author HYZ (huangyz1988@qq.com)
 */
public class FileMonitor implements TimerListener
{

	/* static fields */
	/** 变化标志常量，文件增加标志，文件删除标志，文件被修改标志 */
	public static final int ADD=1,DELETE=2,MODIFY=3;
	/** 监听时间常量，默认为3秒 */
	public static final int RUN_TIME=3000;

	/** 当前的文件监视器 */
	private static FileMonitor monitor=new FileMonitor();

	/** 日志记录 */
	private static final Logger log=Logger.getLogger(FileMonitor.class);

	/* static methods */
	/** 获得当前的定时器中心 */
	public static FileMonitor getInstance()
	{
		return monitor;
	}
	/** 加上一个监听指定文件的文件修改监听器 */
	public static void add(String file,ChangeListener listener)
	{
		monitor.addListener(file,listener);
	}
	/** 移除一个文件修改监听器，不再监听它所监听的所有的文件修改事件 */
	public static void remove(ChangeListener listener)
	{
		monitor.removeListener(listener);
	}
	/** 移除一个监听指定文件的文件修改监听器 */
	public static void remove(String file,ChangeListener listener)
	{
		monitor.removeListener(file,listener);
	}

	/* fields */
	/** 文件监视对象列表 */
	ObjectArray monitorList=new ObjectArray();
	/** 定时器事件 */
	TimerEvent tev=new TimerEvent(this,"fileMonitor",RUN_TIME);

	/* properties */
	/** 获得监视时间 */
	public int getRunTime()
	{
		return tev.getIntervalTime();
	}
	/** 设置监视时间 */
	public void setRunTime(int time)
	{
		tev.setIntervalTime(time);
	}
	/** 获得定时器事件 */
	public TimerEvent getTimerEvent()
	{
		return tev;
	}
	/* methods */
	/** 定时器开始方法 */
	public void timerStart()
	{
		TimerCenter.getSecondTimer().add(tev);
	}
	/** 获得指定文件所对应的文件监视对象的位置 */
	FileMonitorItem get(String file)
	{
		FileMonitorItem item;
		Object[] array=monitorList.getArray();
		for(int i=array.length-1;i>=0;i--)
		{
			item=(FileMonitorItem)(array[i]);
			if(item.name.equals(file)) return item;
		}
		return null;
	}
	/** 判断一个文件是否被指定的文件修改监听器监视 */
	public boolean checkListener(String file,ChangeListener listener)
	{
		if(listener==null||file==null||file.length()==0) return false;
		FileMonitorItem item=get(file);
		if(item==null) return false;
		return item.listenerList.contain(listener);
	}
	/** 加上一个监听指定文件的文件修改监听器 */
	public void addListener(String file,ChangeListener listener)
	{
		if(listener==null||file==null||file.length()==0) return;
		FileMonitorItem item=get(file);
		if(item==null)
		{
			item=new FileMonitorItem(this,file);
			monitorList.add(item);
		}
		item.listenerList.add(listener);
	}
	/** 移除一个文件修改监听器，不再监听它所监听的所有的文件修改事件 */
	public void removeListener(ChangeListener listener)
	{
		if(listener==null) return;
		FileMonitorItem item;
		Object[] array=monitorList.getArray();
		for(int i=array.length-1;i>=0;i--)
		{
			item=(FileMonitorItem)(array[i]);
			item.listenerList.remove(listener);
			if(item.listenerList.size()>0) continue;
			monitorList.remove(item);
		}
	}
	/** 移除一个监听指定文件的文件修改监听器 */
	public void removeListener(String file,ChangeListener listener)
	{
		if(listener==null||file==null||file.length()==0) return;
		FileMonitorItem item=get(file);
		if(item==null) return;
		item.listenerList.remove(listener);
		if(item.listenerList.size()>0) return;
		monitorList.remove(item);
	}
	/** 引发指定文件名和类型的文件监视事件，通知该文件的改变事件监听器 */
	void fire(String file,int type,Object[] array)
	{
		ChangeListener listener;
		for(int i=array.length-1;i>=0;i--)
		{
			listener=(ChangeListener)array[i];
			try
			{
				listener.change(this,type,file);
			}
			catch(Exception e)
			{
				if(log.isWarnEnabled())
					log.warn("fire error, file="+file+", type="+type
						+", listener="+listener,e);
			}
		}
	}
	/**
	 * 引发指定目录下的文件名和类型的文件监视事件， 通知该文件所有的改变事件监听器。
	 */
	void fire(String dir,String file,int type,Object[] array)
	{
		ChangeListener listener;
		for(int i=array.length-1;i>=0;i--)
		{
			listener=(ChangeListener)array[i];
			try
			{
				listener.change(this,type,dir,file);
			}
			catch(Exception e)
			{
				if(log.isWarnEnabled())
					log.warn("fire error, dir="+dir+" file="+file+", type="
						+type+", listener="+listener,e);
			}
		}
	}
	/** 检查文件是否被修改 */
	void checkModified()
	{
		Object[] array=monitorList.getArray();
		for(int i=array.length-1;i>=0;i--)
			((FileMonitorItem)array[i]).checkModified();
	}
	/** 定时事件的监听接口方法 */
	public void onTimer(TimerEvent e)
	{
		if(e==tev) checkModified();
	}
}