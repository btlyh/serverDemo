/**
 * 
 */
package com.cambrian.common.net;

public class ConnectArray
{

	public static final NioTcpConnect[] NULL=new NioTcpConnect[0];

	NioTcpConnect[] array=NULL;

	public int size()
	{
		return this.array.length;
	}

	public NioTcpConnect[] getArray()
	{
		return this.array;
	}

	public boolean contain(NioTcpConnect paramConnect)
	{
		NioTcpConnect[] arrayOfConnect=this.array;
		int i;
		if(paramConnect!=null)
		{
			for(i=arrayOfConnect.length-1;i>=0;--i)
			{
				if(paramConnect.equals(arrayOfConnect[i])) return true;
			}

		}
		else
		{
			for(i=arrayOfConnect.length-1;i>=0;--i)
			{
				if(arrayOfConnect[i]==null) return true;
			}
		}
		return false;
	}

	public synchronized void add(NioTcpConnect paramConnect)
	{
		int i=this.array.length;
		NioTcpConnect[] arrayOfConnect=new NioTcpConnect[i+1];
		if(i>0) System.arraycopy(this.array,0,arrayOfConnect,0,i);
		arrayOfConnect[i]=paramConnect;
		this.array=arrayOfConnect;
	}

	public synchronized boolean remove(NioTcpConnect paramConnect)
	{
		int i=this.array.length-1;
		if(paramConnect!=null)
		{
			for(;i>=0;--i)
			{
				if(paramConnect.equals(this.array[i])) break;
			}
		}
		else
		{
			for(;i>=0;--i)
			{
				if(this.array[i]==null) break;
			}
		}
		if(i<0) return false;
		if(this.array.length==1)
		{
			this.array=NULL;
			return true;
		}
		NioTcpConnect[] arrayOfConnect=new NioTcpConnect[this.array.length-1];
		if(i>0) System.arraycopy(this.array,0,arrayOfConnect,0,i);
		if(i<arrayOfConnect.length)
			System.arraycopy(this.array,i+1,arrayOfConnect,i,
				arrayOfConnect.length-i);
		this.array=arrayOfConnect;
		return true;
	}

	public synchronized void clear()
	{
		this.array=NULL;
	}

	public String toString()
	{
		return super.toString()+"[size="+this.array.length+"]";
	}
}
