/**
 * 
 */
package com.cambrian.common.util;

public class ArrayList implements Cloneable
{

	public static final int CAPACITY=10;
	Object[] array;
	int size;

	public ArrayList()
	{
		this(10);
	}

	public ArrayList(int paramInt)
	{
		if(paramInt<1)
			throw new IllegalArgumentException(super.getClass().getName()
				+" <init>, invalid capatity:"+paramInt);
		this.array=new Object[paramInt];
	}

	public ArrayList(Object[] array)
	{
		this(array,(array!=null)?array.length:0);
	}

	public ArrayList(Object[] array,int paramInt)
	{
		if(array==null)
			throw new IllegalArgumentException(super.getClass().getName()
				+" <init>, null array");
		if(paramInt>array.length)
			throw new IllegalArgumentException(super.getClass().getName()
				+" <init>, invalid length:"+paramInt);
		this.array=array;
		this.size=paramInt;
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
		return (this.size<=0);
	}

	public boolean isFull()
	{
		return false;
	}

	public Object[] getArray()
	{
		return this.array;
	}

	public void setCapacity(int paramInt)
	{
		Object[] arrayOfObject1=this.array;
		int i=arrayOfObject1.length;
		if(paramInt<=i) return;
		for(;i<paramInt;i=(i<<1)+1)
			;
		Object[] arrayOfObject2=new Object[i];
		System.arraycopy(arrayOfObject1,0,arrayOfObject2,0,this.size);
		this.array=arrayOfObject2;
	}

	public Object get()
	{
		return getLast();
	}

	public Object get(int paramInt)
	{
		return this.array[paramInt];
	}

	public Object getFirst()
	{
		return this.array[0];
	}

	public Object getLast()
	{
		return this.array[(this.size-1)];
	}

	public boolean contain(Object paramObject)
	{
		return (indexOf(paramObject,0)>=0);
	}

	public int indexOf(Object paramObject)
	{
		return indexOf(paramObject,0);
	}

	public int indexOf(Object paramObject,int paramInt)
	{
		int i=this.size;
		if(paramInt>=i) return -1;
		Object[] arrayOfObject=this.array;
		int j;
		if(paramObject==null)
		{
			for(j=paramInt;j<i;++j)
			{
				if(arrayOfObject[j]==null) return j;
			}

		}
		else
		{
			for(j=paramInt;j<i;++j)
			{
				if(paramObject.equals(arrayOfObject[j])) return j;
			}
		}
		return -1;
	}

	public int lastIndexOf(Object paramObject)
	{
		return lastIndexOf(paramObject,this.size-1);
	}

	public int lastIndexOf(Object paramObject,int paramInt)
	{
		if(paramInt>=this.size) return -1;
		Object[] arrayOfObject=this.array;
		int i;
		if(paramObject==null)
		{
			for(i=paramInt;i>=0;--i)
			{
				if(arrayOfObject[i]==null) return i;
			}

		}
		else
		{
			for(i=paramInt;i>=0;--i)
			{
				if(paramObject.equals(arrayOfObject[i])) return i;
			}
		}
		return -1;
	}

	public Object set(Object paramObject,int paramInt)
	{
		if(paramInt>=this.size)
			throw new ArrayIndexOutOfBoundsException(super.getClass()
				.getName()+" set, invalid index="+paramInt);
		Object localObject=this.array[paramInt];
		this.array[paramInt]=paramObject;
		return localObject;
	}

	public boolean add(Object paramObject)
	{
		if(this.size>=this.array.length) setCapacity(this.size+1);
		this.array[(this.size++)]=paramObject;
		return true;
	}

	public void add(Object paramObject,int paramInt)
	{
		if(paramInt<this.size)
		{
			if(this.size>=this.array.length) setCapacity(this.size+1);
			System.arraycopy(this.array,paramInt,this.array,paramInt+1,
				this.size-paramInt);
			this.array[paramInt]=paramObject;
			this.size+=1;
		}
		else
		{
			if(paramInt>=this.array.length) setCapacity(paramInt+1);
			this.array[paramInt]=paramObject;
			this.size=(paramInt+1);
		}
	}

	public void addAt(Object paramObject,int paramInt)
	{
		if(paramInt<this.size)
		{
			if(this.size>=this.array.length) setCapacity(this.size+1);
			this.array[(this.size++)]=this.array[paramInt];
			this.array[paramInt]=paramObject;
		}
		else
		{
			if(paramInt>=this.array.length) setCapacity(paramInt+1);
			this.array[paramInt]=paramObject;
			this.size=(paramInt+1);
		}
	}

	public boolean remove(Object paramObject)
	{
		int i=indexOf(paramObject,0);
		if(i<0) return false;
		remove(i);
		return true;
	}

	public boolean removeAt(Object paramObject)
	{
		int i=indexOf(paramObject,0);
		if(i<0) return false;
		removeAt(i);
		return true;
	}

	public Object remove()
	{
		return remove(this.size-1);
	}

	public Object remove(int paramInt)
	{
		if(paramInt>=this.size)
			throw new ArrayIndexOutOfBoundsException(super.getClass()
				.getName()+" remove, invalid index="+paramInt);
		Object[] arrayOfObject=this.array;
		Object localObject=arrayOfObject[paramInt];
		int i=this.size-paramInt-1;
		if(i>0)
			System.arraycopy(arrayOfObject,paramInt+1,arrayOfObject,
				paramInt,i);
		arrayOfObject[(--this.size)]=null;
		return localObject;
	}

	public Object removeAt(int paramInt)
	{
		if(paramInt>=this.size)
			throw new ArrayIndexOutOfBoundsException(super.getClass()
				.getName()+" removeAt, invalid index="+paramInt);
		Object[] arrayOfObject=this.array;
		Object localObject=arrayOfObject[paramInt];
		arrayOfObject[paramInt]=arrayOfObject[(--this.size)];
		arrayOfObject[this.size]=null;
		return localObject;
	}

	public void clear()
	{
		Object[] arrayOfObject=this.array;
		for(int i=this.size-1;i>=0;--i)
			arrayOfObject[i]=null;
		this.size=0;
	}

	public Object[] toArray()
	{
		Object[] arrayOfObject=new Object[this.size];
		System.arraycopy(this.array,0,arrayOfObject,0,this.size);
		return arrayOfObject;
	}

	public int toArray(Object[] paramArrayOfObject)
	{
		int i=(paramArrayOfObject.length>this.size)?this.size
			:paramArrayOfObject.length;
		System.arraycopy(this.array,0,paramArrayOfObject,0,i);
		return i;
	}

	public Object clone()
	{
		try
		{
			ArrayList localArrayList=(ArrayList)super.clone();
			Object[] arrayOfObject=localArrayList.array;
			localArrayList.array=new Object[localArrayList.size];
			System.arraycopy(arrayOfObject,0,localArrayList.array,0,
				localArrayList.size);
			return localArrayList;
		}
		catch(CloneNotSupportedException localCloneNotSupportedException)
		{
			throw new RuntimeException(super.getClass().getName()
				+" clone, capacity="+this.array.length,
				localCloneNotSupportedException);
		}
	}

	public String toString()
	{
		return super.toString()+"[size="+this.size+", capacity="
			+this.array.length+"]";
	}
}
