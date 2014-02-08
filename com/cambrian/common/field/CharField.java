package com.cambrian.common.field;

import com.cambrian.common.util.ByteKit;

public final class CharField extends Field
{

	public char value;

	public void coppy(Field dest)
	{
		super.coppy(dest);
		if(dest instanceof CharField) ((CharField)dest).value=value;
	}

	public Object getValue()
	{
		return new Character(this.value);
	}

	@Override
	public void setValue(Object obj)
	{
		if(obj==null)
			this.value=0;
		else
			this.value=(Character)obj;
	}

	@Override
	public void setBytesValue(byte[] bytes)
	{
		value=ByteKit.getChar(bytes);
	}

	@Override
	public byte[] getBytesValue()
	{
		return ByteKit.getBytes(value);
	}
}
