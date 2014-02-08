/** */
package com.cambrian.common.util;

import com.cambrian.common.net.CharBuffer;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 2013-4-22
 * @author HYZ (huangyz1988@qq.com)
 */
public final class AttributeList implements Cloneable
{

	public static final String[] NULL=new String[0];
	String[] array;

	public AttributeList()
	{
		this.array=NULL;
	}

	public AttributeList(String[] strs)
	{
		if(strs==null)
			throw new IllegalArgumentException(super.getClass().getName()
				+" <init>, null strs");
		if(strs.length%2!=0)
			throw new IllegalArgumentException(super.getClass().getName()
				+" <init>, invalid strs length:"+strs.length);
		this.array=strs;
	}

	public int size()
	{
		return (this.array.length/2);
	}

	public String[] getArray()
	{
		return this.array;
	}

	int indexOf(String[] array,String key)
	{
		int i=array.length-2;
		if(key!=null)
		{
			for(;i>=0;i-=2)
			{
				if(key.equals(array[i])) break;
			}
		}
		else
			for(;i>=0;i-=2)
			{
				if(array[i]==null) break;
			}
		return i;
	}

	public boolean contain(String key)
	{
		return (indexOf(this.array,key)>0);
	}

	public String get(String key)
	{
		String[] array=this.array;
		int i=indexOf(array,key);
		if(i<0) return null;
		return array[(i+1)];
	}

	public synchronized String set(String key,String value)
	{
		String[] array=this.array;
		int i=indexOf(array,key);
		if(i>=0)
		{
			String old=array[(i+1)];
			array[(i+1)]=value;
			return old;
		}
		i=array.length;
		String[] temp=new String[i+2];
		if(i>0) System.arraycopy(array,0,temp,0,i);
		temp[i]=key;
		temp[(i+1)]=value;
		this.array=temp;
		return null;
	}

	public synchronized String remove(String key)
	{
		String[] array=this.array;
		int i=indexOf(array,key);
		if(i<0) return null;
		String value=array[(i+1)];
		if(array.length<=2)
		{
			this.array=NULL;
			return value;
		}
		String[] temp=new String[array.length-2];
		if(i>0) System.arraycopy(array,0,temp,0,i);
		if(i<temp.length) System.arraycopy(array,i+2,temp,i,temp.length-i);
		this.array=temp;
		return value;
	}

	public synchronized void reset(String[] strs)
	{
		if(strs==null)
			throw new IllegalArgumentException(super.getClass().getName()
				+" reset, null strs");
		if(strs.length%2!=0)
			throw new IllegalArgumentException(super.getClass().getName()
				+" reset, invalid strs length:"+strs.length);
		this.array=strs;
	}

	public synchronized void clears()
	{
		this.array=NULL;
	}

	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			throw new RuntimeException(super.getClass().getName()
				+" clone, size="+this.array.length,e);
		}
	}

	public String toString()
	{
		String[] array=this.array;
		CharBuffer cb=new CharBuffer(array.length*10+30);
		cb.append(super.toString());
		cb.append("[size=").append(array.length).append(", {");
		if(array.length>0)
		{
			for(int i=0;i<array.length;i+=2)
			{
				cb.append(array[i]).append('=');
				cb.append(array[(i+1)]).append(' ');
			}
			cb.setTop(cb.top()-1);
		}
		cb.append('}').append(']');
		return cb.getString();
	}
}