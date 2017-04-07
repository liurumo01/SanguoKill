package space.snowwolf.sgkill.frame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import space.snowwolf.sgkill.CardDispatcher;
import space.snowwolf.sgkill.Record;
import space.snowwolf.sgkill.card.Card;
import space.snowwolf.sgkill.constant.Message;
import space.snowwolf.sgkill.constant.RecordLevel;
import space.snowwolf.sgkill.player.HumanPlayer;
import space.snowwolf.sgkill.player.Player;
import space.snowwolf.sgkill.player.Table;

public abstract class 游戏窗口 extends JFrame implements Observer, IOHandler {

	private static final long serialVersionUID = 390597198363295829L;

	private static final int SIZE = 0;
	private static final int LOCATION = 1;

	protected JButton 出牌;
	protected JButton 弃牌;
	protected JTextArea 日志栏;

	protected Table table;

	protected Player[] players;
	protected CardDispatcher dispatcher = new CardDispatcher();

	protected int[][][] arg0;
	protected int[][] arg1;

	protected HumanPlayer player;

	protected boolean flag = false;
	protected Card selectedCard = null;

	protected 游戏窗口(Player[] players, HumanPlayer player) {
		super("三国杀");
		this.players = players;
		this.player = player;
		setSize(800, 600);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				游戏窗口 frame = (游戏窗口) e.getSource();
				Dimension size = frame.getSize();
				frame.initLayout(size.getWidth(), size.getHeight());
			}
		});
		setLocationRelativeTo(null);
		initComponents();
		initLayout(800, 600);
		initEvent();
		for (int i = 0; i < players.length; i++) {
			players[i].display();
		}
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public static 游戏窗口 newInstatce(int num, Player[] players, HumanPlayer player) {
		switch (num) {
		case 2:
			游戏窗口 frame = new 二人游戏窗口(players, player);
			player.setFrame(frame);
			return frame;
		}
		return null;
	}

	protected abstract void initSizeArgs(double width, double height);

	protected void initEvent() {
		for (int i = 0; i < players.length; i++) {
			players[i].武将栏().setEditable(false);
			players[i].身份栏().setEditable(false);
			players[i].血量栏().setEditable(false);
			// players[i].武将栏().setHorizontalAlignment(JTextField.CENTER);
			// players[i].身份栏().setHorizontalAlignment(JTextField.CENTER);
			// players[i].血量栏().setHorizontalAlignment(JTextField.CENTER);
		}

		出牌.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// JButton button = (JButton) e.getSource();
				// 游戏窗口 frame = (游戏窗口) button.getRootPane().getParent();
				selectedCard = player.手牌栏().getSelectedValue();
				// if(x instanceof 杀) {
				// Player dest = frame.selectDestination();
				// if(dest != null) {
				// Logger.出牌(player, dest, x);
				// dest.handle(player, x);
				// }
				// } else if(x instanceof 闪) {
				// JOptionPane.showMessageDialog(null, "不能主动出闪");
				// } else if(x instanceof 桃) {
				// if(player.getHealth() >= 4) {
				// JOptionPane.showMessageDialog(null, "未损失血量不能出桃");
				// } else {
				// player.setHealth(player.getHealth() + 1);
				// Logger.出牌(player, player, x);
				// }
				// }
				flag = true;
			}
		});
	}

	// private Player selectDestination() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	protected void initComponents() {
		for (int i = 0; i < players.length; i++) {
			players[i].武将栏(new JTextField());
			players[i].身份栏(new JTextField());
			players[i].血量栏(new JTextField());
			players[i].判定区(new JList<Card>(new DefaultListModel<Card>()));
			players[i].装备栏(new JList<Card>(new DefaultListModel<Card>()));
			players[i].手牌栏(new JList<Card>(new DefaultListModel<Card>()));
		}
		日志栏 = new JTextArea();
		出牌 = new JButton("出牌");
		弃牌 = new JButton("弃牌");
		出牌.setEnabled(false);
		弃牌.setEnabled(false);
	}

	protected void initLayout(double width, double height) {
		setLayout(null);
		initSizeArgs(width, height);
		for (int i = 0; i < players.length; i++) {
			players[i].武将栏().setSize(arg0[i][SIZE][0], arg0[i][SIZE][1]);
			players[i].身份栏().setSize(arg0[i][SIZE][2], arg0[i][SIZE][3]);
			players[i].血量栏().setSize(arg0[i][SIZE][4], arg0[i][SIZE][5]);

			players[i].判定区().setSize(arg0[i][SIZE][6], arg0[i][SIZE][7]);
			players[i].装备栏().setSize(arg0[i][SIZE][8], arg0[i][SIZE][9]);
			players[i].手牌栏().setSize(arg0[i][SIZE][10], arg0[i][SIZE][11]);

			players[i].武将栏().setLocation(arg0[i][LOCATION][0], arg0[i][LOCATION][1]);
			players[i].身份栏().setLocation(arg0[i][LOCATION][2], arg0[i][LOCATION][3]);
			players[i].血量栏().setLocation(arg0[i][LOCATION][4], arg0[i][LOCATION][5]);

			players[i].判定区().setLocation(arg0[i][LOCATION][6], arg0[i][LOCATION][7]);
			players[i].装备栏().setLocation(arg0[i][LOCATION][8], arg0[i][LOCATION][9]);
			players[i].手牌栏().setLocation(arg0[i][LOCATION][10], arg0[i][LOCATION][11]);

			add(players[i].武将栏());
			add(players[i].身份栏());
			add(players[i].血量栏());

			add(players[i].判定区());
			add(players[i].装备栏());
			add(players[i].手牌栏());
		}

		日志栏.setSize((int) (width * 15 / 80), (int) (height * 13 / 15));
		出牌.setSize((int) (width * 6 / 80), (int) (height * 1 / 24));
		弃牌.setSize((int) (width * 6 / 80), (int) (height * 1 / 24));

		日志栏.setLocation((int) (width * 65 / 80), (int) (height * 1 / 15));
		出牌.setLocation((int) (width * 49 / 80), (int) (height * 64 / 75));
		弃牌.setLocation((int) (width * 58 / 80), (int) (height * 64 / 75));

		add(日志栏);
		add(出牌);
		add(弃牌);
	}

	@Override
	@SuppressWarnings("unused")
	public void update(Observable o, Object arg) {
		Record record = (Record) arg;
		Player src = record.getSrc();
		Player dest = record.getDest();
		Card card = record.getCard();
		DefaultListModel<Card> model = (DefaultListModel<Card>) src.手牌栏().getModel();

		switch (record.getType()) {
		case 出牌:
			model.removeElement(card);
			break;
		case 受到伤害:
			break;
		case 弃牌:
			break;
		case 摸牌:
			model.addElement(card);
			src.手牌栏().updateUI();
			break;
		case 放弃:
			break;
		case 死亡:
			break;
		case 求救:
			break;
		case 游戏开始:
			break;
		case 游戏结束:
			break;
		case 状态变化:
			break;
		default:
			break;
		}
		if (record.getLevel() == RecordLevel.INFO) {
			日志栏.setText(日志栏.getText() + record.toString() + "\n");
		}
	}

	public boolean nextStep() {
		return flag;
	}

	public void enableSelect() {
		出牌.setEnabled(true);
		弃牌.setEnabled(true);
	}

	public void disableSelect() {
		出牌.setEnabled(false);
		弃牌.setEnabled(false);
	}

	public Card selectCard(Class<?> clazz) {
		flag = false;
		while (true) {
			try {
				Thread.sleep(500);
				if (flag) {
					if(selectedCard == null) {
						JOptionPane.showMessageDialog(null, Message.请选择卡牌);
						continue;
					}
					if (selectedCard.getClass() == clazz) {
						return selectedCard;
					} else {
						JOptionPane.showMessageDialog(null, Message.卡牌类型错误);
					}
				}
			} catch (InterruptedException e) {

			}
		}
	}
	
	public Card selectCard() {
		flag = false;
		while(true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				
			}
			if(flag) {
				return selectedCard;
			}
		}
	}

}
