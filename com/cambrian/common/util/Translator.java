package com.cambrian.common.util;

/**
 * 类说明：文字转换器
 * 
 * @version 2013-4-22
 * @author HYZ (huangyz1988@qq.com)
 */
public class Translator
{

	/** 转换变量1 */
	public static final String VARIABLE1="%$1%";
	/** 转换变量2 */
	public static final String VARIABLE2="%$2%";
	/** 转换变量3 */
	public static final String VARIABLE3="%$3%";
	/** 转换变量4 */
	public static final String VARIABLE4="%$4%";
	/** 转换器 */
	protected static Translator translator=new Translator(null);

	/** 父转换器 */
	Translator parent;
	/** 变量值1 */
	String variable1;
	/** 变量值2 */
	String variable2;
	/** 变量值3 */
	String variable3;
	/** 变量值4 */
	String variable4;

	public static Translator getInstance()
	{
		return translator;
	}

	/** 转换指定字符串 */
	public static String trans(String str)
	{
		return translator.translate(str);
	}

	public static String trans(String str,String value)
	{
		return translator.translate(str,value);
	}

	public static String trans(String str,String v1,String v2)
	{
		return translator.translate(str,v1,v2);
	}

	public static String trans(String str,String v1,String v2,String v3)
	{
		return translator.translate(str,v1,v2,v3);
	}

	public static String trans(String str,String v1,String v2,String v3,
		String v4)
	{
		return translator.translate(str,v1,v2,v4);
	}

	public Translator(Translator parent)
	{
		this.parent=parent;
	}

	public Translator getParent()
	{
		return this.parent;
	}

	public String getVariable1()
	{
		return this.variable1;
	}

	public void setVariable1(String str)
	{
		this.variable1=str;
	}

	public String getVariable2()
	{
		return this.variable2;
	}

	public void setVariable2(String str)
	{
		this.variable2=str;
	}

	public String getVariable3()
	{
		return this.variable3;
	}

	public void setVariable3(String str)
	{
		this.variable3=str;
	}

	public String getVariable4()
	{
		return this.variable4;
	}

	public void setVariable4(String str)
	{
		this.variable4=str;
	}

	public String getText(String str)
	{
		return null;
	}

	public void addText(String str,String text)
	{
	}

	public String removeText(String str)
	{
		return null;
	}

	public void configure()
	{
		translator=this;
	}

	public String translateText(String str)
	{
		String text=getText(str);
		if(text!=null) return text;
		return ((this.parent!=null)?this.parent.translateText(str):null);
	}
	/** 转换 */
	public String translate(String str)
	{
		String text=translateText(str);
		return ((text!=null)?text:str);
	}
	/** 转换 */
	public String translate(String str,String value)
	{
		String text=translateText(str);
		if(text==null) return str;
		return TextKit.replaceAll(text,this.variable1,value);
	}
	/** 转换 */
	public String translate(String str,String v1,String v2)
	{
		String text=translateText(str);
		if(text==null) return str;
		text=TextKit.replaceAll(text,this.variable1,v1);
		return TextKit.replaceAll(text,this.variable2,v2);
	}
	/** 转换 */
	public String translate(String str,String v1,String v2,String v3)
	{
		String text=translateText(str);
		if(text==null) return str;
		text=TextKit.replaceAll(text,this.variable1,v1);
		text=TextKit.replaceAll(text,this.variable2,v2);
		return TextKit.replaceAll(text,this.variable3,v3);
	}

	/** 转换 */
	public String translate(String str,String v1,String v2,String v3,
		String v4)
	{
		String text=translateText(str);
		if(text==null) return str;
		text=TextKit.replaceAll(text,this.variable1,v1);
		text=TextKit.replaceAll(text,this.variable2,v2);
		text=TextKit.replaceAll(text,this.variable3,v3);
		return TextKit.replaceAll(text,this.variable4,v4);
	}
}
