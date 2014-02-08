/**
 * 
 */
package com.cambrian.common.net;

import com.cambrian.common.util.ChangeListenerList;

public abstract class ConnectFactory extends ChangeListenerList implements
	Runnable
{

	protected static ConnectFactory factory;

	public static ConnectFactory getFactory()
	{
		return factory;
	}

	public static NioTcpConnect checkConnect(URL paramURL)
	{
		if(factory==null)
		{
			NioTcpConnect localConnect=new NioTcpConnect();
			localConnect.open(paramURL);
			return localConnect;
		}
		return factory.checkInstance(paramURL);
	}

	public static NioTcpConnect getConnect(URL paramURL)
	{
		if(factory==null)
		{
			NioTcpConnect localConnect=new NioTcpConnect();
			localConnect.open(paramURL);
			return localConnect;
		}
		return factory.getInstance(paramURL);
	}

	public static NioTcpConnect openConnect(URL paramURL)
	{
		if(factory==null)
		{
			NioTcpConnect localConnect=new NioTcpConnect();
			localConnect.open(paramURL);
			return localConnect;
		}
		return factory.openInstance(paramURL);
	}

	public abstract int size();

	public abstract NioTcpConnect[] getConnects();

	public abstract NioTcpConnect checkInstance(URL paramURL);

	public abstract NioTcpConnect getInstance(URL paramURL);

	public abstract NioTcpConnect openInstance(URL paramURL);
}