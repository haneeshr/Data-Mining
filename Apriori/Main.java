package com.ub.cse.datamining;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	static Record head;
	
	public static void main(String[] args) throws FileNotFoundException {
		String currentRecord = "";
		int inputTransacations = 0;
		
		//Read the file
		BufferedReader fileReader = new BufferedReader(new FileReader("associationruletestdata.txt"));
		head = null;
		
		try {
			while((currentRecord = fileReader.readLine()) != null) {
				Record record = new Record(currentRecord);
				record.next = head;
				head = record;
				inputTransacations++;
			}
			
			
			ArrayList<String> tempArrayList1 = new ArrayList<>();
			ArrayList<String> tempArrayList2 = new ArrayList<>();
			
			tempArrayList1.add("G1_Up");
			tempArrayList2.add("G2_Down");
			
			List<List<String>> inputList = new ArrayList<>();
			inputList.add(tempArrayList1);
			inputList.add(tempArrayList2);
			
			List<List<String>> resultList = new ArrayList<List<String>>();
			resultList = getFrequentItemSets(inputList, 66);
			System.out.println("Result " + resultList.toString());
			
			
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
