/** */
package com.cambrian.common.xml;

import org.dom4j.Element;
import org.dom4j.Node;

import com.cambrian.common.field.FieldValue;
import com.cambrian.common.util.ArrayList;
import com.cambrian.common.util.ReflectKit;
import com.cambrian.common.util.TextKit;

/**
 * 类说明：方法调用解析器
 * 
 * @version 2013-4-11
 * @author HYZ (huangyz1988@qq.com)
 */
public class MethodParser extends XmlParser
{

	private static final String TARGET="method";

	/** 解析方法配置参数 */
	FieldValue[] parseParams(Element element,Context context)
	{
		int count=element.nodeCount();
		if(count==0) return null;
		ArrayList list=new ArrayList();
		Object obj=null;
		for(int i=0;i<count;i++)
		{
			Node node=element.node(i);
			if(node.getNodeType()==Node.TEXT_NODE)
			{
				String str=node.getText().trim();
				if(str.isEmpty()) continue;
				if("null".equalsIgnoreCase(str))
					list.add(new FieldValue());// TODO null处理
				else
					list.add(new FieldValue(String.class,str));
			}
			else if(node.getNodeType()==Node.ELEMENT_NODE)
			{
				String name=((Element)node).getName();
				obj=getParser(name).parse(null,(Element)node,context);
				if(obj==null)
					throwErr(" Parameter error, paramXml:"+node.asXML());
				if(obj instanceof FieldValue)
					list.add(obj);
				else
				{
					Class<?> type=ReflectKit.getClass(obj);
					if(type==null) type=obj.getClass();
					list.add(new FieldValue(type,obj));
				}
			}
		}
		if(list.size()==0) return null;
		FieldValue[] fvs=new FieldValue[list.size()];
		list.toArray(fvs);
		return fvs;
	}
	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String name=element.attributeValue("name");
		String text=element.attributeValue("class");
		String id=element.attributeValue("ref");
		Object obj=null;
		if(id!=null&&text!=null)
			throwErr(" More of refrence, method="+name+", "+element.asXML());
		if(text==null&&id==null)
		{
			if(parent==null)
				throwErr(" Unknown refrence! method="+name+", "
					+element.asXML());
			obj=parent;
		}
		Class<?> clazz=null;
		if(obj!=null)
			clazz=obj.getClass();
		else if(text!=null)
		{
			try
			{
				clazz=Class.forName(text);
			}
			catch(ClassNotFoundException e)
			{
				String xml=element.asXML();
				throwErr(" Class not found, class="+text+", "+xml,e);
			}
		}
		else
		{
			obj=context.get(id);
			if(obj==null)
				throwErr(" Unknown refrence! method="+name+", ref="+id);
			clazz=obj.getClass();
		}
		FieldValue[] params=parseParams(element,context);
		text=element.attributeValue("declared");
		boolean declared=(text==null?false:TextKit.parseBoolean(text));
		FieldValue field=null;
		try
		{
			field=ReflectKit.invoke(clazz,obj,name,params,declared);
		}
		catch(Exception e)
		{
			throwErr("Invoke error, "+element.asXML(),e);
		}
		if(field!=null) obj=field.getValue();
		id=element.attributeValue("id");
		if(id!=null) context.set(id,obj);
		return obj;
	}
}
