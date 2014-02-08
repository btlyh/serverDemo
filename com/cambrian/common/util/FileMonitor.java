package com.cambrian.common.util;

import com.cambrian.common.log.Logger;
import com.cambrian.common.timer.TimerCenter;
import com.cambrian.common.timer.TimerEvent;
import com.cambrian.common.timer.TimerListener;

/**
 * ��˵�����ļ��������� �����ļ��Ƿ��޸ģ���Ŀ¼���Ƿ����ļ������ӡ�ɾ�����޸ġ�
 * 
 * @version 2013-5-9
 * @author HYZ (huangyz1988@qq.com)
 */
public class FileMonitor implements TimerListener
{

	/* static fields */
	/** �仯��־�������ļ����ӱ�־���ļ�ɾ����־���ļ����޸ı�־ */
	public static final int ADD=1,DELETE=2,MODIFY=3;
	/** ����ʱ�䳣����Ĭ��Ϊ3�� */
	public static final int RUN_TIME=3000;

	/** ��ǰ���ļ������� */
	private static FileMonitor monitor=new FileMonitor();

	/** ��־��¼ */
	private static final Logger log=Logger.getLogger(FileMonitor.class);

	/* static methods */
	/** ��õ�ǰ�Ķ�ʱ������ */
	public static FileMonitor getInstance()
	{
		return monitor;
	}
	/** ����һ������ָ���ļ����ļ��޸ļ����� */
	public static void add(String file,ChangeListener listener)
	{
		monitor.addListener(file,listener);
	}
	/** �Ƴ�һ���ļ��޸ļ����������ټ����������������е��ļ��޸��¼� */
	public static void remove(ChangeListener listener)
	{
		monitor.removeListener(listener);
	}
	/** �Ƴ�һ������ָ���ļ����ļ��޸ļ����� */
	public static void remove(String file,ChangeListener listener)
	{
		monitor.removeListener(file,listener);
	}

	/* fields */
	/** �ļ����Ӷ����б� */
	ObjectArray monitorList=new ObjectArray();
	/** ��ʱ���¼� */
	TimerEvent tev=new TimerEvent(this,"fileMonitor",RUN_TIME);

	/* properties */
	/** ��ü���ʱ�� */
	public int getRunTime()
	{
		return tev.getIntervalTime();
	}
	/** ���ü���ʱ�� */
	public void setRunTime(int time)
	{
		tev.setIntervalTime(time);
	}
	/** ��ö�ʱ���¼� */
	public TimerEvent getTimerEvent()
	{
		return tev;
	}
	/* methods */
	/** ��ʱ����ʼ���� */
	public void timerStart()
	{
		TimerCenter.getSecondTimer().add(tev);
	}
	/** ���ָ���ļ�����Ӧ���ļ����Ӷ����λ�� */
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
	/** �ж�һ���ļ��Ƿ�ָ�����ļ��޸ļ��������� */
	public boolean checkListener(String file,ChangeListener listener)
	{
		if(listener==null||file==null||file.length()==0) return false;
		FileMonitorItem item=get(file);
		if(item==null) return false;
		return item.listenerList.contain(listener);
	}
	/** ����һ������ָ���ļ����ļ��޸ļ����� */
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
	/** �Ƴ�һ���ļ��޸ļ����������ټ����������������е��ļ��޸��¼� */
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
	/** �Ƴ�һ������ָ���ļ����ļ��޸ļ����� */
	public void removeListener(String file,ChangeListener listener)
	{
		if(listener==null||file==null||file.length()==0) return;
		FileMonitorItem item=get(file);
		if(item==null) return;
		item.listenerList.remove(listener);
		if(item.listenerList.size()>0) return;
		monitorList.remove(item);
	}
	/** ����ָ���ļ��������͵��ļ������¼���֪ͨ���ļ��ĸı��¼������� */
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
	 * ����ָ��Ŀ¼�µ��ļ��������͵��ļ������¼��� ֪ͨ���ļ����еĸı��¼���������
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
	/** ����ļ��Ƿ��޸� */
	void checkModified()
	{
		Object[] array=monitorList.getArray();
		for(int i=array.length-1;i>=0;i--)
			((FileMonitorItem)array[i]).checkModified();
	}
	/** ��ʱ�¼��ļ����ӿڷ��� */
	public void onTimer(TimerEvent e)
	{
		if(e==tev) checkModified();
	}
}