package com.cambrian.common.field;


import com.cambrian.common.util.ByteKit;

public final class DoubleField extends Field
{

	public double value;

	public void coppy(Field dest)
	{
		super.coppy(dest);
		if(dest instanceof DoubleField) ((DoubleField)dest).value=value;
	}

	public Object getValue()
	{
		return new Double(this.value);
	}

	@Override
	public void setValue(Object obj)
	{
		if(obj==null)
			this.value=0;
		else
			this.value=(Double)obj;
	}

	@Override
	public void setBytesValue(byte[] bytes)
	{
		value=ByteKit.getDouble(bytes);
	}

	@Override
	public byte[] getBytesValue()
	{
		return ByteKit.getBytes(value);
	}
}
