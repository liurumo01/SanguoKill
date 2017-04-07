package space.snowwolf.sgkill.card;

public abstract class Card {

//	public abstract void action(Player src, Player dest);
//	
//	public void action(Player src, List<Player> dest) {
//		if(dest == null || dest.size() <= 0) {
//			this.action(src, (Player) null);
//		} else {
//			for(Player p : dest) {
//				this.action(src, p);
//			}
//		}
//	}
	
	@Override
	public String toString()  {
		return this.getClass().getSimpleName();
	}
	
	public String getType() {
		return this.getClass().getSuperclass().getSimpleName();
	}
	
}
