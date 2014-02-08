package com.cambrian.game.cc;

import com.cambrian.common.log.Logger;
import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.DataAccessException;
import com.cambrian.common.timer.TimerCenter;
import com.cambrian.common.timer.TimerEvent;
import com.cambrian.common.timer.TimerListener;
import com.cambrian.common.util.ArrayList;
import com.cambrian.common.util.AttributeList;
import com.cambrian.common.util.ChangeListenerList;
import com.cambrian.common.util.Selector;
import com.cambrian.common.util.TextKit;
import com.cambrian.common.util.TimeKit;
import com.cambrian.dfhm.Lang;
import com.cambrian.game.Session;
import com.cambrian.game.close.CloseAble;

/**
 * 类说明：认证中心
 * 
 * @version 2013-5-9
 * @author HYZ (huangyz1988@qq.com)
 */
public class CertifyCenter extends ChangeListenerList implements
	TimerListener,CloseAble
{

	/** 日志记录 */
	private static final Logger log=Logger.getLogger(CertifyCenter.class);

	/**
	 * session改变标志常量：认证前改变1，认证改变2，认证缓存改变3，认证版本改变5，登录改变11，活动改变21，退出改变31，更新改变41
	 * ，注册改变51，在线数量改变61
	 */
	public static final int PRE_CERTIFY_CHANGED=1,CERTIFY_CHANGED=2,
					CERTIFY_BUFFER_CHANGED=3,CERTIFY_VISITOR_CHANGED=5,
					LOGIN_CHANGED=11,ACTIVE_CHANGED=21,EXIT_CHANGED=31,
					UPDATE_CHANGED=41,REGISTER_CHANGED=51,
					ONLINE_NUM_CHANGED=61;

	/** 会话数量的属性名称 */
	public static final String SESSION_AMOUNT="sessionAmount";
	/** 无效的认证信息 */
	public static final String err1=CertifyCenter.class.getName()
		+" certify, invalid certify message";
	/** 已经在登录队列中 */
	public static final String err2=CertifyCenter.class.getName()
		+" certify, already in process of certify";
	/** 无效的用户名*/
	public static final String err3=Lang.F616_USER;
	/** 无效的密码*/
	public static final String err4=Lang.F617_PW;
	/** 无效的sid */
	public static final String err11=CertifyCenter.class.getName()
		+" load, invalid sid";

	/** Sid编码器 */
	SidEncoder sidEncoder=new SidEncoder();
	/** Sid会话表 */
	SidSessionMap ssm;
	/** 数据存取 */
	DBAccess access;
	/** 超时时间，默认为30分钟 */
	int timeout=1800000;
	/** collate定时器，60秒 */
	TimerEvent collateTimerEvent=new TimerEvent(this,"collate",60000);
	/** 临时认证表，防止玩家一个时间内认证两次 */
	ArrayList certifyList=new ArrayList();
	/** 属性表 */
	AttributeList attributes=new AttributeList();
	/** 整理选择器 */
	CollateSelector selecter=new CollateSelector();

	/* constructors */
	/** 构造一个数据中心管理器 */
	public CertifyCenter()
	{
		this(new SidSessionMap());
	}
	/** 用指定的Sid会话表构造一个数据中心管理器 */
	public CertifyCenter(SidSessionMap ssm)
	{
		this.ssm=ssm;
	}
	/* properties */
	/** 获得Sid会话表 */
	public SidSessionMap getSidSessionMap()
	{
		return ssm;
	}
	/** 获得Sid编码器 */
	public SidEncoder getSidEncoder()
	{
		return sidEncoder;
	}
	/** 设置Sid编码器 */
	public void setSidEncoder(SidEncoder encoder)
	{
		sidEncoder=encoder;
	}
	/** 获得认证访问接口 */
	public DBAccess getDBAccess()
	{
		return access;
	}
	/** 设置认证访问接口 */
	public void setDBAccess(DBAccess access)
	{
		this.access=access;
	}
	/** 获得超时时间 */
	public int getTimeout()
	{
		return timeout;
	}
	/** 设置超时时间 */
	public void setTimeout(int t)
	{
		timeout=t;
	}
	/** 获得collate定时器 */
	public TimerEvent getCollateTimerEvent()
	{
		return collateTimerEvent;
	}
	/** 获得属性表 */
	public AttributeList getAttributes()
	{
		return attributes;
	}
	/* methods */
	/** 认证方法(info:[id,md5(pw),serverId])，返回Session(Source为sid，Reference为uid) */
	public Session certify(String info,String address,Session session)
	{
		String[] infos = TextKit.split(info, ":");
		if(infos.length < 3)
			throw new DataAccessException(
				DataAccessException.SERVER_CMSG_ERROR,err1);
		int serverId = 0;
		try
		{
			serverId = TextKit.parseInt(infos[2]);
		}
		catch(Exception e)
		{
			throw new DataAccessException(
				DataAccessException.SERVER_CMSG_ERROR,err1);
		}
		return certify(infos[0], infos[1], serverId, address, session);
	}
	/** 认证方法 */
	public Session certify(String id,String pw,int serverId,String address,
		Session session)
	{
		if(log.isDebugEnabled())
			log.debug("certify, id="+id+", address="+address+", "+session);
		change(this,PRE_CERTIFY_CHANGED,id,address,session);
		if(id.length() == 0 || id.equals("null"))
			throw new DataAccessException(
				DataAccessException.SERVER_CMSG_ERROR,err1);
		synchronized(certifyList)
		{
			if(certifyList.contain(id))
				throw new DataAccessException(
					DataAccessException.CLIENT_INTERNAL_ERROR,err2);
			certifyList.add(id);
		}
		try
		{
			Session s = ssm.get(id);
			boolean b = true;
			if(s != null && (serverId+"").equals(s.getAttribute("serverId")))
				b = false;
			if(b)
			{
				if(s != null) ssm.remove(s);
				User user =access.valid(id, pw);// uid:serverId
				if(user == null)
					throw new DataAccessException(
						DataAccessException.SERVER_CMSG_ERROR,err3);
				if(user.userid == -1)
					throw new DataAccessException(
						DataAccessException.SERVER_CMSG_ERROR,err4);
				String sid = sidEncoder.createSid(id);
				s = new Session(id);
				s.setSource(sid);
				long uid=user.userid;
				uid|=(long)serverId<<32;
				s.setReference(uid);// uid
				s.setAttribute("pw", pw);
				s.setActiveTime(System.currentTimeMillis());
				ssm.add(s);
				change(this,CERTIFY_CHANGED,s,address,session);
				if(log.isInfoEnabled())
					log.info("certify ok, id="+id+", sid="+sid+", uid="
						+user.userid+", "+session);
			}
			else
			{
				if(!((serverId+"").equals(s.getAttribute("serverId"))||"0"
					.equals(s.getAttribute("gameserver"))))
					throw new DataAccessException(
						DataAccessException.SERVER_ACCESS_REFUSED,
						" valid, invalid serverId");
				// if(pw==null||!pw.equals(s.getAttribute("pw")))
				// throw new DataAccessException(
				// DataAccessException.SERVER_ACCESS_REFUSED," valid, invalid pw");
				s.setActiveTime(System.currentTimeMillis());
				change(this,CERTIFY_BUFFER_CHANGED,s,address,session);
				if(log.isInfoEnabled())
					log.info("certify ok, buffer, id="+id+", sid="
						+s.getSource()+", uid="+s.getReference()
						+", serverId="+s.getAttribute("serverId")+", "
						+session);
			}
			return s;
		}
		finally
		{
			synchronized(certifyList)
			{
				certifyList.remove(id);
			}
		}
	}
//	/** 认证方法，返回Session，只用于游戏服务器加载不在线的玩家数据 */
//	public Session certify(String id,int serverId,String address,
//		Session session)
//	{
//		if(log.isDebugEnabled())
//			log.debug("certify, id="+id+", address="+address+", "+session);
//		change(this,PRE_CERTIFY_CHANGED,id,address,session);
//		if(id==null||id.length()==0||id.equals("null"))
//			throw new DataAccessException(
//				DataAccessException.SERVER_CMSG_ERROR,err1);
//		synchronized(certifyList)
//		{
//			if(certifyList.contain(id))
//				throw new DataAccessException(
//					DataAccessException.CLIENT_INTERNAL_ERROR,err2);
//			certifyList.add(id);
//		}
//		try
//		{
//			Session s=ssm.get(id);
//			boolean b=true;
//			if(s!=null&&(serverId+"").equals(s.getAttribute("serverId")))
//				b=false;
//			if(b)
//			{
//				if(s!=null) ssm.remove(s);
//				String str=(String)access.valid(id,serverId,address);// uid:gameserver
//				if(str==null)
//					throw new DataAccessException(
//						DataAccessException.SERVER_CMSG_ERROR,err1);
//				String[] strs=TextKit.split(str,":");
//				String sid=sidEncoder.createSid(id);
//				s=new Session(id);
//				s.setSource(sid);
//				s.setReference(strs[0]);
//				s.setAttribute("serverId",strs[1]);// 原来是strs[1]
//				s.setAttribute("pw",strs[2]);
//				s.setActiveTime(System.currentTimeMillis());
//				ssm.add(s);
//				change(this,CERTIFY_CHANGED,s,address,session);
//				if(log.isInfoEnabled())
//					log.info("certify ok, id="+id+", sid="+sid+", uid="
//						+strs[0]+", serverId="+strs[1]+", "+session);
//			}
//			else
//			{
//				if(!((serverId+"").equals(s.getAttribute("serverId"))||"0"
//					.equals(s.getAttribute("serverId"))))
//					throw new DataAccessException(
//						DataAccessException.SERVER_ACCESS_REFUSED,
//						" valid, invalid serverId");
//				s.setActiveTime(System.currentTimeMillis());
//				change(this,CERTIFY_BUFFER_CHANGED,s,address,session);
//				if(log.isInfoEnabled())
//					log.info("certify ok, buffer, id="+id+", sid="
//						+s.getSource()+", uid="+s.getReference()
//						+", serverId="+s.getAttribute("serverId")+", "
//						+session);
//			}
//			return s;
//		}
//		finally
//		{
//			synchronized(certifyList)
//			{
//				certifyList.remove(id);
//			}
//		}
//	}
	/** 用指定的sid加载会话 */
	public Session login(String sid,String address,Session session)
	{
		if(log.isDebugEnabled())
			log.debug("load, sid="+sid+", address="+address+", "+session);
		Session s=ssm.getBySid(sid);
		if(s==null)
			throw new DataAccessException(
				DataAccessException.SERVER_ACCESS_REFUSED,err11);
		s.setActiveTime(TimeKit.nowTimeMills());
		change(this,LOGIN_CHANGED,s,address,session);
		if(log.isInfoEnabled())
			log.info("load ok, sid="+sid+", id="+s.getId()+", uid="
				+s.getReference()+", "+session);
		return s;
	}
	/** 更新方法 */
	public void update(String sid,String serverId,Session session)
	{
		Session s=ssm.getBySid(sid);
		if(s!=null) s.setAttribute("serverId",serverId);
		change(this,UPDATE_CHANGED,sid,s,session);
	}
	/** sid活动方法 */
	public void active(String sid,Session session)
	{
		Session s=ssm.getBySid(sid);
		if(s!=null) s.setActiveTime(System.currentTimeMillis());
		change(this,ACTIVE_CHANGED,sid,s,session);
	}
	/** sid退出方法 */
	public void exit(String sid,Session session)
	{
		Session s=ssm.getBySid(sid);
		if(s!=null)
		{
			ssm.remove(s);
		}

		change(this,EXIT_CHANGED,sid,ssm.getBySid(sid),session);
	}
	/** 用户名和密码退出方法 */
	public void exit(String id,String pw,Session session)
	{
		Session s=ssm.get(id);
		if(s!=null)
		{
			ssm.remove(s);
		}
		change(this,EXIT_CHANGED,id,s);
	}
	/** 定时器开始方法 */
	public void timerStart()
	{
		TimerCenter.getMinuteTimer().add(collateTimerEvent);
	}
	/** 定时事件的监听接口方法 */
	public void onTimer(TimerEvent ev)
	{
		if(ev==collateTimerEvent) collate(ev.getCurrentTime());
	}
	/** 整理方法 */
	public void collate(long time)
	{
		if(log.isInfoEnabled()) log.info("collate, size="+ssm.size());
		selecter.time=time-timeout;
		ssm.select(selecter);
		attributes.set(SESSION_AMOUNT,String.valueOf(ssm.size()));
		change(this,ONLINE_NUM_CHANGED,ssm.size());
		if(log.isInfoEnabled()) log.info("collate ok, size="+ssm.size());
	}
	/** 关闭方法 */
	public void close(ByteBuffer data)
	{
		if(log.isInfoEnabled()) log.info("close, size="+ssm.size());
		TimerCenter.getMinuteTimer().remove(collateTimerEvent);
		ssm.clear();
		attributes.set(SESSION_AMOUNT,"0");
		if(log.isInfoEnabled()) log.info("close ok, size="+ssm.size());
	}
	/** 关闭方法（关闭进程用） */
	public void close()
	{
		this.close(null);
		if(log.isInfoEnabled()) log.info(" CC CLOSE! ");
		System.exit(0);
	}

	/** 类说明：整理选择器 */
	class CollateSelector implements Selector
	{

		/** 超时时间 */
		long time;

		/** 判断对象是否被选择 */
		public int select(Object obj)
		{
			Session s=(Session)obj;
			synchronized(certifyList)
			{
				if(certifyList.contain(s.getId())) return FALSE;
			}
			return (time<s.getActiveTime())?FALSE:TRUE;
		}
	}
}