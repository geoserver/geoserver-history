/* Copyright (c) 2001, 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.stress.jmeter;

import java.io.IOException;


/**
 * Runs the WMS tests one after the other
 * @author Administrator
 *
 */
public class RunWMSTests {
    public static void main(String[] args) throws IOException, InterruptedException {
        final int loops = 5; // up to 64 concurrent threads

        JMeterRunner runner = new JMeterRunner();
        runner.runTest("StatesWMSGetMap", loops);
//        runner.runTest("StatesWMSGetMapSafePalette", loops);
//        runner.runTest("StatesWMSGetMapSafePaletteGif", loops);
    }
}
