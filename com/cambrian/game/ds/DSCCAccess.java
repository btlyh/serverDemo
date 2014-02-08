package com.cambrian.game.ds;

/**
 * ��˵����
 * 
 * @version 2013-4-23
 * @author HYZ (huangyz1988@qq.com)
 */
public interface DSCCAccess
{

	/** �ܷ���� */
	public abstract boolean canAccess();

	/** ������Ϣ */
	public abstract String[] load(String sid,String address);

	/** ��Ծ */
	public abstract void active(String sid);

	/** �˳� */
	public abstract void exit(String sid);

	/** ע�� */
	public abstract void regist(String info,int serverID);

	/** �ر�CC���� */
	public abstract void shutDown();
}