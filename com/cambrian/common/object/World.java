/**
 * 
 */
package com.cambrian.common.object;

import com.cambrian.common.util.IntKeyHashMap;

/**
 * ��˵��������
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class World extends SpaceObject
{

	/* static fields */

	/* static methods */

	/* fields */
	/** ���Ա� */
	IntKeyHashMap map;
	/** ���� */
	SpaceObject[] buffer;

	/* constructors */

	/** ����һ������ */
	public World()
	{
		map=new IntKeyHashMap();
	}
	/** ����һ�����磬����capacity�ǳ�ʼ�Ľڵ����� */
	public World(int capacity)
	{
		map=new IntKeyHashMap(capacity);
	}
	/* properties */

	/* init start */

	/* methods */
	/** ʱ�����з��� */
	public void run(long time)
	{
		SpaceObject[] temp=toArray();
		for(int i=temp.length-1;i>=0;i--)
			temp[i].run(time);
	}
	/* �������� */
	/** ��ýڵ������ */
	public int size()
	{
		synchronized(map)
		{
			return map.size();
		}
	}
	/** ���ָ��id�Ľڵ� */
	public SpaceObject get(int id)
	{
		synchronized(map)
		{
			return (SpaceObject)map.get(id);
		}
	}
	/** ���ָ���ڵ� */
	public void add(SpaceObject node)
	{
		synchronized(map)
		{
			map.put(node.getId(),node);
			buffer=null;
		}
	}
	/** ���ָ���ڵ����� */
	public void add(SpaceObject[] nodes,int index,int length)
	{
		synchronized(map)
		{
			for(int i=index+length-1;i>=index;i--)
				map.put(nodes[i].getId(),nodes[i]);
			buffer=null;
		}
	}
	/** �Ƴ�ָ���ڵ� */
	public SpaceObject remove(int id)
	{
		synchronized(map)
		{
			SpaceObject n=(SpaceObject)map.remove(id);
			if(n!=null) buffer=null;
			return n;
		}
	}
	/** ���ȫ���ڵ� */
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
	/** ������нڵ� */
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
