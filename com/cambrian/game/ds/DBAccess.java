/** */
package com.cambrian.game.ds;

import com.cambrian.common.net.ByteBuffer;

/**
 * ��˵����
 * 
 * @version 2013-4-25
 * @author HYZ (huangyz1988@qq.com)
 */
public interface DBAccess
{

	/** �Ƿ�������� */
	public abstract boolean canAccess();

	/** ���� */
	public abstract ByteBuffer load(String id,String sid,String nickName,String userName);
	
	/** ���� */
	public abstract boolean save(ByteBuffer data);
}
