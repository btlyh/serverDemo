/**
 * 
 */
package com.cambrian.common.util;

public class ArrayQueue implements Container
{

	Object[] array;
	int head;
	int tail;
	int size;

	public ArrayQueue(int paramInt)
	{
		if(paramInt<1)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, invalid capacity:"+paramInt);
		this.array=new Object[paramInt];
		this.head=0;
		this.tail=0;
		this.size=0;
	}

	public int size()
	{
		return this.size;
	}

	public int capacity()
	{
		return this.array.length;
	}

	public boolean isEmpty()
	{
		return this.size<=0;
	}

	public boolean isFull()
	{
		return this.size>=this.array.length;
	}

	public Object[] getArray()
	{
		return this.array;
	}

	public boolean contain(Object paramObject)
	{
		int i;
		int j;
		if(paramObject!=null)
		{
			i=this.head;
			for(j=this.tail>this.head?this.tail:this.array.length;i<j;i++)
			{
				if(paramObject.equals(this.array[i])) return true;
			}
			i=0;
			for(j=this.tail>this.head?0:this.tail;i<j;i++)
			{
				if(paramObject.equals(this.array[i])) return true;
			}
		}
		else
		{
			i=this.head;
			for(j=this.tail>this.head?this.tail:this.array.length;i<j;i++)
			{
				if(this.array[i]==null) return true;
			}
			i=0;
			for(j=this.tail>this.head?0:this.tail;i<j;i++)
			{
				if(this.array[i]==null) return true;
			}
		}
		return false;
	}

	public boolean add(Object paramObject)
	{
		if(isFull()) return false;
		if(this.size<=0)
		{
			this.tail=0;
			this.head=0;
		}
		else
		{
			this.tail+=1;
			if(this.tail>=this.array.length) this.tail=0;
		}
		this.array[this.tail]=paramObject;
		this.size+=1;
		return true;
	}

	public Object get()
	{
		return this.array[this.head];
	}

	public Object remove()
	{
		Object localObject=this.array[this.head];
		this.array[this.head]=null;
		this.size-=1;
		if(this.size>0)
		{
			this.head+=1;
			if(this.head>=this.array.length) this.head=0;
		}
		return localObject;
	}

	public void clear()
	{
		int i=this.head;
		for(int j=this.tail>this.head?this.tail:this.array.length;i<j;i++)
			this.array[i]=null;
		i=0;
		for(int j=this.tail>this.head?0:this.tail;i<j;i++)
			this.array[i]=null;
		this.tail=0;
		this.head=0;
		this.size=0;
	}

	public String toString()
	{
		return super.toString()+"[size="+this.size+", capacity="
			+this.array.length+"]";
	}
}