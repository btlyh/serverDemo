/**
 * 
 */
package com.cambrian.common.net;

public class DataAccessException extends RuntimeException
{

	private static final long serialVersionUID=-5937612350072067603L;
	// public static final int OK=200;
	public static final int SERVER_REDIRECT=300;
	public static final int CLIENT_INTERNAL_ERROR=400;
	public static final int CLIENT_PARAMETER_ERROR=410;
	public static final int CLIENT_IO_ERROR=420;
	public static final int CLIENT_TIMEOUT=440;
	public static final int CLIENT_SDATA_ERROR=450;
	public static final int CLIENT_SMSG_ERROR=451;
	public static final int SERVER_INTERNAL_ERROR=500;
	public static final int SERVER_CDATA_ERROR=550;
	public static final int SERVER_CMSG_ERROR=551;
	public static final int SERVER_ACCESS_REFUSED=560;
	public static final int SERVER_FILE_NOT_FOUND=570;
	public static final int CUSTOM_ERROR=600;
	private int type;
	private String[] variables;
	private String address;

	public static String typeMessage(int paramInt)
	{
		switch(paramInt)
		{
			case 300:
				return "SERVER_REDIRECT";
			case 400:
				return "CLIENT_INTERNAL_ERROR";
			case 410:
				return "CLIENT_PARAMETER_ERROR";
			case 420:
				return "CLIENT_IO_ERROR";
			case 440:
				return "CLIENT_TIMEOUT";
			case 450:
				return "CLIENT_SDATA_ERROR";
			case 451:
				return "CLIENT_SMSG_ERROR";
			case 500:
				return "SERVER_INTERNAL_ERROR";
			case 550:
				return "SERVER_CDATA_ERROR";
			case 551:
				return "SERVER_CMSG_ERROR";
			case 560:
				return "SERVER_ACCESS_REFUSED";
			case 570:
				return "SERVER_FILE_NOT_FOUND";
			case 600:
				return "CUSTOM_ERROR";
		}
		return null;
	}

	public DataAccessException(int paramInt,String paramString)
	{
		this(paramInt,paramString,null,null);
	}

	public DataAccessException(int paramInt,String paramString,
		String[] paramArrayOfString)
	{
		this(paramInt,paramString,paramArrayOfString,null);
	}

	public DataAccessException(int paramInt,String paramString1,
		String[] paramArrayOfString,String paramString2)
	{
		super(paramString1);
		this.type=paramInt;
		this.variables=paramArrayOfString;
		this.address=paramString2;
	}

	public int getType()
	{
		return this.type;
	}

	public String getTypeMessage()
	{
		return typeMessage(this.type);
	}

	public String[] getVariables()
	{
		return this.variables;
	}

	public void setVariables(String[] paramArrayOfString)
	{
		this.variables=paramArrayOfString;
	}

	public String getAddress()
	{
		return this.address;
	}

	public void setAddress(String paramString)
	{
		this.address=paramString;
	}

	public String toString()
	{
		String str=typeMessage(this.type);
		if(str==null) str=Integer.toString(this.type);
		return getClass().getName()+":"+str+", "+getMessage()+", variables="
			+this.variables+", address="+this.address;
	}
}