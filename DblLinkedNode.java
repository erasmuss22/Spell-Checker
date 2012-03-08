///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  BayesNet.java & CBR.java
// File:             DblLinkedNode.java
// Semester:         Fall 2011
//
// Author:           Erin Rasmussen ejrasmussen2@wisc.edu
// CS Login:         rasmusse
// Lecturer's Name:  Jude Shavlik
//
// !!!!!This is mostly recycled code from a data structure I coded for CS367
//
//////////////////////////// 80 columns wide //////////////////////////////////
/**
  * This class represents one node in a doubly-linked list. It stores the next
  * node and the previous node along with its data.
  *
  * <p>Bugs: (a list of bugs and other problems)
  *
  * @author (Erin Rasmussen)
  */
public class DblLinkedNode<E> {

	    private E data;
	    @SuppressWarnings("unused")
		private int position;
	    private DblLinkedNode<E> next;
	    private DblLinkedNode<E> prev;
	    
	    public DblLinkedNode(final DblLinkedNode<E> prev, final E data, final DblLinkedNode<E> next, int position) {
	        this.data = data;
	        this.next = next;
	        this.prev = prev;
	        this.position = position;
	    }

	    public DblLinkedNode(final E data, int position) {
	        this(null, data, null, position);
	        this.position = position;
	    }

	    public E getData() {
	        return data;
	    }

	    public DblLinkedNode<E> getNext() {
	        return next;
	    }

	    public DblLinkedNode<E> getPrev() {
	        return prev;
	    }

	    public void setData(final E data) {
	        this.data = data;
	    }

	    public void setNext(final DblLinkedNode<E> next) {
	        this.next = next;
	    }

	    public void setPrev(final DblLinkedNode<E> prev) {
	        this.prev = prev;
	    }

	    /**
	     * Returns a string representation of this node's data, or "(null)" if this
	     * node's data is {@code null}.
	     * 
	     * @return the node's data as a string
	     */
	    @Override
	    public String toString() {
	        return data == null ? "(null)" : data.toString();
	    }
	
}
