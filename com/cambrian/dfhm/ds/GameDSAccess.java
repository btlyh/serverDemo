package com.cambrian.dfhm.ds;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.cambrian.common.db.MongoDB;
import com.cambrian.common.db.MongoDBPersistence;
import com.cambrian.common.db.Persistence;
import com.cambrian.common.field.Field;
import com.cambrian.common.field.FieldKit;
import com.cambrian.common.log.Logger;
import com.cambrian.common.net.ByteBuffer;
import com.cambrian.common.net.DataAccessException;
import com.cambrian.common.util.ChangeAdapter;
import com.cambrian.common.util.TextKit;
import com.cambrian.dfhm.Lang;
import com.cambrian.dfhm.player.Player;
import com.cambrian.game.cc.SidEncoder;
import com.cambrian.game.ds.CheckNameFilter;
import com.cambrian.game.ds.DBAccess;

/**
 * 类说明：玩家信息取存
 * 
 * @version 2013-5-13
 * @author LazyBear
 */
public class GameDSAccess extends ChangeAdapter implements DBAccess
{

	private static final Logger log=Logger.getLogger(GameDSAccess.class);
	/** Sid编码器 */
	SidEncoder sidEncoder=new SidEncoder();

	MongoDB db;
	CheckNameFilter cnf;

	public void setMongoDB(MongoDB db)
	{
		this.db=db;
	}
	public void setCheckNameFilter(CheckNameFilter ckf)
	{
		this.cnf=ckf;
	}

	public boolean canAccess()
	{
		return true;
	}

	/** 检测用户是否有角色 */
	private boolean checkPlayerExist(int userId)
	{
		MongoDBPersistence persistence=new MongoDBPersistence(db,"player");
		return persistence.isExist(FieldKit.create("userid",userId));
	}

	/** 检查昵称的正确性 */
	private boolean checkNickName(String nickName) throws Exception
	{
		boolean result=true;
		if(!nickName.matches(Lang.REGEX_CHECK_NAME))
		{
			result=false;
		}
		BufferedReader br=new BufferedReader(new InputStreamReader(
			new FileInputStream("./txt/mingzi_use.txt"),"UTF-8"));
		String name=br.readLine();
		while(name!=null)
		{
			if(nickName.equals(name))
			{
				result=false;
				break;
			}
			name=br.readLine();
		}
		for(int i=0;i<cnf.filterStr.size();i++)
		{
			if(nickName.contains((String)cnf.filterStr.get(i)))
			{
				result=false;
				break;
			}
		}
		return result;
	}

	/** 保存名字 */
	private synchronized void saveName(String name) throws IOException
	{
		BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream("./txt/mingzi_use.txt",true),"UTF-8"));
		name+="\r\n";
		bw.write(name,0,name.length());
		bw.flush();
		bw.close();
		cnf.nameList.remove(name);
	}

	public ByteBuffer load(String uid,String sid,String nickName,
		String userName)
	{
		if(log.isInfoEnabled()) log.info("load, uid="+uid+", sid="+sid);
		if(uid==null)
			throw new DataAccessException(
				DataAccessException.SERVER_ACCESS_REFUSED,Lang.F611_DE);
		if(sid==null)
			throw new DataAccessException(
				DataAccessException.SERVER_ACCESS_REFUSED,Lang.F611_DE);
		return load(TextKit.parseLong(uid),nickName,userName);
	}

	public ByteBuffer load(long uid,String nickName,String userName)
	{
		Player p=new Player();
		boolean result=checkPlayerExist((int)uid);
		ByteBuffer data=new ByteBuffer();
		if(result)
		{
			p.setUserId(uid);
			loadPlayer(p,nickName);
		}
		else
		{
			try
			{
				if(!nickName.equals(""))
				{
					if(checkNickName(nickName))
					{
						p.setUserId(uid);
						loadPlayer(p,nickName);
					}
					else
					{
						p.setUserId(-2);
					}
				}
				else
				{
					p.setUserId(-1);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if(log.isDebugEnabled()) log.debug("load ok, player="+p);
		p.dbBytesWrite(data);
		return data;
	}

	/** 加载玩家信息 */
	public boolean loadPlayer(Player p,String nickName)
	{
		Field[] array=new Field[1];

		array[0]=FieldKit.create("userid",(int)p.getUserId());

		Field key=FieldKit.create("userid",(int)p.getUserId());

		MongoDBPersistence persistence=new MongoDBPersistence(db,"player");
		int i=persistence.get(key,array);
		if(i==Persistence.EXCEPTION)
			throw new DataAccessException(
				DataAccessException.SERVER_INTERNAL_ERROR,Lang.F611_DE);
		if(i==Persistence.RESULTLESS)
		{
			initNewPlayer(p,nickName);
//
//			coll_player.save(p);
//			coll_playerInfo.saveAll(p);

			System.out.println("init player   p="+p);
			return true;
		}
//		p.setNickname(((StringField)array[1]).value);
//		p.setMoney(((IntField)array[2]).value);
//		p.setGold(((IntField)array[3]).value);
//		p.setCurToken(((IntField)array[4]).value);
//		p.setMaxToken(((IntField)array[5]).value);
//		p.setSoul(((IntField)array[6]).value);
//		p.setLogoutTime(((LongField)array[7]).value);
//		p.setBuyTokenNum(((IntField)array[8]).value);
//		p.setVipLevel(((IntField)array[9]).value);
//		p.setCurIndexForNormalNPC(((IntField)array[10]).value);
//		p.setCurIndexForHardNPC(((IntField)array[11]).value);
//		p.setCurIndexForCorssNPC(((IntField)array[12]).value);
//		p.setBuyEmployNum(((IntField)array[13]).value);
//		p.setEmployNum(((IntField)array[14]).value);
//		p.setEmployIsOn(Boolean.parseBoolean(array[15].toString()));

		loadPlayerVar(p);
		return false;
	}

	public void initNewPlayer(Player p,String nickName)
	{
		long uid=p.getUserId();
		int serverId=(int)(uid>>32);
		int userid=(int)uid;
		if(log.isInfoEnabled())
			log.info("load ok, initNewPlayer, serverId="+serverId
				+", userid="+userid);//, nickName="+p.getNickname());
		// p.setNickname(nickName);// TODO 昵称默认设置
//		p.setNickname(nickName);
//		p.setMoney(100000);
//		p.setGold(100000);
//		p.setMaxToken(50);
//		p.setCurToken(50);
//		p.setSoul(100000);
//		p.setCurIndexForNormalNPC(GameCFG.getIndexForNormalNPC());
//		p.setVipLevel(5);
		p.init();
		try
		{
			saveName(nickName);
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadPlayerVar(Player p)
	{
		// // -----加载玩家邮件----
//		Field[] array=new Field[14];
//		array[0]=FieldKit.create("mailId",0L);
//		array[1]=FieldKit.create("state",0);
//		array[2]=FieldKit.create("sendTime",(String)null);
//		array[3]=FieldKit.create("title",(String)null);
//		array[4]=FieldKit.create("content",(String)null);
//		array[5]=FieldKit.create("cardList",(String)null);
//		array[6]=FieldKit.create("gold",0);
//		array[7]=FieldKit.create("money",0);
//		array[8]=FieldKit.create("token",0);
//		array[9]=FieldKit.create("soulPoint",0);
//		array[10]=FieldKit.create("normalPoint",0);
//		array[11]=FieldKit.create("sendPlayerName",(String)null);
//		array[12]=FieldKit.create("isFight",false);
//		array[13]=FieldKit.create("isSystem",false);
//
//		MongoDBPersistence persistence=new MongoDBPersistence(db,"t_mail");
//		Field[][] fieldss=persistence.gets(
//			FieldKit.create("userid",(int)p.getUserId()),array);
//		for(int i=0;i<fieldss.length;i++)
//		{
//			Mail mail=new Mail();
//			mail.setMailId(Integer.parseInt(fieldss[i][0].getValue()
//				.toString()));
//			mail.setState(Integer.parseInt(fieldss[i][1].getValue()
//				.toString()));
//			mail.setSendTime(fieldss[i][2].getValue().toString());
//			mail.setTitle(fieldss[i][3].getValue().toString());
//			mail.setContent(fieldss[i][4].getValue().toString());
//			String cardList=fieldss[i][5].getValue().toString();
//			if(!cardList.equals("")&&!cardList.equals("0"))
//			{
//				String[] cardLists=cardList.split("/");
//				for(int j=0;j<cardLists.length;j++)
//				{
//					mail.addCard(Integer.parseInt(cardLists[j]));
//				}
//			}
//			mail.setGold(Integer.parseInt(fieldss[i][6].getValue()
//				.toString()));
//			mail.setMoney(Integer.parseInt(fieldss[i][7].getValue()
//				.toString()));
//			mail.setToken(Integer.parseInt(fieldss[i][8].getValue()
//				.toString()));
//			mail.setSoulPoint(Integer.parseInt(fieldss[i][9].getValue()
//				.toString()));
//			mail.setNormalPoint(Integer.parseInt(fieldss[i][10].getValue()
//				.toString()));
//			mail.setSendPalyerName(fieldss[i][11].getValue().toString());
//			mail.setFight(Boolean.parseBoolean(fieldss[i][12].getValue()
//				.toString()));
//			mail.setSystem(Boolean.parseBoolean(fieldss[i][13].getValue()
//				.toString()));
//			p.addMail(mail);
//
//		// // -----加载玩家信息----
//		array=new Field[7];
//		array[0]=FieldKit.create("cardBag",(byte[])null);
//		array[1]=FieldKit.create("friendInfo",(byte[])null);
//		array[2]=FieldKit.create("armyCamp",(byte[])null);
//		array[3]=FieldKit.create("playerInfo",(byte[])null);
//		array[4]=FieldKit.create("formation",(byte[])null);
//		array[5]=FieldKit.create("tasks",(byte[])null);
//		array[6]=FieldKit.create("identity",(byte[])null);
//		persistence=new MongoDBPersistence(db,"player_info");
//		int i=persistence.get(FieldKit.create("userid",(int)p.getUserId()),
//			array);
//		if(i==Persistence.EXCEPTION)
//			throw new DataAccessException(
//				DataAccessException.SERVER_INTERNAL_ERROR,Lang.F611_DE);
//		byte[] bytes=((ByteArrayField)array[0]).value;
//		p.getCardBag().dbBytesRead(new ByteBuffer(bytes));
//		bytes=((ByteArrayField)array[1]).value;
//		p.getFriendInfo().dbBytesRead(new ByteBuffer(bytes));
//		bytes=((ByteArrayField)array[2]).value;
//		p.getArmyCamp().dbBytesRead(new ByteBuffer(bytes));
//		bytes=((ByteArrayField)array[3]).value;
//		p.getPlayerInfo().dbBytesRead(new ByteBuffer(bytes));
//		bytes=((ByteArrayField)array[4]).value;
//		p.formation.dbBytesRead(new ByteBuffer(bytes));
//		bytes=((ByteArrayField)array[5]).value;
//		p.getTasks().dbBytesRead(new ByteBuffer(bytes));
//		bytes=((ByteArrayField)array[6]).value;
//		p.getIdentity().dbBytesRead(new ByteBuffer(bytes));
	}
//	/** 玩家信息字段映射 */
//	private Fields playerInfoMapping(Player p)
//	{
//		Field[] array=new Field[1];
//		ByteBuffer data=new ByteBuffer();
//		p.getCardBag().dbBytesWrite(data);
//		array[0]=FieldKit.create("userid",(int)p.getUserId());
//		array[1]=FieldKit.create("cardBag",data.toArray());
//		data=new ByteBuffer();
//		p.getFriendInfo().dbBytesWrite(data);
//		array[2]=FieldKit.create("friendInfo",data.toArray());
//		data=new ByteBuffer();
//		p.getArmyCamp().dbBytesWrite(data);
//		array[3]=FieldKit.create("armyCamp",data.toArray());
//		data=new ByteBuffer();
//		p.getPlayerInfo().dbBytesWrite(data);
//		array[4]=FieldKit.create("playerInfo",data.toArray());
//		data=new ByteBuffer();
//		p.formation.dbBytesWrite(data);
//		array[5]=FieldKit.create("formation",data.toArray());
//		data=new ByteBuffer();
//		p.getTasks().dbBytesWrite(data);
//		array[6]=FieldKit.create("tasks",data.toArray());
//		data=new ByteBuffer();
//		p.getIdentity().dbBytesWrite(data);
//		array[7]=FieldKit.create("identity",data.toArray());
//		return new Fields(array);
//
//	}

//	/** 玩家字段映射 */
//	private Fields playerMapping(Player p)
//	{
//		Field[] array=new Field[1];
//		array[0]=FieldKit.create("userid",(int)p.getUserId());
//		array[1]=FieldKit.create("nickname",p.getNickname());
//		array[2]=FieldKit.create("money",p.getMoney());
//		array[3]=FieldKit.create("gold",p.getGold());
//		array[4]=FieldKit.create("curToken",p.getCurToken());
//		array[5]=FieldKit.create("maxToken",p.getMaxToken());
//		array[6]=FieldKit.create("soul",p.getSoul());
//		array[7]=FieldKit.create("logoutTime",p.getLogoutTime());
//		array[8]=FieldKit.create("buyTokenNum",p.getBuyTokenNum());
//		array[9]=FieldKit.create("vipLevel",p.getVipLevel());
//		array[10]=FieldKit.create("curIndexForNormalNPC",
//			p.getCurIndexForNormalNPC());
//		array[11]=FieldKit.create("curIndexForHardNPC",
//			p.getCurIndexForHardNPC());
//		array[12]=FieldKit.create("curIndexForCorssNPC",
//			p.getCurIndexForCorssNPC());
//		array[13]=FieldKit.create("employNum",p.getEmployNum());
//		return new Fields(array);
//	}

	public boolean save(ByteBuffer data)
	{
		// TODO 序列化Player
		if(data!=null)
		{
			Player p=new Player();
			p.dbBytesRead(data);
			return save(p);
		}
		else
		{
			return false;
		}

	}

	/** 玩家对象存储 */
	public boolean save(Player p)
	{
		Field key=FieldKit.create("userid",(int)p.getUserId());
//		Fields playerFields=playerMapping(p);
//		MongoDBPersistence persistence=new MongoDBPersistence(db,"player");
//		persistence.set(key,playerFields.toArray());
		saveVar(p,key);
		return true;
	}

	/** 玩家相关信息存储 */
	private void saveVar(Player p,Field key)
	{
//		MongoDBPersistence player_infoP=new MongoDBPersistence(db,
//			"player_info");
//		Fields playerInfo=playerInfoMapping(p);
//		player_infoP.set(key,playerInfo.toArray());
	}
}
