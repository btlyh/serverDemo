package com.cambrian.common.xml;

import java.util.HashMap;
import java.util.Map;

import com.cambrian.common.log.Logger;
import com.cambrian.common.util.FileMonitor;

/**
 * ��˵�������ü��������Ķ���
 * 
 * @version 2013-4-11
 * @author HYZ (huangyz1988@qq.com)
 */
public class XmlContext extends Context
{

	/** ��־ */
	private static final Logger log=Logger.getLogger(XmlContext.class);
	/** ���ñ��Ӧcontext */
	private static Map<String,Context> fileContext=new HashMap<String,Context>();

	public static void setXmlContext(String xml,Context context)
	{
		fileContext.put(xml,context);
	}
	public static Context getXmlContext(String xml)
	{
		return fileContext.get(xml);
	}

	/** ����һ��Ĭ�������� */
	public XmlContext()
	{
		super();
		set("context",this);
	}

	/** ����һ�����л��������ݵ����ü������ݶ��� */
	public XmlContext(Context context)
	{
		this();
		setBaseContext(context);
	}

	/** ����ָ�������ļ� */
	public Context parse(String xmlPath)
	{
		XmlParser.parse(xmlPath,this);
		return this;
	}
	/** ���ü�����ı���� */
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
