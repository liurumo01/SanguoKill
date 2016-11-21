package space.snowwolf.sgkill.player;

import space.snowwolf.sgkill.Card;
import space.snowwolf.sgkill.CardDispatcher;
import space.snowwolf.sgkill.Main;
import space.snowwolf.sgkill.constant.CardName;
import space.snowwolf.sgkill.constant.Identity;

@Deprecated
public class HumanPlayer extends Player {

	public HumanPlayer(String 昵称, int index, Identity identity) {
		super(昵称, index, identity);
	}
	
	@Override
	public void 出牌(CardDispatcher it) {
		super.出牌(it);
		boolean kill = false;
		while (true) {
			showCards("弃牌");
			int t = Main.input.nextInt();
			if (t < 0 || t > cards.size()) {
				System.out.println("选择有误，请重新选择");
			} else if (t == cards.size()) {
				break;
			} else {
				if (cards.get(t).getName() == CardName.杀) {
					if (kill) {
						System.out.println("只能出一次杀");
					} else {
//						Card x = cards.remove(t);
						kill = true;
						System.out.println(this + "出杀");
//						p[1 - index].handle(this, x);
					}
				} else if (cards.get(t).getName() == CardName.闪) {
					System.out.println("不能主动出闪");
				} else if (cards.get(t).getName() == CardName.桃) {
					if (health >= 0 && health < 4) {
						cards.remove(t);
						System.out.println(this + "出桃");
						health++;
					} else {
						System.out.println("未损失血量不能出桃");
					}
				}
			}
		}
	}
	
	@Override
	public void 弃牌() {
		super.弃牌();
		while (cards.size() > health) {
			showCards();
			int t = Main.input.nextInt();
			if (t < 0 || t >= cards.size()) {
				System.out.println("选择有误，请重新选择");
			} else {
				Card x = cards.remove(t);
				System.out.println(this + "丢弃" + x);
			}
		}
	}
	
	@Override
	public void handle(Player source, Card card) {
		while (true) {
			showCards("放弃");
			int t = Main.input.nextInt();
			if (t < 0 || t > cards.size()) {
				System.out.println("选择有误，请重新选择");
			} else if (t == cards.size()) {
				System.out.println(this + "放弃，血量减1");
				health--;
				if(health == 0) {
					System.out.println(this + "濒死，请求出桃");
					while(true) {
						showCards("放弃");
						t = Main.input.nextInt();
						if(t == cards.size()) {
							System.out.println(this + "放弃");
							break;
						}
						if(cards.get(t).getName() != CardName.桃) {
							System.out.println("只能出桃");
						} else {
							health++;
							break;
						}
					}
//					if(health == 0) {
//						throw new GameOverException();						
//					}
				}
				break;
			} else {
				if (cards.get(t).getName() == CardName.闪) {
					System.out.println(this + "出闪");
					cards.remove(t);
					break;
				} else {
					System.out.println("只能出闪");
				}
			}
		}
	}
	
	private void showCards(String... others) {
		int i;
		System.out.println("请选择：");
		for (i = 0; i < cards.size(); i++) {
			System.out.println("[" + i + "]" + cards.get(i));
		}
		for (String choice : others) {
			System.out.println("[" + i++ + "]" + choice);
		}
	}

	@Override
	public boolean rescue(Player p) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
