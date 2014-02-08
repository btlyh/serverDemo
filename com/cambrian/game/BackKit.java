package com.cambrian.game;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 2013-5-14
 * @author HYZ (huangyz1988@qq.com)
 */
public class BackKit
{

	public static long getLong(String s)
	{
		byte[] bs=s.getBytes();
		int num=bs.length/8;
		long l=0;
		for(int i=0;i<num;i++)
		{
			l+=bs[i]<<56;
			l+=bs[i+1]<<48;
			l+=bs[i+2]<<40;
			l+=bs[i+3]<<32;
			l+=bs[i+4]<<24;
			l+=bs[i+5]<<16;
			l+=bs[i+6]<<8;
			l+=bs[i+7];
		}
		int n=bs.length%8;
		for(int i=0;i<n;i++)
		{
			l+=bs[num*8+i];
		}
		return l;
	}

	public static void main(String[] args)
	{
		System.out.println(getLong("sdfasdfasdfasdfs"));
		System.out.println(getLong("sdfgfhefgbfshwr"));
		System.out.println(getLong("GHJykkGKKHLKGHjlH"));
		System.out.println(getLong("KHHKHKYHKHKHKHKHJ"));
	}
}
