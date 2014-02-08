/** */
package com.cambrian.common.xml;

import org.dom4j.Element;

import com.cambrian.common.util.TextKit;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 2013-4-11
 * @author HYZ (huangyz1988@qq.com)
 */
public class CharsParser extends XmlParser
{

	private static final String TARGET="chars";

	/* @see com.cambrian.common.xml.Parser#parse(org.dom4j.Element) */
	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String text=element.getTextTrim();
		if(text==null||text.isEmpty())
			throwErr(" Booleans value error, "+element.asXML());
		String[] texts=TextKit.split(text,',');
		char[] array=new char[texts.length];
		for(int i=0;i<texts.length;i++)
		{
			array[i]=texts[i].charAt(0);
		}
		String id=element.attributeValue("id");
		if(id!=null) context.set(id,array);
		return array;
	}
}
