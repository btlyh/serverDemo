/** */
package com.cambrian.game.util;

import com.cambrian.common.net.ByteBuffer;

/**
 * ��˵�����ֽڶ�д��
 * 
 * @version 2013-4-27
 * @author HYZ (huangyz1988@qq.com)
 */
public interface BytesRW
{

	public abstract Object bytesRead(ByteBuffer data);

	public abstract void bytesWrite(Object obj,ByteBuffer data);
}
