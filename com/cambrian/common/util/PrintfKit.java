package com.cambrian.common.util;

/**
 * ��˵����
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class PrintfKit
{

	/** ��ӡ���������ַ��� */
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
	/** ��ӡ������������ */
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
