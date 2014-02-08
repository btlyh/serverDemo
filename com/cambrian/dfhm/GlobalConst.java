package com.cambrian.dfhm;

/**
 * 类说明：常量
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public final class GlobalConst
{

	/** 命令处理器改变 */
	public static final int HANDLER_CHANGED=0;
	/** 命令处理器接收端口改变 */
	public static final int PORT_CHANGED=1;

	/* 通信命令号 */
	/** 一般返回消息接收端口 */
	public static final int ACCESS_RETURN_PORT=4;
	/** 获取服务器时间 */
	public static final int TIME_PORT=6;
	/**  */
	public static final int ATTRIBUTE_PORT=11;
	/**  */
	public static final int FILE_PORT=21;
	/**  */
	public static final int AUTHORIZED_FILE_PORT=22;
	/** 认证-获取sid */
	public static final int CC_CERTIFY_PORT=101;
	/** 认证-加载用户数据 */
	public static final int CC_LOAD_PORT=102;
	/** 认证-保持用户活跃 */
	public static final int CC_ACTIVE_PORT=103;
	/** 认证-用户退出 */
	public static final int CC_EXIT_PORT=104;
	/** 认证-服务关闭 */
	public static final int CC_SHUTDOWN_PORT=0x6a;
	/** 认证-用户注册 */
	public static final int CC_REGIST_PORT=0x6b;

	/** 数据中心-玩家登陆 */
	public static final int DC_LOGIN_PORT=111;
	/** 数据中心-加载玩家数据 */
	public static final int DC_LOAD_PORT=112;
	/** 数据中心-保持玩家数据 */
	public static final int DC_SAVE_PORT=113;
	/** 数据中心-检查昵称 */
	public static final int DC_CHECKNICKNAME_PORT=114;
	/** 数据中心-获取随机名字 */
	public static final int DC_RANDOMNAME_PORT=115;
	/** 数据中心-更新玩家数据 */
	public static final int DC_UPDATE_PORT=121;
	/**  */
	public static final int CERTIFY_CODE_PORT=201;
	/**  */
	public static final int CERTIFY_PROXY_PORT=202;
	/** 数据服务端-玩家登陆 */
	public static final int LOGIN_PORT=211;
	/** 数据服务端-加载玩家数据 */
	public static final int LOAD_PORT=212;
	/** 数据服务端-玩家退出 */
	public static final int EXIT_PORT=213;
	/** 数据服务端-登陆并获取玩家数据 */
	public static final int COMMAND_CLL_PORT=214;
	/** 数据服务端-获取随机名字 */
	public static final int RANDOMNAME_PORT=215;
	/** 数据服务端-注册*/
	public static final int REG_PORT=0xD8;
	/**  */
	public static final int PROXY_ECHO_PORT=301;
	/**  */
	public static final int PROXY_PING_PORT=302;
	/**  */
	public static final int PROXY_TIME_PORT=306;
	/**  */
	public static final int PROXY_STATE_PORT=310;
	/**  */
	public static final int PROXY_LOGIN_PORT=402;
	/**  */
	public static final int PROXY_EXIT_PORT=404;
	/**  */
	public static final int CONNECT_REGISTER_PORT=601;
	/**  */
	public static final int ADVISE_OFFLINE_PORT=701;
	/**  */
	public static final int SERVER_LIST_PORT=801;

	public static final int SS_CHECK_PORT=901;

	/* 通信命令号 */
	/** 反射,回应ping消息 */
	public static final int ECHO_PORT=1;
	/** 发送ping消息 */
	public static final int PING_PORT=2;


	/* 外部WEB数据修改端口 */
	public static final int UPDATE_BASE=10001;
	public static final int UPDATE_PRO=10002;
	public static final int UPDATE_CARD=10003;
}
