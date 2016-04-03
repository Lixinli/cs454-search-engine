package asd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Document {
	public final int id;
	public String title;
	public String url;
	public String realURL;
	public Set<Integer> terms;
	public Set<String> linkto; // link to other doc
	public Set<String> linkfrom;
	public Set<Integer> linktoid;
	public Set<Integer> linkfromid;
	public double linkscore;
	public Set<Integer> position;
	public ArrayList<Term> listofterms;
	public String storage;
	HashMap<String, Double> listofTFIDF;
	double TFIDF;
	double proximity;

	public Document(int id, String title, String url, Set<String> linkto) {
		super();
		this.id = id;
		this.title = title;
		this.url = url;
		this.linkto = linkto;
		this.linkfrom = new HashSet<String>();
		this.linkscore = 1;
		this.linktoid = new HashSet<Integer>();
		this.linkfromid = new HashSet<Integer>();
	}

	public Document(int id, String url, int position) {
		super();
		this.id = id;
		this.url = url;
		this.position = new HashSet<Integer>();
		this.position.add(position);
	}

	public Document(String url, String storage, Set<String> linkto) {
		super();
		this.id = url.hashCode();
		this.storage = storage;
		this.url = url;
		this.position = new HashSet<Integer>();
		this.listofterms = new ArrayList<Term>();
	}

	public Document(String url, double linkscore, double t, String realURL) {
		super();
		this.id = 0;
		this.url = url;
		this.linkscore = linkscore;
		this.listofterms = new ArrayList<Term>();
		this.listofTFIDF = new HashMap<String, Double>();
		this.TFIDF = t;
		this.realURL = realURL;
	}

	public void calculate_distance(ArrayList<String> query_terms) {
		ArrayList<Long> pevterm = null;
		double total = 0;
		boolean notfirstTerm = false;
		for (String term : query_terms) {
			// ===get tfidf from terms
			if (listofTFIDF.containsKey(term))
				if (TFIDF < listofTFIDF.get(term))
					TFIDF = listofTFIDF.get(term);

			for (Term t : listofterms) {
				if (t.name.equals(term) && notfirstTerm) {

					// System.out.println(t.docs_pos.get(this.url) + "  "
					// + query_terms.size());
					//
					total = total
							+ cal_eachTerm(t.docs_pos.get(this.url), pevterm);

				}
				notfirstTerm = true;
				pevterm = t.docs_pos.get(this.url);
			}
		}

		proximity = total;
	}

	public double cal_eachTerm(ArrayList<Long> arrayList, ArrayList<Long> term2) {
		double scoreSum = 0;
		
		for (int i = 0; i < arrayList.size(); i++) {
			long t1 = arrayList.get(i);
			for (int j = 0; j < term2.size(); j++) {
				long t2 = term2.get(j);
				if (Math.abs(t1 - t2) < 3 && t1 != t2) {

					scoreSum = 1 / Math.abs(Math.pow(t1, 2) - Math.pow(t2, 2))
							+ scoreSum;
//					System.out.println("yeah " + scoreSum);
					
				}
			}
		}

		return scoreSum;

	}

	public void addTerms(Term term) {
		listofterms.add(term);
	}

	// public void addTFIDF(String name, double t) {
	// this.listofTFIDF.put(name, t);
	// }

	public double get_finalScore() {
		// System.out.println("tfidf: "+TFIDF);
		// System.out.println("linkscore: "+TFIDF * .3 + linkscore * .3
		// +proximity*.4);
		return TFIDF * .3 + linkscore * .3;
	}

	public void getTFIDF() {
		for (Term tmpterm : listofterms) {
			if (TFIDF < tmpterm.tfidf)
				TFIDF = tmpterm.tfidf;
		}

	}

}
