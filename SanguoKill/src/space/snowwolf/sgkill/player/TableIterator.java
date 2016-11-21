package space.snowwolf.sgkill.player;

public class TableIterator {

	CircularListNode head;
	CircularListNode current;
	private int size;

	/**
	 * 以指定头节点为开始创建迭代器。
	 * @param head
	 */
	TableIterator(CircularListNode head) {
		this.head = head;
		this.current = head;
		//计算链表中的元素数量
		if(current == null) {
			this.size = 0;
		}
		this.size = 1;
		CircularListNode p = current.next;
		while(p != current) {
			p = p.next;
			this.size++;
		}
	}

	/**
	 * 计算当前节点与指定节点的距离。若指定节点为 null 则计算当前节点与默认头节点的距离。
	 * @return
	 */
	public int distance(Player p) {
		if(p == null) {
			p = head.val;
		}
		int d1 = searchNext(head, p);
		int d2 = searchPrev(head, p);
		return d1 <= d2 ? d1 : d2;
	}
	
	public int index() {
		return current.val.index;
	}
	
	private int searchNext(CircularListNode n, Player p) {
		if(n.val == p) {
			return 0;
		}
		return searchNext(n.next, p) + 1;
	}
	
	private int searchPrev(CircularListNode n, Player p) {
		if(n.val == p) {
			return 0;
		}
		return searchNext(n.prev, p) + 1;
	}
	
	/**
	 * 返回当前迭代器指向的循环链表的元素数量。
	 * @return
	 */
	public int size() {
		return size;
	}
	
	/**
	 * 在当前节点与上一个节点间差入一个新节点。
	 * @param obj
	 */
	public void insertBefore(Player obj) {
		CircularListNode x = current.prev;
		x.next = new CircularListNode(obj);
		x.next.next = current;
		current.prev = x.next;
		x.next.prev = x;
		size++;
	}

	/**
	 * 在当前节点与下一个节点之间差入一个新节点。
	 * @param obj
	 */
	public void insertAfter(Player obj) {
		CircularListNode x = current.next;
		x.prev = new CircularListNode(obj);
		x.prev.prev = current;
		current.next = x.prev;
		x.prev.next = x;
		size++;
	}

	/**
	 * 获取迭代器当前位置所储存的元素
	 * @return
	 */
	public Player value() {
		return current.val;
	}
	
	/**
	 * 获取当前迭代器后继位置的元素
	 * @return
	 */
	public Player next() {
		return current.next.val;
	}
	
	/**
	 * 获取当前迭代器前驱位置的元素
	 * @return
	 */
	public Player previous() {
		return current.prev.val;
	}
	
	/**
	 * 判断此节点是否有后继节点。对于非空循环链表，此方法永远返回 true。
	 */
	public boolean hasNext() {
		return current != null;
	}

	/**
	 * 判断此节点是否有前驱节点。对于非空循环链表，此方法永远返回 true。
	 * @return
	 */
	public boolean hasPrevious() {
		return current != null;
	}

	/**
	 * 将迭代器向后继方向移动一个元素.
	 */
	public TableIterator forward() {
		if (current == null) {
			return null;
		}
		current = current.next;
		return this;
	}

	/**
	 * 将迭代器向前驱方向移动一个元素。
	 * @return
	 */
	public TableIterator backward() {
		if (current == null) {
			return null;
		}
		current = current.prev;
		return this;
	}
	
	/**
	 * 删除迭代器当前位置之前的节点
	 */
	public void deleteBefore() {
		CircularListNode p = current.prev.prev;
		p.next = current;
		current.prev = p;
		size--;
	}
	
	/**
	 * 删除迭代器当前位置之后的节点
	 */
	public void deleteAfter() {
		CircularListNode p = current.next.next;
		p.prev = current;
		current.next = p;
	}

}
