/*
 * Created on Feb 16, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * ThreadedBatchTester purpose.
 * <p>
 * Description of ThreadedBatchTester ...
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: ThreadedBatchTester.java,v 1.1 2004/02/17 01:14:29 dmzwiers Exp $
 */
public class ThreadedBatchTester {
	private static int runs = 100;
	private static URL url;
	
	public static void main(String[] args) {
		try{
			loadArgs(args);
			TestThread[] threads = new TestThread[runs];
			for(int i=0;i<runs;i++)
				threads[i] = new TestThread(url);

			for(int i=0;i<runs;i++)
				threads[i].run();

			int good = 0;
			for(int i=0;i<runs;i++){
				switch (threads[i].getResult()){
					case HttpURLConnection.	HTTP_OK:
						good++;
					default:
				}
			}
			System.out.println(good+"/"+runs+" Tests 'OK' ("+((good*1.0)/(runs*1.0))+")");
		}catch(Exception e){
			e.printStackTrace();
			usage();
		}
	}
	
	private static void loadArgs(String[] args) throws IOException{
		if(args.length==0)
			return;
		int i = 0;
		while(i+1<args.length){
			String key = args[i++];
			String val = args[i++];
			if("-n".equals(key)){
				runs = Integer.parseInt(val);
			}else{
			if("-f".equals(key)){
				File f = new File(val);
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String t = "";
				while(br.ready())
					t += br.readLine();
				url = new URL(t);
			}else{
			if("-u".equals(key)){
			  	url = new URL(val);
			}else{// usage
				usage();
			}}}
		}
	}
	
	static void usage(){
		System.out.println("USAGE:\n");
		System.out.println("ThreadedBatchTester [ -n ] [-f | -u ]");
		System.out.println("-n\t Optional\t Number of duplicate requests to create and run.");
		System.out.println("-f\t Optional\t Mutually Exclusive with -u\t The file containing the URL to execute.");
		System.out.println("-u\t Optional\t Mutually Exclusive with -f\t The URL to execute.");
	}
}

class TestThread extends Thread{
	URL url;
	int result = 0;
	TestThread(URL u) throws MalformedURLException{
		url = new URL(u.toString());
	}
	
	int getResult(){
		return result;
	}
	
	public void run(){
		try{
			HttpURLConnection hc = (HttpURLConnection)url.openConnection();
			hc.connect();
			yield(); //wait to let everyone else connect before getting result
			result = hc.getResponseCode();
			yield(); //wait to let everyone else hit before disconnecting
			hc.disconnect();
		}catch(Exception e){
			e.printStackTrace();
			result = 0;
		}
	}
}
