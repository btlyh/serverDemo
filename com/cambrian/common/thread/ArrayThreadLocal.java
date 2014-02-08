/**
 * 
 */
package com.cambrian.common.thread;

@SuppressWarnings("rawtypes")
public final class ArrayThreadLocal extends ThreadLocal
{

	public static final int CAPACITY=32;
	private static final ThreadLocal instance=new ArrayThreadLocal();
	boolean[] booleanArray;
	byte[] byteArray;
	char[] charArray;
	short[] shortArray;
	int[] intArray;
	long[] longArray;
	float[] floatArray;
	double[] doubleArray;
	String[] stringArray;

	public static boolean[] getBooleanArray()
	{
		return getBooleanArray(32);
	}

	public static boolean[] getBooleanArray(int paramInt)
	{
		ArrayThreadLocal localArrayThreadLocal=(ArrayThreadLocal)instance
			.get();
		if((localArrayThreadLocal.booleanArray==null)
			||(localArrayThreadLocal.booleanArray.length<paramInt))
			localArrayThreadLocal.booleanArray=new boolean[paramInt];
		return localArrayThreadLocal.booleanArray;
	}

	public static byte[] getByteArray()
	{
		return getByteArray(32);
	}

	public static byte[] getByteArray(int paramInt)
	{
		ArrayThreadLocal localArrayThreadLocal=(ArrayThreadLocal)instance
			.get();
		if((localArrayThreadLocal.byteArray==null)
			||(localArrayThreadLocal.byteArray.length<paramInt))
			localArrayThreadLocal.byteArray=new byte[paramInt];
		return localArrayThreadLocal.byteArray;
		// ByteBuffer bb=new ByteBuffer();
		// bb.writeByte(0);
		// bb.writeInt(123);
		// bb.writeShort(333);
		// bb.writeInt(3333);
		// return bb.toArray();
	}

	public static char[] getCharArray()
	{
		return getCharArray(32);
	}

	public static char[] getCharArray(int paramInt)
	{
		ArrayThreadLocal localArrayThreadLocal=(ArrayThreadLocal)instance
			.get();
		if((localArrayThreadLocal.charArray==null)
			||(localArrayThreadLocal.charArray.length<paramInt))
			localArrayThreadLocal.charArray=new char[paramInt];
		return localArrayThreadLocal.charArray;
	}

	public static short[] getShortArray()
	{
		return getShortArray(32);
	}

	public static short[] getShortArray(int paramInt)
	{
		ArrayThreadLocal localArrayThreadLocal=(ArrayThreadLocal)instance
			.get();
		if((localArrayThreadLocal.shortArray==null)
			||(localArrayThreadLocal.shortArray.length<paramInt))
			localArrayThreadLocal.shortArray=new short[paramInt];
		return localArrayThreadLocal.shortArray;
	}

	public static int[] getIntArray()
	{
		return getIntArray(32);
	}

	public static int[] getIntArray(int paramInt)
	{
		ArrayThreadLocal localArrayThreadLocal=(ArrayThreadLocal)instance
			.get();
		if((localArrayThreadLocal.intArray==null)
			||(localArrayThreadLocal.intArray.length<paramInt))
			localArrayThreadLocal.intArray=new int[paramInt];
		return localArrayThreadLocal.intArray;
	}

	public static long[] getLongArray()
	{
		return getLongArray(32);
	}

	public static long[] getLongArray(int paramInt)
	{
		ArrayThreadLocal localArrayThreadLocal=(ArrayThreadLocal)instance
			.get();
		if((localArrayThreadLocal.longArray==null)
			||(localArrayThreadLocal.longArray.length<paramInt))
			localArrayThreadLocal.longArray=new long[paramInt];
		return localArrayThreadLocal.longArray;
	}

	public static float[] getFloatArray()
	{
		return getFloatArray(32);
	}

	public static float[] getFloatArray(int paramInt)
	{
		ArrayThreadLocal localArrayThreadLocal=(ArrayThreadLocal)instance
			.get();
		if((localArrayThreadLocal.floatArray==null)
			||(localArrayThreadLocal.floatArray.length<paramInt))
			localArrayThreadLocal.floatArray=new float[paramInt];
		return localArrayThreadLocal.floatArray;
	}

	public static double[] getDoubleArray()
	{
		return getDoubleArray(32);
	}

	public static double[] getDoubleArray(int paramInt)
	{
		ArrayThreadLocal localArrayThreadLocal=(ArrayThreadLocal)instance
			.get();
		if((localArrayThreadLocal.doubleArray==null)
			||(localArrayThreadLocal.doubleArray.length<paramInt))
			localArrayThreadLocal.doubleArray=new double[paramInt];
		return localArrayThreadLocal.doubleArray;
	}

	public static String[] getStringArray()
	{
		return getStringArray(32);
	}

	public static String[] getStringArray(int paramInt)
	{
		ArrayThreadLocal localArrayThreadLocal=(ArrayThreadLocal)instance
			.get();
		if((localArrayThreadLocal.stringArray==null)
			||(localArrayThreadLocal.stringArray.length<paramInt))
			localArrayThreadLocal.stringArray=new String[paramInt];
		return localArrayThreadLocal.stringArray;
	}

	protected Object initialValue()
	{
		return new ArrayThreadLocal();
	}
}