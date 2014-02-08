package com.cambrian.common.db;

import com.cambrian.common.field.Field;
import com.cambrian.common.log.Logger;
import com.cambrian.common.net.DataAccessException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * ��˵����MongoDB���ݴ�ȡ��
 * 
 * @version 1.0
 * @date 2013-9-26
 * @author LazyBear
 */
public class MongoDBPersistence extends Persistence
{

	/* static fields */
	/** ��־��¼ */
	private static final Logger log=Logger
		.getLogger(MongoDBPersistence.class);

	/* static methods */

	/* fields */
	/** ���ȡ�� */
	DBCollection coll;

	/** ���ȡ�� */
	DBCollection key_coll;

	/* constructors */

	public MongoDBPersistence()
	{

	}
	public MongoDBPersistence(MongoDB db,String table)
	{
		this.init(db,table);
	}
	/* properties */

	/* init start */
	/** ��ʼ�� */
	public void init(MongoDB db,String table)
	{

		coll=db.getDBCollection(table);
		key_coll=db.getDBCollection(table+"_autoincrkey");
		if(log.isDebugEnabled())
			log.debug(table+","+coll.getName()+","+key_coll.getName());
	}

	/* methods */
	/**
	 * ���� or �޸�
	 */
	public int set(Field key,Field[] fields)
	{
		BasicDBObject doc=new BasicDBObject(fields.length);
		for(int i=0;i<fields.length;i++)
		{
			doc.append(fields[i].name,fields[i].getValue());
		}

		DBObject obj=getDBObject(key);
		if(obj==null)
		{
			coll.insert(doc);
			return ADD;
		}
		else
		{
			DBObject upObj=new BasicDBObject("$set",doc);
			coll.update(obj,upObj);
			// TODO �Ƿ�ɹ�
			return OK;
		}
	}

	/**
	 * ����
	 */
	public int get(Field key,Field[] fields)
	{
		DBObject obj=getDBObject(key);
		if(obj==null) return RESULTLESS;
		for(int i=0;i<fields.length;i++)
		{
			fields[i].setValue(obj.get(fields[i].name));
		}
		return OK;
	}
	/**
	 * ɾ��
	 */
	@Override
	public int remove(Field key)
	{
		BasicDBObject query=new BasicDBObject(key.name,key.getValue());
		DBObject obj=coll.findAndRemove(query);
		if(obj==null) return RESULTLESS;
		return OK;
	}

	/**
	 * �鿴�����Ƿ����
	 * 
	 * @param key
	 * @return
	 */
	public boolean isExist(Field key)
	{
		boolean isExist=false;
		DBObject obj=getDBObject(key);
		if(obj!=null) isExist=true;
		return isExist;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.cambrian.common.db.Persistence#remove(com.cambrian.common.field
	 * .Field)
	 */

	/** ������ */
	public long getCount()
	{
		return coll.getCount();
	}
	/** ָ������������ */
	public long getCount(BasicDBObject query)
	{
		return coll.getCount(query);
	}
	/** ����ָ������ֵ������ */
	public long getCount(Field key)
	{
		return coll.getCount(new BasicDBObject(key.name,key.getValue()));
	}
	/** ������ָ������ֵ������ */
	public long getNCount(Field key)
	{
		return coll.getCount(new BasicDBObject(key.name,new BasicDBObject(
			"$ne",key.getValue())));
	}
	/** ���ڵ���ָ������ֵ������ */
	public long getGtECount(Field key)
	{
		return coll.getCount(new BasicDBObject(key.name,new BasicDBObject(
			"$gte",key.getValue())));
	}
	/** С��ָ������ֵ������ */
	public long getLtCount(Field key)
	{
		return coll.getCount(new BasicDBObject(key.name,new BasicDBObject(
			"$lt",key.getValue())));
	}
	/** ָ����Χ��ֵ������[min,max),��Ϊ���� */
	public long getSpaceCount(Field key,Object min,Object max)
	{
		return coll.getCount(new BasicDBObject(key.name,new BasicDBObject(
			"$gte",min).append("$lt",max)));
	}
	public long getMaxValue(Field key)
	{
		long maxId=0;
		DBCursor cr=coll.find();
		while(cr.hasNext())
		{
			DBObject obj=cr.next();
			if(maxId<Long.parseLong(obj.get(key.name).toString()))
				maxId=Long.parseLong(obj.get(key.name).toString());
		}
		return maxId;
	}
	/**
	 * ��ȡ��������
	 * 
	 * @param fields
	 * @return
	 */
	public Field[][] gets(Field[] fields)
	{
		try
		{
			DBObject[] objs=getAllObjects();
			if(objs==null) return null;
			Field[][] fieldss=new Field[objs.length][];
			for(int i=0;i<objs.length;i++)
			{
				fieldss[i]=new Field[fields.length];
				for(int j=0;j<fields.length;j++)
				{
					fieldss[i][j]=Field.coppyField(fields[j]);
					fieldss[i][j].setValue(objs[i].get(fieldss[i][j].name));
				}
			}
			return fieldss;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	/**
	 * ����key��ȡnum����������
	 * 
	 * @param key
	 * @param fields
	 * @param num ����
	 * @return
	 */
	public Field[][] gets(Field key,Field[] fields,int num)
	{
		try
		{
			DBObject[] objs=getDBObjects(key,num);
			if(objs==null) return null;
			Field[][] fieldss=new Field[objs.length][];
			for(int i=0;i<objs.length;i++)
			{
				fieldss[i]=new Field[fields.length];
				for(int j=0;j<fields.length;j++)
				{
					fieldss[i][j]=Field.coppyField(fields[j]);
					fieldss[i][j].setValue(objs[i].get(fieldss[i][j].name));
				}
			}
			return fieldss;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	/**
	 * ����key��ȡ��������
	 */
	public Field[][] gets(Field key,Field[] fields)
	{
		try
		{
			DBObject[] objs=getDBObjects(key);
			if(objs==null) return null;
			Field[][] fieldss=new Field[objs.length][];
			for(int i=0;i<objs.length;i++)
			{
				fieldss[i]=new Field[fields.length];
				for(int j=0;j<fields.length;j++)
				{
					fieldss[i][j]=Field.coppyField(fields[j]);
					fieldss[i][j].setValue(objs[i].get(fieldss[i][j].name));
				}
			}
			return fieldss;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	/** */
	private DBObject getDBObject(Field key)
	{
		BasicDBObject query=new BasicDBObject(key.name,key.getValue());
		DBCursor cursor=coll.find(query);
		try
		{
			while(cursor.hasNext())
			{
				return cursor.next();
			}
		}
		// TODO �������
		catch(Exception e)
		{
			throw new DataAccessException(500,e.getMessage());
		}
		finally
		{
			cursor.close();
		}
		return null;
	}
	/**  */
	private DBObject[] getDBObjects(Field key)
	{
		BasicDBObject query=new BasicDBObject(key.name,key.getValue());
		DBCursor cursor=coll.find(query);
		int num=cursor.count();
		int i=0;
		DBObject[] objs=new BasicDBObject[num];
		try
		{
			while(cursor.hasNext())
			{
				objs[i++]=cursor.next();
			}
			return objs;
		}
		// TODO �������
		catch(Exception e)
		{
			throw new DataAccessException(500,e.getMessage());
		}
		finally
		{
			cursor.close();
		}
	}
	/** ��key���������ȡָ���� */
	private DBObject[] getDBObjects(Field key,int count)
	{
		BasicDBObject query=new BasicDBObject(key.name,-1);
		if(log.isDebugEnabled()) log.debug("key="+key.name+",count="+count);
		DBCursor cursor=coll.find().sort(query).limit(count);
		int num=cursor.count();
		int i=0;
		DBObject[] objs=new BasicDBObject[num];
		try
		{
			while(cursor.hasNext())
			{
				objs[i++]=cursor.next();
			}
			return objs;
		}
		catch(Exception e)
		{
			throw new DataAccessException(500,e.getMessage());
		}
		finally
		{
			cursor.close();
		}
	}

	/** ��ѯ�������� */
	private DBObject[] getAllObjects()
	{
		DBCursor cursor=coll.find();
		int num=cursor.count();
		int i=0;
		DBObject[] objs=new BasicDBObject[num];
		try
		{
			while(cursor.hasNext())
			{
				objs[i++]=cursor.next();
			}
			return objs;
		}
		catch(Exception e)
		{
			throw new DataAccessException(500,e.getMessage());
		}
		finally
		{
			cursor.close();
		}
	}

	// public long getAutoIncrId(String key)
	// {
	// if(log.isDebugEnabled()) log.debug("getAutoIncrId,key="+key);
	// BasicDBObject query=new BasicDBObject();
	// query.put("name",key);
	// BasicDBObject update=new BasicDBObject();
	// update.put("$inc",new BasicDBObject("id",(long)1));
	// DBObject dbObject2=key_coll.findAndModify(query,null,null,false,
	// update,true,false);
	// Long id=(Long)dbObject2.get("id");
	// if(log.isDebugEnabled())
	// log.debug("getAutoIncrId ok,key="+key+","+id);
	// return id;
	// }
	/* common methods */

	/* inner class */
}
