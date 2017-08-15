package com.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import com.Client.Card;
import com.Client.CardCtrl;

public class ServerCtrl {

	Socket[] socket = new Socket[5];
	DataOutputStream[] dos = new DataOutputStream[5];
	DataInputStream[] dis = new DataInputStream[5];
	List<Card> bossList;

	public ServerCtrl(Socket[] socket, DataOutputStream[] dos, DataInputStream[] dis) {
		super();
		this.socket = socket;
		this.dos = dos;
		this.dis = dis;
	}

	// 确定地主
	public void sendBoss(int num) throws JSONException, IOException {
		JSONObject json = new JSONObject();
		json.put("type", 1);
		json.put("mark", num);
		sendMsg(json);
	}

	// 询问叫地主
	public void sendBossMsg(int num) throws JSONException, IOException {
		JSONObject json = new JSONObject();
		json.put("type", 7);
		json.put("mark", num);
		sendMsg(json);
	}

	// 出牌
	public void sendPutCards(int num, String cards) throws JSONException, IOException {
		JSONObject json = new JSONObject();
		json.put("type", 2);
		json.put("mark", num);
		json.put("msg", cards);
		sendMsg(json);
	}

	// 发牌
	public void sendCards() throws JSONException, IOException {
		List<Card> list = new ArrayList<>(); // 定义108张牌
		List<Card> player1 = new ArrayList<>(); // 定义玩家1
		List<Card> player2 = new ArrayList<>(); // 定义玩家2
		List<Card> player3 = new ArrayList<>(); // 定义玩家3
		List<Card> player4 = new ArrayList<>(); // 定义玩家4
		this.bossList = new ArrayList<>(); // 定义地主牌
		for (int i = 1; i <= 4; i++) {
			for (int j = 1; j <= 13; j++) {
				String c = Integer.toString(i);
				c += "-";
				c += Integer.toString(j);
				list.add(new Card(c, false));
				list.add(new Card(c, false));
			}
		}
		list.add(new Card("5-1", false));
		list.add(new Card("5-1", false));
		list.add(new Card("5-2", false));
		list.add(new Card("5-2", false));

		Collections.shuffle(list);
		// 开始发牌
		for (int j = 0; j < 108;) {
			player1.add(list.get(j++));
			player2.add(list.get(j++));
			player3.add(list.get(j++));
			player4.add(list.get(j++));
			if (bossList.size() < 8)
				bossList.add(list.get(j++));
		}
		CardCtrl.cardListSort(player1);
		CardCtrl.cardListSort(player2);
		CardCtrl.cardListSort(player3);
		CardCtrl.cardListSort(player4);

		String cards1 = "";
		String cards2 = "";
		String cards3 = "";
		String cards4 = "";
		for (int i = 0; i < player1.size(); i++) {
			cards1 += player1.get(i).name + " ";
			cards2 += player2.get(i).name + " ";
			cards3 += player3.get(i).name + " ";
			cards4 += player4.get(i).name + " ";
		}

		JSONObject p1 = new JSONObject();
		JSONObject p2 = new JSONObject();
		JSONObject p3 = new JSONObject();
		JSONObject p4 = new JSONObject();

		p1.put("type", 3);
		p1.put("mark", 1);
		p1.put("msg", cards1.trim());

		p2.put("type", 3);
		p2.put("mark", 2);
		p2.put("msg", cards2.trim());

		p3.put("type", 3);
		p3.put("mark", 3);
		p3.put("msg", cards3.trim());

		p4.put("type", 3);
		p4.put("mark", 4);
		p4.put("msg", cards4.trim());

		dos[1].write(p1.toString().getBytes());
		dos[2].write(p2.toString().getBytes());
		dos[3].write(p3.toString().getBytes());
		dos[4].write(p4.toString().getBytes());

	}

	// 发出地主牌
	public void sendBossCards(int num) throws JSONException, IOException {
		String bossString = "";
		for (int i = 0; i < bossList.size(); i++) {
			bossString += bossList.get(i).getName() + " ";
		}

		JSONObject json = new JSONObject();
		json.put("type", 6);
		json.put("mark", num);
		json.put("msg", bossString);

		dos[num].write(json.toString().getBytes());
	}

	// 发送玩家信息列表
	public void setPlayer(String playername) throws JSONException, IOException {
		JSONObject json = new JSONObject();
		json.put("type", 5);
		json.put("msg", playername);
		System.out.println(json.toString());
		sendMsg(json);
	}

	// 游戏结束
	public void sendGameOver(int num) throws JSONException, IOException {
		if (num == 0) {
			sendCards();
		} else {
			JSONObject json = new JSONObject();
			json.put("type", 4);
			json.put("mark", num);
			sendMsg(json);
		}
	}

	// 发送json数据给每个客户端
	private void sendMsg(JSONObject json) throws IOException {
		for (int i = 1; i <= 4; i++) {
			dos[i].write(json.toString().getBytes());
		}
		System.out.println("服务器发送"+json.toString());
	}
}
