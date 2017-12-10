package GUI;

import java.awt.Button;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

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
	private int count;
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
				inCoin.setText("");
				String msg = "User 배팅=" + s;
				userBet = Integer.parseInt(s);
				if(userBet<=0)
				{
					list.add("0이하로는 배팅할 수 없습니다. 다시 배팅해주세요");
					return ;
				}

				else {
					// 총 유저가 배팅한 갯수
					totalUserBet += userBet;

					if(totalUserBet<totalAiBet && totalUserBet==game.getUser().getUserCard()) {
						roundWinner();
					}
					//user가 ai가 배팅한 수보다 적게 못걸게 하기
					else if(totalUserBet<totalAiBet) {
						userLimitCoin(totalUserBet,totalAiBet);
						totalUserBet -=userBet;


					}
					else if (userBet > game.getUser().getUserCoin() || totalUserBet > game.getUser().getUserCoin()) {
						// 배팅 건 갯수가 더크다면
						list.add("배팅 건 숫자 현재 보유한 코인 보다 더많습니다!");
						list.add("다시 배팅");
						totalUserBet -= userBet;

					}
					else {
						count++;
						System.out.println("count수 세보기 "+count);
						//ai가 배팅을 하였는데 user가 같은 수의 배팅을 하였을때 카드를 비교해서 승자 출력
						if(((totalAiBet!=0&&totalUserBet!=0)&&totalAiBet==totalUserBet)) {
							list.add(msg);
							list.add("user와 ai가 총 배팅한 수가 같습니다.");
							roundWinner();
						}
						//유저가 코인을 올인 하였고, ai가 배팅한 상태일때
						else if(game.getUser().getUserCoin()==totalUserBet&&totalAiBet>0) {
							list.add(msg);
							roundWinner();
						}
						else {
							// *****************************************************************
							System.out.println("유저 배팅값: " + userBet + " 유저토탈 배팅값: " + totalUserBet);
							// *****************************************************************
							list.add(msg);

							if(count ==3) //추가배팅 3회로 하기
							{
								//                  list.add(game.winner());
								//                  roundTwentyEnd(); //20라운드 되면  종료 시키기 ->라인 738
								list.add("추가 배팅3회가 결과를 발표합니다!");
								roundWinner();
								count = 0;//0으로 초기화
								return ;

							}
							battleAiWithUser();

						}

					}
				}
			}
		});

		// 배팅 버튼 눌렀을때 입력버튼과 숫자 입력창 활성화
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
				// 라운드가 끝났으니 유저의 카드 보여주기
				usergui.im(game).removeAll();
				// panel.remove(usergui.im(game));
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();
				userDie();
				count = 0; // 라운드가넘어갈때마다 초기화 시키기
			}
		});

		// 계속 버튼 눌렀을 때 이벤트, 라운드가 바뀌고 카드 바뀜
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
				// 라운드가 끝났으니 유저의 카드 보여주기
				// ai의 카드도 바꾸기
				if (game.getRound() <= 20) {
					usergui.im(game).removeAll();
					// panel.remove(usergui.im(game));
					panel.remove(aigui.im(game));
					panel.add(aigui.changeView(game));
					panel.add(usergui.changeView(game, 0));
					changeText(game, 0);
					panel.repaint();
				} else if (game.getRound() > 20) {
					list.add("");
					list.add("게임이 끝났습니다");
					list.add(game.winner());
					roundTwentyEnd();
				}

				totalUserBet = 0;
				totalAiBet = 0;
				userBet = 0;
				aiBet = 0;
				count = 0;
			}
		});

		return panel;
	}

	// 게임화면 밑에 나오는 글씨들
	public void changeText(Game game, int check) {
		list.setBounds(0, 366, 484, 85);
		if (check == 1) {
			String msg = "사용자의 숫자는" + game.getUser().number(game);
			list.add(msg);
		} else if (game.getRound() == 1) { // 1라운드 시작

			int first =((int) (Math.random() * 2)) + 1;
			list.add("INDIAN 포커에 오신걸 환영합니다.");
			list.add("\n");
			list.add(String.format("%d라운드를 시작하겠습니다.", game.getRound()));
			String currentCoin = String.format("User 코인: %d, Ai 코인: %d, 무승부 코인: %d", game.getUser().getUserCoin(),
					game.getAi().getAiCoin(), game.getDrawCoin());
			list.add(currentCoin);
			list.add("\n");
			String betFirstMsg = first == 1 ? "사용자가 먼저 배팅을 시작합니다" : "Ai가 먼저 배팅을 시작합니다.";
			list.add("추가배팅은 3회까지 가능합니다!");
			list.add("\n");
			list.add(betFirstMsg);

			if (first == 1) // 사용자 배팅 시작
			{
				// 위에 버튼이벤트가 활성화되서 addActionListener가 실행
			} else { // ai 배팅 시작
				//            count += 1; // AI가 먼저 배팅을 하게 되면 count수를 1증가
				battleAiWithUser();
			}

		} else if (game.getRound() > 1 && game.getRound() <= 20)// 10라운드 까지
		{
			if (game.exhaustion() == true) // 코인을 다쓰게 되면 승리자 판별하기
			{
				roundWinner();
				list.add(game.coinExhaustion());
				System.exit(0);
			} else // 코인을 다 안썼다면
			{
				int first = ((int) (Math.random() * 2)) + 1;
				list.add("\n");
				list.add(String.format("%d라운드를 시작하겠습니다.", game.getRound()));
				String currentCoin = String.format("User 코인: %d, Ai 코인: %d, 무승부 코인: %d", game.getUser().getUserCoin(),
						game.getAi().getAiCoin(), game.getDrawCoin());
				list.add(currentCoin);
				list.add("\n");
				String betFirstMsg = first == 1 ? "사용자가 먼저 배팅을 시작합니다" : "Ai가 먼저 배팅을 시작합니다.";
				list.add("추가배팅은 3회까지 가능합니다!");
				list.add("\n");
				list.add(betFirstMsg);

				if (first == 1) // 사용자 배팅 시작
				{
					// 위에 버튼이벤트가 활성화되서 addActionListener가 실행
				} else {// ai 배팅 시작
					//               count += 1; // AI가 먼저 배팅을 하게 되면 count수를 1증가
					battleAiWithUser();

				}

				// ********************************************
				System.out.println(
						"totalUserBet: " + totalUserBet + " userBet: " + userBet + " totalAiBet: " + totalAiBet);
				// *******************************************
			}
		}

		else // 라운드 수가 20초과인 라운드 넘어가면
			list.add(game.winner());
	}

	// AI와 User배팅 싸움
	public void battleAiWithUser() {
		// aiBattingBattle이 aiDiePercentage보다 더크다면 -> 배팅할 확률이 더 크다면
		if (game.getAi().aiBattingBattle(game.getUser().number(game), userBet) >= game.getAi()
				.aiDiePercentage(game.getUser().number(game))) {

			String ai = String.format("ai가 배팅한 코인 수는 -> %d",
					aiBet = game.getAi().aiBattingBattle(game.getUser().number(game), userBet));
			// 총 AI가 배팅한 갯수

			totalAiBet += aiBet;



			if (aiBet > game.getAi().getAiCoin() || totalAiBet > game.getAi().getAiCoin()) {
				// 배팅 건 갯수가 더크다면
				list.add("ai가 배팅을 자기 코인보다 많이 하려고 합니다!,ai 다시 배팅");
				totalAiBet -= aiBet;
				againBatting();  // -> 657
				roundWinner();
				return ;
			}
			// user가 배팅을하였는데 ai가 총 배팅한 값과 같으면 카드값을 비교해서 결과를 출력해야 한다.
			else if (((totalAiBet != 0 && totalUserBet != 0) && totalAiBet == totalUserBet)) {
				list.add(ai);
				list.add("\n");
				list.add("ai와 user가 배팅한 수가 같습니다");
				roundWinner();
			}
			// 유저가 배팅한 상태이고, ai가 코인을 모두 배팅하였을때
			else if (game.getAi().getAiCoin() == totalAiBet && totalUserBet > 0) {
				list.add(ai);
				roundWinner();
			}
			// 유저가 코인을 다 배팅한상태이고, ai가 배팅을 하였을때
			else if (totalUserBet == game.getUser().getUserCoin() && totalAiBet > 0) {
				list.add(ai);
				roundWinner();

			} else
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
			// 서로 배팅을 하지 않았는데, 에이아이가 다이 할때
			if (aiBet == 0 && totalUserBet == 0) {
				System.out.println("here");
				// 유저가 코인하나를 먹고, 무승부 코인있으면 먹기
				game.getUser().setCoin(game.getUser().getUserCoin() + 1 + game.getDrawCoin());
				game.getAi().aiBetCoin(1);
				count = 0; // 0으로초기화
				list.add("유저가 승리했습니다!");

				// 라운드가 끝났으니 유저의 카드 보여주기
				if (game.getRound() <= 20) {
					usergui.im(game).removeAll();
					panel.add(usergui.changeView(game, 1));
					changeText(game, 1);
					panel.repaint();
				}

				game.setDrawCoin(0);
			} else if (aiBet == 0 && userBet > 0) // 내가 처음에 배팅걸고 ai가 바로죽는다면
			{
				game.getUser().setCoin(game.getUser().getUserCoin() + 1 + game.getDrawCoin());
				game.getAi().aiBetCoin(1);
				count = 0; // 0으로초기화
				list.add("유저가 승리했습니다!");

				// 라운드가 끝났으니 유저의 카드 보여주기
				if (game.getRound() <= 20) {
					usergui.im(game).removeAll();
					panel.add(usergui.changeView(game, 1));
					changeText(game, 1);
					panel.repaint();
				}

				game.setDrawCoin(0);
				totalUserBet = 0;
				userBet = 0;

			} else if (aiBet > 0 || userBet > 0) // 서로 배팅을 걸었는데 AI가 죽었다면
			{
				// AI가배팅건 코인들과 user가 그코인들을 가져가야 한다, 무승부코인있으면 먹기
				game.getUser()
				.setCoin(game.getUser().getUserCoin() - userBet + totalAiBet + userBet + game.getDrawCoin());
				game.getAi().setCoin(game.getAi().getAiCoin() - totalAiBet);
				count = 0; // 0으로초기화
				list.add("유저가 승리했습니다!");

				// 라운드가 끝났으니 유저의 카드 보여주기
				if (game.getRound() <= 20) {
					usergui.im(game).removeAll();
					panel.add(usergui.changeView(game, 1));
					changeText(game, 1);
					panel.repaint();
				}

				// 배팅한값 다 초기화
				game.setDrawCoin(0);
				aiBet = 0;
				totalUserBet = 0;
				userBet = 0;
				totalAiBet = 0;
			}

		}

		return panel;

	}

	// 사용자가 다이할때
	public void userDie() {
		if (totalUserBet == 0) // 내가 배팅을 한번도 안걸었다면
		{
			game.getUser().setCoin(game.getUser().getUserCoin() - 1);
			game.getAi().setCoin(game.getAi().getAiCoin() + 1 + game.getDrawCoin());
			list.add(String.format("die 하셨습니다 User코인이 %d 줄고 다음라운드로 넘어갑니다", 1));
		} else // 내가 배팅을 한번이라도 걸고 다이를 눌렀다면
		{
			game.getUser().setCoin(game.getUser().getUserCoin() - totalUserBet);
			game.getAi().setCoin(game.getAi().getAiCoin() + totalUserBet + game.getDrawCoin());
			list.add(String.format("die 하셨습니다 User코인이 %d 줄고 다음라운드로 넘어갑니다", totalUserBet));

		}
		game.setDrawCoin(0);
		totalUserBet = 0;
		totalAiBet = 0;
	}

	public void gameWinRule() {
		// user의 카드가 더크면!(user승)
		if (game.getUser().number(game) > game.getAi().number(game)) {
			list.add("\n");
			list.add("user 카드가 ai의 카드보다 크므로 user 승리");

			// 라운드가 끝났으니 유저의 카드 보여주기
			if (game.getRound() <= 20) {
				usergui.im(game).removeAll();
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();
			}
			// user가 이겼으니 ai가 총 배팅했던코인먹기, 전 라운드가 무승부 였다면 무승부 코인도 먹기
			game.getUser().setCoin(game.getUser().getUserCoin() + totalAiBet + game.getDrawCoin());
			// ai가 졌으므로 ai가 가지고 있는 코인에서 총 배팅한 코인을 뺀다.
			game.getAi().setCoin(game.getAi().getAiCoin() - totalAiBet);

			// 무승부 코인 0으로 초기화
			game.setDrawCoin(0);

		}
		// ai의 카드가 더크면!(ai승)
		else if (game.getUser().number(game) < game.getAi().number(game)) {
			list.add("\n");
			list.add("ai 카드가 user의 카드보다 크므로 ai 승리");

			// 라운드가 끝났으니 유저의 카드 보여주기
			if (game.getRound() <= 20) {
				usergui.im(game).removeAll();
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();
			}

			// ai가 이겼으므로 user가 배팅한 코인을 먹는다., 전 라운드가 무승부였다면 무승부 코인도 먹기
			game.getAi().setCoin(game.getAi().getAiCoin() + totalUserBet + game.getDrawCoin());
			// user가 졌으므로 가지고 있는 코인에서 배팅한 코인을 뺀다.
			game.getUser().setCoin(game.getUser().getUserCoin() - totalUserBet);

			// 무승부 코인 0으로 초기화
			game.setDrawCoin(0);

		}
		// ai의 카드와 user의 카드가 같으면(무승부)
		else if (game.getUser().number(game) == game.getAi().number(game)) {
			list.add("\n");
			list.add("ai 카드와 user가 카드가 같습니다");
			list.add("이번 라운드 코인을 저장하고, 다음라운드 승자가 저장한 코인을 가져갑니다.");
			// 비겼으므로 서로 배팅한 코인 합쳐 저장하고, 다음라운드에 사용
			game.setDrawCoin(totalAiBet + totalUserBet);
			// 비겼으므로 user가 배팅한 코인 빼기
			game.getUser().setCoin(game.getUser().getUserCoin() - totalUserBet);
			// 비겼으므로 ai가 배팅한 코인 빼기
			game.getAi().setCoin(game.getAi().getAiCoin() - totalAiBet);

			// 라운드가 끝났으니 유저의 카드 보여주기
			if (game.getRound() <= 20) {
				usergui.im(game).removeAll();
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();
			}
		}
	}

	// 서로 배팅했을때 상황에 따라 승자 결과
	public void roundWinner() {
		// user가 총 배팅한 코인수와 ai가 총 배팅한 코인 수가 같으면
		if (totalUserBet == totalAiBet) {
			// user와 ai의 카드를 비교한다
			gameWinRule();
		}

		// ai가 코인을 배팅에 올인
		else if (game.getAi().getAiCoin() == totalAiBet) {
			gameWinRule();

			// ai가 코인을 올인했고, 카드비교시 user가 이겼고, ai가 코인을 다 소진하면
			if (game.getAi().getAiCoin() == 0) {
				list.add("ai가 코인을 모두 소진하였습니다. 게임이 끝났습니다");
				list.add(game.coinExhaustion());
				addBattingEnd(); // 메소드 라인수 -> 712
			}

			// ai가 코인을 올인했는데 카드비교했을때 ai가 이기고, user가 코인을 다 소비하면
			if (game.getUser().getUserCoin() == 0) {
				list.add("user가 코인을 모두 소비했습니다. 게임이 끝났습니다");
				list.add(game.coinExhaustion());
				addBattingEnd(); // 메소드 라인수 -> 712
			}

		}

		// user가 코인을 올인
		else if (game.getUser().getUserCoin() == totalUserBet) {
			gameWinRule();
			// user가 코인을 올인했고, 카드비교시 user가 이겼고, ai가 코인을 다 소진하면
			if (game.getAi().getAiCoin() == 0) {
				list.add("ai가 코인을 모두 소진하였습니다. 게임이 끝났습니다");
				list.add(game.coinExhaustion());
				addBattingEnd(); // 메소드 라인수 -> 712
			}
			// user가 코인을 올인했는데 카드비교했을때 ai가 이기면
			if (game.getUser().getUserCoin() == 0) {
				list.add("user가 코인을 모두 소비했습니다. 게임이 끝났습니다");
				list.add(game.coinExhaustion());
				addBattingEnd(); // 메소드 라인수 -> 712
			}

		}

		// 추가 배팅 3회되면
		// 카드를 까서 승리자 가리기
		else if (count == 3) {
			gameWinRule();
			// user가 코인을 올인했고, 카드비교시 user가 이겼고, ai가 코인을 다 소진하면
			if (game.getAi().getAiCoin() == 0) {
				list.add("ai가 코인을 모두 소진하였습니다. 게임이 끝났습니다");
				list.add(game.coinExhaustion());
				addBattingEnd(); // 메소드 라인수 -> 712

			}
			// user가 코인을 올인했는데 카드비교했을때 ai가 이기면
			if (game.getUser().getUserCoin() == 0) {
				list.add("user가 코인을 모두 소비했습니다. 게임이 끝났습니다");
				list.add(game.coinExhaustion());
				addBattingEnd(); // 메소드 라인수 -> 712
			}
		}

		totalUserBet = 0;
		userBet = 0;
		totalAiBet = 0;
		aiBet = 0;
	}

	// 유저가 ai가 배팅한거보다 더작게 못걸게 막기
	public void userLimitCoin(int totalUserBet, int totalAiBet) {
		if (totalAiBet > totalUserBet) // 유저가 AI배팅수보다 작으면
		{
			list.add("AI가 건 배팅코인 수보다 작습니다 같거나 크게 다시 배팅하세요");
			return;
		}
	}

	// 게임 끝나게 하기
	public void addBattingEnd() {
		list.add("수고 하셨습니다 재밌었나요? 10초뒤에 종료됩니다 ");
		list.add("다시 또 뵈요~");
		Timer timer = new Timer();
		TimerTask time = new TimerTask() {
			@Override
			public void run() {
				System.exit(0); // 강제 종료
			}
		};
		timer.schedule(time, 10000); // 게임이 끝나면 10초후에 종료시키기

	}

	// 20라운드 되면 끝내기
	public void roundTwentyEnd() {
		if (game.getRound() > 20) {
			list.add("수고 하셨습니다 재밌었나요? 10초뒤에 종료됩니다 ");
			list.add("다시 또 뵈요~");
			Timer timer = new Timer();
			TimerTask time = new TimerTask() {
				@Override
				public void run() {
					System.exit(0); // 강제 종료
				}
			};
			timer.schedule(time, 10000); // 게임이 끝나면 10초후에 종료시키기
		}

	}
	//추가배팅걸때 총 코인수보다 더놓게 걸기
	public int againBatting() // ->339
	{
		// 두 수를 빼면 이 것 보다 이상 더걸게 해야한다
		int rebet = (int)(Math.random()*(game.getAi().getAiCoin()-totalAiBet-totalUserBet-1))+Math.abs(totalUserBet - totalAiBet);
		list.add(String.format("ai가 배팅할 코인 수는  -> %d", rebet));
		totalAiBet += rebet;
		return rebet;
	}
}