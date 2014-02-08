/**
 * 
 */
package com.cambrian;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import com.cambrian.common.log.Logger;
import com.cambrian.common.net.Command;
import com.cambrian.common.net.DataAccessHandler;
import com.cambrian.common.net.ProxyDataHandler;
import com.cambrian.common.net.client.ClientConnectManager;
import com.cambrian.common.net.server.TcpConnectServer;
import com.cambrian.common.thread.TaskPoolExecutor;
import com.cambrian.common.util.FileMonitor;
import com.cambrian.common.util.TextKit;
import com.cambrian.common.xml.Context;
import com.cambrian.common.xml.XmlContext;
import com.sun.net.httpserver.HttpServer;

/**
 * 类说明：
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class Main implements Runnable
{

	/** 日志 */
	private static Logger log=Logger.getLogger(Main.class);

	/** 加载内容 */
	public static Context context;

	/** 运行 */
	public void run()
	{
		try
		{
			ClientConnectManager factory=ClientConnectManager.getFactory();
			TaskPoolExecutor executorPool=new TaskPoolExecutor();
			factory.setExecutor(executorPool);
 
			// init clientService
			ProxyDataHandler clientService=new ProxyDataHandler();
			// int port=DataAccessHandler.getInstance()
			// .getAccessReturnPort();
			short report=4;
			Command cmd=DataAccessHandler.getInstance();
			clientService.setPort((short)report,cmd);
			factory.setTransmitHandler(clientService);

			// init service
			ProxyDataHandler service=new ProxyDataHandler();

			Context baseContext=new Context(); 
			baseContext.set("service",service);
			baseContext.set("clientService",clientService);
			// baseContext.set("httpService",httpProxy);

			checkAddress();
			int port=TextKit.parseInt(System.getProperty("serverPort"));
			int httpPort=TextKit.parseInt(System
				.getProperty("httpServerPort"));
			String host=System.getProperty("localAddress");
			String str=System.getProperty("serialExecute");
			boolean serialExecute=false;
			if(str!=null) serialExecute=TextKit.parseBoolean(str);

			TcpConnectServer server=new TcpConnectServer();
			server.setExecutor(executorPool);
			server.setTransmitHandler(service);
			server.setSerialExecute(serialExecute);
			server.open(host,port);
			if(log.isInfoEnabled()) log.info(server);

			HttpServer httpServer=HttpServer.create(new InetSocketAddress(
				host,httpPort),0);
			httpServer.createContext("/",service);
			httpServer.setExecutor(null); // creates a default executor
			httpServer.start();

			// TODO 考虑线程
			XmlContext context=new XmlContext(baseContext);
			Main.context=loadXmls(context);
			monitorXmls();

			factory.loop();
		}
		catch(Throwable localThrowable)
		{
			if(log.isWarnEnabled()) log.warn("run error, ",localThrowable);
			System.exit(0);
		}
	}

	void checkAddress()
	{
		String str=System.getProperty("serverPort");
		if(str==null)
			throw new IllegalArgumentException(super.getClass().getName()
				+" start, null serverPort property");
		String host=System.getProperty("localAddress");
		if(host==null)
		{
			try
			{
				host=InetAddress.getLocalHost().getHostAddress();
				System.setProperty("localAddress",host);
			}
			catch(Exception localException1)
			{
				throw new RuntimeException(super.getClass().getName()
					+" start, localAddress get error",localException1);
			}
		}
	}

	/** 加载配置表 */
	Context loadXmls(XmlContext context)
	{
		String loadXmls=System.getProperty("loadXmls");
		if(loadXmls==null||loadXmls.isEmpty()) return context;
		String[] xmls=TextKit.split(loadXmls,':');
		for(int i=0;i<xmls.length;i++)
		{
			XmlContext.setXmlContext(xmls[i],context);
//			long time=TimeKit.nowTimeMills();
			context.parse(xmls[i]);
			if(i<xmls.length-1)
			{
				XmlContext xContext=new XmlContext(context.getBaseContext());
				xContext.setContext(context);
				context=xContext;
			}
//			System.out.println(xmls[i]+"--- "+(TimeKit.nowTimeMills()-time));
		}
		return context;
	}

	/** 实时监控配置表 */
	void monitorXmls()
	{
		String monitorXmls=System.getProperty("monitorXmls");
		if(monitorXmls==null||monitorXmls.isEmpty()) return;
		String[] xmls=TextKit.split(monitorXmls,':');
		XmlContext context=null;
		for(int i=0;i<xmls.length;i++)
		{
			context=(XmlContext)XmlContext.getXmlContext(xmls[i]);
			if(context==null)
			{
				context=new XmlContext(Main.context.getBaseContext());
				context.setContext(Main.context);
				XmlContext.setXmlContext(xmls[i],context);
				Main.context=context;
			}
			FileMonitor.add(xmls[i],context);
		}
		FileMonitor.getInstance().timerStart();
	}
	/* common methods */

	/* inner class */

}
