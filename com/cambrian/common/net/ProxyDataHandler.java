package com.cambrian.common.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.cambrian.common.log.Logger;
import com.cambrian.common.util.StringKit;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * 类说明：数据分发器
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class ProxyDataHandler implements HttpHandler
{

	/* static fields */

	private static final Logger log=Logger.getLogger(ProxyDataHandler.class);
	public static final int ECHO_PORT=1;
	public static final int PING_PORT=2;
	public static final int ACCESS_RETURN_PORT=4;
	public static final int TIME_PORT=6;
	public static final int ATTRIBUTE_PORT=11;
	public static final int FILE_PORT=21;
	public static final int AUTHORIZED_FILE_PORT=22;
	public static final int CC_CERTIFY_PORT=101;
	public static final int CC_LOAD_PORT=102;
	public static final int CC_ACTIVE_PORT=103;
	public static final int CC_EXIT_PORT=104;
	public static final int CC_SHUTDOWN_PORT=0x6a;
	public static final int CC_REGIST_PORT=0x6b;
	public static final int DC_LOGIN_PORT=111;
	public static final int DC_LOAD_PORT=112;
	public static final int DC_SAVE_PORT=113;
	public static final int DC_UPDATE_PORT=121;
	public static final int CERTIFY_CODE_PORT=201;
	public static final int CERTIFY_PROXY_PORT=202;
	public static final int LOGIN_PORT=211;
	public static final int LOAD_PORT=212;
	public static final int EXIT_PORT=213;
	public static final int CLL_PORT=214;
	public static final int PROXY_ECHO_PORT=301;
	public static final int PROXY_PING_PORT=302;
	public static final int PROXY_TIME_PORT=306;
	public static final int PROXY_STATE_PORT=310;
	public static final int PROXY_LOGIN_PORT=402;
	public static final int PROXY_EXIT_PORT=404;
	public static final int CONNECT_REGISTER_PORT=601;
	public static final int ADVISE_OFFLINE_PORT=701;
	public static final int SERVER_LIST_PORT=801;
	public static final int HANDLER_CHANGED=0;
	public static final int PORT_CHANGED=1;

	/* static methods */

	/* fields */
	/** 过滤器 */
	Command filter;
	/** 命令处理器 */
	Command[] handlers=new Command[0xffff];

	/** 命令处理器 */
	Map<String,HttpCommand> httpHandlers=new HashMap<String,HttpCommand>();

	/* constructors */

	/* properties */

	public void setFilter(Command filter)
	{
		this.filter=filter;
	}

	public Command getPort(int command)
	{
		return this.handlers[command];
	}

	public void setPort(int command,Command handler)
	{
		if(log.isDebugEnabled())
			log.debug(",setport ,command="+command+",handler="+handler);
		System.err.println(",setport ,command="+command+",handler="+handler);
		this.handlers[command]=handler;
	}
	/**  */
	public HttpCommand getHttpCommand(String command)
	{
		return httpHandlers.get(command);
	}

	/**  */
	public void setHttpCommand(String command,HttpCommand handler)
	{
		if(httpHandlers.get(command)!=null)
			throw new DataAccessException(500,
				"setHttpCommand error,double command="+command);
		httpHandlers.put(command,handler);
	}

	/* init start */

	/* methods */
	/** 消息分发 */
	public void transmit(NioTcpConnect connect,ByteBuffer data)
	{
		byte hlen=data.readByte();// 1 头长度
		byte version=data.readByte();// 1 协议版本号
		int uid=data.readInt();// 4 流水号
		int cmd=data.readUnsignedShort(); // 2 command
		int check=data.readInt(); // 4 CRC32
		int recmd=(hlen==18)?data.readUnsignedShort():0;// 2 长头才读
		int reuuid=(hlen==18)?data.readInt():0;// 4 长头才读
		// -----------------------------
		if(log.isDebugEnabled())
			log.debug("receive, version="+version+", uid="+uid+", command="
				+cmd);

		if(filter!=null) filter.transmit(connect,data);
		Command handler=getPort(cmd);
		if(log.isDebugEnabled())
			log.debug("transmit, command="+cmd+", check="+check+", handler="
				+handler);
		if(handler==null) return;
		try
		{
			if(hlen==18)
			{
				// check
				handler.transmit(recmd,reuuid,connect,data);
			}
			else if(hlen==12)
			{
				// check
				handler.transmit(connect,data);
			}
		}
		catch(Throwable t)
		{
			if(log.isWarnEnabled())
				log.warn("transmit error, port="+cmd+", "+connect+", "
					+handler,t);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.
	 * HttpExchange)
	 */
	public void handle(HttpExchange httpExchange) throws IOException
	{
		URI uri=httpExchange.getRequestURI();
		if(log.isDebugEnabled())
			log.debug("["+uri.getScheme()+":][//"+uri.getAuthority()+"]["
				+uri.getPath()+"][?"+uri.getQuery()+"][#"+uri.getFragment());
		// System.err.println("["+uri.getScheme()+":][//"+uri.getAuthority()+"]["+uri.getPath()+"][?"+uri.getQuery()+"][#"+uri.getFragment());
		// System.err.println("["+uri.getUserInfo()+"@]"+uri.getHost()+"[:"+uri.getPort()+"]");

		HttpCommand command=httpHandlers.get(uri.getPath());
		if(command==null)
		{
			if(log.isWarnEnabled())
				log.warn("error,command="+uri.getPath()+",null");
			outPut(httpExchange,"null");
		}
		else
		{
			command.handle(httpExchange,StringKit.parseMap(uri.getQuery()));
		}
	}
	/**
	 * @throws IOException
	 */
	private void outPut(HttpExchange httpExchange,String value)
		throws IOException
	{
		byte[] bb=value.getBytes();
		httpExchange.sendResponseHeaders(200,bb.length);
		OutputStream os=httpExchange.getResponseBody();
		os.write(bb);
		os.close();
	}
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("HANDLERS:");
		for(int i=0;i<handlers.length;i++)
		{
			if(handlers[i]!=null)
				sb.append(" [").append(i).append(": ").append(handlers[i])
					.append(']');
		}
		return sb.toString();
	}
	/* common methods */

	/* inner class */

}
