package com.opinion.results;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class ClassifyQuestion {
	
	public String questionclassfinder(String url) throws IOException
	{
		
		//System.out.println("question class finder");
		String webpage = "";
		URL u;
		u = new URL(url);
		URLConnection conn = u.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String currentLine;
		while ((currentLine = br.readLine()) != null) {
			webpage = webpage + currentLine + "\n";
		}
		//System.out.println(webpage);
		return webpage;
	}
	
	public String returnAnswer(String question) throws IOException, JSONException{
		
		System.out.print("Question: "+question);
		String ques1=question.replaceAll(" ","%20");
		ClassifyQuestion cq=new ClassifyQuestion();
		String answer="";
		String temp=cq.questionclassfinder("http://127.0.0.1:5000/add?a="+ques1);
		int qclass=Integer.parseInt(temp.trim());
		
		if(qclass==1)
		{
			ClassB cb=new ClassB();
			String sentiments = "";
			cb.classBresult(question);
		}
		else if(qclass==2)
		{
			//System.out.println("class 3");
			ClassAResults car=new ClassAResults();
			String summary=car.classA(question);
			System.out.println(summary);
			answer=summary;
			
			
		}
	
		
		return answer;
	}
	
	/*
	public static void main(String args[]) throws IOException
	{
		
		System.out.print("Question: ");
		Scanner in=new Scanner(System.in);
		String ques=in.nextLine();
		String ques1=ques.replaceAll(" ","%20");
		ClassifyQuestion cq=new ClassifyQuestion();
		String temp=cq.questionclassfinder("http://127.0.0.1:5000/add?a="+ques1);
		int qclass=Integer.parseInt(temp.trim());
		//System.out.println("qclass is : "+qclass);
		if(qclass==1)
		{
			//System.out.println("class 1");
			ClassB cb=new ClassB();
			cb.classBresult(ques);
		}
		else if(qclass==3)
		{
			//System.out.println("class 3");
			ClassAResults car=new ClassAResults();
			String summary=car.classA(ques);
			System.out.println(summary);
			
			
		}
	}
	*/

}
