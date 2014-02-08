/**
 * 
 */
package com.cambrian.common.object;

import com.cambrian.common.util.IntKeyHashMap;

/**
 * 类说明：世界
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class World extends SpaceObject
{

	/* static fields */

	/* static methods */

	/* fields */
	/** 属性表 */
	IntKeyHashMap map;
	/** 缓存 */
	SpaceObject[] buffer;

	/* constructors */

	/** 构造一个世界 */
	public World()
	{
		map=new IntKeyHashMap();
	}
	/** 构造一个世界，参数capacity是初始的节点容量 */
	public World(int capacity)
	{
		map=new IntKeyHashMap(capacity);
	}
	/* properties */

	/* init start */

	/* methods */
	/** 时间运行方法 */
	public void run(long time)
	{
		SpaceObject[] temp=toArray();
		for(int i=temp.length-1;i>=0;i--)
			temp[i].run(time);
	}
	/* 容器方法 */
	/** 获得节点的数量 */
	public int size()
	{
		synchronized(map)
		{
			return map.size();
		}
	}
	/** 获得指定id的节点 */
	public SpaceObject get(int id)
	{
		synchronized(map)
		{
			return (SpaceObject)map.get(id);
		}
	}
	/** 添加指定节点 */
	public void add(SpaceObject node)
	{
		synchronized(map)
		{
			map.put(node.getId(),node);
			buffer=null;
		}
	}
	/** 添加指定节点数组 */
	public void add(SpaceObject[] nodes,int index,int length)
	{
		synchronized(map)
		{
			for(int i=index+length-1;i>=index;i--)
				map.put(nodes[i].getId(),nodes[i]);
			buffer=null;
		}
	}
	/** 移除指定节点 */
	public SpaceObject remove(int id)
	{
		synchronized(map)
		{
			SpaceObject n=(SpaceObject)map.remove(id);
			if(n!=null) buffer=null;
			return n;
		}
	}
	/** 获得全部节点 */
	public SpaceObject[] toArray()
	{
		synchronized(map)
		{
			SpaceObject[] temp=buffer;
			if(temp!=null) return temp;
			temp=new SpaceObject[map.size()];
			map.valueArray(temp);
			buffer=temp;
			return temp;
		}
	}
	/** 清除所有节点 */
	public void clear()
	{
		synchronized(map)
		{
			map.clear();
			buffer=null;
		}
	}

	/* common methods */

	/* inner class */

}
