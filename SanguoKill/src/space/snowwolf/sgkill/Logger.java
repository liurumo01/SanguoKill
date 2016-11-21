package space.snowwolf.sgkill;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import space.snowwolf.sgkill.GameOverException.VictoryStatus;
import space.snowwolf.sgkill.constant.RecordType;
import space.snowwolf.sgkill.constant.State;
import space.snowwolf.sgkill.player.Player;

public class Logger {

	private static List<Record> records = new ArrayList<Record>();
	private static DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	public static void reset() {
		File dir = new File("D:\\SanguoKill");
		if(!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File("D:\\SanguoKill\\" + format.format(new Date()) + ".log");
		OutputStream fileLogger = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fileLogger = new PrintStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.setOut(new PrintStream(new LoggerOutputStream(fileLogger, System.out)));
		records.clear();
	}

	public static void 状态变化(Player p, State state) {
		Record record = new Record(RecordType.状态变化, p, state);
		records.add(record);
		System.out.println(record);
	}

	public static void 出牌(Player src, Player dest, Card card) {
		Record record = new Record(RecordType.出牌, dest, src, card);
		records.add(record);
		System.out.println(record);
	}

	public static void 弃牌(Player player, Card card) {
		Record record = new Record(RecordType.弃牌, player, card);
		records.add(record);
		System.out.println(record);
	}

	public static void 受到伤害(Player player, Player src, Card card, int num) {
		List<Player> dests = new ArrayList<Player>();
		dests.add(player);
		Record record = new Record(RecordType.受到伤害, player, src, card, num);
		records.add(record);
		System.out.println(record);
	}

	public static void 求救(Player player) {
		Record record = new Record(RecordType.求救, player);
		records.add(record);
		System.out.println(record);
	}

	public static void 放弃(Player player) {
		Record record = new Record(RecordType.放弃, player);
		records.add(record);
		System.out.println(record);
	}

	public static void 死亡(Player player) {
		Record record = new Record(RecordType.死亡, player);
		records.add(record);
		System.out.println(record);
	}

	public static void 游戏开始(Player player) {
		Record record = new Record(RecordType.游戏开始, player);
		records.add(record);
		System.out.println(record);
	}

	public static void 游戏结束(VictoryStatus victory, List<Player> players) {
		Record record = new Record(RecordType.游戏结束, victory, players);
		records.add(record);
		System.out.println(record);
	}

	/**
	 * 获取指定玩家从上次弃牌之后的所有操作记录
	 * 
	 * @param p
	 * @return
	 */
	public static List<Record> getLatestRecords(Player player) {
		List<Record> list = records.stream().filter(new Predicate<Record>() {
			@Override
			public boolean test(Record t) {
				return t.type == RecordType.状态变化 && t.state == State.弃牌阶段 && t.src == player;
			}
		}).collect(Collectors.toList());
		if(list.size() == 0) {
			return records;
		}
		return records.subList(records.indexOf(list.get(list.size() - 1)), records.size());
	}

}
