package com.cambrian.common.xml;

import java.util.HashMap;
import java.util.Map;

import com.cambrian.common.log.Logger;
import com.cambrian.common.util.ChangeAdapter;

/**
 * 类说明：上下文对象
 * 
 * @version 2013-4-19
 * @author HYZ (huangyz1988@qq.com)
 */
public class Context extends ChangeAdapter
{

	/** 日志 */
	private static final Logger log=Logger.getLogger(Context.class);

	/** 是否警告重复 */
	protected boolean warnExist=true;
	/** 是否忽略变量名大小写 */
	protected boolean ignoreCase=false;
	/** 前置上下文 */
	protected Context context;
	/** 基础上下文 */
	protected Context baseContext;
	/** 引用变量缓存 */
	protected Map<String,Object> refMap;

	public Context()
	{
		refMap=new HashMap<String,Object>(100);
	}

	/** 设置前置上下文 */
	public void setBaseContext(Context context)
	{
		this.baseContext=context;
	}
	/** 设置前置上下文 */
	public Context getBaseContext()
	{
		return baseContext;
	}
	/** 设置前置上下文 */
	public void setContext(Context context)
	{
		this.context=context;
	}
	/** 设置前置上下文 */
	public Context getContext()
	{
		return context;
	}
	/** 设置是否忽略变量名大小写 */
	public void setIgnoreCase(boolean ignore)
	{
		ignoreCase=ignore;
	}
	/** 设置是否警告重复定义变量 */
	public void setWarnExist(boolean warn)
	{
		warnExist=warn;
	}
	/** 检查基础内容是否包含指定变量 */
	boolean checkExist(String id)
	{
		return baseContext==null?false:baseContext.contain(id);
	}
	/** 获得上下文变量 */
	public synchronized Object get(String name)
	{
		if(name==null||name.isEmpty()) return null;
		if(ignoreCase) name=name.toLowerCase();
		if(checkExist(name)) return baseContext.get(name);
		Object value=refMap.get(name);
		if(context==null) return value;
		return value==null?context.get(name):value;
	}
	/** 设置上下文变量 */
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
				"Can't override the system variablse var="+name);// 不能覆盖系统基础内置变量
		Object old=refMap.get(name);
		if(old!=null&&warnExist&&log.isWarnEnabled())
			log.warn("id exist, id='"+name+"' old="+value);
		return refMap.put(name,value);
	}
	/** 上下文中是否包含指定变量 */
	public boolean contain(String id)
	{
		if(id==null||id.isEmpty()) return false;
		if(ignoreCase) id=id.toLowerCase();
		if(checkExist(id)) return true;
		if(refMap.containsKey(id)) return true;
		return context==null?false:context.contain(id);
	}
	/** 移除上下文中指定变量 */
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
	/** 获得变量名列表 */
	public synchronized String[] keyArray()
	{
		return refMap.keySet().toArray(new String[refMap.size()]);
	}
	/** 清除所有上下文变量 */
	public synchronized void clear()
	{
		refMap.clear();
	}
	/** 信息 */
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
