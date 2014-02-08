/** */
package com.cambrian.common.xml;

import java.lang.reflect.Array;
import java.util.List;

import org.dom4j.Element;

import com.cambrian.common.object.Sample;
import com.cambrian.common.object.SampleFactory;
import com.cambrian.common.util.ReflectKit;

/**
 * 类说明：sample数组解析器
 * 
 * @version 2013-4-16
 * @author HYZ (huangyz1988@qq.com)
 */
public class SamplesParser extends XmlParser
{

	/** 当前解析器对应标签 */
	private static final String TARGET="samples";

	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String factoryId=element.attributeValue("factory");
		SampleFactory factory=(SampleFactory)context.get(factoryId);
		if(factory==null)
			throwErr("SampleFactory not found, factory="+factoryId);
		List<?> list=element.elements();
		int[] dims=new int[]{list.size()};
		Object array=ReflectKit.newArray(Sample.class,dims);
		Element child=null;
		Object sample=null;
		for(int index=0;index<list.size();index++)
		{
			child=(Element)list.get(index);
			sample=getParser(SampleParser.TARGET).parse(null,child,context);
			Array.set(array,index,sample);
		}
		String id=element.attributeValue("id");
		if(id!=null) context.set("id",array);
		factory.init((Sample[])array);
		return array;
	}
}
