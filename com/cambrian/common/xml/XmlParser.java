package com.cambrian.common.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cambrian.common.log.Logger;

/**
 * ��˵����xml���ñ������
 * 
 * @version 2013-5-14
 * @author HYZ (huangyz1988@qq.com)
 */
public abstract class XmlParser implements Parser
{

	/** ��־ */
	public static final Logger log=Logger.getLogger(XmlParser.class);
	/** �������б� */
	private static Map<String,Parser> parserMap=new HashMap<String,Parser>();
	static
	{
		// TODO -------��̬��ʼ���������б�
		parserMap.put("objects",new ObjectsParser());
		parserMap.put("ref",new RefParser());
		parserMap.put("obj",new ObjParser());
		parserMap.put("objs",new ObjsParser());
		parserMap.put("field",new FieldParser());
		parserMap.put("method",new MethodParser());
		parserMap.put("sample",new SampleParser());
		parserMap.put("samples",new SamplesParser());
		parserMap.put("constructor",new ConstructorParser());
		parserMap.put("value",new ValueParser());
		parserMap.put("bytes",new BytesParser());
		parserMap.put("chars",new CharsParser());
		parserMap.put("shorts",new ShortsParser());
		parserMap.put("ints",new IntsParser());
		parserMap.put("longs",new LongsParser());
		parserMap.put("floats",new FloatsParser());
		parserMap.put("doubles",new DoublesParser());
		parserMap.put("booleans",new BooleansParser());
		parserMap.put("array",new ArrayParser());
		parserMap.put("string",new StringParser());
		parserMap.put("strings",new StringsParser());
		parserMap.put("print",new PrintParser());
		parserMap.put("null",new NullParser());
	}

	/** ����ָ�������ļ� */
	public static void parse(String xmlFile,Context context)
	{
		if(log.isInfoEnabled()) log.info("parse xml file="+xmlFile);
		if(context==null)
			throwErr("parse error, invalid context= "+context);
		Document document=null;
		try
		{
			document=new SAXReader().read(new File(xmlFile));
		}
		catch(DocumentException e)
		{
			throwErr("xml file read error, file="+xmlFile,e);
		}
		Element root=document.getRootElement();
		if(root==null) throwErr("root target not found, file="+xmlFile);
		Parser parser=getParser(root.getName());
		parser.parse(null,root,context);
	}

	/** �쳣�׳� */
	protected static void throwErr(String msg)
	{
		throw new RuntimeException(msg);
	}
	/** �쳣�׳� */
	protected static void throwErr(String msg,Exception e)
	{
		throw new RuntimeException(msg,e);
	}

	/** ���xml��ǩ�Ƿ�ƥ�䵱ǰ������ */
	public void checkTarget(String target,Element element)
	{
		if(target==null||target.isEmpty()||element==null)
			throwErr(" Not a valid target, "+element.asXML());
		String name=element.getName();
		if(!target.equalsIgnoreCase(name))
			throwErr(" Not matche the target, "+element.asXML());
	}

	public static Parser getParser(String target)
	{
		Parser p=null;
		if(target!=null&&!target.isEmpty())
			p=parserMap.get(target.toLowerCase());
		if(p!=null) return p;
		if(log.isWarnEnabled())
			log.warn("Parser not found, target='"+target+"'");
		return null;
	}
}
