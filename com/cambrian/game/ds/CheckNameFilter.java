package com.cambrian.game.ds;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.cambrian.common.util.MathKit;

/**
 * 类说明：c
 * 
 * @author：LazyBear
 */
public class CheckNameFilter
{

	/* static fields */

	/* static methods */

	/* fields */
	/** 可用名字库 */
	public ArrayList<String> nameList=new ArrayList<String>();
	/** 过滤字符 */
	public ArrayList<String> filterStr=new ArrayList<String>();

	/* constructors */

	/* properties */

	/* init start */

	/* methods */
	/** 过滤字符加载 */
	public void loadFilter() throws IOException
	{
		BufferedReader br=new BufferedReader(new InputStreamReader(
			new FileInputStream("./txt/filter.txt"),"UTF-8"));
		String name=br.readLine();
		while(name!=null)
		{
			filterStr.add(name);
			name=br.readLine();
		}
	}

	/** 名字加载 */
	public void loadMingZi() throws IOException
	{
		// 加载已用过名字名字
		ArrayList<String> temp=new ArrayList<String>();
		BufferedReader br=new BufferedReader(new InputStreamReader(
			new FileInputStream("./txt/mingzi_use.txt")));
		String name=br.readLine();
		while(name!=null)
		{
			temp.add(name);
			name=br.readLine();
		}

		// 加载可用名字
		br=new BufferedReader(new InputStreamReader(new FileInputStream(
			"./txt/xingMing.txt"),"UTF-8"));
		name=br.readLine();
		boolean b=false;
		while(name!=null)
		{
			for(int i=0;i<temp.size();i++)
			{
				if(name.equals(temp.get(i)))
				{
					b=false;
					break;
				}
				if(i==temp.size()-1) b=true;
			}
			if(b) nameList.add(name);
			name=br.readLine();
		}
	}
	/** 随机获取名字 */
	public String getRandomName()
	{
		if(nameList.size()<=0) return null;
		int index=MathKit.randomValue(0,nameList.size()-1);
		String name=nameList.get(index).toString();
		return name;
	}
}
