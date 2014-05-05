package com.opinion.results;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ClassAResults {

	public String extractProductname(String question) throws IOException {
		String productname="";
		String[] quesKeywords = question.split(" ");
		for (String q : quesKeywords) {
			if (Utility.stopWords.contains(q.toLowerCase()))
				continue;
			else
				productname += q + " ";
		}
		return productname.toLowerCase();
	}

	public File findFile(String productname) {
		String[] questionProduct = productname.split(" ");
		File file = null;
		File folder = new File(
				"C://project/sentimentDatabase/opinionMining/jsonsummary");
		File[] files = folder.listFiles();
		for (File f : files) {
			int count = 0;
			String filename = f.getName().replace(".json", "");
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
				//System.out.println("file found>> " + f.getName());
				break;
			}
		}
		}
		return file;
		
	}
	
	public String readfile(File file) throws IOException, JSONException
	{
		System.out.println(file.getAbsolutePath());
		FileReader fr=new FileReader(file.getAbsolutePath());
		BufferedReader br=new BufferedReader(fr);
		String str="";
		String summary="";
		while((str=br.readLine())!=null)
		{
			
			summary+=str;
		}
		 	
		return summary;
	}
	
	public String classA(String question) throws IOException, JSONException
	{
		//System.out.println("class a");
		question=question.replace("?","");
		ClassAResults car=new ClassAResults();
		String productname=car.extractProductname(question);
		File file=car.findFile(productname);
		String summary=car.readfile(file);
		return summary;	
		
		
	}
	/*public static void main(String[] args) throws IOException {
		ClassAResults car=new ClassAResults();
		Scanner in=new Scanner(System.in);
		String question=in.nextLine();
		question=question.replace("?","");
		String productname=car.extractProductname(question);
		File file=car.findFile(productname);
		String summary=car.readfile(file);
		System.out.println(summary);
	}
*/
}
