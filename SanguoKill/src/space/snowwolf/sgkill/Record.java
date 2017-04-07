package space.snowwolf.sgkill;

import java.util.Arrays;
import java.util.List;

import space.snowwolf.sgkill.GameOverException.VictoryStatus;
import space.snowwolf.sgkill.card.Card;
import space.snowwolf.sgkill.card.basic.杀;
import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.constant.RecordLevel;
import space.snowwolf.sgkill.constant.RecordType;
import space.snowwolf.sgkill.constant.State;
import space.snowwolf.sgkill.player.Player;

public class Record {

	RecordType type;
	Player src;
	Player dest;
	Card card;
	State state;
	int num;
	VictoryStatus victory;
	List<Player> winners;
	RecordLevel level;
	
	/**
	 * 游戏结束
	 * @param type
	 * @param victory
	 * @param winners
	 */
	public Record(RecordType type, VictoryStatus victory, List<Player> winners) {
		this.type = type;
		this.victory = victory;
		this.winners = winners;
	}
	
	/**
	 * 出牌
	 * @param type
	 * @param player
	 * @param src
	 * @param card
	 */
	public Record(RecordType type, Player dest, Player src, Card card) {
		this.type = type;
		this.dest = dest;
		this.src = src;
		this.card = card;
	}
	
	/**
	 * 受到伤害
	 * @param type
	 * @param player
	 * @param num
	 */
	public Record(RecordType type, Player player, Player src, Card card, int num) {
		this.type = type;
		this.src = src;
		this.num = num;
		this.card = card;
		this.dest = player;
	}
	
	/**
	 * 游戏开始，用于记录日志
	 * @param type
	 * @param 主公
	 * @param players
	 */
	public Record(RecordType type, Player 主公, Player[] players) {
		this.type = type;
		this.src = 主公;
		this.winners = Arrays.asList(players);
	}
	
	/**
	 * 游戏开始，用于向一人宣告另一人身份
	 * @param type
	 * @param 主公
	 * @param player
	 */
	public Record(RecordType type, Player src, Player dest) {
		this.type = type;
		this.src = src;
		this.dest = dest;
	}
	
	
	/**
	 * 状态改变
	 * @param type
	 * @param p
	 * @param state
	 */
	public Record(RecordType type, Player p, State state) {
		this.type = type;
		this.src = p;
		this.state = state;
	}

	/**
	 * 摸牌、弃牌
	 * @param player
	 * @param card
	 */
	public Record(RecordType type, Player player, Card card) {
		this.type = type;
		this.src = player;
		this.card = card;
	}
	
	/**
	 * 求救
	 * @param type
	 * @param player
	 */
	public Record(RecordType type, Player player) {
		this.type = type;
		this.src = player;
	}

	/**
	 * 将记录对象转换成字符串用于打印日志
	 */
	@Override
	public String toString() {
		StringBuilder b = null;
		switch(type) {
		case 状态变化:
			return src.toString() + state.toString();
		case 摸牌:
			return src.toString() + "摸到" + card.toString();
		case 出牌:
			return new StringBuilder(src.toString()).append("对").append(dest).append("出").append(card).toString();
		case 弃牌:
			return src + "丢弃" + card;
		case 受到伤害:
			if(card instanceof 杀) {
				return new StringBuilder(dest.toString()).append("受到").append(num).append("点伤害").toString();
			}
		case 求救:
			return src + "濒死，请求出桃";
		case 放弃:
			return src + "放弃";
		case 死亡:
			return src + "死亡，身份是" + src.getIdentity(null);
		case 游戏开始:
			b = new StringBuilder("游戏开始，").append(src.getName() + "身份是主公").append("\n");
			if(winners != null) {
				for(Player p : winners) {
					if(p.getIdentity(null) != Identity.主公) {
						b.append(p.getName() + "身份是" + p.getIdentity(p)).append("\n");
					}
				}
			} else {
				b.append(dest.getName() + "身份是" + dest.getIdentity(dest)).append("\n");
			}
			return "游戏开始 " + src.getName() + "身份是" + src.getIdentity(null);
		case 游戏结束:
			b = new StringBuilder("游戏结束 ");
			switch (victory) {
			case 主公忠臣胜利:
				b.append("主公、忠臣获胜：");
				break;
			case 反贼胜利:
				b.append("反贼获胜：");
				break;
			case 内奸胜利:
				b.append("内奸获胜：");
				break;
			}
			for (int i = 0; i < winners.size(); i++) {
				b.append(winners.get(i).getName()).append(",");
			}
			b.deleteCharAt(b.length() - 1);
			return b.toString();
		default:
			break;
		}
		return null;
	}
	
	public RecordLevel getLevel() {
		return level;
	}

	public RecordType getType() {
		return type;
	}

	public void setType(RecordType type) {
		this.type = type;
	}

	public Player getSrc() {
		return src;
	}

	public void setSrc(Player src) {
		this.src = src;
	}

	public Player getDest() {
		return dest;
	}

	public void setDest(Player dest) {
		this.dest = dest;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public VictoryStatus getVictory() {
		return victory;
	}

	public void setVictory(VictoryStatus victory) {
		this.victory = victory;
	}
	
}
