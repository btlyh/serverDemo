package com.cambrian.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 类说明：转换字符串为int(0-3维)数组,分隔符为",","|",":"
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class StringKit
{

	/* static fields */

	/* static methods */
	/** 解析字符串为数字 */
	public static int parseInt(String value)
	{
		return Integer.parseInt(value);
	}
	/** 解析字符串(以,分割)为一维数字数组 */
	public static int[] parseInts(String value)
	{
		if(value==null||value.equals("")) return new int[0];
		String[] strs=value.split(",");
		int[] returns=new int[strs.length];
		for(int i=0;i<returns.length;i++)
		{
			returns[i]=parseInt(strs[i]);
		}
		return returns;
	}
	/** 解析字符串(以,|分割)为二维数字数组 */
	public static int[][] parseIntss(String value)
	{
		if(value==null||value.equals("")) return new int[0][];
		String[] strs=value.split("\\|");
		int[][] returns=new int[strs.length][];
		for(int i=0;i<returns.length;i++)
		{
			returns[i]=parseInts(strs[i]);
		}
		return returns;
	}
	/** 解析字符串(以,|,:分割)为三维数字数组 */
	public static int[][][] parseIntsss(String value)
	{
		if(value==null||value.equals("")) return new int[0][][];
		String[] strs=value.split(":");
		int[][][] returns=new int[strs.length][][];
		for(int i=0;i<returns.length;i++)
		{
			returns[i]=parseIntss(strs[i]);
		}
		return returns;
	}
	/** 解析字符串为数字 */
	public static Map<String,String> parseMap(String value)
	{
		Map<String,String> map=new HashMap<String,String>();
		if(value==null||value.equals("")) return map;
		String[] strs=value.split("&");
		for(int i=0;i<strs.length;i++)
		{
			if(strs[i].indexOf('=')==-1) continue;
			String[] entry=strs[i].split("=");
			map.put(entry[0],entry[1]);
		}
		return map;
	}
	/* fields */

	/* constructors */

	/* properties */

	/* init start */

	/* methods */

	/* common methods */

	/* inner class */
}
