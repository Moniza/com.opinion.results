//sentiments
package com.opinion.results;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import opennlp.maxent.io.OldFormatGISModelReader;



public class ClassB {
	static boolean bool = true;
	String oldpositive = null;
	String oldnegative = null;
	ArrayList<String> posSentence = new ArrayList<>();
	ArrayList<String> negSentence = new ArrayList<>();
	static boolean stop = false;

	
	public void classBresult(String question) throws IOException
	{
		//ClassA ca = new ClassA();
		ClassB obj = new ClassB();
		System.out.println("Queation:");
        Scanner q=new Scanner(System.in);
		question=question.replace("?","");
		
		String FP = obj.extractProductname(question);
		
		String[] s = FP.split("==");

		File file = obj.findFile(s[1]);
		String feature = obj.StemWords(s[0]);
		obj.ExtractProductFeaturesSentiments(file, feature,s[0]);

		if (!bool) {
			while (!stop) {
				System.out.println("Do you want to see more reviews? (Y/N)");
				Scanner in = new Scanner(System.in);
				String review = in.next();
				if (review.toLowerCase().equals("y")) {
					obj.ExtractProductFeaturesSentiments(file, feature,s[0]);
				} else {
					System.out.println("See you soon!");
					stop=true;
				}
				
			}
		}
	}
	
	
	public String extractProductname(String question) throws IOException {
		String[] products = { "google nexus 7","nexus 7", "samsung galaxy ace","samsung galaxy ace duos",
				"samsung galaxy grand", "nokia lumia 520", "sony xperia e","sony xperia",
				"moto g" };
		String productname = "";
		String keywords = "";
		String productfeatures = "";
		String[] quesKeywords = question.split(" ");
		for (String q : quesKeywords) {
			//System.out.println(".."+q+"..");
			if (Utility.stopWords.contains(q.toLowerCase().trim()))
				continue;
			else
				keywords += q + " ";
		}
		keywords = keywords.replaceAll(",", " ");
		keywords = keywords.replaceAll("-", " ");
		keywords = keywords.replace("?", "");
		for (String p : products) {
			if (keywords.toLowerCase().contains(p.toLowerCase()))
				productname = p;
		}
		if (productname == "") {
			System.out.println("given product is not valid");
		}
		String[] key = keywords.split(" ");
		for (String k : key) {
			if (productname.toLowerCase().contains(k.toLowerCase()))
				continue;
			else
				productfeatures += k + " ";
		}
		String FP = productfeatures + "==" + productname;
		return FP;
	}

	
	public File findFile(String productname) {
		String[] questionProduct = productname.split(" ");
		File file = null;
		File folder = new File(
				"C://project/sentimentDatabase/opinionMining/SentencePolarity/");
		File[] files = folder.listFiles();
		for (File f : files) {
			int count = 0;
			String filename = f.getName().replace(".txt", "");
			String product = filename.replace("-", " ");
			for (int i = 0; i < questionProduct.length; i++) {
				if (product.toLowerCase().contains(questionProduct[i].toLowerCase()))
					{
					for(String p:product.split(" "))
					{ 
						
					if(p.toLowerCase().equals(questionProduct[i].toLowerCase()))
							{
					//System.out.println(product+"   "+questionProduct[i]);
					count++;
							}
					}
			}
		if (count == questionProduct.length) {
				file = f;
				System.out.println("file found>> " + f.getName());
				break;
			}
		}
		}
		return file;
		
	}
	
	
	
	
	
	
	public void ExtractProductFeaturesSentiments(File file, String featurename,String feature)
			throws IOException {
		HashMap<String, Integer> map = new HashMap<>();
		String posf = "";
		String negf = "";
		int poscount = 0;
		int negcount = 0;
		int total = 0;
		int posr = 0;
		int negr = 0;
		int count = 0;
		featurename = featurename.replaceAll(" ", "");
		String featureSentences = "";
		FileReader fr = new FileReader(file.getAbsolutePath());
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while ((line = br.readLine()) != null) {
			if (line.toLowerCase().contains(featurename.toLowerCase().trim())) {
				featureSentences += line + "\r\n";
				String[] sentiment = line.split("::");
				// System.out.println(sentiment[1]);
				if (sentiment[1].equals("positive")
						|| sentiment[1].equals("strong_positive")
						|| sentiment[1].endsWith("weak_positive")) {
					 //System.out.println(line);
					poscount++;
					count++;
				} else if (sentiment[1].equals("negative")
						|| sentiment[1].equals("weak_negative")
						|| sentiment[1].equals("strong_negative")) {
					//System.out.println(line);
					negcount++;
					count++;
				}

			}
		}
		br.close();
		// System.out.println("count" + count);
		if (count > 1) {
			//System.out.println("pos: " + poscount + " neg: " + negcount);
			total = negcount + poscount;
			posr = (poscount * 100) / total;

			findSentences(featureSentences, featurename, posr,feature);
		} else
			System.out.println("not found in database");
	}

	public String StemWords(String features) {
		PorterStemmer pt = new PorterStemmer();
		String textFromFile;
		String stemmed = "";
		String clean = pt.Clean(features);

		PorterStemmer porStem = new PorterStemmer(); // because we have no
														// static methods

		for (String word : clean.split(" ")) {
			stemmed += pt.stem(word) + " ";
		}

		return stemmed;

	}

	SentencePolarity sp = new SentencePolarity();

	public void findSentences(String featureSentences, String featurename, int posr,String feature)
			throws IOException {
		// ArrayList<String> list = Utility.filetoarray(featureSentences);
		String positive = null;
		String negative = null;
		String pospolarity = null;
		String negPolarity = null;
		for (String f : featureSentences.split("\r\n")) {
			//System.out.println(f);
		String[] s = f.split("::");
			
				if (s[1].equalsIgnoreCase("strong_positive")
						|| s[1].equals("positive")
						|| s[1].equals("weak_positive")){
					if (!posSentence.contains(s[0])) {
						
					if (positive == null) {
						
						pospolarity = s[1];
						positive = s[0];
						
					} 
				
					else if (pospolarity != s[1]) {
						
						if (pospolarity != "strong_positive"){
								
							if (s[1].equals("strong_positive") || s[1].equals("positive")) {
								pospolarity = s[1];
								positive = s[0];
							}
							}
					}
				}
				}
				else if (s[1].equals("strong_negative")
						|| s[1].equals("negative")
						|| s[1].equals("weak_negative")) {
					if (!negSentence.contains(s[0])) {
				//	System.out.println(s[0]+s[1]);
					if (negative == null) {
						negPolarity = s[1];
						negative = s[0];
					} else if (!negPolarity.equals(s[1])) {
						
						if (!negPolarity.equals("strong_negative")) {
							
							if (s[1].equals("strong_negative") || s[1].equals("negative")) {
								
								negPolarity = s[1];
								negative = s[0];
							}
						}
					}
				}
			}

		}
		
		posSentence.add(positive);
		negSentence.add(negative);
		if (bool) {
			
			System.out.println(feature.toUpperCase());
			if(positive!=null){
			
			System.out.println("positive " + posr + "%");
			System.out.println(positive);
			}
			if(negative!=null){
			
			System.out.println("\nnegative" + (posr - 100) + "%");
			System.out.println(negative);
			}
			bool = false;
			oldpositive = positive;
			oldnegative = negative;
		}
		else if (!bool){
			if((oldpositive.equals(positive) && oldnegative.equals(negative)) || (positive==null && negative==null)){
				stop=true;
				System.out.println("no more reviews");
			} 
		
			else {
				if (!oldpositive.equals(positive) && positive!=null) {
					System.out.println("\npositive");
					System.out.println(positive);
					oldpositive=positive;
					}
				if (!oldnegative.equals(negative) && negative!=null) {
					//System.out.println(">>>>>"+oldnegative);
					//System.out.println("<<<<<"+negative);
					System.out.println("\nnegative");
					System.out.println(negative);
					oldnegative=negative;
					//System.out.println(oldnegative);
				}
			}
		}

	}

	

		
}
		
	/*
	
	public static void main(String args[]) throws IOException {
		ClassA ca = new ClassA();
		ClassB obj = new ClassB();
		System.out.println("Queation:");
		 Scanner q=new Scanner(System.in);
		 String question=q.nextLine();
		 question=question.replace("?","");
		//String question = "how is the camera quality of Moto G ?";
		String FP = obj.extractProductname(question);
		//System.out.println(FP);
		String[] s = FP.split("==");

		File file = ca.findFile(s[1]);
		String feature = obj.StemWords(s[0]);
		obj.ExtractProductFeaturesSentiments(file, feature,s[0]);

		if (!bool) {
			while (!stop) {
				System.out.println("Do you want to see more reviews? (Y/N)");
				Scanner in = new Scanner(System.in);
				String review = in.next();
				if (review.toLowerCase().equals("y")) {
					obj.ExtractProductFeaturesSentiments(file, feature,s[0]);
				} else {
					System.out.println("good buy!");
					stop=true;
				}
				
			}
		}
	}
*/

