package GUI;

import java.awt.Button;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.skhu.Ai;
import net.skhu.Game;
import net.skhu.User;

//첫 화면 frame
public class MainFrame extends JFrame {
	private UserGUI usergui = new UserGUI();
	private AiGUI aigui = new AiGUI();
	private Game game;
	private List list = new List();
	private JLabel label;
	private JTextField textField;
	private User user;
	private Ai ai;
	private int aiBet;
	private int userBet;
	private int aiDie;
	private int totalAiBet;
	private int totalUserBet;
	// 추가배팅count수
	private int count = 0;
	private JPanel panel;
	Button button;

	public MainFrame(Game game) {
		this.game = game;
	}

	// 게임시작 버튼 클릭시 화면 repaint
	public void MainViewchange(JPanel panel) {

		getContentPane().removeAll();
		getContentPane().add(panel);
		revalidate();
		repaint();
	}

	public void gameViewChange(JPanel panel, AiGUI aigui) {

	}

	// 메인 화면 설정
	public JPanel mainView() {
		JPanel panel = new JPanel(null);

		Label label = new Label("INDIAN   POKER\r\n");
		label.setFont(new Font("Dialog", Font.PLAIN, 34));
		label.setBounds(110, 31, 423, 68);
		panel.add(label);

		JLabel jlabel = new JLabel("");
		ImageIcon icon = new ImageIcon(getClass().getResource("/image/joker.png"));
		jlabel.setIcon(icon);
		icon.setImage(icon.getImage().getScaledInstance(180, 200, Image.SCALE_SMOOTH));
		jlabel.setBounds(149, 105, 200, 250);
		panel.add(jlabel);

		button = new Button("\uAC8C\uC784\uC2DC\uC791\r\n");
		button.setBounds(177, 382, 128, 29);
		panel.add(button);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel panel = gameView(game);
				MainViewchange(panel);
			}
		});
		return panel;
	}

	// 게임 시작 누르고 다음 화면 설정
	public JPanel gameView(Game game) {
		panel = new JPanel(null);
		changeText(game, 0);
		Button batting = new Button("배팅");
		batting.setBounds(20, 300, 128, 29); // 배팅
		Button die = new Button("다이");
		die.setBounds(170, 300, 128, 29); // 다이
		Button cont = new Button("계속");
		cont.setBounds(330, 300, 128, 29); // 계속

		panel.add(aigui.changeView(game));
		panel.add(batting);
		panel.add(die);
		panel.add(cont);
		panel.add(usergui.changeView(game, 0));
		panel.add(list);

		Button betBtn = new Button("입력");
		betBtn.setBounds(243, 335, 50, 27);
		panel.add(betBtn);
		betBtn.setEnabled(false);

		label = new JLabel();
		label.setBounds(new Rectangle(86, 310, 120, 79));
		label.setText("배팅할 코인 입력: ");
		panel.add(label);

		JTextField inCoin = new JTextField();
		inCoin.setColumns(3);
		inCoin.setBounds(190, 337, 50, 25);
		panel.add(inCoin);
		inCoin.setEnabled(false);

		//배팅에서 입력 버튼 눌렀을때 이벤트, user가 배팅하면 그에따라 ai가 배팅 or 다이
		betBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s = inCoin.getText();
				// JOptionPane.showMessageDialog(panel, s + "개의 코인이 입력됐습니다");
				inCoin.setText("");
				String msg = "User 배팅=" + s;
				userBet = Integer.parseInt(s);
				// 총 유저가 배팅한 갯수
				totalUserBet += userBet;
				if (userBet > game.getUser().getUserCoin() || totalUserBet > game.getUser().getUserCoin()) {
					// 배팅 건 갯수가 더크다면
					list.add("배팅 건 숫자가 더많습니다!");
					totalUserBet -= userBet;
				}
				//ai가 배팅을 하였는데 user가 같은 수의 배팅을 하였을때 카드를 비교해서 승자 출력
				else if(totalAiBet!=0&&totalUserBet!=0&&totalAiBet==totalUserBet) {
					list.add(msg);
					list.add("user와 ai가 배팅한 수가 같습니다.");
					roundWinner(/*totalUserBet,totalAiBet*/);
				}
				else if(game.getUser().getUserCoin()==totalUserBet&&totalAiBet>0) {
					list.add(msg);
					roundWinner();
				}
				else {
					// *****************************************************************
					System.out.println("유저 배팅값: " + userBet + " 유저토탈 배팅값: " + totalUserBet);
					// *****************************************************************
					list.add(msg);
					if (userBet > 0) {
						battleAiWithUser();

					}
				}
			}
		});

		//배팅 버튼 눌렀을때 입력버튼과 숫자 입력창 활성화
		batting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				betBtn.setEnabled(true);
				inCoin.setEnabled(true);
			}
		});

		// die 버튼 클릭시 이벤트, user가 다이, 유저카드 보여주고 ai 승리
		die.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//라운드가 끝났으니 유저의 카드 보여주기
				usergui.im(game).removeAll();
				//panel.remove(usergui.im(game));
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();
				userDie();
			}
		});

		//계속 버튼 눌렀을 때 이벤트, 라운드가 바뀌고 카드 바뀜
		cont.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (game.getRound() == 11) {
					game.getAi().aiRechargeDeck();
					game.getUser().userRechargeDeck();
				}
				betBtn.setEnabled(false);
				inCoin.setEnabled(false);
				game.setRound();         

				//라운드가 끝났으니 유저의 카드 보여주기
				//ai의 카드도 바꾸기
				usergui.im(game).removeAll();
				//panel.remove(usergui.im(game));
				panel.remove(aigui.im(game));
				panel.add(aigui.changeView(game));
				panel.add(usergui.changeView(game, 0));
				changeText(game, 0);
				panel.repaint();
				
				totalUserBet=0;
				totalAiBet=0;
				userBet=0;
				aiBet=0;

			}
		});

		return panel;
	}

	// 게임화면 밑에 나오는 글씨들
	public void changeText(Game game, int check) {
		list.setBounds(0, 366, 484, 85);
		if (check == 1) {
			String msg = "사용자의 숫자는"+ game.getUser().number(game);
			list.add(msg);
		} else if (game.getRound() == 1) {

			int first = 0;//((int) (Math.random() * 2)) + 1;
			list.add("INDIAN 포커에 오신걸 환영합니다.");
			list.add("\n");
			list.add(String.format("%d라운드를 시작하겠습니다.", game.getRound()));
			String currentCoin = String.format("User 코인: %d, Ai 코인: %d", game.getUser().getUserCoin(),
					game.getAi().getAiCoin());
			list.add(currentCoin);
			list.add("\n");
			String betFirstMsg = first == 1 ? "사용자가 먼저 배팅을 시작합니다" : "Ai가 먼저 배팅을 시작합니다.";
			list.add(betFirstMsg);
			if (first == 1) // 사용자 배팅 시작
			{
				// 위에 버튼이벤트가 활성화되서 addActionListener가 실행
			} else {// ai 배팅 시작
				battleAiWithUser();

			}

		} else if (game.getRound() > 1 && game.getRound() <= 20)// 10라운드 까지
		{
			int first = ((int) (Math.random() * 2)) + 1;
			list.add("\n");
			list.add(String.format("%d라운드를 시작하겠습니다.", game.getRound()));
			String currentCoin = String.format("User 코인: %d, Ai 코인: %d", game.getUser().getUserCoin(),
					game.getAi().getAiCoin());
			list.add(currentCoin);
			String betFirstMsg = first == 1 ? "사용자가 먼저 배팅을 시작합니다" : "Ai가 먼저 배팅을 시작합니다.";
			list.add("\n");
			list.add(betFirstMsg);
			if (first == 1) // 사용자 배팅 시작
			{
				// 위에 버튼이벤트가 활성화되서 addActionListener가 실행
			} else {// ai 배팅 시작
				battleAiWithUser();

			}

			// ********************************************
			System.out.println("totalUserBet: " + totalUserBet + " userBet: " + userBet + " totalAiBet: " + totalAiBet);
			// *******************************************
		}

		else // 라운드 수가 20초과인 라운드 넘어가면
			list.add(game.winner());
	}

	// AI와 User배팅 싸움
	public void battleAiWithUser() {
		// aiBattingBattle이 aiDiePercentage보다 더크다면 -> 배팅할 확률이 더 크다면
		if (game.getAi().aiBattingBattle(game.getUser().number(game),userBet) > game.getAi()
				.aiDiePercentage(game.getUser().number(game))) {
			String ai = String.format("ai가 배팅한 코인 수는 -> %d",
					aiBet = game.getAi().aiBattingBattle(game.getUser().number(game),userBet));
			// 총 AI가 배팅한 갯수
			totalAiBet += aiBet;
			if (aiBet > game.getAi().getAiCoin() || totalAiBet > game.getAi().getAiCoin()) {
				// 배팅 건 갯수가 더크다면
				list.add("ai가 배팅을 자기 코인보다 많이 하려고 합니다!, 유저에게 배팅 걸 기회가 넘어 갑니다");
				totalAiBet -= aiBet;


			}
			//user가 배팅을하였는데 ai가 배팅한 값이 같으면 카드값을 비교해서 결과를 출력해야 한다.
			else if(totalAiBet!=0&&totalUserBet!=0&&totalAiBet==totalUserBet) {
				list.add(ai);
				list.add("ai와 user가 배팅한 수가 같습니다");
				roundWinner(/*totalUserBet,totalAiBet*/);
			}
			else if(game.getAi().getAiCoin()==totalAiBet&&totalUserBet>0) {
				list.add(ai);
				roundWinner();
			}
			else
				list.add(ai);

			// ************************************************************
			System.out.println("aiBet값: " + aiBet + " totalAiBet값: " + totalAiBet);
			// **************************************************************
		} else // 죽을 확률이 더크다면
		{
			String message = "ai가  고민 하더니 다이했습니다";
			// 내가 죽었는지 안죽었는지 확인하는 변수 ->숫자5면 죽었다는 소리
			aiDie = 5;
			if (aiDie == 5) {
				list.add(message);
				aiDieCheck(aiDie);
				aiDie = 0;

			}

		}
	}

	public JPanel aiDieCheck(int aiDie) {
		if (aiDie == 5) {
			if (aiBet == 0 && totalUserBet == 0) {
				System.out.println("here");
				game.getUser().setCoin(game.getUser().getUserCoin() + 1);
				game.getAi().aiBetCoin(1);
				count = 0; // 0으로초기화
				list.add("유저가 승리했습니다!");

				//라운드가 끝났으니 유저의 카드 보여주기
				usergui.im(game).removeAll();
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();
			} else if (aiBet == 0 && userBet > 0) // 내가 처음에 배팅걸고 ai가 바로죽는다면
			{
				game.getUser().setCoin(game.getUser().getUserCoin() + 1);
				game.getAi().aiBetCoin(1);
				count = 0; // 0으로초기화
				list.add("유저가 승리했습니다!");

				//라운드가 끝났으니 유저의 카드 보여주기
				usergui.im(game).removeAll();
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();

				totalUserBet = 0;
				userBet = 0;

			} else if (aiBet > 0 || userBet > 0) // 배팅을 걸었는데 AI가 죽었다면 AI가배팅건 코인들과 user가 그코인들을 가져가야 한다
			{
				game.getUser().setCoin(game.getUser().getUserCoin() - userBet + totalAiBet + userBet);
				game.getAi().setCoin(game.getAi().getAiCoin() - totalAiBet);
				count = 0; // 0으로초기화
				list.add("유저가 승리했습니다!");

				//라운드가 끝났으니 유저의 카드 보여주기
				usergui.im(game).removeAll();
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();

				//배팅한값 다 초기화
				aiBet = 0;
				totalUserBet = 0;
				userBet = 0;
				totalAiBet = 0;
			}

		}
		// AI가 배팅걸었다면
		else {
			if (count < 2) // 추가배팅 3회로 제한하기
			{
				// 배팅 누를때마다 위에 addActionListener호출 된다
				list.add("사용자는 추가배팅 원하시면 배팅누르고 입력하세요!");
				count++;
				list.add(String.format("count 수는 %d", count));
			} else // 추가배팅이 3회가 지나면
			{
				// list.add("추가 배팅 3회초과");
				// list.add(String.format("game.getAi().number()는 %d",
				// game.getAi().number(game)));

				//user의 카드가 ai의 카드보다 클때
				if (game.getUser().number(game) > game.getAi().number(game))
				{
					//라운드가 끝났으니 유저의 카드 보여주기
					usergui.im(game).removeAll();
					panel.add(usergui.changeView(game, 1));
					changeText(game, 1);
					panel.repaint();

					//내가 배팅했던 코인과 ai배팅했던코인먹기
					game.getUser().setCoin(game.getUser().getUserCoin() + totalAiBet); 
					game.getAi().setCoin(game.getAi().getAiCoin() - totalAiBet);
					count = 0; // 0으로 초기화
					list.add("유저가 승리했습니다!");

				} else // AI카드가 더 크다면
				{
					//라운드가 끝났으니 유저의 카드 보여주기
					usergui.im(game).removeAll();
					panel.add(usergui.changeView(game, 1));
					changeText(game, 1);
					panel.repaint();

					//ai가 이겼으므로 user가 배팅한 코인 ai가 먹는다
					game.getAi().setCoin(game.getAi().getAiCoin() + totalUserBet);
					game.getUser().setCoin(game.getUser().getUserCoin() - totalUserBet);
					count = 0; // 0으로 초기화
					list.add("AI가 승리했습니다");
				}
			}
		}
		return panel;

	}

	// 사용자가 다이할때
	public void userDie() {
		if (totalUserBet == 0) // 내가 배팅을 한번도 안걸었다면
		{
			game.getUser().setCoin(game.getUser().getUserCoin() - 1);
			game.getAi().setCoin(game.getAi().getAiCoin() + 1);
			list.add(String.format("die 하셨습니다 User코인이 %d 줄고 다음라운드로 넘어갑니다", 1));
		} else // 내가 배팅을 한번이라도 걸고 다이를 눌렀다면
		{
			game.getUser().setCoin(game.getUser().getUserCoin() - totalUserBet);
			game.getAi().setCoin(game.getAi().getAiCoin() + totalUserBet);
			list.add(String.format("die 하셨습니다 User코인이 %d 줄고 다음라운드로 넘어갑니다", totalUserBet));

		}
		totalUserBet =0;
		totalAiBet=0;
	}

	//서로 배팅했을때 상황에 따라 승자 결과
	public void roundWinner(/*int userTBcoin, int aiTBcoin*/) {
		//user가 총 배팅한 코인수와 ai가 총 배팅한 코인 수가 같으면
		//if(userTBcoin==aiTBcoin) {
		if(totalUserBet==totalAiBet) {
			//user와 ai의 카드를 비교한다 

			//user의 카드가 더크면!
			if(game.getUser().number(game)>game.getAi().number(game)) {
				list.add("user 카드가 ai의 카드보다 크므로 user 승리");

				//라운드가 끝났으니 유저의 카드 보여주기
				usergui.im(game).removeAll();
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();

				//user가 이겼으니 ai가 총 배팅했던코인먹기
				game.getUser().setCoin(game.getUser().getUserCoin() + totalAiBet); 
				//ai가 졌으므로 ai가 가지고 있는 코인에서  총 배팅한 코인을 뺀다.
				game.getAi().setCoin(game.getAi().getAiCoin() - totalAiBet);

				

			}
			//ai의 카드가 더크면!
			else if(game.getUser().number(game)<game.getAi().number(game)) {
				list.add("ai 카드가 user의 카드보다 크므로 ai 승리");

				//라운드가 끝났으니 유저의 카드 보여주기
				usergui.im(game).removeAll();
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();

				//user가 졌으므로 가지고 있는 코인에서 배팅한 코인을 뺀다.
				game.getUser().setCoin(game.getUser().getUserCoin() - totalUserBet);
				//ai가 이겼으므로 user가 배팅한 코인을 먹는다.
				game.getAi().setCoin(game.getAi().getAiCoin() + totalUserBet);

				

			}
		}


		//ai가 코인을 배팅에 올인
		else if(game.getAi().getAiCoin()==totalAiBet) {
			//user의 카드가 더크면!
			if(game.getUser().number(game)>game.getAi().number(game)) {
				list.add("user 카드가 ai의 카드보다 크므로 user 승리");

				//라운드가 끝났으니 유저의 카드 보여주기
				usergui.im(game).removeAll();
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();

				//user가 이겼으니 ai가 총 배팅했던코인먹기
				game.getUser().setCoin(game.getUser().getUserCoin() + totalAiBet); 
				//ai가 졌으므로 ai가 가지고 있는 코인에서  총 배팅한 코인을 뺀다.
				game.getAi().setCoin(game.getAi().getAiCoin() - totalAiBet);
				
				//ai가 코인을 올인했고, 카드비교시 user가 이겼고, ai가 코인을 다 소진하면
				if(game.getAi().getAiCoin()==0) {
					list.add("ai가 코인을 모두 소진하였습니다. 게임이 끝났습니다");
					list.add(game.coinExhaustion());
				}

				

			}
			//ai의 카드가 더크면!
			else if(game.getUser().number(game)<game.getAi().number(game)) {
				list.add("ai 카드가 user의 카드보다 크므로 ai 승리");

				//라운드가 끝났으니 유저의 카드 보여주기
				usergui.im(game).removeAll();
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();

				//user가 졌으므로 가지고 있는 코인에서 배팅한 코인을 뺀다.
				game.getUser().setCoin(game.getUser().getUserCoin() - totalUserBet);
				//ai가 이겼으므로 user가 배팅한 코인을 먹는다.
				game.getAi().setCoin(game.getAi().getAiCoin() + totalUserBet);
				
				//ai가 코인을 올인했는데 카드비교했을때 ai가 이기고, user가 코인을 다 소비하면
				if(game.getUser().getUserCoin()==0) {
					list.add("user가 코인을 모두 소비했습니다. 게임이 끝났습니다");
					list.add(game.coinExhaustion());
				}

				

			}

		}
		//user가 코인을 올인
		else if(game.getUser().getUserCoin()==totalUserBet) {
			//user의 카드가 더크면!
			if(game.getUser().number(game)>game.getAi().number(game)) {
				list.add("user 카드가 ai의 카드보다 크므로 user 승리");

				//라운드가 끝났으니 유저의 카드 보여주기
				usergui.im(game).removeAll();
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();

				//user가 이겼으니 ai가 총 배팅했던코인먹기
				game.getUser().setCoin(game.getUser().getUserCoin() + totalAiBet); 
				//ai가 졌으므로 ai가 가지고 있는 코인에서  총 배팅한 코인을 뺀다.
				game.getAi().setCoin(game.getAi().getAiCoin() - totalAiBet);

				//user가 코인을 올인했고, 카드비교시 user가 이겼고, ai가 코인을 다 소진하면
				if(game.getAi().getAiCoin()==0) {
					list.add("ai가 코인을 모두 소진하였습니다. 게임이 끝났습니다");
					list.add(game.coinExhaustion());
				}

				

			}
			//ai의 카드가 더크면!
			else if(game.getUser().number(game)<game.getAi().number(game)) {
				list.add("ai 카드가 user의 카드보다 크므로 ai 승리");

				//라운드가 끝났으니 유저의 카드 보여주기
				usergui.im(game).removeAll();
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();

				//user가 졌으므로 가지고 있는 코인에서 배팅한 코인을 뺀다.
				game.getUser().setCoin(game.getUser().getUserCoin() - totalUserBet);
				//ai가 이겼으므로 user가 배팅한 코인을 먹는다.
				game.getAi().setCoin(game.getAi().getAiCoin() + totalUserBet);


				//user가 코인을 올인했는데 카드비교했을때 ai가 이기면
				if(game.getUser().getUserCoin()==0) {
					list.add("user가 코인을 모두 소비했습니다. 게임이 끝났습니다");
					list.add(game.coinExhaustion());
				}
				

			}

		}
		totalUserBet=0;
		userBet=0;
		totalAiBet=0;
		aiBet=0;
	}
	
}
