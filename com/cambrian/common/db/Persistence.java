package com.cambrian.common.db;

import com.cambrian.common.field.Field;

/**
 * ��˵�������ݴ�ȡ�ӿ�
 * 
 * @version 1.0
 * @date 2013-9-26
 * @author maxw<woldits@qq.com>
 */
public class Persistence
{

	public static final int EXCEPTION=-1;
	/** �쳣 */
	/** û������ */
	public static final int RESULTLESS=0;
	/** �ɹ� */
	public static final int OK=1;
	/** �������� */
	public static final int ADD=2;

	public int set(Field key,Field[] fields)
	{
		return -1;
	}

	public int get(Field key,Field[] fields)
	{
		return -1;
	}

	public Field[][] gets(Field key,Field[] fields)
	{
		return null;
	}

	public int remove(Field key)
	{
		return -1;
	}

	public int remove(Field key,Field[] fields)
	{
		return -1;
	}

	public int set(Field[] key,Field[] fields)
	{
		return -1;
	}

	public int get(Field[] key,Field[] fields)
	{
		return -1;
	}

	public Field[][] gets(Field[] key,Field[] fields)
	{
		return null;
	}

	public int remove(Field[] key)
	{
		return -1;
	}

	public int remove(Field[] key,Field[] fields)
	{
		return -1;
	}
}