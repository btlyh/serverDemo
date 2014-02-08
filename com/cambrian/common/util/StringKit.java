package com.cambrian.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * ��˵����ת���ַ���Ϊint(0-3ά)����,�ָ���Ϊ",","|",":"
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class StringKit
{

	/* static fields */

	/* static methods */
	/** �����ַ���Ϊ���� */
	public static int parseInt(String value)
	{
		return Integer.parseInt(value);
	}
	/** �����ַ���(��,�ָ�)Ϊһά�������� */
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
	/** �����ַ���(��,|�ָ�)Ϊ��ά�������� */
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
	/** �����ַ���(��,|,:�ָ�)Ϊ��ά�������� */
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
	/** �����ַ���Ϊ���� */
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
