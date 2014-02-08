package com.cambrian.common.field;

import com.cambrian.common.util.ByteKit;

public final class StringField extends Field
{

	public String value;

	public void coppy(Field dest)
	{
		super.coppy(dest);
		if(dest instanceof StringField) ((StringField)dest).value=value;
	}

	public Object getValue()
	{
		return this.value;
	}

	@Override
	public void setValue(Object obj)
	{
		this.value=obj.toString();
	}

	@Override
	public void setBytesValue(byte[] bytes)
	{
		value=ByteKit.getUTFString(bytes);
	}

	@Override
	public byte[] getBytesValue()
	{
		return ByteKit.getUTFBytes(value);
	}
}
