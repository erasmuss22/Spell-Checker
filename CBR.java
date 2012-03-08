/**
 * CBR.java
 * Original Author: Nick Bridle (nbridle@cs.wisc.edu)
 * (feel free to add your name to this header)
 * 
 * This is a starter file for the programming portion of HW3 for Professor Shavlik's
 * Fall 2011 class. It represents the beginning of a class that can perform two different
 * variations of case-based reasoning for semantic spell checking.
 * 
 * Note: If you have any questions about this file or how to proceed, feel free to come see me.
 * Don't feel that you have to use this template if you don't want to; it's here for your convenience.
 */
///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            CBR HW3
// Files:            CBR.java, Graphnode.java, DblLinkedList.java,
//					 DblLinkedNode.java, DblLinkedListIterator.java, 
//					 SimpleSet.java, Pentuplet.java
// Semester:         Fall 2012
//
// Author:           Erin Rasmussen   ejrasmussen2@wisc.edu
// CS Login:         rasmusse
// Lecturer's Name:  Jude Shavlik
//
//////////////////////////// 80 columns wide //////////////////////////////////
import java.io.*;
import java.util.*;

public class CBR {
	public static void main(String[] args) {

		// Verify that the correct number of command-line arguments were passed in
		if(args.length != 5) {
			System.err.println("usage: java CBR wordX wordY fractionXoverY " +
					"fileOfTrainingCases fileOfTestPhrases");
			System.exit(1);
		}

		// Initialize the input variables
		String wordX = args[0];
		String wordY = args[1];
		@SuppressWarnings("unused")
		double ratioXOverY = Double.parseDouble(args[2]);
		String trainFilename = args[3];
		String testFilename = args[4];
		DblLinkedList<String> firstHalfWords;
		DblLinkedList<String> firstHalfLang;
		DblLinkedList<String> secondHalfWords;
		DblLinkedList<String> secondHalfLang;
		DblLinkedList<String> firstHalfWordsBasic;
		DblLinkedList<String> firstHalfLangBasic;
		DblLinkedList<String> secondHalfWordsBasic;
		DblLinkedList<String> secondHalfLangBasic;
		String centerWord = null;
		int training = 0;
		int testing = 0;
		ArrayList<Pentuplet> list = new ArrayList<Pentuplet>();
		ArrayList<Pentuplet> testList = new ArrayList<Pentuplet>();
		ArrayList<Pentuplet> listBasic = new ArrayList<Pentuplet>();
		ArrayList<Pentuplet> testListBasic = new ArrayList<Pentuplet>();
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
		// This implementation works the same as in the BayesNet.java by taking
		// tokens of the input files and storing them according to a boolean
		// pattern
		while(trainFile.hasNextLine()) {
			String line = trainFile.nextLine();
			training++;
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
			firstHalfWordsBasic = new DblLinkedList<String>();
			firstHalfLangBasic = new DblLinkedList<String>();
			secondHalfWordsBasic = new DblLinkedList<String>();
			secondHalfLangBasic = new DblLinkedList<String>();
			for (int i = 0; i < wordList.length; i++){
				if (i < 7){
					firstHalfWords.add(wordList[i], i + 1);
					firstHalfLang.add(langList[i], i + 1);
					firstHalfWordsBasic.add(wordList[i], i + 1);
					firstHalfLangBasic.add(langList[i], i + 1);
				}
				else if (i == 7){
					centerWord = wordList[i];
				}
				else {
					secondHalfWords.add(wordList[i], i);
					secondHalfLang.add(langList[i], i);
					secondHalfWordsBasic.add(wordList[i], i);
					secondHalfLangBasic.add(langList[i], i);
				}
			}
			list.add(new Pentuplet(firstHalfWords, firstHalfLang,
					secondHalfWords, secondHalfLang, centerWord));
			listBasic.add(new Pentuplet(firstHalfWordsBasic, 
					firstHalfLangBasic, secondHalfWordsBasic, 
						secondHalfLangBasic, centerWord));


			//System.out.println(line);
		}

		// The test file is read in the same way except that it loops twice to
		// get the 2 input lines, and once more to read the blank line to avoid
		// an end of file error
		while(testFile.hasNextLine()) {
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
					firstHalfWordsBasic = new DblLinkedList<String>();
					firstHalfLangBasic = new DblLinkedList<String>();
					secondHalfWordsBasic = new DblLinkedList<String>();
					secondHalfLangBasic = new DblLinkedList<String>();
					for (int i = 0; i < wordList.length; i++){
						if (i < 7){
							firstHalfWords.add(wordList[i], i);
							firstHalfLang.add(langList[i], i);
							firstHalfWordsBasic.add(wordList[i], i);
							firstHalfLangBasic.add(langList[i], i);
						}
						else if (i == 7){
							centerWord = wordList[i];
						}
						else {
							secondHalfWords.add(wordList[i], i);
							secondHalfLang.add(langList[i], i);
							secondHalfWordsBasic.add(wordList[i], i);
							secondHalfLangBasic.add(langList[i], i);
						}
					}
					testList.add(new Pentuplet(firstHalfWords, 
							firstHalfLang, secondHalfWords, secondHalfLang,
								centerWord));
					testListBasic.add(new Pentuplet(firstHalfWordsBasic, 
							firstHalfLangBasic, secondHalfWordsBasic, 
								secondHalfLangBasic, centerWord));
				}
				else {
					if (testFile.hasNextLine()){
						@SuppressWarnings("unused")
						String line = testFile.nextLine();
					}
				}
			}
		}

		ArrayList<ArrayList<double[]>> results = new ArrayList<ArrayList
			<double[]>>();
		String answer;
		int count = 0;
		// iterating through each test example and comparing it to it's
		// neighbors
		for (int i = 0; i < testList.size(); i++){
			results.add(creativeCBR(list, testList.get(i)));
		}
		for (int i = 0; i < testList.size(); i += 2){
			answer = processCBR(results, list, wordX, wordY, testList.get(i),
					testList.get(i + 1), i);
			if (answer.equals(testList.get(i).getCenterWord())){
				count++;
			}
			// an incorrect answer has the right answer printed out in the
			// entire phrase
			else {
				Iterator<String> iter2 = testList.get(i).getFirstWord().
					iterator();
				while (iter2.hasNext()){
					System.out.print(iter2.next() + " ");
				}
				System.out.print(testList.get(i).getCenterWord() + " ");
				iter2 = testList.get(i).getSecondWord().iterator();
				while(iter2.hasNext()){
					System.out.print(iter2.next() + " ");
				}
				System.out.println();
			}
		}
		System.out.println((double) count / (double) testing);
		System.out.println();
		
		// This portion does the same as above, only with the other CBR method
		System.out.println("Basic CBR");
		results = new ArrayList<ArrayList<double[]>>();
		count = 0;
		for (int i = 0; i < testList.size(); i++){
			results.add(basicCBR(list, testListBasic.get(i)));
		}
		for (int i = 0; i < testList.size(); i += 2){
			answer = processCBR(results, listBasic, wordX, wordY, 
					testListBasic.get(i), testListBasic.get(i + 1), i);
			if (answer.equals(testListBasic.get(i).getCenterWord())){
				count++;
			}
			else {
				Iterator<String> iter2 = testList.get(i).getFirstWord().
					iterator();
				while (iter2.hasNext()){
					System.out.print(iter2.next() + " ");
				}
				System.out.print(testListBasic.get(i).getCenterWord() + " ");
				iter2 = testListBasic.get(i).getSecondWord().iterator();
				while(iter2.hasNext()){
					System.out.print(iter2.next() + " ");
				}
				System.out.println();
			}
		}
		System.out.println((double) count / (double) testing);
		System.out.println("Training read: " + training + " Testing Read: "
				+ testing);
	}

	
	/**
	  * This method performs the nearest neighbor test on each test example
	  * by using the 4 nearest neighbors to the left and right of the
	  * center word. It uses both words and POS.
	  *
	  * @param (training) (the list of every training example to compare to)
	  * @param (test) (a single test example that is controlled by iteration
	  * in main())
	  * @return (a list of double[] where double[0] is the score for words
	  * and double[1] is the score for POS)
	  */
	
	public static ArrayList<double[]> creativeCBR(ArrayList<Pentuplet> training,
			Pentuplet test){
		Iterator<Pentuplet> iter = training.iterator();
		DblLinkedList<String> firstWord;
		DblLinkedList<String> secondWord;
		DblLinkedList<String> firstLang;
		DblLinkedList<String> secondLang;
		ArrayList<double[]> list = new ArrayList<double[]>();
		Pentuplet temp;
		double[][] distance; //the first row stores word distance,
		double wordDistance; // the seoncd stores POS distance
		double langDistance; // wordDistance andlangDistance are the distance
		Iterator<String> wordIter; // the test case is from the train cases
		Iterator<String> langIter;
		Iterator<String> testIterWord;
		Iterator<String> testIterLang;
		String tempWord;
		String tempLang;
		String testWord;
		String testLang;
		int testPosition;	
		int trainPosition;
		int arrayPos;	// keeps track of where to store the next element
		int arrayPosLang;
		boolean foundWord;
		boolean foundLang;
		// iterate through all the train examples
		while (iter.hasNext()){
			foundWord = false;
			foundLang = false;
			arrayPos = 0;
			arrayPosLang = 0;
			langDistance = 0;
			wordDistance = 0;
			distance = new double[2][8];
			testPosition = 1;
			temp = iter.next();
			testIterWord = test.getFirstWord().iterator();
			testIterLang = test.getFirstLang().iterator();
			firstWord = temp.getFirstWord();
			firstLang = temp.getFirstLang();
			while (testIterWord.hasNext() && testIterLang.hasNext()){
				trainPosition = 1;
				testWord = testIterWord.next();
				testLang = testIterLang.next();
				// since only the four nearest neighbors are used, a few
				// iterations must run
				if (testPosition > 3 && testPosition < 12){
					wordIter = firstWord.iterator();
					langIter = firstLang.iterator();
					foundWord = false;
					foundLang = false;
					while (wordIter.hasNext() && langIter.hasNext()){
						tempWord = wordIter.next();
						tempLang = langIter.next();
						if (trainPosition > 3 && trainPosition < 12){
							if (testWord.equals(tempWord)){
								//If there are two of the same words in the phrase,
								//this will save the closest to center
								distance[0][arrayPos] = Math.sqrt(Math.pow
										(trainPosition - testPosition, 2));
								arrayPos++;
								foundWord = true;
							}
							// do the same as well for POS
							if (testLang.equals(tempLang)){
								distance[1][arrayPosLang] = Math.sqrt(Math.pow
										(trainPosition - testPosition, 2));
								arrayPosLang++;
								foundLang = true;
							}
						}
						trainPosition++;
					}
					if (!foundWord){
						distance[0][arrayPos] = 10; // Greater than max distance
						arrayPos++;					// from center word as penalty
						foundWord = true;
					}
					if (!foundLang){
						distance[1][arrayPosLang] = 10;
						arrayPosLang++;
						foundLang = true;
					}
					if (foundLang && foundWord){
						foundLang = false;
						foundWord = false;
						break;
					}
				}
				testPosition++;
			}
			// do the above for the 4 words after the center word
			foundWord = false;
			foundLang = false;
			testIterWord = test.getSecondWord().iterator();
			testIterLang = test.getSecondLang().iterator();
			secondWord = temp.getSecondWord();
			secondLang = temp.getSecondLang();
			testPosition = 8;
			trainPosition = 8;
			while (testIterWord.hasNext() && testIterLang.hasNext()){
				wordIter = secondWord.iterator();
				langIter = secondLang.iterator();
				testWord = testIterWord.next();
				testLang = testIterLang.next();
				while (wordIter.hasNext() && langIter.hasNext()){
					tempWord = wordIter.next();
					tempLang = langIter.next();
					if (testPosition < 12 && trainPosition < 12){
						if (testWord.equals(tempWord)){
							//If there are two of the same words in the phrase,
							//this will save the closest to center
							distance[0][arrayPos] = Math.sqrt(Math.
									pow(trainPosition - testPosition, 2));
							arrayPos++;
							foundWord = true;
						}
						if (testLang.equals(tempLang)){
							distance[1][arrayPosLang] = Math.sqrt(Math.
									pow(trainPosition - testPosition, 2));
							arrayPosLang++;
							foundLang = true;
						}
						trainPosition++;

						if (!foundWord){
							distance[0][arrayPos] = 10; // Greater than max 
							arrayPos++;					//distance from center word
						}
						if (!foundLang){
							distance[1][arrayPosLang] = 10;
							arrayPosLang++;
						}
					}
				}
				testPosition++;
			}

			//sum up the total distances for the total score
			for (int i = 0; i < distance[0].length; i++){
				wordDistance = wordDistance + distance[0][i];
				langDistance = langDistance + distance[1][i];
			}
			// store the total distances for word and POS in a small
			// array of length 2
			double[] array = new double[2];
			array[0] = wordDistance;
			array[1] = langDistance;
			list.add(array);
		}
		return list;
	}

	
	/**
	  * The method takes the testing examples and decides which word to return
	  * based on the previous distance calculations. It takes the minimum 5
	  * nearest neighbors and takes a vote according to their returned word.
	  *
	  * @param (results) (list of lists that have score of each training example
	  * for each test examples)
	  * @param (trainList) (used to obtain the center word for each train example
	  * since only scores of those were passed in)
	  * @param (wordX) (the first word, used for comparrison)
	  * @param (wordY) (the second word, used for comparrison)
	  * @param (test1) (the first phrase of the test example)
	  * @param (test2) (the second phrase, used in case of POS changes)
	  * @return (the word voted on by the nearest neighbors)
	  */
	public static String processCBR(ArrayList<ArrayList<double[]>> results, 
			ArrayList<Pentuplet> trainList, String wordX, String wordY, Pentuplet
				test1, Pentuplet test2, int index){
		ArrayList<double[]> list = results.get(index);
		Iterator<double[]> iter = list.iterator();
		double[] temp;
		//use a high value to set the minimum values, so the real minimums
		// can be found later
		// min, min2, min3, min4, and min5 all store the word their score
		//corresponds to. minVal...is the actual score
		String min = null;
		double minVal = Math.pow(14, 10);
		String min2 = null;
		double minVal2 = Math.pow(14, 10);
		String min3 = null;
		double minVal3 = Math.pow(14, 10);
		String min4 = null;
		double minVal4 = Math.pow(14, 10);
		String min5 = null;
		double minVal5 = Math.pow(14, 10);
		String minWord = null;
		double minValWord = Math.pow(14, 10);
		String min2Word = null;
		double minVal2Word = Math.pow(14, 10);
		String min3Word = null;
		double minVal3Word = Math.pow(14, 10);
		String min4Word = null;
		double minVal4Word = Math.pow(14, 10);
		String min5Word = null;
		double minVal5Word = Math.pow(14, 10);
		int position = 0; // the index of the training example being examined
		int wordPosition = 0;
		while (iter.hasNext()){
			temp = iter.next();
			// this scores the POS
			//each value slides down if there is a new lowest
			if (temp[1] <= minVal){
				minVal5 = minVal4;
				min5 = min4;
				minVal4 = minVal3;
				min4 = min3;
				minVal3 = minVal2;
				min3 = min2;
				minVal2 = minVal;
				min2 = min;
				minVal = temp[1];
				min = trainList.get(position).getCenterWord();
				position++;
			}
			// all but the first slide down with a new 2nd lowest
			else if (temp[1] <= minVal2){
				minVal5 = minVal4;
				min5 = min4;
				minVal4 = minVal3;
				min4 = min3;
				minVal3 = minVal2;
				min3 = min2;
				minVal2 = temp[1];
				min2 = trainList.get(position).getCenterWord();
				position++;
			}
			// all but the first 2 slide down with a new 3rd lowest
			else if (temp[1] <= minVal3){
				minVal5 = minVal4;
				min5 = min4;
				minVal4 = minVal3;
				min4 = min3;
				minVal3 = temp[1];
				min3 = trainList.get(position).getCenterWord();
				position++;
			}
			// the 4th and 5th slide down with a new 4th lowest
			else if (temp[1] <= minVal4){
				minVal5 = minVal4;
				min5 = min4;
				minVal4 = temp[1];
				min4 = trainList.get(position).getCenterWord();
				position++;
			}
			// the 5th is replaced by a new lower value
			else if (temp[1] <= minVal5){
				minVal5 = temp[1];
				min5 = trainList.get(position).getCenterWord();
				position++;
			}
			// if nothing is lower, the training list index increases to the
			// next example
			else {
				position++;
			}

			// do the scoring of the words and not the POS in the same manner
			if (temp[0] <= minValWord){
				minVal5Word = minVal4Word;
				min5Word = min4Word;
				minVal4Word = minVal3Word;
				min4Word = min3Word;
				minVal3Word = minVal2Word;
				min3Word = min2Word;
				minVal2Word = minValWord;
				min2Word = minWord;
				minValWord = temp[1];
				minWord = trainList.get(wordPosition).getCenterWord();
				wordPosition++;
			}
			else if (temp[0] <= minVal2Word){
				minVal5Word = minVal4Word;
				min5Word = min4Word;
				minVal4Word = minVal3Word;
				min4Word = min3Word;
				minVal3Word = minVal2Word;
				min3Word = min2Word;
				minVal2Word = temp[1];
				min2Word = trainList.get(wordPosition).getCenterWord();
				wordPosition++;
			}
			else if (temp[0] <= minVal3Word){
				minVal5Word = minVal4Word;
				min5Word = min4Word;
				minVal4Word = minVal3Word;
				min4Word = min3Word;
				minVal3Word = temp[1];
				min3Word = trainList.get(wordPosition).getCenterWord();
				wordPosition++;
			}
			else if (temp[0] <= minVal4Word){
				minVal5Word = minVal4Word;
				min5Word = min4Word;
				minVal4Word = temp[1];
				min4Word = trainList.get(wordPosition).getCenterWord();
				wordPosition++;
			}
			else if (temp[0] <= minVal5Word){
				minVal5Word = temp[1];
				min5Word = trainList.get(wordPosition).getCenterWord();
				wordPosition++;
			}
			else {
				wordPosition++;
			}
		}
		
		//take votes of the final minimum nearest neighbors
		int langCount = 0;
		int wordCount = 0;
		if(min.equals(wordX)){
			langCount++;
		}
		if(min2.equals(wordX)){
			langCount++;
		}
		if(min3.equals(wordX)){
			langCount++;
		}
		if(min4.equals(wordX)){
			langCount++;
		}
		if(min5.equals(wordX)){
			langCount++;
		}
		if (minWord.equals(wordX)){
			wordCount++;
		}
		if (min2Word.equals(wordX)){
			wordCount++;
		}
		if (min3Word.equals(wordX)){
			wordCount++;
		}
		if (min4Word.equals(wordX)){
			wordCount++;
		}
		if (min5Word.equals(wordX)){
			wordCount++;
		}
		// results showed that a 4:1 ratio was very accurate
		// along with a 5:0
		if (wordCount >= 4 || langCount >= 4){
			return wordX;
		}
		// similarly accurate for 1:4 and 0:5
		else if (wordCount <= 1 || langCount <= 1){
			return wordY;
		}
		// words seemed stronger than POS, and 3:2 splits were inaccurate,
		// so is POS had 3 votes, word was the tie-breaker.
		else if (langCount == 3){
			if (wordCount == 3){
				return wordX;
			}
			else {
				return wordY;
			}
		}
		else{
			if(wordCount == 3){
				return wordX;
			}
			else {
				return wordY;
			}
		}

	}

	/**
	  * This method calculates the score of the entire phrase against a test
	  * phrase using words and POS.
	  *
	  * @param (training) (all training examples)
	  * @param (test) (the single test example)
	  * @return (a list of total scores for word and POS for each training example)
	  */
	public static ArrayList<double[]> basicCBR(ArrayList<Pentuplet> training,
			Pentuplet test){
		Iterator<Pentuplet> iter = training.iterator();
		DblLinkedList<String> firstWord;
		DblLinkedList<String> secondWord;
		DblLinkedList<String> firstLang;
		DblLinkedList<String> secondLang;
		ArrayList<double[]> list = new ArrayList<double[]>(); // stores each result
		Pentuplet temp;
		double[][] distance;
		double wordDistance; // the total score, summed at the end of the method
		double langDistance;
		Iterator<String> wordIter;
		Iterator<String> langIter;
		Iterator<String> testIterWord;
		Iterator<String> testIterLang;
		String tempWord;
		String tempLang;
		String testWord;
		String testLang;
		int testPosition; // used to calculate the distance between test and train
		int trainPosition;
		int arrayPos;	// stores the position of the next element in the array
		int arrayPosLang;
		boolean foundWord;
		boolean foundLang;
		while (iter.hasNext()){
			foundWord = false;
			foundLang = false;
			arrayPos = 0;
			arrayPosLang = 0;
			langDistance = 0;
			wordDistance = 0;
			distance = new double[2][14]; //1st row stores word scores, 2nd POS
			testPosition = 1;
			temp = iter.next();
			testIterWord = test.getFirstWord().iterator();
			testIterLang = test.getFirstLang().iterator();
			firstWord = temp.getFirstWord();
			firstLang = temp.getFirstLang();
			while (testIterWord.hasNext() && testIterLang.hasNext()){
				trainPosition = 1;
				wordIter = firstWord.iterator();
				langIter = firstLang.iterator();
				testWord = testIterWord.next();
				testLang = testIterLang.next();
				foundWord = false;
				foundLang = false;
				while (wordIter.hasNext() && langIter.hasNext()){
					tempWord = wordIter.next();
					tempLang = langIter.next();
					// calculate the distance for word
					if (testWord.equals(tempWord) && !foundWord){
						//If there are two of the same words in the phrase,
						//this will save the closest to center
						distance[0][arrayPos] = Math.sqrt(Math.pow(trainPosition 
								- testPosition, 2));
						arrayPos++;
						foundWord = true;
					}
					// calculate the distance for POS
					if (testLang.equals(tempLang) && !foundLang){
						distance[1][arrayPosLang] = Math.sqrt(Math.pow(trainPosition - testPosition, 2));
						arrayPosLang++;
						foundLang = true;
					}
					trainPosition++;
				}
				if (!foundWord){
					distance[0][arrayPos] = 10; // Greater than max distance from center word
					arrayPos++;
				}
				if (!foundLang){
					distance[1][arrayPosLang] = 10;
					arrayPosLang++;
				}
				testPosition++;
			}
			foundWord = false;
			foundLang = false;
			testIterWord = test.getSecondWord().iterator();
			testIterLang = test.getSecondLang().iterator();
			secondWord = temp.getSecondWord();
			secondLang = temp.getSecondLang();
			testPosition = 8; // the real position and not array-based position
			// do the same for the last 7 words
			while (testIterWord.hasNext() && testIterLang.hasNext()){
				wordIter = secondWord.iterator();
				langIter = secondLang.iterator();
				testWord = testIterWord.next();
				testLang = testIterLang.next();
				foundWord = false;
				foundLang = false;
				trainPosition = 8;
				while (wordIter.hasNext() && langIter.hasNext()){
					tempWord = wordIter.next();
					tempLang = langIter.next();
					// calculate for words
					if (testWord.equals(tempWord) && !foundWord){
						//If there are two of the same words in the phrase,
						//this will save the closest to center
						distance[0][arrayPos] = Math.sqrt(Math.pow(trainPosition
								- testPosition, 2));
						arrayPos++;
						foundWord = true;
					}
					// calculate for POS
					if (testLang.equals(tempLang) && !foundLang){
						distance[1][arrayPosLang] = Math.sqrt(Math.
								pow(trainPosition - testPosition, 2));
						arrayPosLang++;
						foundLang = true;
					}
					trainPosition++;
				}
				if (!foundWord){
					distance[0][arrayPos] = 10; // Greater than max distance 
					arrayPos++;				//from center word
				}
				if (!foundLang){
					distance[1][arrayPosLang] = 10;
					arrayPosLang++;
				}
				testPosition++;
			}

			// sum up the entire score and store it in an array of length 2
			for (int i = 0; i < distance[0].length; i++){
				wordDistance = wordDistance + distance[0][i];
				langDistance = langDistance + distance[1][i];
			}
			// add the array to the results list
			double[] array = new double[2];
			array[0] = wordDistance;
			array[1] = langDistance;
			list.add(array);
		}
		return list;
	}


	/** 
	 * In some Java IDE's, the console window can close up before you get a chance to read it,                                                        
	 * so this method can be used to wait until you're ready to proceed.
	 * @param msg the message to display before waiting
	 */                                                                              
	public static void waitHere(String msg)
	{
		System.out.print("\n" + msg);
		try { System.in.read(); }
		catch(Exception e) {} // Ignore any errors while reading.                                                                                       
	}

}
