package com.ub.cse.datamining;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Record {
	Record next;
	Set<String> itemSet;
	Record(String str){
		next = null;
		
		itemSet = new HashSet<>();
		String[] inputItemset = str.split("\\t");
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < inputItemset.length; i++) {
			sb.append("G").append(i+1).append("_").append(inputItemset[i]);
			itemSet.add(sb.toString());
			sb.setLength(0);
		}
	}
	
	public boolean isSubSet(List<String> subsetArr) {
		
		for(String subset: subsetArr) {
			if(!itemSet.contains(subset)) return false;
		}
		
		return true;	
	}
}
