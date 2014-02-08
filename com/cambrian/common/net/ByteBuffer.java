/**
 * 
 */
package com.cambrian.common.net;

/**
 * 类说明：读写字节流
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class ByteBuffer
{

	/* static fields */
	public static final int CAPACITY=32;
	public static final int MAX_DATA_LENGTH=409600;
	public static final byte[] EMPTY_ARRAY=new byte[0];
	public static final String EMPTY_STRING="";
	/* static methods */

	/* fields */
	/** 数据存放 */
	byte[] array;
	/** 记录写到的位置 */
	int top;
	/** 记录读到的位置 */
	int offset;

	/* constructors */
	public ByteBuffer()
	{
		this(CAPACITY);
	}

	public ByteBuffer(int paramInt)
	{
		if(paramInt<1)
			throw new IllegalArgumentException(super.getClass().getName()
				+" <init>, invalid capatity:"+paramInt);
		this.array=new byte[paramInt];
		this.top=0;
		this.offset=0;
	}

	public ByteBuffer(byte[] paramArrayOfByte)
	{
		if(paramArrayOfByte==null)
			throw new IllegalArgumentException(super.getClass().getName()
				+" <init>, null data");
		this.array=paramArrayOfByte;
		this.top=paramArrayOfByte.length;
		this.offset=0;
	}

	// public ChannelBuffer(byte[] paramArrayOfByte,int paramInt1,int
	// paramInt2)
	// {
	// if(paramArrayOfByte==null)
	// throw new IllegalArgumentException(super.getClass().getName()
	// +" <init>, null data");
	// if((paramInt1<0)||(paramInt1>paramArrayOfByte.length))
	// throw new IllegalArgumentException(super.getClass().getName()
	// +" <init>, invalid index:"+paramInt1);
	// if((paramInt2<0)||(paramArrayOfByte.length<paramInt1+paramInt2))
	// throw new IllegalArgumentException(super.getClass().getName()
	// +" <init>, invalid length:"+paramInt2);
	// this.array=paramArrayOfByte;
	// this.top=(paramInt1+paramInt2);
	// this.offset=paramInt1;
	// }

	/* properties */

	/* init start */

	/* methods */
	public int capacity()
	{
		return this.array.length;
	}

	public void setCapacity(int paramInt)
	{
		int i=this.array.length;
		if(paramInt<=i) return;
		for(;i<paramInt;i=(i<<1)+1)
			;
		byte[] arrayOfByte=new byte[paramInt];
		System.arraycopy(this.array,0,arrayOfByte,0,this.top);
		this.array=arrayOfByte;
	}

	public int top()
	{
		return this.top;
	}

	public void setTop(int paramInt)
	{
		if(paramInt<this.offset)
			throw new IllegalArgumentException(this+" setTop, invalid top:"
				+paramInt);
		if(paramInt>this.array.length) setCapacity(paramInt);
		this.top=paramInt;
	}

	public int offset()
	{
		return this.offset;
	}

	public void setOffset(int paramInt)
	{
		if((paramInt<0)||(paramInt>this.top))
			throw new IllegalArgumentException(this
				+" setOffset, invalid offset:"+paramInt);
		this.offset=paramInt;
	}

	public int length()
	{
		return (this.top-this.offset);
	}

	public byte[] getArray()
	{
		return this.array;
	}

	public int getHashCode()
	{
		int i=17;
		for(int j=this.top-1;j>=0;--j)
			i=65537*i+this.array[j];
		return i;
	}

	public byte read(int paramInt)
	{
		return this.array[paramInt];
	}

	public void write(int paramInt1,int paramInt2)
	{
		this.array[paramInt2]=(byte)paramInt1;
	}

	public void read(byte[] paramArrayOfByte,int paramInt1,int paramInt2)
	{
		System.arraycopy(this.array,this.offset,paramArrayOfByte,paramInt1,
			paramInt2);
		this.offset+=paramInt2;
	}
	/** 读取一个布尔值 */
	public boolean readBoolean()
	{
		return (this.array[(this.offset++)]!=0);
	}
	/** 读取一个字节 */
	public byte readByte()
	{
		return this.array[(this.offset++)];
	}
	/** 读取一个无符号位byte */
	public int readUnsignedByte()
	{
		return (this.array[(this.offset++)]&0xFF);
	}
	/** 读取一个无符号位整形 */
	public int readUnsignedShort()
	{
		int i=this.offset;
		this.offset+=2;
		return ((this.array[(i+1)]&0xFF)+((this.array[i]&0xFF)<<8));
	}

	// public char readChar()
	// {
	// return (char)readUnsignedShort();
	// }
	/** 读取一个短整形 */
	public short readShort()
	{
		return (short)readUnsignedShort();
	}
	/** 读取一个整形 */
	public int readInt()
	{
		int i=this.offset;
		this.offset+=4;
		return ((this.array[(i+3)]&0xFF)+((this.array[(i+2)]&0xFF)<<8)
			+((this.array[(i+1)]&0xFF)<<16)+((this.array[i]&0xFF)<<24));
	}

	// public float readFloat()
	// {
	// return Float.intBitsToFloat(readInt());
	// }

	/** 读取一个长整形 */
	public long readLong()
	{
		int i=this.offset;
		this.offset+=8;
		long l=array[(i+7)]&0xFF;
		l|=((long)(array[(i+6)]&0xFF))<<8;
		l|=((long)(array[(i+5)]&0xFF))<<16;
		l|=((long)(array[(i+4)]&0xFF))<<24;
		l|=((long)(array[(i+3)]&0xFF))<<32;
		l|=((long)(array[(i+2)]&0xFF))<<40;
		l|=((long)(array[(i+1)]&0xFF))<<48;
		l|=((long)(array[i]&0xFF))<<56;
		return l;
	}

	// public double readDouble()
	// {
	// return Double.longBitsToDouble(readLong());
	// }

	// public int readLength()
	// {
	// // int i=this.array[this.offset]&0xFF;
	// // if(i>=128)
	// // {
	// // this.offset+=1;
	// // return (i-128);
	// // }
	// // if(i>=64) return (readUnsignedShort()-16384);
	// // if(i>=32) return (readInt()-536870912);
	// // throw new IllegalArgumentException(this
	// // +" readLength, invalid number:"+i);
	// return readInt();
	// }

	// public byte[] readData()
	// {
	// int i=readInt()-1;
	// if(i<0) return null;
	// if(i>MAX_DATA_LENGTH)
	// throw new IllegalArgumentException(this
	// +" readData, data overflow:"+i);
	// if(i==0) return EMPTY_ARRAY;
	// byte[] arrayOfByte=new byte[i];
	// read(arrayOfByte,0,i);
	// return arrayOfByte;
	// }

	/** 按指定编码格式读取一个字符串 */
	public String readString(String paramString)
	{
		int i=readInt();
		if(i<0) return null;
		if(i>MAX_DATA_LENGTH)
			throw new IllegalArgumentException(this
				+" readString, data overflow:"+i);
		if(i==0) return EMPTY_STRING;
		byte[] arrayOfByte=new byte[i];
		read(arrayOfByte,0,i);
		if(paramString==null) return new String(arrayOfByte);
		try
		{
			return new String(arrayOfByte,paramString);
		}
		catch(Exception localException)
		{
			throw new IllegalArgumentException(this
				+" readString, invalid charsetName:"+paramString);
		}
	}
	/** 按utf-8编码格式读取一个字符串 */
	public String readUTF()
	{
		return readString("utf-8");
	}

	/** 写入一个布尔值 */
	public void writeBoolean(boolean paramBoolean)
	{
		if(this.array.length<this.top+1) setCapacity(this.top+32);
		this.array[(this.top++)]=(byte)((paramBoolean)?1:0);
	}
	/** 写入一个字节 */
	public void writeByte(int paramInt)
	{
		if(this.array.length<this.top+1) setCapacity(this.top+32);
		this.array[(this.top++)]=(byte)paramInt;
	}
	/** 写入一个段整形 */
	public void writeShort(int paramInt)
	{
		int i=this.top;
		if(this.array.length<i+2) setCapacity(i+32);
		this.array[i]=(byte)(paramInt>>>8);
		this.array[(i+1)]=(byte)paramInt;
		this.top+=2;
	}
	// public void writeChar(int paramInt)
	// {
	// writeShort(paramInt);
	// }
	/** 写入一个整形 */
	public void writeInt(int paramInt)
	{
		int i=this.top;
		if(this.array.length<i+4) setCapacity(i+32);
		this.array[i]=(byte)(paramInt>>>24);
		this.array[(i+1)]=(byte)(paramInt>>>16);
		this.array[(i+2)]=(byte)(paramInt>>>8);
		this.array[(i+3)]=(byte)paramInt;
		this.top+=4;
	}
	// public void writeFloat(float f)
	// {
	// writeInt(Float.floatToIntBits(f));
	// }
	/** 写入一个长整形 */
	public void writeLong(long l)
	{
		int i=this.top;
		if(this.array.length<i+8) setCapacity(i+32);
		this.array[i]=(byte)(int)(l>>>56);
		this.array[i+1]=(byte)(int)(l>>>48);
		this.array[i+2]=(byte)(int)(l>>>40);
		this.array[i+3]=(byte)(int)(l>>>32);
		this.array[i+4]=(byte)(int)(l>>>24);
		this.array[i+5]=(byte)(int)(l>>>16);
		this.array[i+6]=(byte)(int)(l>>>8);
		this.array[i+7]=(byte)(int)l;
		this.top+=8;
	}

	// public void writeDouble(double paramDouble)
	// {
	// writeLong(Double.doubleToLongBits(paramDouble));
	// }

	// public void writeLength(int paramInt)
	// {
	// // if((paramInt>=536870912)||(paramInt<0))
	// // throw new IllegalArgumentException(this
	// // +" writeLength, invalid len:"+paramInt);
	// // if(paramInt<128)
	// // writeByte(paramInt+128);
	// // else if(paramInt<16384)
	// // writeShort(paramInt+16384);
	// // else
	// // writeInt(paramInt+536870912);
	// writeInt(paramInt);
	// }

	// public void writeData(byte[] paramArrayOfByte)
	// {
	// writeData(paramArrayOfByte,0,(paramArrayOfByte!=null)
	// ?paramArrayOfByte.length:0);
	// }
	// /** 写入带长度的byte数组 */
	// public void writeData(byte[] paramArrayOfByte,int paramInt1,int
	// paramInt2)
	// {
	// if(paramArrayOfByte==null)
	// {
	// writeInt(0);
	// return;
	// }
	// writeInt(paramInt2+1);
	// write(paramArrayOfByte,paramInt1,paramInt2);
	// }
	/** 写入byte数组 */
	public void write(byte[] bytes,int paramInt1,int paramInt2)
	{
		if(paramInt2<=0) return;
		if(this.array.length<this.top+paramInt2)
			setCapacity(this.top+paramInt2);
		System.arraycopy(bytes,paramInt1,this.array,this.top,paramInt2);
		this.top+=paramInt2;
	}
	/** 写入一个指定编码类型的字符串 */
	public void writeString(String paramString1,String paramString2)
	{
		if(paramString1==null)
		{
			writeInt(-1);
			return;
		}
		if(paramString1.length()<=0)
		{
			writeInt(0);
			return;
		}
		byte[] arrayOfByte;
		if(paramString2!=null)
		{
			try
			{
				arrayOfByte=paramString1.getBytes(paramString2);
			}
			catch(Exception localException)
			{
				throw new IllegalArgumentException(this
					+" writeString, invalid charsetName:"+paramString2);
			}
		}
		else
			arrayOfByte=paramString1.getBytes();
		writeInt(arrayOfByte.length);
		write(arrayOfByte,0,arrayOfByte.length);
	}
	/** 写入一个utf-8编码类型的字符串 */
	public void writeUTF(String paramString)
	{
		writeString(paramString,"utf-8");
	}

	// public void zeroOffset()
	// {
	// int i=this.offset;
	// if(i<=0) return;
	// int j=this.top-i;
	// System.arraycopy(this.array,i,this.array,0,j);
	// this.top=j;
	// this.offset=0;
	// }

	public byte[] toArray()
	{
		byte[] bytes=new byte[top-offset];
		if(bytes.length==0) return bytes;
		System.arraycopy(array,offset,bytes,0,bytes.length);
		return bytes;
	}
	/** 清空 */
	public void clear()
	{
		top=0;
		offset=0;
	}

	/* common methods */
	// public String toString()
	// {
	// return super.toString()+"["+this.top+","+this.offset+","
	// +this.array.length+"] ";
	// }
	/* inner class */

}
