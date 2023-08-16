import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

class Node<T extends Comparable<T>>{//Node for linked list
	private T item;
	private Node<T> next;

	public Node(T obj){
		this.item = obj;
		this.next = null;
	}
	public Node(T obj, Node<T> nxtNode){
		this.item = obj;
		this.next = nxtNode;
	}
	public final T getItem(){
		return item;
	}
	public final void setItem(T obj){
		this.item = obj;
	}
	public Node<T> getNext(){
		return this.next;
	}
	public final void setNext(Node<T> nxtNode){
		this.next = nxtNode;
	}
	public final void insertNext(T obj){
		Node<T> newNode = new Node<T>(obj, this.next);
		this.next = newNode;
	}
	public final void removeNext(){
		this.next = this.next.next;
	}
}
class LinkedList<T extends Comparable<T>>{
	//dummy head
	Node<T> head;
	int numItems;
	public LinkedList(){
		head = new Node<T>(null);
	}
	public LinkedList(T data){
		head = new Node<T>(null);
		this.add(data);
	}
	public boolean isEmpty(){
		return head.getNext() == null;
	}
	public int size(){
		return numItems;
	}
	public T first(){
		return head.getNext().getItem();
	}
	public void add(T item){//add maintaining sorted conditions
		Node<T> last = head;
		while (last.getNext() != null && last.getNext().getItem().compareTo(item) < 0){
			last = last.getNext();
		}
		last.insertNext(item);
		numItems += 1;
	}
	public void del(T item){//delete maintaining sorted conditions
		Node<T> curr = head;
		while(curr.getNext() != null && curr.getNext().getItem().compareTo(item) < 0){
			curr = curr.getNext();
		}
		if(curr.getNext() != null && curr.getNext().getItem().equals(item)){
			curr.removeNext();
			numItems -= 1;
		}
	}
	public Iterator<T> iterator(){
		return new LinkedListIterator<>(head);
	}
	public void removeAll(){
		head.setNext(null);
	}
}
class LinkedListIterator<T extends Comparable<T>> implements Iterator<T>{//iterator for linked list
	private Node<T> current;
    private Node<T> previous;
    private boolean canRemove;

    public LinkedListIterator(Node<T> head) {
        current = head.getNext();
        previous = head;
        canRemove = false;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        T data = current.getItem();
        previous = current;
        current = current.getNext();
        canRemove = true;
        return data;
    }

    @Override
    public void remove() {//did not use.
        if (!canRemove) {
            throw new IllegalStateException("Next method has not been called, or remove() is called multiple times in a row.");
        }
        if (previous == null) {
            throw new IllegalStateException("remove() cannot be called before calling next().");
        }
        previous.setNext(current.getNext());
        canRemove = false;
    }
}
class pair<T, S>{//simple pair, for delMin algorithm
	public T first;
	public S second;
	public pair(T a, S b){
		this.first = a;
		this.second = b;
	}
}
class cPair<T extends Comparable<T>, S extends Comparable<S>> implements Comparable<cPair<T, S>>{// comparable pair (tuple)
	public T first;
	public S second;
	public cPair(T a, S b){
		this.first = a;
		this.second = b;
	}
	
	@Override
	public int compareTo(cPair<T, S> other){
		if(first.equals(other.first))
			return second.compareTo(other.second);
		return first.compareTo(other.first);
	}

	@Override
	public boolean equals(Object other){
		if(this == other) return true;
		if(other == null || getClass() != other.getClass()) return false;
		cPair<?, ?> others = (cPair<?, ?>)other;
		return first.equals(others.first) && second.equals(others.second);
	}

}
class AVLNode<T extends Comparable<T>, S extends Comparable<S>>{//AVL tree node
	private T data;//T is string
	private LinkedList<S> indexLinkedList;//S is pair
	private AVLNode<T, S> left;
	private AVLNode<T, S> right;
	private int height = 1;
	
	public AVLNode(T obj, S pair){
		this.data = obj;
		this.indexLinkedList = new LinkedList<S>(pair);
		this.left = null;
		this.right = null;
		this.height = 1;
	}
	public T getData(){
		return this.data;
	}
	public void setData(T data){
		this.data = data;
	}
	public void addList(S pair){
		this.indexLinkedList.add(pair);
	}
	public void delList(S pair){
		this.indexLinkedList.del(pair);
	}
	public LinkedList<S> getIndexList(){
		return this.indexLinkedList;
	}
	public AVLNode<T, S> getLeft(){
		return this.left;
	}
	public void setLeft(AVLNode<T, S> left){
		this.left = left;
	}
	public AVLNode<T, S> getRight(){
		return this.right;
	}
	public void setRight(AVLNode<T, S> right){
		this.right = right;
	}
	public int getHeight(){
		return this.height;
	}
	public void setHeight(int h){
		this.height = h;
	}
}
class AVLTree<T extends Comparable<T>, S extends Comparable<S>>{//avltree
	private AVLNode<T, S> root;
	public AVLTree(){
		this.root = null;
	}
	public AVLNode<T, S> getRoot(){
		return this.root;
	}
	public int getBalance(AVLNode<T, S> root){
		if(root == null) return 0;
		return getNodeHeight(root.getLeft()) - getNodeHeight(root.getRight());
	}
	public int getNodeHeight(AVLNode<T, S> root){
		if(root == null) return 0;
		return root.getHeight();
	}
	public void resetHeight(AVLNode<T, S> root){//made as a method
        if(root == null) return;
		root.setHeight(1 + Math.max(getNodeHeight(root.getRight()), getNodeHeight(root.getLeft())));
	}
	public AVLNode<T, S> rotateLeft(AVLNode<T, S> root){
        if(root == null || root.getRight() == null) return null;
		AVLNode<T, S> temp = root.getRight();
		root.setRight(temp.getLeft());
		temp.setLeft(root);
		resetHeight(root);
		resetHeight(temp);
		return temp;
	}
	public AVLNode<T, S> rotateRight(AVLNode<T, S> root){
        if(root == null || root.getLeft() == null) return null;
		AVLNode<T, S> temp = root.getLeft();
		root.setLeft(temp.getRight());
		temp.setRight(root);
		resetHeight(root);
		resetHeight(temp);
		return temp;
	}
	public void insert(T data, S pair){
		this.root = insertNode(root, data, pair);
	}
	public AVLNode<T, S> insertNode(AVLNode<T, S> root, T data, S pair){
		if(root == null){
			return new AVLNode<>(data, pair);
		}
		if(data.equals(root.getData())){// just add on existing linked list
			root.addList(pair);
			return root;
		}//otherwise, make new node
		if(data.compareTo(root.getData()) < 0){
			root.setLeft(insertNode(root.getLeft(), data, pair));
		}
		else{
			root.setRight(insertNode(root.getRight(), data, pair));
		}
		resetHeight(root);//tree balancing is needed for only make new node case
		int bal = getBalance(root);
		if(bal < -1){
			if(data.compareTo(root.getRight().getData()) > 0)
				root = rotateLeft(root);
			else{
				root.setRight(rotateRight(root.getRight()));
				root = rotateLeft(root);
			}
		}
		else if(bal > 1){
			if(data.compareTo(root.getLeft().getData()) < 0)
				root = rotateRight(root);
			else{
				root.setLeft(rotateLeft(root.getLeft()));
				root = rotateRight(root);
			}
		}
		return root;
	}
	public void delete(T data, S pairs){
		this.root = deleteNode(root, data, pairs);
	}
	public AVLNode<T, S> deleteNode(AVLNode<T, S> root, T data, S pairs){
		if(root == null){
			return null;
		}
        int comp = data.compareTo(root.getData());
		if(comp == 0){
			root.delList(pairs);
			if(root.getIndexList().isEmpty()){ // need to delete node, because node becomes empty
				if(root.getLeft() == null && root.getRight() == null)
					return null;
				else if(root.getLeft() == null)
					return root.getRight();
				else if(root.getRight() == null)
					return root.getLeft();
				pair<T, AVLNode<T, S>> rPair = delMinItem(root);
				root.setData(rPair.first);
				root.setLeft(rPair.second);
			}
		}
		else if(comp < 0){
			root.setLeft(deleteNode(root.getLeft(), data, pairs));
		}
		else{
			root.setRight(deleteNode(root.getRight(), data, pairs));
		}
		resetHeight(root);//tree balancing needed when deleting node
		int bal = getBalance(root);
		if(bal < -1){
			if(data.compareTo(root.getRight().getData()) > 0)
				root = rotateLeft(root);
			else{
				root.setRight(rotateRight(root.getRight()));
				root = rotateLeft(root);
			}
		}
		else if(bal > 1){
			if(data.compareTo(root.getLeft().getData()) < 0)
				root = rotateRight(root);
			else{
				root.setLeft(rotateLeft(root.getLeft()));
				root = rotateRight(root);
			}
		}
		return root;
	}
	private pair<T, AVLNode<T, S>> delMinItem(AVLNode<T, S> root){
		if(root.getLeft() == null)
			return new pair<>(root.getData(), root.getRight());
		pair<T, AVLNode<T, S>> rPair = delMinItem(root.getLeft());
		root.setLeft(rPair.second);
		resetHeight(root);
		int bal = getBalance(root);
		if(bal < -1){
			if(getBalance(root.getRight()) <= 0)
				root = rotateLeft(root);
			else{
				root.setRight(rotateRight(root.getRight()));
				root = rotateLeft(root);
			}
		}
		else if(bal > 1){
			if(getBalance(root.getLeft()) >= 0)
				root = rotateRight(root);
			else{
				root.setLeft(rotateLeft(root.getLeft()));
				root = rotateRight(root);
			}
		}
		rPair.second = root;
		return rPair;
	}
    public LinkedList<S> search(T data){
		return searchNode(root, data);
	}
	public LinkedList<S> searchNode(AVLNode<T, S> root, T data){
		if(root == null) return null;
		int comp = data.compareTo(root.getData());
		if(comp == 0) return root.getIndexList();
		if(comp < 0) return searchNode(root.getLeft(), data);
		return searchNode(root.getRight(), data);
	}
	public void printPreorder(){
		if(root == null) System.out.print("EMPTY");
		else doPreorder(root);
	}
	public void doPreorder(AVLNode<T, S> node){
		if(node == null) return;
		if(node == this.root) System.out.print(node.getData());
		else System.out.print(" " + node.getData());
		doPreorder(node.getLeft());
		doPreorder(node.getRight());
	}
}
class Hashtable{
	AVLTree<String, cPair<Integer, Integer>>[] table;
	public Hashtable(){
		table = (AVLTree<String, cPair<Integer, Integer>>[]) new AVLTree<?,?>[100];
        for(int i=0; i<100; i++) table[i] = new AVLTree<>();
	}
	private int hashfunc(String s){//hash function
		int ret = 0;
		for(int i=0; i<6; i++) ret += (int)s.charAt(i);
		return ret%100;
	}
	public void put(String s, cPair<Integer, Integer> pairs){
		int code = hashfunc(s);
		table[code].insert(s, pairs);
	}
	public AVLTree<String, cPair<Integer, Integer>> get(String s){
		return table[hashfunc(s)];
	}
	public void preOrder(int numIndex){
		table[numIndex].printPreorder();
        System.out.println();
	}
}	
public class Matching
{	
	static int lines_cnt;
	static List<String> lines;
	private static Hashtable table;
	private static void init(String input){// partially used chatGPT only for manipulating file paths
		table = new Hashtable();
        
		lines = new ArrayList<>();
        Path path = Paths.get(input);
		if(!path.isAbsolute()){
			path = Paths.get(System.getProperty("user.dir"), input);
		}
		try {
            FileReader fileReader = new FileReader(input);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//From here, done by man
		int cnt = 1;
		for(String line : lines){
			int len = line.length();
			for(int i=0; i<=len-6; i++){
				table.put(line.substring(i, i+6), new cPair<Integer, Integer>(cnt, i+1));
			} cnt++;
		}
		lines_cnt = cnt;        
	}
	private static void printIndex(int numIndex){
		table.preOrder(numIndex);
	}
	private static void findPattern(String pattern){
		String curr_pattern = pattern.substring(0, 6);
		LinkedList<cPair<Integer, Integer>> candidates = table.get(curr_pattern).search(curr_pattern);
		if(candidates == null || candidates.size() == 0){
			System.out.println("(0, 0)"); return;
		}
		boolean[] isValid = new boolean[candidates.size()];
		List<cPair<Integer, Integer>> candList = new ArrayList<>();
		Node<cPair<Integer, Integer>> last = candidates.head;
		for(int i=0; i<candidates.size(); i++){
			last = last.getNext();
			candList.add(i, last.getItem());
			isValid[i] = true;
		}
		int diff = 0;
		for (int i = 1; i <= pattern.length()/6; i++) {
			if(6*i+6 <= pattern.length())
				diff = i*6;	
			else
				diff = pattern.length() - 6;
			String curr_pattern2 = pattern.substring(diff, diff+6);
			LinkedList<cPair<Integer, Integer>> candidates2 = table.get(curr_pattern2).search(curr_pattern2);
			if(candidates2 == null || candidates2.size() == 0){
				System.out.println("(0, 0)"); return;
			}
			List<cPair<Integer, Integer>> candList2 = new ArrayList<>();
			Node<cPair<Integer, Integer>> last2 = candidates2.head;
			for(int j=0; j<candidates2.size(); j++){
				last2 = last2.getNext();
				candList2.add(j, last2.getItem());
			}
			for(int j=0; j<candidates.size(); j++){//used contains method to check
				if(!isValid[j]) continue;
				if(!candList2.contains(new cPair<>(candList.get(j).first, candList.get(j).second + diff)))
					isValid[j] = false;
			}
		}
		int cnt = 0, lastidx = -1;
		for(int i=0; i<candidates.size(); i++){
			if(isValid[i]){//isvalid=true means that it is in the union of all possible sets
				cnt++; lastidx = Math.max(lastidx, i);
			}
		}
		if(cnt == 0)
			System.out.println("(0, 0)");
		else{
			for(int i=0; i<candidates.size(); i++){
				if(isValid[i] && i!= lastidx)
					System.out.printf("(%d, %d) ", candList.get(i).first, candList.get(i).second);
			}
			System.out.printf("(%d, %d)\n", candList.get(lastidx).first, candList.get(lastidx).second);
		}
	}
	private static void deleteString(String pattern){
        int delete_count = 0;
		LinkedList<cPair<Integer, Integer>> deleteList = table.get(pattern).search(pattern);
        if(deleteList == null){
            System.out.println(0); return;
        }
		List<Integer>[] targetList = (List<Integer>[])new List<?>[lines_cnt];
        for(int i=0; i<lines_cnt-1; i++) targetList[i] = new ArrayList<>();
		Iterator<cPair<Integer, Integer>> iter1 = deleteList.iterator();
		while(iter1.hasNext()){
			cPair<Integer, Integer> elm = iter1.next();
			targetList[elm.first-1].add(elm.second-1);
            delete_count++;
		}
		for(int i=0; i<lines_cnt-1; i++){
			if(targetList[i].isEmpty()) continue;
			String target = lines.get(i);//delete whole target string
			for(int j=0; j<=target.length()-6; j++){
				table.get(target.substring(j, j+6)).delete(target.substring(j, j+6) , new cPair<Integer, Integer>(i+1, j+1));
			}
			String newString = delString(target, targetList[i]); // string after altered
			lines.set(i, newString);
			int len = newString.length();
			for(int j=0; j<=len-6; j++){//updating hashtable again with new string
				table.put(newString.substring(j, j+6), new cPair<Integer, Integer>(i+1, j+1));
			}
		}
        System.out.println(delete_count);
	}
	private static String delString(String target, List<Integer> indices){ // used to return new string
		String ret = "";
		int stridx = 0;
		for(Integer idx : indices){ // similar to two pointer algorithm
			while(stridx < idx){
				ret = ret + target.charAt(stridx);
				stridx++;
			}
			stridx = idx+6;// stridx may decrease, depending on the distance between indices, hence can check all
		}
		if(stridx < target.length() - 1){
			while(stridx < target.length()){
				ret = ret + target.charAt(stridx);
				stridx++;
			}
		}
		return ret;
	}
	private static void addString(String pattern){
		lines.add(pattern);
		int len = pattern.length();
		for(int i=0; i<=len-6; i++){
			table.put(pattern.substring(i, i+6), new cPair<Integer, Integer>(lines_cnt, i+1));
		}
		System.out.println(lines_cnt);
        lines_cnt++;
	}
	
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input){
		switch (input.charAt(0)) {
			case '<':
				init(input.substring(2));
				break;
			case '@':
				printIndex(Integer.valueOf(input.substring(2)));
				break;
			case '?':
				findPattern(input.substring(2));
				break;
			case '/':
				deleteString(input.substring(2));
				break;
			case '+':
				addString(input.substring(2));
				break;
			default:
				break;
		}
	}
}

