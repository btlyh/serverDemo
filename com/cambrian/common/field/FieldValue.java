package com.cambrian.common.field;

/***
 * ��˵�����ֶζ���
 * 
 * @version 2013-4-18
 * @author HYZ (huangyz1988@qq.com)
 */
public final class FieldValue extends Field
{

	/** �ֶ����� */
	public Class<?> type;
	/** �ֶ�ֵ */
	public Object value;

	/** ����һ�����ֶ� */
	public FieldValue()
	{
	}

	/** ����һ���ֶ�����Ϊtype,ֵΪvalue */
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

	/** ��ӡ��Ϣ */
	public String toString()
	{
		return super.toString()+", type="+(type==null?type:type.getName());
	}
}
