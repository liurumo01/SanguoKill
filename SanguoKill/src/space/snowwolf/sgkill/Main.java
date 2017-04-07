package space.snowwolf.sgkill;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import space.snowwolf.common.utils.InputReader;
import space.snowwolf.sgkill.player.HumanPlayer;
import space.snowwolf.sgkill.player.Player;

public class Main {

	/**
	 * 游戏主程序
	 * 
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring.xml");
		InputReader reader = new InputReader();
		reader.init(System.in);

		int human = 1;
		Controller controller = context.getBean(Controller.class);
		controller.init(choosePlayersCount(reader), human);
		Player[] table = controller.getTable();
		for(Player p : table) {
			if(p instanceof HumanPlayer) {
				((HumanPlayer) p).setInputStream(System.in);
				((HumanPlayer) p).setOutputStream(System.out);
				Logger logger = context.getBean(Logger.class);
				logger.addPlayerOutputStream((HumanPlayer) p);
			}
		}
		controller.startGame();
		System.exit(0);
	}

	/**
	 * 询问是否再来一局
	 * 
	 * @return
	 */
//	private static boolean playAgain(InputReader reader) {
//		int result = reader.choose("是否再来一局", "继续", "退出");
//		return result == 1;
//	}

	/**
	 * 设置游戏人数
	 * 
	 * @return
	 */
	private static int choosePlayersCount(InputReader reader) {
		String[] options = { "2人：1主公 1反贼", "3人：1主公 1内奸 1反贼", "4人：1主公 1忠臣 1内奸 1反贼", "5人：1主公 1忠臣 1内奸 2反贼",
				"6人：1主公 1忠臣 1内奸 3反贼", "7人：1主公 2忠臣 1内奸 3反贼", "8人：1主公 2忠臣 1内奸 4反贼" };
		return reader.choose("请输入游戏人数（2-8）", options) + 1;
	}

}
