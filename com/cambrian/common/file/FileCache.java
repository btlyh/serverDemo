/**
 * 
 */
package com.cambrian.common.file;

import java.io.File;
import java.io.IOException;

import com.cambrian.common.log.Logger;
import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.timer.TimerCenter;
import com.cambrian.common.timer.TimerEvent;
import com.cambrian.common.timer.TimerListener;

/**
 * ��˵�����ļ�����
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public abstract class FileCache implements TimerListener
{

	/* static fields */
	/** ��־ */
	private static Logger log=Logger.getLogger(FileCache.class);

	/* static methods */

	/* fields */
	/** ����·�� */
	public String path;
	/** ��ʱ�� */
	private TimerEvent timer;

	/* constructors */

	public FileCache()
	{
		timer=new TimerEvent(this,"filecache",60*60*1000);
	}

	/* properties */

	/* init start */

	/* methods */
	/** ��ʱ������ */
	public void fileTimerStart()
	{
		TimerCenter.getMinuteTimer().add(timer);
	}
	/** ��ʱ��ֹͣ */
	public void fileTimerStop()
	{
		TimerCenter.getSecondTimer().remove(timer);
	}
	// @Override
	public void onTimer(TimerEvent timer)
	{
		if(!timer.equals(this.timer)) return;
		save(path+"_"+System.currentTimeMillis());
	}
	/** �������� */
	public void load()
	{
		try
		{
			byte[] bb=FileKit.readFile(new File(path));
			if(bb==null||bb.length<=0)
			{
				cacheBytesRead(null);
			}
			else
			{
				cacheBytesRead(new ByteBuffer(bb));
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	/** �������� */
	public void save()
	{
		save(path);
	}
	/** �������� */
	private void save(String path)
	{
		if(log.isDebugEnabled()) log.debug("save, path="+path);
		try
		{
			FileKit.writeFile(new File(path),
				cacheBytesWrite(new ByteBuffer()));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

	}
	/** ���л� */
	protected abstract byte[] cacheBytesWrite(ByteBuffer data);

	/** �����л� */
	protected abstract Object cacheBytesRead(ByteBuffer data);

	/* common methods */

	/* inner class */

}
