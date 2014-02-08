package com.cambrian.common.net;

/***
 * ��˵�����ַ��������
 * 
 * @version 2013-4-26
 * @author HYZ (huangyz1988@qq.com)
 */
public class CharBuffer
{

	/** ���� */
	public static final int CAPACITY=32;
	/** �� */
	public static final String NULL="null";
	/** �ַ����� */
	char[] array;
	/** ��λ:��һ��д��λ�� */
	int top;
	/** �α꣺��һ����ȡλ�� */
	int offset;

	/** ����һ��Ĭ�ϵ��ַ�������� */
	public CharBuffer()
	{
		this(CAPACITY);
	}

	/** ����һ��ָ���������ַ�������� */
	public CharBuffer(int capacity)
	{
		if(capacity<1)
			throw new IllegalArgumentException(CharBuffer.class.getName()
				+" <init>, invalid capatity:"+capacity);
		array=new char[capacity];
		top=0;
		offset=0;
	}

	/** ͨ��һ���ַ����鹹��һ���ַ�������� */
	public CharBuffer(char[] chars)
	{
		if(chars==null)
			throw new IllegalArgumentException(CharBuffer.class.getName()
				+" <init>, null data");
		array=chars;
		top=chars.length;
		offset=0;
	}

	/** ͨ��һ���ַ����飬offset��ʼ����Ϊlen�����򹹽�һ���ַ����� */
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

	/** ͨ��һ���ַ�������һ���ַ�������� */
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

	/** ���� */
	public int capacity()
	{
		return array.length;
	}

	/** �������� */
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

	/** ��λ */
	public int top()
	{
		return this.top;
	}

	/** ������λ */
	public void setTop(int paramInt)
	{
		if(paramInt<this.offset)
			throw new IllegalArgumentException(this+" setTop, invalid top:"
				+paramInt);
		if(paramInt>this.array.length) setCapacity(paramInt);
		this.top=paramInt;
	}

	/** �α� */
	public int offset()
	{
		return this.offset;
	}

	/** �����α� */
	public void setOffset(int paramInt)
	{
		if((paramInt<0)||(paramInt>this.top))
			throw new IllegalArgumentException(this
				+" setOffset, invalid offset:"+paramInt);
		this.offset=paramInt;
	}

	/** ���� */
	public int length()
	{
		return (this.top-this.offset);
	}

	/** ��ȡ�ַ����� */
	public char[] getArray()
	{
		return this.array;
	}

	/** ��ȡһ���ַ� */
	public char read()
	{
		return array[offset++];
	}

	/** ��ȡָ���±��ַ� */
	public char read(int index)
	{
		return array[index];
	}

	/** д��һ���ַ� */
	public void write(char ch)
	{
		array[top++]=ch;
	}

	/** ���ַ�д��ָ���±� */
	public void write(char ch,int index)
	{
		array[index]=ch;
	}

	/** ��ȡlen���ַ�������chars�У����±�index��ʼ */
	public void read(char[] chars,int index,int len)
	{
		System.arraycopy(this.array,this.offset,chars,index,len);
		this.offset+=len;
	}

	/** д��һ���ַ����飬���±�offset��ʼ������len */
	public void write(char[] chars,int offset,int len)
	{
		if(array.length<top+len) setCapacity(top+len);
		System.arraycopy(chars,offset,array,top,len);
		top+=len;
	}

	/** ����һ������ */
	public CharBuffer append(Object obj)
	{
		return append((obj!=null)?obj.toString():null);
	}

	/** ����һ���ַ��� */
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

	/** ����һ���ַ� */
	public CharBuffer append(char[] chars)
	{
		if(chars==null) return append("null");
		return append(chars,0,chars.length);
	}

	/** ����һ���ַ�����ָ���±�offset��ʼ������len�������ַ� */
	public CharBuffer append(char[] chars,int offset,int len)
	{
		if(chars==null) return append("null");
		write(chars,offset,len);
		return this;
	}

	/** ����һ��boolean */
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

	/** ����һ��char */
	public CharBuffer append(char paramChar)
	{
		if(array.length<top+1) setCapacity(top+32);
		array[(top++)]=paramChar;
		return this;
	}

	/** ����һ��int */
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

	/** ����һ��long */
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

	/** ����һ��float */
	public CharBuffer append(float paramFloat)
	{
		return append(Float.toString(paramFloat));
	}

	/** ����һ��double */
	public CharBuffer append(double paramDouble)
	{
		return append(String.valueOf(paramDouble));
	}

	/** ת��Ϊ�ַ����� */
	public char[] toArray()
	{
		char[] chars=new char[top-offset];
		System.arraycopy(array,offset,chars,0,chars.length);
		return chars;
	}

	/** ��� */
	public void clear()
	{
		top=0;
		offset=0;
	}

	/** ��ȡ�ַ��� */
	public String getString()
	{
		return new String(array,offset,top-offset);
	}

	/** hash�� */
	public int hashCode()
	{
		int i=0;
		char[] arrayOfChar=array;
		int k=offset;
		for(;k<top;++k)
			i=31*i+arrayOfChar[k];
		return i;
	}

	/** �ȶԷ��� */
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

	/** ��Ϣ */
	public String toString()
	{
		return super.toString()+"["+top+","+offset+","+array.length+"]";
	}
}