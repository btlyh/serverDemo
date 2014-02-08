package com.cambrian.common.field;

/***
 * 类说明：字段对象
 * 
 * @version 2013-4-18
 * @author HYZ (huangyz1988@qq.com)
 */
public final class FieldValue extends Field
{

	/** 字段类型 */
	public Class<?> type;
	/** 字段值 */
	public Object value;

	/** 构建一个空字段 */
	public FieldValue()
	{
	}

	/** 构建一个字段类型为type,值为value */
	public FieldValue(Class<?> type,Object value)
	{
		this.type=type;
		this.value=value;
	}

	@Override
	public Object getValue()
	{
		return value;
	}

	@Override
	public void setValue(Object obj)
	{
		throw new RuntimeException();
	}

	@Override
	public void setBytesValue(byte[] bytes)
	{
	}

	@Override
	public byte[] getBytesValue()
	{
		return null;
	}

	public void coppy(Field dest)
	{
		super.coppy(dest);
		if(dest instanceof FieldValue)
		{
			((FieldValue)dest).type=type;
			((FieldValue)dest).value=value;
		}
	}

	/** 打印信息 */
	public String toString()
	{
		return super.toString()+", type="+(type==null?type:type.getName());
	}
}
