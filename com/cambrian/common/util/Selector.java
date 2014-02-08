package com.cambrian.common.util;

/**
 * 类说明：选择器
 * 
 * @author HYZ(huangyz1988@qq.com)
 * @version 2013-7-30
 */
public abstract interface Selector
{

	/** 不符合选择 */
	public static final int FALSE=0;
	/** 符合选择 */
	public static final int TRUE=1;
	/** 不符合选择跳出 */
	public static final int FALSE_BREAK=2;
	/** 符合选择跳出 */
	public static final int TRUE_BREAK=3;

	/** 选择指定对象，返回选择状态 */
	public abstract int select(Object paramObject);
}