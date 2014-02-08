package com.cambrian.game;

import java.util.Map;
import java.util.TreeMap;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.common.util.ChangeListenerList;

/**
 * ��˵�����Ự
 * 
 * @version 2013-4-22
 * @author HYZ (huangyz1988@qq.com)
 */
public class Session extends ChangeListenerList implements Cloneable
{

	/** ��¼�ı� */
	public static final int LOGIN_CHANGED=1;
	/** ���µ�¼�ı� */
	public static final int LOGIN_AGAIN_CHANGED=2;
	/** ���µ�¼�ı� */
	public static final int LOGIN_RENEW_CHANGED=3;
	/** ״̬�ı� */
	public static final int STATE_CHANGED=8;
	/** ���¸ı� */
	public static final int UPDATE_CHANGED=11;
	/** �رոı� */
	public static final int CLOSE_CHANGED=21;
	/** �˳��ı� */
	public static final int EXIT_CHANGED=22;

	/** id��ʶ */
	String id;
	/** ״̬ */
	int state;
	/** �ʱ�� */
	long activeTime;
	/** ���Ӷ��� */
	NioTcpConnect connect;
	/** Դ */
	Object source;
	/** ���ö��� */
	Object reference;
	/** ״̬�б� */
	StateList states;
	/** �����б� */
	Map<Object,Object> attributes;

	/** ͨ��ָ��id����һ���Ự */
	public Session(String id)
	{
		if(id==null)
			throw new IllegalArgumentException(super.getClass().getName()
				+" <init>, null id");
		this.id=id;
		this.states=new StateList();
		this.attributes=new TreeMap<Object,Object>();
	}

	/** ��ȡid */
	public String getId()
	{
		return this.id;
	}

	/** �Ƿ���ĳ��״̬ */
	public boolean isState(int type)
	{
		return ((this.state&type)!=0);
	}

	/** ����ĳ��״̬ */
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

	/** ��ȡ�ʱ�� */
	public long getActiveTime()
	{
		return this.activeTime;
	}

	/** ���ûʱ�� */
	public void setActiveTime(long time)
	{
		this.activeTime=time;
	}

	/** ��ȡ���Ӷ��� */
	public NioTcpConnect getConnect()
	{
		return this.connect;
	}

	/** �������Ӷ��� */
	public void setConnect(NioTcpConnect connect)
	{
		this.connect=connect;
	}

	/** ��ȡԴ */
	public Object getSource()
	{
		return this.source;
	}

	/** ����Դ */
	public void setSource(Object source)
	{
		this.source=source;
	}

	/** ��ȡ���ö��� */
	public Object getReference()
	{
		return this.reference;
	}

	/** ����Ӧ�ö��� */
	public void setReference(Object ref)
	{
		this.reference=ref;
	}

	/** �Ƿ�����ʱ�Ự */
	public boolean isTemp()
	{
		return this.isState(2);
	}

	/** �����Ƿ�����ʱ�Ự */
	public void setTemp(boolean b)
	{
		this.setState(2,b);
	}

	/** ״̬�б����� */
	public int stateSize()
	{
		return this.states.size();
	}

	/** ���״̬���� */
	public int[] getStates()
	{
		return this.states.getArray();
	}

	/** ���ָ��״̬ */
	public int getState(int name)
	{
		return this.states.get(name);
	}

	/** ����ָ��״ֵ̬ */
	public void setState(int name,int value)
	{
		this.states.set(name,value);
	}

	/** �Ƴ�ָ��״̬ */
	public int removeState(int name)
	{
		return this.states.remove(name);
	}

	/** �������״̬ */
	public void clearStates()
	{
		this.states.clear();
	}

	/** �������� */
	public int attributeSize()
	{
		synchronized(this.attributes)
		{
			return this.attributes.size();
		}
	}

	/** ��ȡ�����ֶ������� */
	public Object[] getAttributeNames()
	{
		synchronized(this.attributes)
		{
			Object[] array=new Object[this.attributes.size()];
			this.attributes.keySet().toArray(array);
			return array;
		}
	}

	/** ��ȡָ�������ֶ�ֵ */
	public Object getAttribute(Object name)
	{
		synchronized(this.attributes)
		{
			return this.attributes.get(name);
		}
	}

	/** ����ָ�������ֶ�ֵ */
	public void setAttribute(Object name,Object value)
	{
		synchronized(this.attributes)
		{
			this.attributes.put(name,value);
		}
	}

	/** �Ƴ�ָ�������ֶ�ֵ */
	public Object removeAttribute(Object name)
	{
		synchronized(this.attributes)
		{
			return this.attributes.remove(name);
		}
	}

	/** ��������ֶ�ֵ */
	public void clearAttributes()
	{
		this.attributes.clear();
	}

	/** ״̬�ı� */
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
	/** ��Ϣ */
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