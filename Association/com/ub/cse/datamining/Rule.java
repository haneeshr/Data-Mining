package com.ub.cse.datamining;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Rule {

	Set<String> body;
	Set<String> head;
	int size;
	
	Rule(){
		size=0;
		body=new HashSet<>();
		head=new HashSet<>();
	}

	public void addtoBody(String item){
		body.add(item);
		size++;
	}
	public void addtoHead(String item){
		head.add(item);
		size++;
	}
	
	
	public boolean isPresentInBody(String item){
		return body.contains(item);
	}
	
	
	public boolean isPresentInHead(String item){
		return head.contains(item);
	}
	
	public boolean isPresentInRule(String item){
		return body.contains(item) || head.contains(item);
	}
	
	
	public void buildHeadMap(Map<String,Set<Rule>> map){
		for(String item: head){
			map.putIfAbsent(item, new HashSet<>());
			map.get(item).add(this);
		}
	}
	
	
	public void buildBodyMap(Map<String,Set<Rule>> map){
		for(String item: body){
			map.putIfAbsent(item, new HashSet<>());
			map.get(item).add(this);
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();	
		sb.append("\n").append(this.body).append(" -> ").append(this.head);		
		return sb.toString();
		
	}
	
}
