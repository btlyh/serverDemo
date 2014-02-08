package com.cambrian.game.ds;

import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.DataAccessException;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.common.util.ChangeAdapter;
import com.cambrian.dfhm.player.Player;
import com.cambrian.game.Session;

/**
 * 类说明：
 * 
 * @version 2013-5-10
 * @author HYZ (huangyz1988@qq.com)
 */
public final class DSChangeAdapter extends ChangeAdapter
{

	int adviseOfflinePort=701;

	String loginCollisionAction="loginCollision";

	String saveRefusedAction="saveRefused";

	public int getAdviseOfflinePort()
	{
		return this.adviseOfflinePort;
	}

	public void setAdviseOfflinePort(int port)
	{
		this.adviseOfflinePort=port;
	}

	public String getLoginCollisionAction()
	{
		return this.loginCollisionAction;
	}

	public void setLoginCollisionAction(String action)
	{
		this.loginCollisionAction=action;
	}

	public String getSaveRefusedAction()
	{
		return this.saveRefusedAction;
	}

	public void setSaveRefusedAction(String action)
	{
		this.saveRefusedAction=action;
	}

	public void change(Object source,int type)
	{
		if(!(source instanceof NioTcpConnect)) return;
		if(type!=2) return;
		NioTcpConnect c=(NioTcpConnect)source;
		Session s=c.getSession();
		if(s==null) return;
		s.change(s,21);
	}

	/**
	 * 登录
	 */
	public void change(Object source,int type,Object value)
	{
		if(!(source instanceof DataServer)) return;
		Session s=(Session)value;
		Player player=(Player)s.getSource();
		if(player!=null)
		{
			//player.initToken();
		}
		if(type==4)// 新用户登录
		{
			s.change(s,3);
		}
		else
		// 重新登录
		{
			if(type!=3) return;
			s.change(s,2);
		}
	}

	/**
	 * 登录
	 */
	public void change(Object source,int type,Object v1,Object v2)
	{
		if(!(source instanceof DataServer)) return;

		if(type==2)
		{
			Session s=(Session)v1;
			NioTcpConnect c=s.getConnect();
			if((c==null)||(this.adviseOfflinePort<=0)) return;
			ByteBuffer bb=new ByteBuffer();
			bb.writeUTF(this.loginCollisionAction);
			bb.writeUTF(((NioTcpConnect)v2).getURL().getHost());
			c.send((short)adviseOfflinePort,bb);
			c.setSession(null);
			try
			{
				Thread.sleep(10L);
			}
			catch(Throwable localThrowable1)
			{
			}
			c.close();
		}
		else if(type==22)
		{
			if(v2==null) return;
			DataAccessException e=(DataAccessException)v2;
			if(e.getType()!=560) return;
			Session session=(Session)v1;
			((DataServer)source).getSessionMap().remove(session);
			NioTcpConnect c=session.getConnect();
			if((c==null)||(this.adviseOfflinePort<=0)) return;
			ByteBuffer bb=new ByteBuffer();
			bb.writeUTF(this.saveRefusedAction);
			bb.writeUTF("");
			c.send((short)adviseOfflinePort,bb);
			c.setSession(null);
			try
			{
				Thread.sleep(10L);
			}
			catch(Throwable localThrowable2)
			{
			}
			c.close();
		}
		else
		{
			if(type!=32) return;
			Session s=(Session)v1;
			if(v2!=null)
			{
				DataAccessException e=(DataAccessException)v2;
				if(e.getType()!=560)
				{
					((DataServer)source).getSessionMap().add(s);
					return;
				}
			}
			NioTcpConnect c=s.getConnect();
			if(c!=null) c.setSession(null);
			s.change(s,22);
		}
	}

	public void change(Object source,int type,Object v1,Object v2,Object v3)
	{
		if(!(source instanceof DataServer)) return;
		if(type!=42) return;
		if(v2==null) return;
		Session s=(Session)v1;
		s.change(s,11,v2,v3);
	}
}