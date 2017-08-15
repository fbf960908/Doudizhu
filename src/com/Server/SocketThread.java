package com.Server;

import java.io.DataInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class SocketThread implements Runnable {

	private DataInputStream dis;
	private ServerCtrl sc;
	private int bossflag ;

	public SocketThread(ServerCtrl sc, DataInputStream dis) {
		super();
		this.sc = sc;
		this.dis = dis;
		this.bossflag=0;
	}

	@Override
	public void run() {
		try {
			while (true) {
				byte [] b = new byte[10240];
				this.dis.read(b);
				String clientMsg = new String(b).trim();// 来自客户端的命令
				JSONObject json = new JSONObject(clientMsg);// 转化为json数据格式
				System.out.println("服务器收到:"+clientMsg);
				switch (json.getInt("type")) {
				case 1:
					receiveSetBoss(json);
					break;
				case 2:
					receivePutCards(json);
					break;
				case 3:
					receiveGameOver(json);
					break;
				default:
					System.out.println("json数据不合法");
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//收到叫地主（叫：yes 不叫：no）
	private void receiveSetBoss(JSONObject json) throws JSONException, IOException {
		int num = json.getInt("mark");
		String flag = json.getString("msg");
		if(flag.equals("yes")){
			sc.sendBoss(num);
			sc.sendBossCards(num);
		}else if(flag.equals("no")){
			bossflag++;
			if(bossflag==4){
				sc.sendGameOver(0);
			}else{
				sc.sendBossMsg((num+1)>4?(num-3):(num+1));
			}
		}
	}

	//收到出牌命令
	private void receivePutCards(JSONObject json) throws JSONException, IOException {
		int num = json.getInt("mark");
		String cards = json.getString("msg");
		sc.sendPutCards(num, cards);
	}
	
	//收到胜利命令
	private void receiveGameOver(JSONObject json) throws JSONException, IOException {
		int num = json.getInt("mark");
		sc.sendGameOver(num);
	}
}
