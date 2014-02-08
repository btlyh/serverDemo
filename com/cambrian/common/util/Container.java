/**
 * 
 */
package com.cambrian.common.util;

public abstract interface Container
{

	/** 当前容器元素数量 */
	public abstract int size();

	/** 容器是否是空的 */
	public abstract boolean isEmpty();

	/** 容器是否已满 */
	public abstract boolean isFull();

	/** 是否包含指定元素 */
	public abstract boolean contain(Object paramObject);

	/** 添加指定元素 */
	public abstract boolean add(Object paramObject);

	/** 获取一个元素 */
	public abstract Object get();

	/** 移除一个元素 */
	public abstract Object remove();

	/** 清空容器 */
	public abstract void clear();
}