package com.cambrian.common.xml;

import java.util.Iterator;

import org.dom4j.Element;

import com.cambrian.common.object.Sample;
import com.cambrian.common.util.ArrayList;

/**
 * 类说明：根对象解析
 * 
 * @version 2013-5-14
 * @author HYZ (huangyz1988@qq.com)
 */
public class ObjectsParser extends XmlParser
{

	public static final String TARGET="objects";

	public Object parse(Object obj,Element element,Context context)
	{
		checkTarget(TARGET,element);
		Iterator<?> iter=element.elementIterator();
		Element child=null;
		ArrayList list=null;
		while(iter.hasNext())
		{
			child=(Element)iter.next();
			Parser p=getParser(child.getName().toLowerCase());
			obj=p.parse(null,child,context);
			if(obj==null||!(obj instanceof Sample)) continue;
			if(list==null)
			{
				list=new ArrayList();
				context.set("samples",list);
			}
			list.add(obj);
		}
		return null;
	}
}
