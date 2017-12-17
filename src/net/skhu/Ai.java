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
   static List<Integer> aiRemoveCard = new ArrayList<>();

   public List<Integer> getAiRemoveCard(){
	   return aiRemoveCard;
   }
   public void setAiRemoveCard(int aiCard) {
	   aiRemoveCard.add(aiCard);
   }

   public Ai(){
      cardList=Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10);
      usercardList = new ArrayList<>();
      coin = 30;
      Collections.shuffle(cardList);
      System.out.println("ai 카드 덱: "+cardList);
   }
//   public void aiRechargeDeck() {
//      this.cardList=Arrays.asList(1,2,3,4,5,6,7,8,9,10);
//      Collections.shuffle(this.cardList);
//   }
   public List<Integer> getCard(){
      return cardList;
   }
   public void setUserCard(int usercard){
      usercardList.add(usercard);
   }
   public int getUserCard(Game game){
      return usercardList.get(game.getRound()-1);
   }
   public int number(Game game){
	   return cardList.get(game.getRound() - 1);
	   // 수정전
//	   if(Game.round<=10) {
//		   return cardList.get(game.getRound()-1);
//	   }
//	   else {
//		   return cardList.get(game.getRound()-11);
//	   }
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

   // 조금 더 세부적인 확률 계산을 위한 함수
   public int detailPer(int value) {
	   int percent = 0;
	   Collections.sort(aiRemoveCard);
	   for(int i = value - 1; i > 0; --i) {
		   if(aiRemoveCard.contains(i))
			   if(aiRemoveCard.indexOf(i) != aiRemoveCard.lastIndexOf(i)) percent += 10;
			   else percent += 5;
	   }
	   for(int i = value; i <= 10; ++i) {
		   if(aiRemoveCard.contains(i))
			   if(aiRemoveCard.indexOf(i) != aiRemoveCard.lastIndexOf(i)) percent -= 10;
			   else percent -= 5;
	   }
	   return percent;
   }

   //내카드를 보고 있는 ai가 내카드가 크다면 배팅 갯수적게하고 내카드가 작다면 배팅 갯수 높임(배팅할 확률)
   public int aiBattingBattle(int userCard,int userBet)
   {
      int bet =0;
      int rebet =0; //ai가 0을걸때 다시 걸게 할 확률

      // System.out.println("ai 버린 덱: "+aiRemoveCard);

      if(userCard == 1) bet = 5;
      else if(Game.round == 1 || Game.round == 2 || Game.round == 3 || Game.round == 4 || Game.round == 5) {
    	  if(userCard == 2 || userCard == 3 || userCard == 4) bet = 4;
    	  else bet = 2;
      }
      else if(Game.round == 6) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -5) bet = 4;
    	  else if(userCard == 3 && this.detailPer(userCard) >= 15) bet = 4;
    	  else if(userCard == 3 && this.detailPer(userCard) >= 5) bet = 3;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -25) bet = 2;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 25) bet = 3;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 5) bet = 2;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 25) bet = 2;
    	  else bet = 1;
      }
      else if(Game.round == 7) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -10) bet = 4;
    	  else if(userCard == 3 && this.detailPer(userCard) >= 0) bet = 4;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -10) bet = 3;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -20) bet = 2;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 20) bet = 4;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 10) bet = 3;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 0) bet = 2;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 30) bet = 2;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 30) bet = 2;
    	  else bet = 1;
      }
      else if(Game.round == 8) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -25) bet = 4;
    	  else if(userCard == 3 && this.detailPer(userCard) >= 5) bet = 4;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -5) bet = 3;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -15) bet = 2;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 15) bet = 4;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 5) bet = 3;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -5) bet = 2;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 25) bet = 3;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 15) bet = 2;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 35) bet = 2;
    	  else bet = 1;
      }
      else if(Game.round == 9) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -20) bet = 4;
    	  else if(userCard == 2 && this.detailPer(userCard) >= -30) bet = 2;
    	  else if(userCard == 3 && this.detailPer(userCard) >= 0) bet = 4;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -20) bet = 2;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 10) bet = 4;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 0) bet = 3;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -20) bet = 2;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 40) bet = 5;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 20) bet = 2;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 40) bet = 2;
    	  else bet = 1;
      }
      else if(Game.round == 10) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -25) bet = 4;
    	  else if(userCard == 2 && this.detailPer(userCard) >= -35) bet = 2;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -5) bet = 4;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -15) bet = 3;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -25) bet = 2;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 15) bet = 4;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -15) bet = 2;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 35) bet = 5;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 25) bet = 2;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 45) bet = 2;
    	  else bet = 1;
      }
      else if(Game.round == 11) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -30) bet = 5;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -10) bet = 4;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -20) bet = 2;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 10) bet = 4;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 0) bet = 3;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 30) bet = 5;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 20) bet = 2;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 50) bet = 5;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 40) bet = 2;
    	  else bet = 1;
      }
      else if(Game.round == 12) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -35) bet = 5;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -15) bet = 4;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 5) bet = 4;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -5) bet = 2;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 25) bet = 5;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 15) bet = 2;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 45) bet = 5;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 35) bet = 2;
    	  else bet = 1;
      }
      else if(Game.round == 13) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -40) bet = 5;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -20) bet = 4;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 0) bet = 4;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -10) bet = 2;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 20) bet = 5;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 10) bet = 2;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 40) bet = 5;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 30) bet = 2;
    	  else bet = 1;
      }
      else if(Game.round == 14) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -45) bet = 5;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -25) bet = 4;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -5) bet = 4;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -15) bet = 2;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 15) bet = 5;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 5) bet = 2;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 35) bet = 5;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 25) bet = 2;
    	  else bet = 1;
      }
      else if(Game.round == 15) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -50) bet = 3;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -30) bet = 3;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -10) bet = 3;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 10) bet = 3;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 30) bet = 3;
    	  else if(userCard == 7 && this.detailPer(userCard) >= 50) bet = 3;
    	  else if(userCard == 8 && this.detailPer(userCard) >= 70) bet = 3;
    	  else bet = 1;
      }
      else if(Game.round == 16) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -55) bet = 3;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -35) bet = 3;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -15) bet = 3;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 5) bet = 3;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 25) bet = 3;
    	  else if(userCard == 7 && this.detailPer(userCard) >= 45) bet = 3;
    	  else if(userCard == 8 && this.detailPer(userCard) >= 65) bet = 3;
    	  else bet = 1;
      }
      else if(Game.round == 17) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -60) bet = 3;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -40) bet = 3;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -20) bet = 3;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 0) bet = 3;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 20) bet = 3;
    	  else if(userCard == 7 && this.detailPer(userCard) >= 40) bet = 3;
    	  else if(userCard == 8 && this.detailPer(userCard) >= 60) bet = 3;
    	  else if(userCard == 9 && this.detailPer(userCard) >= 80) bet = 3;
    	  else bet = 1;
      }
      else if(Game.round == 18) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -65) bet = 3;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -45) bet = 3;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -25) bet = 3;
    	  else if(userCard == 5 && this.detailPer(userCard) >= -5) bet = 3;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 15) bet = 3;
    	  else if(userCard == 7 && this.detailPer(userCard) >= 35) bet = 3;
    	  else if(userCard == 8 && this.detailPer(userCard) >= 55) bet = 3;
    	  else if(userCard == 9 && this.detailPer(userCard) >= 75) bet = 3;
    	  else bet = 1;
      }
      else if(Game.round == 19) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -70) bet = 3;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -50) bet = 3;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -30) bet = 3;
    	  else if(userCard == 5 && this.detailPer(userCard) >= -10) bet = 3;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 10) bet = 3;
    	  else if(userCard == 7 && this.detailPer(userCard) >= 30) bet = 3;
    	  else if(userCard == 8 && this.detailPer(userCard) >= 50) bet = 3;
    	  else if(userCard == 9 && this.detailPer(userCard) >= 70) bet = 3;
    	  else bet = 1;
      }
      else if(Game.round == 20) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -75) bet = 3;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -55) bet = 3;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -35) bet = 3;
    	  else if(userCard == 5 && this.detailPer(userCard) >= -15) bet = 3;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 5) bet = 3;
    	  else if(userCard == 7 && this.detailPer(userCard) >= 25) bet = 3;
    	  else if(userCard == 8 && this.detailPer(userCard) >= 45) bet = 3;
    	  else if(userCard == 9 && this.detailPer(userCard) >= 65) bet = 3;
    	  else bet = 1;
      }

      if(bet < userBet) bet = userBet + 1;
      return bet;

      // 민섭이, 태훈이가 짠 AI 배팅 코드
//      if(userCard == 1) //내카드가 1이라면 AI는 배팅(카드 수가 1이니까 다이가 없음)
//      {
//         bet = (int)(Math.random()*(userBet+2))+userBet;
//         if(bet ==0) //ai가 배팅을 0을건다면 0을 못걸게 해야되니까
//         {
//            rebet =(int)(Math.random()*(userBet+2))+userBet+1;
//            return rebet;
//         }
//         else
//            return bet;
//      }
//      else if(userCard>=2 && userCard<=5)
//      {
//         bet = (int)(Math.random()*(userBet+1))+userBet;
//         if(bet == 0)//ai가 배팅을 0을건다면 0을 못걸게 해야되니까
//         {
//            rebet = (int)(Math.random()*(userBet+1))+userBet+1;
//            return rebet;
//         }
//         else
//            return bet;
//      }
//      else if(userCard<=6 && userCard<=8)
//      {
//         bet = (int)(Math.random()*(userBet))+userBet;
//         if(bet == 0)//ai가 배팅을 0을건다면 0을 못걸게 해야되니까
//         {
//            rebet = (int)(Math.random()*(userBet))+userBet+1;
//            return rebet;
//         }
//         else
//            return bet;
//      }
//      else
//      {
//         bet = (int)(Math.random()*(userBet))+userBet;
//         if(bet == 0) //ai가 배팅을 0을건다면 0을 못걸게 해야되니까
//         {
//            rebet = (int)(Math.random()*(userBet))+userBet+1;
//            return rebet;
//         }
//         else
//            return bet;
//      }
   }

   // ai 배팅 다이 여부
   public int aiDiePercentage(int userCard)
   {
      int die =0;
      // 계속 Die만 해도 이기는 경우 Ai는 계속 Die 하도록 구현
      if(User.getCoin() + (21 - Game.round) <= getAiCoin() - (21 - Game.round)) die = 2;
      else if(userCard == 10) die = 2;
      else if(userCard == 1) die = 1;
      else if(Game.round == 1 || Game.round == 2 || Game.round == 3) {
    	  if(userCard == 2 || userCard == 3 || userCard == 4) die = 1;
    	  else die = 2;
      }
      else if(Game.round == 4) {
    	  if(userCard == 2 || userCard == 3) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -5) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 5) die = 1;
    	  else die = 2;
      }
      else if(Game.round == 5) {
    	  if(userCard == 2 || userCard == 3) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 0) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 20) die = 1;
    	  else die = 2;
      }
      else if(Game.round == 7) {
    	  if(userCard == 2 || userCard == 3) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 0) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 10) die = 1;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 30) die = 1;
    	  else die = 2;
      }
      else if(Game.round == 8) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -25) die = 1;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -15) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -5) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 15) die = 1;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 35) die = 1;
    	  else die = 2;
      }
      else if(Game.round == 9) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -30) die = 1;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -20) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 0) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 10) die = 1;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 40) die = 1;
    	  else die = 2;
      }
      else if(Game.round == 10) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -25) die = 1;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -5) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 5) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 25) die = 1;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 45) die = 1;
    	  else die = 2;
      }
      else if(Game.round == 11) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -25) die = 1;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -5) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= 5) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 25) die = 1;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 45) die = 1;
    	  else if(userCard == 7) die = 1;
    	  else die = 2;
      }
      else if(Game.round == 12) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -45) die = 1;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -25) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -5) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 15) die = 1;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 35) die = 1;
    	  else if(userCard == 7 && this.detailPer(userCard) >= 55) die = 1;
    	  else die = 2;
      }
      else if(Game.round == 13) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -50) die = 1;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -30) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -10) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 10) die = 1;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 30) die = 1;
    	  else if(userCard == 7 && this.detailPer(userCard) >= 60) die = 1;
    	  else die = 2;
      }
      else if(Game.round == 14) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -55) die = 1;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -30) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -10) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 10) die = 1;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 30) die = 1;
    	  else if(userCard == 7 && this.detailPer(userCard) >= 50) die = 1;
    	  else if(userCard == 8 && this.detailPer(userCard) >= 65) die = 1;
    	  else die = 2;
      }
      else if(Game.round == 15) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -60) die = 1;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -35) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -15) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 5) die = 1;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 25) die = 1;
    	  else if(userCard == 7 && this.detailPer(userCard) >= 45) die = 1;
    	  else if(userCard == 8 && this.detailPer(userCard) >= 70) die = 1;
    	  else die = 2;
      }
      else if(Game.round == 16) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -65) die = 1;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -40) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -20) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 0) die = 1;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 20) die = 1;
    	  else if(userCard == 7 && this.detailPer(userCard) >= 40) die = 1;
    	  else if(userCard == 8 && this.detailPer(userCard) >= 65) die = 1;
    	  else die = 2;
      }
      else if(Game.round == 17) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -60) die = 1;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -40) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -20) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= 0) die = 1;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 20) die = 1;
    	  else if(userCard == 7 && this.detailPer(userCard) >= 40) die = 1;
    	  else if(userCard == 8 && this.detailPer(userCard) >= 60) die = 1;
    	  else if(userCard == 9 && this.detailPer(userCard) >= 80) die = 1;
    	  else die = 2;
	}
      else if(Game.round == 18) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -65) die = 1;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -45) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -25) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= -5) die = 1;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 15) die = 1;
    	  else if(userCard == 7 && this.detailPer(userCard) >= 35) die = 1;
    	  else if(userCard == 8 && this.detailPer(userCard) >= 55) die = 1;
    	  else if(userCard == 9 && this.detailPer(userCard) >= 75) die = 1;
    	  else die = 2;
      }
      else if(Game.round == 19) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -70) die = 1;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -50) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -30) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= -10) die = 1;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 10) die = 1;
    	  else if(userCard == 7 && this.detailPer(userCard) >= 30) die = 1;
    	  else if(userCard == 8 && this.detailPer(userCard) >= 50) die = 1;
    	  else if(userCard == 9 && this.detailPer(userCard) >= 70) die = 1;
    	  else if(userCard == 10 && this.detailPer(userCard) >= 90) die = 1;
    	  else die = 2;
      }
      else if(Game.round == 20) {
    	  if(userCard == 2 && this.detailPer(userCard) >= -75) die = 1;
    	  else if(userCard == 3 && this.detailPer(userCard) >= -55) die = 1;
    	  else if(userCard == 4 && this.detailPer(userCard) >= -35) die = 1;
    	  else if(userCard == 5 && this.detailPer(userCard) >= -15) die = 1;
    	  else if(userCard == 6 && this.detailPer(userCard) >= 5) die = 1;
    	  else if(userCard == 7 && this.detailPer(userCard) >= 25) die = 1;
    	  else if(userCard == 8 && this.detailPer(userCard) >= 45) die = 1;
    	  else if(userCard == 9 && this.detailPer(userCard) >= 65) die = 1;
    	  else if(userCard == 10 && this.detailPer(userCard) >= 85) die = 1;
    	  else die = 2;
      }

      return die;

      // 민섭 태훈 로직
//      if(userCard ==1) //내카드가 1이면 무조건 걸게하기
//         return 0;
//      else if(userCard>=2 && userCard<=5)
//      {
//         die = (int)(Math.random()*1)+1;
//         return die; //확률 내보내기
//      }
//      else if(userCard<=6 && userCard<=8)
//      {
//         die = (int)(Math.random()*1)+1;
//         return die;
//      }
//      else
//      {
//         die = (int)(Math.random()*3)+1;
//         return die;
//      }

   }
}