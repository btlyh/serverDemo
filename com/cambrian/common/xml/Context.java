package com.cambrian.common.xml;

import java.util.HashMap;
import java.util.Map;

import com.cambrian.common.log.Logger;
import com.cambrian.common.util.ChangeAdapter;

/**
 * ��˵���������Ķ���
 * 
 * @version 2013-4-19
 * @author HYZ (huangyz1988@qq.com)
 */
public class Context extends ChangeAdapter
{

	/** ��־ */
	private static final Logger log=Logger.getLogger(Context.class);

	/** �Ƿ񾯸��ظ� */
	protected boolean warnExist=true;
	/** �Ƿ���Ա�������Сд */
	protected boolean ignoreCase=false;
	/** ǰ�������� */
	protected Context context;
	/** ���������� */
	protected Context baseContext;
	/** ���ñ������� */
	protected Map<String,Object> refMap;

	public Context()
	{
		refMap=new HashMap<String,Object>(100);
	}

	/** ����ǰ�������� */
	public void setBaseContext(Context context)
	{
		this.baseContext=context;
	}
	/** ����ǰ�������� */
	public Context getBaseContext()
	{
		return baseContext;
	}
	/** ����ǰ�������� */
	public void setContext(Context context)
	{
		this.context=context;
	}
	/** ����ǰ�������� */
	public Context getContext()
	{
		return context;
	}
	/** �����Ƿ���Ա�������Сд */
	public void setIgnoreCase(boolean ignore)
	{
		ignoreCase=ignore;
	}
	/** �����Ƿ񾯸��ظ�������� */
	public void setWarnExist(boolean warn)
	{
		warnExist=warn;
	}
	/** �����������Ƿ����ָ������ */
	boolean checkExist(String id)
	{
		return baseContext==null?false:baseContext.contain(id);
	}
	/** ��������ı��� */
	public synchronized Object get(String name)
	{
		if(name==null||name.isEmpty()) return null;
		if(ignoreCase) name=name.toLowerCase();
		if(checkExist(name)) return baseContext.get(name);
		Object value=refMap.get(name);
		if(context==null) return value;
		return value==null?context.get(name):value;
	}
	/** ���������ı��� */
	public synchronized Object set(String name,Object value)
	{
		if(name==null||name.isEmpty())
		{
			if(log.isWarnEnabled()) log.warn("SET, invalid id='"+name);
			return null;
		}
		if(ignoreCase) name=name.toLowerCase();
		if(checkExist(name))
			throw new RuntimeException(
				"Can't override the system variablse var="+name);// ���ܸ���ϵͳ�������ñ���
		Object old=refMap.get(name);
		if(old!=null&&warnExist&&log.isWarnEnabled())
			log.warn("id exist, id='"+name+"' old="+value);
		return refMap.put(name,value);
	}
	/** ���������Ƿ����ָ������ */
	public boolean contain(String id)
	{
		if(id==null||id.isEmpty()) return false;
		if(ignoreCase) id=id.toLowerCase();
		if(checkExist(id)) return true;
		if(refMap.containsKey(id)) return true;
		return context==null?false:context.contain(id);
	}
	/** �Ƴ���������ָ������ */
	public synchronized Object remove(String name)
	{
		if(name==null||name.isEmpty())
		{
			if(log.isDebugEnabled())
				log.debug("remove error id='"+name+"'");
			return null;
		}
		if(ignoreCase) name=name.toLowerCase();
		return refMap.remove(name);
	}
	/** ��ñ������б� */
	public synchronized String[] keyArray()
	{
		return refMap.keySet().toArray(new String[refMap.size()]);
	}
	/** ������������ı��� */
	public synchronized void clear()
	{
		refMap.clear();
	}
	/** ��Ϣ */
	public String toString()
	{
		StringBuilder sb=new StringBuilder("[");
		sb.append("lastContext=").append(context);
		synchronized(refMap)
		{
			for(String key:refMap.keySet())
			{
				sb.append(' ');
				sb.append(key).append('=').append(refMap.get(key));
			}
		}
		sb.append("]");
		return sb.toString();
	}
}
