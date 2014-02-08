/** */
package com.cambrian.common.xml;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Iterator;

import org.dom4j.Element;
import org.dom4j.Node;

/**
 * ��˵������ӡ��ǩ����
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

	/** �ݹ�����������Ϣ(�������͡�ά�Ⱥͳ��Ȳ���) */
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

	/** ���һ�������ά�ȣ�ͨ�÷������Ƚϣ� */
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

	/** �Ƿ��ǻ�����������,�Ƿ���true������false */
	public static boolean isBaseArray(Object obj)
	{
		if(obj==null) return false;
		// �������c��Ϊ�գ���ͨ����cΪ�գ�����cΪ�ղ�������
		Class<?> c=obj.getClass().getComponentType();
		return c==null?false:c.isPrimitive();
	}
}
