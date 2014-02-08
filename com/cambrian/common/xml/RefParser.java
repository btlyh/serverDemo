/** */
package com.cambrian.common.xml;

import org.dom4j.Element;

/**
 * 类说明：对象引用解析器
 * 
 * @version 2013-4-11
 * @author HYZ (huangyz1988@qq.com)
 */
public class RefParser extends XmlParser
{

	private static final String TARGET="ref";

	/* @see com.cambrian.common.xml.Parser#parse(org.dom4j.Element) */
	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String id=element.attributeValue("id");
		Object obj=context.get(id);
		if(obj==null)
			throwErr(" Refrence not found, ref="+id+", "+element.asXML());
		return obj;
	}

}
