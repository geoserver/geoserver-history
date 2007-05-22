/* Copyright (c) 2001, 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.stress.jmeter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


/**
 * Runs the specified jmeter tests with an increasing number of parallel threads
 * TODO: add decent reporting, maybe with a ready to paste confluence syntax
 *
 */
public class JMeterRunner {
    private File jmeter;
    private File jmeterJar;
    private File javaBin;
    private File javaExe;
    private File logDir;

    public JMeterRunner() throws IOException {
        // lookup jmeter and java
        Properties p = new Properties();
        p.load(new FileInputStream("./stress.properties"));
        jmeter = new File(p.getProperty("jmeter.home"));
        jmeterJar = new File(jmeter, "bin/ApacheJMeter.jar");
        javaBin = new File(System.getProperty("java.home"), "bin");
        javaExe = new File(javaBin, "java");
        logDir = new File("./target/log");

        if (!logDir.exists()) {
            logDir.mkdirs();
        }

        if (!javaExe.exists()) {
            javaExe = new File(javaBin, "java.exe");
        }
    }

    /**
     * Runs the specified test, looping a number of times. Each loop will double
     * the number of threads hitting the server
     *
     * @param planName
     * @param loops
     * @throws IOException
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    public void runTest(String planName, int loops)
        throws IOException, FileNotFoundException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        File plan = new File("./plans/" + planName + ".jmx");
        int users = 1;
        System.out.println("Running " + plan.getName());

        for (int i = 0; i < loops; i++) {
            File logFile = new File(logDir, plan.getName() + "-" + i + ".log");

            if (logFile.exists()) {
                logFile.delete();
            }

            String[] jmeterArgs = new String[] {
                    javaExe.getAbsolutePath(), "-jar", jmeterJar.getAbsolutePath(),
                    "-Jthreadcount=" + users, "-n", "-t", plan.getAbsolutePath(), "-l",
                    logFile.getAbsolutePath()
                };

            // execute the process, and report out the summary results
            System.out.print(users + " concurrent threads -> ");

            Process process = runtime.exec(jmeterArgs, null, jmeter);
            InputStream stdout = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Generate Summary Results =")) {
                    System.out.println(line);
                }
            }

            process.waitFor();

            // double the thread number and repeat
            users = users * 2;
        }
    }
}
