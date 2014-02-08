/** */
package com.cambrian.game.cc;

import com.cambrian.common.codec.Base64;
import com.cambrian.common.codec.CodecKit;
import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.CharBuffer;
import com.cambrian.common.util.ByteBufferThreadLocal;
import com.cambrian.common.util.CharBufferThreadLocal;
import com.cambrian.common.util.MathKit;
import com.cambrian.common.util.TimeKit;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 2013-4-25
 * @author HYZ (huangyz1988@qq.com)
 */
public class SidEncoder
{

	public static final Base64 BASE64=new Base64(CodecKit.ENCODING_TABLE,
		CodecKit.DECODING_TABLE);
	int sidNumber;
	Base64 base64;

	public SidEncoder()
	{
		this(0,BASE64);
	}

	public SidEncoder(int sidNumber,Base64 base64)
	{
		this.sidNumber=sidNumber;
		this.base64=base64;
	}

	public int getSidNumber()
	{
		return this.sidNumber;
	}

	public Base64 getBase64()
	{
		return this.base64;
	}

	public String createSid(String id)
	{
		ByteBuffer bb=ByteBufferThreadLocal.getByteBuffer();
		bb.clear();
		bb.writeInt(MathKit.randomInt());
		bb.writeUTF(id);
		bb.writeInt(TimeKit.nowTime());
		CharBuffer cb=CharBufferThreadLocal.getCharBuffer();
		cb.clear();
		synchronized(this)
		{
			cb.append(this.sidNumber++);
		}
		bb.writeUTF(cb.getString());
		byte[] array=bb.toArray();
		bb.clear();
		this.base64.encode(array,0,array.length,bb);
		return new String(bb.getArray(),0,bb.top());
	}

	public String parseSid(String sid)
	{
		String str=sid;
		ByteBuffer bb=ByteBufferThreadLocal.getByteBuffer();
		bb.clear();
		this.base64.decode(str,0,str.length(),bb);
		bb.readInt();
		return bb.readUTF();
	}
}