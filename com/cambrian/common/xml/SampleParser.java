package com.cambrian.common.xml;

import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.cambrian.common.field.FieldValue;
import com.cambrian.common.log.Logger;
import com.cambrian.common.object.Sample;
import com.cambrian.common.util.ArrayList;
import com.cambrian.common.util.ReflectKit;
import com.cambrian.common.util.TextKit;

/**
 * 类说明：模板对象解析器
 * 
 * @version 2013-4-15
 * @author HYZ (huangyz1988@qq.com)
 */
public class SampleParser extends XmlParser
{

	private static final Logger log=Logger.getLogger(SampleParser.class);
	/** 当前解析器对应标签 */
	protected static final String TARGET="sample";
	/** 变量名称屏蔽 */
	protected static final String[] filter={"class","static","final","void",
		"abstruct","boolean","short","byte","long","int","double","float",
		"static","public","extends","private","protected"+"implements",
		"this","super","try","catch","null","true","false","while","return",
		"case","throw","throws","for","if","else","do","goto","new",
		"continue","break","enum","volatile","assert","default","finally",
		"char","import","instenceof","native","packge","strictfp","switch",
		"synchronized","transient"};
	protected static final String[] REGEXS={",","\\|",":","!","#",""};

	public Object parse(Object parent,Element element,Context context)
	{
		checkTarget(TARGET,element);
		String text=element.attributeValue("class");
		Class<?> clazz=null;
		Object obj=null;
		try
		{
			clazz=Class.forName(text);
			obj=clazz.newInstance();
		}
		catch(Exception e)
		{
			String xml=element.asXML();
			throwErr(" newInstence error, class="+text+", "+xml,e);
		}
		Iterator<?> iter=element.attributeIterator();
		Attribute att=null;
		ArrayList list=new ArrayList();
		while(iter.hasNext())
		{
			att=(Attribute)iter.next();
			if(isFilter(att.getName())) continue;
			String name=att.getName();
			Object value=att.getValue();
			if(name.charAt(0)=='_')
			{
				char c=name.charAt(1);
				if(c=='f'||c=='F')
					list.add(att);
				else
				{
					int dim=getIntValue(c);
					if(dim==0)
					{
						String xml=element.asXML();
						int row=xml.indexOf(name)+1;
						throwErr("invalid attribute, attribute="+name
							+", row="+row+", "+xml);
					}
					String[] regexs=new String[dim];
					for(int i=0,j=dim-1;j>=0;i=dim-1-(--j))
					{
						regexs[i]=REGEXS[j];
					}
					name=name.substring(2);
					FieldValue field=null;
					try
					{
						field=ReflectKit.getField(clazz,obj,name,true);
					}
					catch(Exception e)
					{
						String sid=element.attributeValue("sid");
						throwErr("Sample getField error, sid="+sid+" class="
							+clazz+", field="+name+", "+element.asXML(),e);
					}
					Class<?> type=ReflectKit.baseComponenType(field.type);
					value=TextKit.split((String)value,type,regexs,0);
					ReflectKit.setField(clazz,obj,name,value,true);
				}
			}
			else
				ReflectKit.setField(clazz,obj,name,value,true);
		}
		for(int i=list.size()-1;i>=0;i--)
		{
			att=(Attribute)list.get(i);
			if(isFilter(att.getName())) continue;
			String name=att.getName();
			Object value=att.getValue();
			name=name.substring(2);
			FieldValue[] fvs=new FieldValue[1];
			fvs[0]=new FieldValue(String.class,value);
			try
			{
				ReflectKit.invoke(clazz,obj,name,fvs,true);
			}
			catch(Exception e)
			{
				String sid=element.attributeValue("sid");
				throwErr("Sample invoke error, sid="+sid+", class="+clazz
					+", method="+name+", "+element.asXML(),e);
			}
		}
		Sample.factory.init((Sample)obj);
		return obj;
	}

	/** 是否是屏蔽字段名称 */
	boolean isFilter(String name)
	{
		for(int i=0;i<filter.length;i++)
		{
			if(filter[i].equals(name))
			{
				if(filter[i]!="class"&&log.isWarnEnabled())
					log.warn("Not a valid field, name="+name);
				return true;
			}
		}
		return false;
	}

	public int getIntValue(char c)
	{
		switch(c)
		{
			case '0':
				return 0;
			case '1':
				return 1;
			case '2':
				return 2;
			case '3':
				return 3;
			case '4':
				return 4;
			case '5':
				return 5;
			case '6':
				return 6;
			case '7':
				return 7;
			case '8':
				return 8;
			case '9':
				return 9;
			default:
				return 0;
		}
	}
}
