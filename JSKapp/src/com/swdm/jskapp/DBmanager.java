package com.swdm.jskapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DBmanager extends Thread {

	ArrayList<NameValuePair> nameValuePairs;

	private static final int JSON_ARR = 5;
	String query;
	String result = "";
	Handler mainHandler;
	/**
	 * @file DBmanger.java
	 * @category Constructor
	 * @param none
	 * @author Sungsoo Kim
	 * @return none
	 * @version ver.1
	 * @code This class initializes the variables. 
	 * @endcode
	 */	

	public DBmanager(Handler mHandler, String query) {
		nameValuePairs = new ArrayList<NameValuePair>();
		mainHandler = mHandler;
		this.query = query;
	}

	/**
	 * @file DBmanger.java
	 * @category Constructor
	 * @param String
	 * @author Sungsoo
	 * @return none
	 * @version ver.1
	 * @code This class initializes the variables. 
	 * @endcode
	 */	

	public DBmanager(String query) {
		nameValuePairs = new ArrayList<NameValuePair>();
		this.query = query;

	}

	/**
	 * @file DBmanger.java
	 * @category Request query
	 * @param String
	 * @author Sungsoo Kim
	 * @return none
	 * @version ver.1
	 * @code This class request query which needs result values.
	 * @endcode
	 */	

	public JSONArray requestQuery(String sql) {

		InputStream is = null;
		nameValuePairs.add(new BasicNameValuePair("sql",sql));

		nameValuePairs.add(new BasicNameValuePair("sql2","set names utf8"));
		// [HTTP POST]
		try{		
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://sungsoo.my.dude.kr/getter.php");
			Log.i("Log_tag","5 - client & post");

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			Log.i("Log_tag","6 - entity");

			HttpResponse response = httpclient.execute(httppost);
			Log.i("Log_tag","7 - response");

			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			Log.i("Log_tag","8 - content");

		}catch(Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		//	Log.e("log_tag", "Result: "+ result);

		// [RESPONSE -> STRING]
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				Log.i("log_tag", "line: "+line);
				sb.append(line + "\n");
			}

			is.close();
			result = sb.toString();
			//	Log.i("log_tag", "Result: "+ result);

		}catch(Exception e) {
			Log.e("log_tag", "Error converting result "+e.toString());
		}

		// [PARSING DATA]
		try{
			if(result.equals("1\n")) Log.e("Log_tag", "Error: Connection");
			if(result.equals("2\n")) Log.e("Log_tag", "Error: Selection");
			if(result.equals("3\n")) Log.e("Log_tag", "Error: Request");
			if(result.equals("4\n")) Log.e("Log_tag", "Error: Sql query");

			// bring data from database
			else {
				JSONArray jArray = new JSONArray(result);
			
				return jArray;
			}
		}catch(JSONException e) {
			Log.e("Log_tag", "Error: Parsing data" + e.toString());
		}
		return null;
	}

	/**
	 * @file DBmanger.java
	 * @category Send query
	 * @param String
	 * @author Sungsoo Kim
	 * @return none
	 * @version ver.1
	 * @code This class request query which doesn't need result values.
	 * @endcode
	 */	

	public boolean sendQuery(String sql) {

		InputStream is = null;
		nameValuePairs.add(new BasicNameValuePair("sql",sql));
		nameValuePairs.add(new BasicNameValuePair("sql2","set names utf8"));
		// [HTTP POST]
		try {	
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://sungsoo.my.dude.kr/setter.php");
			Log.i("Log_tag","10 - client & post");

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));
			Log.i("Log_tag","11 - entity");

			HttpResponse response = httpclient.execute(httppost);
			Log.i("Log_tag","12 - response");

			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			Log.i("Log_tag","13 - content");

		}catch(Exception e) {
			Log.e("Log_tag", "Error: Http connection" + e.toString());
		}

	
		if(result.equals("1\n")) Log.e("Log_tag", "Error: Connection");
		if(result.equals("2\n")) Log.e("Log_tag", "Error: Selection");
		if(result.equals("3\n")) Log.e("Log_tag", "Error: Request");
		if(result.equals("4\n")) Log.e("Log_tag", "Error: Sql query");

		if(result.equals("5\n")) {
			Log.i("Log_tag", "Info: query success");

			return true;
		}

		return false;
	}

	/**
	 * @file DBmanger.java
	 * @category Run 
	 * @param none
	 * @author Sungsoo Kim
	 * @return none
	 * @version ver.1
	 * @code This class processes the requested query
	 * @endcode
	 */	

//	@Override
//	public void run() {
//		JSONArray arr = requestQuery(query);
//		//sendQuery(query);
//		Message.obtain(mainHandler, JSON_ARR, arr).sendToTarget();
//		//Log.e("END_OF_RUN",query);
//	}
	
	public void start3() {
		sendQuery(query);
	}
	
	public void start1() {
		boolean arr = sendQuery(query);
		//sendQuery(query);
		Message.obtain(mainHandler, 0, arr).sendToTarget();
		//Log.e("END_OF_RUN",query);
	}
	
	public void start2() {
		JSONArray arr = requestQuery(query);
		//sendQuery(query);
		Message.obtain(mainHandler, JSON_ARR, arr).sendToTarget();
		//Log.e("END_OF_RUN",query);
	}
}
