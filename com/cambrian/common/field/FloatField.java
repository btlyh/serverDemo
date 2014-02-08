package com.cambrian.common.field;

import com.cambrian.common.util.ByteKit;

public final class FloatField extends Field
{

	public float value;

	public void coppy(Field dest)
	{
		super.coppy(dest);
		if(dest instanceof FloatField) ((FloatField)dest).value=value;
	}

	public Object getValue()
	{
		return new Float(this.value);
	}

	@Override
	public void setValue(Object obj)
	{
		if(obj==null)
			this.value=0;
		else
			this.value=(Float)obj;
	}

	@Override
	public void setBytesValue(byte[] bytes)
	{
		value=ByteKit.getFloat(bytes);
	}

	@Override
	public byte[] getBytesValue()
	{
		return ByteKit.getBytes(value);
	}
}
