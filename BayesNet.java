/**
 * BayesNet.java
 * Original Author: Nick Bridle (nbridle@cs.wisc.edu)
 * (feel free to add your name to this header)
 * 
 * This is a starter file for the programming portion of HW3 for Professor Shavlik's
 * Fall 2011 class. It represents the beginning of a class that can perform two different
 * variations of Bayesian network inference for semantic spell checking.
 * 
 * Note: If you have any questions about this file or how to proceed, feel free to come see me.
 * Don't feel that you have to use this template if you don't want to; it's here for your convenience.
 */
///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            BayesNet HW3
// Files:            BayesNet.java, Graphnode.java, DblLinkedList.java,
//					 DblLinkedNode.java, DblLinkedListIterator.java, 
//					 SimpleSet.java, Pentuplet.java
// Semester:         Fall 2011
//
// Author:           Erin Rasmussen   ejrasmussen2@wisc.edu
// CS Login:         rasmusse
// Lecturer's Name:  Jude Shavlik
//
//////////////////////////// 80 columns wide //////////////////////////////////
import java.io.*;
import java.util.*;

public class BayesNet {
	public static void main(String[] args) {

		// Verify that the correct number of command-line arguments were passed
		//in
		if(args.length != 5) {
			System.err.println("usage: java BayesNet wordX wordY " +
			"fractionXoverY fileOfTrainingCases fileOfTestPhrases");
			System.exit(1);
		}

		// Initialize the input variables
		String wordX = args[0];
		String wordY = args[1];
		double ratioXOverY = Double.parseDouble(args[2]);
		double probability = ratioXOverY / (1 + ratioXOverY);
		Graphnode firstWord = new Graphnode(wordX, probability);
		Graphnode secondWord = new Graphnode(wordY, 1 - probability);
		Graphnode firstLang = new Graphnode(wordX, probability);
		Graphnode secondLang = new Graphnode(wordY, 1 - probability);
		//the following are the same as above, just for the 2nd function
		Graphnode firstWordBag = new Graphnode(wordX, probability);
		Graphnode secondWordBag = new Graphnode(wordY, 1 - probability);
		String trainFilename = args[3];
		String testFilename = args[4];
		int training = 0; // keep track of the training examples read in
		int testing = 0; // keep track of the testing examples read in
		DblLinkedList<String> firstHalfWords; //stores the first 7 words
		DblLinkedList<String> firstHalfLang; // stores the first 7 POS tags
		DblLinkedList<String> secondHalfWords; //stores the last 7 words
		DblLinkedList<String> secondHalfLang; // stores the last 7 POS tags
		String centerWord = null;
		ArrayList<Pentuplet> list = new ArrayList<Pentuplet>();
		ArrayList<Pentuplet> bagList = new ArrayList<Pentuplet>();
		ArrayList<Pentuplet> testList = new ArrayList<Pentuplet>();
		ArrayList<Pentuplet> bagTestList = new ArrayList<Pentuplet>();
		String[] wordList = new String[15];
		String[] langList = new String[15];
		// Open the training set and test file
		Scanner trainFile = null;
		Scanner testFile = null;
		try {
			trainFile = new Scanner(new File(trainFilename));
			testFile = new Scanner(new File(testFilename));
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		// Iterate through the lines in the training file
		while(trainFile.hasNextLine()) {
			String line = trainFile.nextLine();
			training++;
			int wordCount = 0;
			int langCount = 0;
			String temp;
			// st breaks the line into tokens based on white space
			// I made a boolean system to check if the items in the
			// brackets had been visited yet
			StringTokenizer st = new StringTokenizer(line);
			boolean leftBracket = false;
			boolean language = false;
			while (st.hasMoreTokens()) {
				temp = st.nextToken();
				// POS will be the first thing following the left bracket
				if (!language && leftBracket){
					language = true;
					langList[langCount] = temp;
					langCount++;
				}
				// this will store the word, or if the word has already
				// been stored, it will keep track of the bracket
				if (!leftBracket && !language){
					if (!temp.equals("[")){
						wordList[wordCount] = temp;
						wordCount++;
					}
					else {
						leftBracket = true;
					}
				}
				if (temp.equals("]")){
					leftBracket = false;
					language = false;
				}
			}
			firstHalfWords = new DblLinkedList<String>();
			firstHalfLang = new DblLinkedList<String>();
			secondHalfWords = new DblLinkedList<String>();
			secondHalfLang = new DblLinkedList<String>();
			// DblLinkedLists were made of the words before and after
			// the center word. 
			for (int i = 0; i < wordList.length; i++){
				if (i < 7){
					firstHalfWords.add(wordList[i], i + 1);
					firstHalfLang.add(langList[i], i + 1);
				}
				else if (i == 7){
					centerWord = wordList[i];
				}
				else {
					secondHalfWords.add(wordList[i], i);
					secondHalfLang.add(langList[i], i);
				}
			}
			// the following ArrayLists store each training example
			list.add(new Pentuplet(firstHalfWords, firstHalfLang, 
					secondHalfWords, secondHalfLang, centerWord));
			bagList.add(new Pentuplet(firstHalfWords, firstHalfLang, 
					secondHalfWords, secondHalfLang, centerWord));

		}

		// each training example gets put into a Naive Bayes Network using the
		// two different functions
		bagOfWords(bagList, firstWordBag, secondWordBag);
		creativeNaiveBayes(list, firstWord, secondWord, firstLang, secondLang);

		while(testFile.hasNextLine()) {
			// to deal with the 3rd line of white space, this line is
			// read, but ignored each time. Otherwise everything is the
			// same as the training read in
			for (int j = 0; j < 3; j++){
				if (j != 2){
					String line = testFile.nextLine();
					if (j != 1)
						testing++;
					int wordCount = 0;
					int langCount = 0;
					String temp;
					StringTokenizer st = new StringTokenizer(line);
					boolean leftBracket = false;
					boolean language = false;
					while (st.hasMoreTokens()) {
						temp = st.nextToken();
						if (!language && leftBracket){
							language = true;
							langList[langCount] = temp;
							langCount++;
						}
						if (!leftBracket && !language){
							if (!temp.equals("[")){
								wordList[wordCount] = temp;
								wordCount++;
							}
							else {
								leftBracket = true;
							}
						}
						if (temp.equals("]")){
							leftBracket = false;
							language = false;
						}
					}
					firstHalfWords = new DblLinkedList<String>();
					firstHalfLang = new DblLinkedList<String>();
					secondHalfWords = new DblLinkedList<String>();
					secondHalfLang = new DblLinkedList<String>();
					for (int i = 0; i < wordList.length; i++){
						if (i < 7){
							firstHalfWords.add(wordList[i], i);
							firstHalfLang.add(langList[i], i);
						}
						else if (i == 7){
							centerWord = wordList[i];
						}
						else {
							secondHalfWords.add(wordList[i], i);
							secondHalfLang.add(langList[i], i);
						}
					}
					testList.add(new Pentuplet(firstHalfWords, firstHalfLang, 
							secondHalfWords, secondHalfLang, centerWord));
					bagTestList.add(new Pentuplet(firstHalfWords, 
							firstHalfLang, secondHalfWords, secondHalfLang,
							centerWord));
				}
				else {
					if (testFile.hasNextLine()){
						@SuppressWarnings("unused")
						String line = testFile.nextLine();
					}
				}
			}

		}

		String[] result;
		double count = 0; 

		// here each test phrase is tested and compared to the answer. In
		// 'result', result[0] is the word Naive Bayes approximation, and
		// result[1] is the POS Naive Bayes approximation

		for (int i = 0; i < testList.size(); i += 2){
			result = computeNaiveBayesPosition(firstWord, secondWord, 
					firstLang, secondLang, testList.get(i), 
					testList.get(i + 1));
			if (result[0].equals(result[1]) && result[0].equals(testList.
					get(i).getCenterWord())){
				count++;
			}
			// if both word and POS don't agree, probability is used as a
			// tie-breaker
			else if (!result[0].equals(result[1])) {
				if (probability > (1 - probability)){
					result[0] = wordX;
				}
				else {
					result[0] = wordY;
				}
				if (result[0].equals(testList.get(i).getCenterWord())){
					count++;
				}
				// the incorrect phrases are printed out with their correct
				// center word
				else {
					Iterator<String> wordIter = testList.get(i).getFirstWord()
					.iterator();
					while (wordIter.hasNext()){
						System.out.print(wordIter.next() + " ");
					}
					System.out.print(testList.get(i).getCenterWord() + " ");
					wordIter = testList.get(i).getSecondWord().iterator();
					while(wordIter.hasNext()){
						System.out.print(wordIter.next() + " ");
					}
					System.out.println();
				}
			}
			// the incorrect phrases are printed out with their correct
			// center word
			else{
				Iterator<String> wordIter = testList.get(i).
				getFirstWord().iterator();
				while (wordIter.hasNext()){
					System.out.print(wordIter.next() + " ");
				}
				System.out.print(testList.get(i).getCenterWord() + " ");
				wordIter = testList.get(i).getSecondWord().iterator();
				while(wordIter.hasNext()){
					System.out.print(wordIter.next() + " ");
				}
				System.out.println();
			}
		}
		System.out.println(count / (double) testing);
		System.out.println();
		System.out.println("Basic Naive Bayes");
		count = 0; 
		String answer;
		// the whole process of computing the correct word is repeated for
		// the other NB function
		for (int i = 0; i < testList.size(); i += 2){
			answer = computeNaiveBayes(firstWord, secondWord, bagTestList.
					get(i), bagTestList.get(i + 1));
			if (answer.equals(bagTestList.get(i).getCenterWord())){
				count++;
			}
			else {
				Iterator<String> wordIter = bagTestList.get(i).
				getFirstWord().iterator();
				while (wordIter.hasNext()){
					System.out.print(wordIter.next() + " ");
				}
				System.out.print(bagTestList.get(i).getCenterWord() + " ");
				wordIter = bagTestList.get(i).getSecondWord().iterator();
				while(wordIter.hasNext()){
					System.out.print(wordIter.next() + " ");
				}
				System.out.println();
			}
		}
		System.out.println(count / (double) testing);
		System.out.println("Training read: " + training + " Testing Read: "
				+ testing);

	}


	/**
	 * This method creates a Naive Bayes network using the bag of words
	 * style where position does not matter. This style was chosen because
	 * it is simple and makes a good baseline to compare to. It also stores
	 * the probabilities of each word based on the total counts.
	 *
	 * @param (list) (The list of Pentuplets of training examples)
	 * @param (firstWord) (the root of wordX's Bayes' Net)
	 * @param (secondWord) (the root of wordY's Bayes' Net)
	 */
	@SuppressWarnings("unused")
	public static void bagOfWords(ArrayList<Pentuplet> list, Graphnode 
			firstWord, Graphnode secondWord){
		Iterator<Pentuplet> iter = list.iterator();
		Pentuplet temp; // each individual training example
		DblLinkedList<String> firstHalf;
		DblLinkedList<String> secondHalf;
		Graphnode child;
		Iterator<String> wordIter;
		String centerWord;
		String tempWord;
		boolean contains; // if the word is already in the graph, set to true
		int countX = 1; // starts at 1 to avoid probability of 0
		int countY = 1; // both store the total words in the amount of entries
		// into the graph
		while (iter.hasNext()){
			temp = iter.next();
			// if equal, works on wordX's graph, if not, works on wordY's
			if (temp.getCenterWord().equals(firstWord.getWord())){
				firstHalf = temp.getFirstWord();
				secondHalf = temp.getSecondWord();
				wordIter = firstHalf.iterator();
				while (wordIter.hasNext()){
					tempWord = wordIter.next();

					// for first time through graph
					if (firstWord.getSuccessors().size() == 0){
						child = new Graphnode(tempWord, firstWord);
						firstWord.addChild(child);
						countX++;
					}
					// all other times
					else {
						contains = false;
						for (int i = 0; i < firstWord.getSuccessors().size();
							i++){
							if (firstWord.getSuccessors().get(i).getWord().
									equals(tempWord)){
								firstWord.getSuccessors().get(i).addCount();
								contains = true;
								countX++; 
								break;
							}
						}
						if (!contains) {
							child = new Graphnode(tempWord, firstWord);
							firstWord.addChild(child);
							countX++;
						}
					}
				}
				// repeat for last 7 words in same manner
				wordIter = secondHalf.iterator();
				while (wordIter.hasNext()){
					tempWord = wordIter.next();

					// for first time through graph
					if (firstWord.getSuccessors().size() == 0){
						child = new Graphnode(tempWord, firstWord);
						firstWord.addChild(child);
						countX++;
					}
					// all other times
					else {
						contains = false;
						for (int i = 0; i < firstWord.getSuccessors().size();
							i++){
							if (firstWord.getSuccessors().get(i).getWord().
									equals(tempWord)){
								firstWord.getSuccessors().get(i).addCount();
								contains = true;
								countX++;
								break;
							}
						}
						if (!contains) {
							child = new Graphnode(tempWord, firstWord);
							firstWord.addChild(child);
							countX++;
						}
					}
				}

			}

			// same as above except with the other word
			else {
				firstHalf = temp.getFirstWord();
				secondHalf = temp.getSecondWord();
				wordIter = firstHalf.iterator();
				while (wordIter.hasNext()){
					tempWord = wordIter.next();

					// for first time through graph
					if (secondWord.getSuccessors().size() == 0){
						child = new Graphnode(tempWord, firstWord);
						secondWord.addChild(child);
						countY++;
					}
					// all other times
					else {
						contains = false;
						for (int i = 0; i < secondWord.getSuccessors().size();
							i++){
							if (secondWord.getSuccessors().get(i).getWord().
									equals(tempWord)){
								secondWord.getSuccessors().get(i).addCount();
								contains = true;
								countY++;
								break;
							}
						}
						if (!contains) {
							child = new Graphnode(tempWord, secondWord);
							secondWord.addChild(child);
							countY++;
						}
					}
				}
				wordIter = secondHalf.iterator();
				while (wordIter.hasNext()){
					tempWord = wordIter.next();

					// for first time through graph
					if (secondWord.getSuccessors().size() == 0){
						child = new Graphnode(tempWord, secondWord);
						secondWord.addChild(child);
						countY++;
					}
					// all other times
					else {
						contains = false;
						for (int i = 0; i < secondWord.getSuccessors().size();
							i++){
							if (secondWord.getSuccessors().get(i).getWord().
									equals(tempWord)){
								secondWord.getSuccessors().get(i).addCount();
								contains = true;
								countY++;
							}
						}
						if (!contains) {
							child = new Graphnode(tempWord, secondWord);
							secondWord.addChild(child);
							countY++;
						}
					}
				}
			}

		}
		// store the probabilities of each word by adjusting to avoid
		// a probability of 0. totalCountX is the denominator.
		int totalCountX = countX + firstWord.getSuccessors().size();
		firstWord.addTotalCount(totalCountX);
		int totalCountY = countY + secondWord.getSuccessors().size();
		secondWord.addTotalCount(totalCountY);
		for (int i = 0; i < firstWord.getSuccessors().size(); i++){
			firstWord.getSuccessors().get(i).computeProb(totalCountX);
		}

		for (int i = 0; i < secondWord.getSuccessors().size(); i++){
			secondWord.getSuccessors().get(i).computeProb(totalCountY);
		}
	}


	/**
	 * This method uses the ratio of probabilities between wordX and wordY to
	 * determine which word it should return
	 *
	 * @param (firstWord) (the completed NB graph for wordX)
	 * @param (secondWord) (the completed NB graph for wordY)
	 * @param (test1) (the first line of a phrase from the test file)
	 * @param (test2) (the second line of the same phrase with the other word)
	 * @return (the word predicted by the Bayes' net)
	 */
	public static String computeNaiveBayes(Graphnode firstWord, Graphnode 
			secondWord, Pentuplet test1, Pentuplet test2){		
		// prob of word X
		double probabilityX = calcProb(firstWord, test1);
		// prob of ~word X (which is word Y)
		double probabilityY = calcProb(secondWord, test2);
		double ratio = probabilityX / probabilityY;

		// if ratio is less than 1, that means wordY was stronger and so
		// we pick it
		if (ratio > 1)
			return firstWord.getWord();
		else
			return secondWord.getWord();
	}

	/**
	 * This method returns the probability of wordX or wordY given the
	 * words in the test phrase. It stores each probability in an array
	 * and then multiplies each at the end.
	 *
	 * @param (firstWord) (the graph of either wordX or wordY, the method
	 * started out in main and was moved to a method so the naming stayed)
	 * @param (test1) (the single test example to compare to)
	 * @return (the multiplied conditional probabilities along with the
	 * probability of the root node)
	 */
	@SuppressWarnings("unused")
	public static double calcProb(Graphnode firstWord, Pentuplet test1){
		DblLinkedList<String> firstHalf;
		DblLinkedList<String> secondHalf;
		double[] probs = new double[14]; // 7 words before center, 7 after
		int arrayCount = 0; // keeps track of the position in the array
		String centerWord;
		String temp;
		Iterator<String> wordIter;
		firstHalf = test1.getFirstWord();
		secondHalf = test1.getSecondWord();
		centerWord = test1.getCenterWord();
		wordIter = firstHalf.iterator();
		boolean found;
		// store the probabilities of the first 7 words in an array
		while (wordIter.hasNext()){
			temp = wordIter.next();
			found = false;
			for (int i = 0; i < firstWord.getSuccessors().size(); i++){
				if (firstWord.getSuccessors().get(i).getWord().equals(temp)){
					probs[arrayCount] = firstWord.getSuccessors().get(i).
						getProb();
					found = true;
					arrayCount++;
					break;
				}
			}
			// if the word isn't found, a minimal probability is used to
			// avoid a probability of zero.
			if (!found){
				probs[arrayCount] = 1 / (double) firstWord.getTotalCount();
				arrayCount++;
			}
		}
		// store the last 7 words in the array
		wordIter = secondHalf.iterator();
		while (wordIter.hasNext()){
			temp = wordIter.next();
			found = false;
			for (int i = 0; i < firstWord.getSuccessors().size(); i++){
				if (firstWord.getSuccessors().get(i).getWord().equals(temp)){
					probs[arrayCount] = firstWord.getSuccessors().get(i).
						getProb();
					found = true;
					arrayCount++;
					break;
				}
			}
			if (!found){
				probs[arrayCount] = 1 / (double) firstWord.getTotalCount();
				arrayCount++;
			}
		}
		double firstProbability = probs[0];

		// multiply the conditional probabilities
		for (int i = 1; i < probs.length; i++){
			firstProbability = firstProbability * probs[i];
		}
		// multiply the probability of the word
		firstProbability = firstProbability * firstWord.getProb();
		return firstProbability;
	}


	/**
	 * This method creates a Bayes' Net using an approach I learned in
	 * Computational Photography. For matching two photos, the sum of
	 * the squared differences of locations determined the best matching
	 * features in an image, so a similar method was applied using the
	 * average location of each word and POS.
	 *
	 * @param (list) (The list of Pentuplets of training examples)
	 * @param (firstWord) (the root of wordX's Bayes' Net)
	 * @param (secondWord) (the root of wordY's Bayes' Net)
	 * @param (firstLang) (the root of wordX's BN based on POS)
	 * @param (secondLang) (the root of wordY's BN based on POS)
	 */
	@SuppressWarnings("unused")
	public static void creativeNaiveBayes(ArrayList<Pentuplet> list, Graphnode
			firstWord, Graphnode secondWord,
			Graphnode firstLang, Graphnode secondLang){
		Iterator<Pentuplet> iter = list.iterator();
		Pentuplet temp;
		DblLinkedList<String> firstHalf;
		DblLinkedList<String> secondHalf;
		DblLinkedList<String> langFirst;
		DblLinkedList<String> langSecond;
		Graphnode child;
		Iterator<String> wordIter;
		Iterator<String> langIter;
		String centerWord;
		String tempWord;
		String tempLang;
		boolean contains;
		int countX = 1;  // stores the count of the example word
		int countY = 1;
		int countXLang = 1; // stores the count of the example POS
		int countYLang = 1;
		int countLang; // stores the position of the example POS
		int count; // stores the position of the example word
		while (iter.hasNext()){
			temp = iter.next();
			count = 1;
			countLang = 1;
			// calculate the probabilities based on if the example is wordX
			if (temp.getCenterWord().equals(firstWord.getWord())){
				firstHalf = temp.getFirstWord();
				secondHalf = temp.getSecondWord();
				langFirst = temp.getFirstLang();
				langSecond = temp.getSecondLang();
				wordIter = firstHalf.iterator();
				langIter = langFirst.iterator();
				while (wordIter.hasNext() && langIter.hasNext()){
					tempWord = wordIter.next();
					tempLang = langIter.next();
					// for first time through graph
					if (firstWord.getSuccessors().size() == 0){
						child = new Graphnode(tempWord, firstWord, count);
						firstWord.addChild(child);
						// each time a child is added, it's average position
						// is recalculated
						firstWord.getSuccessors().get(0).setAveragePosition();
						countX++;
						count++;
					}
					// all other times
					else {
						contains = false;
						for (int i = 0; i < firstWord.getSuccessors().size(); 
							i++){
							if (firstWord.getSuccessors().get(i).getWord().
									equals(tempWord) &&
									firstWord.getSuccessors().get(i).
									getAvgPos() < 7){
								firstWord.getSuccessors().get(i).addCount();
								// add to the total position stored by the node
								firstWord.getSuccessors().get(i).setPosition
									(count);
								// recalculate based on the total position and
								// total counts
								firstWord.getSuccessors().get(i).
									setAveragePosition();
								contains = true;
								countX++;
								count++;
								break;
							}
						}
						if (!contains) {
							child = new Graphnode(tempWord, firstWord, count);
							firstWord.addChild(child);
							firstWord.getSuccessors().get(firstWord.
									getSuccessors().size() - 1).
										setAveragePosition();
							countX++;
							// to get the true position of the word instead of
							// an array-type position [1,2...] vs [0,1...]
							if (count == 7){
								count = 9;
							}
							else {
								count++;
							}
						}
					}

					// repeat the above process, except only for POS
					if(firstLang.getSuccessors().size() == 0){
						child = new Graphnode(tempLang, firstLang, countLang);
						firstLang.addChild(child);
						firstLang.getSuccessors().get(0).setAveragePosition();
						countXLang++;
						countLang++;
					}
					else {
						contains = false;
						for (int i = 0; i < firstLang.getSuccessors().size(); 
							i++){
							if (firstLang.getSuccessors().get(i).getWord().
									equals(tempLang) && firstLang.
									getSuccessors().get(i).getAvgPos() < 7){
								firstLang.getSuccessors().get(i).addCount();
								firstLang.getSuccessors().get(i).setPosition
									(countLang);
								firstLang.getSuccessors().get(i).
									setAveragePosition();
								contains = true;
								countXLang++;
								countLang++;
								break;
							}
						}
						if (!contains) {
							child = new Graphnode(tempLang, firstLang, 
									countLang);
							firstLang.addChild(child);
							firstLang.getSuccessors().get(firstLang.
									getSuccessors().size() - 1).
										setAveragePosition();
							countXLang++;
							if (countLang == 7){
								countLang = 9;
							}
							else {
								countLang++;
							}
						}
					}

				}


				// repeat everything from above except only for the last 7
				// words
				wordIter = secondHalf.iterator();
				langIter = langSecond.iterator();
				while (wordIter.hasNext() && langIter.hasNext()){
					tempWord = wordIter.next();
					tempLang = langIter.next();
					// for first time through graph
					if (firstWord.getSuccessors().size() == 0){
						child = new Graphnode(tempWord, firstWord, count);
						firstWord.addChild(child);
						firstWord.getSuccessors().get(firstWord.getSuccessors()
								.size() - 1).setAveragePosition();
						countX++;
						count++;
					}
					// all other times
					else {
						contains = false;
						for (int i = 0; i < firstWord.getSuccessors().size(); 
							i++){
							if (firstWord.getSuccessors().get(i).getWord().
									equals(tempWord) &&
									firstWord.getSuccessors().get(i).
									getAvgPos() > 7){
								firstWord.getSuccessors().get(i).addCount();
								firstWord.getSuccessors().get(i).
									setAveragePosition();
								contains = true;
								countX++;
								count++;
								break;
							}
						}
						if (!contains) {
							child = new Graphnode(tempWord, firstWord, count);
							firstWord.addChild(child);
							firstWord.getSuccessors().get(firstWord.
									getSuccessors().size() - 1).
										setAveragePosition();
							countX++;
							if (count == 7){
								count = 9;
							}
							else {
								count++;
							}
						}
					}

					// repeat for POS, 2nd time through
					if(firstLang.getSuccessors().size() == 0){
						child = new Graphnode(tempLang, firstLang, countLang);
						firstLang.addChild(child);
						firstLang.getSuccessors().get(0).setAveragePosition();
						countXLang++;
						countLang++;
					}
					else {
						contains = false;
						for (int i = 0; i < firstLang.getSuccessors().size();
							i++){
							if (firstLang.getSuccessors().get(i).getWord().
									equals(tempLang) &&
									firstLang.getSuccessors().get(i).
										getAvgPos() > 7){
								firstLang.getSuccessors().get(i).addCount();
								firstLang.getSuccessors().get(i).setPosition
									(countLang);
								firstLang.getSuccessors().get(i).
									setAveragePosition();
								contains = true;
								countXLang++;
								countLang++;
								break;
							}
						}
						if (!contains) {
							child = new Graphnode(tempLang, firstLang, 
									countLang);
							firstLang.addChild(child);
							firstLang.getSuccessors().get(firstLang.
									getSuccessors().size() - 1).
										setAveragePosition();
							countXLang++;
							if (countLang == 7){
								countLang = 9;
							}
							else {
								countLang++;
							}
						}
					}

				}
			}

			// repeat everything from above except only for wordY
			else {
				firstHalf = temp.getFirstWord();
				secondHalf = temp.getSecondWord();
				langFirst = temp.getFirstLang();
				langSecond = temp.getSecondLang();
				langIter = langFirst.iterator();
				wordIter = firstHalf.iterator();
				while (wordIter.hasNext() && langIter.hasNext()){
					tempWord = wordIter.next();
					tempLang = langIter.next();
					// for first time through graph
					if (secondWord.getSuccessors().size() == 0){
						child = new Graphnode(tempWord, secondWord, count);
						secondWord.addChild(child);
						secondWord.getSuccessors().get(0).setAveragePosition();
						countY++;
						count++;
					}
					// all other times
					else {
						contains = false;
						for (int i = 0; i < secondWord.getSuccessors().size(); 
							i++){
							if (secondWord.getSuccessors().get(i).getWord().
									equals(tempWord) && secondWord.
									getSuccessors().get(i).getAvgPos() < 7){
								secondWord.getSuccessors().get(i).addCount();
								secondWord.getSuccessors().get(i).
									setAveragePosition();
								contains = true;
								countY++;
								count++;
								break;
							}
						}
						if (!contains) {
							child = new Graphnode(tempWord, secondWord, count);
							secondWord.addChild(child);
							secondWord.getSuccessors().get(secondWord.
									getSuccessors().size() - 1).
										setAveragePosition();
							countY++;
							if (count == 7){
								count = 9;
							}
							else {
								count++;
							}
						}
					}


					if(secondLang.getSuccessors().size() == 0){
						child = new Graphnode(tempLang, secondLang, countLang);
						secondLang.addChild(child);
						secondLang.getSuccessors().get(0).setAveragePosition();
						countYLang++;
						countLang++;
					}
					else {
						contains = false;
						for (int i = 0; i < secondLang.getSuccessors().size(); 
							i++){
							if (secondLang.getSuccessors().get(i).getWord().
									equals(tempLang) && secondLang.
									getSuccessors().get(i).getAvgPos() < 7){
								secondLang.getSuccessors().get(i).addCount();
								secondLang.getSuccessors().get(i).setPosition
									(countLang);
								secondLang.getSuccessors().get(i).
									setAveragePosition();
								contains = true;
								countYLang++;
								countLang++;
								break;
							}
						}
						if (!contains) {
							child = new Graphnode(tempLang, secondLang, 
									countLang);
							secondLang.addChild(child);
							secondLang.getSuccessors().get(secondLang.
									getSuccessors().size() - 1).
										setAveragePosition();
							countYLang++;
							if (countLang == 7){
								countLang = 9;
							}
							else {
								countLang++;
							}
						}
					}	

				}

				// last 7 words
				langIter = langSecond.iterator();
				wordIter = secondHalf.iterator();
				while (wordIter.hasNext() && langIter.hasNext()){
					tempWord = wordIter.next();
					tempLang = langIter.next();
					// for first time through graph
					if (secondWord.getSuccessors().size() == 0){
						child = new Graphnode(tempWord, secondWord, count);
						secondWord.addChild(child);
						countY++;
						count++;
					}
					// all other times
					else {
						contains = false;
						for (int i = 0; i < secondWord.getSuccessors().size();
						i++){
							if (secondWord.getSuccessors().get(i).getWord().
									equals(tempWord) &&
									secondWord.getSuccessors().get(i).
										getAvgPos() > 7){
								secondWord.getSuccessors().get(i).addCount();
								secondWord.getSuccessors().get(i).
									setAveragePosition();
								contains = true;
								countY++;
								count++;
							}
						}
						if (!contains) {
							child = new Graphnode(tempWord, secondWord, count);
							secondWord.addChild(child);
							secondWord.getSuccessors().get(secondWord.
									getSuccessors().size() - 1).
										setAveragePosition();
							countY++;
							if (count == 7){
								count = 9;
							}
							else {
								count++;
							}
						}
					}

					if(secondLang.getSuccessors().size() == 0){
						child = new Graphnode(tempLang, secondLang, countLang);
						secondLang.addChild(child);
						secondLang.getSuccessors().get(0).setAveragePosition();
						countYLang++;
						countLang++;
					}
					else {
						contains = false;
						for (int i = 0; i < secondLang.getSuccessors().size();
							i++){
							if (secondLang.getSuccessors().get(i).getWord().
									equals(tempLang) &&
									secondLang.getSuccessors().get(i).
										getAvgPos() > 7){
								secondLang.getSuccessors().get(i).addCount();
								secondLang.getSuccessors().get(i).setPosition
									(countLang);
								secondLang.getSuccessors().get(i).
									setAveragePosition();
								contains = true;
								countYLang++;
								countLang++;
								break;
							}
						}
						if (!contains) {
							child = new Graphnode(tempLang, secondLang, 
									countLang);
							secondLang.addChild(child);
							secondLang.getSuccessors().get(secondLang.
									getSuccessors().size() - 1).
										setAveragePosition();
							countYLang++;
							if (countLang == 7){
								countLang = 9;
							}
							else {
								countLang++;
							}
						}
					}
				}
			}
			// calculate the probabilities of the words and POS's
			int totalCountX = countX + firstWord.getSuccessors().size();
			firstWord.addTotalCount(totalCountX);
			int totalCountY = countY + secondWord.getSuccessors().size();
			secondWord.addTotalCount(totalCountY);
			for (int i = 0; i < firstWord.getSuccessors().size(); i++){
				firstWord.getSuccessors().get(i).computeProb(totalCountX);
			}

			for (int i = 0; i < secondWord.getSuccessors().size(); i++){
				secondWord.getSuccessors().get(i).computeProb(totalCountY);
			}

			int totalCountLangX = countXLang + firstLang.getSuccessors().
				size();
			firstLang.addTotalCount(totalCountLangX);
			int totalCountLangY = countYLang + secondLang.getSuccessors().
				size();
			secondLang.addTotalCount(totalCountLangY);
			for (int i = 0; i < firstLang.getSuccessors().size(); i++){
				firstLang.getSuccessors().get(i).computeProb(totalCountLangX);
			}

			for (int i = 0; i < secondLang.getSuccessors().size(); i++){
				secondLang.getSuccessors().get(i).computeProb(totalCountLangY);
			}
		}


	}

	/**
	 * This method returns the probability of wordX or wordY given the
	 * words in the test phrase. It stores each probability in an array
	 * and then multiplies each at the end. It also returns the probability
	 * of the words based on the POS. it also only uses the 3 words before
	 * and 3 words after the center word.
	 *
	 * @param (firstWord) (the graph of either wordX or wordY, the method
	 * started out in main and was moved to a method so the naming stayed)
	 * @param (lang) (the graph of the POS for wordX or wordY)
	 * @param (test1) (the single test example to compare to)
	 * @return (an array of doubles of length 2 where the first position
	 * is the prob of the word, and the second is the prob of POS)
	 */
	@SuppressWarnings("unused")
	public static double[] calcProbPosition(Graphnode firstWord, Graphnode 
			lang, Pentuplet test1){
		DblLinkedList<String> firstHalf;
		DblLinkedList<String> secondHalf;
		DblLinkedList<String> firstLang;
		DblLinkedList<String> secondLang;
		double[] probs = new double[6];
		double[] probsLang = new double[6];
		int arrayCount = 0; // keeps track of the position in the word array
		int langArrayCount = 0; //tracks position of POS array
		String centerWord;
		String temp;
		String tempLang;
		Iterator<String> wordIter;
		Iterator<String> langIter;
		firstHalf = test1.getFirstWord();
		secondHalf = test1.getSecondWord();
		firstLang = test1.getFirstLang();
		secondLang = test1.getSecondLang();
		centerWord = test1.getCenterWord();
		wordIter = firstHalf.iterator();
		langIter = firstLang.iterator();
		int count = 1;
		int langCount = 1;
		int unmatchedWords = 0;
		double distanceWeight;  // the sum of the squared differences
		boolean found;
		while (wordIter.hasNext() && langIter.hasNext()){
			// this conditional is used so only the 3 words before the center
			// word are examined
			if (count < 5){
				tempLang = langIter.next();
				temp = wordIter.next();
				count++;
				langCount++;
			}
			else {
				tempLang = langIter.next();
				temp = wordIter.next();
				found = false;
				for (int i = 0; i < firstWord.getSuccessors().size(); i++){
					if (firstWord.getSuccessors().get(i).getWord().equals(temp)
							&& firstWord.getSuccessors().get(i).getAvgPos() 
								< 7){
						distanceWeight = Math.sqrt(Math.pow(count - 
								firstWord.getSuccessors().get(i).
									getAvgPos(), 2));
						// the distance weight acts as a penalty
						if (distanceWeight >= 1){
							probs[arrayCount] = (1 / distanceWeight) * 
							firstWord.getSuccessors().get(i).getProb();
							found = true;
							arrayCount++;
							count++;
							break;
						}
						// if the word is within 1 word of the average position
						// of that word, it receives no penalty
						else{
							probs[arrayCount] = firstWord.getSuccessors().
								get(i).getProb();
							found = true;
							arrayCount++;
							count++;
							break;
						}
					}
				}
				// if the word isn't found assign it a minimal probability
				if (!found){
					probs[arrayCount] = 1 / (double) firstWord.getTotalCount();
					arrayCount++;
					count++;
					unmatchedWords++;
				}
				// do the same for POS
				found = false;
				for (int i = 0; i < lang.getSuccessors().size(); i++){
					if (lang.getSuccessors().get(i).getWord().equals(tempLang)
							&& lang.getSuccessors().get(i).getAvgPos() < 7){
						distanceWeight = Math.sqrt(Math.pow(langCount - 
								lang.getSuccessors().get(i).getAvgPos(), 2));
						if (distanceWeight >= 1){
							probsLang[langArrayCount] = (1 / distanceWeight) 
								* lang.getSuccessors().get(i).getProb();
						}
						else{
							probsLang[langArrayCount] = lang.getSuccessors().
								get(i).getProb();
						}
						found = true;
						langArrayCount++;
						langCount++;
						break;
					}
				}
				if (!found){
					probsLang[langArrayCount] = 1 / (double) lang.
						getTotalCount();
					langArrayCount++;
					langCount++;
				}
			}
		}


		// repeat this all for the second half
		wordIter = secondHalf.iterator();
		langIter = secondLang.iterator();
		count = 9;
		langCount = 9;
		while (wordIter.hasNext() && langIter.hasNext()){
			if (count > 11){
				tempLang = langIter.next();
				temp = wordIter.next();
				count++;
				langCount++;
			}
			else {
				temp = wordIter.next();
				tempLang = langIter.next();
				found = false;
				for (int i = 0; i < firstWord.getSuccessors().size(); i++){
					if (firstWord.getSuccessors().get(i).getWord().equals(temp)
							&& firstWord.getSuccessors().get(i).getAvgPos() 
								> 7){
						distanceWeight = Math.sqrt(Math.pow(count - firstWord.
								getSuccessors().get(i).getAvgPos(), 2));
						if (distanceWeight >= 1)
							probs[arrayCount] = (1 / distanceWeight) * 
								firstWord.getSuccessors().get(i).getProb();
						else
							probs[arrayCount] = firstWord.getSuccessors().
								get(i).getProb();
						found = true;
						arrayCount++;
						count++;
						break;
					}
				}
				if (!found){
					probs[arrayCount] = 1 / (double) firstWord.getTotalCount();
					arrayCount++;
					count++;
					unmatchedWords++;
				}

				found = false;
				for (int i = 0; i < lang.getSuccessors().size(); i++){
					if (lang.getSuccessors().get(i).getWord().equals(temp) &&
							lang.getSuccessors().get(i).getAvgPos() > 7){
						distanceWeight = Math.sqrt(Math.pow(langCount - lang.
								getSuccessors().get(i).getAvgPos(), 2));
						if (distanceWeight >= 1){
							probsLang[langArrayCount] = (1 / distanceWeight) 
								* lang.getSuccessors().get(i).getProb();
						}
						else{
							probsLang[langArrayCount] = lang.getSuccessors().
							get(i).getProb();
						}
						found = true;
						langArrayCount++;
						langCount++;
						break;
					}
				}
				if (!found){
					probsLang[langArrayCount] = 1 / (double) lang.
						getTotalCount();
					langArrayCount++;
					langCount++;
				}
			}
		}
		// since the probabilities are very small, use logs to normalize them
		// to 1
		double firstProbability = Math.log(probs[0]) / Math.log(2);
		double langProbability = probsLang[0];
		// multiply the conditional probabilities
		for (int i = 1; i < probs.length; i++){
			firstProbability = firstProbability * (Math.log(probs[i]) / 
					Math.log(2));
			langProbability = langProbability * (Math.log(probsLang[0]) / 
					Math.log(2));
		}
		firstProbability = firstProbability * firstWord.getProb();
		langProbability = langProbability * lang.getProb();
		double[] prob = new double[3];
		prob[0] = firstProbability;
		prob[1] = langProbability;
		prob[2] = unmatchedWords;  // the amount of unmatched words, used as an
		return prob;				// experiment
	}

	/**
	 * This method uses the ratio of probabilities between wordX and wordY to
	 * determine which word it should return. Multiple tests were run to find
	 * the best combination of data to use, however it was found that using
	 * only POS returned the best results.
	 *
	 * @param (firstWord) (the completed NB graph for wordX)
	 * @param (secondWord) (the completed NB graph for wordY)
	 * @param (lang1) (the completed NB graph of POS for wordX)
	 * @param (lang2) (the completed NB graph of POS for wordY)
	 * @param (test1) (the first line of a phrase from the test file)
	 * @param (test2) (the second line of the same phrase with the other word)
	 * @return (the two words predicted by the BN, however only one is used
	 * and the rest were left just for trying different ratio combinations
	 * for better results)
	 */
	@SuppressWarnings("unused")
	public static String[] computeNaiveBayesPosition(Graphnode firstWord, 
			Graphnode secondWord, Graphnode lang1, Graphnode lang2, 
				Pentuplet test1, Pentuplet test2){

		String[] answer = new String[3];
		// prob of word X
		double[] probabilityX = calcProbPosition(firstWord, lang1, test1);
		// prob of ~word X (which is word Y)
		double[] probabilityY = calcProbPosition(secondWord, lang2, test2);

		double ratio = probabilityX[0] / probabilityY[0];
		double langRatio = probabilityX[1] / probabilityY[1];
		// if ratio is greater than 1, that means wordY was stronger and so
		// we pick it
		if (langRatio > 1){
			answer[0] = firstWord.getWord();
			answer[1] = firstWord.getWord();
		}
		else{
			answer[0] = secondWord.getWord();
			answer[1] = secondWord.getWord();

		}

		return answer;
	}

	/** 
	 * In some Java IDE's, the console window can close up before you get a 
	 * chance to read it, so this method can be used to wait until you're 
	 * ready to proceed.
	 * @param msg the message to display before waiting
	 */                                                                              
	public static void waitHere(String msg)
	{
		System.out.print("\n" + msg);
		try { System.in.read(); }
		catch(Exception e) {} // Ignore any errors while reading.                                                                                       
	}

}
