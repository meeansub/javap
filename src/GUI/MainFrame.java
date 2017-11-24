package GUI;

import java.awt.Button;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.skhu.Game;

//ù ȭ�� frame
public class MainFrame extends JFrame {
	private UserGUI usergui = new UserGUI();
	private AiGUI aigui = new AiGUI();
	private Game game;
	private List list = new List();
	private JLabel label;
	private JTextField textField;
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
		changeText(game,0);
		JPanel panel = new JPanel(null);
		Button batting = new Button("����");
		batting.setBounds(20, 300, 128, 29);
		Button die = new Button("����");
		die.setBounds(170, 300, 128, 29);
		Button cont = new Button("���");
		cont.setBounds(330, 300, 128, 29);

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
		label.setBounds(new Rectangle(86,310,120,79));
		label.setText("������ ���� �Է�: ");
		panel.add(label);

		JTextField inCoin = new JTextField();
		inCoin.setColumns(3);
		inCoin.setBounds(190, 337, 50, 25);
		panel.add(inCoin);
		inCoin.setEnabled(false);

		betBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				String s =inCoin.getText();

				JOptionPane.showMessageDialog(panel, s + "���� ������ �Էµƽ��ϴ�");
				inCoin.setText("");
				String msg="User ����="+s;
				int bet=Integer.parseInt(s);
				game.getUser().betCoin(bet);
				list.add(msg);
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

				panel.remove(usergui.im(game));
				panel.add(usergui.changeView(game, 1));
				changeText(game,1);
				panel.repaint();
				game.getUser().setCoin(game.getUser().getUserCoin()-1);
				String dieMsg="die �ϼ̽��ϴ� User�� ������ �ϳ� �ٰ� ���� ����� �Ѿ�ϴ�.";
				list.add(dieMsg);



			}
		});
		cont.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				betBtn.setEnabled(false);
				inCoin.setEnabled(false);
				game.setRound();
				panel.remove(usergui.im(game));
				panel.remove(aigui.im(game));
				panel.add(aigui.changeView(game));
				panel.add(usergui.changeView(game, 0));
				changeText(game,0);
				panel.repaint();



			}
		});

		return panel;
	}

	// ����ȭ�� �ؿ� ������ �۾���
	public void changeText(Game game,int check) {
		list.setBounds(0, 366, 484, 85);
		if(check==1){
			String msg = "������� ���ڴ�" + game.getUser().number(game);
			list.add(msg);
		}
		else if (game.getRound() == 1) {
			int first = ((int) (Math.random() * 10)) % 2;
			list.add("INDIAN ��Ŀ�� ���Ű� ȯ���մϴ�.");
			list.add("1���带 �����ϰڽ��ϴ�.");
			String betFirstMsg = first == 1 ? "����ڰ� ���� ������ �����մϴ�." : "Ai�� ���� ������ �����մϴ�.";
			list.add(betFirstMsg);
			String currentCoin= String.format("User ����: %d, Ai ����: %d",game.getUser().getUserCoin(),game.getAi().getAiCoin());
			list.add(currentCoin);

		} else {
			if(game.getRound() !=21) {
				list.add("\n");
				list.add(game.getRound() + "���带 �����ϰڽ��ϴ�.");
				String s= String.format("User ����: %d, Ai ����: %d",game.getUser().getUserCoin(),game.getAi().getAiCoin());
				list.add(s);
			}

		}

	}
}