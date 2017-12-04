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

//ù ȭ�� frame
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
	// �߰�����count��
	private int count = 0;
	private JPanel panel;
	Button button;

	public MainFrame(Game game) {
		this.game = game;
	}

	// ���ӽ��� ��ư Ŭ���� ȭ�� repaint
	public void MainViewchange(JPanel panel) {

		getContentPane().removeAll();
		getContentPane().add(panel);
		revalidate();
		repaint();
	}

	public void gameViewChange(JPanel panel, AiGUI aigui) {

	}

	// ���� ȭ�� ����
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

	// ���� ���� ������ ���� ȭ�� ����
	public JPanel gameView(Game game) {
		panel = new JPanel(null);
		changeText(game, 0);
		Button batting = new Button("����");
		batting.setBounds(20, 300, 128, 29); // ����
		Button die = new Button("����");
		die.setBounds(170, 300, 128, 29); // ����
		Button cont = new Button("���");
		cont.setBounds(330, 300, 128, 29); // ���

		panel.add(aigui.changeView(game));
		panel.add(batting);
		panel.add(die);
		panel.add(cont);
		panel.add(usergui.changeView(game, 0));
		panel.add(list);

		Button betBtn = new Button("�Է�");
		betBtn.setBounds(243, 335, 50, 27);
		panel.add(betBtn);
		betBtn.setEnabled(false);

		label = new JLabel();
		label.setBounds(new Rectangle(86, 310, 120, 79));
		label.setText("������ ���� �Է�: ");
		panel.add(label);

		JTextField inCoin = new JTextField();
		inCoin.setColumns(3);
		inCoin.setBounds(190, 337, 50, 25);
		panel.add(inCoin);
		inCoin.setEnabled(false);

		// if(aiDie ==5)
		// {
		// panel.remove(usergui.im(game));
		// panel.add(usergui.changeView(game, 1));
		// changeText(game,1);
		// panel.repaint();
		// game.getUser().setCoin(game.getUser().getUserCoin()+1);
		// game.getAi().setCoin(game.getAi().getAiCoin()-1);
		// }

		betBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s = inCoin.getText();
				// JOptionPane.showMessageDialog(panel, s + "���� ������ �Էµƽ��ϴ�");
				inCoin.setText("");
				String msg = "User ����=" + s;
				userBet = Integer.parseInt(s);
				// �� ������ ������ ����
				totalUserBet += userBet;
				if (userBet > game.getUser().getUserCoin() || totalUserBet > game.getUser().getUserCoin()) {
					// ���� �� ������ ��ũ�ٸ�
					list.add("���� �� ���ڰ� �������ϴ�!");
					totalUserBet -= userBet;
				}

				else {
					// *****************************************************************
					System.out.println("���� ���ð�: " + userBet + " ������Ż ���ð�: " + totalUserBet);
					// *****************************************************************
					list.add(msg);
					if (userBet > 0) {
						battleAiWithUser();

					}
				}
			}
		});
		batting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				betBtn.setEnabled(true);
				inCoin.setEnabled(true);
			}
		});
		// die ��ư Ŭ����
		die.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				usergui.im(game).removeAll();
				//panel.remove(usergui.im(game));
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();
				userDie();
			}
		});

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
				usergui.im(game).removeAll();

				//panel.remove(usergui.im(game));
				panel.remove(aigui.im(game));
				panel.add(aigui.changeView(game));

				panel.add(usergui.changeView(game, 0));

				changeText(game, 0);
				panel.repaint();

			}
		});

		return panel;
	}

	// ����ȭ�� �ؿ� ������ �۾���
	public void changeText(Game game, int check) {
		list.setBounds(0, 366, 484, 85);
		if (check == 1) {
			String msg = "������� ���ڴ�" + game.getUser().number(game);
			list.add(msg);
		} else if (game.getRound() == 1) {

			int first = 0;//((int) (Math.random() * 2)) + 1;
			list.add("INDIAN ��Ŀ�� ���Ű� ȯ���մϴ�.");
			list.add(String.format("%d���带 �����ϰڽ��ϴ�.", game.getRound()));
			String currentCoin = String.format("User ����: %d, Ai ����: %d", game.getUser().getUserCoin(),
					game.getAi().getAiCoin());
			list.add(currentCoin);
			String betFirstMsg = first == 1 ? "����ڰ� ���� ������ �����մϴ�" : "Ai�� ���� ������ �����մϴ�.";
			list.add(betFirstMsg);
			if (first == 1) // ����� ���� ����
			{
				// ���� ��ư�̺�Ʈ�� Ȱ��ȭ�Ǽ� addActionListener�� ����
			} else {// ai ���� ����
				battleAiWithUser();

			}

		} else if (game.getRound() > 1 && game.getRound() <= 20)// 10���� ����
		{
			int first = ((int) (Math.random() * 2)) + 1;
			list.add(String.format("%d���带 �����ϰڽ��ϴ�.", game.getRound()));
			String currentCoin = String.format("User ����: %d, Ai ����: %d", game.getUser().getUserCoin(),
					game.getAi().getAiCoin());
			list.add(currentCoin);
			String betFirstMsg = first == 1 ? "����ڰ� ���� ������ �����մϴ�" : "Ai�� ���� ������ �����մϴ�.";
			list.add(betFirstMsg);
			if (first == 1) // ����� ���� ����
			{
				// ���� ��ư�̺�Ʈ�� Ȱ��ȭ�Ǽ� addActionListener�� ����
			} else {// ai ���� ����
				battleAiWithUser();

			}

			// ********************************************
			System.out.println("totalUserBet: " + totalUserBet + " userBet: " + userBet + " totalAiBet: " + totalAiBet);
			// *******************************************
		}
		/*
		 * �ϴ� ��� else if(game.getRound() <=10)//10���� ���� �ƴ϶�� {
		 * if(game.exhaustion() == true) { list.add(game.coinExhaustion());
		 * return ; } list.add("\n"); list.add(game.getRound() +
		 * "���带 �����ϰڽ��ϴ�."); String s=
		 * String.format("User ����: %d, Ai ����: %d",game.getUser().getUserCoin(),
		 * game.getAi().getAiCoin()); list.add(s); }
		 */
		else // ���� ���� 20�ʰ��� ���� �Ѿ��
			list.add(game.winner());
	}

	// AI�� User���� �ο�
	public void battleAiWithUser() {
		// aiBattingBattle�� aiDiePercentage���� ��ũ�ٸ� -> ������ Ȯ���� �� ũ�ٸ�
		if (game.getAi().aiBattingBattle(game.getUser().number(game),userBet) > game.getAi()
				.aiDiePercentage(game.getUser().number(game))) {
			String ai = String.format("ai�� ������ ���� ���� -> %d",
					aiBet = game.getAi().aiBattingBattle(game.getUser().number(game),userBet));
			// �� AI�� ������ ����
			totalAiBet += aiBet;
			if (aiBet > game.getAi().getAiCoin() || totalAiBet > game.getAi().getAiCoin()) {
				// ���� �� ������ ��ũ�ٸ�
				list.add("ai�� ������ �ڱ� ���κ��� ���� �Ϸ��� �մϴ�!, �������� ���� �� ��ȸ�� �Ѿ� ���ϴ�");
				totalAiBet -= aiBet;
				

			}
			else
				list.add(ai);

			// ************************************************************
			System.out.println("aiBet��: " + aiBet + " ��ŻaiBet��: " + totalAiBet);
			// **************************************************************
		} else // ���� Ȯ���� ��ũ�ٸ�
		{
			String message = "ai��  ��� �ϴ��� �����߽��ϴ�";
			// ���� �׾����� ���׾����� Ȯ���ϴ� ���� ->����5�� �׾��ٴ� �Ҹ�
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
				count = 0; // 0�����ʱ�ȭ
				list.add("������ �¸��߽��ϴ�!");
				usergui.im(game).removeAll();
				//panel.remove(usergui.im(game));
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();
			} else if (aiBet == 0 && userBet > 0) // ���� ó���� ���ðɰ� ai�� �ٷ��״´ٸ�
			{
				game.getUser().setCoin(game.getUser().getUserCoin() + 1);
				game.getAi().aiBetCoin(1);
				count = 0; // 0�����ʱ�ȭ
				list.add("������ �¸��߽��ϴ�!");
				usergui.im(game).removeAll();
				//panel.remove(usergui.im(game));
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();

				totalUserBet = 0;
				userBet = 0;

			} else if (aiBet > 0 || userBet > 0) // ������ �ɾ��µ� AI�� �׾��ٸ� AI�����ð�
				// ���ε�� user�� �����ε��� �������� �Ѵ�
			{
				game.getUser().setCoin(game.getUser().getUserCoin() - userBet + totalAiBet + userBet);
				game.getAi().setCoin(game.getAi().getAiCoin() - totalAiBet);
				count = 0; // 0�����ʱ�ȭ
				list.add("������ �¸��߽��ϴ�!");
				usergui.im(game).removeAll();
				//panel.remove(usergui.im(game));
				panel.add(usergui.changeView(game, 1));
				changeText(game, 1);
				panel.repaint();

				aiBet = 0;
				totalUserBet = 0;
				userBet = 0;
				totalAiBet = 0;
			}
			// else //������ �Ȱɰ� �׾��ٸ�
			// {
			// game.getUser().setCoin(game.getUser().getUserCoin()+1);
			// game.getAi().aiBetCoin(1);
			// count = 0; //0�����ʱ�ȭ
			// list.add("������ �¸��߽��ϴ�!");
			// panel.remove(usergui.im(game));
			// panel.add(usergui.changeView(game, 1));
			// changeText(game,1);
			// panel.repaint();
			// }
		}
		// AI�� ���ðɾ��ٸ�
		else {
			if (count < 2) // �߰����� 3ȸ�� �����ϱ�
			{
				// ���� ���������� ���� addActionListenerȣ�� �ȴ�
				list.add("����ڴ� �߰����� ���Ͻø� ���ô����� �Է��ϼ���!");
				count++;
				list.add(String.format("count ���� %d", count));
			} else // �߰������� 3ȸ�� ������
			{
				// list.add("�߰� ���� 3ȸ�ʰ�");
				// list.add(String.format("game.getAi().number()�� %d",
				// game.getAi().number(game)));
				if (game.getUser().number(game) > game.getAi().number(game))// ��ī�尡
					// ��ũ�ٸ�
				{
					usergui.im(game).removeAll();
					//panel.remove(usergui.im(game));
					panel.add(usergui.changeView(game, 1));
					changeText(game, 1);
					panel.repaint();
					game.getUser().setCoin(game.getUser().getUserCoin() + totalAiBet); // ����
					// �����ߴ�
					// ���ΰ�
					// ai�����ߴ����θԱ�
					game.getAi().setCoin(game.getAi().getAiCoin() - totalAiBet);
					count = 0; // 0���� �ʱ�ȭ
					list.add("������ �¸��߽��ϴ�!");

				} else // AIī�尡 �� ũ�ٸ�
				{
					usergui.im(game).removeAll();
					//panel.remove(usergui.im(game));
					panel.add(usergui.changeView(game, 1));
					changeText(game, 1);
					panel.repaint();
					game.getAi().setCoin(game.getAi().getAiCoin() + totalUserBet);
					game.getUser().setCoin(game.getUser().getUserCoin() - totalUserBet);
					count = 0; // 0���� �ʱ�ȭ
					list.add("AI�� �¸��߽��ϴ�");
				}
			}
		}
		return panel;

	}

	// ����ڰ� �����Ҷ�
	public void userDie() {
		if (totalUserBet == 0) // ���� ������ �ѹ��� �Ȱɾ��ٸ�
		{
			game.getUser().setCoin(game.getUser().getUserCoin() - 1);
			game.getAi().setCoin(game.getAi().getAiCoin() + 1);
			list.add(String.format("die �ϼ̽��ϴ� User������ %d �ٰ� ��������� �Ѿ�ϴ�", 1));
		} else // ���� ������ �ѹ��̶� �ɰ� ���̸� �����ٸ�
		{
			game.getUser().setCoin(game.getUser().getUserCoin() - totalUserBet);
			game.getAi().setCoin(game.getAi().getAiCoin() + totalUserBet);
			list.add(String.format("die �ϼ̽��ϴ� User������ %d �ٰ� ��������� �Ѿ�ϴ�", totalUserBet));
			totalUserBet = 0;
		}
	}
}