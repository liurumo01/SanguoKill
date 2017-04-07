package space.snowwolf.sgkill.player.ai;

import java.util.List;

import space.snowwolf.common.utils.ArrayUtils;
import space.snowwolf.sgkill.Controller;
import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.player.Player;

public class 内奸 extends ComputerPlayer {

	protected 内奸(String name, int index, Identity identity, Controller controller) {
		super(name, index, identity, controller);
	}

	/**
	 * 更新判定场上玩家的身份信息
	 * @return
	 */
	private boolean refreshIdentities() {
		List<Identity> list = getRemainIdentities();
		int[] num = new int[2];
		int[] j = new int[2];
		for(Identity id : list) {
			if(id == Identity.忠臣) {
				num[0]++;
			} else if(id == Identity.反贼) {
				num[1]++;
			}
		}
		if(num[0] == 0 || num[1] == 0) {
			for(int i=0;i<identities.length;i++) {
				if(identities[i] == Identity.未知) {
					identities[i] = num[0] == 0 ? Identity.反贼 : Identity.忠臣;
				}
			}
			for(Identity id : identities) {
				if(id == Identity.忠臣) {
					j[0]++;
				} else if(id == Identity.反贼) {
					j[1]++;
				}
			}
			return j[0] + 1 != j[1];
		}
		return false;
	}

	@Override
	protected Player searchEnemy() {
		boolean flag = refreshIdentities();
		Player[] table = controller.getTable();
		// 场上只剩两人说明只剩职工和内奸，此时主公为敌人
		if (table.length == 2) {
			return table[table.length - 1 - ArrayUtils.index(table, this)];
		}
		if(flag) {
			//否则计算场中主、反两方各自的手牌数、血量数之和，将数量大的一方的一位玩家选为对手
			int[] num = new int[2];
			Player p1 = null;
			Player p2 = null;
			for (int i = 0; i < table.length; i++) {
				if(identities[table[i].getIndex()] == Identity.反贼) {
					p2 = table[i];
					num[1] += table[i].getCardCount() + table[i].getHealth();
				} else if(identities[table[i].getIndex()] == Identity.主公 || identities[table[i].getIndex()] == Identity.忠臣) {
					num[0] += table[i].getCardCount() + table[i].getHealth();
					if(identities[table[i].getIndex()] == Identity.忠臣) {
						p1 = table[i];						
					}
				}
			}
			return  num[0] >= num[1] ? p1 : p2;
		}
		return null;
	}

}
