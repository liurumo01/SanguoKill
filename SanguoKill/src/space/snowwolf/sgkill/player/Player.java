package space.snowwolf.sgkill.player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import space.snowwolf.common.utils.ArrayUtils;
import space.snowwolf.sgkill.Controller;
import space.snowwolf.sgkill.card.Card;
import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.constant.Message;

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
	 * 判定区
	 */
	protected List<Card> judgeCards;

	/**
	 * 本人身份
	 */
	protected Identity identity;
	protected Identity[] identities;

	@Autowired
	protected Controller controller;

	protected int index;

	protected boolean end;

	public Player(String name, Identity identity, Controller controller) {
		this.name = name;
		health = 2;
		cards = new LinkedList<Card>();
		this.identity = identity;
		judgeCards = new ArrayList<Card>();
		this.controller = controller;
	}

	/**
	 * 设置当前玩家所判定的其他对手身份
	 * 
	 * @param players
	 */
	public void init() {
		Player[] table = controller.getTable();
		if (this.identity == Identity.主公 && table.length > 4) {
			this.health++;
		}
		identities = new Identity[table.length];
		index = ArrayUtils.index(table, this);
	}

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
	 * 获取当前玩家的身份，在以下三种情况下能正常获取 1.系统操作 2.自己 3.自己已经死亡 4.自己是主公 否则，获取到的身份为未知
	 * 
	 * @param p
	 * @return
	 */
	public Identity getIdentity(Player p) {
		if (p == null || p == this || health == 0 || identity == Identity.主公) {
			return identity;
		}
		return Identity.未知;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	/**
	 * 获取手牌数量
	 * 
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
	 * 
	 * @param index
	 *            指定玩家在列表中的位置
	 * @param id
	 *            指定玩家的身份
	 * @param death
	 *            指定玩家是否死亡
	 */
	public void setIdentity(int index, Identity id, boolean death) {
		this.identities[index] = id;
	}

	public int getIndex() {
		return index;
	}

	public List<Card> getJudgeCards() {
		return judgeCards;
	}

	public abstract void error(Message e);

	public Method damage(int count) {
		this.health -= count;
		return null;
	}

	public boolean 出牌结束() {
		return end;
	}

	/**
	 * 主动出牌
	 * 
	 * @return
	 */
	public abstract Card selectCard();

	/**
	 * 被动出牌
	 * 
	 * @param dest
	 * @param classes
	 * @return
	 */
	public abstract Card selectCard(Player dest, Class<?> ... classes);

	/**
	 * 选择出牌对象
	 * 
	 * @return
	 */
	public abstract Player selectPlayer();

	public void removeCard(Card card) {
		cards.remove(card);
	}

}
