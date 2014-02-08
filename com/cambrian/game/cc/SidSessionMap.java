/** */
package com.cambrian.game.cc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cambrian.common.util.Selector;
import com.cambrian.game.Session;
import com.cambrian.game.SessionMap;

/**
 * 类说明：Sid会话列表
 * 
 * @version 2013-4-25
 * @author HYZ (huangyz1988@qq.com)
 */
public class SidSessionMap extends SessionMap
{

	@SuppressWarnings("rawtypes")
	Map sidMap;

	public SidSessionMap()
	{
		this(101);
	}

	@SuppressWarnings("rawtypes")
	public SidSessionMap(int size)
	{
		super(size);
		this.sidMap=new HashMap(size,0.5F);
	}

	public synchronized Session getBySid(String sid)
	{
		return ((Session)this.sidMap.get(sid));
	}

	@SuppressWarnings("unchecked")
	public synchronized boolean add(Session session)
	{
		if(!(super.add(session))) return false;
		this.sidMap.put(session.getSource(),session);
		return true;
	}

	@SuppressWarnings("unchecked")
	public synchronized boolean add(Session[] ss,int index,int length)
	{
		if(!(super.add(ss,index,length))) return false;
		int i=0;
		for(int j=index;i<length;++j)
		{
			if(ss[j]!=null) this.sidMap.put(ss[j].getSource(),ss[j]);
			++i;
		}
		return true;
	}

	public synchronized Session remove(String id)
	{
		Session s=super.remove(id);
		if(s!=null) this.sidMap.remove(s.getSource());
		return s;
	}

	public synchronized int select(Selector selector)
	{
		int r=0;

		@SuppressWarnings("rawtypes")
		Iterator it=this.sidMap.values().iterator();
		while(it.hasNext())
		{
			Object session=it.next();
			int t=selector.select(session);
			if(t!=0) if(t==1)
			{
				it.remove();
				super.remove((Session)session);
				r=t;
			}
			else
			{
				if(t==3)
				{
					it.remove();
					super.remove((Session)session);
				}
				return t;
			}
		}
		return r;
	}

	public synchronized void clear()
	{
		super.clear();
		this.sidMap.clear();
	}
}
