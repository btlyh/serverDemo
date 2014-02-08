package com.cambrian.common.db;

import com.cambrian.common.field.Field;
import com.cambrian.common.log.Logger;
import com.cambrian.common.net.DataAccessException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * 类说明：MongoDB数据存取器
 * 
 * @version 1.0
 * @date 2013-9-26
 * @author LazyBear
 */
public class MongoDBPersistence extends Persistence
{

	/* static fields */
	/** 日志记录 */
	private static final Logger log=Logger
		.getLogger(MongoDBPersistence.class);

	/* static methods */

	/* fields */
	/** 表存取器 */
	DBCollection coll;

	/** 表存取器 */
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
	/** 初始化 */
	public void init(MongoDB db,String table)
	{

		coll=db.getDBCollection(table);
		key_coll=db.getDBCollection(table+"_autoincrkey");
		if(log.isDebugEnabled())
			log.debug(table+","+coll.getName()+","+key_coll.getName());
	}

	/* methods */
	/**
	 * 增加 or 修改
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
			// TODO 是否成功
			return OK;
		}
	}

	/**
	 * 查找
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
	 * 删除
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
	 * 查看数据是否存在
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

	/** 总数量 */
	public long getCount()
	{
		return coll.getCount();
	}
	/** 指定条件的数量 */
	public long getCount(BasicDBObject query)
	{
		return coll.getCount(query);
	}
	/** 等于指定键的值的数量 */
	public long getCount(Field key)
	{
		return coll.getCount(new BasicDBObject(key.name,key.getValue()));
	}
	/** 不等于指定键的值的数量 */
	public long getNCount(Field key)
	{
		return coll.getCount(new BasicDBObject(key.name,new BasicDBObject(
			"$ne",key.getValue())));
	}
	/** 大于等于指定键的值的数量 */
	public long getGtECount(Field key)
	{
		return coll.getCount(new BasicDBObject(key.name,new BasicDBObject(
			"$gte",key.getValue())));
	}
	/** 小于指定键的值的数量 */
	public long getLtCount(Field key)
	{
		return coll.getCount(new BasicDBObject(key.name,new BasicDBObject(
			"$lt",key.getValue())));
	}
	/** 指定范围的值的数量[min,max),并为数字 */
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
	 * 获取所有数据
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
	 * 根据key获取num数量的数据
	 * 
	 * @param key
	 * @param fields
	 * @param num 数量
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
	 * 根据key获取所有数据
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
		// TODO 后面测试
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
		// TODO 后面测试
		catch(Exception e)
		{
			throw new DataAccessException(500,e.getMessage());
		}
		finally
		{
			cursor.close();
		}
	}
	/** 按key降序排序获取指定个 */
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

	/** 查询所有数据 */
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
