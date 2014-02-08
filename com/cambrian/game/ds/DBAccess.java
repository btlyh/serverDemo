/** */
package com.cambrian.game.ds;

import com.cambrian.common.net.ByteBuffer;

/**
 * 类说明：
 * 
 * @version 2013-4-25
 * @author HYZ (huangyz1988@qq.com)
 */
public interface DBAccess
{

	/** 是否可以连接 */
	public abstract boolean canAccess();

	/** 加载 */
	public abstract ByteBuffer load(String id,String sid,String nickName,String userName);
	
	/** 保存 */
	public abstract boolean save(ByteBuffer data);
}
