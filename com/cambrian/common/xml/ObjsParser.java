/** */
package com.cambrian.common.xml;

import org.dom4j.Element;

import com.cambrian.common.util.ReflectKit;
import com.cambrian.common.util.TextKit;

/**
 * 类说明：对象数组
 * 
 * @version 2013-4-11
 * @author HYZ (huangyz1988@qq.com)
 */
public class ObjsParser extends XmlParser
{

	private static final String TARGET="objs";

	/* @see com.cambrian.common.xml.Parser#parse(org.dom4j.Element) */
	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String text=element.attributeValue("class");
		Class<?> clazz=null;
		try
		{
			clazz=Class.forName(text);
		}
		catch(Exception e)
		{
			throwErr(" Class not found, class="+text+", "+element.asXML(),e);
		}
		text=element.attributeValue("length");
		int length=TextKit.parseInt(text);
		Object obj=ReflectKit.newArray(clazz,new int[]{length});
		text=element.attributeValue("id");
		if(text!=null) context.set(text,obj);
		return obj;
	}
}
