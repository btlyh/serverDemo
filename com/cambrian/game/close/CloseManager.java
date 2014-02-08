/** */
package com.cambrian.game.close;

import com.cambrian.common.util.ChangeAdapter;

/**
 * 类说明：可关闭模块管理器（关闭将根据加入顺序依次进行）
 * 
 * @version 2013-5-6
 * @author HYZ (huangyz1988@qq.com)
 */
public class CloseManager extends ChangeAdapter implements CloseAble
{

	/** 关闭服务端口 */
	CloseList closers=new CloseList();

	/** 添加可关闭对象 */
	public void add(CloseAble closer)
	{
		closers.add(closer);
	}
	/** 插入一个可关闭对象到指定可关闭对象之前 */
	public void insert(CloseAble closer,CloseAble dest)
	{
		int index=closers.indexOf(dest);
		if(index==-1)
			closers.add(closer);
		else
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
		if((src instanceof CloseAble)&&type==CLOSEOVER) close();
	}

	/***
	 * 类说明：可关闭对象列表
	 * 
	 * @version 2013-5-6
	 * @author HYZ (huangyz1988@qq.com)
	 */
	static class CloseList
	{

		public static final CloseAble[] NULL=new CloseAble[0];
		/** 可关闭对象数组 */
		CloseAble[] array=NULL;

		/** 列表数量 */
		public int size()
		{
			return array.length;
		}
		/** 添加一个可关闭对象 */
		public void add(CloseAble closer)
		{
			int size=array.length;
			CloseAble[] newArray=new CloseAble[size+1];
			System.arraycopy(array,0,newArray,0,size);
			newArray[size]=closer;
			array=newArray;
		}
		/** 插入一个可关闭对象到指定下标 */
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
		/** 获得指定可关闭对象的下标 */
		public int indexOf(CloseAble closer)
		{
			for(int i=0;i<array.length;i++)
			{
				if(array[i]==closer) return i;
			}
			return -1;
		}
		/** 获取指定位置上的可关闭对象 */
		public CloseAble get(int index)
		{
			if(index<0||index>=array.length) return null;
			return array[index];
		}
		/** 清空 */
		public void clear()
		{
			array=NULL;
		}
	}
}
