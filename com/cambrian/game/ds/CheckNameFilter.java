package com.cambrian.game.ds;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.cambrian.common.util.MathKit;

/**
 * ��˵����c
 * 
 * @author��LazyBear
 */
public class CheckNameFilter
{

	/* static fields */

	/* static methods */

	/* fields */
	/** �������ֿ� */
	public ArrayList<String> nameList=new ArrayList<String>();
	/** �����ַ� */
	public ArrayList<String> filterStr=new ArrayList<String>();

	/* constructors */

	/* properties */

	/* init start */

	/* methods */
	/** �����ַ����� */
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

	/** ���ּ��� */
	public void loadMingZi() throws IOException
	{
		// �������ù���������
		ArrayList<String> temp=new ArrayList<String>();
		BufferedReader br=new BufferedReader(new InputStreamReader(
			new FileInputStream("./txt/mingzi_use.txt")));
		String name=br.readLine();
		while(name!=null)
		{
			temp.add(name);
			name=br.readLine();
		}

		// ���ؿ�������
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
	/** �����ȡ���� */
	public String getRandomName()
	{
		if(nameList.size()<=0) return null;
		int index=MathKit.randomValue(0,nameList.size()-1);
		String name=nameList.get(index).toString();
		return name;
	}
}
