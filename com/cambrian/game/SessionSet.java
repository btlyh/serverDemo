/** */
package com.cambrian.game;

import com.cambrian.common.net.ByteBuffer;

/**
 * ÀàËµÃ÷£º
 * 
 * @version 2013-4-23
 * @author HYZ (huangyz1988@qq.com)
 */
public abstract interface SessionSet extends Selectable
{

	public static final Session[] NULL=new Session[0];

	public abstract int size();

	public abstract Session[] getSessions();

	public abstract boolean contain(Session paramSession);

	public abstract boolean add(Session paramSession);

	public abstract boolean add(Session[] paramArrayOfSession);

	public abstract boolean add(Session[] paramArrayOfSession,int paramInt1,
		int paramInt2);

	public abstract boolean remove(Session paramSession);

	public abstract Session get(String paramString);

	public abstract Session remove(String paramString);

	public abstract void send(ByteBuffer paramByteBuffer);

	public abstract void sendWithout(ByteBuffer paramByteBuffer,
		Session paramSession);

	public abstract void clear();
}