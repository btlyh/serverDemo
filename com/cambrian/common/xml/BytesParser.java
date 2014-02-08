/** */
package com.cambrian.common.xml;

import org.dom4j.Element;

import com.cambrian.common.util.TextKit;

/**
 * 类说明：byte数组解析器
 * 
 * @version 2013-4-11
 * @author HYZ (huangyz1988@qq.com)
 */
public class BytesParser extends XmlParser
{

	private static final String TARGET="bytes";

	/* @see com.cambrian.common.xml.Parser#parse(org.dom4j.Element) */
	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String text=element.getTextTrim();
		if(text==null||text.isEmpty())
			throwErr(" Bytes value error, "+element.asXML());
		String[] texts=TextKit.split(text,',');
		byte[] array=new byte[texts.length];
		for(int i=0;i<texts.length;i++)
		{
			array[i]=(byte)TextKit.parseInt(texts[i]);
		}
		String id=element.attributeValue("id");
		if(id!=null) context.set(id,array);
		return array;
	}

}
