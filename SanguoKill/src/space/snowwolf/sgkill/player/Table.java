package space.snowwolf.sgkill.player;

/**
 * 使用迭代器操作的循环链表
 * 
 * @author 天问雪狼
 *
 * @param <Player>
 */
public class Table {

	private CircularListNode head;

	/**
	 * 通过数组构建循环链表，链表头结点指向数组第一个元素，若数组为 null 或数组中只有一个元素，则创建一个空链表。
	 * @param arr 
	 */
	public Table(Player[] arr) {
		if (arr == null || arr.length == 0) {
			return;
		}
		head = new CircularListNode(arr[0]);
		head.next = head;
		head.prev = head.prev;
		TableIterator it = (TableIterator) iterator();
		for (int i = 1; i < arr.length; i++) {
			it.insertAfter(arr[i]);
			it.forward();
		}
	}
	
	public Player getHead() {
		return head.val;
	}
	
	/**
	 * 修改链表头结点的位置，此操作不会通知已经创建的迭代器头结点已经改变，只会使新创建的默认迭代器以新头结点开始。若当前链表中找不到指定的元素，则头结点位置不变。
	 * @param p
	 */
	public void setHead(Player p) {
		int count = size();
		CircularListNode t = head;
		while(t.val != p && count > 0) {
			t = t.next;
			count--;
		}
		if(count != 0) {
			head = t;
		}
	}

	/**
	 * 通过迭代器计算此链表中元素的个数
	 * @return
	 */
	public int size() {
		if (head == null) {
			return 0;
		}
		return ((TableIterator) iterator()).size();
	}

	/**
	 * 创建一个新的以默认头结点开始的迭代器。
	 */
	public TableIterator iterator() {
		return new TableIterator(head);
	}
	
	/**
	 * 搜索指定节点，若找到则创建以该节点开始的迭代器，否则返回 null。
	 * @param obj
	 * @return
	 */
	public TableIterator iterator(Player obj) {
		if(head == null) {
			return null;
		}
		int count = size();
		TableIterator it = (TableIterator) iterator();
		while(it.current.val != obj && count > 0) {
			it.forward();
			count--;
		}
		if(count == 0) {
			return null;
		}
		return it;
	}

}

class CircularListNode {

	CircularListNode(Player obj) {
		this.val = obj;
	}

	Player val;
	CircularListNode next;
	CircularListNode prev;

	@Override
	public String toString() {
		return val.toString();
	}
	
}