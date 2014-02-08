package com.cambrian.common.field;

/***
 * ��˵�����ֶμ�����
 * 
 * @version 2013-4-18
 * @author HYZ (huangyz1988@qq.com)
 */
public final class Fields
{

	/** ���ֶμ� */
	public static final Field[] NULL=new Field[0];

	/** �ֶμ� */
	private Field[] fields=NULL;

	/** ����һ���յ��ֶμ����� */
	public Fields()
	{
		this(NULL);
	}

	/** ��һ���ֶι���һ���ֶμ����� */
	public Fields(Field[] fields)
	{
		this.fields=fields;
	}

	/** �ֶ��� */
	public int size()
	{
		return fields.length;
	}

	/** ��ȡ�ֶμ� */
	public Field[] getFields()
	{
		return fields;
	}

	/** ��ȡָ���ֶε����� */
	private int indexOf(Field field)
	{
		if(field==null) return -1;
		int i=fields.length-1;
		for(;i>=0;i--)
		{
			if(field.equals(fields[i])) break;
		}
		return i;
	}

	/** �Ƿ�������ֶ� */
	public boolean contain(Field field)
	{
		return indexOf(field)>=0;
	}

	/** ����ֶΣ��������ظ��� */
	public synchronized boolean add(Field field)
	{
		if(field==null||indexOf(field)>=0) return false;
		int index=fields.length;
		Field[] newFields=new Field[index+1];
		if(index>0) System.arraycopy(fields,0,newFields,0,index);
		newFields[index]=field;
		fields=newFields;
		return true;
	}

	/** ���һ���ֶ� */
	public boolean add(Field[] fields)
	{
		if(fields==null||fields.length==0) return false;
		return add(fields,0,fields.length);
	}

	/** ���һ���ֶΣ����±�Ϊindex��ʼ���������Ϊlength */
	public synchronized boolean add(Field[] fields,int index,int length)
	{
		if(fields==null||index<0||length<=0||fields.length<(index+length))
			return false;
		int len=fields.length;
		Field[] newFields=new Field[len+length];
		if(len>0) System.arraycopy(this.fields,0,newFields,0,len);
		System.arraycopy(fields,index,newFields,len,length);
		this.fields=newFields;
		return true;
	}

	/** �Ƴ�ָ���ֶ� */
	public synchronized boolean remove(Field field)
	{
		if(field==null) return false;
		int index=indexOf(field);
		if(index<0) return false;
		remove(index);
		return true;
	}

	/** �Ƴ�ָ���±��ֶ� */
	private void remove(int index)
	{
		if(fields.length==1)
		{
			fields=NULL;
			return;
		}
		Field[] newFields=new Field[fields.length-1];
		if(index>0) System.arraycopy(fields,0,newFields,0,index);
		if(index<newFields.length)
			System.arraycopy(fields,index+1,newFields,index,newFields.length
				-index);
		fields=newFields;
	}

	/** ��ȡָ�������ֶε��±� */
	private int indexOf(String name)
	{
		if(name==null) return -1;
		int i=fields.length-1;
		for(;i>=0;i--)
		{
			if(fields[i]==null) continue;
			if(name.equals(fields[i].name)) break;
		}
		return i;
	}

	/** ��ȡָ�������ֶ� */
	public Field get(String name)
	{
		if(name==null) return null;
		int i=indexOf(name);
		return i<0?null:fields[i];
	}

	/** �Ƴ�ָ�������ֶ� */
	public synchronized Field remove(String name)
	{
		if(name==null) return null;
		int i=indexOf(name);
		if(i<0) return null;
		Field field=fields[i];
		remove(i);
		return field;
	}

	/** ��ȡ�ֶμ� */
	public synchronized Field[] toArray()
	{
		Field[] arrayOfFieldObject2=new Field[fields.length];
		System.arraycopy(fields,0,arrayOfFieldObject2,0,fields.length);
		return arrayOfFieldObject2;
	}

	/** ��ȡ�ֶμ� */
	public synchronized Field[] toArray(Field[] array)
	{
		int length=array.length>fields.length?fields.length:array.length;
		System.arraycopy(fields,0,array,0,length);
		return array;
	}

	/** �������� */
	public synchronized void clear()
	{
		fields=NULL;
	}

	/** ��Ϣ */
	public String toString()
	{
		return super.toString()+" SIZE="+fields.length;
	}
}
