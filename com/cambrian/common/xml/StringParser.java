/** */
package com.cambrian.common.xml;

import org.dom4j.Element;

/**
 * 类说明：字符串解析器
 * 
 * @version 2013-4-11
 * @author HYZ (huangyz1988@qq.com)
 */
public class StringParser extends XmlParser
{

	private static final String TARGET="string";

	/* @see com.cambrian.common.xml.Parser#parse(org.dom4j.Element) */
	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String text=element.getTextTrim();
		String id=element.attributeValue("id");
		if(id!=null) context.set(id,text);
		return text;
	}

}
