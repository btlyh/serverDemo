/**
 * 
 */
package com.cambrian.common.net;

/**
 * 类说明：
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public interface TransmitHandler
{

	/* 命令 */
	/** 心跳命令 */
	public static final short CMD_HEART=0x01;
	/** 心跳返回命令 */
	public static final short CMD_HEART_RES=0x02;

	/** 用户注册命令 */
	public static final short CMD_USER_REGISTER=0x0101;
	/** 用户注册返回命令 */
	public static final short CMD_USER_REGISTER_RES=0x0102;
	/** 用户登陆命令 */
	public static final short CMD_USER_LOGIN=0x0103;
	/** 用户登陆返回命令 */
	public static final short CMD_USER_LOGIN_RES=0x0104;

	/** 用户创建角色命令 */
	public static final short CMD_PLAYER_CREATE=0x0201;
	/** 用户创建角色返回命令 */
	public static final short CMD_PLAYER_CREATE_RES=0x0202;

	/** 获得大厅列表 */
	public static final short CMD_LOBBY_LIST=0x0301;
	/** 获得大厅列表返回 */
	public static final short CMD_LOBBY_LIST_RES=0x0302;
	/** 进入大厅 */
	public static final short CMD_LOBBY_JOIN=0x0303;
	/** 进入大厅返回 */
	public static final short CMD_LOBBY_JOIN_RES=0x0304;
	/** 退出大厅 */
	public static final short CMD_LOBBY_QUIT=0x0305;
	/** 退出大厅返回 */
	public static final short CMD_LOBBY_QUIT_RES=0x0306;
	/** 切换大厅 */
	public static final short CMD_LOBBY_SWITCH=0x0307;
	/** 切换大厅返回 */
	public static final short CMD_LOBBY_SWITCH_RES=0x0308;
	/** 获得大厅赛场列表 */
	public static final short CMD_LOBBY_MATCHLIST=0x0309;
	/** 获得大厅赛场列表返回 */
	public static final short CMD_LOBBY_MATCHLIST_RES=0x030a;

	/** 创建赛场 */
	public static final short CMD_MATCH_CREATE=0x0401;
	/** 创建赛场返回 */
	public static final short CMD_MATCH_CREATE_RES=0x0402;
	/** 进入赛场 */
	public static final short CMD_MATCH_JOIN=0x0403;
	/** 进入赛场返回 */
	public static final short CMD_MATCH_JOIN_RES=0x0404;
	/** 退出赛场 */
	public static final short CMD_MATCH_QUIT=0x0405;
	/** 退出赛场返回 */
	public static final short CMD_MATCH_QUIT_RES=0x0406;
	/** 赛场更新 */
	public static final short CMD_MATCH_UPDATE=0x0408;
	/** 赛场玩家更新 */
	public static final short CMD_MATCH_UPDATEPLAYER=0x040a;
	/** 赛场比赛开始 */
	public static final short CMD_MATCH_START=0x040b;
	/** 赛场比赛开始返回 */
	public static final short CMD_MATCH_START_RES=0x040c;

	/** 赛车游戏获取当前场景 */
	public static final short CMD_RACING_SCENE=0x0501;
	/** 赛车游戏获取当前场景返回 */
	public static final short CMD_RACING_SCENE_RES=0x0502;
	/** 赛车游戏速度变化 */
	public static final short CMD_RACING_SPEED=0x0503;
	/** 赛车游戏速度变广播到前台 */
	public static final short CMD_RACING_SPEED_RES=0x0504;
	/** 赛车游戏赛道变化 */
	public static final short CMD_RACING_LOCATION=0x0505;
	/** 赛车游戏赛道变化广播到前台 */
	public static final short CMD_RACING_LOCATION_RES=0x0506;
	/** 赛车游戏碰撞 */
	public static final short CMD_RACING_COLLISION=0x0507;
	/** 赛车游戏碰撞广播到前台 */
	public static final short CMD_RACING_COLLISION_RES=0x0508;
	/** 赛车游戏游戏结束 */
	public static final short CMD_RACING_OVER=0x050a;

	/** 聊天服务器接收消息 */
	public static final short CMD_CHAT_RECEIVE=0x0601;
	/** 聊天服务器发送消息 */
	public static final short CMD_CHAT_RECEIVE_REC=0x0602;

	/** 通信处理 */
	public void transmit(int uuid,NioTcpConnect connect,ByteBuffer data);
}
