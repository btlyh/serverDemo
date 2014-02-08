package com.cambrian.common.field;

import com.cambrian.common.util.ByteKit;

public final class BooleanField extends Field
{

	public boolean value;

	public void coppy(Field dest)
	{
		super.coppy(dest);
		if(!(dest instanceof BooleanField)) return;
		((BooleanField)dest).value=value;
	}

	public Object getValue()
	{
		return new Boolean(this.value);
	}

	@Override
	public void setValue(Object obj)
	{
		if(obj==null)
			this.value=false;
		else
			this.value=(Boolean)obj;
	}

	@Override
	public void setBytesValue(byte[] bytes)
	{
		value=ByteKit.getBoolean(bytes);
	}

	@Override
	public byte[] getBytesValue()
	{
		return ByteKit.getBytes(value);
	}
}
