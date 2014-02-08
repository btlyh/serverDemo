/** */
package com.cambrian.common.xml;

import org.dom4j.Element;

import com.cambrian.common.util.TextKit;

/**
 * 类说明：字符串数组解析器
 * 
 * @version 2013-4-11
 * @author HYZ (huangyz1988@qq.com)
 */
public class StringsParser extends XmlParser
{

	private static final String TARGET="strings";

	/* @see com.cambrian.common.xml.Parser#parse(org.dom4j.Element) */
	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String text=element.getTextTrim();
		Object texts=TextKit.split(text,',');
		text=element.attributeValue("id");
		if(text!=null) context.set(text,texts);
		return texts;
	}
}
