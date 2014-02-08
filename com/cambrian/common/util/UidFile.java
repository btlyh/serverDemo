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
 * ��˵����
 * 
 * @author��Sebastian
 */
public class UidFile
{

	/* static fields */
	/** �ļ���С���� */
	public static final int SIZE=8;
	/** ��־��¼ */
	// private static final Logger log=Logger.getLogger(DataServer.class);
	/** ���� */
	private static UidFile propFile;
	/** ���� */
	private static UidFile cardFile;

	/* static methods */
	/** ��õ���Ψһ��ŵ��ļ� */
	public static UidFile getPropFile()
	{
		return propFile;
	}
	/** ���õ���Ψһ��ŵ��ļ� */
	public static void setPropFile(UidFile uidFile)
	{
		propFile=uidFile;
	}
	/** ��þ���Ψһ��ŵ��ļ� */
	public static UidFile getCardFile()
	{
		return cardFile;
	}
	/** ���þ���Ψһ��ŵ��ļ� */
	public static void setCardFile(UidFile uidFile)
	{
		cardFile=uidFile;
	}

	/* fields */
	// /** ����Ψһ��� */
	// private int plusUid;
	// /** ����Ψһ��ŵ���Сֵ */
	// private int plusUidMin;
	// /** ����Ψһ��ŵ����ֵ */
	// private int plusUidMax;
	// /** ����Ψһ��� */
	// private int minusUid;
	// /** ����Ψһ��ŵ���Сֵ */
	// private int minusUidMin;
	// /** ����Ψһ��ŵ����ֵ */
	// private int minusUidMax;
	// /** ����Ψһ���ͬ������ */
	// private Object plusLock=new Object();
	// /** ����Ψһ���ͬ������ */
	// private Object minusLock=new Object();
	/** �����ŵ��ļ� */
	String file;
	/** ������� */
	byte[] data=new byte[SIZE];

	/** ��������ļ� */
	RandomAccessFile randomAccessFile;

	/* properties */
	/** ��������Ψһ��ŵ����� */
	public void setPlusUid(int min,int max)
	{
		// plusUidMin=min;
		// plusUidMax=max;
		// if(plusUid<min) plusUid=min;
		// if(plusUid>max) plusUid=max;
	}
	/** ���ø���Ψһ��ŵ����� */
	public void setMinusUid(int min,int max)
	{
		// minusUidMin=min;
		// minusUidMax=max;
		// if(minusUid<min) minusUid=min;
		// if(minusUid>max) minusUid=max;
	}
	/** ��ñ����ŵ��ļ� */
	public String getFile()
	{
		return file;
	}
	/** �Ƿ��Ѿ��򿪱����ŵ��ļ� */
	public boolean isFileOpen()
	{
		return randomAccessFile!=null;
	}
	/* methods */
	/** �����ļ���ʼ����� */
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
	/** ����Ψһ��������� */
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
	/** ����Ψһ�ĸ������ */
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
	/** �����ŵ��ļ� */
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
