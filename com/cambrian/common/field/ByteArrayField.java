package com.cambrian.common.field;

public final class ByteArrayField extends Field
{

	public byte[] value;

	public void coppy(Field dest)
	{
		super.coppy(dest);
		if(dest instanceof ByteArrayField)
			((ByteArrayField)dest).value=value;
	}

	public Object getValue()
	{
		return this.value;
	}

	@Override
	public void setValue(Object obj)
	{
		this.value=(byte[])obj;
	}

	@Override
	public void setBytesValue(byte[] bytes)
	{
		value=bytes;
	}

	@Override
	public byte[] getBytesValue()
	{
		return value;
	}
}
