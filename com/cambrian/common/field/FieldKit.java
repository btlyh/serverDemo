package com.cambrian.common.field;

/***
 * 类说明：字段工具集
 * 
 * @version 2013-5-14
 * @author HYZ (huangyz1988@qq.com)
 */
public final class FieldKit
{

	/** 创建一个boolean字段 */
	public static BooleanField create(String name,boolean value)
	{
		BooleanField localBooleanField=new BooleanField();
		localBooleanField.name=name;
		localBooleanField.value=value;
		return localBooleanField;
	}
	/** 创建一个byte字段 */
	public static ByteField create(String name,byte value)
	{
		ByteField localByteField=new ByteField();
		localByteField.name=name;
		localByteField.value=value;
		return localByteField;
	}
	/** 创建一个short字段 */
	public static ShortField create(String name,short value)
	{
		ShortField localShortField=new ShortField();
		localShortField.name=name;
		localShortField.value=value;
		return localShortField;
	}
	/** 创建一个char字段 */
	public static CharField create(String name,char value)
	{
		CharField localCharField=new CharField();
		localCharField.name=name;
		localCharField.value=value;
		return localCharField;
	}
	/** 创建一个int字段 */
	public static IntField create(String name,int value)
	{
		IntField localIntField=new IntField();
		localIntField.name=name;
		localIntField.value=value;
		return localIntField;
	}
	/** 创建一个long字段 */
	public static LongField create(String name,long value)
	{
		LongField localLongField=new LongField();
		localLongField.name=name;
		localLongField.value=value;
		return localLongField;
	}
	/** 创建一个float字段 */
	public static FloatField create(String name,float value)
	{
		FloatField localFloatField=new FloatField();
		localFloatField.name=name;
		localFloatField.value=value;
		return localFloatField;
	}
	/** 创建一个double字段 */
	public static DoubleField create(String name,double value)
	{
		DoubleField localDoubleField=new DoubleField();
		localDoubleField.name=name;
		localDoubleField.value=value;
		return localDoubleField;
	}
	/** 创建一个String字段 */
	public static StringField create(String name,String value)
	{
		StringField localStringField=new StringField();
		localStringField.name=name;
		localStringField.value=value;
		return localStringField;
	}
	/** 创建一个byte[]字段 */
	public static ByteArrayField create(String name,byte[] value)
	{
		ByteArrayField localByteArrayField=new ByteArrayField();
		localByteArrayField.name=name;
		localByteArrayField.value=value;
		return localByteArrayField;
	}
	/** 复制一个字段集 */
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

	/** 复制一个字段 */
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
