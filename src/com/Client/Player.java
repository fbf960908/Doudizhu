package com.Client;

import java.util.List;

public class Player {
	
	String name;//玩家姓名
	boolean isBoss;//是否是地主
	boolean isLocal;//是否是本地玩家
	int playNumber;//玩家序号
	int cardNumber;//玩家手牌数量
	List<Card> cardList;//玩家持有手牌
	
	public Player(String name, int playNumber) {
		super();
		this.name = name;
		this.playNumber = playNumber;
		this.isLocal = false;
		this.isBoss = false;
	}
	
	public void setCards(List<Card> list){
		this.cardNumber+=list.size();
		this.cardList.addAll(list);
	}

	public String getName() {
		return name;
	}

	public boolean isBoss() {
		return isBoss;
	}

	public int getPlayNumber() {
		return playNumber;
	}

	public int getCardNumber() {
		return cardNumber;
	}

	public List<Card> getCardList() {
		return cardList;
	}
	
	

}
