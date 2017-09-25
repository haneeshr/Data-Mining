package com.ub.cse.datamining;

import java.util.HashSet;
import java.util.Set;

public class QueryParser {
	
	public Set<Rule> queryParser(int type, String query) {
		if(type==1) {
			int index = query.indexOf(',');
			String part = query.substring(0, index).trim();
			
			query = query.substring(index+1).trim();
			index = query.indexOf(',');
			String occurence = query.substring(0, index).trim();
			
			query = query.substring(index+1).trim();
			query = query.substring(1, query.length()-1);
			String items[] = query.split(",");
			
			Set<String> itemSet = new HashSet<>();
			for(int i=0; i<items.length; i++){
				items[i] = items[i].trim();
				items[i] = items[i].replaceAll("'", "");
				itemSet.add(items[i]);
			}
//			return template1(part, occurence, itemSet);
			
		}else if(type==2) {
			int index = query.indexOf(',');
			String part = query.substring(0, index).trim();
			
			query = query.substring(index+1).trim();
			int size = Integer.parseInt(query);
//			return template2(part, size);
		}else if(type==3) {
			
			int index = query.indexOf(',');
			
			String operation = query.substring(0, index).trim();
			operation = operation.replaceAll("\"", "");
			
			System.out.println(operation);
			int type1 = operation.charAt(0)-'0';
			int type2 = operation.charAt(index-3)-'0';
			operation = operation.substring(1, operation.length()-1);
			
			query = query.substring(index+1).trim();
			System.out.println(type1);
			System.out.println(type2);
			System.out.println(operation);
			
			if(type1 == 1) {
				String query1 = query.split("]")[0].trim()+"]";
				String query2 = query.split("]")[1].substring(1).trim();
				
				if(type2 == 1)query2+="]";
				System.out.println(query1);
				System.out.println(query2);
			}else {
//				String split = query.inde
			}
			
		}else {
			System.out.println("Invalid type");
			return null;
		}
		
		return null;
	}
}
