package com.cambrian.common.net;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import com.sun.net.httpserver.HttpExchange;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 1.0
 * @date 2013-6-24
 * @author maxw<woldits@qq.com>
 */
public abstract class HttpCommand
{

	/* static fields */

	/* static methods */

	/* fields */

	/* constructors */

	/* properties */

	/* init start */

	/* methods */
	/**  */
	public abstract void handle(HttpExchange httpExchange,
		Map<String,String> map);
	/**
	 * @throws IOException
	 */
	protected void outPut(HttpExchange httpExchange,String value)
	{
		byte[] bb=value.getBytes();
		try
		{
			httpExchange.sendResponseHeaders(200,bb.length);
			OutputStream os=httpExchange.getResponseBody();
			os.write(bb);
			os.close();
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/* common methods */

	/* inner class */
}
