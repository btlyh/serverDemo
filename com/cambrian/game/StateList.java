package com.cambrian.game;

import com.cambrian.common.net.CharBuffer;

/**
 * ��˵����״̬�б�
 * 
 * @version 2013-4-23
 * @author HYZ (huangyz1988@qq.com)
 */
public final class StateList implements Cloneable
{

	/** �� */
	public static final int[] NULL=new int[0];
	/** ״̬���� */
	int[] array;

	/** ����һ����״̬�б� */
	public StateList()
	{
		this.array=NULL;
	}

	/** ͨ���������鹹��һ��״̬�б� */
	public StateList(int[] array)
	{
		if(array==null)
			throw new IllegalArgumentException(StateList.class.getName()
				+" <init>, null array");
		if(array.length%2!=0)
			throw new IllegalArgumentException(StateList.class.getName()
				+" <init>, invalid array length:"+array.length);
		this.array=array;
	}

	/** ��ǰ�б��С */
	public int size()
	{
		return (array.length/2);
	}

	/** ��ȡ״̬���� */
	public int[] getArray()
	{
		return array;
	}

	/** �Ƿ����ָ��״̬ */
	public boolean contain(int key)
	{
		return (indexOf(key)>0);
	}

	/** ��ȡָ��״̬ */
	public int get(int key)
	{
		int i=indexOf(key);
		if(i<0) return 0;
		return array[i+1];
	}

	/** ����ָ��״ֵ̬ */
	public synchronized int set(int key,int value)
	{
		int i=indexOf(key);
		if(i>=0)
		{
			int old=array[i+1];
			array[i+1]=value;
			return old;
		}
		i=array.length;
		int[] temp=new int[i+2];
		if(i>0) System.arraycopy(array,0,temp,0,i);
		temp[i]=key;
		temp[i+1]=value;
		array=temp;
		return 0;
	}

	/** �Ƴ�ָ��״ֵ̬ */
	public synchronized int remove(int key)
	{
		int i=indexOf(key);
		if(i<0) return 0;
		int value=array[(i+1)];
		if(array.length==2)
		{
			array=NULL;
			return value;
		}
		int[] temp=new int[array.length-2];
		if(i>0) System.arraycopy(array,0,temp,0,i);
		if(i<temp.length) System.arraycopy(array,i+2,temp,i,temp.length-i);
		array=temp;
		return value;
	}

	/** ��ȡָ��״̬���±� */
	int indexOf(int key)
	{
		int i=array.length-2;
		for(;(i>=0)&&(key!=array[i]);i-=2)
			;
		return i;
	}

	/** ����״̬�б� */
	public synchronized void reset(int[] array)
	{
		if(array==null)
			throw new IllegalArgumentException(StateList.class.getName()
				+" reset, null array");
		if(array.length%2!=0)
			throw new IllegalArgumentException(StateList.class.getName()
				+" reset, invalid array length:"+array.length);
		this.array=array;
	}

	/** ��� */
	public synchronized void clear()
	{
		this.array=NULL;
	}

	/** ��¡ */
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			throw new RuntimeException(StateList.class.getName()
				+" clone, size="+array.length,e);
		}
	}

	/** ��Ϣ */
	public String toString()
	{
		CharBuffer cb=new CharBuffer(array.length*8+30);
		cb.append(super.toString());
		cb.append("[size=").append(array.length).append(", {");
		if(array.length>0)
		{
			for(int i=0;i<array.length;i+=2)
			{
				cb.append(array[i]).append('=');
				cb.append(array[i+1]).append(' ');
			}
			cb.setTop(cb.top()-1);
		}
		cb.append("}]");
		return cb.getString();
	}
}