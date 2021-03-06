package com.ub.cse.datamining;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RuleGroup {
	

	Set<Rule> allRules;
	Set<Rule> heads;
	Set<Rule> bodies;
	Map<String,Set<Rule>> bodyMap;
	Map<String,Set<Rule>> headMap;
	
	
	int length;
	
	RuleGroup(int length){
		this.length=length;
		allRules=new HashSet<>();
		bodyMap=new HashMap<>();
		headMap=new HashMap<>();
		heads = new HashSet<>();
		bodies = new HashSet<>();
	}
	
	public void addToRuleGroup(Rule rule){
		allRules.add(rule);
		addHeadandBody(rule);
		for(String each : rule.head){
			headMap.putIfAbsent(each, new HashSet<>());
			headMap.get(each).add(rule);
		}
		for(String each : rule.body){
			bodyMap.putIfAbsent(each, new HashSet<>());
			bodyMap.get(each).add(rule);
		}
	}
	
	
	
	private void addHeadandBody(Rule rule) {
		Main.ruleGroups.get(rule.head.size()).heads.add(rule);
		Main.ruleGroups.get(rule.body.size()).bodies.add(rule);
	}

	public boolean containsInHead(String item){ return headMap.containsKey(item);}
	
	public boolean containsInBody(String item){ return bodyMap.containsKey(item);}
	
	public boolean containsInRule(String item){ 
		return bodyMap.containsKey(item) || headMap.containsKey(item);
	}

	
	public Set<Rule> getFromHead(String item){
		
		Set<Rule> out=new HashSet<>(headMap.get(item));
		return out;
	}
	
	public Set<Rule> getFromBody(String item){
		Set<Rule> out=new HashSet<>(bodyMap.get(item));
		return out;
	}
	
	
	public Set<Rule> getFromRule(String item){
		Set<Rule> out=new HashSet<>(bodyMap.get(item));
		out.addAll(headMap.get(item));
		return out;
	}
	
	public void generateRules(double threshold, Set<List<String>> frequentSets){
		for(List<String> eachSet:frequentSets){
			validateAndAdd(threshold,eachSet);
		}	
	}
	
	
	public void validateAndAdd(double threshold,List<String> itemList){
		int count=1<<itemList.size();
		Rule rules[]=new Rule[count];
		double numerator=(double)Main.getSupportCount(itemList);
		
		for(int i=0;i<count;i++) rules[i]=new Rule();
		
		for(int i=1;i<count-1;i++){
			for(int j=0;j<itemList.size();j++){
				if((i>>j & 1)==1){
					rules[i].addtoBody(itemList.get(j));
				}
				else{
					rules[i].addtoHead(itemList.get(j));
				}
			}
		}
		
		for(int i=1;i<count-1;i++){
			double denomenator=(double)Main.getSupportCount(rules[i].body);
			double confidence=numerator/denomenator;
			if(confidence>=threshold){
//				System.out.println(rules[i].body +"-->"+rules[i].head);
				Main.ruleCount++;
				addToRuleGroup(rules[i]);
			}
		}
	}
	
}
