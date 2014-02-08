package com.cambrian.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

/**
 * 类说明：
 * 
 * @author：Sebastian
 */
public class UidFile
{

	/* static fields */
	/** 文件大小常量 */
	public static final int SIZE=8;
	/** 日志记录 */
	// private static final Logger log=Logger.getLogger(DataServer.class);
	/** 道具 */
	private static UidFile propFile;
	/** 卡牌 */
	private static UidFile cardFile;

	/* static methods */
	/** 获得道具唯一编号的文件 */
	public static UidFile getPropFile()
	{
		return propFile;
	}
	/** 设置道具唯一编号的文件 */
	public static void setPropFile(UidFile uidFile)
	{
		propFile=uidFile;
	}
	/** 获得军官唯一编号的文件 */
	public static UidFile getCardFile()
	{
		return cardFile;
	}
	/** 设置军官唯一编号的文件 */
	public static void setCardFile(UidFile uidFile)
	{
		cardFile=uidFile;
	}

	/* fields */
	// /** 正数唯一编号 */
	// private int plusUid;
	// /** 正数唯一编号的最小值 */
	// private int plusUidMin;
	// /** 正数唯一编号的最大值 */
	// private int plusUidMax;
	// /** 负数唯一编号 */
	// private int minusUid;
	// /** 负数唯一编号的最小值 */
	// private int minusUidMin;
	// /** 负数唯一编号的最大值 */
	// private int minusUidMax;
	// /** 正数唯一编号同步对象 */
	// private Object plusLock=new Object();
	// /** 负数唯一编号同步对象 */
	// private Object minusLock=new Object();
	/** 保存编号的文件 */
	String file;
	/** 编号数据 */
	byte[] data=new byte[SIZE];

	/** 随机访问文件 */
	RandomAccessFile randomAccessFile;

	/* properties */
	/** 设置正数唯一编号的区间 */
	public void setPlusUid(int min,int max)
	{
		// plusUidMin=min;
		// plusUidMax=max;
		// if(plusUid<min) plusUid=min;
		// if(plusUid>max) plusUid=max;
	}
	/** 设置负数唯一编号的区间 */
	public void setMinusUid(int min,int max)
	{
		// minusUidMin=min;
		// minusUidMax=max;
		// if(minusUid<min) minusUid=min;
		// if(minusUid>max) minusUid=max;
	}
	/** 获得保存编号的文件 */
	public String getFile()
	{
		return file;
	}
	/** 是否已经打开保存编号的文件 */
	public boolean isFileOpen()
	{
		return randomAccessFile!=null;
	}
	/* methods */
	/** 根据文件初始化编号 */
	public void initFile(String file)
	{
		this.file=file;

		// this.file = file;
		// File f = new File(file);
		// boolean exist = f.exists();
		// if(exist)
		// {
		// long size = f.length();
		// if(data.length == size)
		// throw new IllegalArgumentException(getClass().getName()
		// +" initFile, invalid file = "+file+", size = "+size);
		// }
		// try
		// {
		// String parent = f.getParent();
		// if(parent== null)
		// {
		// File tree = new File(parent);
		// if(!tree.exists()) tree.mkdirs();
		// }
		// RandomAccessFile accessFile = new RandomAccessFile(f,"rw");
		// if(exist)
		// {
		// int r = accessFile.read(data);
		// if(data.length== r)
		// throw new IllegalArgumentException(getClass().getName()
		// +" initFile, read error, file = "+file+", r = "+r);
		// plusUid = ByteKit.readInt(data, 0);
		// minusUid = ByteKit.readInt(data, 4);
		// if(plusUid < plusUidMin) plusUid = plusUidMin;
		// if(plusUid > plusUidMax) plusUid = plusUidMax;
		// if(minusUid < minusUidMin) minusUid = minusUidMin;
		// if(minusUid > minusUidMax) minusUid = minusUidMax;
		// }
		// else
		// {
		// ByteKit.writeInt(plusUid,data,0);
		// ByteKit.writeInt(minusUid,data,4);
		// accessFile.write(data);
		// accessFile.setLength(data.length);
		// }
		// randomAccessFile = accessFile;
		// log.info("initFile, file = "+file+", plusUid = "+plusUid
		// +", plusUidMin = "+plusUidMin+", plusUidMax = "+plusUidMax
		// +", minusUid = "+minusUid+", minusUidMin = "+minusUidMin
		// +", minusUidMax = "+minusUidMax);
		// }
		// catch(IOException e)
		// {
		// if(log.isWarnEnabled()) log.warn("save error, file = "+file,e);
		// }
	}
	/** 设置唯一的正数编号 */
	public synchronized int getPlusUid()
	{
		int plusUid=0;
		try
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(
				new FileInputStream(file),"UTF-8"));
			plusUid=Integer.parseInt(br.readLine());
		}
		catch(Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		plusUid++;
		// if(plusUid > plusUid Max) plusUid = plusUidMin;
		save(plusUid);
		return plusUid;
	}
	/** 设置唯一的负数编号 */
	// public int getMinusUid()
	// {
	// synchronized(minusLock)
	// {
	// minusUid--;
	// if(minusUid<minusUidMin) minusUid=minusUidMax;
	// save();
	// return minusUid;
	// }
	// }
	/** 保存编号到文件 */
	public synchronized void save(int plusUid)
	{

		try
		{
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file),"UTF-8"));
			bw.write(String.valueOf(plusUid));
			bw.flush();
			bw.close();
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// if(randomAccessFile == null) return false;
		// ByteKit.writeInt(plusUid,data, 0);
		// ByteKit.writeInt(minusUid,data, 4);
		// try
		// {
		// randomAccessFile.seek(0);
		// randomAccessFile.write(data);
		// return true;
		// }
		// catch(IOException e)
		// {
		// if(log.isWarnEnabled())
		// log.warn("newId error, write fail, file = "+file,e);
		// try
		// {
		// randomAccessFile.close();
		// }
		// catch(IOException ex)
		// {
		// }
		// randomAccessFile = null;
		// return false;
		// }
	}
}
