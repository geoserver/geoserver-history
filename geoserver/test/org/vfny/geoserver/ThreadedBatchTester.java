/*
 * Created on Feb 16, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ThreadedBatchTester purpose.
 * <p>
 * Description of ThreadedBatchTester ...
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: ThreadedBatchTester.java,v 1.4 2004/03/04 20:47:48 dmzwiers Exp $
 */
public class ThreadedBatchTester {
	private static int runs = 100;
	private static URL url;
	private static boolean isPost;
	private static String req = "";
	private static File log;
	
	public static void main(String[] args) {
		try{
			loadArgs(args);
			Thread[] threads = new Thread[runs];
			if(isPost){
				for(int i=0;i<runs;i++)
						threads[i] = new TestPostThread(url,req);
			}else{
				if(url == null)
					url = new URL(req);
				for(int i=0;i<runs;i++)
					threads[i] = new TestGetThread(url);
			}
			

			for(int i=0;i<runs;i++)
				threads[i].run();

			PrintStream os = System.out;
			if(log!=null && log.canWrite()){
				os = new PrintStream(new FileOutputStream(log));
			}
			
			generateOutput(threads,os);
		}catch(Exception e){
			e.printStackTrace();
			usage();
		}
	}
	
	private static void generateOutput(Thread[] threads, PrintStream os){
		int good = 0;
		for(int i=0;i<runs;i++){
			switch (((TestGetThread)threads[i]).getResult()){
				case HttpURLConnection.	HTTP_OK:
					good++;
				default:
			}
		}
		os.println(good+"/"+runs+" Tests 'OK' ("+((good*1.0)/(runs*1.0))+")\n");
		for(int i=0;i<runs;i++){
			TestGetThread tpt = (TestGetThread) threads[i];
			os.print(tpt.getResult()+", ");
			os.print(tpt.getTime1()+", ");
			os.print(tpt.getTime2()+", ");
			os.print(tpt.getTime3()+"\n");
		}
	}
	
	private static void loadArgs(String[] args) throws IOException{
		if(args.length==0)
			return;
		int i = 0;
		while(i<args.length){
			String key = args[i++];
			if("-n".equals(key) && i<args.length){
				String val = args[i++];
				runs = Integer.parseInt(val);
			}else{
			if("-r".equals(key) && i<args.length){
				String val = args[i++];
				File f = new File(val);
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String t = "";
				while(br.ready())
					t += br.readLine();
				req = t;
			}else{
			if("-u".equals(key) && i<args.length){
				String val = args[i++];
			  	url = new URL(val);
			}else{
			if("-l".equals(key) && i<args.length){
				String val = args[i++];
			  	log = new File(val);
			}else{
			if("-p".equals(key)){
			  	isPost = true;
			}else{// usage
				usage();
			}}}}}
		}
	}
	
	static void usage(){
		System.out.println("USAGE:\n");
		System.out.println("ThreadedBatchTester [-p][-n] [-r | -u]");
		System.out.println("-n\t Optional\t Number of duplicate requests to create and run.");
		System.out.println("-n\t Optional\t Number of duplicate requests to create and run.");
		System.out.println("-r\t Optional\t Mutually Exclusive with -u\t The file containing the request to execute.");
		System.out.println("-u\t Optional\t Mutually Exclusive with -r\t The URL to execute.");
		System.out.println("-l\t Optional\t The Log file.");
	}
}
