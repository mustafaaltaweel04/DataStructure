package application;

public class Node implements Comparable<Object>{
	private Object data;
	private Node next;
	private Node prev;
	
	public Node(Object data) {
		this.data = data;
	}

	public Node(Object data, Node next, Node prev) {
		super();
		this.data = data;
		this.next = next;
		this.prev = prev;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public Node getPrev() {
		return prev;
	}

	public void setPrev(Node prev) {
		this.prev = prev;
	}

	@Override
	public String toString() {
		return "data=" + data + ", next=" + next + ", previous=" + prev;
	}

	@Override
	public int compareTo(Object o) {
		if(((Student)this.getData()).getAge() == ((Student) o).getAge())
			return 0;
		else if(((Student)this.getData()).getAge() > ((Student) o).getAge())
			return 1;
		else
			return -1;
	}
	
	
}
