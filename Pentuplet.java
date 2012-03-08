///////////////////////////////////////////////////////////////////////////////
//ALL STUDENTS COMPLETE THESE SECTIONS
//Main Class File:  BayesNet.java & CBR.java
//File:             Pentuplet.java
//Semester:         Fall 2011
//
//Author:           Erin Rasmussen ejrasmussen2@wisc.edu
//CS Login:         rasmusse
//Lecturer's Name:  Jude Shavlik
//
//
////////////////////////////80 columns wide //////////////////////////////////
/**
  * This class stores 5 things: the 7 words and POS before the center word,
  * the center word, and the 7 words and POS after the center word.
  *
  * <p>Bugs: (none known)
  *
  * @author (Erin Rasmussen)
  */

public class Pentuplet {

	private DblLinkedList<String> firstHalfWords;
	private DblLinkedList<String> firstHalfLang;
	private DblLinkedList<String> secondHalfWords;
	private DblLinkedList<String> secondHalfLang;
	private String centerWord;
	
	public Pentuplet(DblLinkedList<String> firstHalfWords, DblLinkedList<String> firstHalfLang,
		DblLinkedList<String> secondHalfWords, DblLinkedList<String> secondHalfLang,
		String centerWord) {
		
		this.firstHalfWords = firstHalfWords;
		this.firstHalfLang = firstHalfLang;
		this.secondHalfWords = secondHalfWords;
		this.secondHalfLang = secondHalfLang;
		this.centerWord = centerWord;
	}
	
	public DblLinkedList<String> getFirstWord(){
		return this.firstHalfWords;
	}
	
	public DblLinkedList<String> getFirstLang(){
		return this.firstHalfLang;
	}
	
	public DblLinkedList<String> getSecondWord(){
		return this.secondHalfWords;
	}
	
	public DblLinkedList<String> getSecondLang(){
		return this.secondHalfLang;
	}
	
	public String getCenterWord(){
		return this.centerWord;
	}
	
}
