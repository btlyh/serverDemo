package com.cambrian.common.util;

/**
 * 类说明：对象数组（同步）
 * 
 * @author HYZ(huangyz1988@qq.com)
 * @version 2013-7-30
 */
public class ObjectArray implements Cloneable,Container
{

	public static final Object[] NULL=new Object[0];
	private Object[] array;
	// private Comparator comparator;
	private boolean descending;

	public static Object[] remove(Object[] paramArrayOfObject,int paramInt)
	{
		if(paramArrayOfObject.length<=1) return NULL;
		Object[] arrayOfObject=new Object[paramArrayOfObject.length-1];
		if(paramInt>0)
			System.arraycopy(paramArrayOfObject,0,arrayOfObject,0,paramInt);
		if(paramInt<arrayOfObject.length)
			System.arraycopy(paramArrayOfObject,paramInt+1,arrayOfObject,
				paramInt,arrayOfObject.length-paramInt);
		return arrayOfObject;
	}

	public ObjectArray()
	{
		this(NULL);
	}

	public ObjectArray(Object[] paramArrayOfObject)
	{
		this.array=paramArrayOfObject;
	}

	public int size()
	{
		return this.array.length;
	}

	public boolean isEmpty()
	{
		return this.array.length<=0;
	}

	public boolean isFull()
	{
		return false;
	}

	public Object[] getArray()
	{
		return this.array;
	}

	// public Comparator getComparator()
	// {
	// return this.comparator;
	// }
	//
	// public void setComparator(Comparator paramComparator)
	// {
	// this.comparator = paramComparator;
	// }

	public boolean isDescending()
	{
		return this.descending;
	}

	public void setDescending(boolean paramBoolean)
	{
		this.descending=paramBoolean;
	}

	public boolean contain(Object paramObject)
	{
		Object[] arrayOfObject=this.array;
		int i;
		if(paramObject!=null)
		{
			for(i=arrayOfObject.length-1;i>=0;i--)
			{
				if(paramObject.equals(arrayOfObject[i])) return true;
			}
		}
		else
		{
			for(i=arrayOfObject.length-1;i>=0;i--)
			{
				if(arrayOfObject[i]==null) return true;
			}
		}
		return false;
	}

	public synchronized boolean add(Object paramObject)
	{
		Object[] arrayOfObject1=this.array;
		int i=arrayOfObject1.length;
		Object[] arrayOfObject2=new Object[i+1];
		if(i>0) System.arraycopy(arrayOfObject1,0,arrayOfObject2,0,i);
		arrayOfObject2[i]=paramObject;
		// if (this.comparator != null) SetKit.sort(arrayOfObject2,
		// this.comparator, this.descending);
		this.array=arrayOfObject2;
		return true;
	}

	public void add(Object[] paramArrayOfObject)
	{
		if((paramArrayOfObject!=null)&&(paramArrayOfObject.length>0))
			add(paramArrayOfObject,0,paramArrayOfObject.length);
	}

	public synchronized void add(Object[] paramArrayOfObject,int paramInt1,
		int paramInt2)
	{
		if((paramArrayOfObject==null)||(paramInt1<0)||(paramInt2<=0)
			||(paramArrayOfObject.length<paramInt1+paramInt2)) return;
		Object[] arrayOfObject1=this.array;
		int i=arrayOfObject1.length;
		Object[] arrayOfObject2=new Object[i+paramInt2];
		if(i>0) System.arraycopy(arrayOfObject1,0,arrayOfObject2,0,i);
		System.arraycopy(paramArrayOfObject,paramInt1,arrayOfObject2,i,
			paramInt2);
		// if (this.comparator != null) SetKit.sort(arrayOfObject2,
		// this.comparator, this.descending);
		this.array=arrayOfObject2;
	}

	public Object get()
	{
		Object[] arrayOfObject=this.array;
		return arrayOfObject[(arrayOfObject.length-1)];
	}

	int indexOf(Object[] paramArrayOfObject,Object paramObject)
	{
		int i=paramArrayOfObject.length-1;
		if(paramObject!=null)
		{
			do
			{
				i--;
				if(i<0) break;
			}
			while(!paramObject.equals(paramArrayOfObject[i]));
		}
		else
		{
			while((i>=0)&&(paramArrayOfObject[i]!=null))
				i--;
		}

		return i;
	}

	public synchronized boolean remove(Object paramObject)
	{
		Object[] arrayOfObject=this.array;
		int i=indexOf(arrayOfObject,paramObject);
		if(i<0) return false;
		this.array=remove(arrayOfObject,i);
		return true;
	}

	public synchronized Object remove()
	{
		Object[] arrayOfObject=this.array;
		int i=arrayOfObject.length-1;
		Object localObject=arrayOfObject[i];
		this.array=remove(arrayOfObject,i);
		return localObject;
	}

	// public void sort()
	// {
	// sort(this.comparator, this.descending);
	// }
	//
	// public synchronized void sort(Comparator paramComparator, boolean
	// paramBoolean)
	// {
	// if (paramComparator == null) return;
	// Object[] arrayOfObject1 = this.array;
	// Object[] arrayOfObject2 = new Object[arrayOfObject1.length];
	// System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0,
	// arrayOfObject1.length);
	// SetKit.sort(arrayOfObject2, paramComparator, paramBoolean);
	// this.array = arrayOfObject2;
	// }
	//
	// public synchronized int select(Selector paramSelector)
	// {
	// Object[] arrayOfObject1 = this.array;
	// Object[] arrayOfObject2 = (Object[])null;
	// int i = arrayOfObject1.length;
	// int j = 0; int k = i;
	//
	// int n = 0;
	// for (; j < i; j++)
	// {
	// int m = paramSelector.select(arrayOfObject1[j]);
	// Object[] arrayOfObject3;
	// if (m != 0)
	// if (m == 1)
	// {
	// if (arrayOfObject2 == null)
	// {
	// arrayOfObject2 = new Object[arrayOfObject1.length];
	// System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0,
	// arrayOfObject1.length);
	// }
	// arrayOfObject2[j] = arrayOfObject2;
	// k--;
	// n = m;
	// }
	// else {
	// if (m == 3)
	// {
	// if (arrayOfObject2 == null)
	// {
	// arrayOfObject2 = new Object[arrayOfObject1.length];
	// System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0,
	// arrayOfObject1.length);
	// }
	// arrayOfObject2[j] = arrayOfObject2;
	// k--;
	// }
	// n = m;
	// break;
	// }
	// }
	// if (arrayOfObject2 == null) return n;
	// if (k <= 0)
	// {
	// this.array = NULL;
	// return n;
	// }
	// arrayOfObject3 = new Object[k];
	// j = 0; for (k = 0; j < i; j++)
	// {
	// if (arrayOfObject2[j] == arrayOfObject2) continue;
	// arrayOfObject3[(k++)] = arrayOfObject2[j];
	// }
	// this.array = arrayOfObject3;
	// return n;
	// }

	public Object[] toArray()
	{
		Object[] arrayOfObject1=this.array;
		Object[] arrayOfObject2=new Object[arrayOfObject1.length];
		System.arraycopy(arrayOfObject1,0,arrayOfObject2,0,
			arrayOfObject1.length);
		return arrayOfObject2;
	}

	public Object[] toArray(Object[] paramArrayOfObject)
	{
		Object[] arrayOfObject=this.array;
		int i=paramArrayOfObject.length>arrayOfObject.length
			?arrayOfObject.length:paramArrayOfObject.length;
		System.arraycopy(arrayOfObject,0,paramArrayOfObject,0,i);
		return paramArrayOfObject;
	}

	public synchronized void clear()
	{
		this.array=NULL;
	}

	// public Object clone()
	// {
	// try
	// {
	// return super.clone();
	// }
	// catch (CloneNotSupportedException localCloneNotSupportedException) {
	// }
	// throw new RuntimeException(getClass().getName() +
	// " clone, size=" + this.array.length, localCloneNotSupportedException);
	// }
	//
	// public String toString()
	// {
	// return super.toString() + "[size=" + this.array.length + (
	// this.comparator != null ? " descending=" + this.descending : "") +
	// "]";
	// }
}