package com.cambrian.common.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.cambrian.common.codec.CRC32;
import com.cambrian.common.net.CharBuffer;
import com.cambrian.common.util.ArrayList;

/***
 * 类说明：文件操作工具集
 * 
 * @version 2013-4-24
 * @author HYZ (huangyz1988@qq.com)
 */
public final class FileKit
{

	public static final String toString=FileKit.class.getName();
	public static final int LEN_LIMIT=80;
	public static final int BUFFER_SIZE=2048;
	public static final byte[] EMPTY=new byte[0];

	public static String synthesizeFile(String paramString1,
		String paramString2)
	{
		if((paramString2==null)||(paramString2.length()==0))
			return paramString1;
		if('/'==paramString2.charAt(0)) return paramString2;
		if(paramString2.indexOf(58)>0) return paramString2;
		if((paramString1==null)||(paramString1.length()==0))
			return paramString2;
		CharBuffer localCharBuffer=new CharBuffer(paramString1.length()
			+paramString2.length());
		localCharBuffer.append(paramString1);
		int i=localCharBuffer.top()-1;
		if((paramString1.charAt(i)!='/')&&(paramString1.charAt(i)!='#'))
			localCharBuffer.append('/');
		else
		{
			--i;
		}
		if(paramString2.startsWith("./"))
		{
			return localCharBuffer.append(paramString2.substring(2))
				.getString();
		}
		int j=0;
		int k=0;
		for(;paramString2.indexOf("../",j)==j;++k)
			j+=3;

		if(k<=0) return localCharBuffer.append(paramString2).getString();

		--k;

		for(;i>=0;--i)
		{
			int l=localCharBuffer.read(i);
			if((l==47)||(l==35))
			{
				if(k<=0) break;
				--k;
			}
		}
		if(i<0) return paramString2.substring(j-1);
		localCharBuffer.setTop(i+1);
		return localCharBuffer.append(paramString2.substring(j)).getString();
	}

	public static byte[] readFile(File file) throws IOException
	{
		return readFile(file,0L,2147483647);
	}

	public static byte[] readFile(File file,long offset,int length)
		throws IOException
	{
		if(offset<0L)
			throw new IllegalArgumentException(toString+" readFile, file="
				+file+", invalid offset:"+offset);
		if(length<0)
			throw new IllegalArgumentException(toString+" readFile, file="
				+file+" invalid length:"+length);
		if(!file.exists()) return null;
		if(length==0) return EMPTY;
		RandomAccessFile randomAccessFile=null;
		try
		{
			randomAccessFile=new RandomAccessFile(file,"r");
			long len=randomAccessFile.length();
			if(offset>=len) return EMPTY;
			if(length>len-offset) length=(int)(len-offset);
			byte[] data=new byte[length];
			if(offset>0L) randomAccessFile.seek(offset);
			length=randomAccessFile.read(data);
			if(length<data.length)
			{
				byte[] arrayOfByte=new byte[length];
				System.arraycopy(data,0,arrayOfByte,0,length);
				data=arrayOfByte;
			}
			return data;
		}
		finally
		{
			try
			{
				if(randomAccessFile!=null) randomAccessFile.close();
			}
			catch(IOException localIOException3)
			{
			}
		}
	}

	public static void writeFile(File paramFile,byte[] paramArrayOfByte)
		throws IOException
	{
		writeFile(paramFile,paramArrayOfByte,0,paramArrayOfByte.length,false);
	}

	public static void writeFile(File paramFile,byte[] paramArrayOfByte,
		boolean paramBoolean) throws IOException
	{
		writeFile(paramFile,paramArrayOfByte,0,paramArrayOfByte.length,
			paramBoolean);
	}

	public static void writeFile(File paramFile,byte[] paramArrayOfByte,
		int paramInt1,int paramInt2,boolean paramBoolean) throws IOException
	{
		long l=0L;
		if((paramBoolean)&&(paramFile.exists())) l=paramFile.length();
		writeFile(paramFile,l,paramArrayOfByte,paramInt1,paramInt2,true);
	}

	public static void writeFile(File paramFile,long paramLong,
		byte[] paramArrayOfByte,int paramInt1,int paramInt2,
		boolean paramBoolean) throws IOException
	{
		if((paramInt1<0)||(paramInt1>paramArrayOfByte.length))
			throw new IllegalArgumentException(toString+" writeFile, file="
				+paramFile+", invalid offset:"+paramInt1);
		if((paramInt2<0)||(paramInt1+paramInt2>paramArrayOfByte.length))
			throw new IllegalArgumentException(toString+" writeFile, file="
				+paramFile+" invalid length:"+paramInt2);
		if(!(paramFile.exists()))
		{
			File localObject1=paramFile.getParentFile();
			if((localObject1!=null)
				&&(((!(((File)localObject1).exists()))||(!(((File)localObject1)
					.isDirectory()))))&&(!(((File)localObject1).mkdirs())))
				throw new IOException(toString
					+" writeFile, mkdirs fail, file="+paramFile);
		}
		else if(!(paramFile.isFile()))
		{
			throw new IOException(toString+" writeFile, not file, file="
				+paramFile);
		}
		Object localObject1=null;
		try
		{
			localObject1=new RandomAccessFile(paramFile,"rw");
			((RandomAccessFile)localObject1).seek(paramLong);
			((RandomAccessFile)localObject1).write(paramArrayOfByte,
				paramInt1,paramInt2);
			if(!(paramBoolean))
				((RandomAccessFile)localObject1).setLength(paramLong
					+paramInt2);
		}
		finally
		{
			try
			{
				if(localObject1!=null)
					((RandomAccessFile)localObject1).close();
			}
			catch(IOException localIOException1)
			{
			}
		}
	}

	public static String[] listFileName(File paramFile)
	{
		return listFileName(paramFile,true);
	}

	public static String[] listFileName(File paramFile,boolean paramBoolean)
	{
		if(!(paramFile.exists())) return null;
		if(!(paramFile.isDirectory())) return null;
		ArrayList localArrayList=new ArrayList();
		listFileName(paramFile,"",localArrayList,paramBoolean);
		String[] arrayOfString=new String[localArrayList.size()];
		localArrayList.toArray(arrayOfString);
		return arrayOfString;
	}

	private static void listFileName(File paramFile,String paramString,
		ArrayList paramArrayList,boolean paramBoolean)
	{
		File[] arrayOfFile=paramFile.listFiles();
		if(arrayOfFile==null) return;
		if(paramBoolean)
		{
			for(int j=0;j<arrayOfFile.length;++j)
			{
				if(arrayOfFile[j].isDirectory())
				{
					String str=paramString+arrayOfFile[j].getName()+'/';
					paramArrayList.add(str);
					listFileName(arrayOfFile[j],str,paramArrayList,
						paramBoolean);
				}
				else
				{
					paramArrayList.add(paramString+arrayOfFile[j].getName());
				}
			}
		}
		else
			for(int i=0;i<arrayOfFile.length;++i)
			{
				if(arrayOfFile[i].isDirectory())
					listFileName(arrayOfFile[i],
						paramString+arrayOfFile[i].getName()+'/',
						paramArrayList,paramBoolean);
				else
					paramArrayList.add(paramString+arrayOfFile[i].getName());
			}
	}

	public static File[] listFile(File paramFile,boolean paramBoolean)
	{
		if(!(paramFile.exists())) return null;
		if(!(paramFile.isDirectory())) return null;
		ArrayList localArrayList=new ArrayList();
		listFile(paramFile,localArrayList,paramBoolean);
		File[] arrayOfFile=new File[localArrayList.size()];
		localArrayList.toArray(arrayOfFile);
		return arrayOfFile;
	}

	private static void listFile(File paramFile,ArrayList paramArrayList,
		boolean paramBoolean)
	{
		File[] arrayOfFile=paramFile.listFiles();
		if(arrayOfFile==null) return;
		int i;
		if(paramBoolean)
		{
			for(i=0;i<arrayOfFile.length;++i)
			{
				if(arrayOfFile[i].isDirectory())
				{
					paramArrayList.add(arrayOfFile[i]);
					listFile(arrayOfFile[i],paramArrayList,paramBoolean);
				}
				else
				{
					paramArrayList.add(arrayOfFile[i]);
				}
			}
		}
		else
			for(i=0;i<arrayOfFile.length;++i)
			{
				if(arrayOfFile[i].isDirectory())
					listFile(arrayOfFile[i],paramArrayList,paramBoolean);
				else
					paramArrayList.add(arrayOfFile[i]);
			}
	}

	public static int getFileCrc(File paramFile) throws IOException
	{
		RandomAccessFile localRandomAccessFile=null;
		try
		{
			localRandomAccessFile=new RandomAccessFile(paramFile,"r");
			int i=getFileCrc(localRandomAccessFile);
			return i;
		}
		finally
		{
			try
			{
				if(localRandomAccessFile!=null)
					localRandomAccessFile.close();
			}
			catch(IOException localIOException2)
			{
			}
		}
	}

	public static int getFileCrc(RandomAccessFile paramRandomAccessFile)
		throws IOException
	{
		byte[] arrayOfByte=new byte[2048];
		int i=-1;
		paramRandomAccessFile.seek(0L);
		int j;
		while((j=paramRandomAccessFile.read(arrayOfByte))>0)
			i=CRC32.getValue(arrayOfByte,0,j,i);
		return (i^0xFFFFFFFF);
	}

	public static boolean checkFileCrc(File paramFile) throws IOException
	{
		RandomAccessFile localRandomAccessFile=null;
		try
		{
			localRandomAccessFile=new RandomAccessFile(paramFile,"r");
			boolean bool1=checkFileCrc(localRandomAccessFile);
			return bool1;
		}
		finally
		{
			try
			{
				if(localRandomAccessFile!=null)
					localRandomAccessFile.close();
			}
			catch(IOException localIOException2)
			{
			}
		}
	}

	public static boolean checkFileCrc(RandomAccessFile paramRandomAccessFile)
		throws IOException
	{
		return (-558161693==(getFileCrc(paramRandomAccessFile)^0xFFFFFFFF));
	}

	public static int addFileCrc(File paramFile) throws IOException
	{
		RandomAccessFile localRandomAccessFile=null;
		try
		{
			localRandomAccessFile=new RandomAccessFile(paramFile,"rw");
			int i=addFileCrc(paramFile);
			return i;
		}
		finally
		{
			try
			{
				if(localRandomAccessFile!=null)
					localRandomAccessFile.close();
			}
			catch(IOException localIOException2)
			{
			}
		}
	}

	public static int addFileCrc(RandomAccessFile paramRandomAccessFile)
		throws IOException
	{
		int i=getFileCrc(paramRandomAccessFile);
		paramRandomAccessFile.seek(paramRandomAccessFile.length());
		paramRandomAccessFile.writeByte(i&0xFF);
		paramRandomAccessFile.writeByte(i>>>8&0xFF);
		paramRandomAccessFile.writeByte(i>>>16&0xFF);
		paramRandomAccessFile.writeByte(i>>>24&0xFF);
		return i;
	}
}
