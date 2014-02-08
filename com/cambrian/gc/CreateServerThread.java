package com.cambrian.gc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.cambrian.game.ds.DSCCAccess;
import com.cambrian.game.ds.DataServer;
import com.cambrian.gc.notice.BaseUpdateNotice;
import com.cambrian.gc.notice.CardUpdateNotice;
import com.cambrian.gc.notice.ProUpdateNotice;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 类说明：服务线程
 * 
 * @author：LazyBear
 */
public class CreateServerThread extends Thread
{

	/* static fields */
	/** 1 基础属性修改 2道具修改 3增加卡牌 4关闭服务器 */
	public static final int TYPE_BASE=1,TYPE_PRO=2,TYPE_CARD=3,
					TYPE_SHUTDOWN=4;
	/* static methods */

	/* fields */
	private Socket client;
	private BufferedReader in;
	private DataServer dataServer;
	private BaseUpdateNotice bun;
	private ProUpdateNotice pun;
	private CardUpdateNotice cun;

	/* constructors */
	public CreateServerThread(Socket socket,DataServer dataServer,
		BaseUpdateNotice bun,ProUpdateNotice pun,CardUpdateNotice cun)
	{
		client=socket;
		this.dataServer=dataServer;
		this.bun=bun;
		this.pun=pun;
		this.cun=cun;
		try
		{
			in=new BufferedReader(new InputStreamReader(
				client.getInputStream()));
			start();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

	}
	/* properties */

	/* init start */

	/* methods */
	public void run()
	{
		try
		{
			String data=in.readLine();
			JsonParser jParser=new JsonParser();
			JsonElement jElement=jParser.parse(data);
			JsonObject jObject=jElement.getAsJsonObject();
			int type=jObject.get("type").getAsInt();
			switch(type)
			{
//				case TYPE_BASE:
//					doBase(jObject);
//					break;
//				case TYPE_PRO:
//					doPro(jObject);
//					break;
//				case TYPE_CARD:
//					doCard(jObject);
//					break;
				case TYPE_SHUTDOWN:
					doShutDown();
					break;
				default:
					break;
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				client.close();
				in.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 开始关闭GS
	 */
	private void doShutDown()
	{
		DSCCAccess cc=dataServer.getDSCCAccess();
		cc.shutDown();

		dataServer.saveSessionByShutDown();

	}

//	/**
//	 * 基础属性修改
//	 */
//	private void doBase(JsonObject jObject)
//	{
//		Session session=dataServer.getSession(jObject.get("name")
//			.getAsString());
//		if(session==null)
//		{
//			return;
//		}
//		Player player=(Player)session.getSource();
//		int money=jObject.get("money").getAsInt();
//		int gold=jObject.get("gold").getAsInt();
//		int token=jObject.get("token").getAsInt();
//		int soul=jObject.get("soul").getAsInt();
//
//		player.incrMoney(money);
//		player.incrGold(gold);
//		player.incrToken(token);
//		player.incrSoul(soul);
//
//		bun.send(session,new Object[]{player.getMoney(),player.getGold(),
//			player.getCurToken(),player.getSoul()});
//	}
//
//	/**
//	 * 道具修改
//	 */
//	private void doPro(JsonObject jObject)
//	{
//		Session session=dataServer.getSession(jObject.get("name")
//			.getAsString());
//		if(session==null)
//		{
//			return;
//		}
//		Player player=(Player)session.getSource();
//		int cardSid=jObject.get("cardSid").getAsInt();
//		Card card=player.getCardBag().add(cardSid,player.getAchievements());
//
//		pun.send(session,
//			new Object[]{card.getSid(),card.uid,card.getSkillId()});
//	}
//
//	/**
//	 * 卡牌熟悉修改
//	 */
//	private void doCard(JsonObject jObject)
//	{
//		Session session=dataServer.getSession(jObject.get("name")
//			.getAsString());
//		if(session==null)
//		{
//			return;
//		}
//		Player player=(Player)session.getSource();
//		int cardUid=jObject.get("cardUid").getAsInt();
//		Card card=player.getCardBag().getById(cardUid);
//		int att=jObject.get("att").getAsInt();
//		if(att>0)
//		{
//			card.setAtt(att);
//		}
//		int skillRate=jObject.get("skillRate").getAsInt();
//		if(skillRate>0)
//		{
//			card.setSkillRate(skillRate);
//		}
//		int maxHp=jObject.get("maxHp").getAsInt();
//		if(maxHp>0)
//		{
//			card.setMaxHp(maxHp);
//		}
//		int skillId=jObject.get("skillId").getAsInt();
//		if(skillId>0)
//		{
//			card.setSkillId(skillId);
//		}
//		int level=jObject.get("level").getAsInt();
//		if(level>0)
//		{
//			card.setLevel(level);
//		}
//		int critRate=jObject.get("critRate").getAsInt();
//		if(critRate>0)
//		{
//			card.setCritRate(critRate);
//		}
//		int dodgeRate=jObject.get("dodgeRate").getAsInt();
//		if(dodgeRate>0)
//		{
//			card.setDodgeRate(dodgeRate);
//		}
//		int skillLevel=jObject.get("skillLevel").getAsInt();
//		if(skillLevel>0)
//		{
//			card.setSkillLevel(skillLevel);
//		}
//		cun.send(session,new Object[]{cardUid,att,skillRate,maxHp,skillId,
//			level,critRate,dodgeRate,skillLevel});
//	}
}
