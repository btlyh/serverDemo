package com.cambrian.common.xml;

import org.dom4j.Element;

/***
 * ��˵����������
 * 
 * @version 1.0
 * @author HYZ (huangyz1988@qq.com)
 */
public interface Parser
{

	/** ����Ԫ�� */
	public abstract Object parse(Object obj,Element e,Context c);
}
