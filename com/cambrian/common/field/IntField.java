package com.cambrian.common.field;

import com.cambrian.common.util.ByteKit;

public final class IntField extends Field
{

	public int value;

	public void coppy(Field dest)
	{
		super.coppy(dest);
		if(dest instanceof IntField) ((IntField)dest).value=value;
	}

	public Object getValue()
	{
		return new Integer(this.value);
	}

	@Override
	public void setValue(Object obj)
	{
		if(obj==null)
			this.value=0;
		else
			this.value=(Integer)obj;
	}

	@Override
	public void setBytesValue(byte[] bytes)
	{
		value=ByteKit.getInt(bytes);
	}

	@Override
	public byte[] getBytesValue()
	{
		return ByteKit.getBytes(value);
	}
}
