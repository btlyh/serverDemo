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
 * ��˵������֤����
 * 
 * @version 2013-5-9
 * @author HYZ (huangyz1988@qq.com)
 */
public class CertifyCenter extends ChangeListenerList implements
	TimerListener,CloseAble
{

	/** ��־��¼ */
	private static final Logger log=Logger.getLogger(CertifyCenter.class);

	/**
	 * session�ı��־��������֤ǰ�ı�1����֤�ı�2����֤����ı�3����֤�汾�ı�5����¼�ı�11����ı�21���˳��ı�31�����¸ı�41
	 * ��ע��ı�51�����������ı�61
	 */
	public static final int PRE_CERTIFY_CHANGED=1,CERTIFY_CHANGED=2,
					CERTIFY_BUFFER_CHANGED=3,CERTIFY_VISITOR_CHANGED=5,
					LOGIN_CHANGED=11,ACTIVE_CHANGED=21,EXIT_CHANGED=31,
					UPDATE_CHANGED=41,REGISTER_CHANGED=51,
					ONLINE_NUM_CHANGED=61;

	/** �Ự�������������� */
	public static final String SESSION_AMOUNT="sessionAmount";
	/** ��Ч����֤��Ϣ */
	public static final String err1=CertifyCenter.class.getName()
		+" certify, invalid certify message";
	/** �Ѿ��ڵ�¼������ */
	public static final String err2=CertifyCenter.class.getName()
		+" certify, already in process of certify";
	/** ��Ч���û���*/
	public static final String err3=Lang.F616_USER;
	/** ��Ч������*/
	public static final String err4=Lang.F617_PW;
	/** ��Ч��sid */
	public static final String err11=CertifyCenter.class.getName()
		+" load, invalid sid";

	/** Sid������ */
	SidEncoder sidEncoder=new SidEncoder();
	/** Sid�Ự�� */
	SidSessionMap ssm;
	/** ���ݴ�ȡ */
	DBAccess access;
	/** ��ʱʱ�䣬Ĭ��Ϊ30���� */
	int timeout=1800000;
	/** collate��ʱ����60�� */
	TimerEvent collateTimerEvent=new TimerEvent(this,"collate",60000);
	/** ��ʱ��֤����ֹ���һ��ʱ������֤���� */
	ArrayList certifyList=new ArrayList();
	/** ���Ա� */
	AttributeList attributes=new AttributeList();
	/** ����ѡ���� */
	CollateSelector selecter=new CollateSelector();

	/* constructors */
	/** ����һ���������Ĺ����� */
	public CertifyCenter()
	{
		this(new SidSessionMap());
	}
	/** ��ָ����Sid�Ự����һ���������Ĺ����� */
	public CertifyCenter(SidSessionMap ssm)
	{
		this.ssm=ssm;
	}
	/* properties */
	/** ���Sid�Ự�� */
	public SidSessionMap getSidSessionMap()
	{
		return ssm;
	}
	/** ���Sid������ */
	public SidEncoder getSidEncoder()
	{
		return sidEncoder;
	}
	/** ����Sid������ */
	public void setSidEncoder(SidEncoder encoder)
	{
		sidEncoder=encoder;
	}
	/** �����֤���ʽӿ� */
	public DBAccess getDBAccess()
	{
		return access;
	}
	/** ������֤���ʽӿ� */
	public void setDBAccess(DBAccess access)
	{
		this.access=access;
	}
	/** ��ó�ʱʱ�� */
	public int getTimeout()
	{
		return timeout;
	}
	/** ���ó�ʱʱ�� */
	public void setTimeout(int t)
	{
		timeout=t;
	}
	/** ���collate��ʱ�� */
	public TimerEvent getCollateTimerEvent()
	{
		return collateTimerEvent;
	}
	/** ������Ա� */
	public AttributeList getAttributes()
	{
		return attributes;
	}
	/* methods */
	/** ��֤����(info:[id,md5(pw),serverId])������Session(SourceΪsid��ReferenceΪuid) */
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
	/** ��֤���� */
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
//	/** ��֤����������Session��ֻ������Ϸ���������ز����ߵ�������� */
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
//				s.setAttribute("serverId",strs[1]);// ԭ����strs[1]
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
	/** ��ָ����sid���ػỰ */
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
	/** ���·��� */
	public void update(String sid,String serverId,Session session)
	{
		Session s=ssm.getBySid(sid);
		if(s!=null) s.setAttribute("serverId",serverId);
		change(this,UPDATE_CHANGED,sid,s,session);
	}
	/** sid����� */
	public void active(String sid,Session session)
	{
		Session s=ssm.getBySid(sid);
		if(s!=null) s.setActiveTime(System.currentTimeMillis());
		change(this,ACTIVE_CHANGED,sid,s,session);
	}
	/** sid�˳����� */
	public void exit(String sid,Session session)
	{
		Session s=ssm.getBySid(sid);
		if(s!=null)
		{
			ssm.remove(s);
		}

		change(this,EXIT_CHANGED,sid,ssm.getBySid(sid),session);
	}
	/** �û����������˳����� */
	public void exit(String id,String pw,Session session)
	{
		Session s=ssm.get(id);
		if(s!=null)
		{
			ssm.remove(s);
		}
		change(this,EXIT_CHANGED,id,s);
	}
	/** ��ʱ����ʼ���� */
	public void timerStart()
	{
		TimerCenter.getMinuteTimer().add(collateTimerEvent);
	}
	/** ��ʱ�¼��ļ����ӿڷ��� */
	public void onTimer(TimerEvent ev)
	{
		if(ev==collateTimerEvent) collate(ev.getCurrentTime());
	}
	/** ������ */
	public void collate(long time)
	{
		if(log.isInfoEnabled()) log.info("collate, size="+ssm.size());
		selecter.time=time-timeout;
		ssm.select(selecter);
		attributes.set(SESSION_AMOUNT,String.valueOf(ssm.size()));
		change(this,ONLINE_NUM_CHANGED,ssm.size());
		if(log.isInfoEnabled()) log.info("collate ok, size="+ssm.size());
	}
	/** �رշ��� */
	public void close(ByteBuffer data)
	{
		if(log.isInfoEnabled()) log.info("close, size="+ssm.size());
		TimerCenter.getMinuteTimer().remove(collateTimerEvent);
		ssm.clear();
		attributes.set(SESSION_AMOUNT,"0");
		if(log.isInfoEnabled()) log.info("close ok, size="+ssm.size());
	}
	/** �رշ������رս����ã� */
	public void close()
	{
		this.close(null);
		if(log.isInfoEnabled()) log.info(" CC CLOSE! ");
		System.exit(0);
	}

	/** ��˵��������ѡ���� */
	class CollateSelector implements Selector
	{

		/** ��ʱʱ�� */
		long time;

		/** �ж϶����Ƿ�ѡ�� */
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