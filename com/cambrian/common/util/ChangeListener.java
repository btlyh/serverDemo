package com.cambrian.common.util;

/***
 * 类说明：改变监听器
 * 
 * @version 2013-5-4
 * @author HYZ (huangyz1988@qq.com)
 */
public abstract interface ChangeListener
{

	/** 监听方法 */
	public abstract void change(Object source,int type);
	/** 监听方法 */
	public abstract void change(Object source,int type,int v);
	/** 监听方法 */
	public abstract void change(Object source,int type,int v1,int v2);
	/** 监听方法 */
	public abstract void change(Object source,int type,int v1,int v2,int v3);
	/** 监听方法 */
	public abstract void change(Object source,int type,Object o1);
	/** 监听方法 */
	public abstract void change(Object source,int type,Object o1,Object o2);
	/** 监听方法 */
	public abstract void change(Object source,int type,Object o1,Object o2,
		Object o3);
}