package com.cambrian.common.net;

/***
 * 类说明：字符缓冲对象
 * 
 * @version 2013-4-26
 * @author HYZ (huangyz1988@qq.com)
 */
public class CharBuffer
{

	/** 容量 */
	public static final int CAPACITY=32;
	/** 空 */
	public static final String NULL="null";
	/** 字符数组 */
	char[] array;
	/** 首位:下一个写入位置 */
	int top;
	/** 游标：下一个读取位置 */
	int offset;

	/** 构建一个默认的字符缓冲对象 */
	public CharBuffer()
	{
		this(CAPACITY);
	}

	/** 构建一个指定容量的字符缓冲对象 */
	public CharBuffer(int capacity)
	{
		if(capacity<1)
			throw new IllegalArgumentException(CharBuffer.class.getName()
				+" <init>, invalid capatity:"+capacity);
		array=new char[capacity];
		top=0;
		offset=0;
	}

	/** 通过一个字符数组构建一个字符缓冲对象 */
	public CharBuffer(char[] chars)
	{
		if(chars==null)
			throw new IllegalArgumentException(CharBuffer.class.getName()
				+" <init>, null data");
		array=chars;
		top=chars.length;
		offset=0;
	}

	/** 通过一个字符数组，offset开始数量为len的区域构建一个字符缓冲 */
	public CharBuffer(char[] chars,int offset,int len)
	{
		if(chars==null)
			throw new IllegalArgumentException(CharBuffer.class.getName()
				+" <init>, null data");
		if((offset<0)||(offset>chars.length))
			throw new IllegalArgumentException(CharBuffer.class.getName()
				+" <init>, invalid index:"+offset);
		if((len<0)||(chars.length<offset+len))
			throw new IllegalArgumentException(CharBuffer.class.getName()
				+" <init>, invalid length:"+len);
		this.array=chars;
		this.top=(offset+len);
		this.offset=offset;
	}

	/** 通过一个字符串构建一个字符缓冲对象 */
	public CharBuffer(String str)
	{
		if(str==null)
			throw new IllegalArgumentException(CharBuffer.class.getName()
				+" <init>, null str");
		int i=str.length();
		this.array=new char[i+32];
		str.getChars(0,i,this.array,0);
		this.top=i;
		this.offset=0;
	}

	/** 容量 */
	public int capacity()
	{
		return array.length;
	}

	/** 设置容量 */
	public void setCapacity(int paramInt)
	{
		int i=this.array.length;
		if(paramInt<=i) return;
		for(;i<paramInt;i=(i<<1)+1)
			;
		char[] arrayOfChar=new char[i];
		System.arraycopy(this.array,0,arrayOfChar,0,this.top);
		this.array=arrayOfChar;
	}

	/** 首位 */
	public int top()
	{
		return this.top;
	}

	/** 设置首位 */
	public void setTop(int paramInt)
	{
		if(paramInt<this.offset)
			throw new IllegalArgumentException(this+" setTop, invalid top:"
				+paramInt);
		if(paramInt>this.array.length) setCapacity(paramInt);
		this.top=paramInt;
	}

	/** 游标 */
	public int offset()
	{
		return this.offset;
	}

	/** 设置游标 */
	public void setOffset(int paramInt)
	{
		if((paramInt<0)||(paramInt>this.top))
			throw new IllegalArgumentException(this
				+" setOffset, invalid offset:"+paramInt);
		this.offset=paramInt;
	}

	/** 长度 */
	public int length()
	{
		return (this.top-this.offset);
	}

	/** 获取字符数组 */
	public char[] getArray()
	{
		return this.array;
	}

	/** 读取一个字符 */
	public char read()
	{
		return array[offset++];
	}

	/** 读取指定下标字符 */
	public char read(int index)
	{
		return array[index];
	}

	/** 写入一个字符 */
	public void write(char ch)
	{
		array[top++]=ch;
	}

	/** 将字符写到指定下标 */
	public void write(char ch,int index)
	{
		array[index]=ch;
	}

	/** 读取len个字符到数组chars中，从下标index开始 */
	public void read(char[] chars,int index,int len)
	{
		System.arraycopy(this.array,this.offset,chars,index,len);
		this.offset+=len;
	}

	/** 写入一个字符数组，冲下标offset开始，数量len */
	public void write(char[] chars,int offset,int len)
	{
		if(array.length<top+len) setCapacity(top+len);
		System.arraycopy(chars,offset,array,top,len);
		top+=len;
	}

	/** 附加一个对象 */
	public CharBuffer append(Object obj)
	{
		return append((obj!=null)?obj.toString():null);
	}

	/** 附加一个字符串 */
	public CharBuffer append(String str)
	{
		if(str==null) str="null";
		int i=str.length();
		if(i<=0) return this;
		if(array.length<top+i) setCapacity(top+i);
		str.getChars(0,i,array,top);
		top+=i;
		return this;
	}

	/** 附加一组字符 */
	public CharBuffer append(char[] chars)
	{
		if(chars==null) return append("null");
		return append(chars,0,chars.length);
	}

	/** 附加一组字符，冲指定下标offset开始，附加len数量个字符 */
	public CharBuffer append(char[] chars,int offset,int len)
	{
		if(chars==null) return append("null");
		write(chars,offset,len);
		return this;
	}

	/** 附加一个boolean */
	public CharBuffer append(boolean bool)
	{
		int i=top;
		if(bool)
		{
			if(this.array.length<i+4) setCapacity(i+32);
			this.array[i]='t';
			this.array[i+1]='r';
			this.array[i+2]='u';
			this.array[i+3]='e';
			this.top+=4;
		}
		else
		{
			if(this.array.length<i+5) setCapacity(i+32);
			this.array[i]='f';
			this.array[i+1]='a';
			this.array[i+2]='l';
			this.array[i+3]='s';
			this.array[i+4]='e';
			this.top+=5;
		}
		return this;
	}

	/** 附加一个char */
	public CharBuffer append(char paramChar)
	{
		if(array.length<top+1) setCapacity(top+32);
		array[(top++)]=paramChar;
		return this;
	}

	/** 附加一个int */
	public CharBuffer append(int number)
	{
		if(number==-2147483648)
		{
			append("-2147483648");
			return this;
		}
		int i=top;
		int j=0;
		int k=0;
		int l;
		if(number<0)
		{
			number=-number;
			k=0;
			for(l=number;(l/=10)>0;++k)
				;
			j=k+2;
			if(array.length<i+j) setCapacity(i+j);
			array[(i++)]='-';
		}
		else
		{
			k=0;
			for(l=number;(l/=10)>0;++k)
				;
			j=k+1;
			if(array.length<i+j) setCapacity(i+j);
		}
		while(k>=0)
		{
			array[(i+k)]=(char)(48+number%10);
			number/=10;
			--k;
		}
		this.top+=j;
		return this;
	}

	/** 附加一个long */
	public CharBuffer append(long paramLong)
	{
		if(paramLong==-9223372036854775808L)
		{
			append("-9223372036854775808");
			return this;
		}
		int i=top;
		int j=0;
		int k=0;
		long l;
		if(paramLong<0L)
		{
			paramLong=-paramLong;
			k=0;
			for(l=paramLong;(l/=10L)>0L;++k)
				;
			j=k+2;
			if(array.length<i+j) setCapacity(i+j);
			array[(i++)]='-';
		}
		else
		{
			k=0;
			for(l=paramLong;(l/=10L)>0L;++k)
				;
			j=k+1;
			if(array.length<i+j) setCapacity(i+j);
		}
		while(k>=0)
		{
			array[(i+k)]=(char)(int)(48L+paramLong%10L);
			paramLong/=10L;
			--k;
		}
		top+=j;
		return this;
	}

	/** 附加一个float */
	public CharBuffer append(float paramFloat)
	{
		return append(Float.toString(paramFloat));
	}

	/** 附加一个double */
	public CharBuffer append(double paramDouble)
	{
		return append(String.valueOf(paramDouble));
	}

	/** 转换为字符数组 */
	public char[] toArray()
	{
		char[] chars=new char[top-offset];
		System.arraycopy(array,offset,chars,0,chars.length);
		return chars;
	}

	/** 清除 */
	public void clear()
	{
		top=0;
		offset=0;
	}

	/** 获取字符串 */
	public String getString()
	{
		return new String(array,offset,top-offset);
	}

	/** hash码 */
	public int hashCode()
	{
		int i=0;
		char[] arrayOfChar=array;
		int k=offset;
		for(;k<top;++k)
			i=31*i+arrayOfChar[k];
		return i;
	}

	/** 比对方法 */
	public boolean equals(Object obj)
	{
		if(this==obj) return true;
		if(!(obj instanceof CharBuffer)) return false;
		CharBuffer charBuffer=(CharBuffer)obj;
		if(charBuffer.top!=top) return false;
		if(charBuffer.offset!=offset) return false;
		for(int i=top-1;i>=0;--i)
		{
			if(charBuffer.array[i]!=array[i]) return false;
		}
		return true;
	}

	/** 信息 */
	public String toString()
	{
		return super.toString()+"["+top+","+offset+","+array.length+"]";
	}
}