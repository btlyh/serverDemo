package com.cambrian.common.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import com.cambrian.common.log.Logger;

/**
 * 类说明：http请求及返回
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class HttpRequester
{

	/* static fields */
	/** 日志 */
	private static Logger log=Logger.getLogger(HttpRequester.class);

	/* static methods */
	/**
	 * 发送GET请求
	 * 
	 * @param urlString URL地址
	 * @return 响应对象
	 * @throws IOException
	 */
	public static String sendGet(String urlString)
	{
		return send(urlString,"GET",null,null);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param urlString URL地址
	 * @param params 参数集合
	 * @return 响应对象
	 * @throws IOException
	 */
	public static String sendPost(String urlString,Map<String,String> params)
	{
		return send(urlString,"POST",params,null);
	}
	/**
	 * 发送HTTP请求
	 * 
	 * @param urlString
	 * @return 响映对象
	 * @throws IOException
	 */
	private static String send(String urlString,String method,
		Map<String,String> parameters,Map<String,String> propertys)
	{
		HttpURLConnection urlConnection=null;

		if(method.equalsIgnoreCase("GET")&&parameters!=null)
		{
			StringBuffer param=new StringBuffer();
			int i=0;
			for(String key:parameters.keySet())
			{
				if(i==0)
					param.append("?");
				else
					param.append("&");
				param.append(key).append("=").append(parameters.get(key));
				i++;
			}
			urlString+=param;
		}
		try
		{
			URL url=new URL(urlString);
			urlConnection=(HttpURLConnection)url.openConnection();
			urlConnection.setRequestMethod(method);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			if(propertys!=null) for(String key:propertys.keySet())
			{
				urlConnection.addRequestProperty(key,propertys.get(key));
			}
			if(method.equalsIgnoreCase("POST")&&parameters!=null)
			{
				StringBuffer param=new StringBuffer();
				for(String key:parameters.keySet())
				{
					param.append("&");
					param.append(key).append("=")
						.append(parameters.get(key));
				}
				urlConnection.getOutputStream().write(
					param.toString().getBytes());
				urlConnection.getOutputStream().flush();
				urlConnection.getOutputStream().close();
			}

			InputStream in=urlConnection.getInputStream();
			BufferedReader bufferedReader=new BufferedReader(
				new InputStreamReader(in));
			StringBuffer temp=new StringBuffer();
			String line=bufferedReader.readLine();
			while(line!=null)
			{
				temp.append(line).append("\r\n");
				line=bufferedReader.readLine();
			}
			bufferedReader.close();

			String ecod=urlConnection.getContentEncoding();
			if(ecod==null) ecod=Charset.defaultCharset().name();
			urlConnection.getInputStream().close();
			return new String(temp.toString().getBytes(),ecod);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			log.warn("send to web error, urlStr="+urlString);
			return "";
		}
	}

	/* fields */

	/* constructors */

	/* properties */

	/* init start */

	/* methods */

	/* common methods */

	/* inner class */

}