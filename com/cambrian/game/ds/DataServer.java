package com.cambrian.game.ds;

import com.cambrian.common.codec.CRC32;
import com.cambrian.common.codec.MD5;
import com.cambrian.common.log.Logger;
import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.DataAccessException;
import com.cambrian.common.net.NioTcpConnect;
import com.cambrian.common.timer.TimerCenter;
import com.cambrian.common.timer.TimerEvent;
import com.cambrian.common.timer.TimerListener;
import com.cambrian.common.util.ArrayList;
import com.cambrian.common.util.AttributeList;
import com.cambrian.common.util.ChangeListenerList;
import com.cambrian.common.util.MathKit;
import com.cambrian.common.util.Selector;
import com.cambrian.common.util.TextKit;
import com.cambrian.common.util.TimeKit;
import com.cambrian.dfhm.Lang;
import com.cambrian.dfhm.player.Player;
import com.cambrian.game.Session;
import com.cambrian.game.SessionMap;
import com.cambrian.game.close.CloseAble;
import com.cambrian.game.util.BytesRW;

/**
 * 类说明：游戏数据服务
 * 
 * @version 2013-4-22
 * @author HYZ (huangyz1988@qq.com)
 */
public class DataServer extends ChangeListenerList implements TimerListener,
	CloseAble
{

	/** 日志 */
	private static final Logger log=Logger.getLogger(DataServer.class);
	/** 类名 */
	public static final String toString=DataServer.class.getName();
	/** 会话数量KEY */
	public static final String SESSION_AMOUNT="sessionAmount";
	/** 循环冗余校验状态 */
	public static final int CRC_STATE=100;
	/** 保存时间状态 */
	public static final int SAVE_TIME_STATE=200;
	/** 活跃时间状态 */
	public static final int ACTIVE_TIME_STATE=201;
	/** 默认超时时间 */
	public static final int TIMEOUT=6000000;
	/** 默认活跃超时时间 */
	public static final int ACTIVE_TIMEOUT=360000;
	/** 默认保存超时时间 */
	public static final int SAVE_TIMEOUT=300000;
	/** 登录改变 */
	public static final int LOGIN_CHANGED=1;
	/** 登录冲突改变 */
	public static final int LOGIN_COLLISION_CHANGED=2;
	/** 重新登录改变 */
	public static final int LOGIN_AGAIN_CHANGED=3;
	/** 新建登录改变 */
	public static final int LOGIN_RENEW_CHANGED=4;
	/** 加载之前改变 */
	public static final int PRE_LOAD_CHANGED=11;
	/** 加载改变 */
	public static final int LOAD_CHANGED=12;
	/** 加载缓存改变 */
	public static final int LOAD_BUFFER_CHANGED=13;
	/** 保存之前改变 */
	public static final int PRE_SAVE_CHANGED=21;
	/** 保存改变 */
	public static final int SAVE_CHANGED=22;
	/** 退出之前改变 */
	public static final int PRE_EXIT_CHANGED=31;
	/** 退出改变 */
	public static final int EXIT_CHANGED=32;
	/** 更新之前改变 */
	public static final int PRE_UPDATE_CHANGED=41;
	/** 更新改变 */
	public static final int UPDATE_CHANGED=42;
	/** 正常注册 */
	public static final int REGIST_NORMAL=0x0;
	/** 一键注册 */
	public static final int REGIST_ONEKEY=0x1;

	/** 登录，服务器人数已满 */
	public static final String err1=toString
		+" login, session amount is full";
	/** 登录，认证服务器无法连接 */
	public static final String err2=toString
		+" login, dsc can not be accessed";
	/** 登陆，数据中心无法连接 */
	public static final String err3=toString
		+" login, server can not be accessed";
	/** 登陆，正在登录请勿重复登录 */
	public static final String err4=toString
		+" login, already in process of login";
	/** 登陆，数据正在保存中 */
	public static final String err5=toString
		+" login, session is in process of save";
	/** 登陆，当前会话无法新建 */
	public static final String err6=toString
		+" login, session can not be renewed";
	/** 加载，数据中心无法连接 */
	public static final String err11=toString
		+" load, server can not be accessed";
	/** 加载，数据正在保存 */
	public static final String err12=toString
		+" load, session is in process of save";
	/** 登出，数据中心无法连接 */
	public static final String err21=toString
		+" exit, server can not be accessed";
	/** 登出，数据正在保存 */
	public static final String err22=toString
		+" exit, session is in process of save";
	/** 登出，正在登录中 */
	public static final String err23=toString
		+" exit, session is in process of login";
	/** 更新，数据中心无法连接 */
	public static final String err31=toString
		+" update, server can not be accessed";
	/** 更新，数据正在保存 */
	public static final String err32=toString
		+" update, session is in process of save";

	/** 所连的数据库的id,如果合服这里考虑写成数组，暂时按这样写 */
	int serverId;
	/** 超时时间 */
	int timeout;
	/** 活跃超时时间 */
	int activeTimeout;
	/** 保存超时时间 */
	int saveTimeout;
	/** 会话数量 */
	int sessionAmount;
	/** 会话管理列表 */
	SessionMap sm;
	/** CC访问类 */
	DSCCAccess ccAccess;
	/** 加载数据读取器 */
	BytesRW dcRW;
	/** 客户端数据写入器 */
	BytesRW userWriter;
	/** 整理定时器事件 */
	TimerEvent collateTimerEvent;
	/** 属性列表 */
	AttributeList attributes;
	/** 整理选择器 */
	CollateSelector selecter;
	/** 当前登录列表 */
	ArrayList loginList;
	/** 保存列表 */
	ArrayList saveList;
	/** 保存缓存 */
	private ByteBuffer saveBuffer;
	/** 昵称验证类 */
	CheckNameFilter cnf;
	/** 数据访问对象 */
	DBAccess access;

	/** 构造一个游戏数据服务管理器 */
	public DataServer()
	{
		this(new SessionMap());
	}
	/** 构造一个具有指定会话列表的游戏数据服务管理器 */
	public DataServer(SessionMap sm)
	{
		this.sm=sm;
		timeout=TIMEOUT;
		saveTimeout=SAVE_TIMEOUT;
		activeTimeout=ACTIVE_TIMEOUT;
		sessionAmount=0xFFFF;
		collateTimerEvent=new TimerEvent(this,"collate",30000);
		saveList=new ArrayList();
		loginList=new ArrayList();
		saveBuffer=new ByteBuffer();
		selecter=new CollateSelector();
		attributes=new AttributeList();
	}
	/** 设置会话管理列表 */
	public SessionMap getSessionMap()
	{
		return sm;
	}
	/** 获得认证中心访问器 */
	public DSCCAccess getDSCCAccess()
	{
		return ccAccess;
	}
	/** 设置认证中心访问器 */
	public void setDSCCAccess(DSCCAccess access)
	{
		ccAccess=access;
		System.err.println("ccAccess ===="+ccAccess);
	}
	/** 获得数据读写器 */
	public BytesRW getDCRW()
	{
		return dcRW;
	}
	/** 设置数据读写器 */
	public void setDCRW(BytesRW reader)
	{
		this.dcRW=reader;
	}
	/** 获取用户数据写入器 */
	public BytesRW getUserWriter()
	{
		return userWriter;
	}
	/** 设置用户数据写入器 */
	public void setUserWriter(BytesRW writer)
	{
		userWriter=writer;
	}
	/** 获得超时时间 */
	public int getTimeout()
	{
		return timeout;
	}
	/** 设置超时时间 */
	public void setTimeout(int timeout)
	{
		this.timeout=timeout;
	}
	/** 获得活跃超时时间 */
	public int getActiveTimeout()
	{
		return activeTimeout;
	}
	/** 设置活跃超时时间 */
	public void setActiveTimeout(int activeTimeout)
	{
		this.activeTimeout=activeTimeout;
	}
	/** 获得保存超时时间 */
	public int getSaveTimeout()
	{
		return saveTimeout;
	}
	/** 设置保存超时时间 */
	public void setSaveTimeout(int saveTimeout)
	{
		this.saveTimeout=saveTimeout;
	}
	/** 获得会话数量 */
	public int getSessionAmount()
	{
		return sessionAmount;
	}
	/** 设置会话数量 */
	public void setSessionAmount(int amount)
	{
		sessionAmount=amount;
	}
	/** 设置整理定时器事件 */
	public TimerEvent getCollateTimerEvent()
	{
		return collateTimerEvent;
	}
	/** 获得属性列表 */
	public AttributeList getAttributes()
	{
		return attributes;
	}
	/** 是否为这个服务上玩家 */
	public boolean isThis(long userid)
	{
		// TODO Auto-generated method stub
		return true;
	}
	/** 设置昵称验证器 */
	public void setCheckNameFilter(CheckNameFilter cnf)
	{
		this.cnf=cnf;
	}

	/***
	 * 设置数据访问对象
	 * 
	 * @param access
	 */
	public void setDBAccess(DBAccess access)
	{
		this.access=access;
	}

	/**
	 * 访问CC验证并注册账号
	 * 
	 * @param regType
	 * @param info
	 */
	public String regist(int regType,String info)
	{
		String[] infos;
		String pw=null;
		if(regType==REGIST_NORMAL)
		{
			infos=TextKit.split(info,":");
		}
		else
		{
			infos=oneKeyRegist();
			pw=infos[1];
			infos[1]=MD5.getValue(pw);
		}
		if(infos[0].matches(Lang.REGEX_CHECK_USERNAME))
		{
			StringBuffer sb=new StringBuffer();
			sb.append(infos[0]).append(":").append(infos[1]);
			ccAccess.regist(sb.toString(),serverId);
		}
		else
		{
			throw new DataAccessException(
				DataAccessException.SERVER_INTERNAL_ERROR,
				Lang.F619_REG_ILLEGALITY);
		}
		String backInfo=null;
		StringBuffer back_sb=new StringBuffer();
		if(regType==REGIST_ONEKEY)
		{
			back_sb.append(infos[0]).append(":").append(pw).toString();
			backInfo=back_sb.toString();
		}
		return backInfo;
	}
	/**
	 * 一键注册
	 * 
	 * @return
	 */
	private String[] oneKeyRegist()
	{
		String[] infos=new String[2];
		infos[0]=getStrCode();
		infos[1]=getStrCode();
		return infos;
	}

	/**
	 * 获取字符串码 （一键注册用，这个方法可以优化）
	 * 
	 * @return
	 */
	private String getStrCode()
	{
		char[] chars=TextKit.getCharExceptArgs(new char[]{'s','b'},
			TextKit.TYPE_LOWER);
		int index_1=MathKit.randomValue(0,chars.length-1);
		int index_2=MathKit.randomValue(0,chars.length-1);
		StringBuffer userNameBuffer=new StringBuffer();
		userNameBuffer.append(chars[index_1]).append(chars[index_2]);
		String tempStr=String.valueOf(TimeKit.nowTimeMills()).substring(7);
		userNameBuffer.append(tempStr);
		return userNameBuffer.toString();
	}

	/** sid登录，是否重新构建会话 */
	public void login(String sid,boolean renew,NioTcpConnect connect)
	{
		if(log.isDebugEnabled())
			log.debug("login, sid="+sid+", renew="+renew+", "+connect);
		if(sm.size()>=sessionAmount)
			throw new DataAccessException(
				DataAccessException.SERVER_INTERNAL_ERROR,err1);
		if(!ccAccess.canAccess())
			throw new DataAccessException(
				DataAccessException.SERVER_INTERNAL_ERROR,err2);
		String[] infos=ccAccess.load(sid,connect.getURL().getHost());
		if(log.isDebugEnabled())
			log.debug("login, sid="+sid+", id="+infos[0]+", uid="+infos[1]
				+", "+connect);
		String uid=infos[1];
		synchronized(loginList)
		{
			if(loginList.contain(uid))
				throw new DataAccessException(500,err4);
			loginList.add(uid);
		}
		try
		{
			synchronized(saveList)
			{
				if(saveList.contain(uid))
					throw new DataAccessException(
						DataAccessException.SERVER_INTERNAL_ERROR,err5);
			}
			// dcAccess.login(uid,sid,connect.getURL().getHost());
			Session s=sm.get(uid);
			System.err.println("s ===="+s);
			if(s!=null)
			{
				if(s.getConnect()==connect) return;
				change(this,LOGIN_COLLISION_CHANGED,s,connect);
				NioTcpConnect old=s.getConnect();
				if(old!=null)
				{
					old.setSession(null);
					old.close();
				}
				s.setReference(sid);
				connect.setSession(s);
				s.setConnect(connect);
				long time=TimeKit.nowTimeMills();
				s.setActiveTime(time);
				s.setState(ACTIVE_TIME_STATE,TimeKit.timeSecond(time));
				if(renew)
				{
					change(this,LOGIN_RENEW_CHANGED,s);
					if(log.isInfoEnabled())
						log.info("login ok, renew, sid="+sid+", "+s);
				}
				change(this,LOGIN_AGAIN_CHANGED,s);
				if(log.isInfoEnabled())
					log.info("login ok, again, sid="+sid+", "+s);
				return;
			}
			if(renew) throw new DataAccessException(560,err6);
			s=new Session(uid);
			s.setReference(sid);
			connect.setSession(s);
			s.setConnect(connect);
			long time=System.currentTimeMillis();
			s.setActiveTime(time);
			s.setState(ACTIVE_TIME_STATE,TimeKit.timeSecond(time));
			sm.add(s);
			change(this,LOGIN_CHANGED,s);
			if(log.isInfoEnabled()) log.info("login ok, sid="+sid+", "+s);
		}
		catch(DataAccessException e)
		{
			if(log.isWarnEnabled())
				log.warn("login error, sid="+sid+", id="+uid+", "+connect,e);
			throw e;
		}
		finally
		{
			synchronized(loginList)
			{
				loginList.remove(uid);
			}
		}
	}
	/**
	 * 获取随机昵称
	 * 
	 * @return
	 */
	public String getRandomName()
	{
		return cnf.getRandomName();
	}

	// /***
	// * 临时添加卡牌（策划测试）
	// *
	// * @param session
	// */
	// private void addCards(Object obj)
	// {
	// if(obj!=null)
	// {
	// Player player=(Player)obj;
	// if(player!=null&&player.getUserId()>0)
	// {
	// if(player.getNickname().equals("沙立轩"))
	// {
	// for(int i=10002;i<10225;i++)
	// {
	// player.getCardBag().add(i,player.getAchievements());
	// }
	// }
	// }
	// }
	// }

	/** 加载 */
	public ByteBuffer load(Session session,ByteBuffer data,String nickName,
		String userName)
	{
		data.clear();
		String uid=session.getId();// UID
		if(log.isDebugEnabled()) log.debug("load, id="+uid);
		synchronized(saveList)
		{
			if(saveList.contain(uid))
				throw new DataAccessException(
					DataAccessException.SERVER_INTERNAL_ERROR,err12);
		}
		change(this,PRE_LOAD_CHANGED,session);
		Object obj=session.getSource();
		if(obj!=null)
		{
			// addCards(obj);
			userWriter.bytesWrite(obj,data);
			change(this,LOAD_BUFFER_CHANGED,session);
			if(log.isDebugEnabled())
				log.debug("load ok, buffer,"+session+" "+obj);
			return data;
		}
		ByteBuffer bb=access.load(uid,session.getId(),nickName,userName);
		obj=dcRW.bytesRead(bb);
		// addCards(obj);
		Player player=(Player)obj;
		if(player.getUserId()!=-1&&player.getUserId()!=-2)
		{
			session.setSource(obj);
			setSession(session,obj,bb);
			change(this,LOAD_CHANGED,session);
		}
		userWriter.bytesWrite(obj,data);
		if(log.isDebugEnabled()) log.debug("load ok, "+session+" "+obj);
		return data;
	}
	/** 登出 */
	public void exit(Session session,ByteBuffer data)
	{
		String id=session.getId();
		if(log.isDebugEnabled()) log.debug("exit, id="+id);
		synchronized(saveList)
		{
			if(saveList.contain(id))
				throw new DataAccessException(
					DataAccessException.SERVER_INTERNAL_ERROR,err22);
			saveList.add(id);
		}
		DataAccessException e;
		try
		{
			synchronized(loginList)
			{
				if(loginList.contain(id))
					throw new DataAccessException(
						DataAccessException.SERVER_INTERNAL_ERROR,err23);
			}
			if(!(sm.remove(session))) return;
			change(this,PRE_EXIT_CHANGED,session);
			e=saveSession(session,true,data);
			change(this,EXIT_CHANGED,session,e);
		}
		finally
		{
			synchronized(saveList)
			{
				saveList.remove(id);
			}
		}
	}

	/** 设置会话 */
	public void setSession(Session s,Object obj,ByteBuffer data)
	{
		long time=TimeKit.nowTimeMills();
		s.setActiveTime(time);
		s.setState(SAVE_TIME_STATE,TimeKit.timeSecond(time));
		data.clear();
		try
		{
			dcRW.bytesWrite(obj,data);
			int crc=CRC32.getValue(data.getArray(),0,data.top());
			s.setState(CRC_STATE,crc);
		}
		catch(Throwable t)
		{
			if(log.isWarnEnabled())
				log.warn("setSession error, id="+s.getId()+", obj="+obj,t);
		}
	}

	/** 开始定时器 */
	public void timerStart()
	{
		TimerCenter.getMinuteTimer().add(collateTimerEvent);
	}

	// TODO 定时器响应
	/** 定时器事件响应 */
	public void onTimer(TimerEvent ev)
	{
		if(ev!=collateTimerEvent) return;
		collate(ev.getCurrentTime());
	}

	/** 整理 */
	void collate(long time)
	{
		boolean ccAccess=this.ccAccess.canAccess();
		boolean dcAccess=this.access.canAccess();
		if(log.isDebugEnabled()) log.debug(runtimeLog(ccAccess,dcAccess));
		selecter.remove=dcAccess;
		selecter.list1.clear();
		selecter.list2.clear();
		selecter.time=(time-timeout);
		try
		{
			synchronized(saveList)
			{
				sm.select(selecter);
			}
			if(dcAccess)
			{
				exitSessions(selecter.list1,saveBuffer);
				saveSessions(selecter.list2,time,saveBuffer);
				attributes.set(SESSION_AMOUNT,String.valueOf(sm.size()));
			}
			if(ccAccess) activeSessions(selecter.list2,time);
			if(log.isInfoEnabled()) log.info(runtimeLog(ccAccess,dcAccess));
		}
		finally
		{
			synchronized(saveList)
			{
				saveList.clear();
			}
		}
	}
	/** 退出一组会话 */
	public void exitSessions(ArrayList list,ByteBuffer data)
	{
		int i=0;
		for(int n=list.size();i<n;++i)
		{
			Session s=(Session)list.get(i);
			change(this,PRE_EXIT_CHANGED,s);
			// 保存退出时间
			Player player=(Player)s.getSource();
			if(player!=null)
			{
				//player.setLogoutTime(TimeKit.nowTimeMills());
			}
			DataAccessException e=saveSession(s,true,data);
			change(this,EXIT_CHANGED,s,e);
		}
		change(this,CLOSEOVER);
	}

	/** 保存一组会话 */
	public void saveSessions(ArrayList list,long time,ByteBuffer data)
	{
		int t=TimeKit.timeSecond(time);
		int ct=TimeKit.timeSecond(time-saveTimeout);
		int i=0;
		for(int n=list.size();i<n;++i)
		{
			Session s=(Session)list.get(i);
			int ctime=s.getState(SAVE_TIME_STATE);
			if(ct<ctime) continue;
			change(this,PRE_SAVE_CHANGED,s);
			DataAccessException e=saveSession(s,false,data);
			if(e==null) s.setState(SAVE_TIME_STATE,t);
			change(this,SAVE_CHANGED,s,e);
		}
	}

	/** 设置一组会话活跃状态 */
	public void activeSessions(ArrayList list,long time)
	{
		int t=TimeKit.timeSecond(time);
		int ct=TimeKit.timeSecond(time-activeTimeout);

		int i=0;
		for(int n=list.size();i<n;++i)
		{
			Session s=(Session)list.get(i);
			int ctime=s.getState(ACTIVE_TIME_STATE);
			if(ct<ctime) continue;
			activeSession(s);
			s.setState(ACTIVE_TIME_STATE,t);
		}
	}

	/** 设置会话活跃状态 */
	public void activeSession(Session session)
	{
		String sid=(String)session.getReference();
		try
		{
			ccAccess.active(sid);
		}
		catch(Throwable t)
		{
			if(log.isWarnEnabled())
				log.warn("activeSession error, sid="+sid,t);
		}
	}

	/***
	 * 关服前保存数据
	 */
	public void saveSessionByShutDown()
	{
		Session[] sessions=sm.getSessions();
		Object obj=null;
		ByteBuffer data=new ByteBuffer();
		boolean save=false;
		for(Session session:sessions)
		{
			obj=session.getSource();
			if(obj!=null)
			{
				Player p=(Player)obj;
				if(p!=null&&p.getUserId()>0)
				{
					p.dbBytesWrite(data);
					int crc=CRC32.getValue(data.getArray(),data.offset(),
						data.length());
					save=(crc!=session.getState(CRC_STATE));
					access.save(save?data:null);
				}
			}
		}
		sm.clear();
		System.exit(0);
	}
	/** 保存会话 */
	public DataAccessException saveSession(Session s,boolean exit,
		ByteBuffer data)
	{
		String uid=s.getId();
		Object obj=s.getSource();
		if(obj==null)
		{
			if(log.isInfoEnabled()) log.info("saveSession, id="+uid);
			return null;
		}
		data.clear();
		boolean save=false;
		try
		{
			dcRW.bytesWrite(obj,data);
			int crc=CRC32.getValue(data.getArray(),data.offset(),
				data.length());
			save=(crc!=s.getState(CRC_STATE));// 数据改变
			access.save(save?data:null);
			s.setState(CRC_STATE,crc);
			if(log.isInfoEnabled())
				log.info("saveSession, save="+save+", exit="+exit+", id="
					+uid+", obj="+obj);
			return null;
		}
		catch(DataAccessException e)
		{
			if(log.isWarnEnabled())
				log.warn("saveSession error, save="+save+", exit="+exit
					+", id="+uid+", obj="+obj,e);
			return e;
		}
		catch(Throwable t)
		{
			if(log.isWarnEnabled())
				log.warn("saveSession error, save="+save+", exit="+exit
					+", id="+uid+", obj="+obj,t);
			return new DataAccessException(400,t.toString());
		}
	}

	/** 关闭当前游戏服务 */
	public void close()
	{
		boolean b=access.canAccess();
		if(log.isInfoEnabled())
			log.info("close, access="+b+", size="+sm.size());
		TimerCenter.getMinuteTimer().remove(collateTimerEvent);
		Session[] array=sm.getSessions();
		sm.clear();
		for(int i=0;i<array.length;++i)
		{
			NioTcpConnect c=array[i].getConnect();
			if(c!=null)
			{
				c.setSession(null);
				c.close();
			}
		}
		if(b) exitSessions(new ArrayList(array),new ByteBuffer());
		attributes.set(SESSION_AMOUNT,"0");
		if(!(log.isInfoEnabled())) return;
		log.info("close ok, size="+sm.size());
	}

	/** 运行日志 */
	private String runtimeLog(boolean ccAccess,boolean dcAccess)
	{
		Runtime r=Runtime.getRuntime();
		long memory=r.totalMemory();
		long used=memory-r.freeMemory();
		return "collate ok, dcAccess="+dcAccess+", ccAccess="+ccAccess
			+", size="+sm.size()+", memory="+used+"/"+memory+", maxMemory="
			+r.maxMemory();
	}

	/***
	 * 类说明：会话整理选择器
	 * 
	 * @version 2013-4-24
	 * @author HYZ (huangyz1988@qq.com)
	 */
	class CollateSelector implements Selector
	{

		/** 是否移除 */
		boolean remove;
		/** 保存不要活跃的 */
		ArrayList list1;
		/** 保存需要活跃的 */
		ArrayList list2;
		/** 超时时间 */
		long time;

		/** 构建一个整理选择器 */
		CollateSelector()
		{
			list1=new ArrayList();
			list2=new ArrayList();
		}

		/** 选择 */
		public int select(Object obj)
		{
			Session s=(Session)obj;
			if(s.isTemp()) return 0;
			synchronized(loginList)
			{
				if(loginList.contain(s.getId())) return 0;
			}
			if(time<s.getActiveTime())
			{
				list2.add(s);
				return 0;
			}
			list1.add(s);
			if(!remove) return 0;
			saveList.add(s.getId());
			return 1;
		}
	}

	/**
	 * 根据玩家昵称获取session
	 * 
	 * @param nickName 玩家昵称
	 * @return
	 */
	public Session getSession(String nickName)
	{
		Session[] sessions=sm.getSessions();
		Session session=null;
		Player player=null;
		for(int i=0;i<sessions.length;i++)
		{
			session=sessions[i];
			if(session!=null)
			{
				player=(Player)session.getSource();
			}
			if(player!=null)
			{
//				if(player.getNickname().equals(nickName))
//				{
					return session; //TODO 获得session
//				}
			}
		}
		return null;
	}

	/**
	 * 根据玩家ID获取session
	 * 
	 * @param nickName 玩家昵称
	 * @return
	 */
	public Session getSession(int userId)
	{
		Session[] sessions=sm.getSessions();
		Session session=null;
		Player player=null;
		for(int i=0;i<sessions.length;i++)
		{
			session=sessions[i];
			if(session!=null)
			{
				player=(Player)session.getSource();
			}
			if(player!=null)
			{
				if((int)player.getUserId()==userId)
				{
					return session;
				}
			}
		}
		return null;
	}

}
