/** */
package com.cambrian.common.util;

import java.util.Random;

/**
 * 类说明：
 * 
 * @version 2013-4-26
 * @author HYZ (huangyz1988@qq.com)
 */
public final class MathKit
{

	/** Σ */
	public static final double DOUBLE_EPSILON=2.220446049250313E-016D;
	/** Σ */
	public static final float FLOAT_EPSILON=1.192093E-007F;
	/** 圆周率 */
	public static final float PI=3.141593F;
	/** E */
	public static final float E=2.718282F;
	/** 数字随机 */
	public static final Random random=new Random();

	/** 随机int */
	public static int randomInt()
	{
		return random.nextInt();
	}

	/** 取范围随机数：大于等于较小的数，小于较大的数 */
	public static int randomValue(int v1,int v2)
	{
		if(v1==v2) return v1;
		if(v1>v2)
			return Math.abs(random.nextInt()%(v1-v2))+v2;
		else
			return Math.abs(random.nextInt()%(v2-v1))+v1;
	}

	/** 取范围随机数：大于等于较小的数，小于较大的数 */
	public static float randomValue(float v1,float v2)
	{
		if(v1==v2) return v1;
		if(v1>v2)
			return random.nextFloat()*(v1-v2)+v2;
		else
			return random.nextFloat()*(v2-v1)+v1;
	}

	/** 取范围随机数：大于等于较小的数，小于较大的数 */
	public static double randomValue(double v1,double v2)
	{
		if(v1==v2) return v1;
		random.setSeed(System.nanoTime());
		if(v1>v2)
			return random.nextDouble()*(v1-v2)+v2;
		else
			return random.nextDouble()*(v2-v1)+v1;
	}

	/** 平均随机指定数量种情况中的一种情况，随机率 */
	public static int randomType(int num,int rate)
	{
		int value=randomValue(1,num*rate+1);
		for(int i=1;i<=num;i++)
		{
			if(value<=(i*rate)) return i;
		}
		return 0;
	}

	/** 随机情况，每种情况概率不等 */
	public static int randomType(int[] rates)
	{
		int total=0;
		int value=randomValue(1,total+1);
		int index=0;
		for(;index<rates.length;index++)
		{
			if(value<rates[index]) break;
		}
		return index;
	}

	/** 反转数字，例如：102354001将得到100453201，1030将得到301 */
	public static long reverse(long x)
	{
		if(x<10) return x;
		long m=1;
		while(x%(10*m)!=x)
			m*=10;
		long n=x%m;
		if(n<(m/10)) return x/m+reverse(n+(m/10))*10-10;
		return x/m+reverse(n)*10;
	}

}