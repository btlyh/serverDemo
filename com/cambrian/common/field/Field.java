package com.cambrian.common.field;

/***
 * ��˵�����ֶζ���
 * 
 * @version 2013-4-18
 * @author HYZ (huangyz1988@qq.com)
 */
public abstract class Field
{

	/** ����һ��boolean�ֶ� */
	public static BooleanField createBool(String name,boolean value)
	{
		BooleanField field=new BooleanField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** ����һ��byte�ֶ� */
	public static ByteField createByte(String name,byte value)
	{
		ByteField field=new ByteField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** ����һ��short�ֶ� */
	public static ShortField createShort(String name,short value)
	{
		ShortField field=new ShortField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** ����һ��char�ֶ� */
	public static CharField createChar(String name,char value)
	{
		CharField field=new CharField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** ����һ��int�ֶ� */
	public static IntField createInt(String name,int value)
	{
		IntField field=new IntField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** ����һ��long�ֶ� */
	public static LongField createLong(String name,long value)
	{
		LongField field=new LongField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** ����һ��float�ֶ� */
	public static FloatField createFloat(String name,float value)
	{
		FloatField field=new FloatField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** ����һ��double�ֶ� */
	public static DoubleField createDouble(String name,double value)
	{
		DoubleField field=new DoubleField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** ����һ��String�ֶ� */
	public static StringField createString(String name,String value)
	{
		StringField field=new StringField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** ����һ��byte[]�ֶ� */
	public static ByteArrayField createBytes(String name,byte[] value)
	{
		ByteArrayField field=new ByteArrayField();
		field.name=name;
		field.value=value;
		return field;
	}
	/** ����һ���ֶμ� */
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

	/** ����һ���ֶ� */
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

	/** �ֶ����� */
	public String name;

	/** ��ȡ�ֶ�ֵ */
	public abstract Object getValue();

	/** ��ȡ�ֶ��ֽ�����ֵ */
	public abstract void setBytesValue(byte[] bytes);
	/** ��ȡ�ֶ��ֽ�����ֵ */
	public abstract byte[] getBytesValue();

	/** ����ֵ */
	public abstract void setValue(Object obj);

	/** ���� */
	public void coppy(Field dest)
	{
		dest.name=name;
	}

	public String toString()
	{
		return "Field: name="+name+", value="+getValue();
	}
}
