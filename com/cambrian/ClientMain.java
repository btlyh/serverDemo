/**
 * 
 */
package com.cambrian;

import com.cambrian.common.log.Logger;
import com.cambrian.common.net.ProxyDataHandler;
import com.cambrian.common.net.client.ClientConnectManager;
import com.cambrian.common.thread.TaskPoolExecutor;
import com.cambrian.common.xml.Context;
import com.cambrian.common.xml.XmlContext;

/**
 * 类说明：
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class ClientMain implements Runnable
{

	/* static fields */
	/** 日志 */
	private static Logger log=Logger.getLogger(ClientMain.class);

	/* static methods */
	Context context;

	/* fields */

	/* constructors */

	/* properties */

	/* init start */

	/* methods */

	public void run()
	{
		try
		{
			ClientConnectManager connectFactory=ClientConnectManager
				.getFactory();
			TaskPoolExecutor localTaskPoolExecutor=new TaskPoolExecutor();
			connectFactory.setExecutor(localTaskPoolExecutor);
			// clientService
			ProxyDataHandler clientService=new ProxyDataHandler();
			connectFactory.setTransmitHandler(clientService);

			Context basicContext=new Context();
			basicContext.set("clientService",clientService);
			context=new XmlContext(basicContext);
			// ClientLoadContext.loadContext.setClientService(clientService);
			// TODO 考虑线程
			// ClientLoadContext.loadContext.init();
			connectFactory.loop();

		}
		catch(Throwable localThrowable)
		{
			if(log.isWarnEnabled()) log.warn("run error, ",localThrowable);
			System.exit(0);
		}
	}

	/* common methods */

	/* inner class */

}
