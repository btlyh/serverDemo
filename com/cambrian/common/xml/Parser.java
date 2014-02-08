package com.cambrian.common.xml;

import org.dom4j.Element;

/***
 * 类说明：解析器
 * 
 * @version 1.0
 * @author HYZ (huangyz1988@qq.com)
 */
public interface Parser
{

	/** 解析元素 */
	public abstract Object parse(Object obj,Element e,Context c);
}
