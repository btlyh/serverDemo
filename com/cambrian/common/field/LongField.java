package com.cambrian.common.field;

import com.cambrian.common.util.ByteKit;

public final class LongField extends Field
{

	public long value;

	public void coppy(Field dest)
	{
		super.coppy(dest);
		if(dest instanceof LongField) ((LongField)dest).value=value;
	}

	public Object getValue()
	{
		return new Long(this.value);
	}

	@Override
	public void setBytesValue(byte[] bytes)
	{
		value=ByteKit.getLong(bytes);
	}

	@Override
	public void setValue(Object obj)
	{
		if(obj==null)
			this.value=0;
		else
			this.value=(Long)obj;
	}

	@Override
	public byte[] getBytesValue()
	{
		return ByteKit.getBytes(value);
	}
}
