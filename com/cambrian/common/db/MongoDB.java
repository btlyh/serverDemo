package com.cambrian.common.db;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.cambrian.common.net.DataAccessException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
 * 类说明：mongoDB数据库连接管理器
 * 
 * @version 1.0
 * @date 2013-9-26
 * @author maxw<woldits@qq.com>
 */
public class MongoDB
{
	/** 主机 */
	String host="localhost";
	/** 端口 */
	int[] ports=new int[]{27017,27018,27019};
	/** 数据库 */
	String database="test";
	/** 用户名 */
	String user;
	/** 密码 */
	String password;
	/** 数据库 */
	DB db;
	
	/**
	 * @param host the host to set
	 */
	public void setHost(String host)
	{
		this.host=host;
	}
	/**
	 * @param ports the ports to set
	 */
	public void setPorts(int[] ports)
	{
		this.ports=ports;
	}
	/**
	 * @param database the database to set
	 */
	public void setDatabase(String database)
	{
		this.database=database;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user)
	{
		this.user=user;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password=password;
	}
	/**
	 * @throws UnknownHostException
	 */
	public void init() throws UnknownHostException
	{
		ArrayList<ServerAddress> array=new ArrayList<ServerAddress>(ports.length);
		for(int i=0;i<ports.length;i++)
		{
			array.add(new ServerAddress(host,ports[i]));
		}
		MongoClient mongoClient=new MongoClient(array);

		db=mongoClient.getDB(database);

		if(user!=null&&password!=null)
		{
			boolean auth = db.authenticate(user, password.toCharArray());
			if(!auth)
				throw new DataAccessException(500,"authenticate failure ,user="+user+",password="+password);
			System.err.println(auth);
		}
		System.err.println(db);
	}
	/** 获取指定表连接器 */
	public DBCollection getDBCollection(String table)
	{
		return db.getCollection(table);
	}
}