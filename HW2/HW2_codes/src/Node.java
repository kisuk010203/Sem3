public class Node<T> {//for linked list
    private T item;
    private Node<T> next;

    public Node(T obj) {//for head
        this.item = obj;
        this.next = null;
    }
    
    public Node(T obj, Node<T> next) {//for linked list
    	this.item = obj;
    	this.next = next;
    }
    
    public final T getItem() {
    	return item;
    }
    
    public final void setItem(T item) {
    	this.item = item;
    }
    
    public final void setNext(Node<T> next) {
    	this.next = next;
    }
    
    public Node<T> getNext() {
    	return this.next;
    }
    
    public final void insertNext(T obj) {
      Node<T> new_obj = new Node<T>(obj, this.next);
      this.next = new_obj;
    }
    
    public final void removeNext() {
      this.next = this.next.next;
    }
}