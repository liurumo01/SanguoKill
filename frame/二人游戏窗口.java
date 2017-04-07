package space.snowwolf.sgkill.frame;

import space.snowwolf.sgkill.player.HumanPlayer;
import space.snowwolf.sgkill.player.Player;

public class 二人游戏窗口 extends 游戏窗口 {

	private static final long serialVersionUID = -5882975375526590882L;

	protected 二人游戏窗口(Player[] players, HumanPlayer player) {
		super(players, player);
	}

	protected void initSizeArgs(double width, double height) {
		arg0 = new int[][][] {
			{
				{
					(int) (width * 15 / 80), (int) (height * 1 / 24),
					(int) (width * 15 / 80), (int) (height * 1 / 24),
					(int) (width * 15 / 80), (int) (height * 1 / 24),
					(int) (width * 15 / 80), (int) (height * 5 / 15),
					(int) (width * 15 / 80), (int) (height * 5 / 15),
					(int) (width * 15 / 80), (int) (height * 5 / 15),
				}, {
					(int) (width * 49 / 80), (int) (height * 46 / 75),
					(int) (width * 49 / 80), (int) (height * 52 / 75),
					(int) (width * 49 / 80), (int) (height * 58 / 75),
					(int) (width * 1 / 80), (int) (height * 9 / 15),
					(int) (width * 17 / 80), (int) (height * 9 / 15),
					(int) (width * 33 / 80), (int) (height * 9 / 15)
				}
			}, {
				{
					(int) (width * 15 / 80), (int) (height * 1 / 15),
					(int) (width * 15 / 80), (int) (height * 1 / 15),
					(int) (width * 15 / 80), (int) (height * 1 / 15),
					(int) (width * 15 / 80), (int) (height * 5 / 15),
					(int) (width * 15 / 80), (int) (height * 5 / 15),
					(int) (width * 15 / 80), (int) (height * 5 / 15)
				}, {
					(int) (width * 49 / 80), (int) (height * 3 / 30),
					(int) (width * 49 / 80), (int) (height * 6 / 30),
					(int) (width * 49 / 80), (int) (height * 9 / 30),
					(int) (width * 1 / 80), (int) (height * 1 / 15),
					(int) (width * 17 / 80), (int) (height * 1 / 15),
					(int) (width * 33 / 80), (int) (height * 1 / 15)
				}
			}
		};
	}
}
