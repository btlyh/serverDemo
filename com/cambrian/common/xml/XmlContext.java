package com.cambrian.common.xml;

import java.util.HashMap;
import java.util.Map;

import com.cambrian.common.log.Logger;
import com.cambrian.common.util.FileMonitor;

/**
 * 类说明：配置加载上下文对象
 * 
 * @version 2013-4-11
 * @author HYZ (huangyz1988@qq.com)
 */
public class XmlContext extends Context
{

	/** 日志 */
	private static final Logger log=Logger.getLogger(XmlContext.class);
	/** 配置表对应context */
	private static Map<String,Context> fileContext=new HashMap<String,Context>();

	public static void setXmlContext(String xml,Context context)
	{
		fileContext.put(xml,context);
	}
	public static Context getXmlContext(String xml)
	{
		return fileContext.get(xml);
	}

	/** 构建一个默认上下文 */
	public XmlContext()
	{
		super();
		set("context",this);
	}

	/** 构建一个具有基础上内容的配置加载内容对象 */
	public XmlContext(Context context)
	{
		this();
		setBaseContext(context);
	}

	/** 解析指定配置文件 */
	public Context parse(String xmlPath)
	{
		XmlParser.parse(xmlPath,this);
		return this;
	}
	/** 配置监控器改变监听 */
	public void change(Object source,int type,Object value)
	{
		if(source!=FileMonitor.getInstance()) return;
		if(type==1||type==3)
		{
			if(log.isInfoEnabled())
				log.info("xml changed, reload, file="+value);
			parse((String)value);
		}
	}

	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		if(baseContext==null) sb.append(baseContext);
		sb.append(super.toString());
		return sb.toString();
	}
}
