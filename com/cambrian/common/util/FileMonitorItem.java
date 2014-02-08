package com.cambrian.common.util;

import java.io.File;

/**
 * 类说明：文件监视器条目
 * 
 * @version 1.0
 * @author zminleo <zmin@seasky.cn>
 */

/** 监听条目类 */
class FileMonitorItem
{

	/* static fields */

	/* fields */
	/** 所在的文件监视器 */
	FileMonitor monitor;
	/** 文件名称 */
	String name;
	/** 文件 */
	File file;
	/** 当前是否存在 */
	boolean exist;
	/** 文件最后修改时间 */
	long lastTime;
	/** 监听器列表 */
	ObjectArray listenerList;
	/** 目录内的文件 */
	File[] files;
	/** 目录内的文件最后修改时间 */
	long[] filesLastTime;

	/* constructors */
	/** 构造一个监听条目对象 */
	FileMonitorItem(FileMonitor monitor,String name)
	{
		this.monitor=monitor;
		this.name=name;
		file=new File(name);
		listenerList=new ObjectArray();
		exist=file.exists();
		if(!exist) return;
		lastTime=file.lastModified();
		openDirectory();
	}
	/* methods */
	/** 打开目录 */
	void openDirectory()
	{
		if(!file.isDirectory()) return;
		files=file.listFiles();
		sort(files);
		filesLastTime=new long[files.length];
		for(int i=0;i<files.length;i++)
			filesLastTime[i]=files[i].lastModified();
	}
	/** 检查文件是否被修改 */
	void checkModified()
	{
		boolean b=file.exists();
		if(exist)
		{
			if(b)
			{
				long time=file.lastModified();
				if(time!=lastTime)
				{
					lastTime=time;
					monitor.fire(name,FileMonitor.MODIFY,
						listenerList.getArray());
				}
				if(files!=null) compareFiles();
			}
			else
			{
				exist=b;
				monitor
					.fire(name,FileMonitor.DELETE,listenerList.getArray());
				if(files!=null)
				{
					Object[] array=listenerList.getArray();
					for(int i=files.length-1;i>=0;i--)
					{
						monitor.fire(name,files[i].getName(),
							FileMonitor.DELETE,array);
					}
					files=null;
					filesLastTime=null;
				}
			}
		}
		else if(b)
		{
			exist=b;
			monitor.fire(name,FileMonitor.ADD,listenerList.getArray());
			openDirectory();
			if(files!=null)
			{
				Object[] array=listenerList.getArray();
				for(int i=files.length-1;i>=0;i--)
				{
					monitor.fire(name,files[i].getName(),FileMonitor.ADD,
						array);
				}
			}
		}
	}
	/** 比较文件目录中的文件是否被修改、增加和删除 */
	void compareFiles()
	{
		File[] array1=files;
		long[] times1=filesLastTime;
		File[] array2=file.listFiles();
		sort(array2);
		long[] times2=new long[array2.length];
		for(int i=0;i<array2.length;i++)
			times2[i]=array2[i].lastModified();
		// 进行两个排序数组的比较
		int i=0,j=0,iLen=array1.length,jLen=array2.length;
		while(i<iLen&&j<jLen)
		{
			int c=compare((File)array1[i],(File)array2[j]);
			if(c>0)
			{
				monitor.fire(name,array2[i].getName(),FileMonitor.ADD,
					listenerList.getArray());
				j++;
			}
			else if(c<0)
			{
				monitor.fire(name,array1[i].getName(),FileMonitor.DELETE,
					listenerList.getArray());
				i++;
			}
			else
			{
				if(times1[i]!=times2[j])
					monitor.fire(name,array1[i].getName(),
						FileMonitor.MODIFY,listenerList.getArray());
				i++;
				j++;
			}
		}
		files=array2;
		filesLastTime=times2;
	}

	static int compare(File file1,File file2)
	{
		return file1.getAbsolutePath().compareTo(file2.getAbsolutePath());
	}

	public static void sort(Object[] array)
	{
		sort(array,0,array.length,false);
	}

	public static void sort(Object[] array,int offset,int len,
		boolean descending)
	{
		if(array.length<20)
			shellSort(array,offset,len,descending);
		else
			quickSort(array,offset,len,descending);
	}

	public static void shellSort(Object[] array,int offset,int len,
		boolean descending)
	{
		if(len<=0) return;
		if(offset<0) offset=0;
		if(offset+len>array.length) len=array.length-offset;
		int comp=(descending)?-1:1;
		int inc=1;
		for(int n=len/9;inc<=n;inc=3*inc+1)
			;
		for(;inc>0;inc/=3)
		{
			int i=inc;
			for(int n=offset+len;i<n;i+=inc)
			{
				Object o=array[i];
				int j=i;
				while((j>=inc)
					&&(compare((File)array[(j-inc)],(File)o)==comp))
				{
					array[j]=array[(j-inc)];
					j-=inc;
				}
				array[j]=o;
			}
		}
	}

	public static void quickSort(Object[] array,int offset,int len,
		boolean descending)
	{
		if(len<=0) return;
		if(offset<0) offset=0;
		if(offset+len>array.length) len=array.length-offset;
		int comp=(descending)?1:-1;
		int size=32;
		int[] lefts=new int[size];
		int[] rights=new int[size];
		int top=0;
		int l=offset;
		int r=offset+len-1;
		while(true)
		{
			while(r>l)
			{
				int scanr,pivot,scanl;
				if(r-l>7)
				{
					int mid=(l+r)/2;
					if(compare((File)array[mid],(File)array[l])==comp)
					{
						Object temp=array[mid];
						array[mid]=array[l];
						array[l]=temp;
					}
					if(compare((File)array[r],(File)array[l])==comp)
					{
						Object temp=array[r];
						array[r]=array[l];
						array[l]=temp;
					}
					if(compare((File)array[r],(File)array[mid])==comp)
					{
						Object temp=array[mid];
						array[mid]=array[r];
						array[r]=temp;
					}
					pivot=r-1;
					Object temp=array[mid];
					array[mid]=array[pivot];
					array[pivot]=temp;
					scanl=l+1;
					scanr=r-2;
				}
				else
				{
					pivot=r;
					scanl=l;
					scanr=r-1;
				}
				while(true)
				{
					do
					{
						++scanl;
						if(scanl>=r) break;
					}
					while(compare((File)array[scanl],(File)array[pivot])==comp);
					while((scanr>l)
						&&(compare((File)array[pivot],(File)array[scanr])==comp))
					{
						--scanr;
					}
					if(scanl>=scanr) break;
					Object temp=array[scanl];
					array[scanl]=array[scanr];
					array[scanr]=temp;
					if(scanl<r) ++scanl;
					if(scanr<=l) continue;
					--scanr;
				}
				Object temp=array[scanl];
				array[scanl]=array[pivot];
				array[pivot]=temp;
				int lsize=scanl-l;
				int rsize=r-scanl;
				if(lsize>rsize)
				{
					if(lsize!=1)
					{
						++top;
						if(top==size)
							throw new IllegalArgumentException(
								FileMonitorItem.class
									+" quickSort, stack overflow");
						lefts[top]=l;
						rights[top]=(scanl-1);
					}
					if(rsize==0) break;
					l=scanl+1;
				}
				else
				{
					if(rsize!=1)
					{
						++top;
						if(top==size)
							throw new IllegalArgumentException(
								FileMonitorItem.class
									+" quickSort, stack overflow");
						lefts[top]=(scanl+1);
						rights[top]=r;
					}
					if(lsize==0) break;
					r=scanl-1;
				}
			}
			if(top==0) return;
			l=lefts[top];
			r=rights[(top--)];
		}
	}
}