package com.cambrian.common.util;

/**
 * 类说明：
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class PrintfKit
{

	/** 打印任意数量字符串 */
	public static void println(String...args)
	{
		for(int i=0;i<args.length;i++)
		{
			if(i==args.length-1)
				System.err.println(args[i]);
			else
				System.err.print(args[i]+",");
		}
	}
	/** 打印任意数量对象 */
	public static void println(Object...args)
	{
		for(int i=0;i<args.length;i++)
		{
			if(i==args.length-1)
				System.err.println(args[i]);
			else
				System.err.print(args[i]+",");
		}
	}
}
