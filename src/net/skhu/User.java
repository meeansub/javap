

package net.skhu;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class User {
	private List<Integer> cardList;
	private int coin;
	static public int nowUser;
	
	public User(){
		cardList=Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		coin = 20;
		Collections.shuffle(cardList);
	}
	
	public void userRechargeDeck() {
		this.cardList=Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		Collections.shuffle(this.cardList);
	}
	
	public int getUserCard() {
		//if(Game.round<=10)
		nowUser=cardList.get(Game.round);
		//else if(Game.round>10)
			//nowUser=cardList.get(Game.round-10);
		return nowUser;
	}
	
	public List userCardDeck(){
		return cardList;
	}
	
	public int number(Game game){
		return cardList.get(game.getRound()-1);
	}
	
	public List card(){
		return cardList;
	}
	
	public int getUserCoin() {
		return coin;
	}
	
	public void betCoin(int bet) {
		this.coin -=bet;
	}
	
	public void setCoin(int coin) {
		this.coin=coin;
	}
}
