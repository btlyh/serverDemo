/**
 * 
 */
package com.cambrian.common.thread;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class ThreadAccessEntry
{

	/* static fields */
	public static final Object NONE=new Object();

	public static final Object VOID=new Object();
	private static int entryId;

	/* static methods */
	public static synchronized int newId()
	{
		return (entryId++);
	}

	/* fields */
	int id;
	Object result;

	/* constructors */

	public ThreadAccessEntry()
	{
		this(newId(),NONE);
	}

	public ThreadAccessEntry(Object paramObject)
	{
		this(newId(),paramObject);
	}

	public ThreadAccessEntry(int paramInt,Object paramObject)
	{
		this.id=super.hashCode();

		this.result=NONE;

		this.id=paramInt;
		this.result=paramObject;
	}
	/* properties */

	public int getId()
	{
		return this.id;
	}

	public Object getResult()
	{
		return this.result;
	}

	/* init start */

	/* methods */

	public void access()
	{
	}
	/* common methods */

	/* inner class */

}
