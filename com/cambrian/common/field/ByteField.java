package com.cambrian.common.field;

import com.cambrian.common.util.ByteKit;

public final class ByteField extends Field
{

	public byte value;

	public void coppy(Field dest)
	{
		super.coppy(dest);
		if(dest instanceof ByteField) ((ByteField)dest).value=value;
	}

	public Object getValue()
	{
		return new Byte(this.value);
	}

	@Override
	public void setValue(Object obj)
	{
		if(obj==null)
			this.value=0;
		else
			this.value=(Byte)obj;
	}

	@Override
	public void setBytesValue(byte[] bytes)
	{
		value=ByteKit.getByte(bytes);
	}

	@Override
	public byte[] getBytesValue()
	{
		return ByteKit.getBytes(value);
	}
}
