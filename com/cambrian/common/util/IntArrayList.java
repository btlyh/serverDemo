package com.cambrian.common.util;

/**
 * 类说明：
 * 
 * @version 1.0
 * @date 2013-7-17
 * @author maxw<woldits@qq.com>
 */
public class IntArrayList
{

	/** 数组 */
	int[] array;
	/** 当前个数 */
	int size;

	public IntArrayList()
	{
		this(10);
	}

	public IntArrayList(int capacity)
	{
		this.array=new int[capacity];
	}

	public int size()
	{
		return this.size;
	}

	public int capacity()
	{
		return this.array.length;
	}

	public int[] getArray()
	{
		return this.array;
	}

	public void setCapacity(int value)
	{
		int[] arrayOfObject1=this.array;
		int i=arrayOfObject1.length;
		if(value<=i) return;
		for(;i<value;i=(i<<1)+1)
			;
		int[] arrayOfObject2=new int[i];
		System.arraycopy(arrayOfObject1,0,arrayOfObject2,0,this.size);
		this.array=arrayOfObject2;
	}
	public int get(int index)
	{
		return this.array[index];
	}
	public int set(int value,int index)
	{
		if(index>=this.size)
			throw new ArrayIndexOutOfBoundsException(super.getClass()
				.getName()+" set, invalid index="+index);
		int localObject=this.array[index];
		this.array[index]=value;
		return localObject;
	}

	public boolean add(int value)
	{
		if(this.size>=this.array.length) setCapacity(this.size+1);
		this.array[(this.size++)]=value;
		return true;
	}

	public int remove(int index)
	{
		if(index>=this.size)
			throw new ArrayIndexOutOfBoundsException(super.getClass()
				.getName()+" remove, invalid index="+index);
		int[] arrayOfObject=this.array;
		int localObject=arrayOfObject[index];
		int i=this.size-index-1;
		if(i>0)
			System.arraycopy(arrayOfObject,index+1,arrayOfObject,index,i);
		arrayOfObject[(--this.size)]=0;
		return localObject;
	}

	public void clear()
	{
		for(int i=this.size-1;i>=0;--i)
			array[i]=0;
		this.size=0;
	}

	public int[] toArray()
	{
		int[] arrayOfObject=new int[this.size];
		System.arraycopy(this.array,0,arrayOfObject,0,this.size);
		return arrayOfObject;
	}

	public String toString()
	{
		return super.toString()+"[size="+this.size+", capacity="
			+this.array.length+"]";
	}
}
