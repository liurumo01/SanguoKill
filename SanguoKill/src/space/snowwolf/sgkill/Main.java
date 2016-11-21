package space.snowwolf.sgkill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.player.Player;
import space.snowwolf.sgkill.player.Table;
import space.snowwolf.sgkill.player.TableIterator;
import space.snowwolf.sgkill.player.ai.ComputerPlayer;

public class Main {

	public static final Scanner input = new Scanner(System.in);

	/**
	 * 游戏主程序
	 * @param args
	 */
	public static void main(String[] args) {

		CardDispatcher it = new CardDispatcher();

		// 进行多局游戏
		while (true) {
			Logger.reset();
			// 选择游戏人数
			Player[] players = new Player[choosePlayersCount()];
			GameOverException.setPlayers(players);
			List<Identity> identites = getIdentities(players.length);
			Collections.shuffle(identites);
			// 创建玩家
			for (int i = 0; i < players.length; i++) {
				players[i] = ComputerPlayer.newInstance("电脑" + (i + 1), i, identites.get(i));
			}
			// 创建游戏圆桌
			Table table = new Table(players);
			TableIterator tb = (TableIterator) table.iterator();
			int index = -1;
			for (int i = 0; i < tb.size(); i++, tb.forward()) {
				Player player = tb.value();
				Identity id = identites.get(i);
				if (id == Identity.主公) {
					index = player.getIndex();
				}
				//设置每个玩家判定的其他玩家角色
				player.init(table);
				// 开场每人分四张牌
				for (int j = 0; j < 4; j++) {
					player.receiveCard(it.next());
				}
			}
			//设置主公所在位置为游戏圆桌起点
			Logger.游戏开始(players[index]);
			table.setHead(players[index]);
			while (true) {
				try {
					// 轮流行动
					TableIterator p = table.iterator();
					for (int i = 0; i < table.size(); i++, p.forward()) {
						Player k = p.value();
						if (k.getHealth() > 0) {
							k.action(it);
							Thread.sleep(0);
						}
					}
				} catch (GameOverException e) {
					List<Player> winners = checkResult(players, e);
					Logger.游戏结束(e.getType(), winners);
					break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (!playAgain()) {
				break;
			}
		}
	}
	
	/**
	 * 处理游戏结束事件，找到所有胜利者
	 * @param ex
	 */
	private static List<Player> checkResult(Player[] players, GameOverException ex) {
		List<Player> winners = new ArrayList<Player>();
		switch (ex.getType()) {
		case 主公忠臣胜利:
			for (int i = 0; i < players.length; i++) {
				if (players[i].getIdentity(null) == Identity.主公
						|| players[i].getIdentity(null) == Identity.忠臣) {
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

	/**
	 * 询问是否再来一局
	 * @return
	 */
	private static boolean playAgain() {
		System.out.println("是否再来一局？");
		System.out.println("[1]继续");
		System.out.println("[0]退出");
		boolean flag = true;
		while (true) {
			int t = input.nextInt();
			if (t == 0) {
				flag = false;
				break;
			} else if (t != 1) {
				System.out.println("输入错误，请重新选择");
			}
			break;
		}
		return flag;
	}
	
	/**
	 * 获取指定人数对应的玩家身份列表
	 * @param table
	 */
	public static List<Identity> getIdentities(int num) {
		List<Identity> list = new ArrayList<Identity>();
		switch(num) {
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

	/**
	 * 设置游戏人数
	 * @return
	 */
	private static int choosePlayersCount() {
		while (true) {
			System.out.println("请输入游戏人数（2-8）");
			System.out.println("2人：1主公 1反贼");
			System.out.println("3人：1主公 1内奸 1反贼");
			System.out.println("4人：1主公 1忠臣 1内奸 1反贼");
			System.out.println("5人：1主公 1忠臣 1内奸 2反贼");
			System.out.println("6人：1主公 1忠臣 1内奸 3反贼");
			System.out.println("7人：1主公 2忠臣 1内奸 3反贼");
			System.out.println("8人：1主公 2忠臣 1内奸 4反贼");
			int t = input.nextInt();
			if (t < 2 || t > 8) {
				System.out.println("输入错误");
				continue;
			}
			return t;
		}
	}

}
