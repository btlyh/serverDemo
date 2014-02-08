/**
 * 
 */
package com.cambrian.common.net;

/**
 * ��˵����
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public interface TransmitHandler
{

	/* ���� */
	/** �������� */
	public static final short CMD_HEART=0x01;
	/** ������������ */
	public static final short CMD_HEART_RES=0x02;

	/** �û�ע������ */
	public static final short CMD_USER_REGISTER=0x0101;
	/** �û�ע�᷵������ */
	public static final short CMD_USER_REGISTER_RES=0x0102;
	/** �û���½���� */
	public static final short CMD_USER_LOGIN=0x0103;
	/** �û���½�������� */
	public static final short CMD_USER_LOGIN_RES=0x0104;

	/** �û�������ɫ���� */
	public static final short CMD_PLAYER_CREATE=0x0201;
	/** �û�������ɫ�������� */
	public static final short CMD_PLAYER_CREATE_RES=0x0202;

	/** ��ô����б� */
	public static final short CMD_LOBBY_LIST=0x0301;
	/** ��ô����б��� */
	public static final short CMD_LOBBY_LIST_RES=0x0302;
	/** ������� */
	public static final short CMD_LOBBY_JOIN=0x0303;
	/** ����������� */
	public static final short CMD_LOBBY_JOIN_RES=0x0304;
	/** �˳����� */
	public static final short CMD_LOBBY_QUIT=0x0305;
	/** �˳��������� */
	public static final short CMD_LOBBY_QUIT_RES=0x0306;
	/** �л����� */
	public static final short CMD_LOBBY_SWITCH=0x0307;
	/** �л��������� */
	public static final short CMD_LOBBY_SWITCH_RES=0x0308;
	/** ��ô��������б� */
	public static final short CMD_LOBBY_MATCHLIST=0x0309;
	/** ��ô��������б��� */
	public static final short CMD_LOBBY_MATCHLIST_RES=0x030a;

	/** �������� */
	public static final short CMD_MATCH_CREATE=0x0401;
	/** ������������ */
	public static final short CMD_MATCH_CREATE_RES=0x0402;
	/** �������� */
	public static final short CMD_MATCH_JOIN=0x0403;
	/** ������������ */
	public static final short CMD_MATCH_JOIN_RES=0x0404;
	/** �˳����� */
	public static final short CMD_MATCH_QUIT=0x0405;
	/** �˳��������� */
	public static final short CMD_MATCH_QUIT_RES=0x0406;
	/** �������� */
	public static final short CMD_MATCH_UPDATE=0x0408;
	/** ������Ҹ��� */
	public static final short CMD_MATCH_UPDATEPLAYER=0x040a;
	/** ����������ʼ */
	public static final short CMD_MATCH_START=0x040b;
	/** ����������ʼ���� */
	public static final short CMD_MATCH_START_RES=0x040c;

	/** ������Ϸ��ȡ��ǰ���� */
	public static final short CMD_RACING_SCENE=0x0501;
	/** ������Ϸ��ȡ��ǰ�������� */
	public static final short CMD_RACING_SCENE_RES=0x0502;
	/** ������Ϸ�ٶȱ仯 */
	public static final short CMD_RACING_SPEED=0x0503;
	/** ������Ϸ�ٶȱ�㲥��ǰ̨ */
	public static final short CMD_RACING_SPEED_RES=0x0504;
	/** ������Ϸ�����仯 */
	public static final short CMD_RACING_LOCATION=0x0505;
	/** ������Ϸ�����仯�㲥��ǰ̨ */
	public static final short CMD_RACING_LOCATION_RES=0x0506;
	/** ������Ϸ��ײ */
	public static final short CMD_RACING_COLLISION=0x0507;
	/** ������Ϸ��ײ�㲥��ǰ̨ */
	public static final short CMD_RACING_COLLISION_RES=0x0508;
	/** ������Ϸ��Ϸ���� */
	public static final short CMD_RACING_OVER=0x050a;

	/** ���������������Ϣ */
	public static final short CMD_CHAT_RECEIVE=0x0601;
	/** ���������������Ϣ */
	public static final short CMD_CHAT_RECEIVE_REC=0x0602;

	/** ͨ�Ŵ��� */
	public void transmit(int uuid,NioTcpConnect connect,ByteBuffer data);
}
