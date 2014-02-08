/**
 * 
 */
package com.cambrian.common.util;

public final class ArrayDeque extends ArrayQueue
{

	public ArrayDeque(int paramInt)
	{
		super(paramInt);
	}

	public boolean addFirst(Object paramObject)
	{
		if(this.size>=this.array.length) return false;
		if(this.size<=0)
		{
			this.tail=0;
			this.head=0;
		}
		else
		{
			this.head-=1;
			if(this.head<0) this.head=(this.array.length-1);
		}
		this.array[this.head]=paramObject;
		this.size+=1;
		return true;
	}

	public Object getLast()
	{
		return this.array[this.tail];
	}

	public Object removeLast()
	{
		Object localObject=this.array[this.tail];
		this.size-=1;
		if(this.size>0)
		{
			this.tail-=1;
			if(this.tail<0) this.tail=(this.array.length-1);
		}
		return localObject;
	}
}