package com.cambrian.common.xml;

import org.dom4j.Element;

import com.cambrian.common.util.TextKit;

/***
 * 类说明：int数组解析器
 * 
 * @version 1.0
 * @author HYZ (huangyz1988@qq.com)
 */
public class IntsParser extends XmlParser
{

	private static final String TARGET="ints";

	/* @see com.cambrian.common.xml.Parser#parse(org.dom4j.Element) */
	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String text=element.getTextTrim();
		if(text==null||text.isEmpty())
			throwErr(" Booleans value error, "+element.asXML());
		String[] texts=TextKit.split(text,',');
		int[] array=new int[texts.length];
		for(int i=0;i<texts.length;i++)
		{
			array[i]=TextKit.parseInt(texts[i]);
		}
		String id=element.attributeValue("id");
		if(id!=null) context.set(id,array);
		return array;
	}
}
