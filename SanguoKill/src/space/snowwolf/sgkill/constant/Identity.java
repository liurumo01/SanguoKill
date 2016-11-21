package space.snowwolf.sgkill.constant;

import space.snowwolf.sgkill.player.Player;

public enum Identity {

	主公, 忠臣, 内奸, 反贼, 未知;
	
	/**
	 * 判断两个人是否是敌对关系
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static boolean isEnemy(Player p1, Player p2) {
		return isEnemy(p1.getIdentity(null), p2.getIdentity(null));
	}
	
	/**
	 * 判断两人是否是队友关系
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static boolean isFriend(Player p1, Player p2) {
		return isFriend(p1.getIdentity(null), p2.getIdentity(null));
	}

	/**
	 * 判断两个身份是否是敌对关系
	 * @param i
	 * @param j
	 * @return
	 */
	public static boolean isEnemy(Identity i, Identity j) {
		if (i == j) {
			return false;
		}
		switch (i) {
		case 主公:
		case 忠臣:
			return j == 反贼 || j == 内奸;
		case 反贼:
			return j == 主公 || j == 忠臣;
		default:
			return false;
		}
	}

	/**
	 * 判断两个身份是否是队友关系
	 * @param i
	 * @param j
	 * @return
	 */
	public static boolean isFriend(Identity i, Identity j) {
		if (i == j && i != 内奸) {
			return true;
		}
		switch (i) {
		case 主公:
		case 忠臣:
			return j == 主公 || j == 忠臣;
		default:
			return false;
		}
	}

}
