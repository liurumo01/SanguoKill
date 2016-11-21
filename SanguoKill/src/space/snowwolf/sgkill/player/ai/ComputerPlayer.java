package space.snowwolf.sgkill.player.ai;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import space.snowwolf.sgkill.Card;
import space.snowwolf.sgkill.CardDispatcher;
import space.snowwolf.sgkill.GameOverException;
import space.snowwolf.sgkill.Logger;
import space.snowwolf.sgkill.Main;
import space.snowwolf.sgkill.Record;
import space.snowwolf.sgkill.constant.CardName;
import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.constant.RecordType;
import space.snowwolf.sgkill.player.Player;
import space.snowwolf.sgkill.player.TableIterator;

public abstract class ComputerPlayer extends Player {

	public ComputerPlayer(String name, int index, Identity identity) {
		super(name, index, identity);
	}

	public static ComputerPlayer newInstance(String name, int index, Identity identity) {
		switch (identity) {
		case 主公:
			return new 主公(name, index, identity);
		case 忠臣:
			return new 忠臣(name, index, identity);
		case 反贼:
			return new 反贼(name, index, identity);
		case 内奸:
			return new 内奸(name, index, identity);
		default:
			return null;
		}
	}

	/**
	 * 在手牌中查找指定的牌
	 * 
	 * @param name
	 * @return
	 */
	private int searchCard(CardName name) {
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).getName() == name) {
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

	@Override
	public void 出牌(CardDispatcher it) {
		readRecords();
		super.出牌(it);
		int t = searchCard(CardName.桃);
		while (health < 4 && t >= 0) {
			Logger.出牌(this, this, cards.remove(t));
			health++;
			t = searchCard(CardName.桃);
		}
		Player e = searchEnemy();
		if (e != null) {
			t = searchCard(CardName.杀);
			if (t >= 0) {
				Card x = cards.remove(t);
				Logger.出牌(this, e, x);
				e.handle(this, x);
			}
		}
	}

	private void readRecords() {
		// 获取每一条出杀的记录并进行解析
		List<Record> records = Logger.getLatestRecords(this).stream().filter(new Predicate<Record>() {
			@Override
			public boolean test(Record t) {
				// 取所有出牌者和接受者不同的出牌记录
				return t.getType() == RecordType.出牌 && t.getSrc() != t.getDest();
			}
		}).collect(Collectors.toList());
		for (Record record : records) {
			Player src = record.getSrc();
			Player dest = record.getDest();
			// 若能取到出牌者和接牌者身份，则直接设置判定身份为其身份
			boolean a = false;
			boolean b = false;
			if (src.getIdentity(this) != Identity.未知 || identities[src.getIndex()] != Identity.未知) {
				identities[src.getIndex()] = identities[src.getIndex()] == Identity.未知 ? src.getIdentity(this) : identities[src.getIndex()];
				a = true;
			}
			if (dest.getIdentity(this) != Identity.未知 || identities[dest.getIndex()] != Identity.未知) {
				identities[dest.getIndex()] = identities[dest.getIndex()] == Identity.未知 ? dest.getIdentity(this) : identities[dest.getIndex()];
				b = true;
			}
			// 若出牌者和接牌者身份都已经确定可以跳过
			if (a && b) {
				continue;
			}
			Identity[][] t = { { Identity.反贼, Identity.反贼, Identity.未知, Identity.忠臣 },
					{ Identity.忠臣, Identity.忠臣, Identity.未知, Identity.反贼 } };
			if (a && !b) {
				int x = record.getCard().getName() == CardName.杀 ? 0 : 1;
				int y = identities[src.getIndex()] == Identity.主公 ? 0
						: (identities[src.getIndex()] == Identity.忠臣 ? 1 : (identities[src.getIndex()] == Identity.内奸 ? 2 : 3));
				if (identities[dest.getIndex()] == Identity.未知) {
					identities[dest.getIndex()] = t[x][y];
				} else if (identities[dest.getIndex()] != t[x][y]) {
					identities[dest.getIndex()] = Identity.内奸;
				}
			} else if (!a && b) {
				int x = record.getCard().getName() == CardName.杀 ? 0 : 1;
				int y = identities[dest.getIndex()] == Identity.主公 ? 0
						: (identities[dest.getIndex()] == Identity.忠臣 ? 1 : (identities[dest.getIndex()] == Identity.内奸 ? 2 : 3));
				if (identities[src.getIndex()] == Identity.未知) {
					identities[src.getIndex()] = t[x][y];
				} else if (identities[src.getIndex()] != t[x][y]) {
					identities[src.getIndex()] = Identity.内奸;
				}
			}
		}
	}

	@Override
	public void 弃牌() {
		super.弃牌();
		while (cards.size() > health) {
			int t = chooseCardToDrop();
			if (t >= 0) {
				Card x = cards.remove(t);
				Logger.弃牌(this, x);
			}
		}
	}

	/**
	 * 选择一张要弃置的牌
	 * 
	 * @return
	 */
	private int chooseCardToDrop() {
		CardName[] priority = new CardName[] { CardName.杀, CardName.闪, CardName.桃 };
		for (int i = 0; i < priority.length; i++) {
			for (int j = 0; j < cards.size(); j++) {
				if (cards.get(j).getName() == priority[i]) {
					return j;
				}
			}
		}
		return -1;
	}

	@Override
	public void handle(Player source, Card card) {
		if (card.getName() == CardName.杀) {
			// 若某人对自己出杀，则判定对方为敌人（若自己是主公或忠臣，则对方为反贼，若自己是反贼，则对方是主公或忠臣）
			if (identity == Identity.主公 || identity == Identity.忠臣) {
				identities[table.iterator(source).index()] = Identity.反贼;
			} else if (identity == Identity.反贼 && source.getIdentity(this) != Identity.主公) {
				identities[table.iterator(source).index()] = Identity.忠臣;
			}
			// 出闪
			int t = searchCard(CardName.闪);
			if (t >= 0) {
				Logger.出牌(this, this, cards.remove(t));
			} else {
				Logger.放弃(this);
				Logger.受到伤害(this, source, card, 1);
				health--;
				if (health == 0) {
					// 濒死状态，从出牌者开始依次选择是否营救
					Logger.求救(this);
					t = -1;
					TableIterator it = table.iterator(source);
					for (int i = 0; i < it.size(); i++, it.forward()) {
						do {
							if (!it.value().rescue(this)) {
								break;
							}
						} while (health <= 0);
						// 血量大于0时停止救援继续游戏
						if (health > 0) {
							break;
						}
					}
					// 若轮询后血量扔小于等于0则该玩家死亡
					if (health <= 0) {
						GameOverException ex = GameOverException.isGameOver(table, this);
						if (ex != null) {
							throw ex;
						}
					}
				}
			}
		}
	}

	@Override
	public boolean rescue(Player p) {
		int t = searchCard(CardName.桃);
		if (Identity.isEnemy(this, p) && t >= 0) {
			Logger.出牌(this, p, cards.remove(t));
			p.setHealth(p.getHealth() + 1);
			return true;
		} else {
			Logger.放弃(this);
		}
		return false;
	}

	protected List<Identity> getRemainIdentities() {
		List<Identity> list = Main.getIdentities(identities.length);
		for (int i = 0; i < identities.length; i++) {
			if (identities[i] != Identity.未知) {
				list.remove(identities[i]);
			}
		}
		list.remove(Identity.主公);
		list.remove(this.identity);
		return list;
	}
	
}
