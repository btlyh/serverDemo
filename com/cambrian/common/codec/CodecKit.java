/**
 * 
 */
package com.cambrian.common.codec;

public final class CodecKit
{

	public static final byte[] ENCODING_TABLE={48,49,50,51,52,53,54,55,56,
		57,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,
		86,87,88,89,90,97,98,99,100,101,102,103,104,105,106,107,108,109,110,
		111,112,113,114,115,116,117,118,119,120,121,122,36,64};

	public static final byte[] DECODING_TABLE={-1,-1,-1,-1,-1,-1,-1,-1,-1,
		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
		-1,-1,-1,-1,-1,62,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0,1,2,3,4,5,6,7,
		8,9,-1,-1,-1,-1,-1,-1,63,10,11,12,13,14,15,16,17,18,19,20,21,22,23,
		24,25,26,27,28,29,30,31,32,33,34,35,-1,-1,-1,-1,-1,-1,36,37,38,39,
		40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,
		-1,-1,-1,-1};

	public static byte[] byteHex(byte b)
	{
		byte[] bytes=new byte[2];
		bytes[0]=ENCODING_TABLE[(b>>>4&0xF)];
		bytes[1]=ENCODING_TABLE[(b&0xF)];
		return bytes;
	}

	public static byte[] byteHex(byte[] bytes)
	{
		return byteHex(bytes,0,bytes.length);
	}

	public static byte[] byteHex(byte[] bytes,int start,int length)
	{
		byte[] _bytes=new byte[length<<1];
		length+=start;
		int i=start;
		for(int j=0;i<length;j+=2)
		{
			_bytes[j]=ENCODING_TABLE[(bytes[i]>>>4&0xF)];
			_bytes[(j+1)]=ENCODING_TABLE[(bytes[i]&0xF)];
			++i;
		}
		return _bytes;
	}

	public static byte[] hexBytes(String str)
	{
		int i=str.length();
		byte[] bytes=new byte[i/2];
		hexBytes(str,0,i,bytes,0);
		return bytes;
	}

	public static byte[] hexBytes(String str,int paramInt1,int paramInt2)
	{
		byte[] arrayOfByte=new byte[paramInt2/2];
		hexBytes(str,paramInt1,paramInt2,arrayOfByte,0);
		return arrayOfByte;
	}

	public static void hexBytes(String str,int paramInt1,int paramInt2,
		byte[] bytes,int paramInt3)
	{
		paramInt2+=paramInt1;
		int i=paramInt1;
		int j=paramInt3;
		for(int k=bytes.length;(i<paramInt2)&&(j<k);i+=2)
			bytes[(j++)]=(byte)(DECODING_TABLE[str.charAt(i+1)]+(DECODING_TABLE[str
				.charAt(i)]<<4));
	}
}