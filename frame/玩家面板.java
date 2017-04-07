package space.snowwolf.sgkill.frame;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import space.snowwolf.sgkill.card.Card;

public class 玩家面板 extends JPanel {

	private static final long serialVersionUID = -7542199244074703091L;

	private JTextField 武将栏 = new JTextField();
	private JTextField 身份栏 = new JTextField();
	private JTextField 血量栏 = new JTextField();
	
	private JComboBox<Card> 判定区 = new JComboBox<Card>();
	private JComboBox<Card> 装备栏 = new JComboBox<Card>();
	private JComboBox<Card> 手牌栏 = new JComboBox<Card>();
	
	public 玩家面板() {
		setSize(100, 150);
		initLayout();
		initEvent();
	}
	
	private void initLayout() {
		武将栏.setSize(60, 30);
		身份栏.setSize(60, 30);
		血量栏.setSize(60, 30);
		
		判定区.setSize(60, 150);
		装备栏.setSize(60, 150);
		手牌栏.setSize(60, 150);
	}
	
	private void initEvent() {
		
	}
	
}
