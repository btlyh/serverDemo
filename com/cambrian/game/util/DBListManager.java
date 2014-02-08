package com.cambrian.game.util;

import com.cambrian.common.db.MongoDB;
import com.cambrian.common.log.Logger;
import com.cambrian.common.util.IntKeyHashMap;

public class DBListManager
{

	public static Logger log=Logger.getLogger(DBListManager.class);
	private IntKeyHashMap connectionManagerList=new IntKeyHashMap();

	public void addConnectionManager(int serverId,MongoDB db)
	{
		if(log.isDebugEnabled())
			log.debug("<ADD KEY:"+serverId+"  VALUE:"+db+">");
		if(db==null) return;
		connectionManagerList.put(serverId,db);
	}

	public MongoDB getConnectionManager(int serverId)
	{
		if(log.isInfoEnabled())
			log.info("Get the connectionManager for [ "+serverId+" ]");
		return (MongoDB)connectionManagerList.get(serverId);
	}

	public void remove(int serverId)
	{
		connectionManagerList.remove(serverId);
	}

	public void removeAll()
	{
		int size=connectionManagerList.size();
		for(int i=size;i>0;i--)
		{
			connectionManagerList.remove(i-1);
		}
	}

	public String toString()
	{
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<connectionManagerList.size();i++)
		{
			sb.append(i+1).append(":");
			sb.append(connectionManagerList.get(i+1)).append("\n");
		}
		return sb.toString();
	}
}
