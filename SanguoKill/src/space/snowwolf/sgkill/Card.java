package space.snowwolf.sgkill;

import space.snowwolf.sgkill.constant.CardName;
import space.snowwolf.sgkill.constant.CardType;

public class Card {

	private CardType type;
	private CardName name;

	public Card(CardType type, CardName name) {
		this.type = type;
		this.name = name;
	}

	@Override
	public String toString() {
		return name.toString();
	}

	public CardType getType() {
		return type;
	}

	public CardName getName() {
		return name;
	}
	
}
