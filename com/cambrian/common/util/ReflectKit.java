package com.cambrian.common.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.cambrian.common.field.FieldValue;

/**
 * ��˵�������乤�߼�
 * 
 * @version 2013-4-15
 * @author HYZ (huangyz1988@qq.com)
 */
public class ReflectKit
{

	/** ����һ�����飬dimensions ����Ϊά�ȣ�dimensionsԪ��ֵ��Ӧָ��ά�ȵ����鳤�� */
	public static Object newArray(Class<?> clazz,int...dimensions)
	{
		if(clazz==null||dimensions==null||dimensions.length==0) return null;
		Class<?> componenType=clazz.getComponentType();
		if(componenType==null) componenType=clazz;
		return Array.newInstance(componenType,dimensions);
	}

	/**
	 * ���ָ��Class����Ļ����������ͣ������磺Class[int[]] ==> int.class��Class[Object[][]]
	 * ==> Object.class��
	 */
	public static Class<?> baseComponenType(Class<?> c)
	{
		Class<?> clazz=c;
		while(true)
		{
			c=c.getComponentType();
			if(c==null) break;
			clazz=c;
		}
		return clazz;
	}

	/** ��������ֵת�� */
	public static Object getPrimitive(Class<?> type,String value)
	{
		if(type==Character.TYPE) return value.charAt(0);
		if(type==Byte.TYPE) return (byte)TextKit.parseInt(value);
		if(type==Short.TYPE) return (short)TextKit.parseInt(value);
		if(type==Integer.TYPE) return TextKit.parseInt(value);
		if(type==Long.TYPE) return TextKit.parseLong(value);
		if(type==Float.TYPE) return TextKit.parseFloat(value);
		if(type==Double.TYPE) return TextKit.parseDouble(value);
		if(type==Boolean.TYPE) return TextKit.parseBoolean(value);
		return null;
	}

	/** ��ȡ�����Class����(������java�Զ���װ�������Ͷ�����ȡ��Class�����ǻ�������Class����) */
	public static Class<?> getClass(Object obj)
	{
		if(obj==null) return null;
		if(obj instanceof Character) return Character.TYPE;
		if(obj instanceof Byte) return Integer.TYPE;
		if(obj instanceof Short) return Short.TYPE;
		if(obj instanceof Integer) return Integer.TYPE;
		if(obj instanceof Long) return Long.TYPE;
		if(obj instanceof Float) return Float.TYPE;
		if(obj instanceof Double) return Double.TYPE;
		if(obj instanceof Boolean) return Boolean.TYPE;
		return obj.getClass();
	}

	/** ����ָ���������ֶ�ֵ */
	public static FieldValue setField(Class<?> clazz,Object obj,String name,
		Object value,boolean declared)
	{
		Field field=null;
		Field[] fields=null;
		if(declared)
		{
			Class<?> c=clazz;
			while(c!=null)
			{
				fields=c.getDeclaredFields();
				for(int i=fields.length-1;i>=0;--i)
				{
					if(!fields[i].getName().equals(name)) continue;
					field=fields[i];
					break;
				}
				if(field!=null) break;
				c=c.getSuperclass();
			}
		}
		else
		{
			fields=clazz.getFields();
			for(int i=fields.length-1;i>=0;--i)
			{
				if(!fields[i].getName().equals(name)) continue;
				field=fields[i];
				break;
			}
		}
		if(field==null)
			throw new RuntimeException(" setField, field not found, "+clazz
				+", field="+name+", declared="+declared);
		try
		{
			if(declared) field.setAccessible(true);
			boolean isPrimitive=field.getType().isPrimitive();
			if(isPrimitive&&(value==null))
				throw new RuntimeException(
					"setField, Can't set null to primitive, "+clazz
						+", field="+name+", declared="+declared);
			if(isPrimitive&&(value instanceof String))
				value=getPrimitive(field.getType(),(String)value);
			field.set(obj,value);
		}
		catch(Exception e)
		{
			throw new RuntimeException(" setField, set, "+clazz+", field="
				+name+", value="+value+", declared="+declared,e);
		}
		return new FieldValue(field.getType(),value);
	}
	/** ��ȡָ����������ֶ�ֵ */
	public static FieldValue getField(Class<?> c,Object obj,String name,
		boolean declared)
	{
		Field field=null;
		Field[] fields=null;
		Class<?> clazz=c;
		if(declared)
		{
			while(clazz!=null)
			{
				fields=clazz.getDeclaredFields();
				for(int i=0;i<fields.length;i++)
				{
					if(!fields[i].getName().equals(name)) continue;
					field=fields[i];
					break;
				}
				if(field!=null) break;
				clazz=clazz.getSuperclass();
			}
		}
		else
		{
			fields=clazz.getFields();
			for(int i=0;i<fields.length;i++)
			{
				if(!fields[i].getName().equals(name)) continue;
				field=fields[i];
				break;
			}
		}
		if(field==null)
			throw new RuntimeException("getField, field not found, "+c
				+", field="+name+", declared="+declared);
		try
		{
			if(declared) field.setAccessible(true);
			return new FieldValue(field.getType(),field.get(obj));
		}
		catch(Exception e)
		{
			throw new RuntimeException("getField, get, class="+c.getName()
				+", field="+name+" declared="+declared,e);
		}
	}

	/** ����ָ��������ķ��� */
	public static FieldValue invoke(Class<?> c,Object o,String name,
		FieldValue args[],boolean declared)
	{
		int n=args==null?0:args.length;
		Method m=null;
		if(declared)
			m=adaptDeclaredMethod(c,name,args);
		else
			m=adaptMethod(c,name,args);
		if(m==null)
			throw new RuntimeException(" invoke, method not found, "+c
				+", method="+name+", argNum="+n+", declared="+declared);
		Object objs[]=null;
		if(n>0)
		{
			objs=new Object[n];
			for(int i=0;i<n;i++)
				if(args[i]!=null) objs[i]=args[i].value;
		}
		try
		{
			if(declared) m.setAccessible(true);
			return new FieldValue(m.getReturnType(),m.invoke(o,objs));
		}
		catch(Exception e)
		{
			throw new RuntimeException(" invoke error, "+c+", object="+o
				+", method="+name+" argNum="+n+", declared="+declared,e);
		}
	}

	/** ѡ������ָ�������Ĺ��췽�� */
	public static Constructor<?> adaptConstructor(Class<?> c,
		FieldValue[] args,boolean declared)
	{
		int n=(args==null?0:args.length);
		Constructor<?> cons=null;
		for(Class<?> cc=c;cc!=null;cc=cc.getSuperclass())
		{
			Constructor<?> consts[]=null;
			if(declared)
				consts=c.getDeclaredConstructors();
			else
				consts=c.getConstructors();
			int j=0;
			for(int i=0;i<consts.length;i++)
			{
				Class<?> pars[]=consts[i].getParameterTypes();
				if(pars.length!=n) continue;
				if(n==0)
				{
					cons=consts[i];
					break;
				}
				for(j=0;j<n;j++)
				{
					boolean b=false;
					if(args[j]!=null)
					{
						if(pars[j].isPrimitive())
							b=args[j].type.isPrimitive()
								||args[j].type==String.class;
						else if(!args[j].type.isPrimitive())
							b=pars[j].isAssignableFrom(args[j].type);
						else
							b=pars[j]==Object.class;
					}
					else
						b=!pars[j].isPrimitive();// null�������������
					if(!b) break;
				}
				if(j<n) continue;
				for(j=0;j<n;j++)
				{
					if(args[j]==null||!pars[j].isPrimitive()) continue;
					if(args[j].type==String.class)
					{
						Object obj=getPrimitive(pars[j],
							(String)args[j].value);
						if(obj==null) break;
						args[j].type=pars[j];
						args[j].value=obj;
					}
					else if(args[j].type!=pars[j]) break;
				}
				if(j<n) continue;
				cons=consts[i];
				break;
			}
			if(!declared||cons!=null) break;
		}
		if(cons!=null&&declared) cons.setAccessible(true);
		return cons;
	}

	/** ѡ��ƥ��ָ�������Ĺ������� */
	public static Method adaptMethod(Class<?> c,String methodName,
		FieldValue[] args)
	{
		if((methodName==null)||(methodName.length()<=0)) return null;
		Method[] methods=c.getMethods();
		for(int i=0;i<methods.length;++i)
		{
			if(isAdapter(methods[i],methodName,args)) return methods[i];
		}
		return null;
	}

	/** ѡ��ƥ��ָ�����������������������Լ�����ĺ����� */
	public static Method adaptDeclaredMethod(Class<?> c,String methodName,
		FieldValue args[])
	{
		if(methodName==null||methodName.length()<=0) return null;
		for(Class<?> cc=c;cc!=null;cc=cc.getSuperclass())
		{
			Method methods[]=cc.getDeclaredMethods();
			for(int i=0;i<methods.length;i++)
			{
				if(isAdapter(methods[i],methodName,args)) return methods[i];
			}
		}
		return null;
	}

	/** ���ָ�������Ƿ�ƥ��ָ�����ƺͲ��� */
	public static boolean isAdapter(Method method,String name,
		FieldValue[] args)
	{
		if(!name.equals(method.getName())) return false;
		Class<?> clazz[]=method.getParameterTypes();
		int n=args==null?0:args.length;
		if(clazz.length!=n) return false;// ����������ƥ��
		if(n==0) return true;
		int i=0;
		for(i=0;i<n;i++)
		{
			boolean b=false;
			if(args[i]!=null)
			{
				if(args[i].type==null&&args[i].value==null)
				{
					if(!clazz[i].isPrimitive()) b=true;
				}
				else if(clazz[i].isPrimitive())
				{
					b=args[i].type.isPrimitive();
					if(!b) b=canAsPrimitive(clazz[i],args[i]);
				}
				else if(!args[i].type.isPrimitive())
					b=clazz[i].isAssignableFrom(args[i].type);
				else
					b=clazz[i]==Object.class;
			}
			else
				b=!clazz[i].isPrimitive();// null�������������
			if(!b) break;
		}
		if(i<n) return false;// �������Ͳ�ƥ��
		for(i=0;i<n;i++)
		{
			if(args[i]==null||!clazz[i].isPrimitive()) continue;
			if(args[i].type==String.class)
			{
				Object obj=getPrimitive(clazz[i],(String)args[i].value);
				if(obj==null) break;
				args[i].type=clazz[i];
				args[i].value=obj;
			}
			else if(!adapterType(clazz[i],args[i].type)) break;
		}
		if(i<n) return false;
		return true;
	}

	/** ָ���ֶ��Ƿ����ת��Ϊָ���������� */
	private static boolean canAsPrimitive(Class<?> type,FieldValue field)
	{
		if(field.type==null||field.type!=String.class) return false;
		if(field.value==null) return false;
		String value=(String)field.value;
		if(type==Character.TYPE&&value.length()!=1) return false;
		if(type==Boolean.TYPE
			&&!value.matches("^[0]$|^[1]$|(^true$)|(^false$)"))
			return false;
		try
		{
			getPrimitive(type,value);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	/** �ж�����c2�Ƿ����ת��Ϊc1���� */
	public static boolean adapterType(Class<?> c1,Class<?> c2)
	{
		Class<?>[] types={Character.TYPE,Byte.TYPE,Short.TYPE,Integer.TYPE,
			Long.TYPE,Float.TYPE,Double.TYPE};
		if(c1==Object.class) return true;
		if(!c1.isPrimitive()&&!c2.isPrimitive())
			return c1.isAssignableFrom(c2);
		if(c1.isPrimitive()&&c2.isPrimitive())
		{
			int m=0,n=0;
			for(int i=0;i<types.length;i++)
			{
				if(c1==types[i]) m=i;
				if(c2==types[i]) n=i;
			}
			if(m==0||n==0) return c1==c2;
			if(m>=n)
				return true;
			else
				return false;
		}
		return false;
	}

	/** �ж�ָ�������Ƿ��ǻ����������� */
	public static boolean isBaseArray(Object array)
	{
		if(array==null) return false;
		Class<?> c=array.getClass().getComponentType();
		return c==null?false:c.isPrimitive();
	}
}
