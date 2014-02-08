/** */
package com.cambrian.common.xml;

import org.dom4j.Element;

import com.cambrian.common.util.TextKit;

/**
 * 类说明：数值解析器
 * 
 * @version 2013-4-10
 * @author HYZ (huangyz1988@qq.com)
 */
public class ValueParser extends XmlParser
{

	private static final String TARGET="value";

	/* @see com.cambrian.common.xml.Parser#parse(org.dom4j.Element) */
	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String type=element.attributeValue("type");
		String text=element.getTextTrim();
		Object obj=getValueForType(type,text);
		if(obj==null)
			throwErr(" Not a valid type, type="+type+", "+element.asXML());
		text=element.attributeValue("id");
		if(text!=null) context.set(text,obj);
		return obj;
	}

	Object getValueForType(String type,String text)
	{
		if(type==null||type.isEmpty()) return TextKit.parseInt(text);
		if("char".equalsIgnoreCase(type)) return text.charAt(0);
		if("int".equalsIgnoreCase(type)) return TextKit.parseInt(text);
		if("long".equalsIgnoreCase(type)) return TextKit.parseLong(text);
		if("float".equalsIgnoreCase(type)) return TextKit.parseFloat(text);
		if("double".equalsIgnoreCase(type))
			return TextKit.parseDouble(text);
		if("boolean".equalsIgnoreCase(type))
			return TextKit.parseBoolean(text);
		if("byte".equalsIgnoreCase(type))
			return (byte)TextKit.parseInt(text);
		if("short".equalsIgnoreCase(type))
			return (short)TextKit.parseInt(text);
		return null;
	}

}
