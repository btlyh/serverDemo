/** */
package com.cambrian.common.xml;

import java.util.Iterator;

import org.dom4j.Element;

/**
 * 类说明：对象解析器
 * 
 * @version 2013-4-11
 * @author HYZ (huangyz1988@qq.com)
 */
public class ObjParser extends XmlParser
{

	private static final String TARGET="obj";

	/* @see com.cambrian.common.xml.Parser#parse(org.dom4j.Element) */
	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String text=element.attributeValue("class");
		Object obj=null;
		try
		{
			Class<?> clazz=Class.forName(text);
			obj=clazz.newInstance();
		}
		catch(Exception e)
		{
			String xml=element.asXML();
			throwErr(" newInstence error, class="+text+", "+xml,e);
		}
		sequenParse(obj,element,context);
		text=element.attributeValue("id");
		if(text!=null) context.set(text,obj);
		return obj;
	}

	void sequenParse(Object obj,Element element,Context context)
	{
		Iterator<?> iter=element.elementIterator();
		Element child=null;
		Parser parser=null;
		while(iter.hasNext())
		{
			child=(Element)iter.next();
			parser=getParser(child.getName());
			if(parser==null) throwErr(" Unknown target! "+child.asXML());
			parser.parse(obj,child,context);
		}
	}
}
