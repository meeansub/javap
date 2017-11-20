package GUI;

import java.awt.Button;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.skhu.Game;

//첫 화면 frame
public class MainFrame extends JFrame {
	private UserGUI usergui = new UserGUI();
	private AiGUI aigui = new AiGUI();
	private Game game;
	private List list = new List();
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
			public void actionPerformed(ActionEvent e) {
				JPanel panel = gameView(game);
				MainViewchange(panel);
			}
		});
		return panel;
	}

	// 게임 시작 누르고 다음 화면 설정
	public JPanel gameView(Game game) {
		changeText(game,0);
		JPanel panel = new JPanel(null);
		Button batting = new Button("배팅");
		batting.setBounds(20, 300, 128, 29);
		Button die = new Button("다이");
		die.setBounds(170, 300, 128, 29);
		Button cont = new Button("계속");
		cont.setBounds(330, 300, 128, 29);
		
		panel.add(aigui.changeView(game));
		panel.add(batting);
		panel.add(die);
		panel.add(cont);
		panel.add(usergui.changeView(game, 0));
		panel.add(list);

		
		// die 버튼 클릭시
		die.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				panel.remove(usergui.im(game));
				panel.add(usergui.changeView(game, 1));
				changeText(game,1);
				panel.repaint();
				

			}
		});
		cont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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

	// 게임화면 밑에 나오는 글씨들
	public void changeText(Game game,int check) {
		list.setBounds(0, 366, 484, 85);
		if(check==1){
			String msg = "사용자의 숫자는" + game.getUser().number(game);
			list.add(msg);
		}
		else if (game.getRound() == 0) {
			int first = ((int) (Math.random() * 10)) % 2;
			list.add("INDIAN 포커에 오신걸 환영합니다.");
			list.add("1라운드를 시작하겠습니다.");
			String msg = first == 1 ? "사용자가 먼저 배팅을 시작합니다." : "Ai가 먼저 배팅을 시작합니다.";
			list.add(msg);
			game.setRound();
		} else {
			list.add(game.getRound() + "라운드를 시작하겠습니다.");

		}

	}
}
