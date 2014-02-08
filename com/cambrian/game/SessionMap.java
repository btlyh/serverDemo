/** */
package com.cambrian.game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.common.util.Selector;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 2013-4-22
 * @author HYZ (huangyz1988@qq.com)
 */
public class SessionMap implements SessionSet,Selectable
{

	public static final int SIZE=101;
	@SuppressWarnings("rawtypes")
	private Map map;

	public SessionMap()
	{
		this(101);
	}

	@SuppressWarnings("rawtypes")
	public SessionMap(int size)
	{
		this.map=new HashMap(size,0.5F);
	}

	public synchronized int size()
	{
		return this.map.size();
	}

	@SuppressWarnings("unchecked")
	public synchronized Session[] getSessions()
	{
		Session[] sessions=new Session[this.map.size()];
		this.map.values().toArray(sessions);
		return sessions;
	}

	public boolean contain(Session session)
	{
		if(session==null) return false;
		return (get(session.getId())!=null);
	}

	public synchronized Session get(String id)
	{
		if(id==null) return null;
		return ((Session)this.map.get(id));
	}

	@SuppressWarnings("unchecked")
	public synchronized boolean add(Session session)
	{
		this.map.put(session.getId(),session);
		return true;
	}

	public boolean add(Session[] ss)
	{
		if((ss==null)||(ss.length==0)) return false;
		return add(ss,0,ss.length);
	}

	@SuppressWarnings("unchecked")
	public synchronized boolean add(Session[] ss,int index,int length)
	{
		if((ss==null)||(index<0)||(length<=0)||(ss.length<index+length))
			return false;
		int i=0;
		for(int j=index;i<length;++j)
		{
			if(ss[j]!=null) this.map.put(ss[j].getId(),ss[j]);
			++i;
		}

		return true;
	}

	public boolean remove(Session session)
	{
		if(session==null) return false;
		return (remove(session.getId())!=null);
	}

	public synchronized Session remove(String id)
	{
		if(id==null) return null;
		return ((Session)this.map.remove(id));
	}

	public synchronized void send(ByteBuffer data)
	{
		Iterator<?> it=this.map.values().iterator();
		while(it.hasNext())
		{
			NioTcpConnect connect=((Session)it.next()).getConnect();
			if(connect==null) continue;
			// connect.send(data);
		}
	}

	public synchronized void sendWithout(ByteBuffer data,Session session)
	{
		Iterator<?> it=this.map.values().iterator();
		while(it.hasNext())
		{
			Object temp=it.next();
			if(session!=temp)
			{
				NioTcpConnect connect=((Session)temp).getConnect();
				if(connect==null) continue;
				// connect.send(data);
			}
		}
	}

	public synchronized int select(Selector selector)
	{
		int r=0;
		Iterator<?> it=this.map.values().iterator();
		while(it.hasNext())
		{
			Object session=it.next();
			int t=selector.select(session);
			if(t!=0) if(t==1)
			{
				it.remove();
				r=t;
			}
			else
			{
				if(t==3) it.remove();
				return t;
			}
		}
		return r;
	}

	public synchronized void clear()
	{
		this.map.clear();
	}

	public String toString()
	{
		return super.toString()+"[size="+size()+"]";
	}
}