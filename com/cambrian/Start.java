/**
 * 
 */
package com.cambrian;

import java.io.File;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.log4j.PropertyConfigurator;

import com.cambrian.common.log.Logger;
import com.cambrian.common.system.SystemPropertiesHandler;

/**
 * ��˵����������
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public final class Start
{

	/* static fields */
	/** ��־ */
	private static Logger log=Logger.getLogger(Start.class);

	/* static methods */
	/** ��������� */
	public static void main(String[] args)
	{
		if(args.length==0)
		{
			System.err.println(" need setProperties.");
			return;
		}
		new Start(args[0]);
	}

	/* fields */

	/* constructors */
	/**  */
	private Start(String str)
	{
		// Ĭ��ϵͳ����
		Properties properties=System.getProperties();

		// System.err.println("----------------------------------------------");
		// for(Object argument:properties.keySet())
		// {
		// System.err.println(argument.toString()+"="
		// +properties.get(argument).toString());
		// }
		// System.err.println("----------------------------------------------");
		// �����õ�ϵͳ����
		properties.putAll(SystemPropertiesHandler
			.getSystemProperties(new File(str)));
		initLog4J();
		initLocale();
		if(log.isInfoEnabled())
		{
			log.info("----------------------------------------------");
			for(Object argument:properties.keySet())
			{
				log.info(argument.toString()+"="
					+properties.get(argument).toString());
			}
			log.info("----------------------------------------------");
		}
		init();
	}
	/* properties */

	/* init start */
	/** ��ʼ����־ */
	private void initLog4J()
	{
		String str=System.getProperty("logProperties");
		if(str==null)
		{
			System.err.println(" need initLog4J.");
			return;
		}
		PropertyConfigurator.configure(str);
	}
	/** ���ػ� */
	private void initLocale()
	{
		String timezone=System.getProperty("user.timezone");
		if(timezone!=null)
			TimeZone.setDefault(TimeZone.getTimeZone(timezone));
		String language=System.getProperty("user.language");
		String country=System.getProperty("user.country");
		String variant=System.getProperty("user.variant");
		// PrintfKit.println(timezone,language,country,variant);
		// PrintfKit.println(TimeZone.getDefault(),Locale.getDefault());
		Locale localLocale=null;
		if(language!=null)
		{
			if(country!=null)
			{
				if(variant!=null)
					localLocale=new Locale(language,country,variant);
				else
					localLocale=new Locale(language,country);
			}
			else
				localLocale=new Locale(language);
		}
		if(localLocale!=null) Locale.setDefault(localLocale);
		// PrintfKit.println(TimeZone.getDefault(),Locale.getDefault());
	}
	/** ��ʼ������ */
	private void init()
	{
		// System.err.println("-------------------------------");
		// ��һ�����ص���
		String value=System.getProperty("start.run");
		// System.err.println(value);
		try
		{
			@SuppressWarnings("unchecked")
			Class<Runnable> localClass=(Class<Runnable>)Class.forName(value);
			Runnable localRunnable=localClass.newInstance();
			// System.err.println(localRunnable);
			localRunnable.run();
		}
		catch(Exception localException)
		{
			throw new RuntimeException(" run, load fail! "+localException);
			// localException.printStackTrace();
			// return;
		}
	}
	/* methods */

	/* common methods */

	/* inner class */

}
