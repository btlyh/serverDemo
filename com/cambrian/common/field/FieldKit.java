package com.cambrian.common.field;

/***
 * ��˵�����ֶι��߼�
 * 
 * @version 2013-5-14
 * @author HYZ (huangyz1988@qq.com)
 */
public final class FieldKit
{

	/** ����һ��boolean�ֶ� */
	public static BooleanField create(String name,boolean value)
	{
		BooleanField localBooleanField=new BooleanField();
		localBooleanField.name=name;
		localBooleanField.value=value;
		return localBooleanField;
	}
	/** ����һ��byte�ֶ� */
	public static ByteField create(String name,byte value)
	{
		ByteField localByteField=new ByteField();
		localByteField.name=name;
		localByteField.value=value;
		return localByteField;
	}
	/** ����һ��short�ֶ� */
	public static ShortField create(String name,short value)
	{
		ShortField localShortField=new ShortField();
		localShortField.name=name;
		localShortField.value=value;
		return localShortField;
	}
	/** ����һ��char�ֶ� */
	public static CharField create(String name,char value)
	{
		CharField localCharField=new CharField();
		localCharField.name=name;
		localCharField.value=value;
		return localCharField;
	}
	/** ����һ��int�ֶ� */
	public static IntField create(String name,int value)
	{
		IntField localIntField=new IntField();
		localIntField.name=name;
		localIntField.value=value;
		return localIntField;
	}
	/** ����һ��long�ֶ� */
	public static LongField create(String name,long value)
	{
		LongField localLongField=new LongField();
		localLongField.name=name;
		localLongField.value=value;
		return localLongField;
	}
	/** ����һ��float�ֶ� */
	public static FloatField create(String name,float value)
	{
		FloatField localFloatField=new FloatField();
		localFloatField.name=name;
		localFloatField.value=value;
		return localFloatField;
	}
	/** ����һ��double�ֶ� */
	public static DoubleField create(String name,double value)
	{
		DoubleField localDoubleField=new DoubleField();
		localDoubleField.name=name;
		localDoubleField.value=value;
		return localDoubleField;
	}
	/** ����һ��String�ֶ� */
	public static StringField create(String name,String value)
	{
		StringField localStringField=new StringField();
		localStringField.name=name;
		localStringField.value=value;
		return localStringField;
	}
	/** ����һ��byte[]�ֶ� */
	public static ByteArrayField create(String name,byte[] value)
	{
		ByteArrayField localByteArrayField=new ByteArrayField();
		localByteArrayField.name=name;
		localByteArrayField.value=value;
		return localByteArrayField;
	}
	/** ����һ���ֶμ� */
	public static Fields coppy(Fields src)
	{
		Field[] array=src.toArray();
		for(int i=0;i<array.length;i++)
		{
			if(array[i]==null) continue;
			array[i]=coppy(array[i]);
		}
		Fields dest=new Fields(array);
		return dest;
	}

	/** ����һ���ֶ� */
	public static Field coppy(Field field)
	{
		if(field==null) return null;
		Field dest=null;
		try
		{
			dest=field.getClass().newInstance();
			field.coppy(dest);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return dest;
	}
}
