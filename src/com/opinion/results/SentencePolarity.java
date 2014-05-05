package com.opinion.results;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import opennlp.model.DataReader;

public class SentencePolarity {
	private String pathToSWN = "D://Projects/Thesis/Sentiwordnet/swn/www/admin/dump/SentiWordNet_3.0.0_20130122.txt";
	private HashMap<String, Double> _dict;
	public SentencePolarity() {
		_dict = new HashMap<String, Double>();
		HashMap<String, Vector<Double>> _temp = new HashMap<String, Vector<Double>>();

		try {
			int lineNumber = 0;

			BufferedReader csv = new BufferedReader(new FileReader(pathToSWN));
			String line = "";
			while ((line = csv.readLine()) != null) {
				lineNumber++;
				// Double score = 0.0;
				// String[] words = null;
				String[] data = line.split("\t");
				if (data.length != 6) {
					// System.out.println("continue");
					continue;
				} else {
					// for(int i=0;i<data.length;i++)

					// data[i] = data[i].replaceAll("([^a-zA-Z\\s])","");

					Double score = Double.parseDouble(data[2])
							- Double.parseDouble(data[3]);

					String[] words = data[4].split(" ");

					for (String w : words) {
						String[] w_n = w.split("#");
						w_n[0] += "#" + data[0];
						// float xxx=Float.parseFloat(w_n[1]);
						// System.out.println(xxx);
						int index = Integer.parseInt(w_n[1]) - 1;
						if (_temp.containsKey(w_n[0])) {
							Vector<Double> v = _temp.get(w_n[0]);
							if (index > v.size())
								for (int i = v.size(); i < index; i++)
									v.add(0.0);
							v.add(index, score);
							_temp.put(w_n[0], v);
						} else {
							Vector<Double> v = new Vector<Double>();
							for (int i = 0; i < index; i++)
								v.add(0.0);
							v.add(index, score);
							_temp.put(w_n[0], v);
						}
					}
				}
			}

			Set<String> temp = _temp.keySet();

			for (Iterator<String> iterator = temp.iterator(); iterator
					.hasNext();) {

				String word = iterator.next();
				Vector<Double> v = _temp.get(word);
				double score = 0.0;
				double sum = 0.0;

				for (int i = 0; i < v.size(); i++) {
					score += ((double) 1 / (double) (i + 1)) * v.get(i);
				}
				for (int i = 1; i <= v.size(); i++)
					sum += (double) 1 / (double) i;
				score /= sum;

				String sent = "";

				if(score>=0.75)
			        sent = "strong_positive";
			    else
			    if(score > 0.25 && score<=0.5)
			        sent = "positive";
			    else
			    if(score > 0 && score>=0.25)
			        sent = "weak_positive";
			    else
			    if(score < 0 && score>=-0.25)
			        sent = "weak_negative";
			    else
			    if(score < -0.25 && score>=-0.5)
			        sent = "negative";
			    else
			    if(score<=-0.75)
			        sent = "strong_negative";
			    else
			    if(score==0.00)
			    	sent="neutral";
				// System.out.println("word: "+word+" score: "+score+" sent: "+sent);
				_dict.put(word, score);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


		public Double extract(String word) {
		Double total = new Double(0);
		if (_dict.get(word + "#n") != null)
			total = _dict.get(word + "#n") + total;
		if (_dict.get(word + "#a") != null)
			total = _dict.get(word + "#a") + total;
		if (_dict.get(word + "#r") != null)
			total = _dict.get(word + "#r") + total;
		if (_dict.get(word + "#v") != null)
			total = _dict.get(word + "#v") + total;
		return total;
	}

	public static void main(String[] args) throws IOException {

		SentencePolarity test = new SentencePolarity();

		
		File folder = new File("C://project/sentimentDatabase/preprocessing/");
		File[] file = folder.listFiles();
		File f=new File("C://project/sentimentDatabase/stopWords.txt");
		FileReader fr=new FileReader(f);
		BufferedReader br=new BufferedReader(fr);
		String Str="";
		String stopwords="";
		while((Str=br.readLine())!=null)
		{
			stopwords+=Str+" ";
		}
		br.close();
		fr.close();
		for (int i = 0; i < file.length; i++) {

			String pathtofile = file[i].getAbsolutePath();
			BufferedReader br1 = new BufferedReader(new FileReader(pathtofile));
			String sentence;
			String document = "";
			while ((sentence = br1.readLine()) != null) {
				String[] words = sentence.split("\\s+");
				
				double totalScore = 0;
				for (String word : words) {
					if(stopwords.contains(word))
						continue;
					else{
						word = word.replaceAll("([^a-zA-Z\\s])", "");
					//System.out.println(word);
					if (test.extract(word) == null)
						continue;
					else
						totalScore +=(((double)1/(i+2))*test.extract(word));
					}
				}
				
				String sent = "";
				 if(totalScore>=0.75)
				        sent = "strong_positive";
				    else
				    if(totalScore > 0.25 && totalScore<0.75)
				        sent = "positive";
				    else
				    if(totalScore > 0 && totalScore>=0.25)
				        sent = "weak_positive";
				    else
				    if(totalScore < 0 && totalScore>=-0.25)
				        sent = "weak_negative";
				    else
				    if(totalScore < -0.25 && totalScore>-0.75)
				        sent = "negative";
				    else
				    if(totalScore<=-0.75)
				        sent = "strong_negative";
				    else
					    sent="neutral";
						
				// System.out.println(sentence+"::"+sent);
				document += sentence + "::" + sent+"\r\n" ;
				//System.out.println(file[i].getName()+"\t"+sentence+"\t"+sent);
			}
		
			File f1 = new File("C://project/sentimentDatabase/opinionMining/SentencePolarity/"
					+ file[i].getName());
			if (!f1.exists())
				f1.createNewFile();
			FileWriter fw = new FileWriter(f1.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			String[] doc=document.split("\r\n");
			for(String d:doc)
				{
				bw.write(d+"\r\n");
		//		System.out.println(d);
				}
			bw.close();
			System.out.println(file[i].getName());
		}
		System.out.println("done");
	}
}
