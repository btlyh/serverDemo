package com.cambrian.common.util;

/**
 * 类说明：改变监听转发器（监听列表）
 * 
 * @author HYZ(huangyz1988@qq.com)
 * @version 2013-7-30
 */
public class ChangeListenerList extends ChangeAdapter
{

	/** 监听列表 */
	ObjectArray listeners=new ObjectArray();

	/** 监听器数量 */
	public int size()
	{
		return this.listeners.size();
	}

	public Object[] getListeners()
	{
		return this.listeners.getArray();
	}

	public void addListener(ChangeListener listener)
	{
		if(listener!=null) this.listeners.add(listener);
	}

	public void removeListener(ChangeListener listener)
	{
		this.listeners.remove(listener);
	}

	public void removeListeners()
	{
		this.listeners.clear();
	}

	public void change(Object src,int type)
	{
		Object[] objs=this.listeners.getArray();
		for(int i=objs.length-1;i>=0;i--)
			((ChangeListener)objs[i]).change(src,type);
	}

	public void change(Object src,int type,int v)
	{
		Object[] objs=this.listeners.getArray();
		for(int i=objs.length-1;i>=0;i--)
			((ChangeListener)objs[i]).change(src,type,v);
	}

	public void change(Object src,int type,int v1,int v2)
	{
		Object[] objs=this.listeners.getArray();
		for(int i=objs.length-1;i>=0;i--)
			((ChangeListener)objs[i]).change(src,type,v1,v2);
	}

	public void change(Object src,int type,int v1,int v2,int v3)
	{
		Object[] arrayOfObject=this.listeners.getArray();
		for(int i=arrayOfObject.length-1;i>=0;i--)
			((ChangeListener)arrayOfObject[i]).change(src,type,v1,v2,v3);
	}

	public void change(Object src,int type,Object o)
	{
		Object[] arrayOfObject=this.listeners.getArray();
		for(int i=arrayOfObject.length-1;i>=0;i--)
			((ChangeListener)arrayOfObject[i]).change(src,type,o);
	}

	public void change(Object src,int type,Object o1,Object o2)
	{
		Object[] objs=this.listeners.getArray();
		for(int i=objs.length-1;i>=0;i--)
			((ChangeListener)objs[i]).change(src,type,o1,o2);
	}

	public void change(Object src,int type,Object o1,Object o2,Object o3)
	{
		Object[] objs=this.listeners.getArray();
		for(int i=objs.length-1;i>=0;i--)
			((ChangeListener)objs[i]).change(src,type,o1,o2,o3);
	}
}