import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
public class MovieDB{
	private MyLinkedList<Genre> genreLinkedList;
    public MovieDB() {
		this.genreLinkedList = new MyLinkedList<Genre>();
    }

    public void insert(MovieDBItem item) {
		Node<Genre> last = this.genreLinkedList.head;
		while (last.getNext() != null && 
			last.getNext().getItem().getItem().compareTo(item.getGenre()) < 0){
			last = last.getNext();
		}
		if(last.getNext() == null || !(last.getNext().getItem().getItem().equals(item.getGenre()))){
			Genre new_Genre = new Genre(item.getGenre());
			last.insertNext(new_Genre);
			last = last.getNext();
			last.getItem().add_movie(item);
		}
		else{
			last = last.getNext();
			last.getItem().add_movie(item);
		}
    }

    public void delete(MovieDBItem item) {
		Node <Genre> last = this.genreLinkedList.head;
		while (last.getNext() != null && 
			last.getNext().getItem().getItem().compareTo(item.getGenre()) < 0){
			last = last.getNext();
		}
		if(last.getNext().getItem().getItem().equals(item.getGenre())){
			last.getNext().getItem().remove_movie(item);
		}
		if (last.getNext().getItem().getMovieList().isEmpty()){
			last.removeNext();
		}
    }

    public MyLinkedList<MovieDBItem> search(String term) {
		Pattern pattern = Pattern.compile(term);
		// System.out.println("---------------------");
		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
		Node<Genre> genNode = this.genreLinkedList.head;
		while(genNode.getNext() != null){
			genNode = genNode.getNext();
			// System.out.println("Current Genre : " + genNode.getItem().getItem());
			Node<MovieDBItem> startNode = genNode.getItem().getMovieList().head;
			while(startNode.getNext() != null){
				startNode = startNode.getNext();
				if(startNode.getItem().getTitle().indexOf(term) >= 0)
					results.add(startNode.getItem());
			}
		}
    	return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
		Node<Genre> genNode = this.genreLinkedList.head;
		while(genNode.getNext() != null){
			genNode = genNode.getNext();
			Node<MovieDBItem> startNode = genNode.getItem().getMovieList().head;
			while(startNode.getNext() != null){
				startNode = startNode.getNext();
				results.add(startNode.getItem());
			}
		}
    	return results;
    }
}

class Genre extends Node<String> implements Comparable<Genre> {
	private MovieList movieList;
	public Genre(String name) {
		super(name);
		this.movieList = new MovieList();
	}
	
	@Override
	public int compareTo(Genre o) {
		return this.getItem().compareTo(o.getItem());
	}

	@Override
	public int hashCode() {
		return this.getItem().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Genre other = (Genre) obj;
        if (this.getItem() == null) {
            if (other.getItem() != null)
                return false;
        }
		else if (!this.getItem().equals(other.getItem()))
            return false;
        return true;
	}

	public void add_movie(MovieDBItem item){
		this.movieList.add(item);
	}
	public void remove_movie(MovieDBItem item){
		this.movieList.remove(item);
	}
	public MovieList getMovieList(){
		return this.movieList;
	}
}

class MovieList extends MyLinkedList<MovieDBItem> {	
	Node<MovieDBItem> head;
	int numItems;
	public MovieList(){
		head = new Node<MovieDBItem>(null);
	}

	@Override
	public boolean isEmpty() {
		return head.getNext() == null;
	}

	@Override
	public int size() {
		return numItems;
	}

	@Override
	public void add(MovieDBItem item) {
		Node<MovieDBItem> last = head;
		while (last.getNext() != null && last.getNext().getItem().compareTo(item) < 0){
			last = last.getNext();
		}
		if(last.getNext() == null || !(last.getNext().getItem().equals(item))){
			last.insertNext(item);
			numItems += 1;
		}
	}

	public void remove(MovieDBItem item){
		Node<MovieDBItem> curr = head;
		while(curr.getNext() != null && curr.getNext().getItem().compareTo(item) < 0){
			curr = curr.getNext();
		}
		if(curr.getNext() != null && curr.getNext().getItem().equals(item)){
			curr.removeNext();
			numItems -= 1;
		}
	}

	@Override
	public MovieDBItem first() {
		return head.getNext().getItem();
	}
	@Override
	public void removeAll() {
		head.setNext(null);
	}
	@Override
	public Iterator<MovieDBItem> iterator() {
    	return new MyLinkedListIterator<MovieDBItem>(this);
    }
}
