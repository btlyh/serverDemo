package com.cambrian.common.field;

/***
 * 类说明：字段集对象
 * 
 * @version 2013-4-18
 * @author HYZ (huangyz1988@qq.com)
 */
public final class Fields
{

	/** 空字段集 */
	public static final Field[] NULL=new Field[0];

	/** 字段集 */
	private Field[] fields=NULL;

	/** 构建一个空的字段集对象 */
	public Fields()
	{
		this(NULL);
	}

	/** 用一组字段构建一个字段集对象 */
	public Fields(Field[] fields)
	{
		this.fields=fields;
	}

	/** 字段数 */
	public int size()
	{
		return fields.length;
	}

	/** 获取字段集 */
	public Field[] getFields()
	{
		return fields;
	}

	/** 获取指定字段的索引 */
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

	/** 是否包含该字段 */
	public boolean contain(Field field)
	{
		return indexOf(field)>=0;
	}

	/** 添加字段（不允许重复） */
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

	/** 添加一组字段 */
	public boolean add(Field[] fields)
	{
		if(fields==null||fields.length==0) return false;
		return add(fields,0,fields.length);
	}

	/** 添加一组字段，从下标为index开始，添加数量为length */
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

	/** 移除指定字段 */
	public synchronized boolean remove(Field field)
	{
		if(field==null) return false;
		int index=indexOf(field);
		if(index<0) return false;
		remove(index);
		return true;
	}

	/** 移除指定下标字段 */
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

	/** 获取指定名称字段的下标 */
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

	/** 获取指定名称字段 */
	public Field get(String name)
	{
		if(name==null) return null;
		int i=indexOf(name);
		return i<0?null:fields[i];
	}

	/** 移除指定名称字段 */
	public synchronized Field remove(String name)
	{
		if(name==null) return null;
		int i=indexOf(name);
		if(i<0) return null;
		Field field=fields[i];
		remove(i);
		return field;
	}

	/** 获取字段集 */
	public synchronized Field[] toArray()
	{
		Field[] arrayOfFieldObject2=new Field[fields.length];
		System.arraycopy(fields,0,arrayOfFieldObject2,0,fields.length);
		return arrayOfFieldObject2;
	}

	/** 获取字段集 */
	public synchronized Field[] toArray(Field[] array)
	{
		int length=array.length>fields.length?fields.length:array.length;
		System.arraycopy(fields,0,array,0,length);
		return array;
	}

	/** 清理重置 */
	public synchronized void clear()
	{
		fields=NULL;
	}

	/** 信息 */
	public String toString()
	{
		return super.toString()+" SIZE="+fields.length;
	}
}
