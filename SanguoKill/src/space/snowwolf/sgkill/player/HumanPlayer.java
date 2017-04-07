package space.snowwolf.sgkill.player;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import space.snowwolf.common.utils.ArrayUtils;
import space.snowwolf.common.utils.InputReader;
import space.snowwolf.common.utils.StringUtils;
import space.snowwolf.sgkill.Controller;
import space.snowwolf.sgkill.card.Card;
import space.snowwolf.sgkill.constant.Identity;
import space.snowwolf.sgkill.constant.Message;
import space.snowwolf.sgkill.constant.State;

public class HumanPlayer extends Player {

	private OutputStream out;
	private InputReader reader;

	public HumanPlayer(String 昵称, int index, Identity identity, Controller controller) {
		super(昵称, identity, controller);
	}

	private String[] createShowCardOptions(String... others) {
		List<String> options = new ArrayList<String>();
		for (int i = 0; i < cards.size(); i++) {
			options.add(cards.get(i).toString());
		}
		for (String str : others) {
			options.add(str);
		}
		return options.toArray(new String[0]);
	}

	private String[] createSelectPlayerOptions(String... others) {
		List<String> options = new ArrayList<String>();
		Player[] table = controller.getTable();
		for (int i = 0; i < table.length; i++) {
			if(table[i] != this) {
				options.add(table[i].getName());				
			}
		}
		for (String str : others) {
			options.add(str);
		}
		return options.toArray(new String[0]);
	}

	@Override
	public Card selectCard() {
		return selectCard(null);
	}

	@Override
	public Card selectCard(Player dest, Class<?> ... classes) {
		Card x = null;
		while (true) {
			int t = reader.choose("请选择", createShowCardOptions(controller.getCurrentPlayer() == this && controller.getState() != State.求救 ? "弃牌" : "放弃", "标识身份"));
			if (t == cards.size() + 1) {
				setEnd(true);
				return null;
			} else if (t == cards.size() + 2) {
				t = reader.choose("请选择要标识的玩家", createSelectPlayerOptions());
				int u = reader.choose("请选择", "忠臣", "内奸", "反贼");
				Identity[] options = { Identity.忠臣, Identity.内奸, Identity.反贼 };
				identities[t - 1] = options[u - 1];
			} else {
				x = cards.get(t - 1);
				if (classes == null || classes.length == 0 || ArrayUtils.index(classes, x.getClass()) >= 0) {
					return x;
				} else {
					Object[] options = new Object[classes.length];
					for(int i=0;i<options.length;i++) {
						options[i] = classes[i].getSimpleName();
					}
					System.out.println("需要出" + StringUtils.connect('/', options));
				}
			}
		}
	}

	@Override
	public Player selectPlayer() {
		int result = reader.choose("请选择对象：", createSelectPlayerOptions()) - 1;
		Player[] table = controller.getTable();
		if(result >= ArrayUtils.index(table, this)) {
			//创建选项时会将自己排除在外，因此在得到结果后若被选择的是自己也应该
			result = (result + 1) % table.length;
		}
		return controller.getTable()[result];
	}

	@Override
	public void error(Message e) {
		System.out.println(e);
	}

	public void setInputStream(InputStream in) {
		reader = new InputReader();
		reader.init(in);
	}

	public OutputStream getOutputStream() {
		return out;
	}

	public void setOutputStream(OutputStream out) {
		this.out = out;
	}

}
