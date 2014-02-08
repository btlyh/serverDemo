package com.cambrian.common.object;

import com.cambrian.common.log.Logger;
import com.cambrian.common.util.ArrayList;
import com.cambrian.common.xml.Context;

/**
 * ��˵������������
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class SampleFactory
{

	/** ��־ */
	private static final Logger log=Logger.getLogger(SampleFactory.class);
	/** ������� */
	public static final int COUNT=0xFFFF;
	/** ��ģ��������� */
	public static final Sample[] EMPTY=new Sample[0];
	/** ģ��������� */
	Sample[] samples=new Sample[COUNT];

	/** ��ȡָ��sidģ����� */
	public Sample getSample(int sid)
	{
		return samples[sid];
	}

	/** ����ģ����� */
	public void setSample(Sample sample)
	{
		sample=sample.dosome();// ��һ�������������ģ�����
		samples[sample.sid]=sample;
	}

	/** ����һ����ģ����� */
	public Sample newSample(int sid)
	{
		Sample sample=getSample(sid);
		if(sample==null) return null;
		return (Sample)sample.clone();
	}

	/** ���ģ������ */
	public Sample[] getSamples()
	{
		return samples;
	}

	/** ����ģ��������鸱�� */
	public Sample[] newSamples(int start,int end)
	{
		if(start<0) start=0;
		if(start>=end) return EMPTY;
		if(end>=samples.length) end=samples.length;
		Sample[] array=new Sample[end-start];
		for(int i=0;start<=end;start+=(++i))
		{
			Sample s=this.samples[start];
			if(s!=null) array[i]=((Sample)s.clone());
		}
		return array;
	}

	/** ��Context�л�ȡģ�������ʼ��ģ�幤�� */
	public void init(Context context)
	{
		ArrayList list=(ArrayList)context.get("samples");
		Sample[] samples=new Sample[list.size()];
		list.toArray(samples);
		init(samples);
	}

	/** ��Context�л�ȡģ�������ʼ��ģ�幤�� */
	public void init(Sample[] samples)
	{
		if(samples==null&&log.isDebugEnabled())
			log.debug("samples not found");
		for(int i=0;i<samples.length;i++)
		{
			// if(log.isDebugEnabled()) log.debug(samples[i]);
			if(samples[i]!=null) setSample(samples[i]);
		}
	}
	/** ��Context�л�ȡģ�������ʼ��ģ�幤�� */
	public void init(Sample sample)
	{
		if(sample==null&&log.isDebugEnabled())
			log.debug("sample not found");
		setSample(sample);
	}
}
