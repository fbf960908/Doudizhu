package com.Client;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayerCtrl {

	// 收到地主牌
	public static List<Card> getBOssCards(JSONObject json, List<Card> bossCards) throws JSONException {
		String s = json.getString("msg");
		String[] ss = s.split(" ");
		for (int i = 0; i < ss.length; i++) {
			Card a = new Card(ss[i], true);
			a.canClick = true;
			bossCards.add(a);
		}
		return bossCards;
	}

	// 收到确定地主玩家
	public static Player[] determineBoss(JSONObject json, Player[] playerList, int LocalNumber) throws JSONException {
		int n = json.getInt("mark");
		playerList[n].isBoss = true;
		if (n != LocalNumber)
			for (int i = 1; i <= 4; i++) {
				if (i != n)
					continue;
				Card a = new Card("1-1", false);
				a.canClick = false;
				playerList[i].cardList.add(a);
				playerList[i].cardList.add(a);
				playerList[i].cardList.add(a);
				playerList[i].cardList.add(a);
				playerList[i].cardList.add(a);
				playerList[i].cardList.add(a);
				playerList[i].cardList.add(a);
				playerList[i].cardList.add(a);
			}
		return playerList;
	}

	// 收到出牌
	public static List<Card> takeCards(JSONObject json) throws JSONException {
		String s = json.getString("msg");
		List<Card> List = new ArrayList<Card>();
		String[] ss = s.split(" ");
		for (int i = 0; i < ss.length; i++) {
			Card a = new Card(ss[i], true);
			a.canClick = false;
			List.add(a);
		}
		if(CardCtrl.judgCard(List)==CardType.c1112223344)
			Music.feiji();
		if(CardCtrl.judgCard(List)==CardType.c0000)
			Music.wangzha();
		return List;
	}

	// 收到发牌
	public static Player[] releaseCards(JSONObject json, Player[] playerList, int num) throws JSONException {
		String s = json.getString("msg");
		String[] ss = s.split(" ");
		for (int i = 0; i < ss.length; i++) {
			Card a = new Card(ss[i], true);
			a.canClick = true;
			playerList[num].cardList.add(a);
		}
		return playerList;
	}

	// 收到玩家序号及名称
	public static Player[] getLocalPlayer(JSONObject json, Player[] playerList) throws JSONException {
		String str = json.getString("msg");
		String[] s = str.split(" ");
		for (int i = 1; i <= 4; i++) {
			playerList[i].name = s[i - 1];
			playerList[i].playNumber = i;
		}
		return playerList;
	}
}
