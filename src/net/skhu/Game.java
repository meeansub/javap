package net.skhu;

public class Game {
	
	private User user;
	private Ai ai;
	static public int round;
	
	public Game(){
		user = new User();
		ai = new Ai();
		round = 1;
	}
	public Ai getAi(){
		return ai;
	}
	public int getRound(){
		return round;
	}
	public void userDie(){
		ai.setUserCard((int)user.userCardDeck().get(round-1));
	}
	public User getUser(){
		return user;
	}
	public void setRound(){
		round=round+1;
	}
	
	
}
