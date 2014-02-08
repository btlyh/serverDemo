package com.cambrian.common.field;

import com.cambrian.common.util.ByteKit;

public final class ShortField extends Field
{

	public short value;

	public void coppy(Field dest)
	{
		super.coppy(dest);
		if(dest instanceof ShortField) ((ShortField)dest).value=value;
	}

	public Object getValue()
	{
		return new Short(this.value);
	}

	@Override
	public void setValue(Object obj)
	{
		if(obj==null)
			this.value=0;
		else
			this.value=(Short)obj;
	}
	
	@Override
	public void setBytesValue(byte[] bytes)
	{
		value=ByteKit.getShort(bytes);
	}

	@Override
	public byte[] getBytesValue()
	{
		return ByteKit.getBytes(value);
	}
}
