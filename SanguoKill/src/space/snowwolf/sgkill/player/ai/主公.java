package space.snowwolf.sgkill.player.ai;

import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.player.Player;
import space.snowwolf.sgkill.player.TableIterator;

public class 主公 extends ComputerPlayer {
	
	public 主公(String name, int index, Identity identity) {
		super(name, index, identity);
	}

	@Override
	protected Player searchEnemy() {
		TableIterator it = table.iterator(this);
		for(int i=0;i<it.size();i++, it.forward()) {
			if(identities[it.index()] == Identity.反贼) {
				return it.value();
			}
		}
		for(int i=0;i<it.size();i++, it.forward()) {
			if(identities[it.index()] == Identity.内奸) {
				return it.value();
			}
		}
		return null;
	}
	
}
