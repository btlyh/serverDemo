package com.cambrian.dfhm;

/**
 * ��˵��������
 * 
 * @version 1.0
 * @author maxw<woldits@qq.com>
 */
public final class GlobalConst
{

	/** ��������ı� */
	public static final int HANDLER_CHANGED=0;
	/** ����������ն˿ڸı� */
	public static final int PORT_CHANGED=1;

	/* ͨ������� */
	/** һ�㷵����Ϣ���ն˿� */
	public static final int ACCESS_RETURN_PORT=4;
	/** ��ȡ������ʱ�� */
	public static final int TIME_PORT=6;
	/**  */
	public static final int ATTRIBUTE_PORT=11;
	/**  */
	public static final int FILE_PORT=21;
	/**  */
	public static final int AUTHORIZED_FILE_PORT=22;
	/** ��֤-��ȡsid */
	public static final int CC_CERTIFY_PORT=101;
	/** ��֤-�����û����� */
	public static final int CC_LOAD_PORT=102;
	/** ��֤-�����û���Ծ */
	public static final int CC_ACTIVE_PORT=103;
	/** ��֤-�û��˳� */
	public static final int CC_EXIT_PORT=104;
	/** ��֤-����ر� */
	public static final int CC_SHUTDOWN_PORT=0x6a;
	/** ��֤-�û�ע�� */
	public static final int CC_REGIST_PORT=0x6b;

	/** ��������-��ҵ�½ */
	public static final int DC_LOGIN_PORT=111;
	/** ��������-����������� */
	public static final int DC_LOAD_PORT=112;
	/** ��������-����������� */
	public static final int DC_SAVE_PORT=113;
	/** ��������-����ǳ� */
	public static final int DC_CHECKNICKNAME_PORT=114;
	/** ��������-��ȡ������� */
	public static final int DC_RANDOMNAME_PORT=115;
	/** ��������-����������� */
	public static final int DC_UPDATE_PORT=121;
	/**  */
	public static final int CERTIFY_CODE_PORT=201;
	/**  */
	public static final int CERTIFY_PROXY_PORT=202;
	/** ���ݷ����-��ҵ�½ */
	public static final int LOGIN_PORT=211;
	/** ���ݷ����-����������� */
	public static final int LOAD_PORT=212;
	/** ���ݷ����-����˳� */
	public static final int EXIT_PORT=213;
	/** ���ݷ����-��½����ȡ������� */
	public static final int COMMAND_CLL_PORT=214;
	/** ���ݷ����-��ȡ������� */
	public static final int RANDOMNAME_PORT=215;
	/** ���ݷ����-ע��*/
	public static final int REG_PORT=0xD8;
	/**  */
	public static final int PROXY_ECHO_PORT=301;
	/**  */
	public static final int PROXY_PING_PORT=302;
	/**  */
	public static final int PROXY_TIME_PORT=306;
	/**  */
	public static final int PROXY_STATE_PORT=310;
	/**  */
	public static final int PROXY_LOGIN_PORT=402;
	/**  */
	public static final int PROXY_EXIT_PORT=404;
	/**  */
	public static final int CONNECT_REGISTER_PORT=601;
	/**  */
	public static final int ADVISE_OFFLINE_PORT=701;
	/**  */
	public static final int SERVER_LIST_PORT=801;

	public static final int SS_CHECK_PORT=901;

	/* ͨ������� */
	/** ����,��Ӧping��Ϣ */
	public static final int ECHO_PORT=1;
	/** ����ping��Ϣ */
	public static final int PING_PORT=2;


	/* �ⲿWEB�����޸Ķ˿� */
	public static final int UPDATE_BASE=10001;
	public static final int UPDATE_PRO=10002;
	public static final int UPDATE_CARD=10003;
}
