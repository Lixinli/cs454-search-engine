package asd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Term {
	public ArrayList<Integer> position;
	public String name;
	public double tfidf;
	public Set<String> related_url;
	public Set<Document> related_doc;
	public Map<String, ArrayList<Long>> docs_pos;
	public double doc_counter;
	public  int count;

	public Term(String name, ArrayList<Integer> postion, Set<String> related_url) {
		super();
		this.position = postion;
		this.docs_pos = new HashMap<String, ArrayList<Long>>();
	
		this.name = name;
		this.tfidf = 0;
		this.related_url = related_url;
		this.related_doc = new HashSet<Document>();
	
	}

	@SuppressWarnings("static-access")
	public Term(String name, Map<String, ArrayList<Long>> docs_pos,
			double tfidf, Set<String> related_url) {
		super();
	
		this.name = name;
		this.tfidf = tfidf;
		this.docs_pos = docs_pos;
		this.related_url = related_url;
		this.related_doc = new HashSet<Document>();
		
		
	}

	public ArrayList<Integer> getPostion() {
		return position;
	}

	public void setPostion(ArrayList<Integer> postion) {
		this.position = postion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTfidf() {
		return tfidf;
	}

	public void setTfidf(double tfidf) {
		this.tfidf = tfidf;
	}

	public Set<String> getRelated_url() {
		return related_url;
	}

	public void setRelated_url(Set<String> related_url) {
		this.related_url = related_url;
	}

	public void addRelated_url(String url) {
		related_url.add(url);
	}

	public void counter() {
		
		this.count++;
	}

	public void tf(int size) {
		
		double newC = 1;
		if (size != 0)
			newC = count / size;
		else 
			newC=count;
		if (newC > doc_counter)
			doc_counter = newC;
		//System.out.println("co "+doc_counter);
		
	}
	

}
