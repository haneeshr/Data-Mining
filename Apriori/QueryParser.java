package com.ub.cse.datamining;

import java.util.HashSet;
import java.util.Set;

public class QueryParser {
	
	public Set<Rule> queryParser(int type, String query, boolean diff) {
		if(type==1) {
			int index = query.indexOf(',');
			String part = query.substring(0, index).trim();
			part = part.replaceAll("\"", "");
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
			
			
			return template1(part, occurence, itemSet, diff);
			
		}else if(type==2) {
			int index = query.indexOf(',');
			String part = query.substring(0, index).trim();
			part = part.replaceAll("\"", "");
			
			query = query.substring(index+1).trim();
			int size = Integer.parseInt(query);
			
			return template2(part, size, diff);
		}else if(type==3) {
			
			int index = query.indexOf(',');
			
			String operation = query.substring(0, index).trim();
			operation = operation.replaceAll("\"", "");
			
			int type1 = operation.charAt(0)-'0';
			int type2 = operation.charAt(index-3)-'0';
			operation = operation.substring(1, operation.length()-1);

			String query1="";
			String query2="";

			query = query.substring(index+1).trim();
			System.out.println(type1);
			System.out.println(type2);
			System.out.println(operation);
			System.out.println(query);

			if(type1 == 1) {
				query1 = query.split("]")[0].trim()+"]";
				query2 = query.split("]")[1].substring(1).trim();
				
				if(type2 == 1)query2+="]";
				System.out.println(query1);
				System.out.println(query2);
			}else {
				String[] split = query.split(",");
				query1 = split[0]+", "+split[1];
				query2 = "";
				for(int i=2; i<split.length; i++){
					query2+=split[i];
					if(i!=split.length-1)query2+=",";
				}
				System.out.println(query1);
				System.out.println(query2);
			}
			Set<Rule> result1 = new HashSet<>();
			Set<Rule> result2 = new HashSet<>();

			result1 = queryParser(type1, query1, false);
			result2 = queryParser(type2, query2, false);

			operate(operation, result1, result2);
		}else {
			System.out.println("Invalid type");
			return null;
		}
		

		return null;
	}
	
	public Set<Rule> template1(String part, String condition, Set<String> itemSet, boolean diff) {
		
		Set<Rule> result = new HashSet<>();
	
		if(condition.equalsIgnoreCase("any")) {
			handleAny(part, result, itemSet);
		}
		else if(condition.equalsIgnoreCase("none")) {
			handleNone(part, result, itemSet, true);
		}
		else {
			handleOne(part, result, itemSet);
		}

		if(diff){
			System.out.println(result);
			System.out.println(result.size());
		}
		return result;
		
	}
	
	public void handleAny(String part, Set<Rule> result, Set<String> itemSet) {
		System.out.println(part);
		for(int i=0; i < Main.ruleGroups.size(); i++) {
			RuleGroup currRuleGroup = Main.ruleGroups.get(i);
			for(String item : itemSet) {
				if(part.equalsIgnoreCase("rule") && currRuleGroup.containsInRule(item)) {
//					System.out.println("Here");
					result.addAll(currRuleGroup.getFromRule(item));
				}
				else if(part.equalsIgnoreCase("body") && currRuleGroup.containsInBody(item)) {
					result.addAll(currRuleGroup.getFromBody(item));
				}
				else if(part.equalsIgnoreCase("head") && currRuleGroup.containsInHead(item)) {
					result.addAll(currRuleGroup.getFromHead(item));
				}
			}
		}
	}
	
	public void handleNone(String part, Set<Rule> result, Set<String> itemSet, boolean diff) {
		
		for(int i=0; i < Main.ruleGroups.size(); i++) {
			RuleGroup currRuleGroup = Main.ruleGroups.get(i);
			if(diff)result.addAll(currRuleGroup.allRules);
			for(String item: itemSet) {
				if(part.equalsIgnoreCase("rule") && currRuleGroup.containsInRule(item)) {
					result.removeAll(currRuleGroup.getFromRule(item));
				}else if(part.equalsIgnoreCase("body") && currRuleGroup.containsInBody(item)) {
					result.removeAll(currRuleGroup.getFromBody(item));
				}
				else if(part.equalsIgnoreCase("head") && currRuleGroup.containsInHead(item)) {
					result.removeAll(currRuleGroup.getFromHead(item));
				}
			}
		}
		
	}
	
	public void handleOne(String part, Set<Rule> result, Set<String> itemSet) {
		
		Set<String> itemSetClone = new HashSet<>(itemSet);

		for(int i=0; i<Main.ruleGroups.size(); i++) {
			RuleGroup currRuleGroup = Main.ruleGroups.get(i);
			for(String item : itemSet) {
				Set<Rule> set = new HashSet<>();
				itemSetClone.remove(item);
				if(part.equalsIgnoreCase("rule") && currRuleGroup.containsInRule(item)) {
//					System.out.println("Here");
					set.addAll(currRuleGroup.getFromRule(item));
				}
				else if(part.equalsIgnoreCase("body") && currRuleGroup.containsInBody(item)) {
					set.addAll(currRuleGroup.getFromBody(item));
				}
				else if(part.equalsIgnoreCase("head") && currRuleGroup.containsInHead(item)) {
					set.addAll(currRuleGroup.getFromHead(item));
				}
				handleNone(part, set, itemSetClone, false);
				itemSetClone.add(item);
				result.addAll(set);
			}
		}
	}
	
	public Set<Rule> template2(String part, int size, boolean diff) {
		
		Set<Rule> result = new HashSet<>();
		
		for(; size<Main.ruleGroups.size(); size++) {
			RuleGroup ruleGroup = Main.ruleGroups.get(size);
			if(part.equalsIgnoreCase("rule")) {
				result.addAll(ruleGroup.allRules);
			}else if(part.equalsIgnoreCase("head")) {
				result.addAll(ruleGroup.heads);
			}else if(part.equalsIgnoreCase("body")) {
				result.addAll(ruleGroup.bodies);
			}
		}
		
		if(diff){
			System.out.println(result);
			System.out.println(result.size());
		}
		return result;
	}

	public void operate(String operation, Set<Rule> set1, Set<Rule> set2){

		Set<Rule> result = new HashSet<>(); 
		if(operation.equalsIgnoreCase("or")){
			result.addAll(set1);
			result.addAll(set2);
		}else if(operation.equalsIgnoreCase("and")){
			for(Rule rule : set1){
				if(set2.contains(rule))result.add(rule);
			}
		}

		System.out.println(result);
		System.out.println(result.size());
		result.clear();
	}
	
	
}
