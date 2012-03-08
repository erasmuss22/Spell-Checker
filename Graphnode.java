import java.util.ArrayList;
///////////////////////////////////////////////////////////////////////////////
//ALL STUDENTS COMPLETE THESE SECTIONS
//Main Class File:  BayesNet.java & CBR.java
//File:             Graphnode.java
//Semester:         Fall 2011
//
//Author:           Erin Rasmussen  ejrasmussen2@wisc.edu
//CS Login:         rasmusse
//Lecturer's Name:  Jude Shavlik
////////////////////////////80 columns wide //////////////////////////////////
/**
 * This class represents a single graphnode for a graph which can store
 * its label, successors, parents, and other desired data
 * <p>Bugs: (none known)
 *
 * @author (Erin Rasmussen)
 */
public class Graphnode {
	private String label; // the word or POS
	private ArrayList<Graphnode> parents;
	private ArrayList<Graphnode> successors;
	private int count; // the total times the label is in the graph
	private double conditionalProb;
	private int totalCount; // the total word entries into the entire graph
	private int position; // the position of the word
	private double averagePos; // the average position of the word
	
	public Graphnode(String label, Graphnode parent){
		//to avoid p(word) = 0, once the graph is created, the denominator can 
		//be factored because the amount of words will be known
		this.label = label;
		count = 1;  
		parents = new ArrayList<Graphnode>();
		parents.add(parent);
		successors = new ArrayList<Graphnode>();
	}
	
	public Graphnode(String label, Graphnode parent, int position){
		//to avoid p(word) = 0, once the graph is created, the denominator can 
		//be factored because the amount of words will be known
		this.label = label;
		count = 1;  
		parents = new ArrayList<Graphnode>();
		parents.add(parent);
		successors = new ArrayList<Graphnode>();
		this.position = position;
	}
	
	// create the root
	public Graphnode (String label, double conditionalProb){
		this.label = label;
	    this.conditionalProb = conditionalProb;
		successors = new ArrayList<Graphnode>();
	}
	
	public void addChild(Graphnode e) {
		this.successors.add(e);
	}
	
	public void addTotalCount(int totalCount){
		this.totalCount = totalCount;
	}
	
	public void addCount(){
		count++;
	}
	
	public ArrayList<Graphnode> getParents(){
		return this.parents;
	}
	
	public ArrayList<Graphnode> getSuccessors(){
		return this.successors;
	}
	
	public void computeProb(int totalCounts){
		conditionalProb = (double)count  / (double) totalCounts;
	}
	
	public double getProb(){
		return this.conditionalProb;
	}
	
	public String getWord(){
		return this.label;
	}
	
	public int getTotalCount(){
		return this.totalCount;
	}
	
	public void setPosition(int position){
		this.position = this.position + position;
	}
	
	public void setAveragePosition(){
		this.averagePos = (double) this.position / (double) count;
	}
	
	public double getAvgPos(){
		return this.averagePos;
	}
	
	public int getPosition(){
		return this.position;
	}
}
