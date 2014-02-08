package com.cambrian.common.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;


/**
 * 类说明：后台功能函数库
 * 
 * @version 1.1
 * @author
 */
public class BackKit
{

	/* static fields */
	/** 1小时的毫秒数 */
	public static int HOUR=60*60*1000;
	/** 1天的毫秒数 */
	public static int DAY=24*HOUR;
	/** 3天的毫秒数 */
	public static int THREEDAY=3*DAY;
	/** 一周的毫秒数 */
	public static int WEEK=7*DAY;
	/** 半天的毫秒数 */
	public static final int HALF_DAY=12*HOUR;

	/* static methods */

	/* fields */

	/* constructors */

	/* properties */

	/* init start */

	/* methods */
	/** 翻转数组元素的顺序 */
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
	/** 判断是在同一半天(上午或下午) */
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
	/** 判断是否为昨天 */
	public static boolean isYesterday(long time)
	{
		return isToday(time+DAY);
	}
	/** 判断是否为今天之前 */
	public static boolean isBeforeToday(long time)
	{
		long cur=System.currentTimeMillis();
		if(time>cur) return false;
		return (!isToday(time));
	}
	/** 判断是否为今天 */
	public static boolean isToday(long time)
	{
		return isOneDay(time,System.currentTimeMillis());
	}
	/** 判断是否为同一天 */
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
	/** 判断是否为同一周 */
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
	/** 判断是否为同一月 */
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
	/** 获取当前时区指定时间的毫秒为单位的时间值 */
	public static long getTimeInMillis(int year,int month,int day,int hour,
		int minute,int second,int millisecond)
	{
		Calendar cal=Calendar.getInstance();
		cal.setLenient(true);
		cal.set(year,month-1,day,hour,minute,second);
		cal.set(Calendar.MILLISECOND,millisecond);
		return cal.getTimeInMillis();
	}
	/** 获取指定时间的秒为单位的时间值 */
	public static int getTimeInSecond(String yyyymmdd)
	{
		int time=TextKit.parseInt(yyyymmdd);
		Calendar cal=Calendar.getInstance();
		cal.setLenient(true);
		cal.set(time/10000,time%10000/100-1,time%100,0,0,0);
		return (int)(cal.getTimeInMillis()/1000);
	}
	/** 获取指定时间的毫秒为单位的时间值 */
	public static long getTimeInMillis(String yyyymmdd)
	{
		int time=TextKit.parseInt(yyyymmdd);
		if(time==0) return 0;
		Calendar cal=Calendar.getInstance();
		cal.setLenient(true);
		cal.set(time/10000,time%10000/100-1,time%100,0,0,0);
		return cal.getTimeInMillis();
	}
	/** 获取指定时间的毫秒为单位的时间值 */
	public static long getTimeInMillis(int yyyymmdd)
	{
		if(yyyymmdd<=0) return 0;
		Calendar cal=Calendar.getInstance();
		cal.setLenient(true);
		cal.set(yyyymmdd/10000,yyyymmdd%10000/100-1,yyyymmdd%100,0,0,0);
		return cal.getTimeInMillis();
	}
	/** 获取当天指定小时的毫秒为单位的时间值 */
	public static long getDayTimeInMillis(int hour)
	{
		return getDayTimeInMillis(hour,0,0,0);
	}
	/** 获取当天指定小时分钟的毫秒为单位的时间值 */
	public static long getDayTimeInMillis(int hour,int minute)
	{
		return getDayTimeInMillis(hour,minute,0,0);
	}
	/** 获取当天指定时间的毫秒为单位的时间值 */
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
	/** 获取指定时间所在天指定小时的毫秒为单位的时间值 */
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
	/** 获取指定时间所在天指定时间的毫秒为单位的时间值 */
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
	/** 根据秒数获取YYYYMMDD格式的时间 */
	public static String getTimeBySecond(int second)
	{
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(second*1000L);
		return ""+cal.get(Calendar.YEAR)
			+((cal.get(Calendar.MONDAY)+1>9)?"":"0")
			+(cal.get(Calendar.MONDAY)+1)
			+((cal.get(Calendar.DATE)>9)?"":"0")+cal.get(Calendar.DATE);
	}
	/** 获取当前距离明天的毫秒数 */
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

	/** 取随机数(>=v1,<v2) */
	public static int randomValue(int v1,int v2)
	{
		if(v1==v2) return v1;
		if(v1>v2)
			return (int)((v1-v2)*Math.random())+v2;
		else
			return v1+(int)((v2-v1)*Math.random());
	}
	/** 取随机数(>=v1,<v2) */
	public static float randomValue(float v1,float v2)
	{
		if(v1==v2) return v1;
		if(v1>v2)
			return (float)((v1-v2)*Math.random())+v2;
		else
			return v1+(float)((v2-v1)*Math.random());
	}

	/** 根据玩家uid和游戏数据库id得到唯一id */
	public static long getLongUid(int uid,int dbid)
	{
		long luid=uid;
		luid|=(long)dbid<<32;
		return luid;
	}
	/** 获得玩家所在游戏数据库id */
	public static int getDBId(long luid)
	{
		return (int)(luid>>32);
	}
	/** 获得玩家uid */
	public static int getUId(long luid)
	{
		return (int)luid;
	}

	/** 合服重置玩家名称 */
	public static String resetName(long userid,String name)
	{
		return "["+BackKit.getDBId(userid)+"]"+name;
	}
	/** 还原玩家名称 */
	public static String getTrueName(String name)
	{
		if(name.indexOf("[")>=0)
		{
			return name.split("]")[1];
		}
		return null;
	}
	/** 打乱顺序 */
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

	/** 检查截取后的字符串数组 */
	public static boolean checkSplitStrs(String[] strs)
	{
		if(strs==null) return false;
		if(Arrays.equals(strs,TextKit.EMPTY_STRING_ARRAY)) return false;
		if(Arrays.equals(strs,TextKit.NULL_STRING_ARRAY)) return false;
		return true;
	}
	/** 打乱顺序 */
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
	 * 将一个字符串按照指定正则表达式数组regexs顺序拆分为数组，结果数组维度由参数regexs长度决定，拆分顺序也由regexs元素顺序决定。<br>
	 * 例如：<br>
	 * 字符串"1,2:3,4|5,6:7,8" 拆分时传入regexs为拆分正则表达式数组 {"\\|",":",","}，
	 * 获得的数组是一个三维数组为{{{1,2},{3,4}},{{5,6},{7,8}}}<br>
	 * <br>
	 * 参数： <li>str：拆分源字符串<br> <li>regexs: 拆分正则表达式数组<br><li>
	 */
	public static Object split(String str,String[] regexs)
	{
		return split(str,regexs,String.class);
	}

	/**
	 * 将一个字符串按照指定char拆分为int数组<br>
	 * 例如：<br>
	 * 字符串"1|2|3|4" 拆分时传入'|'为拆分char 获得的数组为{1,2,3,4}<br>
	 * <br>
	 * 参数： <li>str：拆分源字符串<br> <li>chr: 拆分字符<br><li>
	 */
	public static Object splitToInt(String str,String[] regexs)
	{
		return split(str,regexs,Integer.TYPE);
	}

	/**
	 * 将一个字符串按照指定字符顺序拆分为数组，维度由参数regexs长度决定，拆分顺序也由regexs元素顺序决定。<br>
	 * 例如：<br>
	 * 字符串"1,2:3,4|5,6:7,8" 拆分时传入regexs为拆分正则表达式数组 {"\\|",":",","}，
	 * 获得的数组是一个三维数组为{{{1,2},{3,4}},{{5,6},{7,8}}}<br>
	 * <br>
	 * 参数： <li>str：拆分源字符串<br> <li>regexs: 拆分正则表达式数组<br><li>
	 * type：标识是否拆分为什么类型的数组<br>
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
	/** 基本类型值转换 */
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

	// /** 写入一个map到data中 ,待验证 */
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
	// /** 从一个ByteBuffer中读出一个Map,待验证 */
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
	// /** 合并int数组 */
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
	// * 将一个int数组作为一个map结构( 0为键 1为值 2为键 3为值 ...) 该方法将一个int键值插入该int表中
	// * 返回最后的map结构的int[]数组 当有需要将int[]做为一个map结构存储时使用
	// */
	// public static int[] insertIntMap(int[] map,int key,int value)
	// {
	// if(map==null) return new int[]{key,value};
	// if(map.length%2!=0)
	// {
	// throw new IllegalArgumentException("map length err");
	// }
	// // 插到已有位置
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
	// * 获得int map中的键值
	// *
	// * @param cid 指定的怪物样式
	// * @return 返回个数 如果为-1表示没有该键
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
	// /** 将一个日期字符串格式化为中文表达形式 */
	// public static String formatDate(String taskDate)
	// {
	// return taskDate;
	// }
	//
	// public static SimpleDateFormat fullDateFormat=new SimpleDateFormat(
	// "yyyy'/'M'/'dd'/'HH'_'mm'/'ss");
	//
	// /** 获取格式化日期时间 */
	// public static String getFormatTime(long time)
	// {
	// Date date=new Date(time);
	// return (fullDateFormat.format(date));
	// }

	/* common methods */

	/* inner class */

}