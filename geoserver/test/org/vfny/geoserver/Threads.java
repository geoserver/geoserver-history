package org.vfny.geoserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

class TestGetThread extends Thread{
	URL url;
	int result = 0;
	
	Date t1,t2;
	Date t3;
	
	TestGetThread(URL u) throws MalformedURLException{
		url = new URL(u.toString());
	}
	
	int getResult(){
		return result;
	}
	
	public void run(){
		try{
			HttpURLConnection hc = null;
			hc = (HttpURLConnection)url.openConnection();
			hc.setRequestMethod("GET");
			yield(); //wait to let everyone else connect before getting result

			t1 = new Date();
			hc.connect();
			t2 = new Date();
    		BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
    		while(br.ready())
    			br.readLine();
			result = hc.getResponseCode();
			t3 = new Date();
			yield(); //wait to let everyone else hit before disconnecting
			hc.disconnect();
		}catch(Exception e){
			e.printStackTrace();
			result = 0;
		}
	}
	
	Date getTime1(){
		return t1;
	}
	Date getTime2(){
		return t2;
	}
	Date getTime3(){
		return t3;
	}
}

class TestPostThread extends TestGetThread{
	
	String request;
	
	TestPostThread(URL u, String request) throws MalformedURLException{
		super(u);
		this.request = request;
	}
	
	public void run(){
		try{
			HttpURLConnection hc = null;
			hc = (HttpURLConnection)url.openConnection();
			hc.setRequestMethod("POST");
			hc.setDoOutput(true);
			yield(); //wait to let everyone else connect before getting result
			
			t1 = new Date();
			hc.connect();
			OutputStreamWriter osw = new OutputStreamWriter(hc.getOutputStream());
			osw.write(request);
			osw.flush();
			
			t2 = new Date();
    		BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));
    		while(br.ready()){
    			br.readLine();
    		}
			result = hc.getResponseCode();
			t3 = new Date();
			yield(); //wait to let everyone else hit before disconnecting
			hc.disconnect();
		}catch(Exception e){
			e.printStackTrace();
			result = 0;
		}
	}
}