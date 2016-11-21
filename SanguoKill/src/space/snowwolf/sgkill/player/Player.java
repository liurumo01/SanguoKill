package space.snowwolf.sgkill.player;

import java.util.LinkedList;
import java.util.List;

import space.snowwolf.sgkill.Card;
import space.snowwolf.sgkill.CardDispatcher;
import space.snowwolf.sgkill.Logger;
import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.constant.State;

public abstract class Player {

	/**
	 * 昵称
	 */
	protected String name;

	/**
	 * 血量
	 */
	protected int health;

	/**
	 * 手牌
	 */
	protected List<Card> cards;

	/**
	 * 本人身份
	 */
	protected Identity identity;

	protected Identity[] identities;
	protected boolean[] death;

	protected Table table;
	
	protected int index;

	public Player(String name, int index, Identity identity) {
		this.name = name;
		health = 4;
		cards = new LinkedList<Card>();
		this.index = index;
		this.identity = identity;
	}

	/**
	 * 设置当前玩家所判定的其他对手身份
	 * 
	 * @param players
	 */
	public void init(Table table) {
		this.table = table;
		if (this.identity == Identity.主公 && table.size() > 4) {
			this.health++;
		}
		identities = new Identity[table.size()];
		death = new boolean[table.size()];
		TableIterator it = table.iterator(this);
		for (int i = 0; i < it.size(); i++, it.forward()) {
			Player player = it.value();
			if (player.identity == Identity.主公 || player == this || table.size() == 2) {
				identities[it.index()] = player.identity;
			} else {
				identities[it.index()] = Identity.未知;
			}
		}
	}

	/**
	 * 每一回合的操作
	 * 
	 * @param p
	 * @param index
	 * @param it
	 */
	public final void action(CardDispatcher it) {
		摸牌(it);
		出牌(it);
		弃牌();
	}

	/**
	 * 摸牌阶段
	 * 
	 * @param it
	 */
	public void 摸牌(CardDispatcher it) {
		Logger.状态变化(this, State.摸牌阶段);
		receiveCard(it.next());
		receiveCard(it.next());
	}

	/**
	 * 出牌阶段
	 * @param it
	 */
	public void 出牌(CardDispatcher it) {
		Logger.状态变化(this, State.出牌阶段);
	}

	/**
	 * 弃牌阶段
	 */
	public void 弃牌() {
		Logger.状态变化(this, State.弃牌阶段);
	}

	/**
	 * 对他人所出的牌进行处理
	 * 
	 * @param source
	 * @param card
	 */
	public abstract void handle(Player source, Card card);

	/**
	 * 判断是否救援指定玩家
	 * @param p
	 * @return
	 */
	public abstract boolean rescue(Player p);

	@Override
	public String toString() {
		return "[" + name + " 血量" + health + "]";
	}

	public String getName() {
		return name;
	}

	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * 获取当前玩家的身份，在以下三种情况下能正常获取
	 * 	1.系统操作
	 * 	2.自己
	 * 	3.自己已经死亡
	 * 	4.自己是主公
	 * 否则，获取到的身份为未知
	 * @param p
	 * @return
	 */
	public Identity getIdentity(Player p) {
		if(p == null || p == this || health == 0 || identity == Identity.主公) {
			return identity;			
		}
		return Identity.未知;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	/**
	 * 获取手牌数量
	 * @return
	 */
	public int getCardCount() {
		return cards.size();
	}

	public void receiveCard(Card x) {
		this.cards.add(x);
	}

	/**
	 * 设置当前玩家判定的指定玩家的身份
	 * @param index 指定玩家在列表中的位置
	 * @param id 指定玩家的身份
	 * @param death 指定玩家是否死亡
	 */
	public void setIdentity(int index, Identity id, boolean death) {
		this.identities[index] = id;
		this.death[index] = death;
	}
	
	public int getIndex() {
		return index;
	}
	
}
