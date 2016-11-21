package space.snowwolf.sgkill.player.ai;

import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.player.Player;
import space.snowwolf.sgkill.player.TableIterator;

public class 反贼 extends ComputerPlayer {

	public 反贼(String name, int index, Identity identity) {
		super(name, index, identity);
	}

	@Override
	protected Player searchEnemy() {
		TableIterator it = table.iterator(this);
		for(int i=0;i<it.size();i++, it.forward()) {
			if(identities[it.index()] == Identity.主公) {
				return it.value();
			}
		}
		return null;
	}

}
