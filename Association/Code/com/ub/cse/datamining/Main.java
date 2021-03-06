package com.ub.cse.datamining;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;


public class Main {
	
	static double supportThreshold    = 0.5;
	static double confidenceThreshold = 0.7;
	
	static int ruleCount = 0;
	static int fileLength = 0;
	static Record head;
	static Set<String> globalItemSet;
	static Set<List<String>> resultList;
	
	static List<RuleGroup> ruleGroups=new ArrayList<>();
	static QueryParser queryParser = new QueryParser();
	
	public static void main(String[] args) throws FileNotFoundException {
		if(args.length > 0) {
			supportThreshold = Double.parseDouble(args[0]);
			confidenceThreshold = Double.parseDouble(args[1]);
		}
		
		String currentRecord = "";
		ruleGroups.add(new RuleGroup(0));
		ruleGroups.add(new RuleGroup(1));
		BufferedReader fileReader = new BufferedReader(new FileReader("associationruletestdata.txt"));
		head = null;
		
		try {
			globalItemSet = new HashSet<>();
			while((currentRecord = fileReader.readLine()) != null) {
				Record record = new Record(currentRecord);
				record.next = head;
				head = record;
				fileLength++;
			}
			resultList = new HashSet<List<String>>();
			
			for(String currentItemSet: globalItemSet)
				resultList.add(new ArrayList<String>() {{ add(currentItemSet);}});
			resultList = getFrequentItemSets(resultList, supportThreshold);
			
			int frequentItemSetCounter = 0;
			int count=0;
			int length=2;
			do{
				frequentItemSetCounter++;
				System.out.println("Number of length "+ frequentItemSetCounter + " frequent itemsets:" + resultList.size());
//				System.out.println(resultList);
				count += resultList.size();
				resultList = getSuperSets(resultList);
				RuleGroup rulegroup=new RuleGroup(length);
				ruleGroups.add(rulegroup);
				rulegroup.generateRules(confidenceThreshold, resultList);
				length++;
			}while(resultList.size()>0); 
			System.out.println("count :"+count);
			System.out.println("Rule count " + ruleCount);
			
			while(true) {
				Scanner sc = new Scanner(System.in);
				System.out.print("Enter template type:");
				int type = Integer.parseInt(sc.nextLine());
				System.out.print("Enter query:");
				String query = sc.nextLine();
				queryParser.queryParser(type, query, true);
			}
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
	
	public static int getSupportCount(Set<String> itemSet) {
		Record tempHead = head;
		int occurenceCount = 0;
		while(tempHead != null) {
			if(tempHead.isSubSet(itemSet)) occurenceCount++;
			tempHead = tempHead.next;
		}
		return occurenceCount;
	}
	
	
	public static Set<List<String>> getSuperSets(Set<List<String>> inputSet){
		resultList = new HashSet<List<String>>();
		
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
		
		return getFrequentItemSets(resultList, supportThreshold);
	}
	
	public static boolean isValidCombination(List<String> newCombination, Set<List<String>> inputSet) {
		
		for(int i = 0; i < newCombination.size(); i++) {
			String poppedElement = newCombination.remove(i);
			if(!inputSet.contains(newCombination)) return false;
			newCombination.add(i,poppedElement);
		}
		
		return true;
	}
	
	public static Set<List<String>> getFrequentItemSets(Set<List<String>> combinations, double supportThreshold){
		Set<List<String>> resultList = new HashSet<List<String>>();
		
		for(List<String> currentCombination: combinations) {
			int support = getSupportCount(currentCombination);
			double supportFactor = (double) support/fileLength;
			
			if(supportFactor >= supportThreshold)
				resultList.add(currentCombination);
			}
		
		return resultList;
	}
	
	
}
