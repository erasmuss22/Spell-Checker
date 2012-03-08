///////////////////////////////////////////////////////////////////////////////
//ALL STUDENTS COMPLETE THESE SECTIONS
//Main Class File:  BayesNet.java & CBR.java
//File:             SimpleSet.java
//Semester:         Fall 2011
//
//Author:           Erin Rasmussen ejrasmussen2@wisc.edu
//CS Login:         rasmusse
//Lecturer's Name:  Jude Shavlik
//
//!!!!!This is mostly recycled code from a data structure I coded for CS367
// specifically an interface designed for a music player search.
////////////////////////////80 columns wide //////////////////////////////////
/**
 * Public interface to abstract data types representing sets of values.
 * <p>
 * <strong>Do not modify this file in any way!</strong>
 * 
 * @author Ben Liblit
 * @param <E>
 *            the type of data to be stored in this set
 **/

public interface SimpleSet<E> extends Iterable<E> {

	/**
	 * Adds the given item to this set if it is not already present.
	 * 
	 * @param item
	 *            reference to the item to be added
	 * @return true if the given item was added, false if it's already present
	 **/
	public boolean add(E item, int position);

	/**
	 * Removes the given item from this set if it is present.
	 * 
	 * @param item
	 *            reference to the item to be removed
	 * @return true if the given item was removed, false otherwise
	 **/
	public boolean remove(E item);

	/**
	 * Determines if this set contains the given item.
	 * 
	 * @param item
	 *            reference to the item to be found
	 * @return true if the given item is in the set, false otherwise
	 **/
	public boolean contains(E item);

	/**
	 * Removes all of the items from this set.
	 **/
	public void clear();

	/**
	 * Returns the size of this set, i.e., the number of items it contains.
	 * 
	 * @return the number of items in this set
	 **/
	public int size();

	/**
	 * Determines if this set is empty, i.e., contains no items.
	 * 
	 * @return true of the set is empty, false otherwise
	 **/
	public boolean isEmpty();
}
