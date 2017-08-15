package com.Client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.json.JSONException;
import org.json.JSONObject;

public class UI extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Player[] playerList = new Player[5];// 玩家列表
	public static int LocalNumber;// 本地玩家序号

	public static int whoBoss;
	public static int flag;// 上一次操作的玩家序号
	public static int operatingNum;// 正在进行操作的玩家
	public static int lastTakeNum;// 上次出牌玩家序号

	public Container container = null;// 定义容器
	public JMenuItem exit, replay, about;// 定义菜单按钮
	public static JButton landlord[] = new JButton[2];// 抢地主按钮
	public static JButton publishCard[] = new JButton[2];// 出牌按钮

	JLabel wait;
	public static JLabel clock[] = new JLabel[5];
	JLabel[] playName = new JLabel[4];// 玩家姓名
	JLabel[] playPhoto = new JLabel[4];// 玩家头像
	JLabel[] cardsWest = new JLabel[33];// 西边牌
	JLabel[] cardsNorth = new JLabel[33];// 北边牌
	JLabel[] cardsEast = new JLabel[33];// 东边牌
	static List<Card> bossCards = new ArrayList<>();// 地主牌
	List<Card> putsList = new ArrayList<Card>();// 自己出牌
	List<Card> sendServerCardList = new ArrayList<Card>();;// 自己出牌
	static List<Card> lastPuts = new ArrayList<>();// 上家出牌
	JLabel[] dizhuJLabels = new JLabel[8];

	String serverIP;
	String playername;
	Socket socket;

	/*
	 * UI类构造方法:当newUI界面时,自动创建斗地主主界面
	 */
	public UI(String serverIP, String playername) throws UnknownHostException, IOException {
		this.serverIP = serverIP;
		this.playername = playername;
		this.socket = new Socket(serverIP, 8888);
		this.setTitle("秒天秒地斗地主-" + playername);
		this.setSize(1200, 700);
		setResizable(false);
		new Thread(new Music()).start();
		wait = new JLabel("等待其他玩家中", JLabel.CENTER);
		wait.setSize(300, 110);
		wait.setVisible(true);
		this.add(wait);
		setLocationRelativeTo(getOwner());

		for (int i = 1; i <= 4; i++) {
			playerList[i] = new Player("未连接", 0);
			playerList[i].cardList = new ArrayList<Card>();
		}

		this.setVisible(true);
	}

	/*
	 * 主界面布局:固定位置 主界面设置
	 */
	public void init() {

		wait.setVisible(false);
		setLocationRelativeTo(getOwner()); // 屏幕居中
		container = this.getContentPane();
		container.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container.setBackground(new Color(0, 112, 26)); // 背景为绿色

	}

	// 菜单栏设置
	public void setMenu() {
		JMenuBar jMenuBar = new JMenuBar();
		JMenu game = new JMenu("游戏");
		JMenu help = new JMenu("帮助");
		replay = new JMenuItem("重新开始");
		exit = new JMenuItem("退出");
		about = new JMenuItem("关于");
		game.add(replay);
		game.add(exit);
		help.add(about);
		jMenuBar.add(game);
		jMenuBar.add(help);
		this.setJMenuBar(jMenuBar);
	}

	// 设置东边玩家布局-包含玩家头像及姓名;扑克牌背面显示;倒计时文本框;小时钟图标
	private void setEast() {
		clock[getEastNum()] = new JLabel(new ImageIcon("images/clock.gif"));
		clock[getEastNum()].setBounds(920, 305, 30, 30);
		clock[getEastNum()].setVisible(false);
		this.add(clock[getEastNum()]);
		// 设置玩家姓名
		playName[0] = new JLabel(playerList[getEastNum()].getName()); // 未获取玩家姓名
		playName[0].setBounds(1100, 270, 80, 30);
		playName[0].setVisible(true);
		playName[0].setBackground(new Color(255, 255, 255));
		this.add(playName[0]);
		// 设置玩家头像
		playPhoto[0] = new JLabel(new ImageIcon("images/nongmin.png"));
		playPhoto[0].setBounds(1100, 300, 80, 70);
		playPhoto[0].setVisible(true);
		this.add(playPhoto[0]);
		upEast();
	}

	// 设置北边玩家布局-包含玩家头像及姓名;扑克牌背面显示;倒计时文本框;小时钟图标
	private void setNorth() {
		clock[getNorthNum()] = new JLabel(new ImageIcon("images/clock.gif"));
		clock[getNorthNum()].setBounds(530, 175, 30, 30);
		clock[getNorthNum()].setVisible(false);
		this.add(clock[getNorthNum()]);
		// 设置玩家姓名
		playName[2] = new JLabel(playerList[getNorthNum()].getName()); // 未获取玩家姓名
		playName[2].setBounds(820, 105, 80, 30);
		playName[2].setVisible(true);
		playName[2].setBackground(new Color(255, 255, 255));
		this.add(playName[2]);
		// 设置玩家头像
		playPhoto[2] = new JLabel(new ImageIcon("images/nongmin.png"));
		playPhoto[2].setBounds(820, 35, 80, 70);
		playPhoto[2].setVisible(true);
		this.add(playPhoto[2]);
		upNorth();

	}

	// 设置西边玩家布局-包含玩家头像及姓名;扑克牌背面显示;倒计时文本框;小时钟图标
	private void setWest() {
		clock[getWestNum()] = new JLabel(new ImageIcon("images/clock.gif"));
		clock[getWestNum()].setBounds(280, 305, 30, 30);
		clock[getWestNum()].setVisible(false);
		this.add(clock[getWestNum()]);
		// 设置玩家姓名
		playName[3] = new JLabel(playerList[getWestNum()].name); // 未获取玩家姓名
		playName[3].setBounds(20, 270, 80, 30);
		playName[3].setVisible(true);
		playName[3].setBackground(new Color(255, 255, 255));
		this.add(playName[3]);
		// 设置玩家头像
		playPhoto[3] = new JLabel(new ImageIcon("images/nongmin.png"));
		playPhoto[3].setBounds(20, 300, 80, 70);
		playPhoto[3].setVisible(true);
		this.add(playPhoto[3]);
		// 设置西边卡牌位置
		upWest();

	}

	// 设置中间面板,主要包含1,地主牌;2显示出牌;3,显示胜利
	private void setCenter() {
		for (int i = dizhuJLabels.length - 1; i >= 0; i--) {
			dizhuJLabels[i] = new JLabel(new ImageIcon("images/rear.gif"));
			dizhuJLabels[i].setBounds(400 + i * 25, 275, 71, 96);
			dizhuJLabels[i].setVisible(true);
			this.add(dizhuJLabels[i]);
		}
	}

	// 设置南面本地玩家布局
	private void setSouth() {
		// 设置玩家姓名
		playName[1] = new JLabel(playerList[LocalNumber].getName()); // 获取玩家姓名
		playName[1].setBounds(200, 610, 80, 30);
		playName[1].setVisible(true);
		playName[1].setBackground(new Color(255, 255, 255));
		this.add(playName[1]);
		// 设置玩家头像
		playPhoto[1] = new JLabel(new ImageIcon("images/nongmin.png"));
		playPhoto[1].setBounds(200, 540, 80, 70);
		playPhoto[1].setVisible(true);
		this.add(playPhoto[1]);
		// 设置南边卡牌位置

		for (int i = playerList[LocalNumber].cardList.size() - 1; i >= 0; i--) {
			Card a = playerList[LocalNumber].cardList.get(i);
			// a.canClick = true;
			this.add(a);
			a.setLocation(300 + i * 15, 540);
		}

	}

	// 设置南面本地玩家布局
	private void setLocal() {
		clock[LocalNumber] = new JLabel(new ImageIcon("images/clock.gif"));
		clock[LocalNumber].setBounds(530, 460, 30, 30);// 500, 460
		clock[LocalNumber].setVisible(false);
		this.add(clock[LocalNumber]);

		new Thread(new Runnable() {
			// 小时中线程
			@Override
			public void run() {
				while (true) {
					for (int i = 1; i <= 4; i++) {
						if (operatingNum == i)
							clock[i].setVisible(true);
						else
							clock[i].setVisible(false);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		// 按妞
		landlord[0] = new JButton("要地主");
		landlord[1] = new JButton("不     要");
		publishCard[0] = new JButton("出牌");
		publishCard[1] = new JButton("不要");

		for (int i = 0; i < 2; i++) {
			publishCard[i].setBounds(450 + i * 100, 500, 60, 20);
			landlord[i].setBounds(450 + i * 100, 500, 75, 20);
			container.add(landlord[i]);
			landlord[i].setVisible(false);
			publishCard[i].setVisible(false);
			container.add(publishCard[i]);
		}

		// 抢地主按钮监听器
		landlord[0].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					JSONObject json = new JSONObject();
					json.put("type", 1);
					json.put("mark", LocalNumber);
					json.put("msg", "yes");
					ToServer.sendMsg(json.toString(), new DataOutputStream(socket.getOutputStream()));
				} catch (JSONException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				for (int i = 0; i < 2; i++) {
					landlord[i].setVisible(false);
				}
				for (int i = dizhuJLabels.length - 1; i >= 0; i--) {
					dizhuJLabels[i].setVisible(false);
				}
				for (int i = 0; i < 2; i++) {
					publishCard[i].setVisible(true);
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				for (Card a : bossCards) {
					playerList[LocalNumber].cardList.add(a);
				}

				// 拿到地主牌后 将所有手牌设置为未点击状态
				for (int i = 0; i < playerList[LocalNumber].cardList.size(); i++) {
					playerList[LocalNumber].cardList.get(i).clicked = false;
				}
				CardCtrl.cardListSort(playerList[LocalNumber].cardList);

				for (int i = playerList[LocalNumber].cardList.size() - 1; i >= 0; i--) {
					Card a = playerList[LocalNumber].cardList.get(i);
					container.add(a);
					a.setLocation(300 + i * 15, 540);
				}

				// 更改地主头像
				playPhoto[1].setIcon(new ImageIcon("images/dizhu.png"));
			}
		});

		// 不要地主按钮监听器
		landlord[1].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					JSONObject json = new JSONObject();
					json.put("type", 1);
					json.put("mark", LocalNumber);
					json.put("msg", "no");
					ToServer.sendMsg(json.toString(), new DataOutputStream(socket.getOutputStream()));
				} catch (JSONException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				for (int i = 0; i < 2; i++) {
					landlord[i].setVisible(false);
				}
			}
		});

		// 出牌按妞
		publishCard[0].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 将点起来的牌放入list 每次点击先清空该lIst
				List<Card> temp = new ArrayList<Card>();
				putsList.clear();

				for (int i = 0; i < playerList[LocalNumber].cardList.size(); i++) {
					Card a = playerList[LocalNumber].cardList.get(i);
					if (a.clicked) {
						putsList.add(a);
					}
				}

				temp.addAll(putsList);

				int count = 0;
				for (Card a : lastPuts) {
					System.out.print(a.name + " ");
				}
				System.out.println();
				for (Card a : putsList) {
					System.out.print(a.name + " ");
				}
				System.out.println();

				if (CardCtrl.judgCard(putsList) != CardType.c0 && CardCtrl.cardCompare(putsList, lastPuts)) {
					for (Card a : lastPuts) {
						a.setVisible(false);
					}

					for (Card a : putsList) {
						CardCtrl.move(a, getLocation(), new Point(400 + count * 15, 275));
						a.canClick = false;// 牌能否点击
						count++;
					}

					// 关闭出牌按钮
					for (int j = 0; j < 2; j++) {
						publishCard[j].setVisible(false);
					}

					// 将出的牌整合为一个字符串
					String putCards = "";
					for (Card c : putsList) {
						putCards += c.getName() + " ";
					}

					try {
						JSONObject json = new JSONObject();
						json.put("type", 2);
						json.put("mark", LocalNumber);
						json.put("msg", putCards.trim());
						ToServer.sendMsg(json.toString(), new DataOutputStream(socket.getOutputStream()));
					} catch (JSONException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					playerList[LocalNumber].cardList.removeAll(temp);
					if (playerList[LocalNumber].cardList.size() == 0) {
						try {
							JSONObject json = new JSONObject();
							json.put("type", 3);
							json.put("mark", LocalNumber);
							ToServer.sendMsg(json.toString(), new DataOutputStream(socket.getOutputStream()));
						} catch (JSONException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				// 出牌后重新排序
				reCardList(playerList[LocalNumber].cardList);
				for (int i = 0; i < playerList[LocalNumber].cardList.size(); i++) {
					playerList[LocalNumber].cardList.get(i).clicked = false;
				}
				// 根据玩家列表重建南侧玩家
				// setSouth();
			}
		});

		// 不出按钮
		publishCard[1].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (lastPuts.size() != 0) {
					// 给服务器端发送不出指令
					try {
						JSONObject json = new JSONObject();
						json.put("type", 2);
						json.put("mark", LocalNumber);
						json.put("msg", "0");
						ToServer.sendMsg(json.toString(), new DataOutputStream(socket.getOutputStream()));
					} catch (JSONException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					for (int j = 0; j < 2; j++) {
						publishCard[j].setVisible(false);
					}
				}
			}
		});
	}

	// 出牌后重新排序
	protected void reCardList(List<Card> cardsLocal2) {
		for (int i = playerList[LocalNumber].cardList.size() - 1; i >= 0; i--) {
			Card a = playerList[LocalNumber].cardList.get(i);
			this.add(a);
			a.setBounds(300 + i * 15, 540, 71, 96);
		}
	}

	// 更新本地玩家序号
	public void getLocalPlayer(Player[] playerList) {
		for (int i = 1; i <= 4; i++) {
			if (playerList[i].getName().equals(playername)) {
				LocalNumber = i;
			}
		}
	}

	// 更新操作
	public void upLocal() {

	}

	public void upWest() {
		for (int i = 0; i < cardsWest.length; i++) {
			if (cardsWest[i] != null)
				cardsWest[i].setVisible(false);
		}
		for (int i = 0; i < playerList[getWestNum()].cardList.size(); i++) {
			cardsWest[i] = new JLabel(new ImageIcon("images/rear.gif"));
			cardsWest[i].setBounds(110, 150 + i * 10, 71, 96);
			cardsWest[i].setVisible(true);
			this.add(cardsWest[i]);
		}
	}

	public void upEast() {
		// 设置東边卡牌位置
		for (int i = 0; i < cardsEast.length; i++) {
			if (cardsEast[i] != null)
				cardsEast[i].setVisible(false);
		}

		for (int i = 0; i < playerList[getEastNum()].cardList.size(); i++) {
			cardsEast[i] = new JLabel(new ImageIcon("images/rear.gif"));
			cardsEast[i].setBounds(1020, 150 + i * 10, 71, 96);
			cardsEast[i].setVisible(true);
			this.add(cardsEast[i]);
		}
	}

	public void upNorth() {
		for (int i = 0; i < cardsNorth.length; i++) {
			if (cardsNorth[i] != null)
				cardsNorth[i].setVisible(false);
		}
		// 设置北边卡牌位置
		for (int i = 0; i < playerList[getNorthNum()].cardList.size(); i++) {
			cardsNorth[i] = new JLabel(new ImageIcon("images/rear.gif"));
			cardsNorth[i].setBounds(730 - i * 15, 35, 71, 96);
			cardsNorth[i].setVisible(true);
			this.add(cardsNorth[i]);
		}
	}

	public void upLastPuts() {
		for (int i = lastPuts.size() - 1; i >= 0; i--) {
			lastPuts.get(i).setBounds(400 + i * 15, 275, 71, 96);
			lastPuts.get(i).setVisible(true);
			this.add(lastPuts.get(i));
		}
	}

	public void upExceptLocal() {
		// 更新除去本地玩家以外所有玩家的手牌
		upWest();
		upEast();
		upNorth();
	}

	// 获得各个方位玩家的号码
	public int getEastNum() {
		return (LocalNumber + 1) > 4 ? (LocalNumber + 1 - 4) : (LocalNumber + 1);
	}

	public int getWestNum() {
		return (LocalNumber + 3) > 4 ? (LocalNumber + 3 - 4) : (LocalNumber + 3);
	}

	public int getNorthNum() {
		return (LocalNumber + 2) > 4 ? (LocalNumber + 2 - 4) : (LocalNumber + 2);
	}

	@Override
	public void run() {

		try {

			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

			dos.write(playername.getBytes());

			while (true) {
				repaint();
				byte[] b = new byte[3000];
				dis.read(b);
				String msg = new String(b).trim();
				System.out.println("服务端发来：" + msg);
				JSONObject json = new JSONObject(msg);
				int commandType = json.getInt("type");
				switch (commandType) {
				case 1:// 收到确定地主玩家 end
					Music.jiaodizhu();
					for (int i = dizhuJLabels.length - 1; i >= 0; i--) {
						dizhuJLabels[i].setVisible(false);
					}
					playerList = PlayerCtrl.determineBoss(json, playerList, LocalNumber);
					int n = json.getInt("mark");
					operatingNum = n;
					for (int i = 1; i <= 4; i++) {
						if (playerList[i].playNumber == n) {
							if (i == ((LocalNumber + 3) > 4 ? (LocalNumber - 1) : (LocalNumber + 3)))
								playPhoto[3].setIcon(new ImageIcon("images/dizhu.png"));
							if (i == ((LocalNumber + 2) > 4 ? (LocalNumber - 2) : (LocalNumber + 2)))
								playPhoto[2].setIcon(new ImageIcon("images/dizhu.png"));
							if (i == ((LocalNumber + 1) > 4 ? (LocalNumber - 3) : (LocalNumber + 1)))
								playPhoto[0].setIcon(new ImageIcon("images/dizhu.png"));
						}
					}
					break;
				case 2:// 收到出牌
					int num = json.getInt("mark");
					if (json.getString("msg").equals("0")) {
						Music.yaobuqi();
						if (lastTakeNum == LocalNumber) {
							for (Card a : lastPuts) {
								a.setVisible(false);
							}
							lastPuts.clear();
						}
					}
					for (Card a : lastPuts) {
						System.out.print(a.name + " ");
					}

					if (!json.getString("msg").equals("0")) {
						lastTakeNum = num;
						for (Card a : lastPuts) {
							a.setVisible(false);
						}

						lastPuts = PlayerCtrl.takeCards(json);
						for (Card a : lastPuts) {
							System.out.print(a.name + " ");
						}
						System.out.println("\n" + lastTakeNum);

						if (num != LocalNumber) {
							for (int i = 0; i < lastPuts.size(); i++) {
								playerList[num].cardList.remove(0);
							}
						}
						for (Card a : putsList) {
							a.setVisible(false);
						}
					}

					upLastPuts();
					upExceptLocal();

					num = num + 1;
					if (num > 4)
						num -= 4;
					if (num == LocalNumber) {
						for (int i = 0; i < 2; i++) {
							publishCard[i].setVisible(true);
						}
						operatingNum = num;
					}
					operatingNum = num;
					break;

				case 3:// 收到发牌 end
					playerList = PlayerCtrl.releaseCards(json, playerList, LocalNumber);
					for (int i = playerList[LocalNumber].cardList.size() - 1; i >= 0; i--) {
						Card a = playerList[LocalNumber].cardList.get(i);
						// a.canClick = true;
						this.add(a);
						a.setLocation(300 + i * 15, 540);
					}
					break;
				case 4:// 收到游戏结束
					Music.shengli();
					int VictoryNum = json.getInt("mark");
					String VictoryCamp = playerList[VictoryNum].isBoss ? "地主" : "农民";
					GameOver.show(playerList[VictoryNum].name + "出完了~\n" + VictoryCamp + "获得胜利~");
					this.setVisible(false);
					break;

				case 5:// 收到玩家序号及名称 end
					playerList = PlayerCtrl.getLocalPlayer(json, playerList);
					getLocalPlayer(playerList);// 更新本地玩家序号-LocalNumber

					for (int i = 1; i <= 4; i++) {
						if (i != LocalNumber) {
							for (int j = 1; j <= 25; j++) {
								Card a = new Card("1-1", false);
								a.canClick = false;
								playerList[i].cardList.add(a);
							}
						}
					}
					break;
				case 6:// 收到地主牌 end
					bossCards = PlayerCtrl.getBOssCards(json, bossCards);
					break;
				case 7:// 收到询问地主命令 end
					whoBoss = json.getInt("mark");
					operatingNum = whoBoss;
					init();// 初始化主界面，设置长宽高，背景颜色等
					setMenu();// 设置菜单栏
					setWest();// 设置东边玩家的头像，姓名以及卡牌位置（需要动态获取玩家姓名与头像）
					setNorth();// 设置北边玩家的头像，姓名以及卡牌位置（需要动态获取玩家姓名与头像）
					setEast();// 设置南边玩家的头像，姓名以及卡牌位置（需要动态获取玩家姓名与头像）
					setCenter();// 开局设置中间地主牌显示
					setSouth();// 设置南面（本地）玩家的姓名与头像，以及显示本地玩家的手牌
					setLocal();// 设置南面（本地）玩家的【抢地主，不要地主】，【出牌，不出牌】四个按钮的位置，默认为不可见。-设置四个按钮的显示时间
					if (whoBoss == LocalNumber) {
						for (int i = 0; i < 2; i++) {
							landlord[i].setVisible(true);
						}
					}
					break;
				default:
					System.out.println("json数据不合法");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

class GameOver {
	public static void show(String text) {
		JDialog dialog = new JDialog();
		JLabel label = new JLabel(text, JLabel.CENTER);
		dialog.add(label);
		dialog.setSize(300, 110);
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);
		dialog.setVisible(true);
		dialog.setResizable(true);
	}
}
