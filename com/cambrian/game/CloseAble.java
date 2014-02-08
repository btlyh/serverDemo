/** */
package com.cambrian.game;

/**
 * 类说明：可关闭对象（用于系统关闭前调用）
 * 
 * @version 2013-5-6
 * @author HYZ (huangyz1988@qq.com)
 */
public interface CloseAble
{

	/** 关闭完成 */
	public static final int CLOSEOVER=0;

	/** 关闭操作 */
	public abstract void close();
}
