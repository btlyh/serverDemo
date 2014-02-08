package com.cambrian.common.xml;

import org.dom4j.Element;

import com.cambrian.common.field.FieldValue;

/***
 * 类说明：null解析器
 * 
 * @version 1.0
 * @author HYZ (huangyz1988@qq.com)
 */
public class NullParser extends XmlParser
{

	private static final String TARGET="null";

	/* @see com.cambrian.common.xml.Parser#parse(org.dom4j.Element) */
	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String type=element.attributeValue("class");
		Class<?> c;
		FieldValue fv=null;
		try
		{
			c=Class.forName(type);
			fv=new FieldValue();
			fv.type=c;
		}
		catch(ClassNotFoundException e)
		{
			throwErr("Class no found class="+type,e);
		}
		return fv;
	}
}
