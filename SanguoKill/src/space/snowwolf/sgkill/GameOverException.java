package space.snowwolf.sgkill;

import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.player.Player;
import space.snowwolf.sgkill.player.Table;
import space.snowwolf.sgkill.player.TableIterator;

public class GameOverException extends RuntimeException {
	
	public enum VictoryStatus {
		主公忠臣胜利,
		反贼胜利,
		内奸胜利
	}
	
	private static final long serialVersionUID = -4830080343614006946L;

	private static Player[] players;

	private VictoryStatus type;

	public GameOverException(VictoryStatus type) {
		this.type = type;
	}

	public VictoryStatus getType() {
		return type;
	}

	public static void setPlayers(Player[] players) {
		GameOverException.players = players;
	}
	
	public static int[] getRemainPlayerIdentities(TableIterator it) {
		int[] count = new int[4];
		for (int i = 0; i < it.size(); i++, it.forward()) {
			if (it.value().getIdentity(null) == Identity.主公) {
				count[0]++;
			} else if(it.value().getIdentity(null) == Identity.忠臣) {
				count[1]++;
			} else if (it.value().getIdentity(null) == Identity.反贼) {
				count[2]++;
			} else if (it.value().getIdentity(null) == Identity.内奸) {
				count[3]++;
			}
		}
		return count;
	}

	/**
	 * 根据游戏圆桌上的玩家数量和身份判断是否应该结束游戏
	 * @param table
	 * @param p
	 * @return
	 */
	public static GameOverException isGameOver(Table table, Player p) {
		Logger.死亡(p);
		int index = searchFromAllPlayers(p);
		// 向场上玩家公布死者身份
		for (int i = 0; i < players.length; i++) {
			players[i].setIdentity(index, p.getIdentity(null), true);
		}
		// 从圆桌上移除死者
		TableIterator it = table.iterator(p);
		it.forward().deleteBefore();
		// 判断场上局势，0表示主公数量，1表示忠臣数量，2表示反贼数量，3表示内奸数量
		int[] count = getRemainPlayerIdentities(it);
		//无主公且有反贼则反贼胜利
		if(count[0] == 0 && count[2] != 0) {
			return new GameOverException(VictoryStatus.反贼胜利);
		}
		//无反贼和内奸则主公和忠臣胜利
		if(count[2] == 0 && count[3] == 0) {
			return new GameOverException(VictoryStatus.主公忠臣胜利);
		}
		//只剩内奸则内奸胜利
		if(count[0] == 0 && count[1] == 0 && count[2] == 0 && count[3] != 0) {
			return new GameOverException(VictoryStatus.内奸胜利);
		}
		//否则游戏未结束，继续进行
		return null;
	}

	/**
	 * 从玩家列表中找到指定玩家的序号
	 * @param p
	 * @return
	 */
	private static int searchFromAllPlayers(Player p) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == p) {
				return i;
			}
		}
		return -1;
	}

}
