package com.cambrian.common.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;


/**
 * ��˵������̨���ܺ�����
 * 
 * @version 1.1
 * @author
 */
public class BackKit
{

	/* static fields */
	/** 1Сʱ�ĺ����� */
	public static int HOUR=60*60*1000;
	/** 1��ĺ����� */
	public static int DAY=24*HOUR;
	/** 3��ĺ����� */
	public static int THREEDAY=3*DAY;
	/** һ�ܵĺ����� */
	public static int WEEK=7*DAY;
	/** ����ĺ����� */
	public static final int HALF_DAY=12*HOUR;

	/* static methods */

	/* fields */

	/* constructors */

	/* properties */

	/* init start */

	/* methods */
	/** ��ת����Ԫ�ص�˳�� */
	public static void reverse(Object[] array)
	{
		int len=array.length;
		for(int i=0;i<len/2;i++)
		{
			Object temp=array[i];
			array[i]=array[len-1-i];
			array[len-1-i]=temp;
		}
	}
	/** �ж�����ͬһ����(���������) */
	public static boolean isHalfDay(long time1,long time2)
	{
		if((time2-time1)>=HALF_DAY||(time2-time1)<=-HALF_DAY) return false;
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
	/** �ж��Ƿ�Ϊ���� */
	public static boolean isYesterday(long time)
	{
		return isToday(time+DAY);
	}
	/** �ж��Ƿ�Ϊ����֮ǰ */
	public static boolean isBeforeToday(long time)
	{
		long cur=System.currentTimeMillis();
		if(time>cur) return false;
		return (!isToday(time));
	}
	/** �ж��Ƿ�Ϊ���� */
	public static boolean isToday(long time)
	{
		return isOneDay(time,System.currentTimeMillis());
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
	/** ��ȡ��ǰʱ��ָ��ʱ��ĺ���Ϊ��λ��ʱ��ֵ */
	public static long getTimeInMillis(int year,int month,int day,int hour,
		int minute,int second,int millisecond)
	{
		Calendar cal=Calendar.getInstance();
		cal.setLenient(true);
		cal.set(year,month-1,day,hour,minute,second);
		cal.set(Calendar.MILLISECOND,millisecond);
		return cal.getTimeInMillis();
	}
	/** ��ȡָ��ʱ�����Ϊ��λ��ʱ��ֵ */
	public static int getTimeInSecond(String yyyymmdd)
	{
		int time=TextKit.parseInt(yyyymmdd);
		Calendar cal=Calendar.getInstance();
		cal.setLenient(true);
		cal.set(time/10000,time%10000/100-1,time%100,0,0,0);
		return (int)(cal.getTimeInMillis()/1000);
	}
	/** ��ȡָ��ʱ��ĺ���Ϊ��λ��ʱ��ֵ */
	public static long getTimeInMillis(String yyyymmdd)
	{
		int time=TextKit.parseInt(yyyymmdd);
		if(time==0) return 0;
		Calendar cal=Calendar.getInstance();
		cal.setLenient(true);
		cal.set(time/10000,time%10000/100-1,time%100,0,0,0);
		return cal.getTimeInMillis();
	}
	/** ��ȡָ��ʱ��ĺ���Ϊ��λ��ʱ��ֵ */
	public static long getTimeInMillis(int yyyymmdd)
	{
		if(yyyymmdd<=0) return 0;
		Calendar cal=Calendar.getInstance();
		cal.setLenient(true);
		cal.set(yyyymmdd/10000,yyyymmdd%10000/100-1,yyyymmdd%100,0,0,0);
		return cal.getTimeInMillis();
	}
	/** ��ȡ����ָ��Сʱ�ĺ���Ϊ��λ��ʱ��ֵ */
	public static long getDayTimeInMillis(int hour)
	{
		return getDayTimeInMillis(hour,0,0,0);
	}
	/** ��ȡ����ָ��Сʱ���ӵĺ���Ϊ��λ��ʱ��ֵ */
	public static long getDayTimeInMillis(int hour,int minute)
	{
		return getDayTimeInMillis(hour,minute,0,0);
	}
	/** ��ȡ����ָ��ʱ��ĺ���Ϊ��λ��ʱ��ֵ */
	public static long getDayTimeInMillis(int hour,int minute,int second,
		int millisecond)
	{
		Calendar cal=Calendar.getInstance();
		cal.setLenient(true);
		cal.set(Calendar.HOUR_OF_DAY,hour);
		cal.set(Calendar.MINUTE,minute);
		cal.set(Calendar.SECOND,second);
		cal.set(Calendar.MILLISECOND,millisecond);
		return cal.getTimeInMillis();
	}
	/** ��ȡָ��ʱ��������ָ��Сʱ�ĺ���Ϊ��λ��ʱ��ֵ */
	public static long getDayTimeInMillis(long time,int hour)
	{
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.setLenient(true);
		cal.set(Calendar.HOUR_OF_DAY,hour);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		return cal.getTimeInMillis();
	}
	/** ��ȡָ��ʱ��������ָ��ʱ��ĺ���Ϊ��λ��ʱ��ֵ */
	public static long getDayTimeInMillis(long time,int hour,int minute,
		int second,int millisecond)
	{
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.setLenient(true);
		cal.set(Calendar.HOUR_OF_DAY,hour);
		cal.set(Calendar.MINUTE,minute);
		cal.set(Calendar.SECOND,second);
		cal.set(Calendar.MILLISECOND,millisecond);
		return cal.getTimeInMillis();
	}
	/** ����������ȡYYYYMMDD��ʽ��ʱ�� */
	public static String getTimeBySecond(int second)
	{
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(second*1000L);
		return ""+cal.get(Calendar.YEAR)
			+((cal.get(Calendar.MONDAY)+1>9)?"":"0")
			+(cal.get(Calendar.MONDAY)+1)
			+((cal.get(Calendar.DATE)>9)?"":"0")+cal.get(Calendar.DATE);
	}
	/** ��ȡ��ǰ��������ĺ����� */
	public static int getDayRemainTime()
	{
		long time=System.currentTimeMillis();
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		return (int)(cal.getTimeInMillis()+DAY-time);
	}

	/** ȡ�����(>=v1,<v2) */
	public static int randomValue(int v1,int v2)
	{
		if(v1==v2) return v1;
		if(v1>v2)
			return (int)((v1-v2)*Math.random())+v2;
		else
			return v1+(int)((v2-v1)*Math.random());
	}
	/** ȡ�����(>=v1,<v2) */
	public static float randomValue(float v1,float v2)
	{
		if(v1==v2) return v1;
		if(v1>v2)
			return (float)((v1-v2)*Math.random())+v2;
		else
			return v1+(float)((v2-v1)*Math.random());
	}

	/** �������uid����Ϸ���ݿ�id�õ�Ψһid */
	public static long getLongUid(int uid,int dbid)
	{
		long luid=uid;
		luid|=(long)dbid<<32;
		return luid;
	}
	/** ������������Ϸ���ݿ�id */
	public static int getDBId(long luid)
	{
		return (int)(luid>>32);
	}
	/** ������uid */
	public static int getUId(long luid)
	{
		return (int)luid;
	}

	/** �Ϸ������������ */
	public static String resetName(long userid,String name)
	{
		return "["+BackKit.getDBId(userid)+"]"+name;
	}
	/** ��ԭ������� */
	public static String getTrueName(String name)
	{
		if(name.indexOf("[")>=0)
		{
			return name.split("]")[1];
		}
		return null;
	}
	/** ����˳�� */
	public static void randomOrder(int[] vals)
	{
		int random;
		int value;
		for(int i=0;i<vals.length;i++)
		{
			random=BackKit.randomValue(0,vals.length);
			value=vals[i];
			vals[i]=vals[random];
			vals[random]=value;
		}
	}

	/** ����ȡ����ַ������� */
	public static boolean checkSplitStrs(String[] strs)
	{
		if(strs==null) return false;
		if(Arrays.equals(strs,TextKit.EMPTY_STRING_ARRAY)) return false;
		if(Arrays.equals(strs,TextKit.NULL_STRING_ARRAY)) return false;
		return true;
	}
	/** ����˳�� */
	public static void randomOrder(Object[] objects)
	{
		int random;
		Object obj;
		for(int i=0;i<objects.length;i++)
		{
			random=BackKit.randomValue(0,objects.length);
			obj=objects[i];
			objects[i]=objects[random];
			objects[random]=obj;
		}
	}
	/**
	 * ��һ���ַ�������ָ��������ʽ����regexs˳����Ϊ���飬�������ά���ɲ���regexs���Ⱦ��������˳��Ҳ��regexsԪ��˳�������<br>
	 * ���磺<br>
	 * �ַ���"1,2:3,4|5,6:7,8" ���ʱ����regexsΪ���������ʽ���� {"\\|",":",","}��
	 * ��õ�������һ����ά����Ϊ{{{1,2},{3,4}},{{5,6},{7,8}}}<br>
	 * <br>
	 * ������ <li>str�����Դ�ַ���<br> <li>regexs: ���������ʽ����<br><li>
	 */
	public static Object split(String str,String[] regexs)
	{
		return split(str,regexs,String.class);
	}

	/**
	 * ��һ���ַ�������ָ��char���Ϊint����<br>
	 * ���磺<br>
	 * �ַ���"1|2|3|4" ���ʱ����'|'Ϊ���char ��õ�����Ϊ{1,2,3,4}<br>
	 * <br>
	 * ������ <li>str�����Դ�ַ���<br> <li>chr: ����ַ�<br><li>
	 */
	public static Object splitToInt(String str,String[] regexs)
	{
		return split(str,regexs,Integer.TYPE);
	}

	/**
	 * ��һ���ַ�������ָ���ַ�˳����Ϊ���飬ά���ɲ���regexs���Ⱦ��������˳��Ҳ��regexsԪ��˳�������<br>
	 * ���磺<br>
	 * �ַ���"1,2:3,4|5,6:7,8" ���ʱ����regexsΪ���������ʽ���� {"\\|",":",","}��
	 * ��õ�������һ����ά����Ϊ{{{1,2},{3,4}},{{5,6},{7,8}}}<br>
	 * <br>
	 * ������ <li>str�����Դ�ַ���<br> <li>regexs: ���������ʽ����<br><li>
	 * type����ʶ�Ƿ���Ϊʲô���͵�����<br>
	 */
	public static Object split(String str,String[] regexs,Class<?> type)
	{
		if(regexs==null||regexs.length==0)
		{
			if(type==String.class) return str;
			return getPrimitive(type,str);
		}
		String[] strs=str.split(regexs[0]);
		int[] dimensions=new int[regexs.length];
		dimensions[0]=strs.length;
		Object array=Array.newInstance(type,dimensions);
		String[] regexs_=new String[regexs.length-1];
		System.arraycopy(regexs,1,regexs_,0,regexs_.length);
		for(int i=0;i<strs.length;i++)
			Array.set(array,i,split(strs[i],regexs_,type));
		return array;
	}
	/** ��������ֵת�� */
	public static Object getPrimitive(Class<?> type,String value)
	{
		if(type==Character.TYPE) return value.charAt(0);
		if(type==Byte.TYPE) return (byte)TextKit.parseInt(value);
		if(type==Short.TYPE) return (short)TextKit.parseInt(value);
		if(type==Integer.TYPE) return TextKit.parseInt(value);
		if(type==Long.TYPE) return TextKit.parseLong(value);
		if(type==Float.TYPE) return Float.parseFloat(value);
		if(type==Double.TYPE) return Double.parseDouble(value);
		if(type==Boolean.TYPE) return TextKit.parseBoolean(value);
		return null;
	}

	// /** д��һ��map��data�� ,����֤ */
	// public static ByteBuffer writeMap(ByteBuffer data,Map<?,?> map)
	// {
	// if(data==null||map==null) return null;
	// int len=0;
	// int offset=data.length();
	// data.writeShort(len);
	// Object[] arry=map.entrySet().toArray();
	// String[] entry=null;
	// for(int i=0;i<arry.length;i++)
	// {
	// if(arry[i]==null) continue;
	// entry=TextKit.split(arry[i].toString(),"=");
	// if(entry==null||entry.length<2) continue;
	// if(entry[0]!=null&&entry[1]!=null)
	// {
	// len++;
	// data.writeUTF(entry[0]);
	// data.writeUTF(entry[1]);
	// }
	// }
	// ByteKit.writeShort((short)len,data.getArray(),(short)offset);
	// return data;
	// }
	// /** ��һ��ByteBuffer�ж���һ��Map,����֤ */
	// public static HashMap<String,String> readMap(ByteBuffer data)
	// {
	// HashMap<String,String> map=new HashMap<String,String>();
	// int len=data.readUnsignedShort();
	// for(int i=0;i<len;i++)
	// {
	// String A=data.readUTF();
	// String B=data.readUTF();
	// map.put(A,B);
	// }
	// return map;
	// }
	// /** �ϲ�int���� */
	// public static int[] mixIntArray(int[] a,int[] b)
	// {
	// if(a==null||b==null) return null;
	// int[] all=new int[a.length+b.length];
	// for(int i=0;i<all.length;i++)
	// {
	// if(i<a.length)
	// {
	// all[i]=a[i];
	// continue;
	// }
	// all[i]=b[i-a.length];
	// }
	// return all;
	// }
	// /**
	// * ��һ��int������Ϊһ��map�ṹ( 0Ϊ�� 1Ϊֵ 2Ϊ�� 3Ϊֵ ...) �÷�����һ��int��ֵ�����int����
	// * ��������map�ṹ��int[]���� ������Ҫ��int[]��Ϊһ��map�ṹ�洢ʱʹ��
	// */
	// public static int[] insertIntMap(int[] map,int key,int value)
	// {
	// if(map==null) return new int[]{key,value};
	// if(map.length%2!=0)
	// {
	// throw new IllegalArgumentException("map length err");
	// }
	// // �嵽����λ��
	// for(int i=0,n=map.length;i<n;i+=2)
	// {
	// if(map[i]==key)
	// {
	// map[i+1]+=value;
	// return map;
	// }
	// }
	// int[] newMap=BackKit.mixIntArray(map,new int[]{key,value});
	// return newMap;
	// }
	// /**
	// * ���int map�еļ�ֵ
	// *
	// * @param cid ָ���Ĺ�����ʽ
	// * @return ���ظ��� ���Ϊ-1��ʾû�иü�
	// */
	// public static int getIntMapValue(int[] map,int key)
	// {
	// if(map==null) return 0;
	// for(int i=0;i<map.length;i+=2)
	// {
	// if(map[i]==key)
	// {
	// return map[i+1];
	// }
	// }
	// return -1;
	// }
	// /** ��һ�������ַ�����ʽ��Ϊ���ı����ʽ */
	// public static String formatDate(String taskDate)
	// {
	// return taskDate;
	// }
	//
	// public static SimpleDateFormat fullDateFormat=new SimpleDateFormat(
	// "yyyy'/'M'/'dd'/'HH'_'mm'/'ss");
	//
	// /** ��ȡ��ʽ������ʱ�� */
	// public static String getFormatTime(long time)
	// {
	// Date date=new Date(time);
	// return (fullDateFormat.format(date));
	// }

	/* common methods */

	/* inner class */

}