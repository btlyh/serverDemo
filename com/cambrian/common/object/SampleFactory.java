package com.cambrian.common.object;

import com.cambrian.common.log.Logger;
import com.cambrian.common.util.ArrayList;
import com.cambrian.common.xml.Context;

/**
 * 类说明：样本工厂
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class SampleFactory
{

	/** 日志 */
	private static final Logger log=Logger.getLogger(SampleFactory.class);
	/** 最大数量 */
	public static final int COUNT=0xFFFF;
	/** 空模板对象数组 */
	public static final Sample[] EMPTY=new Sample[0];
	/** 模板对象数组 */
	Sample[] samples=new Sample[COUNT];

	/** 获取指定sid模板对象 */
	public Sample getSample(int sid)
	{
		return samples[sid];
	}

	/** 设置模板对象 */
	public void setSample(Sample sample)
	{
		sample=sample.dosome();// 进一步处理，获得最终模板对象
		samples[sample.sid]=sample;
	}

	/** 构建一个新模板对象 */
	public Sample newSample(int sid)
	{
		Sample sample=getSample(sid);
		if(sample==null) return null;
		return (Sample)sample.clone();
	}

	/** 获得模板数组 */
	public Sample[] getSamples()
	{
		return samples;
	}

	/** 构建模板对象数组副本 */
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

	/** 从Context中获取模板数组初始化模板工厂 */
	public void init(Context context)
	{
		ArrayList list=(ArrayList)context.get("samples");
		Sample[] samples=new Sample[list.size()];
		list.toArray(samples);
		init(samples);
	}

	/** 从Context中获取模板数组初始化模板工厂 */
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
	/** 从Context中获取模板数组初始化模板工厂 */
	public void init(Sample sample)
	{
		if(sample==null&&log.isDebugEnabled())
			log.debug("sample not found");
		setSample(sample);
	}
}
