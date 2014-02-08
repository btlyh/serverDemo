/** */
package com.cambrian.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * ��˵����
 * 
 * @version 2013-4-22
 * @author HYZ (huangyz1988@qq.com)
 */
public class TimeKit
{

	/** �ܡ��ա�ʱ���ֵ�λʱ�䣨�룩 */
	public static final int MIN=60,HOUR=60*MIN,DAY=24*HOUR,WEEK=7*DAY;

	/** �ܡ��ա����졢ʱ���֡��뵥λʱ�䣨���룩 */
	public static final long SEC_MILLS=1000L,MIN_MILLS=60*SEC_MILLS,
					HOUR_MILLS=60*MIN_MILLS,HALF_DAY_MILLS=12*HOUR_MILLS,
					DAY_MILLS=24*HOUR_MILLS,WEEK_MILLS=7*DAY_MILLS;

	/** Ĭ��ʱ���ʽ */
	public static final String FORMAT="yyyy/MM/dd HH:mm";

	/** ��õ�ǰʱ�䣨���룩 */
	public static long nowTimeMills()
	{
		return System.currentTimeMillis();
	}
	/** ��õ�ǰʱ�䣨�룩 */
	public static int nowTime()
	{
		return (int)(System.currentTimeMillis()/1000L);
	}
	/** ����ת��Ϊ�� */
	public static int timeSecond(long timeMillis)
	{
		return (int)(timeMillis/1000L);
	}
	/** ��ת��Ϊ���� */
	public static long timeMillis(long timeSecond)
	{
		return (timeSecond*1000L);
	}
	/**
	 * �õ���ǰʱ�����ָ���������賿ʱ��(��) (����Ϊ����������Ϊ��ȥ,0���ǵ���) ���磺-3 ��ʾ��ȥ��3�죬0 ��ʾ���죬3
	 * ��ʾ������3�죬
	 */
	public static int getDayTime(int dayNum)
	{
		Calendar c=Calendar.getInstance();
		int day=c.get(Calendar.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH,day+dayNum);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		int time=(int)(c.getTimeInMillis()/1000);
		return time;
	}

	/** �õ���ǰʱ�������һ���賿��ʱ�� */
	public static int fromNextDayTime()
	{
		return getDayTime(1)-(int)(System.currentTimeMillis()/1000);
	}

	/** ��ȡָ��ʱ�䵱���ѹ�ʱ�� */
	public static long dayPastTime(long time)
	{
		Calendar c=Calendar.getInstance();
		c.setTimeInMillis(time);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		return time-c.getTimeInMillis();
	}

	/** �ж�����ͬһ����(���������) */
	public static boolean isHalfDay(long time1,long time2)
	{
		if((time2-time1)>=HALF_DAY_MILLS||(time2-time1)<=-HALF_DAY_MILLS)
			return false;
		Calendar cal1=Calendar.getInstance();
		cal1.setTimeInMillis(time1);
		Calendar cal2=Calendar.getInstance();
		cal2.setTimeInMillis(time2);
		if(cal1.get(Calendar.YEAR)==cal2.get(Calendar.YEAR)
			&&cal1.get(Calendar.DAY_OF_YEAR)==cal2.get(Calendar.DAY_OF_YEAR)
			&&cal1.get(Calendar.AM_PM)==cal2.get(Calendar.AM_PM))
			return true;
		return false;
	}
	/** �ж��Ƿ�Ϊͬһ�� */
	public static boolean isOneDay(long time1,long time2)
	{
		Calendar cal1=Calendar.getInstance();
		cal1.setTimeInMillis(time1);
		Calendar cal2=Calendar.getInstance();
		cal2.setTimeInMillis(time2);
		if(cal1.get(Calendar.YEAR)==cal2.get(Calendar.YEAR)
			&&cal1.get(Calendar.DAY_OF_YEAR)==cal2.get(Calendar.DAY_OF_YEAR))
			return true;
		return false;
	}
	/** �ж��Ƿ�Ϊͬһ�� */
	public static boolean isOneWeek(long time1,long time2)
	{
		Calendar cal1=Calendar.getInstance();
		cal1.setTimeInMillis(time1);
		Calendar cal2=Calendar.getInstance();
		cal2.setTimeInMillis(time2);
		if(cal1.get(Calendar.YEAR)==cal2.get(Calendar.YEAR)
			&&cal1.get(Calendar.WEEK_OF_YEAR)==cal2
				.get(Calendar.WEEK_OF_YEAR)) return true;
		return false;
	}
	/** �ж��Ƿ�Ϊͬһ�� */
	public static boolean isOneMonth(long time1,long time2)
	{
		Calendar cal1=Calendar.getInstance();
		cal1.setTimeInMillis(time1);
		Calendar cal2=Calendar.getInstance();
		cal2.setTimeInMillis(time2);
		if(cal1.get(Calendar.YEAR)==cal2.get(Calendar.YEAR)
			&&cal1.get(Calendar.MONTH)==cal2.get(Calendar.MONTH))
			return true;
		return false;
	}
	/** ��ȡ����ָ��Сʱ��ʱ��ֵ�����룩 */
	public static long timeOf(int hour)
	{
		return timeOf(hour,0);
	}
	/** ��ȡ����ָ��Сʱ�ͷ��ӵ�ʱ��ֵ�����룩 */
	public static long timeOf(int hour,int minute)
	{
		return timeOf(hour,minute,0,0);
	}
	/** ��ȡ����ָ��ʱ��������ʱ��ֵ�����룩 */
	public static long timeOf(int hour,int min,int sec,int mill)
	{
		return timeOf(nowTimeMills(),hour,min,sec,mill);
	}
	/** ��ȡָ��ʱ�䵱��ָ��ʱ��������ʱ��ֵ�����룩 */
	public static long timeOf(long time,int hour,int min,int sec,int mill)
	{
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.setLenient(true);
		cal.set(Calendar.HOUR_OF_DAY,hour);
		cal.set(Calendar.MINUTE,min);
		cal.set(Calendar.SECOND,sec);
		cal.set(Calendar.MILLISECOND,mill);
		return cal.getTimeInMillis();
	}
	/** ��ȡ��ǰʱ��ָ��ʱ��ĺ���Ϊ��λ��ʱ��ֵ */
	public static long timeOf(int year,int month,int day,int hour,int min,
		int sec,int mill)
	{
		Calendar cal=Calendar.getInstance();
		cal.setLenient(true);
		cal.set(year,month-1,day,hour,min,sec);
		cal.set(Calendar.MILLISECOND,mill);
		return cal.getTimeInMillis();
	}

	/** ָ��ʱ��ת��Ϊ�ַ���������ʽ */
	public static String dateToString(long time,String format)
	{
		try
		{
			SimpleDateFormat sdf=null;
			if(format==null)
				sdf=new SimpleDateFormat(FORMAT);
			else
				sdf=new SimpleDateFormat(format);
			String res=sdf.format(time);
			return res;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/** ��ȡʱ�䵹��ʱ�ַ�����ʾ�����磺01:59:08�� */
	public static String getCountdown(int time)
	{
		int hour=time/HOUR;
		int min=(time%HOUR)/MIN;
		int sec=time%MIN;
		return getCountdown(hour,min,sec);
	}

	/** ��ȡʱ�䵹��ʱ�ַ�����ʾ�����磺01:59:08�� */
	public static String getCountdown(int hour,int min,int sec)
	{
		return (hour>=10?""+hour:"0"+hour)+(min>=10?":"+min:":0"+min)
			+(sec>=10?":"+sec:":0"+sec);
	}
	
	/** ��ȡָ��ʱ��������ڵ�ʱ��� */
	public static long getTimeFromAssgin(long time)
	{
		return nowTimeMills()-time;
	}


}
