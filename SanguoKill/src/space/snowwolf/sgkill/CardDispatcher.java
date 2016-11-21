package space.snowwolf.sgkill;

import java.util.Iterator;
import java.util.Random;

import space.snowwolf.sgkill.constant.CardName;
import space.snowwolf.sgkill.constant.CardType;

public class CardDispatcher implements Iterator<Card> {

	private Random random = new Random();

	private Card 杀 = new Card(CardType.基本牌, CardName.杀);
	private Card 闪 = new Card(CardType.基本牌, CardName.闪);
	private Card 桃 = new Card(CardType.基本牌, CardName.桃);
	
	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public Card next() {
		int x = random.nextInt(100);
		if (x < 60) {
			return 杀;
		} else if (x < 80) {
			return 闪;
		} else {
			return 桃;
		}
	}

}
