package space.snowwolf.sgkill.card.basic;

import space.snowwolf.sgkill.card.基本牌;

public class 杀 extends 基本牌 {

//	@Override
//	public void action(Player src, Player dest) {
//		Logger.出牌(src, dest, this);
//		Card card = dest.出牌(闪.class);
//		if(!(card instanceof 闪)) {
//			dest.error(ErrorType.卡牌类型错误);
//		}
//		if(card == null) {
//			Logger.放弃(dest);
//			Method method = dest.damage(1);
//			if(method != null) {
//				try {
//					method.invoke(src, dest);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		} else {
//			Logger.出牌(dest, dest, card);
//		}
//	}
	
}
