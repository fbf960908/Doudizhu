package com.Client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class test {

	public static void main(String[] args) throws UnknownHostException, IOException {
		 Card z = new Card("5-2", true);
		 Card zz = new Card("1-2", true);
		 List<Card> ss = new ArrayList();
		 List<Card> sss = new ArrayList();
		 ss.add(new Card("1-6", true));
		 ss.add(new Card("1-6", true));
		 ss.add(new Card("1-7", true));
	 
		 sss.add(new Card("1-3", true));
		 sss.add(new Card("1-3", true));
		 sss.add(new Card("1-3", true));
		 sss.add(new Card("1-4", true));
		 sss.add(new Card("1-4", true));
		 sss.add(new Card("1-4", true));
		 ss.add(new Card("1-7", true));
		 ss.add(new Card("1-8", true));
		 ss.add(new Card("1-8", true));
		 ss.add(new Card("1-9", true));
		 ss.add(new Card("1-9", true));
		 ss.add(new Card("1-10", true));
		 ss.add(new Card("1-10", true));
		 
		 System.out.println(ss.size());
		 
		 CardCtrl.cardListSort(ss);
		 System.out.println(CardCtrl.judgCard(ss));
		 for (Card card : ss) {
		 System.out.println(card.getName());
		 }
		// System.out.println(CardCtrl.cardCompare(z, zz));
		 System.out.println(CardCtrl.cardCompare(ss,sss));
		
		
	
	}
}
