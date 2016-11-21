package space.snowwolf.sgkill.player.ai;

import java.util.List;

import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.player.Player;
import space.snowwolf.sgkill.player.TableIterator;

public class 内奸 extends ComputerPlayer {

	public 内奸(String name, int index, Identity identity) {
		super(name, index, identity);
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
		TableIterator it = table.iterator(this);
		// 场上只剩两人说明只剩职工和内奸，此时主公为敌人
		if (it.size() == 2) {
			return it.forward().value();
		}
		if(flag) {
			//否则计算场中主、反两方各自的手牌数、血量数之和，将数量大的一方的一位玩家选为对手
			int[] num = new int[2];
			Player p1 = null;
			Player p2 = null;
			for (int i = 0; i < it.size(); i++, it.forward()) {
				if(identities[it.index()] == Identity.反贼) {
					p2 = it.value();
					num[1] += it.value().getCardCount() + it.value().getHealth();
				} else if(identities[it.index()] == Identity.主公 || identities[it.index()] == Identity.忠臣) {
					num[0] += it.value().getCardCount() + it.value().getHealth();
					if(identities[it.index()] == Identity.忠臣) {
						p1 = it.value();						
					}
				}
			}
			return  num[0] >= num[1] ? p1 : p2;
		}
		return null;
	}

}
