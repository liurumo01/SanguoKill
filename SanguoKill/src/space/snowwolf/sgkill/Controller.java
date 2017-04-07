package space.snowwolf.sgkill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import space.snowwolf.common.utils.ArrayUtils;
import space.snowwolf.sgkill.GameOverException.VictoryStatus;
import space.snowwolf.sgkill.card.Card;
import space.snowwolf.sgkill.card.basic.杀;
import space.snowwolf.sgkill.card.basic.桃;
import space.snowwolf.sgkill.card.basic.酒;
import space.snowwolf.sgkill.card.basic.闪;
import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.constant.Message;
import space.snowwolf.sgkill.constant.State;
import space.snowwolf.sgkill.player.HumanPlayer;
import space.snowwolf.sgkill.player.Player;
import space.snowwolf.sgkill.player.ai.ComputerPlayer;

@Component
public class Controller {

	private Player[] players;
	private Player[] table;

	@Autowired
	private Logger logger;

	@Autowired
	private CardDispatcher dispatcher;

	private Player currentPlayer;
	private State state;

	/**
	 * 初始化游戏控制器
	 * 
	 * @param players
	 *            空数组，数组长度为参加游戏总人数
	 * @param human
	 *            人类玩家数量
	 */
	public void init(int total, int human) {
		logger.init();
		// 创建玩家，分配身份
		this.players = new Player[total];
		List<Identity> identites = getIdentities(players.length);
		Collections.shuffle(identites);
		int a = 1, b = 1;
		for (int i = 0; i < players.length; i++) {
			if (i < human) {
				players[i] = new HumanPlayer("玩家" + a++, i, identites.get(i), this);
			} else {
				players[i] = ComputerPlayer.newInstance("电脑" + b++, i, identites.get(i), this);
			}
		}
		// 将主公移动到第一位
		List<Player> list = new ArrayList<>(Arrays.asList(players));
		while (list.get(0).getIdentity(null) != Identity.主公) {
			Player p = list.remove(0);
			list.add(p);
		}
		players = list.toArray(players);
		table = players;
		for (int i = 0; i < players.length; i++) {
			players[i].init();
		}
	}

	public void startGame() {
		logger.游戏开始(players, players[0]);
		for (int i = 0; i < players.length; i++) {
			for (int j = 0; j < 4; j++) {
				Card x = dispatcher.next();
				players[i].receiveCard(x);
				logger.摸牌(players[i], x);
			}
		}
		game: while (true) {
			for (Player p : table) {
				if (p.getHealth() == 0) {
					continue;
				}
				try {
					action(p);
				} catch (GameOverException e) {
					List<Player> winners = checkResult(players, e);
					System.out.println(e.getType() + ":" + winners);
					break game;
				}
			}
		}
	}

	/**
	 * 处理游戏结束事件，找到所有胜利者
	 * 
	 * @param ex
	 */
	private static List<Player> checkResult(Player[] players, GameOverException ex) {
		List<Player> winners = new ArrayList<Player>();
		switch (ex.getType()) {
		case 主公忠臣胜利:
			for (int i = 0; i < players.length; i++) {
				if (players[i].getIdentity(null) == Identity.主公 || players[i].getIdentity(null) == Identity.忠臣) {
					winners.add(players[i]);
				}
			}
			break;
		case 反贼胜利:
			for (int i = 0; i < players.length; i++) {
				if (players[i].getIdentity(null) == Identity.反贼) {
					winners.add(players[i]);
				}
			}
			break;
		case 内奸胜利:
			for (int i = 0; i < players.length; i++) {
				if (players[i].getIdentity(null) == Identity.内奸) {
					winners.add(players[i]);
				}
			}
			break;
		}
		return winners;
	}

	private void action(Player p) {
		判定阶段(p);
		摸牌阶段(p);
		出牌阶段(p);
		弃牌阶段(p);
	}

	private void 判定阶段(Player p) {
		currentPlayer = p;
		state = State.判定阶段;
		logger.状态变化(p, State.判定阶段);
		// List<Card> judgeCards = p.getJudgeCards();
		// for (Card card : judgeCards) {
		// card.action(p, Arrays.asList(players));
		// }
	}

	private void 摸牌阶段(Player p) {
		currentPlayer = p;
		state = State.摸牌阶段;
		logger.状态变化(p, State.摸牌阶段);
		for (int i = 0; i < 2; i++) {
			Card x = dispatcher.next();
			p.receiveCard(x);
			logger.摸牌(p, x);
		}
	}

	private void 出牌阶段(Player p) {
		currentPlayer = p;
		state = State.出牌阶段;
		logger.状态变化(p, State.出牌阶段);
		boolean kill = false;
		p.setEnd(false);
		while (!p.出牌结束()) {
			Card x;
			while (true) {
				x = p.selectCard();
				if (x instanceof 闪) {
					p.error(Message.不能主动出闪);
				} else if (x instanceof 桃) {
					if (p.getHealth() == (players.length > 4 && p.getIdentity(null) == Identity.主公 ? 5 : 4)) {
						p.error(Message.未损失血量不能出桃);
					} else {
						p.removeCard(x);
						handle(p, p, x);
					}
					continue;
				} else if (x instanceof 杀) {
					if (kill) {
						p.error(Message.不能重复出杀);
						continue;
					}
					Player dest = p.selectPlayer();
					logger.出牌(p, dest, x);
					if (x != null && dest != null) {
						p.removeCard(x);
						handle(p, dest, x);
						if (x instanceof 杀) {
							kill = true;
						}
					}
				}
				if (x == null) {
					break;
				}
			}
			if (x == null && p.出牌结束()) {
				return;
			}
		}
	}

	private void 弃牌阶段(Player p) {
		currentPlayer = p;
		state = State.弃牌阶段;
		logger.状态变化(p, State.弃牌阶段);
		while (p.getCardCount() > p.getHealth()) {
			Card x = p.selectCard();
			p.removeCard(x);
		}
	}

	private void handle(Player src, Player dest, Card x) {
		if (x instanceof 杀) {
			Card t = dest.selectCard(dest, 闪.class);
			if (t == null) {
				logger.放弃(dest);
				logger.受到伤害(dest, src, x, 1);
				dest.damage(1);
				if (dest.getHealth() <= 0) {
					rescue(dest, src);
				}
			} else {
				dest.removeCard(t);
				logger.出牌(dest, dest, t);
			}
		} else if (x instanceof 桃) {
			dest.setHealth(dest.getHealth() + 1);
			logger.出牌(src, dest, x);
		}
	}

	private void rescue(Player p, Player start) {
		// 记录当前状态
		Player o = currentPlayer;
		State s = state;
		// 设置为某人求救的状态
		currentPlayer = p;
		state = State.求救;
		logger.求救(p);
		// 执行求救
		int index = ArrayUtils.index(table, start);
		for (int i = 0; i < table.length; i++) {
			Player current = table[(index + i) % table.length];
			while (p.getHealth() <= 0) {
				Card x = p == current ? current.selectCard(p, 桃.class, 酒.class) : current.selectCard(p, 桃.class);
				if (x == null) {
					break;
				}
				if (x instanceof 桃 || x instanceof 酒) {
					current.removeCard(x);
					logger.出牌(current, p, x);
					p.setHealth(p.getHealth() + 1);
				}
			}
		}
		if (p.getHealth() <= 0) {
			judge(p);
		}
		// 求救结束，状态复原
		currentPlayer = o;
		state = s;
	}

	private void judge(Player p) {
		logger.死亡(p);
		int index = p.getIndex();
		// 向场上玩家公布死者身份
		for (int i = 0; i < players.length; i++) {
			players[i].setIdentity(index, p.getIdentity(null), true);
		}
		// 从圆桌上移除死者
		updateTable();
		// 判断场上局势，0表示主公数量，1表示忠臣数量，2表示反贼数量，3表示内奸数量
		int[] count = getRemainPlayerIdentities();
		// 无主公且有反贼则反贼胜利
		if (count[0] == 0 && count[2] != 0) {
			throw new GameOverException(VictoryStatus.反贼胜利);
		}
		// 无反贼和内奸则主公和忠臣胜利
		if (count[2] == 0 && count[3] == 0) {
			throw new GameOverException(VictoryStatus.主公忠臣胜利);
		}
		// 只剩内奸则内奸胜利
		if (count[0] == 0 && count[1] == 0 && count[2] == 0 && count[3] != 0) {
			throw new GameOverException(VictoryStatus.内奸胜利);
		}
		// 否则游戏未结束，继续进行
	}

	public int[] getRemainPlayerIdentities() {
		int[] count = new int[4];
		for (int i = 0; i < table.length; i++) {
			if (table[i].getIdentity(null) == Identity.主公) {
				count[0]++;
			} else if (table[i].getIdentity(null) == Identity.忠臣) {
				count[1]++;
			} else if (table[i].getIdentity(null) == Identity.反贼) {
				count[2]++;
			} else if (table[i].getIdentity(null) == Identity.内奸) {
				count[3]++;
			}
		}
		return count;
	}

	/**
	 * 获取指定人数对应的玩家身份列表
	 * 
	 * @param table
	 */
	public static List<Identity> getIdentities(int num) {
		List<Identity> list = new ArrayList<Identity>();
		switch (num) {
		case 8:
			list.add(Identity.反贼);
		case 7:
			list.add(Identity.忠臣);
		case 6:
			list.add(Identity.反贼);
		case 5:
			list.add(Identity.反贼);
		case 4:
			list.add(Identity.忠臣);
		case 3:
			list.add(Identity.内奸);
		case 2:
			list.add(Identity.反贼);
			list.add(Identity.主公);
		}
		return list;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public State getState() {
		return state;
	}

	public Player[] getTable() {
		return table;
	}

	public List<Record> getLatestRecords(Player player) {
		return logger.getLatestRecords(player);
	}

	public void updateTable() {
		List<Player> list = new ArrayList<Player>();
		for (int i = 0; i < table.length; i++) {
			if (table[i].getHealth() != 0) {
				list.add(table[i]);
			}
		}
		table = list.toArray(new Player[0]);
	}

}
