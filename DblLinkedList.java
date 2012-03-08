import java.util.Iterator;
///////////////////////////////////////////////////////////////////////////////
//ALL STUDENTS COMPLETE THESE SECTIONS
//Main Class File:  BayesNet.java & CBR.java
//File:             DblLinkedList.java
//Semester:         Fall 2011
//
//Author:           Erin Rasmussen ejrasmussen2@wisc.edu
//CS Login:         rasmusse
//Lecturer's Name:  Jude Shavlik
//
//!!!!!This is mostly recycled code from a data structure I coded for CS367
//
////////////////////////////80 columns wide //////////////////////////////////
/**
 * This class represents a list of doubly-linked nodes using the node's 
 * previous and next nodes along with a special iterator to perform various
 * operations
 *
 * <p>Bugs: (none known)
 *
 * @author (Erin Rasmussen)
 */
public class DblLinkedList<E> implements SimpleSet<E> {
	private DblLinkedNode<E> tail;
	private DblLinkedNode<E> head = null;
	private int numItems;
	private Iterator<E> iter;

	/**
	 * Create a new, empty list.
	 */
	public DblLinkedList() {
		tail = null;
		numItems = 0;
	}

	@Override
	public boolean add(E item, int position) {
		DblLinkedNode<E> newNode;
		if (numItems == 0){
			tail = new DblLinkedNode<E>(item, position);
			head = tail;
			numItems++;
			return true;
		}
		else {
			iter = iterator();
			newNode = new DblLinkedNode<E>(tail, item, null, position);
			tail.setNext(newNode);
			tail = newNode;
			numItems++;
		}
		return false;
	}

	@Override
	public boolean contains(E target) {
		iter = iterator();
		while (iter.hasNext()){
			if (target ==  iter.next()){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		iter = iterator();
		while (iter.hasNext()){
			return false;
		}
		return true;
	}

	@Override
	public Iterator<E> iterator() {
		return new DblLinkedListIterator<E>(head);
	}

	@Override
	public int size() {
		return numItems;
	}

	@Override
	public boolean remove(E item) {
		if (!contains(item) || isEmpty()){
			return false;
		}
		if (item.equals(head.getData())){
			if (head.equals(tail)){
				tail = null;
				head = null;
				numItems = 0;
				return true;
			}
			else if (head.getNext().equals(tail)){
				tail.setPrev(null);
				head = tail;
				numItems--;
				return true;
			}
			else {
				head = head.getNext();
				head.getNext().setPrev(null);
				head = head.getNext();
				numItems--;
				return true;
			}
		}
		else if (item.equals(tail.getData())){
			if (tail.equals(head)){
				tail = null;
				head = null;
				numItems = 0;
				return true;
			}
			else if (tail.getPrev().equals(head)){
				head.setNext(null);
				tail = null;
				numItems--;
				return true;
			}
			else {
				tail.getPrev().setNext(null);
				tail = tail.getPrev();
				numItems--;
				return true;
			}
		}
		else {
			iter = iterator();
			E curr;
			while (iter.hasNext()){
				curr = iter.next();
				if (item.equals(curr)){
					iter.remove();
					numItems--;
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void clear() {
		head = null;
		tail = null;
		if (head != (null)){
			head.setNext(null);
		}
		numItems = 0;
	}
}
