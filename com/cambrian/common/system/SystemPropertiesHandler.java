/**
 * 
 */
package com.cambrian.common.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class SystemPropertiesHandler
{

	/* static fields */

	/* static methods */
	public static Map<String,String> getSystemProperties(File propertiesFile)
	{
		Map<String,String> propertyMap=new HashMap<String,String>();
		if(!propertiesFile.isFile())
		{
			return propertyMap;
		}
		Properties properties=new Properties();
		try
		{
			FileInputStream inStream=new FileInputStream(propertiesFile);
			try
			{
				properties.load(inStream);
			}
			finally
			{
				inStream.close();
			}
		}
		catch(IOException e)
		{
			throw new RuntimeException("Error when loading properties file="
				+propertiesFile,e);
		}

		for(Object argument:properties.keySet())
		{
			// System.err.println(argument.toString()+"="
			// +properties.get(argument).toString());
			propertyMap.put(argument.toString(),properties.get(argument)
				.toString());
		}
		return propertyMap;
	}
	/* fields */

	/* constructors */

	/* properties */

	/* init start */

	/* methods */

	/* common methods */

	/* inner class */

}
