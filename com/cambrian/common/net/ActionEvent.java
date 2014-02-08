/**
 * 
 */
package com.cambrian.common.net;

public class ActionEvent
{

	public Object source;
	public int type;
	public Object action;
	public Object parameter;

	public ActionEvent(Object paramObject)
	{
		this.action=paramObject;
	}

	public ActionEvent(Object paramObject1,Object paramObject2)
	{
		this.source=paramObject1;
		this.action=paramObject2;
	}

	public ActionEvent(Object paramObject1,int paramInt,Object paramObject2)
	{
		this.source=paramObject1;
		this.type=paramInt;
		this.action=paramObject2;
	}

	public ActionEvent(Object paramObject1,int paramInt,Object paramObject2,
		Object paramObject3)
	{
		this.source=paramObject1;
		this.type=paramInt;
		this.action=paramObject2;
		this.parameter=paramObject3;
	}

	public String toString()
	{
		return super.toString()+"[source="+this.source+", type="+this.type
			+", action="+this.action+", parameter="+this.parameter+"] ";
	}
}