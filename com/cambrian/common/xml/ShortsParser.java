/** */
package com.cambrian.common.xml;

import org.dom4j.Element;

import com.cambrian.common.util.TextKit;

/**
 * 类说明：short数组解析器
 * 
 * @version 2013-4-11
 * @author HYZ (huangyz1988@qq.com)
 */
public class ShortsParser extends XmlParser
{

	private static final String TARGET="shorts";

	/* @see com.cambrian.common.xml.Parser#parse */
	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String text=element.getTextTrim();
		if(text==null||text.isEmpty())
			throwErr(" Booleans value error, "+element.asXML());
		String[] texts=TextKit.split(text,',');
		short[] array=new short[texts.length];
		for(int i=0;i<texts.length;i++)
		{
			array[i]=(short)TextKit.parseInt(texts[i]);
		}
		text=element.attributeValue("id");
		if(text!=null) context.set(text,array);
		return array;
	}

}
