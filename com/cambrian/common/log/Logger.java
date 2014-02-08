/**
 * 
 */
package com.cambrian.common.log;

/**
 * 类说明：日志记录
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public class Logger
{

	/* static fields */

	/* static methods */
	public static Logger getLogger(Class<?> clazz)
	{
		return new Logger(clazz.getName());
	}

	/* fields */
	org.apache.log4j.Logger logger;

	/* constructors */
	public Logger(String name)
	{
		this.logger=org.apache.log4j.Logger.getLogger(name);
	}
	/* properties */

	/* init start */

	/* methods */
	public boolean isTraceEnabled()
	{
		return this.logger.isDebugEnabled();
	}

	public void trace(Object paramObject)
	{
		this.logger.debug(paramObject);
	}

	public void trace(Object paramObject,Throwable paramThrowable)
	{
		this.logger.debug(paramObject,paramThrowable);
	}

	public boolean isDebugEnabled()
	{
		return this.logger.isDebugEnabled();
	}

	public void debug(Object paramObject)
	{
		this.logger.debug(paramObject);
	}

	public void debug(Object paramObject,Throwable paramThrowable)
	{
		this.logger.debug(paramObject,paramThrowable);
	}

	public boolean isInfoEnabled()
	{
		return this.logger.isInfoEnabled();
	}

	public void info(Object paramObject)
	{
		this.logger.info(paramObject);
	}

	public void info(Object paramObject,Throwable paramThrowable)
	{
		this.logger.info(paramObject,paramThrowable);
	}

	public boolean isWarnEnabled()
	{
		return true;
	}

	public void warn(Object paramObject)
	{
		this.logger.warn(paramObject);
	}

	public void warn(Object paramObject,Throwable paramThrowable)
	{
		this.logger.warn(paramObject,paramThrowable);
	}

	public boolean isErrorEnabled()
	{
		return true;
	}

	public void error(Object paramObject)
	{
		this.logger.error(paramObject);
	}

	public void error(Object paramObject,Throwable paramThrowable)
	{
		this.logger.error(paramObject,paramThrowable);
	}

	public boolean isFatalEnabled()
	{
		return true;
	}

	public void fatal(Object paramObject)
	{
		this.logger.fatal(paramObject);
	}

	public void fatal(Object paramObject,Throwable paramThrowable)
	{
		this.logger.fatal(paramObject,paramThrowable);
	}
	/* common methods */

	/* inner class */

}
