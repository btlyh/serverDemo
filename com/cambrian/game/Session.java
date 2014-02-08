package com.cambrian.game;

import java.util.Map;
import java.util.TreeMap;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.common.util.ChangeListenerList;

/**
 * 类说明：会话
 * 
 * @version 2013-4-22
 * @author HYZ (huangyz1988@qq.com)
 */
public class Session extends ChangeListenerList implements Cloneable
{

	/** 登录改变 */
	public static final int LOGIN_CHANGED=1;
	/** 重新登录改变 */
	public static final int LOGIN_AGAIN_CHANGED=2;
	/** 更新登录改变 */
	public static final int LOGIN_RENEW_CHANGED=3;
	/** 状态改变 */
	public static final int STATE_CHANGED=8;
	/** 更新改变 */
	public static final int UPDATE_CHANGED=11;
	/** 关闭改变 */
	public static final int CLOSE_CHANGED=21;
	/** 退出改变 */
	public static final int EXIT_CHANGED=22;

	/** id标识 */
	String id;
	/** 状态 */
	int state;
	/** 活动时间 */
	long activeTime;
	/** 连接对象 */
	NioTcpConnect connect;
	/** 源 */
	Object source;
	/** 引用对象 */
	Object reference;
	/** 状态列表 */
	StateList states;
	/** 属性列表 */
	Map<Object,Object> attributes;

	/** 通过指定id生成一个会话 */
	public Session(String id)
	{
		if(id==null)
			throw new IllegalArgumentException(super.getClass().getName()
				+" <init>, null id");
		this.id=id;
		this.states=new StateList();
		this.attributes=new TreeMap<Object,Object>();
	}

	/** 获取id */
	public String getId()
	{
		return this.id;
	}

	/** 是否是某个状态 */
	public boolean isState(int type)
	{
		return ((this.state&type)!=0);
	}

	/** 设置某个状态 */
	public void setState(int type,boolean b)
	{
		int old;
		if((this.state&type)!=0)
		{
			if(b) return;
			old=this.state;
			this.state&=(type^0xFFFFFFFF);
		}
		else
		{
			if(!(b)) return;
			old=this.state;
			this.state|=type;
		}
		stateChanged(type,old);
	}

	/** 获取活动时间 */
	public long getActiveTime()
	{
		return this.activeTime;
	}

	/** 设置活动时间 */
	public void setActiveTime(long time)
	{
		this.activeTime=time;
	}

	/** 获取连接对象 */
	public NioTcpConnect getConnect()
	{
		return this.connect;
	}

	/** 设置连接对象 */
	public void setConnect(NioTcpConnect connect)
	{
		this.connect=connect;
	}

	/** 获取源 */
	public Object getSource()
	{
		return this.source;
	}

	/** 设置源 */
	public void setSource(Object source)
	{
		this.source=source;
	}

	/** 获取引用对象 */
	public Object getReference()
	{
		return this.reference;
	}

	/** 设置应用对象 */
	public void setReference(Object ref)
	{
		this.reference=ref;
	}

	/** 是否是临时会话 */
	public boolean isTemp()
	{
		return this.isState(2);
	}

	/** 设置是否是临时会话 */
	public void setTemp(boolean b)
	{
		this.setState(2,b);
	}

	/** 状态列表数量 */
	public int stateSize()
	{
		return this.states.size();
	}

	/** 获得状态数组 */
	public int[] getStates()
	{
		return this.states.getArray();
	}

	/** 获得指定状态 */
	public int getState(int name)
	{
		return this.states.get(name);
	}

	/** 设置指定状态值 */
	public void setState(int name,int value)
	{
		this.states.set(name,value);
	}

	/** 移除指定状态 */
	public int removeState(int name)
	{
		return this.states.remove(name);
	}

	/** 清除所有状态 */
	public void clearStates()
	{
		this.states.clear();
	}

	/** 属性数量 */
	public int attributeSize()
	{
		synchronized(this.attributes)
		{
			return this.attributes.size();
		}
	}

	/** 获取属性字段名数量 */
	public Object[] getAttributeNames()
	{
		synchronized(this.attributes)
		{
			Object[] array=new Object[this.attributes.size()];
			this.attributes.keySet().toArray(array);
			return array;
		}
	}

	/** 获取指定属性字段值 */
	public Object getAttribute(Object name)
	{
		synchronized(this.attributes)
		{
			return this.attributes.get(name);
		}
	}

	/** 设置指定属性字段值 */
	public void setAttribute(Object name,Object value)
	{
		synchronized(this.attributes)
		{
			this.attributes.put(name,value);
		}
	}

	/** 移除指定属性字段值 */
	public Object removeAttribute(Object name)
	{
		synchronized(this.attributes)
		{
			return this.attributes.remove(name);
		}
	}

	/** 清除所有字段值 */
	public void clearAttributes()
	{
		this.attributes.clear();
	}

	/** 状态改变 */
	protected void stateChanged(int type,int old)
	{
		change(this,STATE_CHANGED,type,old);
	}
	/**  */
	public void send(ByteBuffer data)
	{
		// TODO Auto-generated method stub
		if(connect==null||!connect.isActive()) return;
		connect.send(data);
	}
	/** 信息 */
	public String toString()
	{
		return super.toString()+"[id="+this.id+", state="+this.state
			+", activeTime="+this.activeTime+", connect="+this.connect+"]";
	}

	@Override
	public Object clone()
	{
		Session session=null;
		try
		{
			session=(Session)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.source=source;
		return session;
	}

}