/** */
package com.cambrian.common.xml;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Iterator;

import org.dom4j.Element;
import org.dom4j.Node;

/**
 * 类说明：打印标签解析
 * 
 * @version 2013-4-23
 * @author HYZ (huangyz1988@qq.com)
 */
public class PrintParser extends XmlParser
{

	public static final String TARGET="print";
	public static final PrintStream ERR=System.err;
	public static final PrintStream OUT=System.out;

	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String type=element.attributeValue("type");
		Iterator<?> iter=element.nodeIterator();
		if(!iter.hasNext()) print("",type);
		while(iter.hasNext())
		{
			Node node=(Node)iter.next();
			if(node.getNodeType()==Node.TEXT_NODE)
			{
				print(node.getText(),type);
			}
			else if(node.getNodeType()==Node.ELEMENT_NODE)
			{
				Object obj=getParser(((Element)node).getName()).parse(null,
					(Element)node,context);
				print(obj,type);
			}
		}
		return null;
	}

	void print(Object obj,String type)
	{
		PrintStream print=OUT;
		if("err".equalsIgnoreCase(type)) print=ERR;
		print.println(toString(obj));
	}

	/** 递归数组数据信息(数组类型、维度和长度不限) */
	public static String toString(Object array)
	{
		if(array==null) return "null";
		StringBuilder buf=new StringBuilder();
		if((array instanceof Object[])||isBaseArray(array))
		{
			buf.append('{');
			int len=Array.getLength(array);
			for(int i=0;i<len;i++)
			{
				Object obj=Array.get(array,i);
				buf.append(toString(obj));
				if(i<len-1) buf.append(',');
			}
			buf.append('}');
		}
		else
			buf.append(array);
		return buf.toString();
	}

	/** 获得一个数组的维度（通用方法，比较） */
	public static int getDim(Object array)
	{
		if(array instanceof Object[]||isBaseArray(array))
		{
			String s=array.toString();
			for(int i=0;i<s.length();i++)
				if(s.charAt(i)!='[') return i;
		}
		return 0;
	}

	/** 是否是基本类型数组,是返回true，否则false */
	public static boolean isBaseArray(Object obj)
	{
		if(obj==null) return false;
		// 数组对象c不为空，普通对象c为空，所以c为空不是数组
		Class<?> c=obj.getClass().getComponentType();
		return c==null?false:c.isPrimitive();
	}
}
