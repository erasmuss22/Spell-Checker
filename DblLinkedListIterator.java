import java.util.Iterator;
import java.util.NoSuchElementException;
///////////////////////////////////////////////////////////////////////////////
//ALL STUDENTS COMPLETE THESE SECTIONS
//Main Class File:  BayesNet.java & CBR.java
//File:             DblLinkedListIterator.java
//Semester:         Fall 2011
//
//Author:           Erin Rasmussen ejrasmussen2@wisc.edu
//CS Login:         rasmusse
//Lecturer's Name:  Jude Shavlik
//
//!!!!!This is mostly recycled code from a data structure I coded for CS367
// specifically a music player search system that iterated through songs,
// genres, and artists
//
////////////////////////////80 columns wide //////////////////////////////////
/**
 * Implementation of the {@link Iterator} interface for use with
 * {@link LinkedSet}.
 * <p>
 * <strong>Modify this class to implement the required {@link Iterator} methods
 * along with any constructors, fields, or other methods you feel are
 * necessary.</strong>
 * 
 * @author Ben Liblit
 * @param <E>
 *            the type of data stored in the list
 */
public class DblLinkedListIterator<E> implements Iterator<E> {
	private DblLinkedNode<E> curr;
	private DblLinkedNode<E> prev;


	DblLinkedListIterator(DblLinkedNode<E> head){
		curr = head;
	}

	@Override
	public boolean hasNext() {
		return curr != null;
	}

	public E next() {
		if (!hasNext()){
			throw new NoSuchElementException();
		}
		E stuff = curr.getData();
		prev = curr;
		curr = curr.getNext();
		return stuff;
	}

	public void remove() {
		prev = prev.getPrev();
		prev.setNext(curr);
		curr.setPrev(prev);
	}
}
