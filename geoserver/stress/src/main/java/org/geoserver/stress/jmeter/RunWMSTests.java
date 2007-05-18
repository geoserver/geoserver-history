package org.geoserver.stress.jmeter;

import java.io.IOException;

/**
 * Runs the WMS tests one after the other
 * @author Administrator
 *
 */
public class RunWMSTests {
    public static void main(String[] args) throws IOException, InterruptedException {
        final int loops = 7; // up to 64 concurrent threads

        JMeterRunner runner = new JMeterRunner();
        runner.runTest("StatesWMSGetMap", loops);
        runner.runTest("StatesWMSGetMapSafePalette", loops);
    }
}
