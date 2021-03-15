package Iterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyIterator<R> implements Iterator<R>{
	
	private final List<R> myList = new ArrayList<R>();
	private int count = 0;
	/*
     * Abstraction function:
     * 		represents a iterator
     */

    /*
     * Representation invariant:
     * 		count is nonnegative and is less than or equals to myList's size
     * 		myList is not null
     */

    /*
     * Safety from rep exposure:
     * 		all fields are private or final 
     * 		all functions return an immutable type
     * 		the only one to return an entry is needed
     */
	private void checkRep() {
		assert myList != null;
		assert count >= 0;
		assert count <= myList.size();
	}
	/**
	 * Constructor for MyIterator
	 * @param list : which to iterate
	 */
	public MyIterator(List<R> list) {
		myList.addAll(list);
		checkRep();
	}
	/**
	 * Constructor for MyIterator
	 * @param list : which to iterate
	 */
	public MyIterator(R list) {
		myList.add(list);
		checkRep();
	}
	/**
	 * Constructor for MyIterator
	 * @param o1 : which to iterate
	 * @param o2 : which to iterate
	 */
	public MyIterator(R o1, R o2) {
		myList.add(o1);
		myList.add(o2);
		checkRep();
	}
	@Override
	public boolean hasNext() {
		return count < myList.size();
	}

	@Override
	public R next() {
		R ansR = myList.get(count);
		count++;
		checkRep();
		return ansR;
	}
	
}
