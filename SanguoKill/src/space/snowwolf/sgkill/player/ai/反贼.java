package space.snowwolf.sgkill.player.ai;

import space.snowwolf.sgkill.Controller;
import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.player.Player;

public class 反贼 extends ComputerPlayer {

	protected 反贼(String name, int index, Identity identity, Controller controller) {
		super(name, index, identity, controller);
	}

	@Override
	protected Player searchEnemy() {
		Player[] table = controller.getTable();
		for(int i=0;i<table.length;i++) {
			if(identities[table[i].getIndex()] == Identity.主公) {
				return table[i];
			}
		}
		return null;
	}

}
