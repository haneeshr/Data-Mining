package com.ub.cse.datamining;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Main {
	static Record head;
	static Set<String> globalItemSet;
	static List<List<String>> resultList;
	static int threshold = 70;
	
	public static void main(String[] args) throws FileNotFoundException {
		String currentRecord = "";
		
		//Read the file
		BufferedReader fileReader = new BufferedReader(new FileReader("associationruletestdata.txt"));
		head = null;
		
		try {
			globalItemSet = new HashSet<>();
			while((currentRecord = fileReader.readLine()) != null) {
				Record record = new Record(currentRecord);
				record.next = head;
				head = record;
//				inputTransacations++;
			}
			resultList = new ArrayList<List<String>>();
			
			for(String currentItemSet: globalItemSet)
				resultList.add(new ArrayList<String>() {{ add(currentItemSet);}});
			resultList = getFrequentItemSets(resultList, threshold);
			
			int count=0;
			while(resultList.size()>1) {
				System.out.println(resultList.size());
				count += resultList.size();
				resultList = getSuperSets(resultList);
			}
			System.out.println(resultList.size());
			System.out.println(count + resultList.size());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in Parsing file");
			e.printStackTrace();
		}
		
	}
	
	public static int getSupportCount(List<String> itemSet) {
		
		Record tempHead = head;
		int occurenceCount = 0;
		
		while(tempHead != null) {
			if(tempHead.isSubSet(itemSet)) occurenceCount++;
			tempHead = tempHead.next;
		}
		return occurenceCount;
	}
	
	public static List<List<String>> getSuperSets(List<List<String>> inputSet){
		resultList = new ArrayList<List<String>>();
		
		Set<String> uniqueItemSet = new TreeSet<>();
		for(List<String> currentInputSet : inputSet) {
			for(String currentInputString : currentInputSet) {
				uniqueItemSet.add(currentInputString);
				}
		}
		
		List<String> newCombination;
		
		for(List<String> currentInputSet: inputSet) {
			String lastElement=currentInputSet.get(currentInputSet.size()-1);
			for(String currentElement : uniqueItemSet) {
				if(currentElement.compareTo(lastElement)>0){
					newCombination = new ArrayList<String>(currentInputSet);
					newCombination.add(currentElement);
					//Validate the combination
					if(isValidCombination(newCombination, inputSet)) resultList.add(newCombination);
				}
			}
		}
		
		return getFrequentItemSets(resultList, threshold);
	}
	
	public static boolean isValidCombination(List<String> newCombination, List<List<String>> inputSet) {
		Set<List<String>> hs=new HashSet<>();
		for(List<String> eachSet:inputSet) {
			hs.add(eachSet);
		}
		
		for(int i = 0; i < newCombination.size(); i++) {
			String poppedElement = newCombination.remove(i);
			if(!hs.contains(newCombination)) return false;
			newCombination.add(i,poppedElement);
		}
		
		return true;
	}
	
	public static List<List<String>> getFrequentItemSets(List<List<String>> combinations, int frequencyThreshold){
		List<List<String>> resultList = new ArrayList<List<String>>();
		
		for(List<String> currentCombination: combinations) {
			int support = getSupportCount(currentCombination);
			if(support >= frequencyThreshold)
				resultList.add(currentCombination);
			}
		
		return resultList;
	}
	

}
