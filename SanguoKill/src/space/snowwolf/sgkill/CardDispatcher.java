package space.snowwolf.sgkill;

import java.util.Iterator;
import java.util.Random;

import org.springframework.stereotype.Component;

import space.snowwolf.sgkill.card.Card;
import space.snowwolf.sgkill.card.basic.杀;
import space.snowwolf.sgkill.card.basic.桃;
import space.snowwolf.sgkill.card.basic.酒;
import space.snowwolf.sgkill.card.basic.闪;

@Component
public class CardDispatcher implements Iterator<Card> {

	private Random random = new Random();

	private Card 杀 = new 杀();
	private Card 闪 = new 闪();
	private Card 桃 = new 桃();
	private Card 酒 = new 酒();
	
	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public Card next() {
		int x = random.nextInt(100);
		if (x < 70) {
			return 杀;
		} else if (x < 70) {
			return 闪;
		} else if(x < 70) {
			return 桃;
		} else {
			return 酒;
		}
	}

}
