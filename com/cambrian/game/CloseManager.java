/** */
package com.cambrian.game;

import com.cambrian.common.util.ChangeAdapter;

/**
 * ��˵�����ɹر�ģ����������رս����ݼ���˳�����ν��У�
 * 
 * @version 2013-5-6
 * @author HYZ (huangyz1988@qq.com)
 */
public class CloseManager extends ChangeAdapter implements CloseAble
{

	/** �رշ���˿� */
	CloseList closers=new CloseList();

	/** ��ӿɹرն��� */
	public void add(CloseAble closer)
	{
		closers.add(closer);
	}
	/** ����һ���ɹرն���ָ���ɹرն���֮ǰ */
	public void insert(CloseAble closer,CloseAble dest)
	{
		int index=closers.indexOf(dest);
		closers.insert(closer,index);
	}

	public void closeAll()
	{
		if(closers.size()==0) return;
		for(int i=0;i<closers.size();i++)
		{
			closers.get(i).close();
		}
		closers.clear();
	}

	/* @see com.cambrian.back.CloseAble#close() */
	public void close()
	{
		System.exit(0);
	}

	/*
	 * @see com.cambrian.common.util.ChangeAdapter#change(java.lang.Object,
	 * int)
	 */
	@Override
	public void change(Object src,int type)
	{
		if(type==CLOSEOVER) close();
	}

	/***
	 * ��˵�����ɹرն����б�
	 * 
	 * @version 2013-5-6
	 * @author HYZ (huangyz1988@qq.com)
	 */
	static class CloseList
	{

		public static final CloseAble[] NULL=new CloseAble[0];
		/** �ɹرն������� */
		CloseAble[] array=NULL;

		/** �б����� */
		public int size()
		{
			return array.length;
		}
		/** ���һ���ɹرն��� */
		public void add(CloseAble closer)
		{
			int size=array.length;
			CloseAble[] newArray=new CloseAble[size+1];
			System.arraycopy(array,0,newArray,0,size);
			newArray[size]=closer;
			array=newArray;
		}
		/** ����һ���ɹرն���ָ���±� */
		public void insert(CloseAble closer,int index)
		{
			int size=array.length;
			if(index<0||index>=size) return;
			CloseAble[] newArray=new CloseAble[size+1];
			if(index!=0) System.arraycopy(array,0,newArray,0,index);
			System.arraycopy(array,index,newArray,index+1,size-index);
			newArray[index]=closer;
			array=newArray;
			size++;
		}
		/** ���ָ���ɹرն�����±� */
		public int indexOf(CloseAble closer)
		{
			for(int i=0;i<array.length;i++)
			{
				if(array[i]==closer) return i;
			}
			return -1;
		}
		/** ��ȡָ��λ���ϵĿɹرն��� */
		public CloseAble get(int index)
		{
			if(index<0||index>=array.length) return null;
			return array[index];
		}
		/** ��� */
		public void clear()
		{
			array=NULL;
		}
	}
}
