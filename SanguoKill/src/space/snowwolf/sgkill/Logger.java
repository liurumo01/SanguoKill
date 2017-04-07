package space.snowwolf.sgkill;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import space.snowwolf.sgkill.GameOverException.VictoryStatus;
import space.snowwolf.sgkill.card.Card;
import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.constant.RecordType;
import space.snowwolf.sgkill.constant.State;
import space.snowwolf.sgkill.player.HumanPlayer;
import space.snowwolf.sgkill.player.Player;

@Component
public class Logger extends Observable {

	private static List<Record> records = new ArrayList<Record>();
	private static DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	private Map<HumanPlayer, PrintStream> map;
	private String loggingPath;
	private PrintStream fileLogger;

	public Logger() {
		map = new HashMap<HumanPlayer, PrintStream>();
	}

	private void writeToAllPlayersAndFile(Record record) {
		writeToAllPlayers(record);
		fileLogger.println(record);
	}

	private void writeToAllPlayers(Record record) {
		for (Entry<HumanPlayer, PrintStream> entry : map.entrySet()) {
			entry.getValue().println(record);
		}
	}

	private void writeToPlayer(Record record, HumanPlayer player) {
		map.get(player).println(record);
	}

	private void writeToFile(Record record) {
		fileLogger.println(record);
	}

	public void addPlayerOutputStream(HumanPlayer player) {
		map.put(player, new PrintStream(player.getOutputStream()));
	}

	public void init() {
		File dir = new File(loggingPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(loggingPath + "\\" + format.format(new Date()) + ".log");
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
		// System.setOut(new PrintStream(new LoggerOutputStream(fileLogger,
		// System.out)));
		records.clear();
	}

	public void 状态变化(Player p, State state) {
		Record record = new Record(RecordType.状态变化, p, state);
		newRecord(record);
		writeToAllPlayersAndFile(record);
	}

	public void 摸牌(Player p, Card card) {
		Record record = new Record(RecordType.摸牌, p, card);
		newRecord(record);
		if (p instanceof HumanPlayer) {
			writeToPlayer(record, (HumanPlayer) p);
		}
		writeToFile(record);
	}

	public void 出牌(Player src, Player dest, Card card) {
		Record record = new Record(RecordType.出牌, dest, src, card);
		newRecord(record);
		writeToAllPlayersAndFile(record);
	}

	public void 弃牌(Player player, Card card) {
		Record record = new Record(RecordType.弃牌, player, card);
		newRecord(record);
		writeToAllPlayersAndFile(record);
	}

	public void 受到伤害(Player player, Player src, Card card, int num) {
		List<Player> dests = new ArrayList<Player>();
		dests.add(player);
		Record record = new Record(RecordType.受到伤害, player, src, card, num);
		newRecord(record);
		writeToAllPlayersAndFile(record);
	}

	public void 求救(Player player) {
		Record record = new Record(RecordType.求救, player);
		newRecord(record);
		writeToAllPlayersAndFile(record);
	}

	public void 放弃(Player player) {
		Record record = new Record(RecordType.放弃, player);
		newRecord(record);
		writeToAllPlayersAndFile(record);
	}

	public void 死亡(Player player) {
		Record record = new Record(RecordType.死亡, player);
		newRecord(record);
		writeToAllPlayersAndFile(record);
	}

	public void 游戏开始(Player[] players, Player 主公) {
		Record record = new Record(RecordType.游戏开始, 主公, players);
		newRecord(record);
		writeToFile(record);
		for (Player p : players) {
			if (p instanceof HumanPlayer) {
				writeToPlayer(new Record(RecordType.游戏开始, 主公, p), (HumanPlayer) p);
				if (p.getIdentity(null) != Identity.主公) {
					writeToPlayer(new Record(RecordType.游戏开始, p, p), (HumanPlayer) p);
				}
			}
		}
	}

	public void 游戏结束(VictoryStatus victory, List<Player> players) {
		Record record = new Record(RecordType.游戏结束, victory, players);
		newRecord(record);
		System.out.println(record);
	}

	/**
	 * 获取指定玩家从上次弃牌之后的所有操作记录
	 * 
	 * @param p
	 * @return
	 */
	public List<Record> getLatestRecords(Player player) {
		List<Record> list = records.stream().filter(new Predicate<Record>() {
			@Override
			public boolean test(Record t) {
				return t.type == RecordType.状态变化 && t.state == State.弃牌阶段 && t.src == player;
			}
		}).collect(Collectors.toList());
		if (list.size() == 0) {
			return records;
		}
		return records.subList(records.indexOf(list.get(list.size() - 1)), records.size());
	}

	private void newRecord(Record record) {
		records.add(record);
		setChanged();
		notifyObservers(record);
	}

	public void setLoggingPath(String loggingPath) {
		this.loggingPath = loggingPath;
	}

}
