/** */
package com.cambrian.game.cc;

import com.cambrian.common.db.MongoDB;
import com.cambrian.common.db.MongoDBPersistence;
import com.cambrian.common.db.Persistence;
import com.cambrian.common.field.Field;
import com.cambrian.common.field.FieldKit;
import com.cambrian.common.field.IntField;
import com.cambrian.common.field.StringField;
import com.cambrian.common.log.Logger;
import com.cambrian.common.net.DataAccessException;
import com.cambrian.common.util.TextKit;
import com.cambrian.dfhm.Lang;

/***
 * 类说明：认证中心数据库访问器
 * 
 * @version 2013-5-6
 * @author HYZ (huangyz1988@qq.com)
 */
public class DBAccess
{

	/* static fields */
	/** 日志记录 */
	private static final Logger log=Logger.getLogger(DBAccess.class);

	/* fields */
	/** 表名 */
	String table="user";
	/** 数据库连接管理器 */
	MongoDB db;
	/** 数据存储器 */
	MongoDBPersistence persistence;

	/* properties */
	/** 设置数据库连接管理器 */
	public void setConnectionManager(MongoDB db)
	{
		this.db=db;
	}

	public void setMongoDBPersistence()
	{
		persistence=new MongoDBPersistence(db,table);
	}

	/** 设置表名 */
	public void setTable(String table)
	{
		this.table=table;
	}

	/* methods */
	/** 获取序列ID */
	private long nextId()
	{
		Field field=Field.createInt("userid",0);
		long i=persistence.getMaxValue(field);
		return ++i;
	}
	/**
	 * 注册检测
	 * 
	 * @param userName 用户名
	 * @return
	 */
	private boolean checkRegist(String userName)
	{
		User user=load(userName);
		if(user==null)
		{
			return true;
		}
		return false;
	}
	/** 注册帐号 */
	public boolean register(String str,int gameserver)
	{
		String[] strs=TextKit.split(str,":");
		if(strs.length==0||strs.length%2!=0)
			throw new DataAccessException(
				DataAccessException.SERVER_CMSG_ERROR,
				"invalid register message");
		if(checkRegist(strs[0]))
		{
			User user=new User((int)nextId(),strs[0],strs[1]);
			Field key=FieldKit.create("userid",user.userid);
			Field[] fields=mapping(user);
			int i=persistence.set(key,fields);
			if(i==Persistence.EXCEPTION) return false;
			return true;
		}
		return false;
	}
	/** 校验帐号密码，返回user */
	public User valid(String id,String pw)
	{
		if(log.isInfoEnabled()) log.info(",valid,id="+id+",pw="+pw);
		User user=load(id);
		if(user==null)
		{
			if(log.isDebugEnabled())
				log.debug("valid fail, id="+id+", pw="+pw);
			return null;
		}
		String pwd=user.passwd;
		if((pwd==null&&pw!=null)||(pwd!=null&&pw==null)
			||(pwd!=null&&(!pwd.equalsIgnoreCase(pw))))
		{
			if(log.isDebugEnabled())
				log.debug(" valid fail, id="+id+", pw="+pw);
			user=new User();
			user.userid=-1;
			return user;
		}
		return user;
	}
	
	//
	// /** 校验帐号密码，只用于游戏服务器加载不在线的玩家数据 */
	// public String valid(String id,int gameserver,String address)
	// {
	// String sql="select "+getPWColumn()+","+getUidColumn()
	// +",gameserver from "+getTable()+" where "+getIdColumn()+"='"+id
	// +"'";
	// Fields fields;
	// try
	// {
	// // TODO---------------如何确定cm--------------------
	// ConnectionManager cm=(ConnectionManager)dbList
	// .getConnectionManager(gameserver);
	// cm=this.cm;
	// fields=SqlKit.query(cm,sql);
	// }
	// catch(Exception e)
	// {
	// throw new DataAccessException(
	// DataAccessException.SERVER_INTERNAL_ERROR,"valid, db error");
	// }
	// if(fields==null) return null;
	// Field array[]=fields.getFields();
	// String pw=(String)array[0].getValue();
	// int uid=((IntField)array[1]).value;
	// int gameserverid=((IntField)array[2]).value;
	// if(uid==0)
	// throw new DataAccessException(
	// DataAccessException.SERVER_INTERNAL_ERROR,
	// "valid, db uidColumn must be no null");
	// else if(gameserverid!=gameserver&&(gameserverid!=0))
	// throw new DataAccessException(
	// DataAccessException.SERVER_INTERNAL_ERROR,
	// "valid, db gameserver error");
	// else
	// return uid+((long)gameserver<<32)+":"+gameserverid+":"+pw;
	// }

	// /** 更新用户信息 */
	// public boolean update(int gameserver,String id,String str)
	// {
	// String[] strs=TextKit.split(str,":");
	// id=TextKit.split(id,":")[0];
	// if(strs.length==0||strs.length%2!=0)
	// throw new DataAccessException(
	// DataAccessException.SERVER_CMSG_ERROR,
	// "invalid update message");
	//
	// StringBuffer sql=new StringBuffer();
	// sql.append("update ").append(getTable());
	// sql.append(" set ");
	// for(int i=0;i<strs.length;i+=2)
	// {
	// sql.append(strs[i]).append("='").append(strs[i+1]).append("'");
	// if(i+2<strs.length) sql.append(",");
	// }
	// sql.append(" where ");
	// sql.append(getIdColumn()).append("='").append(id).append("'");
	//
	// try
	// {
	// // TODO---------------如何确定cm--------------------
	// ConnectionManager cm=(ConnectionManager)dbList
	// .getConnectionManager(gameserver);
	// cm=this.cm;
	// SqlKit.execute(cm,sql.toString());
	// if(log.isDebugEnabled()) log.debug("update ok, sql:="+sql);
	// }
	// catch(Exception e)
	// {
	// if(log.isWarnEnabled())
	// log.warn("update error, id="+id+", sqlstr="+sql,e);
	// return false;
	// }
	// return true;
	// }

	// /** 查看封号状态 */
	// public long getForbidStatus(String username,int gameserver)
	// {
	// String sql="select forbidtime from "+getTable()+" where "
	// +getIdColumn()+"='"+username+"'";
	// Fields fields;
	// try
	// {
	// // TODO---------------如何确定cm--------------------
	// ConnectionManager cm=(ConnectionManager)dbList
	// .getConnectionManager(gameserver);
	// cm=this.cm;
	// fields=SqlKit.query(cm,sql);
	// }
	// catch(Exception e)
	// {
	// throw new DataAccessException(
	// DataAccessException.SERVER_INTERNAL_ERROR,"valid, db error");
	// }
	// if(fields==null) return 0;
	// return ((LongField)fields.get("forbidtime")).value;
	// }

	// /** (合服修改) 查看服务器是否存在该玩家 */
	// public int getUserServerId(int gameserver,String username)
	// {
	// String sql="select gameserver from "+getTable()+" where "
	// +getIdColumn()+"='"+username+"'";
	// Fields fields;
	// try
	// {
	// // TODO---------------如何确定cm--------------------
	// ConnectionManager cm=(ConnectionManager)dbList
	// .getConnectionManager(gameserver);
	// cm=this.cm;
	// fields=SqlKit.query(cm,sql);
	// }
	// catch(Exception e)
	// {
	// throw new DataAccessException(
	// DataAccessException.SERVER_INTERNAL_ERROR,"valid, db error");
	// }
	// if(fields==null) return 0;
	// return ((IntField)fields.get("gameserver")).value;
	// }

	// /** 查看玩家信息 1账号,2昵称,3userId */
	// public ByteBuffer getUserInfo(int type,int gameserver,String[] strs)
	// {
	// ByteBuffer data=new ByteBuffer();
	// if(strs.length==0) return data;
	// StringBuffer strb=new StringBuffer();
	// strb.append("select ").append(getIdColumn()).append(",nickname,")
	// .append(getUidColumn()).append(",gameserver from ")
	// .append(getTable()).append(" where ");
	// if(type==1)
	// strb.append(getIdColumn());
	// else if(type==2)
	// strb.append("nickname");
	// else
	// {
	// strb.append(getUidColumn());
	// // TODO 合服修改
	// for(int i=0;i<strs.length;i++)
	// {
	// long num=TextKit.parseLong(strs[i]);
	// strs[i]=(int)num+"";
	// }
	// }
	// strb.append(" in(");
	//
	// for(int i=0;i<strs.length;i++)
	// {
	// if(i>0) strb.append(",");
	// if(type==1||type==2) strb.append("'");
	// strb.append(strs[i]);
	// if(type==1||type==2) strb.append("'");
	// }
	// // TODO-----------------------
	// strb.append(") and gameserver=").append(gameserver);
	// try
	// {
	// // TODO---------------如何确定cm--------------------
	// ConnectionManager cm=(ConnectionManager)dbList
	// .getConnectionManager(gameserver);
	// cm=this.cm;
	// Fields[] fields=SqlKit.querys(cm,strb.toString());
	// if(fields==null) return data;
	// data.writeInt(fields.length);
	// for(int i=0;i<fields.length;i++)
	// {
	// String id=((StringField)fields[i].get(getIdColumn())).value;
	// String nickname=((StringField)fields[i].get("nickname")).value;
	// int uid=((IntField)fields[i].get(getUidColumn())).value;
	// int gameserver_=((IntField)fields[i].get("gameserver")).value;
	// data.writeUTF(id);
	// data.writeUTF(nickname);
	// data.writeUTF(uid+((long)gameserver<<32)+"");
	// data.writeUTF(""+gameserver_);
	// }
	// }
	// catch(Exception e)
	// {
	// throw new DataAccessException(
	// DataAccessException.SERVER_INTERNAL_ERROR,"valid, db error");
	// }
	// return data;
	// }

	// /** 增加成功邀请次数 */
	// public void addInviteTimes(int gameserver,String[] strs)
	// {
	// String inviteuser=null;
	// int i=0;
	// for(;i<strs.length;i+=2)
	// {
	// if(strs[i].equals("inviter"))
	// {
	// inviteuser=strs[i+1];
	// break;
	// }
	// }
	// if(inviteuser==null) return;
	//
	// String sql="select * from "+getTable()+" where nickname='"
	// +inviteuser+"'";
	// // TODO---------------如何确定cm--------------------
	// ConnectionManager cm=(ConnectionManager)dbList
	// .getConnectionManager(gameserver);
	// cm=this.cm;
	// Connection c=cm.getConnection();
	// Statement st=null;
	// ResultSet rs=null;
	// try
	// {
	// st=c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
	// ResultSet.CONCUR_UPDATABLE);
	// rs=st.executeQuery(sql);
	//
	// if(rs.next())
	// {
	// rs.updateInt("invitetimes",rs.getInt("invitetimes")+1);
	// rs.updateRow();
	// }
	// else
	// {
	// strs[i+1]=null;
	// }
	// }
	// catch(SQLException e)
	// {
	// strs[i+1]=null;
	// e.printStackTrace();
	// }
	// finally
	// {
	// DBKit.close(st,rs);
	// DBKit.close(c);
	// }
	// }

	/** 映射成域对象 */
	public Field[] mapping(User user)
	{
		Field[] array=new Field[3];
		array[0]=FieldKit.create("userid",user==null?0:user.userid);
		array[1]=FieldKit.create("username",user==null?(String)null
			:user.username);
		array[2]=FieldKit.create("passwd",user==null?(String)null
			:user.passwd);
		return array;
	}

	/** 映射成域对象 */
	public void set(User user,Field[] fields)
	{
		user.userid=((IntField)fields[0]).value;
		user.username=((StringField)fields[1]).value;
		user.passwd=((StringField)fields[2]).value;
	}

	public User load(String username)
	{
		Field key=FieldKit.create("username",username);
		Field[] fields=mapping(null);
		int i=persistence.get(key,fields);
		if(i==Persistence.EXCEPTION)
			throw new DataAccessException(
				DataAccessException.SERVER_INTERNAL_ERROR,Lang.F611_DE);
		if(i==Persistence.RESULTLESS) return null;
		User user=new User();
		set(user,fields);
		return user;
	}
}
