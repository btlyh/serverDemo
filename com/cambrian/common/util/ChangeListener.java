package com.cambrian.common.util;

/***
 * ��˵�����ı������
 * 
 * @version 2013-5-4
 * @author HYZ (huangyz1988@qq.com)
 */
public abstract interface ChangeListener
{

	/** �������� */
	public abstract void change(Object source,int type);
	/** �������� */
	public abstract void change(Object source,int type,int v);
	/** �������� */
	public abstract void change(Object source,int type,int v1,int v2);
	/** �������� */
	public abstract void change(Object source,int type,int v1,int v2,int v3);
	/** �������� */
	public abstract void change(Object source,int type,Object o1);
	/** �������� */
	public abstract void change(Object source,int type,Object o1,Object o2);
	/** �������� */
	public abstract void change(Object source,int type,Object o1,Object o2,
		Object o3);
}