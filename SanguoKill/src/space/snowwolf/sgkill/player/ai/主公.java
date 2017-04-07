package space.snowwolf.sgkill.player.ai;

import space.snowwolf.sgkill.Controller;
import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.player.Player;

public class 主公 extends ComputerPlayer {

	protected 主公(String name, int index, Identity identity, Controller controller) {
		super(name, index, identity, controller);
	}

	@Override
	protected Player searchEnemy() {
		Player[] table = controller.getTable();
		for(int i=0;i<table.length;i++) {
			if(identities[table[i].getIndex()] == Identity.反贼) {
				return table[i];
			}
		}
		for(int i=0;i<table.length;i++) {
			if(identities[table[i].getIndex()] == Identity.内奸) {
				return table[i];
			}
		}
		return null;
	}
	
}
