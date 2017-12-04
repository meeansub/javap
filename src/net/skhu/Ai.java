package net.skhu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Ai {
	private Ai ai;
	private List<Integer> cardList;
	private List<Integer> usercardList;
	private int coin;

	public Ai(){
		cardList=Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		usercardList = new ArrayList<>();
		coin = 20;
		Collections.shuffle(cardList);
	}
	public void aiRechargeDeck() {
		this.cardList=Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		Collections.shuffle(this.cardList);
	}
	public List<Integer> getCard(){
		return cardList;
	}
	public void setUserCard(int usercard){
		usercardList.add(usercard);
	}
	public int number(Game game){
		return usercardList.get(game.getRound()-1);
	}
	public int getAiCoin() {
		return coin;
	}
	public void setCoin(int coin) {
		this.coin=coin;
	}
	public int aiBetCoin(int coin)
	{
		return this.coin -=coin;
	}
	//��ī�带 ���� �ִ� ai�� ��ī�尡 ũ�ٸ� ���� ���������ϰ� ��ī�尡 �۴ٸ� ���� ���� ����(������ Ȯ��)
	public int aiBattingBattle(int userCard,int userBet)
	{
		int bet =0;
		if(userCard == 1) //��ī�尡 1�̶�� AI�� ����(ī�� ���� 1�̴ϱ� ���̰� ����)
		{
			bet = (int)(Math.random()*getAiCoin())+userBet;   
			return bet;
		}
		else if(userCard>=2 && userCard<=5) 
		{
			bet = (int)(Math.random()*(userBet+3))+userBet;
			return bet;
		}
		else if(userCard<=6 && userCard<=8)
		{
			bet = (int)(Math.random()*(userBet+1))+userBet;   
			return bet;
		}
		else
		{
			bet = (int)(Math.random()*(userBet+1))+userBet;   
			return bet;
		}
	}
	//ai����Ȯ��
	public int aiDiePercentage(int userCard)
	{
		int die =0;
		if(userCard ==1) //��ī�尡 1�̸� ������ �ɰ��ϱ�
			return 0;
		else if(userCard>=2 && userCard<=5) 
		{
			die = (int)(Math.random()*1)+1;
			return die; //Ȯ�� ��������
		}
		else if(userCard<=6 && userCard<=8)
		{
			die = (int)(Math.random()*1)+1;   
			return die;
		}
		else
		{
			die = (int)(Math.random()*3)+1;      
			return die;
		}


	}
}