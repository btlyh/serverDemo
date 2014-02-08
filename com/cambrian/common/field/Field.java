package com.cambrian.common.field;

/***
 * 类说明：字段对象
 * 
 * @version 2013-4-18
 * @author HYZ (huangyz1988@qq.com)
 */
public abstract class Field
{

	/** 创建一个boolean字段 */
	public static BooleanField createBool(String name,boolean value)
	{
		BooleanField field=new BooleanField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** 创建一个byte字段 */
	public static ByteField createByte(String name,byte value)
	{
		ByteField field=new ByteField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** 创建一个short字段 */
	public static ShortField createShort(String name,short value)
	{
		ShortField field=new ShortField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** 创建一个char字段 */
	public static CharField createChar(String name,char value)
	{
		CharField field=new CharField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** 创建一个int字段 */
	public static IntField createInt(String name,int value)
	{
		IntField field=new IntField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** 创建一个long字段 */
	public static LongField createLong(String name,long value)
	{
		LongField field=new LongField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** 创建一个float字段 */
	public static FloatField createFloat(String name,float value)
	{
		FloatField field=new FloatField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** 创建一个double字段 */
	public static DoubleField createDouble(String name,double value)
	{
		DoubleField field=new DoubleField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** 创建一个String字段 */
	public static StringField createString(String name,String value)
	{
		StringField field=new StringField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** 创建一个byte[]字段 */
	public static ByteArrayField createBytes(String name,byte[] value)
	{
		ByteArrayField field=new ByteArrayField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** 复制一个字段集 */
	public static Fields coppy(Fields src)
	{
		Field[] array=src.toArray();
		for(int i=0;i<array.length;i++)
		{
			if(array[i]==null) continue;
			array[i]=coppyField(array[i]);
		}
		Fields dest=new Fields(array);
		return dest;
	}

	/** 复制一个字段 */
	public static Field coppyField(Field field)
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

	/** 字段名字 */
	public String name;

	/** 获取字段值 */
	public abstract Object getValue();

	/** 获取字段字节数组值 */
	public abstract void setBytesValue(byte[] bytes);
	/** 获取字段字节数组值 */
	public abstract byte[] getBytesValue();

	/** 设置值 */
	public abstract void setValue(Object obj);

	/** 复制 */
	public void coppy(Field dest)
	{
		dest.name=name;
	}

	public String toString()
	{
		return "Field: name="+name+", value="+getValue();
	}
}
