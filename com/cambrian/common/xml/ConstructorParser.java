/** */
package com.cambrian.common.xml;

import java.lang.reflect.Constructor;

import org.dom4j.Element;

import com.cambrian.common.field.FieldValue;
import com.cambrian.common.util.ReflectKit;
import com.cambrian.common.util.TextKit;

/**
 * 类说明：构造方法解析器
 * 
 * @version 2013-4-11
 * @author HYZ (huangyz1988@qq.com)
 */
public class ConstructorParser extends MethodParser
{

	private static final String TARGET="constructor";

	/* @see com.cambrian.common.xml.Parser#parse(org.dom4j.Element) */
	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String text=element.attributeValue("class");
		Class<?> c=null;
		try
		{
			c=Class.forName(text);
		}
		catch(ClassNotFoundException e)
		{
			throwErr(" Class not found, class="+text,e);
		}
		text=element.attributeValue("declared");
		boolean declared=(text==null?false:TextKit.parseBoolean(text));
		FieldValue[] params=parseParams(element,context);
		Constructor<?> constructor=ReflectKit.adaptConstructor(c,params,
			declared);
		if(constructor==null)
			throwErr(" constructor not found, c="+c+", argNum="
				+(params==null?null:params.length));
		Object[] args=null;
		if(params!=null)
		{
			args=new Object[params.length];
			for(int i=0;i<args.length;i++)
				args[i]=params[i].value;
		}
		Object obj=null;
		try
		{
			obj=constructor.newInstance(args);
		}
		catch(Exception e)
		{
			throwErr(" newInstence error, class="+text,e);
		}
		String id=element.attributeValue("id");
		if(id!=null) context.set(id,obj);
		return obj;
	}
}
