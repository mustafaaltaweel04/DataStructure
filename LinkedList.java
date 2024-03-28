package application;

public class LinkedList {
	private int size;
	private Node back;
	private Node front;
	
	LinkedList(){
		front = back = null;
		size = 0;
	}
	
	void addFirst(Node n){
		if(back == null)
			back= front = n;
		else{
		n.setNext(front);
		front.setPrev(n);
		front = n;
		}
		size++;
	}
	void add(int n, Node node){
		if(n == 0)
			addFirst(node);
		else if(n >= size)
			addLast(node);
		else{
			Node current = front;
			for(int i = 0; i < n-1; i++)
				current = current.getNext();
			node.setNext(current.getNext());
			node.setPrev(current);
			current.setNext(node);
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
	        if (node.compareTo(current.getData()) <= 0) {
	            add(i, node);
	            isFound = true;
	            break;
	        }
	        current = current.getNext();
	    }
	    
	    if (!isFound)
	        addLast(node);
	}

	void addLast(Node node){
		if(front == null)
			front = back = node;
		else{
			back.setNext(node);
			node.setPrev(back);
			back = node;
		}
		size++;
	}
	
	boolean removeFirst(){
		if(size == 0)
			return false;
		else if(size == 1)
			front = back = null;
		else{
			front = front.getNext();
			front.setPrev(null);
		}
		size--;
		return true;
	}

	boolean remove(int n){
		if(size == 0)
			return false;
		else if(n == 0)
			return removeFirst();
		else if(n == size - 1)
			return removeLast();
		else if(n < size - 1 && size > 0){
			Node current = front;
			for(int i = 0; i < n; i++)
				current = current.getNext();
			current.getPrev().setNext(current.getNext());
			current.getNext().setPrev(current.getPrev());
			size--;
			return true;
		}
		return false;
	}
	boolean removeLast(){
        if(size == 0)
            return false;
        else if(size == 1) {
            front = back = null;
        } else {
            back = back.getPrev();
            back.setNext(null);
        }
        size--;
        return true;
    }
	
	Object getFirst(){
		if(size == 0)
			return null;
		return front.getData();
	}
	Object get(int index){
		if(size == 0)
			return null;
		else if(index == 1)
			return getFirst();
		else if(index == size-1)
			return getLast();
		else if (index < size-1 && size > 0){
			Node current = front;
			for(int i = 0;i < index;i++)
				current = current.getNext();
			return current.getData();
		}
		else return null;
	}
	Object getLast(){
		if(size == 0)
			return null;
		return back.getData();
	}
	
	int size(){
		return size;
	}
	
	void print(){
		Node current = front;
		for(int i = 0;i < size;i++){
			System.out.println(current.getData());
			current = current.getNext();
		}
	}
}
