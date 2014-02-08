package com.cambrian.dfhm;

public final class Lang
{

	/** 程序或策划bug,配置的物品数据错误 */
	public static final String ERROR_1="101";
	/** 程序或策划bug,前台传参数错误 */
	public static final String ERROR_2="102";

	/* 应用服务器端 */
	/** 600；无效通讯 */
	public static final String F600_CNCM="600";
	/** 611；网络错误 */
	public static final String F611_DE="611";
	/** 613；该账号没有激活 */
	public static final String F613_NA="613";
	/** 615；获得连接失败 */
	public static final String F615_GCF="615";
	/** 616；账号不存在 */
	public static final String F616_USER="616";
	/** 617；密码不正确 */
	public static final String F617_PW="617";
	/** 618；注册失败:账号被占用 */
	public static final String F618_REG_EXIST="618";
	/** 619；注册失败:账号不合法 */
	public static final String F619_REG_ILLEGALITY="619";

	/** 9000；网络错误,请重新登录 */
	public static final String F9000_SDE="9000";
	/** 9001；重连错误，此Session没有断线 */
	public static final String F9001_REC="9001";

	/* 正则 */
	/** 验证昵称 */
	public static final String REGEX_CHECK_NAME="^[\u4E00-\u9FA5A-Za-z0-9_]{2,10}";
	/** 验证账号 */
	public static final String REGEX_CHECK_USERNAME="^[A-Za-z0-9_]{6,16}";

}