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
 * ��˵������Ϸ���ݷ���
 * 
 * @version 2013-4-22
 * @author HYZ (huangyz1988@qq.com)
 */
public class DataServer extends ChangeListenerList implements TimerListener,
	CloseAble
{

	/** ��־ */
	private static final Logger log=Logger.getLogger(DataServer.class);
	/** ���� */
	public static final String toString=DataServer.class.getName();
	/** �Ự����KEY */
	public static final String SESSION_AMOUNT="sessionAmount";
	/** ѭ������У��״̬ */
	public static final int CRC_STATE=100;
	/** ����ʱ��״̬ */
	public static final int SAVE_TIME_STATE=200;
	/** ��Ծʱ��״̬ */
	public static final int ACTIVE_TIME_STATE=201;
	/** Ĭ�ϳ�ʱʱ�� */
	public static final int TIMEOUT=6000000;
	/** Ĭ�ϻ�Ծ��ʱʱ�� */
	public static final int ACTIVE_TIMEOUT=360000;
	/** Ĭ�ϱ��泬ʱʱ�� */
	public static final int SAVE_TIMEOUT=300000;
	/** ��¼�ı� */
	public static final int LOGIN_CHANGED=1;
	/** ��¼��ͻ�ı� */
	public static final int LOGIN_COLLISION_CHANGED=2;
	/** ���µ�¼�ı� */
	public static final int LOGIN_AGAIN_CHANGED=3;
	/** �½���¼�ı� */
	public static final int LOGIN_RENEW_CHANGED=4;
	/** ����֮ǰ�ı� */
	public static final int PRE_LOAD_CHANGED=11;
	/** ���ظı� */
	public static final int LOAD_CHANGED=12;
	/** ���ػ���ı� */
	public static final int LOAD_BUFFER_CHANGED=13;
	/** ����֮ǰ�ı� */
	public static final int PRE_SAVE_CHANGED=21;
	/** ����ı� */
	public static final int SAVE_CHANGED=22;
	/** �˳�֮ǰ�ı� */
	public static final int PRE_EXIT_CHANGED=31;
	/** �˳��ı� */
	public static final int EXIT_CHANGED=32;
	/** ����֮ǰ�ı� */
	public static final int PRE_UPDATE_CHANGED=41;
	/** ���¸ı� */
	public static final int UPDATE_CHANGED=42;
	/** ����ע�� */
	public static final int REGIST_NORMAL=0x0;
	/** һ��ע�� */
	public static final int REGIST_ONEKEY=0x1;

	/** ��¼���������������� */
	public static final String err1=toString
		+" login, session amount is full";
	/** ��¼����֤�������޷����� */
	public static final String err2=toString
		+" login, dsc can not be accessed";
	/** ��½�����������޷����� */
	public static final String err3=toString
		+" login, server can not be accessed";
	/** ��½�����ڵ�¼�����ظ���¼ */
	public static final String err4=toString
		+" login, already in process of login";
	/** ��½���������ڱ����� */
	public static final String err5=toString
		+" login, session is in process of save";
	/** ��½����ǰ�Ự�޷��½� */
	public static final String err6=toString
		+" login, session can not be renewed";
	/** ���أ����������޷����� */
	public static final String err11=toString
		+" load, server can not be accessed";
	/** ���أ��������ڱ��� */
	public static final String err12=toString
		+" load, session is in process of save";
	/** �ǳ������������޷����� */
	public static final String err21=toString
		+" exit, server can not be accessed";
	/** �ǳ����������ڱ��� */
	public static final String err22=toString
		+" exit, session is in process of save";
	/** �ǳ������ڵ�¼�� */
	public static final String err23=toString
		+" exit, session is in process of login";
	/** ���£����������޷����� */
	public static final String err31=toString
		+" update, server can not be accessed";
	/** ���£��������ڱ��� */
	public static final String err32=toString
		+" update, session is in process of save";

	/** ���������ݿ��id,����Ϸ����￼��д�����飬��ʱ������д */
	int serverId;
	/** ��ʱʱ�� */
	int timeout;
	/** ��Ծ��ʱʱ�� */
	int activeTimeout;
	/** ���泬ʱʱ�� */
	int saveTimeout;
	/** �Ự���� */
	int sessionAmount;
	/** �Ự�����б� */
	SessionMap sm;
	/** CC������ */
	DSCCAccess ccAccess;
	/** �������ݶ�ȡ�� */
	BytesRW dcRW;
	/** �ͻ�������д���� */
	BytesRW userWriter;
	/** ����ʱ���¼� */
	TimerEvent collateTimerEvent;
	/** �����б� */
	AttributeList attributes;
	/** ����ѡ���� */
	CollateSelector selecter;
	/** ��ǰ��¼�б� */
	ArrayList loginList;
	/** �����б� */
	ArrayList saveList;
	/** ���滺�� */
	private ByteBuffer saveBuffer;
	/** �ǳ���֤�� */
	CheckNameFilter cnf;
	/** ���ݷ��ʶ��� */
	DBAccess access;

	/** ����һ����Ϸ���ݷ�������� */
	public DataServer()
	{
		this(new SessionMap());
	}
	/** ����һ������ָ���Ự�б����Ϸ���ݷ�������� */
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
	/** ���ûỰ�����б� */
	public SessionMap getSessionMap()
	{
		return sm;
	}
	/** �����֤���ķ����� */
	public DSCCAccess getDSCCAccess()
	{
		return ccAccess;
	}
	/** ������֤���ķ����� */
	public void setDSCCAccess(DSCCAccess access)
	{
		ccAccess=access;
		System.err.println("ccAccess ===="+ccAccess);
	}
	/** ������ݶ�д�� */
	public BytesRW getDCRW()
	{
		return dcRW;
	}
	/** �������ݶ�д�� */
	public void setDCRW(BytesRW reader)
	{
		this.dcRW=reader;
	}
	/** ��ȡ�û�����д���� */
	public BytesRW getUserWriter()
	{
		return userWriter;
	}
	/** �����û�����д���� */
	public void setUserWriter(BytesRW writer)
	{
		userWriter=writer;
	}
	/** ��ó�ʱʱ�� */
	public int getTimeout()
	{
		return timeout;
	}
	/** ���ó�ʱʱ�� */
	public void setTimeout(int timeout)
	{
		this.timeout=timeout;
	}
	/** ��û�Ծ��ʱʱ�� */
	public int getActiveTimeout()
	{
		return activeTimeout;
	}
	/** ���û�Ծ��ʱʱ�� */
	public void setActiveTimeout(int activeTimeout)
	{
		this.activeTimeout=activeTimeout;
	}
	/** ��ñ��泬ʱʱ�� */
	public int getSaveTimeout()
	{
		return saveTimeout;
	}
	/** ���ñ��泬ʱʱ�� */
	public void setSaveTimeout(int saveTimeout)
	{
		this.saveTimeout=saveTimeout;
	}
	/** ��ûỰ���� */
	public int getSessionAmount()
	{
		return sessionAmount;
	}
	/** ���ûỰ���� */
	public void setSessionAmount(int amount)
	{
		sessionAmount=amount;
	}
	/** ��������ʱ���¼� */
	public TimerEvent getCollateTimerEvent()
	{
		return collateTimerEvent;
	}
	/** ��������б� */
	public AttributeList getAttributes()
	{
		return attributes;
	}
	/** �Ƿ�Ϊ������������ */
	public boolean isThis(long userid)
	{
		// TODO Auto-generated method stub
		return true;
	}
	/** �����ǳ���֤�� */
	public void setCheckNameFilter(CheckNameFilter cnf)
	{
		this.cnf=cnf;
	}

	/***
	 * �������ݷ��ʶ���
	 * 
	 * @param access
	 */
	public void setDBAccess(DBAccess access)
	{
		this.access=access;
	}

	/**
	 * ����CC��֤��ע���˺�
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
	 * һ��ע��
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
	 * ��ȡ�ַ����� ��һ��ע���ã�������������Ż���
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

	/** sid��¼���Ƿ����¹����Ự */
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
	 * ��ȡ����ǳ�
	 * 
	 * @return
	 */
	public String getRandomName()
	{
		return cnf.getRandomName();
	}

	// /***
	// * ��ʱ��ӿ��ƣ��߻����ԣ�
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
	// if(player.getNickname().equals("ɳ����"))
	// {
	// for(int i=10002;i<10225;i++)
	// {
	// player.getCardBag().add(i,player.getAchievements());
	// }
	// }
	// }
	// }
	// }

	/** ���� */
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
	/** �ǳ� */
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

	/** ���ûỰ */
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

	/** ��ʼ��ʱ�� */
	public void timerStart()
	{
		TimerCenter.getMinuteTimer().add(collateTimerEvent);
	}

	// TODO ��ʱ����Ӧ
	/** ��ʱ���¼���Ӧ */
	public void onTimer(TimerEvent ev)
	{
		if(ev!=collateTimerEvent) return;
		collate(ev.getCurrentTime());
	}

	/** ���� */
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
	/** �˳�һ��Ự */
	public void exitSessions(ArrayList list,ByteBuffer data)
	{
		int i=0;
		for(int n=list.size();i<n;++i)
		{
			Session s=(Session)list.get(i);
			change(this,PRE_EXIT_CHANGED,s);
			// �����˳�ʱ��
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

	/** ����һ��Ự */
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

	/** ����һ��Ự��Ծ״̬ */
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

	/** ���ûỰ��Ծ״̬ */
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
	 * �ط�ǰ��������
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
	/** ����Ự */
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
			save=(crc!=s.getState(CRC_STATE));// ���ݸı�
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

	/** �رյ�ǰ��Ϸ���� */
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

	/** ������־ */
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
	 * ��˵�����Ự����ѡ����
	 * 
	 * @version 2013-4-24
	 * @author HYZ (huangyz1988@qq.com)
	 */
	class CollateSelector implements Selector
	{

		/** �Ƿ��Ƴ� */
		boolean remove;
		/** ���治Ҫ��Ծ�� */
		ArrayList list1;
		/** ������Ҫ��Ծ�� */
		ArrayList list2;
		/** ��ʱʱ�� */
		long time;

		/** ����һ������ѡ���� */
		CollateSelector()
		{
			list1=new ArrayList();
			list2=new ArrayList();
		}

		/** ѡ�� */
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
	 * ��������ǳƻ�ȡsession
	 * 
	 * @param nickName ����ǳ�
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
					return session; //TODO ���session
//				}
			}
		}
		return null;
	}

	/**
	 * �������ID��ȡsession
	 * 
	 * @param nickName ����ǳ�
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
