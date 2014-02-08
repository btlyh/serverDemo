/** */
package com.cambrian.common.xml;

import java.lang.reflect.Array;

import org.dom4j.Element;

import com.cambrian.common.util.ReflectKit;
import com.cambrian.common.util.TextKit;

/**
 * 类说明：
 * 
 * @version 2013-4-11
 * @author HYZ (huangyz1988@qq.com)
 */
public class ArrayParser extends XmlParser
{

	/** 解析标签 */
	public static final String TARGET="array";

	/* @see com.cambrian.common.xml.Parser#parse(org.dom4j.Element) */
	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String str=element.attributeValue("type");
		Class<?> clazz=getClassForTarget(str);
		if(clazz==null) throwErr(" Array type error, type="+str);
		str=element.getTextTrim();
		if(str==null||str.isEmpty())
			throwErr(" Array value error, "+element.asXML());
		String[] texts=TextKit.split(str,',');
		Object arr=ReflectKit.newArray(clazz,new int[]{texts.length});
		for(int i=0;i<texts.length;i++)
			Array.set(arr,i,ReflectKit.getPrimitive(clazz,texts[i]));
		String id=element.attributeValue("id");
		if(id!=null) context.set(id,arr);
		return arr;
	}
	/** 获取指定标识的Class对象 */
	Class<?> getClassForTarget(String target)
	{
		Class<?> clazz=null;
		if("boolean".equalsIgnoreCase(target))
			clazz=Boolean.TYPE;
		else if("short".equalsIgnoreCase(target))
			clazz=Short.TYPE;
		else if("byte".equalsIgnoreCase(target))
			clazz=Byte.TYPE;
		else if("char".equalsIgnoreCase(target))
			clazz=Character.TYPE;
		else if("long".equalsIgnoreCase(target))
			clazz=Long.TYPE;
		else if("double".equalsIgnoreCase(target))
			clazz=Double.TYPE;
		else if("float".equalsIgnoreCase(target))
			clazz=Float.TYPE;
		else if("int".equalsIgnoreCase(target)) clazz=Integer.TYPE;
		return clazz;
	}
}
