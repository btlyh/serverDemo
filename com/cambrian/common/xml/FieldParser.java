/** */
package com.cambrian.common.xml;

import org.dom4j.Element;
import org.dom4j.Node;

import com.cambrian.common.field.FieldValue;
import com.cambrian.common.util.ReflectKit;
import com.cambrian.common.util.TextKit;

/**
 * 类说明：字段解析器
 * 
 * @version 2013-4-11
 * @author HYZ (huangyz1988@qq.com)
 */
public class FieldParser extends XmlParser
{

	// private static final Logger log=Logger.getLogger(FieldParser.class);
	/** 当前解析器对应xml标签 */
	private static final String TARGET="field";

	/** 解析字段值 */
	Object parseValue(Element element,Context context)
	{
		int count=element.nodeCount();
		Object returnObj=null;
		for(int i=0;i<count;i++)
		{
			Node node=element.node(i);
			if(node.getNodeType()==Node.TEXT_NODE)
			{
				String str=node.getText().trim();
				if(str.isEmpty()) continue;
				if(returnObj!=null)
					throwErr(" More of the value, "+element.asXML());
				returnObj=node.getText();
			}
			else if(node.getNodeType()==Node.ELEMENT_NODE)
			{
				Object obj=getParser(((Element)node).getName()).parse(null,
					(Element)node,context);
				if(returnObj!=null)
					throwErr(" More of the value, "+element.asXML());
				returnObj=obj;
			}
		}
		return returnObj;
	}

	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String name=element.attributeValue("name");
		String text=element.attributeValue("class");
		String id=element.attributeValue("ref");
		if(text!=null&&id!=null)
			throwErr(" More of refrence, field="+name+", "+element.asXML());
		Object obj=null;
		if(text==null&&id==null)
		{
			if(obj==null)
				throwErr(" Unknown refrence, field="+name+", "
					+element.asXML());
			obj=parent;
		}
		Class<?> clazz=null;
		if(obj!=null)
			clazz=obj.getClass();
		else if(id!=null)
		{
			obj=context.get(id);
			if(obj==null)
				throwErr(" Unknown refrence, field="+name+", id="+id+", "
					+element.asXML());
			clazz=obj.getClass();
		}
		else
		{
			try
			{
				clazz=Class.forName(text);
			}
			catch(ClassNotFoundException e)
			{
				throwErr(" Class not found, field="+name+", class="+text
					+", "+element.asXML(),e);
			}
		}
		text=element.attributeValue("declared");
		boolean declared=(text==null?false:TextKit.parseBoolean(text));
		Object value=parseValue(element,context);
		FieldValue fieldValue=null;
		try
		{
			if(value!=null)
				fieldValue=ReflectKit
					.setField(clazz,obj,name,value,declared);
			else
				fieldValue=ReflectKit.getField(clazz,obj,name,declared);
		}
		catch(Exception e)
		{
			throwErr(value==null?"getField error":"setField error",e);
		}
		id=element.attributeValue("id");
		if(id!=null)
			context.set(id,fieldValue==null?null:fieldValue.getValue());
		return fieldValue;
	}

}
