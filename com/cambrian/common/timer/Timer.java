/**
 * 
 */
package com.cambrian.common.timer;

/**
 * ��˵������ʱ����������ʱ�¼�
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */

public class Timer implements Runnable
{

	/* fields */
	/** ��ʱ���¼����� */
	private TimerEventArray array=new TimerEventArray();

	/* properties */
	/** ��ö�ʱ���¼����� */
	public TimerEvent[] getArray()
	{
		return array.getArray();
	}
	/* methods */
	/** �ж��Ƿ����ָ���Ķ��� */
	public boolean contain(TimerEvent e)
	{
		return array.contain(e);
	}
	/** ���ָ���������Ͷ��������Ķ�ʱ���¼����������򷵻ص�һ���� */
	public TimerEvent get(TimerListener listener,Object parameter)
	{
		TimerEvent[] temp=array.getArray();
		TimerEvent ev;
		for(int i=temp.length-1;i>=0;i--)
		{
			ev=temp[i];
			if(listener!=ev.getTimerListener()) continue;
			if(parameter==null||parameter.equals(ev.getParameter()))
				return ev;
		}
		return null;
	}
	/** ����һ����ʱ���¼� */
	public void add(TimerEvent e)
	{
		e.init();
		array.add(e);
	}
	/** �Ƴ�ָ���Ķ�ʱ���¼� */
	public void remove(TimerEvent e)
	{
		array.remove(e);
	}
	/** �Ƴ�ָ����ʱ�¼��������Ķ�ʱ���¼����������е��¼��������� */
	public void remove(TimerListener listener)
	{
		remove(listener,null);
	}
	/** �Ƴ�����ָ���Ķ�ʱ�¼����������¼����������Ķ�ʱ���¼� */
	public void remove(TimerListener listener,Object parameter)
	{
		TimerEvent[] temp=array.getArray();
		TimerEvent ev;
		for(int i=temp.length-1;i>=0;i--)
		{
			ev=temp[i];
			if(listener!=ev.getTimerListener()) continue;
			if(parameter!=null&&!parameter.equals(ev.getParameter()))
				continue;
			array.remove(ev);
		}
	}
	/** ���з��� */
	public void run()
	{
		fire(System.currentTimeMillis());
	}
	/** ֪ͨ���ж�ʱ���¼�������Ƿ�Ҫ������ʱ�¼� */
	public void fire(long time)
	{
		TimerEvent[] temp=array.getArray();
		TimerEvent ev;
		for(int i=temp.length-1;i>=0;i--)
		{
			ev=temp[i];
			if(ev.count<=0)
				remove(ev);
			else if(time>=ev.nextTime) ev.fire(time);
		}
	}
	/** ������ */
	public void clear()
	{
		array.clear();
	}

}