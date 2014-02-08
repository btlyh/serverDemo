/**
 * 
 */
package com.cambrian.common.timer;

/**
 * 类说明：定时事件监听器
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */

public interface TimerListener
{

	/** 定时事件的监听方法 */
	public void onTimer(TimerEvent e);

}