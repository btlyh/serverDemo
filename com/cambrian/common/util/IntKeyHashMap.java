package com.cambrian.common.util;

/***
 * 类说明：Int键映射表
 * 
 * @version 2013-4-22
 * @author HYZ (huangyz1988@qq.com)
 */
public final class IntKeyHashMap

{

	public static final int CAPACITY=16;
	public static final float LOAD_FACTOR=0.75F;
	transient Entry[] array;
	transient int size;
	final float loadFactor;
	int threshold;

	public IntKeyHashMap()
	{
		this(16,0.75F);
	}

	public IntKeyHashMap(int paramInt)
	{
		this(paramInt,0.75F);
	}

	public IntKeyHashMap(int paramInt,float paramFloat)
	{
		if(paramInt<1)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, invalid capatity:"+paramInt);
		if(paramFloat<=0.0F)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, invalid loadFactor:"+paramFloat);
		this.threshold=((int)(paramInt*paramFloat));
		if(this.threshold<=0)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, invalid threshold:"+paramInt+" "+paramFloat);
		this.loadFactor=paramFloat;
		this.array=new Entry[paramInt];
	}

	public int size()
	{
		return this.size;
	}

	public Object get(int paramInt)
	{
		Entry[] arrayOfEntry=this.array;
		Entry localEntry=arrayOfEntry[((paramInt&0x7FFFFFFF)%arrayOfEntry.length)];
		while(localEntry!=null)
		{
			if(localEntry.key==paramInt) return localEntry.value;
			localEntry=localEntry.next;
		}
		return null;
	}

	public Object put(int paramInt,Object paramObject)
	{
		Entry[] arrayOfEntry=this.array;
		int i=(paramInt&0x7FFFFFFF)%arrayOfEntry.length;
		Entry localEntry=arrayOfEntry[i];
		if(localEntry!=null)
		{
			while(true)
			{
				if(localEntry.key==paramInt)
				{
					Object localObject=localEntry.value;
					localEntry.value=paramObject;
					return localObject;
				}
				if(localEntry.next==null) break;
				localEntry=localEntry.next;
			}
			localEntry.next=new Entry(paramInt,paramObject);
		}
		else
		{
			arrayOfEntry[i]=new Entry(paramInt,paramObject);
		}
		this.size+=1;
		if(this.size>=this.threshold) rehash(arrayOfEntry.length+1);
		return null;
	}

	public Object remove(int paramInt)
	{
		Entry[] arrayOfEntry=this.array;
		int i=(paramInt&0x7FFFFFFF)%arrayOfEntry.length;
		Entry localEntry1=arrayOfEntry[i];
		Entry localEntry2=null;
		while(localEntry1!=null)
		{
			if(localEntry1.key==paramInt)
			{
				Object localObject=localEntry1.value;
				if(localEntry2!=null)
					localEntry2.next=localEntry1.next;
				else
					arrayOfEntry[i]=localEntry1.next;
				this.size-=1;
				return localObject;
			}
			localEntry2=localEntry1;
			localEntry1=localEntry1.next;
		}
		return null;
	}

	public void rehash(int paramInt)
	{
		Entry[] arrayOfEntry1=this.array;
		int i=arrayOfEntry1.length;
		if(paramInt<=i) return;
		int j=i;
		while(j<paramInt)
			j=(j<<1)+1;

		Entry[] arrayOfEntry2=new Entry[j];

		int k=i-1;
		for(int m=0;k>=0;k--)
		{
			Entry localObject=arrayOfEntry1[k];

			while(localObject!=null)
			{
				Entry localEntry1=((Entry)localObject).next;
				m=(((Entry)localObject).key&0x7FFFFFFF)%j;
				Entry localEntry2=arrayOfEntry2[m];
				arrayOfEntry2[m]=localObject;
				((Entry)localObject).next=localEntry2;
				localObject=localEntry1;
			}
		}
		this.array=arrayOfEntry2;
		this.threshold=((int)(j*this.loadFactor));
	}

	public int select(Selector paramSelector)
	{
		Entry[] arrayOfEntry=this.array;

		Entry localObject2=null;

		int j=0;
		for(int k=arrayOfEntry.length-1;k>=0;k--)
		{
			Entry localObject1=arrayOfEntry[k];
			while(localObject1!=null)
			{
				int i=paramSelector.select(localObject1);
				Entry localEntry=((Entry)localObject1).next;
				if(i==0)
				{
					localObject1=localEntry;
				}
				else if(i==1)
				{
					if(localObject2!=null)
						localObject2.next=localEntry;
					else
						arrayOfEntry[k]=localEntry;
					this.size-=1;
					j=i;
					localObject1=localEntry;
				}
				else
				{
					if(i==3)
					{
						if(localObject2!=null)
							localObject2.next=localEntry;
						else
							arrayOfEntry[k]=localEntry;
						this.size-=1;
					}
					return i;
				}
			}
		}
		return j;
	}

	public void clear()
	{
		Entry[] arrayOfEntry=this.array;
		for(int i=arrayOfEntry.length-1;i>=0;i--)
			arrayOfEntry[i]=null;
		this.size=0;
	}

	public int[] keyArray()
	{
		Entry[] arrayOfEntry=this.array;
		int[] arrayOfInt=new int[this.size];

		int i=arrayOfEntry.length-1;
		for(int j=0;i>=0;i--)
		{
			Entry localEntry=arrayOfEntry[i];
			while(localEntry!=null)
			{
				arrayOfInt[(j++)]=localEntry.key;
				localEntry=localEntry.next;
			}
		}
		return arrayOfInt;
	}

	public Object[] valueArray()
	{
		Entry[] arrayOfEntry=this.array;
		Object[] arrayOfObject=new Object[this.size];

		int i=arrayOfEntry.length-1;
		for(int j=0;i>=0;i--)
		{
			Entry localEntry=arrayOfEntry[i];
			while(localEntry!=null)
			{
				arrayOfObject[(j++)]=localEntry.value;
				localEntry=localEntry.next;
			}
		}
		return arrayOfObject;
	}

	public int valueArray(Object[] paramArrayOfObject)
	{
		Entry[] arrayOfEntry=this.array;
		int i=paramArrayOfObject.length>this.size?this.size
			:paramArrayOfObject.length;
		if(i==0) return 0;

		int j=0;
		for(int k=arrayOfEntry.length-1;k>=0;k--)
		{
			Entry localEntry=arrayOfEntry[k];
			while(localEntry!=null)
			{
				paramArrayOfObject[(j++)]=localEntry.value;
				if(j>=i) return j;
				localEntry=localEntry.next;
			}
		}
		return j;
	}

	public String toString()
	{
		return super.toString()+"[size="+this.size+", capacity="
			+this.array.length+"]";
	}

	public final class Entry
	{

		int key;
		Entry next;
		Object value;

		Entry(int paramObject,Object arg3)
		{
			this.key=paramObject;
			this.value=arg3;
		}

		public int getKey()
		{
			return this.key;
		}

		public Object getValue()
		{
			return this.value;
		}
	}
}