package space.snowwolf.sgkill.player.ai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import space.snowwolf.common.utils.ArrayUtils;
import space.snowwolf.sgkill.Controller;
import space.snowwolf.sgkill.Record;
import space.snowwolf.sgkill.card.Card;
import space.snowwolf.sgkill.card.basic.杀;
import space.snowwolf.sgkill.card.basic.桃;
import space.snowwolf.sgkill.card.basic.酒;
import space.snowwolf.sgkill.card.basic.闪;
import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.constant.Message;
import space.snowwolf.sgkill.constant.RecordType;
import space.snowwolf.sgkill.constant.State;
import space.snowwolf.sgkill.player.Player;

public abstract class ComputerPlayer extends Player {

	protected Player target;

	protected ComputerPlayer(String name, int index, Identity identity, Controller controller) {
		super(name, identity, controller);
	}

	public static ComputerPlayer newInstance(String name, int index, Identity identity, Controller controller) {
		switch (identity) {
		case 主公:
			return new 主公(name, index, identity, controller);
		case 忠臣:
			return new 忠臣(name, index, identity, controller);
		case 反贼:
			return new 反贼(name, index, identity, controller);
		case 内奸:
			return new 内奸(name, index, identity, controller);
		default:
			return null;
		}
	}

	@Override
	public void init() {
		super.init();
		Player[] table = controller.getTable();
		for (Player p : table) {
			if(table.length == 2) {
				//2人游戏，则可直接认定对方身份
				identities[ArrayUtils.index(table, p)] = p.getIdentity(null);
			} else if(table.length == 3 && this.identity == Identity.主公) {
				//3人游戏，自己为主公，则其余2人均为敌对方，将其余两人均标识为内奸可以使第一轮出牌有效，其后若有反贼出现会进行修改
				if(p != this) {
					identities[ArrayUtils.index(table, p)] = Identity.内奸;					
				} else {
					identities[ArrayUtils.index(table, p)] = this.identity;
				}
			} else if(table.length == 3) {
				//3人游戏，自己不为主公，则可以认定其余人身份
				identities[ArrayUtils.index(table, p)] = p.getIdentity(null);
			} else if(table.length >=4 && table.length <= 6 && this.identity == Identity.忠臣) {
				//4，5，6人游戏，若自己为忠臣，则除主公外其余人均为敌对，可同时标识为反贼
				if(p != this && p.getIdentity(this) != Identity.主公) {
					identities[ArrayUtils.index(table, p)] = Identity.反贼;
				} else {
					identities[ArrayUtils.index(table, p)] = p.getIdentity(this);
				}
			} else {
				identities[ArrayUtils.index(table, p)] = p.getIdentity(this);				
			}
		}
	}
	
	private int searchCard(Class<?> clazz) {
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).getClass() == clazz) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 在当前场上玩家中选择一个敌人
	 * 
	 * @return
	 */
	protected abstract Player searchEnemy();

	protected List<Identity> getRemainIdentities() {
		List<Identity> list = Controller.getIdentities(identities.length);
		for (int i = 0; i < identities.length; i++) {
			if (identities[i] != Identity.未知) {
				list.remove(identities[i]);
			}
		}
		list.remove(Identity.主公);
		list.remove(this.identity);
		return list;
	}

	@Override
	public Card selectCard() {
		refreshIdentities();
		if (controller.getCurrentPlayer() == this && controller.getState() == State.弃牌阶段) {
			Card x = null;
			select: while (x == null) {
				for (Card card : cards) {
					if (card instanceof 杀) {
						x = card;
						break select;
					}
				}
				for (Card card : cards) {
					if (card instanceof 闪) {
						x = card;
						break select;
					}
				}
				for (Card card : cards) {
					if (card instanceof 桃) {
						x = card;
						break select;
					}
				}
			}
			return x;
		} else {
			if(end) {
				return null;
			}
			target = null;
			if (health < 0) {
				for (Card x : cards) {
					if (x instanceof 桃) {
						target = this;
						return x;
					}
				}
			}
			Player enemy = searchEnemy();
			if (enemy != null) {
				int t = searchCard(杀.class);
				if (t == -1) {
					end = true;
					return null;
				}
				target = enemy;
				setEnd(true);
				return cards.get(t);
			}
			end = true;
			return null;
		}
	}

	@Override
	public Card selectCard(Player dest, Class<?> ... classes) {
		if (ArrayUtils.index(classes, 桃.class) >= 0 || ArrayUtils.index(classes, 酒.class) >= 0 && controller.getState() == State.求救) {
			if (Identity.isEnemy(this, dest)) {
				return null;
			}
			int t = searchCard(桃.class);
			if (t == -1) {
				return null;
			}
			return cards.get(t);
		}
		for (Card x : cards) {
			if (ArrayUtils.index(classes, x.getClass()) >= 0) {
				return x;
			}
		}
		return null;
	}

	@Override
	public Player selectPlayer() {
		end = true;
		return target;
	}

	@Override
	public void error(Message e) {

	}
	
	private void refreshIdentities() {
		Map<Identity, Identity> map = new HashMap<Identity, Identity>();
		map.put(Identity.主公, Identity.反贼);
		map.put(Identity.忠臣, Identity.反贼);
		map.put(Identity.内奸, Identity.未知);
		map.put(Identity.反贼, Identity.忠臣);
		
		List<Record> latest = controller.getLatestRecords(this);
		for(Record item : latest) {
			if(item.getType() == RecordType.出牌 && item.getCard() instanceof 杀) {
				Player src = item.getSrc();
				Player dest = item.getDest();
				Card card = item.getCard();
				if(card instanceof 杀) {
					int count = 0;
					if(src.getIdentity(this) != Identity.未知) {
						identities[src.getIndex()] = src.getIdentity(this);
						count+=1;
					}
					if(dest.getIdentity(this) != Identity.未知) {
						identities[dest.getIndex()] = dest.getIdentity(this);
						count+=2;
					}
					if(count == 0) {
						continue;
					} else if(count == 1) {
						identities[src.getIndex()] = map.get(identities[dest.getIndex()]);
					} else if(count == 2) {
						identities[dest.getIndex()] = map.get(identities[src.getIndex()]);
					} else {
						
					}
				}
			}
		}
	}

}
