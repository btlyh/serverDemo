/**
 * 
 */
package com.cambrian.common.net;

import com.cambrian.common.net.DataAccessHandler.Entry;
import com.cambrian.common.util.ArrayList;

/**
 * ��˵����
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class ThreadAccessHandler
{

	/* static fields */
	public static final int TIMEOUT=30000;

	/* static methods */

	/* fields */
	private ArrayList entryList=new ArrayList();

	/* constructors */

	/* properties */

	/* init start */

	/* methods */
	public Entry getEntry(int id)
	{
		synchronized(entryList)
		{
			for(int i=entryList.size()-1;i>=0;--i)
			{
				Entry entry=(Entry)entryList.get(i);
				if(entry.id==id) return entry;
			}
		}
		return null;
	}

	public void addEntry(Entry entry)
	{
		synchronized(this.entryList)
		{
			this.entryList.add(entry);
		}
	}

	public boolean removeEntry(Entry entry)
	{
		synchronized(entryList)
		{
			return entryList.remove(entry);
		}
	}

	/** �Ƴ�ָ��ID�ķ�����Ŀ */
	public Entry removeEntry(int id)
	{
		Entry entry=null;
		synchronized(entryList)
		{
			for(int i=entryList.size()-1;i>=0;--i)
			{
				entry=(Entry)entryList.get(i);
				if(entry.id==id)
				{
					entryList.removeAt(i);
					return entry;
				}
			}
		}
		return null;
	}

	public Object access(Entry entry)
	{
		return access(entry,TIMEOUT);
	}

	public Object access(Entry entry,int timeout)
	{
		addEntry(entry);
		try
		{
			entry.access();
			synchronized(entry)
			{
				if(entry.result==DataAccessHandler.NONE)
				{
					try
					{
						entry.wait(timeout);
					}
					catch(InterruptedException e)
					{
					}
				}
			}
			return entry.result;
		}
		finally
		{
			removeEntry(entry);
		}
	}

	/** ָ֪ͨ��id����Ŀ�ϵȴ����߳� */
	public void notify(int id,Object data)
	{
		Entry entry=getEntry(id);
		if(entry==null) return;
		entry.result=data;
		synchronized(entry)
		{
			entry.notifyAll();
		}
	}
	/* common methods */

	/* inner class */

}
