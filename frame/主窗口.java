package space.snowwolf.sgkill.frame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import space.snowwolf.sgkill.Controller;
import space.snowwolf.sgkill.Logger;
import space.snowwolf.sgkill.player.HumanPlayer;
import space.snowwolf.sgkill.player.Player;

public class 主窗口 extends JFrame {

	private static final long serialVersionUID = 8162728680221311759L;

	private JButton 单人模式 = new JButton("单人模式");
	private JButton 说明图鉴 = new JButton("说明图鉴");
	private JButton 退出游戏 = new JButton("退出游戏");

	public 主窗口() {
		super("三国杀");
		setSize(800, 600);
		setLocationRelativeTo(null);
		initEvent();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				主窗口 frame = (主窗口) e.getSource();
				Dimension size = frame.getSize();
				frame.initLayout(size.getWidth(), size.getHeight());
			}
		});
	}

	private void initLayout(double width, double height) {
		setLayout(null);

		单人模式.setSize((int) (width * 0.25), (int) (height * 0.1));
		说明图鉴.setSize((int) (width * 0.25), (int) (height * 0.1));
		退出游戏.setSize((int) (width * 0.25), (int) (height * 0.1));

		单人模式.setLocation((int) (width * 3.0 / 8), (int) (height * 4.0 / 20));
		说明图鉴.setLocation((int) (width * 3.0 / 8), (int) (height * 9.0 / 20));
		退出游戏.setLocation((int) (width * 3.0 / 8), (int) (height * 14.0 / 20));

		add(单人模式);
		add(说明图鉴);
		add(退出游戏);
	}

	private void initEvent() {
		主窗口 frame = this;

		单人模式.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				while (true) {
					String input = JOptionPane.showInputDialog("请输入玩家数目(2-8)");
					int num;
					try {
						num = Integer.parseInt(input);
						if (num < 2 || num > 8) {
							throw new RuntimeException();
						}
						Player[] players = new Player[num];
						Logger logger = new Logger();
						Controller controller = new Controller();
//						controller.init(players, 1);
						HumanPlayer p = null;
						for (int i = 0; i < players.length; i++) {
							if (players[i] instanceof HumanPlayer) {
								p = (HumanPlayer) players[i];
								break;
							}
						}
						游戏窗口 gameFrame = 游戏窗口.newInstatce(num, players, p);
						logger.addObserver(gameFrame);
						gameFrame.setVisible(true);
						new Thread(new Runnable() {
							@Override
							public void run() {
								controller.startGame();
							}
						}, "Controller").start();
						break;
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, "输入有误，请重新输入");
					}
				}
			}
		});

		退出游戏.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
	}

}
