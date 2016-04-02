package asd;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Search {
	public static HashMap<String, Term> searchterms = new HashMap<String, Term>();
	public static HashMap<String, Term> totalterms = new HashMap<String, Term>();
	public static HashMap<String, Double> linkscore = new HashMap<String, Double>();
	public static HashMap<String, Document> doc = new HashMap<String, Document>();
	public static ArrayList<Document> searchDoc = new ArrayList<Document>();
	public static ArrayList<String> query_terms = new ArrayList<String>();
	public static HashMap<Integer, Double> PRvalue = new HashMap<Integer, Double>();
	Map<String, Double> tfidfList = new HashMap<String, Double>();

	public void readFile() throws IOException, ParseException {
		read_Index_File();
		read_Rank_File();
		System.out.println(doc.size());
		System.out.println(totalterms.size());
	}

	public void read_Index_File() throws IOException, ParseException {
		JSONParser parser = new JSONParser();

		FileReader fileReader = new FileReader("K:/test4/Index.json");
		JSONObject json = (JSONObject) parser.parse(fileReader);
		JSONArray ja = (JSONArray) json.get("index");
		// System.out.println(ja.size());
		double maxtfidf = 1;
		for (int j = 0; j < ja.size(); j++) {

			JSONObject json2 = (JSONObject) ja.get(j);
			JSONArray ja2 = (JSONArray) json2.get("related_doc");
			String name = (String) json2.get("term");
			Set<String> related_url = new HashSet<String>();
			double tfidf = (Double) json2.get("tfidf");

			if (tfidf > maxtfidf)
				maxtfidf = tfidf;
			Map<String, ArrayList<Long>> docs_pos = new HashMap<String, ArrayList<Long>>();
			for (int i = 0; i < ja2.size(); i++) {
				JSONObject json3 = (JSONObject) ja2.get(i);
				String url = (String) json3.get("path");
				String path = url.replaceAll(".html", "");
				path = path + ".html";
				@SuppressWarnings("unchecked")
				ArrayList<Long> pos = (ArrayList<Long>) json3.get("position");

				docs_pos.put(path, pos);
				// ================add url to related url
				if (!related_url.contains(path))
					related_url.add(path);

				if (tfidfList.containsKey(path)) { // add tfidf to list
					if (tfidfList.get(path) < tfidf)
						tfidfList.put(path, tfidf);
				} else {
					tfidfList.put(path, tfidf);
				}
			}
			double finalTFIDF = tfidf / maxtfidf;
			totalterms.put(name, new Term(name, docs_pos, finalTFIDF,
					related_url));
		}
		for (Map.Entry<String, Double> entry : tfidfList.entrySet()) {
			entry.setValue(entry.getValue() / maxtfidf);

		}
	}

	public void read_Rank_File() throws IOException, ParseException {
		JSONParser parser = new JSONParser();

		FileReader fileReader = new FileReader("K:/test4/Rank.json");
		JSONObject json = (JSONObject) parser.parse(fileReader);
		JSONArray ja = (JSONArray) json.get("rank");
		// Set<String> linklist = new HashSet<String>();
		for (int j = 0; j < ja.size(); j++) {
			double tfidf = 0;
			JSONObject json2 = (JSONObject) ja.get(j);
			String url = (String) json2.get("path");
			String path;
			if (url.contains(".html"))
				path = url;
			else
				path = url + ".html";
			if (tfidfList.containsKey(path))
				tfidf = tfidfList.get(path);
			// System.out.println(path);
			doc.put(path, new Document(path, (Double) json2.get("linkscore"),
					tfidf,(String) json2.get("realURL")));
		}

	}

	public ArrayList<String> search(String text) {

		String[] str = text.split(" ");
		System.out.println("exist");

		query_terms = new ArrayList<String>(Arrays.asList(str));
		for (int j = 0; j < query_terms.size(); j++) {
			String key = query_terms.get(j);
			if (totalterms.containsKey(query_terms.get(j))) {
				searchterms.put(key, totalterms.get(key));
				// System.out.println(searchDoc.size());
			}
		}
		addTerm();
		/*
		 * for (Map.Entry<String, Document> entry : doc.entrySet()) {
		 * 
		 * for(Term t: entry.getValue().listofterms) System.out.println("Key = "
		 * + entry.getKey() + ", Value = " +t.name);
		 * 
		 * 
		 * }
		 */
		double maxProx = 0;
		for (Document tmpdoc : searchDoc) {
			tmpdoc.calculate_distance(query_terms);
			if (tmpdoc.proximity > maxProx)
				maxProx = tmpdoc.proximity;
			// System.out.println(tmpdoc.get_finalScore());
			//System.out.println("prox score "+tmpdoc.realURL+" = "+tmpdoc.proximity);
		}
		System.out.println("search result========" + searchDoc.size());
		for (Document tmpdoc : searchDoc) {
			if(tmpdoc.proximity!=0)
			tmpdoc.proximity = tmpdoc.proximity / maxProx;
		
		}

		// System.out.println(searchDoc);
		Collections.sort(searchDoc, new Comparator<Document>() {
			public int compare(Document x, Document y) {
				if (x.get_finalScore() < y.get_finalScore()) {
					return +1;
				}
				if (x.get_finalScore() > y.get_finalScore()) {
					return -1;
				}
				return 0;
			}
		});
		
		// System.out.print("result # " + searchDoc.size());
		ArrayList<String> result = new ArrayList<String>();
		for (Document tmpdoc : searchDoc) {
			 System.out.println("url: " + tmpdoc.url + "  tfidf= "
			 + tmpdoc.TFIDF + "link score =" + tmpdoc.linkscore
			 + "final score: " + tmpdoc.get_finalScore()+" prox "+tmpdoc.proximity);
			result.add(tmpdoc.realURL + "," + tmpdoc.TFIDF + "," + tmpdoc.linkscore
					+ "," + tmpdoc.get_finalScore()+','+tmpdoc.proximity);
		}
		searchterms = new HashMap<String, Term>();
		searchDoc = new ArrayList<Document>();
		return result;
	}

	public void addTerm() {
		for (Map.Entry<String, Term> entry : searchterms.entrySet()) {
			// System.out.println("Key = " + entry.getKey() + ", Value = " +
			// entry.getValue().related_url);

			for (String url : entry.getValue().related_url) {
				// System.out.println("Key = "+url);
				if (doc.containsKey(url)) {
					// System.out.println("Key = " + entry.getKey() +
					// ", Value = " +
					// entry.getValue().related_url);
					Document tmpdoc = doc.get(url);
					if (!tmpdoc.listofterms.contains(entry.getValue()))
						tmpdoc.addTerms(entry.getValue());
					searchDoc.add(tmpdoc);
				}
			}
		}

	}

	public static void main(String[] args) throws Exception {
		Search s = new Search();
		s.readFile();
		s.search("explosive");
		// while (true) {
		// System.out.print("enter search term");
		// Scanner scanner = new Scanner(System.in);
		// String sentence = scanner.next();
		// System.out.println("entered");
		// s.search(sentence);
		// }
	}
}
