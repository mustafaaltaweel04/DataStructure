import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LinkedList {
	private int size;
	private Node back;
	private Node front;

	LinkedList() {
		front = back = null;
		size = 0;
	}

	void addFirst(Node n) {
		if (back == null)
			back = front = n;
		else {
			n.next = front;
			front = n;
		}
		size++;
	}

	void add(int n, Node node) {
		if (n == 0)
			addFirst(node);
		else if (n >= size)
			addLast(node);
		else {
			Node current = front;
			for (int i = 0; i < n - 1; i++)
				current = current.next;
			node.next = current.next;
			current.next = node;
			size++;
		}
	}

	void addSorted(Node node) {
		if (size == 0) {
			addFirst(node);
			return;
		}
		boolean isFound = false;
		Node current = front;
		for (int i = 0; i < size; i++) {
			if (((Martyr)node.data).getAge() < ((Martyr)current.data).getAge()) {
				add(i, node);
				isFound = true;
				break;
			}
			current = current.next;
		}

		if (!isFound)
			addLast(node);
	}

	void addLast(Node node) {
		if (front == null)
			front = back = node;
		else {
			back.next = node;
			back = node;
		}
		size++;
	}

	boolean removeFirst() {
		if (size == 0)
			return false;
		else if (size == 1)
			front = back = null;
		else {
			front = front.next;
		}
		size--;
		return true;
	}

	boolean remove(int n) {
		if (size == 0)
			return false;
		else if (n == 0)
			return removeFirst();
		else if (n == size - 1)
			return removeLast();
		else if (n < size - 1 && size > 0) {
			Node current = front;
			for (int i = 0; i < n - 1; i++)
				current = current.next;
			current.next = current.next.next;
			size--;
			return true;
		}
		return false;
	}

	boolean removeLast() {
		if (size == 0)
			return false;
		else if (size == 1) {
			front = back = null;
		} else {
			Node current = front;
			while (current.next != back) {
				current = current.next;
			}
			current.next = null;
			back = current;
		}
		size--;
		return true;
	}

	Object getFirst() {
		if (size == 0)
			return null;
		return front.data;
	}

	Object get(int index) {
		if (size == 0)
			return null;
		else if (index == 1)
			return getFirst();
		else if (index == size - 1)
			return getLast();
		else if (index < size - 1 && size > 0) {
			Node current = front;
			for (int i = 0; i < index; i++)
				current = current.next;
			return current.data;
		} else
			return null;
	}

	Object getLast() {
		if (size == 0)
			return null;
		return back.data;
	}

	int size() {
		return size;
	}

	public ObservableList<Martyr> getList() {
		ObservableList<Martyr> result = FXCollections.observableArrayList();
		Node current = front;
		while (current != null) {
			result.add((Martyr) current.data);
			current = current.next;
		}
		return result;
	}

	public ObservableList<Martyr> searchByPart(String PartName) {
		ObservableList<Martyr> result = FXCollections.observableArrayList();

		Node current = front;
		while (current != null) {
			if (((Martyr) current.data).getName().contains(PartName)) {
				result.add((Martyr) current.data);
			}
			current = current.next;
		}
		return result;
	}

	public Martyr searchMartyr(String name) {
		Node current = front;
		while (current != null) {
			if (((Martyr) current.data).getName().equalsIgnoreCase(name)) {
				return (Martyr) current.data;
			}
			current = current.next;
		}
		return null;
	}
	public int indexOf(String name) {
		Node current = front;
		int index = 0;
	
		while (current != null) {
			if (((Martyr) current.data).getName().equalsIgnoreCase(name)) {
				return index;
			}
			current = current.next;
			index++;
		}
		
		return -1; 
	}
	public boolean exist(String name){
		return searchMartyr(name) != null;
	}

	public void print_list() {
		Node current = front;
		while (current != null) {
			System.out.println("\t\t\t"+current.data.toString());
			current = current.next;
		}
		
	}
}