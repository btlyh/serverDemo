package com.cambrian.common.util;

import java.lang.reflect.Array;

import com.cambrian.common.net.CharBuffer;

/***
 * 类说明：文本处理工具
 * 
 * @version 2013-4-19
 * @author HYZ (huangyz1988@qq.com)
 */
public final class TextKit
{

	public static final int TYPE_LOWER=0x0;
	public static final int TYPE_UPPER=0x1;
	public static final String toString=TextKit.class.getName();
	public static final int LEN_LIMIT=80;
	public static final String EMPTY_STRING="";
	public static final String[] EMPTY_STRING_ARRAY=new String[0];
	public static final String[] NULL_STRING_ARRAY={""};
	public static final char FIRST_ASCII=32;
	public static final char LAST_ASCII=126;
	public static final char FIRST_NUMBER=48;
	public static final char LAST_NUMBER=57;
	public static final char FIRST_UPPER_ENGLISH=65;
	public static final char LAST_UPPER_ENGLISH=90;
	public static final char FIRST_LOWER_ENGLISH=97;
	public static final char LAST_LOWER_ENGLISH=122;
	public static final char FIRST_CHINESE=19968;
	public static final char LAST_CHINESE=40959;
	public static final char[] NUMBER={'0','9'};
	public static final char[] UPPER_ENGLISH={'A','Z'};
	public static final char[] LOWER_ENGLISH={'a','z'};
	public static final char[] ENGLISH={'A','Z','a','z'};
	public static final char[] ASCII={' ','~'};
	public static final char[] CHINESE={19968,40959};
	public static final char[] DIGITS={'零','一','二','三','四','五','六','七','八',
		'九'};
	public static final char[] DIGIT_BITS={' ','十','百','千','万','亿'};
	public static final char[] UPPER_DIGITS={'零','壹','贰','叁','肆','伍','陆',
		'柒','捌','玖'};
	public static final char[] UPPER_DIGIT_BITS={' ','拾','佰','扦','萬','亿'};
	public static final char[] ALL_LOWER_ENGLISH={'a','b','c','d','e','f',
		'g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w',
		'x','y','z'};
	public static final char[] ALL_UPPER_ENGLISH={'A','B','C','D','E','F',
		'G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W',
		'X','Y','Z'};

	/** 将指定字符串转换为整型数字 */
	public static int parseInt(String paramString)
	{
		return (int)parseLong(paramString);
	}

	/** 将指定字符串转换为长整型 */
	public static long parseLong(String paramString)
	{
		if((paramString==null)||(paramString.length()==0)) return 0L;
		if(paramString.charAt(0)=='#')
			return Long.parseLong(paramString.substring(1),16);
		if((paramString.length()>1)&&(paramString.charAt(0)=='0')
			&&(paramString.charAt(1)=='x'))
			return Long.parseLong(paramString.substring(2),16);
		return Long.parseLong(paramString);
	}

	/** 将指定字符串数组转换为单精度浮点数 */
	public static float parseFloat(String paramString)
	{
		if((paramString==null)||(paramString.length()==0)) return 0.0F;
		if(paramString.charAt(0)=='#')
			return (float)Long.parseLong(paramString.substring(1),16);
		if((paramString.length()>1)&&(paramString.charAt(0)=='0')
			&&(paramString.charAt(1)=='x'))
			return (float)Long.parseLong(paramString.substring(2),16);
		return Float.parseFloat(paramString);
	}

	/** 将字符串转换为双精度浮点数 */
	public static double parseDouble(String paramString)
	{
		if((paramString==null)||(paramString.length()==0)) return 0.0D;
		if(paramString.charAt(0)=='#')
			return Long.parseLong(paramString.substring(1),16);
		if((paramString.length()>1)&&(paramString.charAt(0)=='0')
			&&(paramString.charAt(1)=='x'))
			return Long.parseLong(paramString.substring(2),16);
		return Double.parseDouble(paramString);
	}

	/** 将指定字符串数组转换为int数组 */
	public static int[] parseIntArray(String[] paramArrayOfString)
	{
		if(paramArrayOfString==null) return null;
		int[] arrayOfInt=new int[paramArrayOfString.length];
		for(int i=0;i<paramArrayOfString.length;i++)
			arrayOfInt[i]=parseInt(paramArrayOfString[i]);
		return arrayOfInt;
	}

	/** 将字符串转数组换为单精度浮点数数组 */
	public static float[] parseFloatArray(String[] paramArrayOfString)
	{
		if(paramArrayOfString==null) return null;
		float[] arrayOfFloat=new float[paramArrayOfString.length];
		for(int i=0;i<paramArrayOfString.length;i++)
			arrayOfFloat[i]=parseFloat(paramArrayOfString[i]);
		return arrayOfFloat;
	}

	/** 将指定字符串转换为布尔值 */
	public static boolean parseBoolean(String paramString)
	{
		if((paramString==null)||(paramString.length()==0)) return false;
		if(paramString.length()==1) return paramString.charAt(0)=='1';
		return paramString.equalsIgnoreCase("true");
	}

	/** 将指定字符串以指定字符拆分为数组 */
	public static String[] split(String src,char ch)
	{
		if(src==null) return EMPTY_STRING_ARRAY;
		if(src.length()==0) return NULL_STRING_ARRAY;
		int i=0;
		int j=0;
		int k=1;
		while((j=src.indexOf(ch,i))>=0)
		{
			i=j+1;
			k++;
		}
		String[] arrayOfString=new String[k];
		if(k==1)
		{
			arrayOfString[0]=src;
			return arrayOfString;
		}
		i=j=k=0;
		while((j=src.indexOf(ch,i))>=0)
		{
			arrayOfString[(k++)]=(i==j?"":src.substring(i,j));
			i=j+1;
		}
		arrayOfString[k]=(i>=src.length()?"":src.substring(i));
		return arrayOfString;
	}

	public static String[] split(String paramString1,String paramString2)
	{
		if(paramString1==null) return EMPTY_STRING_ARRAY;
		if(paramString1.length()==0) return NULL_STRING_ARRAY;
		int i=paramString2!=null?paramString2.length():0;
		if(i==0)
		{
			String[] arrayOfString1={paramString1};
			return arrayOfString1;
		}
		int j=0;
		int k=0;
		int m=1;
		while((k=paramString1.indexOf(paramString2,j))>=0)
		{
			j=k+i;
			m++;
		}
		String[] arrayOfString2=new String[m];
		if(m==1)
		{
			arrayOfString2[0]=paramString1;
			return arrayOfString2;
		}
		j=k=m=0;
		while((k=paramString1.indexOf(paramString2,j))>=0)
		{
			arrayOfString2[(m++)]=(j==k?"":paramString1.substring(j,k));
			j=k+i;
		}
		arrayOfString2[m]=(j>=paramString1.length()?"":paramString1
			.substring(j));
		return arrayOfString2;
	}

	public static String[] splitLine(String paramString)
	{
		String[] arrayOfString=split(paramString,'\n');

		for(int k=0;k<arrayOfString.length;k++)
		{
			int i=0;
			int j=arrayOfString[k].length();
			if(j!=0)
			{
				if(arrayOfString[k].charAt(0)=='\r') i++;
				if(arrayOfString[k].charAt(j-1)=='\r') j--;
				if(i>=j)
					arrayOfString[k]="";
				else if(j-i<arrayOfString[k].length())
					arrayOfString[k]=arrayOfString[k].substring(i,j);
			}
		}
		return arrayOfString;
	}

	public static String replace(String paramString1,String paramString2,
		int paramInt1,int paramInt2)
	{
		int i=paramString1.length()+paramString2.length()-paramInt2;
		CharBuffer localCharBuffer=new CharBuffer(i);
		localCharBuffer.append(paramString1.substring(0,paramInt1)).append(
			paramString2);
		localCharBuffer.append(paramString1.substring(paramInt1+paramInt2));
		return localCharBuffer.getString();
	}

	public static String replace(String paramString1,String paramString2,
		int paramInt1,int paramInt2,CharBuffer paramCharBuffer)
	{
		int i=paramString1.length()+paramString2.length()-paramInt2;
		paramCharBuffer.clear();
		paramCharBuffer.setCapacity(i);
		paramCharBuffer.append(paramString1.substring(0,paramInt1)).append(
			paramString2);
		paramCharBuffer.append(paramString1.substring(paramInt1+paramInt2));
		return paramCharBuffer.getString();
	}

	public static String replace(String paramString1,String paramString2,
		String paramString3)
	{
		return replace(paramString1,paramString2,paramString3,false,null);
	}

	public static String replace(String paramString1,String paramString2,
		String paramString3,boolean paramBoolean,CharBuffer paramCharBuffer)
	{
		String str1=paramString1;
		String str2=paramString2;
		if(paramBoolean)
		{
			str1=paramString1.toLowerCase();
			str2=paramString2.toLowerCase();
		}
		int i=str1.indexOf(str2);
		if(i<0) return paramString1;
		if(paramCharBuffer!=null)
		{
			paramCharBuffer.clear();
			paramCharBuffer.setCapacity(paramString1.length()
				+paramString3.length()-paramString2.length());
		}
		else
		{
			paramCharBuffer=new CharBuffer(paramString1.length()
				+paramString3.length()-paramString2.length());
		}
		return replace(paramString1,paramString3,i,paramString2.length(),
			paramCharBuffer);
	}

	public static String replaceAll(String paramString1,String paramString2,
		String paramString3)
	{
		return replaceAll(paramString1,paramString2,paramString3,false,null);
	}

	public static String replaceAll(String paramString1,String paramString2,
		String paramString3,boolean paramBoolean)
	{
		return replaceAll(paramString1,paramString2,paramString3,
			paramBoolean,null);
	}

	public static String replaceAll(String paramString1,String paramString2,
		String paramString3,boolean paramBoolean,CharBuffer paramCharBuffer)
	{
		int i=paramString2.length();
		if(i==0) return paramString1;
		String str1=paramString1;
		String str2=paramString2;
		if(paramBoolean)
		{
			str1=paramString1.toLowerCase();
			str2=paramString2.toLowerCase();
		}
		int j=0;
		int k=str1.indexOf(str2,j);
		if(k<0) return paramString1;
		if(paramCharBuffer!=null)
		{
			paramCharBuffer.clear();
			paramCharBuffer.setCapacity(paramString1.length());
		}
		else
		{
			paramCharBuffer=new CharBuffer(paramString1.length());
		}
		while(k>=0)
		{
			paramCharBuffer.append(paramString1.substring(j,k)).append(
				paramString3);
			j=k+i;
			k=str1.indexOf(str2,j);
		}
		paramCharBuffer.append(paramString1.substring(j));
		return paramCharBuffer.getString();
	}

	public static String getBaseString(Object paramObject)
	{
		CharBuffer localCharBuffer=new CharBuffer();
		getBaseString(paramObject,localCharBuffer);
		return localCharBuffer.getString();
	}

	public static void getBaseString(Object paramObject,
		CharBuffer paramCharBuffer)
	{
		paramCharBuffer.append(paramObject.getClass().getName()).append('@')
			.append(paramObject.hashCode());
	}

	public static String toString(boolean[] paramArrayOfBoolean)
	{
		if(paramArrayOfBoolean==null) return "boolean[]null";
		CharBuffer localCharBuffer=new CharBuffer(
			paramArrayOfBoolean.length*2+2);
		localCharBuffer.append('{');
		toString(paramArrayOfBoolean,0,paramArrayOfBoolean.length,",",
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(boolean[] paramArrayOfBoolean,
		int paramInt1,int paramInt2)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2*2+2);
		localCharBuffer.append('{');
		toString(paramArrayOfBoolean,paramInt1,paramInt2,",",localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(boolean[] paramArrayOfBoolean,
		int paramInt1,int paramInt2,String paramString)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2
			*(paramString.length()+1)+2);
		localCharBuffer.append('{');
		toString(paramArrayOfBoolean,paramInt1,paramInt2,paramString,
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static void toString(boolean[] paramArrayOfBoolean,int paramInt1,
		int paramInt2,String paramString,CharBuffer paramCharBuffer)
	{
		int i=paramInt1+paramInt2-1;
		for(int j=paramInt1;j<i;j++)
			paramCharBuffer.append((paramArrayOfBoolean[j]!=false)?'1':'0')
				.append(paramString);
		if(i>=0) paramCharBuffer.append(paramArrayOfBoolean[i]);
	}

	public static String toString(byte[] paramArrayOfByte)
	{
		if(paramArrayOfByte==null) return "byte[]null";
		CharBuffer localCharBuffer=new CharBuffer(
			paramArrayOfByte.length*5+2);
		localCharBuffer.append('{');
		toString(paramArrayOfByte,0,paramArrayOfByte.length,",",
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(byte[] paramArrayOfByte,int paramInt1,
		int paramInt2)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2*5+2);
		localCharBuffer.append('{');
		toString(paramArrayOfByte,paramInt1,paramInt2,",",localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(byte[] paramArrayOfByte,int paramInt1,
		int paramInt2,String paramString)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2
			*(paramString.length()+4)+2);
		localCharBuffer.append('{');
		toString(paramArrayOfByte,paramInt1,paramInt2,paramString,
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static void toString(byte[] paramArrayOfByte,int paramInt1,
		int paramInt2,String paramString,CharBuffer paramCharBuffer)
	{
		int i=paramInt1+paramInt2-1;
		for(int j=paramInt1;j<i;j++)
			paramCharBuffer.append(paramArrayOfByte[j]).append(paramString);
		if(i>=0) paramCharBuffer.append(paramArrayOfByte[i]);
	}

	public static String toString(short[] paramArrayOfShort)
	{
		if(paramArrayOfShort==null) return "short[]null";
		CharBuffer localCharBuffer=new CharBuffer(
			paramArrayOfShort.length*6+2);
		localCharBuffer.append('{');
		toString(paramArrayOfShort,0,paramArrayOfShort.length,",",
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(short[] paramArrayOfShort,int paramInt1,
		int paramInt2)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2*6+2);
		localCharBuffer.append('{');
		toString(paramArrayOfShort,paramInt1,paramInt2,",",localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(short[] paramArrayOfShort,int paramInt1,
		int paramInt2,String paramString)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2
			*(paramString.length()+5)+2);
		localCharBuffer.append('{');
		toString(paramArrayOfShort,paramInt1,paramInt2,paramString,
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static void toString(short[] paramArrayOfShort,int paramInt1,
		int paramInt2,String paramString,CharBuffer paramCharBuffer)
	{
		int i=paramInt1+paramInt2-1;
		for(int j=paramInt1;j<i;j++)
			paramCharBuffer.append(paramArrayOfShort[j]).append(paramString);
		if(i>=0) paramCharBuffer.append(paramArrayOfShort[i]);
	}

	public static String toString(char[] paramArrayOfChar)
	{
		if(paramArrayOfChar==null) return "char[]null";
		CharBuffer localCharBuffer=new CharBuffer(
			paramArrayOfChar.length*2+2);
		localCharBuffer.append('{');
		toString(paramArrayOfChar,0,paramArrayOfChar.length,",",
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(char[] paramArrayOfChar,int paramInt1,
		int paramInt2)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2*2+2);
		localCharBuffer.append('{');
		toString(paramArrayOfChar,paramInt1,paramInt2,",",localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(char[] paramArrayOfChar,int paramInt1,
		int paramInt2,String paramString1,String paramString2,
		String paramString3)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2
			*(paramString1.length()+1)+paramString2.length()
			+paramString3.length());
		localCharBuffer.append(paramString2);
		toString(paramArrayOfChar,paramInt1,paramInt2,paramString1,
			localCharBuffer);
		localCharBuffer.append(paramString3);
		return localCharBuffer.getString();
	}

	public static void toString(char[] paramArrayOfChar,int paramInt1,
		int paramInt2,String paramString,CharBuffer paramCharBuffer)
	{
		int i=paramInt1+paramInt2-1;
		for(int j=paramInt1;j<i;j++)
			paramCharBuffer.append(paramArrayOfChar[j]).append(paramString);
		if(i>=0) paramCharBuffer.append(paramArrayOfChar[i]);
	}

	public static String toString(int[] paramArrayOfInt)
	{
		if(paramArrayOfInt==null) return "int[]null";
		CharBuffer localCharBuffer=new CharBuffer(paramArrayOfInt.length*9+2);
		localCharBuffer.append('{');
		toString(paramArrayOfInt,0,paramArrayOfInt.length,",",
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(int[] paramArrayOfInt,int paramInt1,
		int paramInt2)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2*9+2);
		localCharBuffer.append('{');
		toString(paramArrayOfInt,paramInt1,paramInt2,",",localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(int[] paramArrayOfInt,int paramInt1,
		int paramInt2,String paramString)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2
			*(paramString.length()+8)+2);
		localCharBuffer.append('{');
		toString(paramArrayOfInt,paramInt1,paramInt2,paramString,
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static void toString(int[] paramArrayOfInt,int paramInt1,
		int paramInt2,String paramString,CharBuffer paramCharBuffer)
	{
		int i=paramInt1+paramInt2-1;
		for(int j=paramInt1;j<i;j++)
			paramCharBuffer.append(paramArrayOfInt[j]).append(paramString);
		if(i>=0) paramCharBuffer.append(paramArrayOfInt[i]);
	}

	public static String toString(long[] paramArrayOfLong)
	{
		if(paramArrayOfLong==null) return "long[]null";
		CharBuffer localCharBuffer=new CharBuffer(
			paramArrayOfLong.length*16+2);
		localCharBuffer.append('{');
		toString(paramArrayOfLong,0,paramArrayOfLong.length,",",
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(long[] paramArrayOfLong,int paramInt1,
		int paramInt2)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2*16+2);
		localCharBuffer.append('{');
		toString(paramArrayOfLong,paramInt1,paramInt2,",",localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(long[] paramArrayOfLong,int paramInt1,
		int paramInt2,String paramString)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2
			*(paramString.length()+15)+2);
		localCharBuffer.append('{');
		toString(paramArrayOfLong,paramInt1,paramInt2,paramString,
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static void toString(long[] paramArrayOfLong,int paramInt1,
		int paramInt2,String paramString,CharBuffer paramCharBuffer)
	{
		int i=paramInt1+paramInt2-1;
		for(int j=paramInt1;j<i;j++)
			paramCharBuffer.append(paramArrayOfLong[j]).append(paramString);
		if(i>=0) paramCharBuffer.append(paramArrayOfLong[i]);
	}

	public static String toString(float[] paramArrayOfFloat)
	{
		if(paramArrayOfFloat==null) return "float[]null";
		CharBuffer localCharBuffer=new CharBuffer(
			paramArrayOfFloat.length*10+2);
		localCharBuffer.append('{');
		toString(paramArrayOfFloat,0,paramArrayOfFloat.length,",",
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(float[] paramArrayOfFloat,int paramInt1,
		int paramInt2)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2*10+2);
		localCharBuffer.append('{');
		toString(paramArrayOfFloat,paramInt1,paramInt2,",",localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(float[] paramArrayOfFloat,int paramInt1,
		int paramInt2,String paramString)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2
			*(paramString.length()+9)+2);
		localCharBuffer.append('{');
		toString(paramArrayOfFloat,paramInt1,paramInt2,paramString,
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static void toString(float[] paramArrayOfFloat,int paramInt1,
		int paramInt2,String paramString,CharBuffer paramCharBuffer)
	{
		int i=paramInt1+paramInt2-1;
		for(int j=paramInt1;j<i;j++)
			paramCharBuffer.append(paramArrayOfFloat[j]).append(paramString);
		if(i>=0) paramCharBuffer.append(paramArrayOfFloat[i]);
	}

	public static String toString(double[] paramArrayOfDouble)
	{
		if(paramArrayOfDouble==null) return "double[]null";
		CharBuffer localCharBuffer=new CharBuffer(
			paramArrayOfDouble.length*16+2);
		localCharBuffer.append('{');
		toString(paramArrayOfDouble,0,paramArrayOfDouble.length,",",
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(double[] paramArrayOfDouble,int paramInt1,
		int paramInt2)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2*16+2);
		localCharBuffer.append('{');
		toString(paramArrayOfDouble,paramInt1,paramInt2,",",localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(double[] paramArrayOfDouble,int paramInt1,
		int paramInt2,String paramString)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2
			*(paramString.length()+15)+2);
		localCharBuffer.append('{');
		toString(paramArrayOfDouble,paramInt1,paramInt2,paramString,
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static void toString(double[] paramArrayOfDouble,int paramInt1,
		int paramInt2,String paramString,CharBuffer paramCharBuffer)
	{
		int i=paramInt1+paramInt2-1;
		for(int j=paramInt1;j<i;j++)
			paramCharBuffer.append(paramArrayOfDouble[j])
				.append(paramString);
		if(i>=0) paramCharBuffer.append(paramArrayOfDouble[i]);
	}

	public static String toString(Object[] paramArrayOfObject)
	{
		if(paramArrayOfObject==null) return "Object[]null";
		CharBuffer localCharBuffer=new CharBuffer(
			paramArrayOfObject.length*25+2);
		localCharBuffer.append('{');
		toString(paramArrayOfObject,0,paramArrayOfObject.length,",",
			localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(Object[] paramArrayOfObject,int paramInt1,
		int paramInt2)
	{
		CharBuffer localCharBuffer=new CharBuffer(
			paramArrayOfObject.length*25+2);
		localCharBuffer.append('{');
		toString(paramArrayOfObject,paramInt1,paramInt2,",",localCharBuffer);
		localCharBuffer.append('}');
		return localCharBuffer.getString();
	}

	public static String toString(Object[] paramArrayOfObject,int paramInt1,
		int paramInt2,String paramString1,String paramString2,
		String paramString3)
	{
		CharBuffer localCharBuffer=new CharBuffer(paramInt2
			*(paramString1.length()+24)+paramString2.length()
			+paramString3.length());
		localCharBuffer.append(paramString2);
		toString(paramArrayOfObject,paramInt1,paramInt2,paramString1,
			localCharBuffer);
		localCharBuffer.append(paramString3);
		return localCharBuffer.getString();
	}

	public static void toString(Object[] paramArrayOfObject,int paramInt1,
		int paramInt2,String paramString,CharBuffer paramCharBuffer)
	{
		int i=paramInt1+paramInt2-1;
		for(int j=paramInt1;j<i;j++)
			paramCharBuffer.append(paramArrayOfObject[j])
				.append(paramString);
		if(i>=0) paramCharBuffer.append(paramArrayOfObject[i]);
	}

	public static int subWith(String paramString1,String paramString2,
		String paramString3)
	{
		if((paramString2==null)||(paramString2.length()==0)) return -1;
		if(paramString2.length()>paramString1.length()) return -1;
		if(paramString2.length()==paramString1.length())
			return paramString1.equals(paramString2)?0:-1;
		int i=0;
		int j=0;
		int k=0;
		while(true)
		{
			j=paramString1.indexOf(paramString2,i);
			if(j<0) return -1;
			i=j+paramString2.length();
			k=j;
			if(k>0)
			{
				k=j-paramString3.length();
				if((k<0)||(paramString1.indexOf(paramString3,k)!=k))
					continue;
			}
			k=i;
			if(k==paramString1.length()) return j;
			if(k+paramString3.length()>paramString1.length()) return -1;
			if(paramString1.indexOf(paramString3,k)==k) return j;
		}
	}

	public static String getSubValue(String paramString1,
		String paramString2,String paramString3,String paramString4)
	{
		if((paramString2==null)||(paramString2.length()==0)) return null;
		if(paramString2.length()+paramString4.length()>paramString1.length())
			return null;
		int i=0;
		int j=0;
		int k=0;
		while(true)
		{
			j=paramString1.indexOf(paramString2,i);
			if(j<0) return null;
			i=j+paramString2.length();
			k=j;
			if(k>0)
			{
				k=j-paramString3.length();
				if((k<0)||(paramString1.indexOf(paramString3,k)!=k))
					continue;
			}
			k=i;
			if(k+paramString4.length()>paramString1.length()) return null;
			if(k+paramString4.length()==paramString1.length()) return "";
			if(paramString1.indexOf(paramString4,k)==k)
			{
				i=k+paramString4.length();
				j=paramString1.indexOf(paramString3,i);
				if(j<0) return paramString1.substring(i);
				return paramString1.substring(i,j);
			}
		}
	}

	public static char valid(String paramString,char[] paramArrayOfChar)
	{
		int i=paramString.length();
		for(int j=0;j<i;j++)
		{
			char c=paramString.charAt(j);
			if(c<' ') return c;
			if(!valid(c,paramArrayOfChar)) return c;
		}
		return '\000';
	}

	public static boolean valid(char paramChar,char[] paramArrayOfChar)
	{
		if(paramChar<' ') return false;
		if(paramArrayOfChar==null) return true;
		int i=0;
		for(int j=paramArrayOfChar.length-1;i<j;i+=2)
		{
			if((paramChar>=paramArrayOfChar[i])
				&&(paramChar<=paramArrayOfChar[(i+1)])) return true;
		}
		return false;
	}

	public static String betweenString(String paramString1,
		String paramString2,String paramString3)
	{
		return betweenString(paramString1,paramString2,paramString3,0);
	}

	public static String betweenString(String paramString1,
		String paramString2,String paramString3,int paramInt)
	{
		if(paramString1==null) return null;
		if(paramInt<0) paramInt=0;
		if(paramString2.length()+paramString3.length()+paramInt>paramString1
			.length()) return null;
		int i=0;
		int j=paramString1.length();
		if((paramString2!=null)&&(paramString2.length()>0))
		{
			i=paramString1.indexOf(paramString2,paramInt);
			if(i<0) return null;
			i+=paramString2.length();
		}
		if((paramString3!=null)&&(paramString3.length()>0))
		{
			j=paramString1.indexOf(paramString3,i);
			if(j<0) return null;
		}
		if(i==j) return "";
		return paramString1.substring(i,j);
	}

	public static String toChineseNumber(int paramInt)
	{
		return toChineseNumber(paramInt,false);
	}

	public static String toChineseNumber(int paramInt,boolean paramBoolean)
	{
		char[] arrayOfChar1=paramBoolean?UPPER_DIGITS:DIGITS;
		char[] arrayOfChar2=paramBoolean?UPPER_DIGIT_BITS:DIGIT_BITS;
		CharBuffer localCharBuffer=new CharBuffer();
		toChineseNumber(paramInt,localCharBuffer,arrayOfChar1,arrayOfChar2,
			false);
		if((!paramBoolean)&&(localCharBuffer.length()>1)
			&&(localCharBuffer.read(0)==arrayOfChar1[1])
			&&(localCharBuffer.read(1)==arrayOfChar2[1]))
			localCharBuffer.setOffset(1);
		return localCharBuffer.getString();
	}

	public static void toChineseNumber(int paramInt,
		CharBuffer paramCharBuffer,char[] paramArrayOfChar1,
		char[] paramArrayOfChar2,boolean paramBoolean)
	{
		if(paramInt<0)
			throw new IllegalArgumentException(toString
				+" toChineseNumber, invalid number, number="+paramInt);
		int i=0;
		if(paramInt>=100000000)
		{
			i=paramInt/100000000;
			toChineseNumber(i,paramCharBuffer,paramArrayOfChar1,
				paramArrayOfChar2,paramBoolean);
			paramCharBuffer.append(paramArrayOfChar2[5]);
			paramInt%=100000000;
			if(paramInt==0) return;
			paramBoolean=true;
			if(i%10==0)
			{
				paramCharBuffer.append(paramArrayOfChar1[0]);
				paramBoolean=false;
			}
		}
		if(paramInt>=10000)
		{
			i=paramInt/10000;
			toChineseNumber(i,paramCharBuffer,paramArrayOfChar1,
				paramArrayOfChar2,paramBoolean);
			paramCharBuffer.append(paramArrayOfChar2[4]);
			paramInt%=10000;
			if(paramInt==0) return;
			paramBoolean=true;
			if(i%10==0)
			{
				paramCharBuffer.append(paramArrayOfChar1[0]);
				paramBoolean=false;
			}
		}
		int j=3;
		for(int k=1000;j>=0;k/=10)
		{
			int m=paramInt/k;
			if(m!=0)
			{
				paramBoolean=true;
				if(i!=0)
				{
					paramCharBuffer.append(paramArrayOfChar1[0]);
					i=0;
				}
				paramCharBuffer.append(paramArrayOfChar1[m]);
				if(j>0) paramCharBuffer.append(paramArrayOfChar2[j]);
				paramBoolean=true;
			}
			else if(paramBoolean)
			{
				i=1;
			}
			paramInt%=k;
			if(paramInt==0) return;
			j--;
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
		return split(str,String.class,regexs,0);
	}

	/**
	 * 将一个字符串按照指定字符顺序拆分为数组，维度由参数regexs长度决定，拆分顺序也由regexs元素顺序决定。<br>
	 * 例如：<br>
	 * 字符串"1,2:3,4|5,6:7,8" 拆分时传入regexs为拆分正则表达式数组 {"\\|",":",","}，
	 * 获得的数组是一个三维数组为{{{1,2},{3,4}},{{5,6},{7,8}}}<br>
	 * <br>
	 * 参数： <li>str：拆分源字符串<br> <li>type：标识拆分为什么类型的数组<br> <li>regexs: 拆分正则表达式数组
	 * <br> <li>offset: 拆分游标<br>
	 */
	public static Object split(String str,Class<?> type,String[] regexs)
	{
		return split(str,type,regexs,0);
	}

	/**
	 * 将一个字符串按照指定字符顺序拆分为数组，维度由参数regexs长度决定，拆分顺序也由regexs元素顺序决定。<br>
	 * 例如：<br>
	 * 字符串"1,2:3,4|5,6:7,8" 拆分时传入regexs为拆分正则表达式数组 {"\\|",":",","}，
	 * 获得的数组是一个三维数组为{{{1,2},{3,4}},{{5,6},{7,8}}}<br>
	 * <br>
	 * 参数： <li>str：拆分源字符串<br> <li>type：标识拆分为什么类型的数组<br> <li>regexs: 拆分正则表达式数组
	 * <br> <li>offset: 拆分游标<br>
	 */
	public static Object split(String str,Class<?> type,String[] regexs,
		int offset)
	{
		if(str==null||str.length()==0) return null;
		if(regexs.length==0||regexs.length<=offset)
		{
			if(type==String.class) return str;
			return ReflectKit.getPrimitive(type,str);
		}
		String[] strs=str.split(regexs[offset]);
		int[] dimensions=new int[regexs.length-(offset++)];
		dimensions[0]=strs.length;
		Object array=Array.newInstance(type,dimensions);
		for(int i=0;i<strs.length;i++)
			Array.set(array,i,split(strs[i],type,regexs,offset));
		return array;
	}

	/** 将指定对象转换为字符串数据(支持数组) */
	public static String toString(Object obj)
	{
		if(obj==null) return "null";
		StringBuilder buf=new StringBuilder();
		if((obj instanceof Object[])||ReflectKit.isBaseArray(obj))
		{
			Object elem=null;
			buf.append('[');
			int len=Array.getLength(obj);
			for(int i=0;i<len;i++)
			{
				elem=Array.get(obj,i);
				buf.append(toString(elem));
				if(i<len-1) buf.append(',');
			}
			buf.append(']');
		}
		else
			buf.append(obj);
		return buf.toString();
	}

	/** 格式化配置表 */
	public static String formatXml(String xml)
	{
		if(xml==null) return null;
		xml=xml.replaceAll("(\\s)+<","<");// 去掉前空白
		xml=xml.replaceAll(">(\\s)+",">");// 去掉后空白
		xml=xml.replaceAll("[?]>","?>\n");// 恢复头信息换行
		int num=0,index=0,lef,lefg,rigg;
		while(true)// 格式化缩进
		{
			index=xml.indexOf("><",index);
			if(index<0) break;
			lef=xml.lastIndexOf('<',index);
			lefg=xml.lastIndexOf('/',index);
			rigg=xml.indexOf("/",index);
			if(lef>lefg||lefg==index-1) num++;
			if(rigg==index+2) num--;
			xml=xml.substring(0,index+1)+'\n'+getTab(num)
				+xml.substring(index+1,xml.length());
			index=index+2+num;
		}
		return xml;
	}

	/** 获得指定数量的制表符 */
	private static String getTab(int num)
	{
		String tab="";
		for(int i=0;i<num;i++)
			tab+='\t';
		return tab;
	}

	/**
	 * 去除字符
	 * 
	 * @param args 去除字符数组
	 * @param type 类型
	 * @return
	 */
	public static char[] getCharExceptArgs(char[] args,int type)
	{
		char[] charArray;
		if(type==TYPE_LOWER)
		{
			charArray=ALL_LOWER_ENGLISH;
		}
		else
		{
			charArray=ALL_UPPER_ENGLISH;
		}
		char[] tempCharArray=new char[charArray.length-args.length];
		int k=0;
		for(int i=0;i<charArray.length;i++)
		{
			boolean exist=false;
			for(int j=0;j<args.length;j++)
			{
				if(args[j]==charArray[i])
				{
					exist=true;
					break;
				}
			}
			if(!exist)
			{
				tempCharArray[k]=charArray[i];
				k++;
			}
		}
		return tempCharArray;
	}
}