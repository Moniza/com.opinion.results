package com.opinion.results;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Jsonformat {

		public String returnjsonforClassB(ArrayList<String> posSentence,ArrayList<String> negSentence,String feature,int posr) throws JSONException
	{
		
		JSONObject obj=new JSONObject();
		JSONArray arr=new JSONArray();
		JSONObject obj1=new JSONObject();
		obj.put("attribute",feature);
		if(posSentence.size()>0)
		{
			obj.put("positive",posr);
		
		for(int i=0;i<posSentence.size();i++)
		{
			obj.put("pos"+i,posSentence.get(i));
		}
		}
		if(negSentence.size()>0){
			int negr=100-posr;
			obj.put("negative",negr);
		for(int i=0;i<negSentence.size();i++)
		{
			obj.put("neg"+i,negSentence.get(i));
		}
		}
		arr.put(obj);
		obj1.put("attributes",arr);
		String answer=obj1.toString();
		return answer;
	}
}
	

