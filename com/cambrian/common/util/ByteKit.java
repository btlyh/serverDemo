package com.cambrian.common.util;

import java.nio.charset.Charset;

/**
 * 类说明：字节数据转换工具
 * 
 * @author HYZ(huangyz1988@qq.com)
 * @version 2013-9-28
 */
public final class ByteKit
{

	public static final String toString=ByteKit.class.getName();

	public static boolean readBoolean(byte[] bytes,int paramInt)
	{
		return bytes[paramInt]!=0;
	}

	public static byte readByte(byte[] bytes,int paramInt)
	{
		return bytes[paramInt];
	}

	public static int readUnsignedByte(byte[] bytes,int paramInt)
	{
		return bytes[paramInt]&0xFF;
	}

	public static char readChar(byte[] bytes,int paramInt)
	{
		return (char)readUnsignedShort(bytes,paramInt);
	}

	public static char readChar_(byte[] bytes,int paramInt)
	{
		return (char)readUnsignedShort_(bytes,paramInt);
	}

	public static short readShort(byte[] bytes,int paramInt)
	{
		return (short)readUnsignedShort(bytes,paramInt);
	}

	public static short readShort_(byte[] bytes,int paramInt)
	{
		return (short)readUnsignedShort_(bytes,paramInt);
	}

	public static int readUnsignedShort(byte[] bytes,int paramInt)
	{
		return (bytes[(paramInt+1)]&0xFF)+((bytes[paramInt]&0xFF)<<8);
	}

	public static int readUnsignedShort_(byte[] bytes,int paramInt)
	{
		return ((bytes[(paramInt+1)]&0xFF)<<8)+(bytes[paramInt]&0xFF);
	}

	public static int readInt(byte[] bytes,int paramInt)
	{
		return (bytes[(paramInt+3)]&0xFF)+((bytes[(paramInt+2)]&0xFF)<<8)
			+((bytes[(paramInt+1)]&0xFF)<<16)+((bytes[paramInt]&0xFF)<<24);
	}

	public static int readInt_(byte[] bytes,int paramInt)
	{
		return ((bytes[(paramInt+3)]&0xFF)<<24)
			+((bytes[(paramInt+2)]&0xFF)<<16)
			+((bytes[(paramInt+1)]&0xFF)<<8)+(bytes[paramInt]&0xFF);
	}

	public static float readFloat(byte[] bytes,int paramInt)
	{
		return Float.intBitsToFloat(readInt(bytes,paramInt));
	}

	public static float readFloat_(byte[] bytes,int paramInt)
	{
		return Float.intBitsToFloat(readInt_(bytes,paramInt));
	}

	public static long readLong(byte[] bytes,int paramInt)
	{
		return (bytes[(paramInt+7)]&0xFF)+((bytes[(paramInt+6)]&0xFF)<<8)
			+((bytes[(paramInt+5)]&0xFF)<<16)
			+((bytes[(paramInt+4)]&0xFF)<<24)
			+((bytes[(paramInt+3)]&0xFF)<<32)
			+((bytes[(paramInt+2)]&0xFF)<<40)
			+((bytes[(paramInt+1)]&0xFF)<<48)+((bytes[paramInt]&0xFF)<<56);
	}

	public static long readLong_(byte[] bytes,int paramInt)
	{
		return ((bytes[(paramInt+7)]&0xFF)<<56)
			+((bytes[(paramInt+6)]&0xFF)<<48)
			+((bytes[(paramInt+5)]&0xFF)<<40)
			+((bytes[(paramInt+4)]&0xFF)<<32)
			+((bytes[(paramInt+3)]&0xFF)<<24)
			+((bytes[(paramInt+2)]&0xFF)<<16)
			+((bytes[(paramInt+1)]&0xFF)<<8)+(bytes[paramInt]&0xFF);
	}

	public static double readDouble(byte[] bytes,int paramInt)
	{
		return Double.longBitsToDouble(readLong(bytes,paramInt));
	}

	public static double readDouble_(byte[] bytes,int paramInt)
	{
		return Double.longBitsToDouble(readLong_(bytes,paramInt));
	}

	public static int getReadLength(byte paramByte)
	{
		int i=paramByte&0xFF;
		if(i>=128) return 1;
		if(i>=64) return 2;
		if(i>=32) return 4;
		throw new IllegalArgumentException(toString
			+" getReadLength, invalid number:"+i);
	}

	public static int readLength(byte[] bytes,int paramInt)
	{
		int i=bytes[paramInt]&0xFF;
		if(i>=128) return i-128;
		if(i>=64) return (i<<8)+(bytes[(paramInt+1)]&0xFF)-16384;
		if(i>=32)
		{
			return (i<<24)+((bytes[(paramInt+1)]&0xFF)<<16)
				+((bytes[(paramInt+2)]&0xFF)<<8)+(bytes[(paramInt+3)]&0xFF)
				-536870912;
		}
		throw new IllegalArgumentException(toString
			+" readLength, invalid number:"+i);
	}

	public static void writeBoolean(boolean paramBoolean,byte[] bytes,
		int paramInt)
	{
		bytes[paramInt]=(byte)(paramBoolean?1:0);
	}

	public static void writeByte(byte paramByte,byte[] bytes,int paramInt)
	{
		bytes[paramInt]=paramByte;
	}

	public static void writeChar(char paramChar,byte[] bytes,int paramInt)
	{
		writeShort((short)paramChar,bytes,paramInt);
	}

	public static void writeChar_(char paramChar,byte[] bytes,int paramInt)
	{
		writeShort_((short)paramChar,bytes,paramInt);
	}

	public static void writeShort(short paramShort,byte[] bytes,int paramInt)
	{
		bytes[paramInt]=(byte)(paramShort>>>8);
		bytes[(paramInt+1)]=(byte)paramShort;
	}

	public static void writeShort_(short paramShort,byte[] bytes,int paramInt)
	{
		bytes[paramInt]=(byte)paramShort;
		bytes[(paramInt+1)]=(byte)(paramShort>>>8);
	}

	public static void writeInt(int paramInt1,byte[] bytes,int paramInt2)
	{
		bytes[paramInt2]=(byte)(paramInt1>>>24);
		bytes[(paramInt2+1)]=(byte)(paramInt1>>>16);
		bytes[(paramInt2+2)]=(byte)(paramInt1>>>8);
		bytes[(paramInt2+3)]=(byte)paramInt1;
	}

	public static void writeInt_(int paramInt1,byte[] bytes,int paramInt2)
	{
		bytes[paramInt2]=(byte)paramInt1;
		bytes[(paramInt2+1)]=(byte)(paramInt1>>>8);
		bytes[(paramInt2+2)]=(byte)(paramInt1>>>16);
		bytes[(paramInt2+3)]=(byte)(paramInt1>>>24);
	}

	public static void writeFloat(float paramFloat,byte[] bytes,int paramInt)
	{
		writeInt(Float.floatToIntBits(paramFloat),bytes,paramInt);
	}

	public static void writeFloat_(float paramFloat,byte[] bytes,int paramInt)
	{
		writeInt_(Float.floatToIntBits(paramFloat),bytes,paramInt);
	}

	public static void writeLong(long paramLong,byte[] bytes,int paramInt)
	{
		bytes[paramInt]=(byte)(int)(paramLong>>>56);
		bytes[(paramInt+1)]=(byte)(int)(paramLong>>>48);
		bytes[(paramInt+2)]=(byte)(int)(paramLong>>>40);
		bytes[(paramInt+3)]=(byte)(int)(paramLong>>>32);
		bytes[(paramInt+4)]=(byte)(int)(paramLong>>>24);
		bytes[(paramInt+5)]=(byte)(int)(paramLong>>>16);
		bytes[(paramInt+6)]=(byte)(int)(paramLong>>>8);
		bytes[(paramInt+7)]=(byte)(int)paramLong;
	}

	public static void writeLong_(long paramLong,byte[] bytes,int paramInt)
	{
		bytes[paramInt]=(byte)(int)paramLong;
		bytes[(paramInt+1)]=(byte)(int)(paramLong>>>8);
		bytes[(paramInt+2)]=(byte)(int)(paramLong>>>16);
		bytes[(paramInt+3)]=(byte)(int)(paramLong>>>24);
		bytes[(paramInt+4)]=(byte)(int)(paramLong>>>32);
		bytes[(paramInt+5)]=(byte)(int)(paramLong>>>40);
		bytes[(paramInt+6)]=(byte)(int)(paramLong>>>48);
		bytes[(paramInt+7)]=(byte)(int)(paramLong>>>56);
	}

	public static void writeDouble(double paramDouble,byte[] bytes,
		int paramInt)
	{
		writeLong(Double.doubleToLongBits(paramDouble),bytes,paramInt);
	}

	public static void writeDouble_(double paramDouble,byte[] bytes,
		int paramInt)
	{
		writeLong_(Double.doubleToLongBits(paramDouble),bytes,paramInt);
	}

	public static int writeLength(int paramInt1,byte[] bytes,int paramInt2)
	{
		if((paramInt1>=536870912)||(paramInt1<0))
			throw new IllegalArgumentException(toString
				+" writeLength, invalid len:"+paramInt1);
		if(paramInt1>=16384)
		{
			writeInt(paramInt1+536870912,bytes,paramInt2);
			return 4;
		}
		if(paramInt1>=128)
		{
			writeShort((short)(paramInt1+16384),bytes,paramInt2);
			return 2;
		}

		writeByte((byte)(paramInt1+128),bytes,paramInt2);
		return 1;
	}

	public static String readISO8859_1(byte[] bytes)
	{
		return readISO8859_1(bytes,0,bytes.length);
	}

	public static String readISO8859_1(byte[] bytes,int paramInt1,
		int paramInt2)
	{
		char[] arrayOfChar=new char[paramInt2];
		int i=paramInt1+paramInt2-1;
		for(int j=arrayOfChar.length-1;i>=paramInt1;j--)
		{
			arrayOfChar[j]=(char)bytes[i];

			i--;
		}
		return new String(arrayOfChar);
	}

	public static byte[] writeISO8859_1(String paramString)
	{
		return writeISO8859_1(paramString,0,paramString.length());
	}

	public static byte[] writeISO8859_1(String paramString,int paramInt1,
		int paramInt2)
	{
		byte[] arrayOfByte=new byte[paramInt2];
		writeISO8859_1(paramString,paramInt1,paramInt2,arrayOfByte,0);
		return arrayOfByte;
	}

	public static void writeISO8859_1(String paramString,int paramInt1,
		int paramInt2,byte[] bytes,int paramInt3)
	{
		int j=paramInt1+paramInt2-1;
		for(int k=paramInt3+paramInt2-1;j>=paramInt1;k--)
		{
			int i=paramString.charAt(j);
			bytes[k]=(i>256?63:(byte)i);

			j--;
		}
	}

	public static void writeISO8859_1(char[] paramArrayOfChar,int paramInt1,
		int paramInt2,byte[] bytes,int paramInt3)
	{
		int j=paramInt1+paramInt2-1;
		for(int k=paramInt3+paramInt2-1;j>=paramInt1;k--)
		{
			int i=paramArrayOfChar[j];
			bytes[k]=(i>256?63:(byte)i);

			j--;
		}
	}

	public static String readUTF(byte[] bytes)
	{
		char[] arrayOfChar=new char[bytes.length];
		int i=readUTF(bytes,0,bytes.length,arrayOfChar);
		return i>=0?new String(arrayOfChar,0,i):null;
	}

	public static String readUTF(byte[] bytes,int paramInt1,int paramInt2)
	{
		char[] arrayOfChar=new char[paramInt2];
		int i=readUTF(bytes,paramInt1,paramInt2,arrayOfChar);
		return i>=0?new String(arrayOfChar,0,i):null;
	}

	public static int readUTF(byte[] bytes,int paramInt1,int paramInt2,
		char[] paramArrayOfChar)
	{
		int n=0;
		int i1=paramInt1+paramInt2;
		while(paramInt1<i1)
		{
			int j=bytes[paramInt1]&0xFF;
			int i=j>>4;
			if(i<8)
			{
				paramInt1++;
				paramArrayOfChar[(n++)]=(char)j;
			}
			else
			{
				int k;
				if((i==12)||(i==13))
				{
					paramInt1+=2;
					if(paramInt1>i1) return -1;
					k=bytes[(paramInt1-1)];
					if((k&0xC0)!=128) return -1;
					paramArrayOfChar[(n++)]=(char)((j&0x1F)<<6|k&0x3F);
				}
				else if(i==14)
				{
					paramInt1+=3;
					if(paramInt1>i1) return -1;
					k=bytes[(paramInt1-2)];
					if((k&0xC0)!=128) return -1;
					int m=bytes[(paramInt1-1)];
					if((m&0xC0)!=128) return -1;
					paramArrayOfChar[(n++)]=(char)((j&0xF)<<12|(k&0x3F)<<6|m&0x3F);
				}
				else
				{
					return -1;
				}
			}
		}
		return n;
	}

	public static int getUTFLength(String paramString,int paramInt1,
		int paramInt2)
	{
		int i=0;

		for(int k=paramInt1;k<paramInt2;k++)
		{
			int j=paramString.charAt(k);
			if((j>=1)&&(j<=127))
				i++;
			else if(j>2047)
				i+=3;
			else
				i+=2;
		}
		return i;
	}

	public static int getUTFLength(char[] paramArrayOfChar,int paramInt1,
		int paramInt2)
	{
		int i=0;

		for(int k=paramInt1;k<paramInt2;k++)
		{
			int j=paramArrayOfChar[k];
			if((j>=1)&&(j<=127))
				i++;
			else if(j>2047)
				i+=3;
			else
				i+=2;
		}
		return i;
	}

	public static byte[] writeUTF(String paramString)
	{
		return writeUTF(paramString,0,paramString.length());
	}

	public static byte[] writeUTF(String paramString,int paramInt1,
		int paramInt2)
	{
		byte[] arrayOfByte=new byte[getUTFLength(paramString,paramInt1,
			paramInt2)];
		writeUTF(paramString,paramInt1,paramInt2,arrayOfByte,0);
		return arrayOfByte;
	}

	public static void writeUTF(String paramString,int paramInt1,
		int paramInt2,byte[] bytes,int paramInt3)
	{
		for(int j=paramInt1;j<paramInt2;j++)
		{
			int i=paramString.charAt(j);
			if((i>=1)&&(i<=127))
			{
				bytes[(paramInt3++)]=(byte)i;
			}
			else if(i>2047)
			{
				bytes[(paramInt3++)]=(byte)(0xE0|i>>12&0xF);
				bytes[(paramInt3++)]=(byte)(0x80|i>>6&0x3F);
				bytes[(paramInt3++)]=(byte)(0x80|i&0x3F);
			}
			else
			{
				bytes[(paramInt3++)]=(byte)(0xC0|i>>6&0x1F);
				bytes[(paramInt3++)]=(byte)(0x80|i&0x3F);
			}
		}
	}

	public static void writeUTF(char[] chs,int start,int length,
		byte[] bytes,int index)
	{
		for(int j=start;j<length;j++)
		{
			int i=chs[j];
			if((i>=1)&&(i<=127))
			{
				bytes[(index++)]=(byte)i;
			}
			else if(i>2047)
			{
				bytes[(index++)]=(byte)(0xE0|i>>12&0xF);
				bytes[(index++)]=(byte)(0x80|i>>6&0x3F);
				bytes[(index++)]=(byte)(0x80|i&0x3F);
			}
			else
			{
				bytes[(index++)]=(byte)(0xC0|i>>6&0x1F);
				bytes[(index++)]=(byte)(0x80|i&0x3F);
			}
		}
	}

	/** 基本类型转换为byte数组 */
	public static byte[] getBytes(byte b)
	{
		byte[] bytes=new byte[1];
		bytes[0]=b;
		return bytes;
	}
	/** 基本类型转换为byte数组 */
	public static byte[] getBytes(boolean b)
	{
		byte[] bytes=new byte[1];
		if(b) bytes[0]=(byte)1;
		return bytes;
	}
	/** 基本类型转换为byte数组 */
	public static byte[] getBytes(char c)
	{
		return getBytes((short)c);
	}
	/** 基本类型转换为byte数组 */
	public static byte[] getBytes(short s)
	{
		byte[] bytes=new byte[2];
		bytes[0]=(byte)(s>>>8);
		bytes[1]=(byte)s;
		return bytes;
	}
	/** 基本类型转换为byte数组 */
	public static byte[] getBytes(int i)
	{
		byte[] bytes=new byte[4];
		bytes[0]=(byte)(i>>>24);
		bytes[1]=(byte)(i>>>16);
		bytes[2]=(byte)(i>>>8);
		bytes[3]=(byte)i;
		return bytes;
	}
	/** 基本类型转换为byte数组 */
	public static byte[] getBytes(long l)
	{
		byte[] bytes=new byte[8];
		bytes[0]=(byte)(l>>>56);
		bytes[1]=(byte)(l>>>48);
		bytes[2]=(byte)(l>>>40);
		bytes[3]=(byte)(l>>>32);
		bytes[4]=(byte)(l>>>24);
		bytes[5]=(byte)(l>>>16);
		bytes[6]=(byte)(l>>>8);
		bytes[7]=(byte)l;
		return bytes;
	}
	/** 基本类型转换为byte数组 */
	public static byte[] getBytes(float data)
	{
		return getBytes(Float.floatToIntBits(data));
	}
	/** 基本类型转换为byte数组 */
	public static byte[] getBytes(double data)
	{
		return getBytes(Double.doubleToLongBits(data));
	}
	/** 字符串转换为byte数组 */
	public static byte[] getUTFBytes(String data)
	{
		return getBytes(data,"UTF-8");
	}
	/** 字符串转换为byte数组 */
	public static byte[] getBytes(String data,String charsetName)
	{
		Charset charset=Charset.forName(charsetName);
		return data.getBytes(charset);
	}

	/** byte数组转换为boolean */
	public static byte getByte(byte[] bytes)
	{
		return bytes[0];
	}
	/** byte数组转换为boolean */
	public static boolean getBoolean(byte[] bytes)
	{
		return bytes[0]!=0;
	}
	/** byte数组转换为字符 */
	public static char getChar(byte[] bytes)
	{
		return (char)getShort(bytes);
	}
	/** byte数组转换为short */
	public static short getShort(byte[] bytes)
	{
		return (short)getUnsignedShort(bytes);
	}
	/** byte数组转换为无符号short */
	public static int getUnsignedShort(byte[] bytes)
	{
		int s=bytes[1]&0xFF;
		s|=((int)bytes[0]&0xFF)<<8;
		return s;
	}
	/** byte数组转换为int */
	public static int getInt(byte[] bytes)
	{
		int i=bytes[3]&0xFF;
		i|=((int)(bytes[2]&0xFF))<<8;
		i|=((int)(bytes[1]&0xFF))<<16;
		i|=((int)(bytes[0]&0xFF))<<24;
		return i;
	}
	/** byte数组转换为long */
	public static long getLong(byte[] bytes)
	{
		long l=bytes[7]&0xFF;
		l|=((long)(bytes[6]&0xFF))<<8;
		l|=((long)(bytes[5]&0xFF))<<16;
		l|=((long)(bytes[4]&0xFF))<<24;
		l|=((long)(bytes[3]&0xFF))<<32;
		l|=((long)(bytes[2]&0xFF))<<40;
		l|=((long)(bytes[1]&0xFF))<<48;
		l|=((long)(bytes[0]&0xFF))<<56;
		return l;
	}
	/** byte数组转换为float */
	public static float getFloat(byte[] bytes)
	{
		return Float.intBitsToFloat(getInt(bytes));
	}
	/** byte数组转换为double */
	public static double getDouble(byte[] bytes)
	{
		return Double.longBitsToDouble(getLong(bytes));
	}
	/** byte数组转换为基本类型 */
	public static String getUTFString(byte[] bytes)
	{
		return getString(bytes,"UTF-8");
	}
	/** byte数组转换为字符串 */
	public static String getString(byte[] bytes,String charset)
	{
		return new String(bytes,Charset.forName(charset));
	}
}