/**
 * 
 */
package com.cambrian.common.util;

public abstract interface Container
{

	/** ��ǰ����Ԫ������ */
	public abstract int size();

	/** �����Ƿ��ǿյ� */
	public abstract boolean isEmpty();

	/** �����Ƿ����� */
	public abstract boolean isFull();

	/** �Ƿ����ָ��Ԫ�� */
	public abstract boolean contain(Object paramObject);

	/** ���ָ��Ԫ�� */
	public abstract boolean add(Object paramObject);

	/** ��ȡһ��Ԫ�� */
	public abstract Object get();

	/** �Ƴ�һ��Ԫ�� */
	public abstract Object remove();

	/** ������� */
	public abstract void clear();
}