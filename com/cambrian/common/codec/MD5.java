/**
 * 
 */
package com.cambrian.common.codec;

import java.security.MessageDigest;

/**
 * 类说明：
 * 
 * @version 1.1
 * @author maxw<woldits@qq.com>
 */
public class MD5
{

	/* static fields */

	/* static methods */
	/** 获取MD5值 */
	public static String getValue(String src)
	{
		try
		{
			byte[] bytes=src.getBytes();
			MessageDigest md5=MessageDigest.getInstance("MD5");
			md5.update(bytes);
			byte[] md5Byte=md5.digest();
			return HexBin.encode(md5Byte).toLowerCase();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	/* fields */

	/* constructors */

	/* properties */

	/* init start */

	/* methods */

	/* common methods */

	/* inner class */

}
