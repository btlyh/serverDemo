package com.cambrian.common.codec;

import com.cambrian.common.net.ByteBuffer;

/***
 * ÀàËµÃ÷£ºBASE64±àÂë
 * 
 * @version 2013-5-14
 * @author HYZ (huangyz1988@qq.com)
 */
public final class Base64
{

	public static final byte PAD=61;
	private static final byte[] ENCODING_TABLE={65,66,67,68,69,70,71,72,73,
		74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,97,98,99,100,101,
		102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,
		119,120,121,122,48,49,50,51,52,53,54,55,56,57,43,47};

	private static final byte[] DECODING_TABLE={-1,-1,-1,-1,-1,-1,-1,-1,-1,
		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,62,-1,-1,-1,63,52,53,54,55,56,
		57,58,59,60,61,-1,-1,-1,-1,-1,-1,-1,0,1,2,3,4,5,6,7,8,9,10,11,12,13,
		14,15,16,17,18,19,20,21,22,23,24,25,-1,-1,-1,-1,-1,-1,26,27,28,29,
		30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,
		-1,-1,-1,-1};

	private static final Base64 instance=new Base64();
	private byte[] encodingTable;
	private byte[] decodingTable;

	public static Base64 getInstance()
	{
		return instance;
	}

	public Base64()
	{
		this(ENCODING_TABLE,DECODING_TABLE);
	}

	public Base64(byte[] encodingTable,byte[] decodingTable)
	{
		this.encodingTable=encodingTable;
		this.decodingTable=decodingTable;
	}

	public int getEncodeLength(int length)
	{
		if(length%3==0) return (4*length/3);
		return (4*length/3+length%3+1);
	}

	public byte[] encode(byte[] data)
	{
		byte[] array=new byte[getEncodeLength(data.length)];
		encode(data,0,data.length,array,0);
		return array;
	}

	public byte[] encode(byte[] data,int pos,int length)
	{
		byte[] array=new byte[getEncodeLength(length)];
		encode(data,pos,length,array,0);
		return array;
	}

	public void encode(byte[] data,int pos,int length,ByteBuffer bb)
	{
		int n=getEncodeLength(length);
		int top=bb.top();
		bb.setCapacity(top+n);
		encode(data,pos,length,bb.getArray(),top);
		bb.setTop(top+n);
	}

	public void encode(byte[] data,int pos,int length,byte[] array,int index)
	{
		if(data==null)
			throw new IllegalArgumentException(this+" encode, null data");
		if((pos<0)||(pos>=data.length))
			throw new IllegalArgumentException(this+" encode, invalid pos:"
				+pos);
		if((length<0)||(pos+length>data.length))
			throw new IllegalArgumentException(this
				+" encode, invalid length:"+length);
		int modulus=length%3;
		int n=pos+length-modulus;
		for(int i=pos;i<n;index+=4)
		{
			int a1=data[i]&0xFF;
			int a2=data[(i+1)]&0xFF;
			int a3=data[(i+2)]&0xFF;
			array[index]=this.encodingTable[(a1>>>2&0x3F)];
			array[(index+1)]=this.encodingTable[((a1<<4|a2>>>4)&0x3F)];
			array[(index+2)]=this.encodingTable[((a2<<2|a3>>>6)&0x3F)];
			array[(index+3)]=this.encodingTable[(a3&0x3F)];
			i+=3;
		}
		if(modulus==1)
		{
			int d1=data[n]&0xFF;
			int b1=d1>>>2&0x3F;
			int b2=d1<<4&0x3F;
			array[index]=this.encodingTable[b1];
			array[(index+1)]=this.encodingTable[b2];
		}
		else
		{
			if(modulus!=2) return;
			int d1=data[n]&0xFF;
			int d2=data[(n+1)]&0xFF;
			int b1=d1>>>2&0x3F;
			int b2=(d1<<4|d2>>>4)&0x3F;
			int b3=d2<<2&0x3F;
			array[index]=this.encodingTable[b1];
			array[(index+1)]=this.encodingTable[b2];
			array[(index+2)]=this.encodingTable[b3];
		}
	}

	public int getDecodeLength(int length)
	{
		if(length%4==0) return (3*length/4);
		return (3*length/4+length%4-1);
	}

	public byte[] decode(byte[] data)
	{
		byte[] bytes=new byte[getDecodeLength(data.length)];
		decode(data,0,data.length,bytes,0);
		return bytes;
	}

	public byte[] decode(byte[] data,int pos,int length)
	{
		byte[] bytes=new byte[getDecodeLength(length)];
		decode(data,pos,length,bytes,0);
		return bytes;
	}

	public void decode(byte[] data,int pos,int length,ByteBuffer bb)
	{
		int n=getDecodeLength(length);
		int top=bb.top();
		bb.setCapacity(top+n);
		decode(data,pos,length,bb.getArray(),top);
		bb.setTop(top+n);
	}

	public void decode(byte[] data,int pos,int length,byte[] bytes,int index)
	{
		if(data==null)
			throw new IllegalArgumentException(this+" decode, null data");
		if((pos<0)||(pos>=data.length))
			throw new IllegalArgumentException(this+" decode, invalid pos:"
				+pos);
		if((length<0)||(pos+length>data.length))
			throw new IllegalArgumentException(this
				+" decode, invalid length:"+length);
		int modulus=length%4;
		int n=pos+length-modulus;
		for(int i=pos;i<n;index+=3)
		{
			byte b1=this.decodingTable[data[i]];
			byte b2=this.decodingTable[data[(i+1)]];
			byte b3=this.decodingTable[data[(i+2)]];
			byte b4=this.decodingTable[data[(i+3)]];
			bytes[index]=(byte)(b1<<2|b2>>4);
			bytes[(index+1)]=(byte)(b2<<4|b3>>2);
			bytes[(index+2)]=(byte)(b3<<6|b4);
			i+=4;
		}
		if(modulus==2)
		{
			byte b1=this.decodingTable[data[n]];
			byte b2=this.decodingTable[data[(n+1)]];
			bytes[index]=(byte)(b1<<2|b2>>4);
		}
		else
		{
			if(modulus!=3) return;
			byte b1=this.decodingTable[data[n]];
			byte b2=this.decodingTable[data[(n+1)]];
			byte b3=this.decodingTable[data[(n+2)]];
			bytes[index]=(byte)(b1<<2|b2>>4);
			bytes[(index+1)]=(byte)(b2<<4|b3>>2);
		}
	}

	public byte[] decode(String str)
	{
		byte[] bytes=new byte[getDecodeLength(str.length())];
		decode(str,0,str.length(),bytes,0);
		return bytes;
	}

	public byte[] decode(String str,int pos,int length)
	{
		byte[] bytes=new byte[getDecodeLength(length)];
		decode(str,pos,length,bytes,0);
		return bytes;
	}

	public void decode(String str,int pos,int length,ByteBuffer bb)
	{
		int n=getDecodeLength(length);
		int top=bb.top();
		bb.setCapacity(top+n);
		decode(str,pos,length,bb.getArray(),top);
		bb.setTop(top+n);
	}

	public void decode(String str,int pos,int length,byte[] bytes,int index)
	{
		if(str==null)
			throw new IllegalArgumentException(this+" decode, null str");
		if((pos<0)||(pos>=str.length()))
			throw new IllegalArgumentException(this+" decode, invalid pos:"
				+pos);
		if((length<0)||(pos+length>str.length()))
			throw new IllegalArgumentException(this
				+" decode, invalid length:"+length);
		int modulus=length%4;
		int n=pos+length-modulus;
		for(int i=pos;i<n;index+=3)
		{
			byte b1=this.decodingTable[str.charAt(i)];
			byte b2=this.decodingTable[str.charAt(i+1)];
			byte b3=this.decodingTable[str.charAt(i+2)];
			byte b4=this.decodingTable[str.charAt(i+3)];
			bytes[index]=(byte)(b1<<2|b2>>4);
			bytes[(index+1)]=(byte)(b2<<4|b3>>2);
			bytes[(index+2)]=(byte)(b3<<6|b4);
			i+=4;
		}
		if(modulus==2)
		{
			byte b1=this.decodingTable[str.charAt(n)];
			byte b2=this.decodingTable[str.charAt(n+1)];
			bytes[index]=(byte)(b1<<2|b2>>4);
		}
		else
		{
			if(modulus!=3) return;
			byte b1=this.decodingTable[str.charAt(n)];
			byte b2=this.decodingTable[str.charAt(n+1)];
			byte b3=this.decodingTable[str.charAt(n+2)];
			bytes[index]=(byte)(b1<<2|b2>>4);
			bytes[(index+1)]=(byte)(b2<<4|b3>>2);
		}
	}
}
