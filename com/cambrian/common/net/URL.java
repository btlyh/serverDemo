package com.cambrian.common.net;

import java.net.InetAddress;

/**
 * ��˵����URL����
 * 
 * @version 2013-4-28
 * @author HYZ (huangyz1988@qq.com)
 */
public final class URL
{

	/** host�ָ� */
	public static final String HOST_SEPARATOR="://";
	/** �˿ڷָ� */
	public static final char PORT_SEPARATOR=':';
	/** �ļ��ָ� */
	public static final char FILE_SEPARATOR='/';
	/** �ļ��ָ��ַ��� */
	public static final String FILE_SEPARATOR_STRING="/";

	/** ����intֵ��ȡIP��int4���ֽڣ�ip4���Σ�ÿ���ֽڶ�Ӧһ���� */
	public static String stringIP(int addr)
	{
		CharBuffer cb=new CharBuffer();
		// �����ֽ�24=3*8������24λȥ���������ֽ�
		cb.append((addr>>>24&0xFF)).append('.').append(addr>>>16&0xFF)
			.append('.').append((addr>>>8&0xFF)).append('.')
			.append((addr&0xFF));
		return cb.getString();
	}

	/** ����IP���int��ʶֵ */
	public static int intIP(String ip)
	{
		int offset=0;
		int i=ip.indexOf('.');
		if(i<0) return 0;
		try
		{
			int t0=Integer.parseInt(ip.substring(offset,i));
			offset=i+1;
			i=ip.indexOf('.',offset);
			if(i<0) return 0;
			int t1=Integer.parseInt(ip.substring(offset,i));
			offset=i+1;
			i=ip.indexOf('.',offset);
			if(i<0) return 0;
			int t2=Integer.parseInt(ip.substring(offset,i));
			int t3=Integer.parseInt(ip.substring(i+1));
			return (t0<<24)+(t1<<16)+(t2<<8)+t3;
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	/** ����ļ� */
	public static String synthesizeFile(String path,String file)
	{
		if((path==null)||(path.length()==0)) path=FILE_SEPARATOR_STRING;
		if((file==null)||(file.length()==0)) return path;
		if(FILE_SEPARATOR==file.charAt(0)) return file;
		int i=0;
		if('?'==file.charAt(0))
		{
			i=path.indexOf('?');
			if(i>0) return path.substring(0,i)+file;
			return path+file;
		}
		if('#'==file.charAt(0))
		{
			i=path.indexOf('#');
			if(i>0) return path.substring(0,i)+file;
			return path+file;
		}
		i=path.lastIndexOf(FILE_SEPARATOR);
		if(i>0)
		{
			if(FILE_SEPARATOR!=path.charAt(0))
				path=FILE_SEPARATOR+path.substring(0,i+1);
			else
				path=path.substring(0,i+1);
		}
		else
			path=FILE_SEPARATOR_STRING;
		if(file.startsWith("./")) return path+file.substring(2);
		int j=0;
		int n=0;
		for(;file.indexOf("../",j)==j;++n)
			j+=3;
		if(n<=0) return path+file;
		for(i=path.length()-1;n>0;--n)
		{
			i=path.lastIndexOf(FILE_SEPARATOR,i-1);
			if(i<=0) return file.substring(j-1);
		}
		return path.substring(0,i+1)+file.substring(j);
	}

	/** ��ȡ�ļ�·�� */
	public static String getFilePath(String file)
	{
		int index=file.indexOf('?');
		if(index>=0) return file.substring(0,index);
		index=file.indexOf("#");
		if(index>=0) return file.substring(0,index);
		return file;
	}

	/** ����ļ����� */
	public static String getFileQuery(String file)
	{
		int index1=file.indexOf('?');
		if(index1<0) return null;
		int index2=file.indexOf('#');
		if(index2<index1) return file.substring(index1+1);
		return file.substring(index1+1,index2);
	}

	/** ��ȡ�ļ�Ƭ�� */
	public static String getFileFragment(String file)
	{
		int index=file.indexOf('#');
		return ((index>=0)?file.substring(index+1):null);
	}

	/** Э�� */
	private String protocol;
	/** ������ */
	private String host;
	/** �˿� */
	private int port;
	/** �ļ� */
	private String file;
	/** ��ַ */
	private String address;
	/** ������ַ */
	private transient InetAddress hostAddress;
	/** URL */
	private transient java.net.URL url;

	public URL(String protocol,String host,String file)
	{
		this(protocol,host,0,file);
	}

	public URL(String protocol,String host,int port,String file)
	{
		if(protocol==null)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, null protocol");
		this.protocol=protocol.toLowerCase();
		if(host==null)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, null host");
		this.host=host.toLowerCase();
		this.port=((port>=0)?port:0);
		if((file!=null)&&(file.length()>0))
		{
			if(FILE_SEPARATOR!=file.charAt(0)) file=FILE_SEPARATOR+file;
			this.file=file;
		}
		else
		{
			this.file="/";
		}
	}

	public URL(String url)
	{
		url=url.trim();
		int i=url.indexOf(HOST_SEPARATOR);
		if(i<0)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, invalid protocol, "+url);
		protocol=url.substring(0,i).toLowerCase();
		int offset=i+3;
		if(offset>=url.length())
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, invalid host, "+url);
		i=url.indexOf(FILE_SEPARATOR,offset);
		int j=url.indexOf(PORT_SEPARATOR,offset);
		if(i<0) i=url.length();
		port=0;
		if((j>0)&&(j<i))
		{
			host=url.substring(offset,j).toLowerCase();
			offset=j+1;

			if(offset<url.length())
			{
				try
				{
					port=Integer.parseInt(url.substring(offset,i));
					if(port<0) port=0;
				}
				catch(NumberFormatException e)
				{
					throw new IllegalArgumentException(getClass().getName()
						+" <init>, invalid port, "+url);
				}
			}
		}
		else
		{
			host=url.substring(offset,i).toLowerCase();
		}
		file=FILE_SEPARATOR_STRING;
		if(i>=url.length()) return;
		file=url.substring(i);
	}

	public URL(URL url,String file)
	{
		protocol=url.protocol;
		host=url.host;
		port=url.port;
		this.file=synthesizeFile(url.file,file);
		hostAddress=url.hostAddress;
	}

	public URL(java.net.URL url)
	{
		this(url.getProtocol(),url.getHost(),url.getPort(),url.getFile());
		this.url=url;
	}

	/** ��ȡЭ������ */
	public String getProtocol()
	{
		return protocol;
	}

	/** ��ȡ������ַ */
	public String getHost()
	{
		return host;
	}

	/** ��ȡ�˿� */
	public int getPort()
	{
		return port;
	}

	/** ��ȡ�ļ� */
	public String getFile()
	{
		return file;
	}

	/** ��ȡ�ļ�·�� */
	public String getFilePath()
	{
		return getFilePath(file);
	}

	/** ��ȡ�ļ����� */
	public String getFileQuery()
	{
		return getFileQuery(file);
	}

	/** ��ȡ�ļ�Ƭ�� */
	public String getFileFragment()
	{
		return getFileFragment(file);
	}

	/** ��ȡinternet��ַ���� */
	public InetAddress getHostAddress()
	{
		if(hostAddress!=null) return hostAddress;
		try
		{
			hostAddress=InetAddress.getByName(host);
		}
		catch(Exception e)
		{
		}
		return hostAddress;
	}

	/** ��ȡURL���� */
	public java.net.URL getURL()
	{
		if(this.url!=null) return this.url;
		try
		{
			if(this.port>0)
			{
				this.url=new java.net.URL(this.protocol,this.host,this.port,
					this.file);
				return this.url;
			}
			this.url=new java.net.URL(this.protocol,this.host,this.file);
		}
		catch(Exception localException)
		{
		}
		return this.url;
	}

	/** ��ȡ��URL�ַ�����Ϣ */
	public String getString()
	{
		if(address==null)
		{
			if(port>0)
				address=protocol+HOST_SEPARATOR+host+PORT_SEPARATOR+port
					+file;
			else
				address=protocol+HOST_SEPARATOR+host+file;
		}
		return address;
	}

	/** ��ϣ�� */
	public int hashCode()
	{
		return (protocol.hashCode()+host.hashCode()+port+file.hashCode());
	}

	/** �ȶ�����URL���� */
	public boolean equals(Object obj)
	{
		if(this==obj) return true;
		if(!(obj instanceof URL)) return false;
		URL url=(URL)obj;
		if(!(protocol.equals(url.protocol))) return false;
		if(!(host.equals(url.host))) return false;
		if(port!=url.port) return false;
		return file.equals(url.file);
	}

	/** ��Ϣ */
	public String toString()
	{
		return super.toString()+"["+getString()+"]";
	}
}