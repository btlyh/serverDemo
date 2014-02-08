/**
 * 
 */
package com.cambrian.common.timer;

/**
 * ��˵������ʱ���¼�����
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */

public class TimerEventArray
{

	/* static fields */
	/** ������ */
	public final static TimerEvent[] NULL={};

	/* fields */
	/** ���� */
	TimerEvent[] array=NULL;

	/* properties */
	/** ������� */
	public int size()
	{
		return array.length;
	}
	/** ������� */
	public TimerEvent[] getArray()
	{
		return array;
	}
	/* methods */
	/** �ж��Ƿ����ָ���Ķ�ʱ���¼� */
	public boolean contain(TimerEvent ev)
	{
		TimerEvent[] array=this.array;
		if(ev!=null)
		{
			for(int i=array.length-1;i>=0;i--)
			{
				if(ev.equals(array[i])) return true;
			}
		}
		else
		{
			for(int i=array.length-1;i>=0;i--)
			{
				if(array[i]==null) return true;
			}
		}
		return false;
	}
	/** ���ָ���Ķ�ʱ���¼� */
	public synchronized void add(TimerEvent ev)
	{
		TimerEvent[] array=this.array;
		int i=array.length;
		TimerEvent[] temp=new TimerEvent[i+1];
		if(i>0) System.arraycopy(array,0,temp,0,i);
		temp[i]=ev;
		this.array=temp;
	}
	/** �Ƴ�ָ���Ķ�ʱ���¼� */
	public synchronized boolean remove(TimerEvent ev)
	{
		TimerEvent[] array=this.array;
		int i=array.length-1;
		if(ev!=null)
		{
			for(;i>=0;i--)
			{
				if(ev.equals(array[i])) break;
			}
		}
		else
		{
			for(;i>=0;i--)
			{
				if(array[i]==null) break;
			}
		}
		if(i<0) return false;
		if(array.length==1)
		{
			this.array=NULL;
			return true;
		}
		TimerEvent[] temp=new TimerEvent[array.length-1];
		if(i>0) System.arraycopy(array,0,temp,0,i);
		if(i<temp.length) System.arraycopy(array,i+1,temp,i,temp.length-i);
		this.array=temp;
		return true;
	}
	/** ����б��е�����Ԫ�� */
	public synchronized void clear()
	{
		array=NULL;
	}
	/* common methods */
	public String toString()
	{
		return super.toString()+"[size="+array.length+"]";
	}

}