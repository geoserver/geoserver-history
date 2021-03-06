/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.wms_1_1_1;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.geoserver.catalog.DimensionPresentation;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.wms.WMSDimensionsTestSupport;

public class DimensionsVectorGetMapTest extends WMSDimensionsTestSupport {

    public void testNoDimension() throws Exception {
        BufferedImage image = getAsImage(
                "wms?service=WMS&version=1.1.1&request=GetMap&bbox=-180,-90,180,90"
                        + "&styles=&Format=image/png&width=80&height=40&srs=EPSG:4326&layers="
                        + getLayerId(V_TIME_ELEVATION), "image/png");

        // we should get everything black, all four squares
        assertPixel(image, 20, 10, Color.BLACK);
        assertPixel(image, 60, 10, Color.BLACK);
        assertPixel(image, 20, 30, Color.BLACK);
        assertPixel(image, 60, 30, Color.BLACK);
    }

    public void testElevationDefault() throws Exception {
        setupVectorDimension(ResourceInfo.ELEVATION, "elevation", DimensionPresentation.LIST, null);
        BufferedImage image = getAsImage("wms?service=WMS&version=1.1.1&request=GetMap"
                + "&bbox=-180,-90,180,90&styles=&Format=image/png&width=80&height=40&srs=EPSG:4326"
                + "&layers=" + getLayerId(V_TIME_ELEVATION), "image/png");

        // we should get only the first
        assertPixel(image, 20, 10, Color.BLACK);
        assertPixel(image, 60, 10, Color.WHITE);
        assertPixel(image, 20, 30, Color.WHITE);
        assertPixel(image, 60, 30, Color.WHITE);
    }

    public void testElevationSingle() throws Exception {
        setupVectorDimension(ResourceInfo.ELEVATION, "elevation", DimensionPresentation.LIST, null);
        BufferedImage image = getAsImage("wms?service=WMS&version=1.1.1&request=GetMap"
                + "&bbox=-180,-90,180,90&styles=&Format=image/png&width=80&height=40&srs=EPSG:4326"
                + "&layers=" + getLayerId(V_TIME_ELEVATION) + "&elevation=1.0", "image/png");

        // we should get only the second
        assertPixel(image, 20, 10, Color.WHITE);
        assertPixel(image, 60, 10, Color.BLACK);
        assertPixel(image, 20, 30, Color.WHITE);
        assertPixel(image, 60, 30, Color.WHITE);
    }

    public void testElevationListMulti() throws Exception {
        setupVectorDimension(ResourceInfo.ELEVATION, "elevation", DimensionPresentation.LIST, null);
        BufferedImage image = getAsImage("wms?service=WMS&version=1.1.1&request=GetMap"
                + "&bbox=-180,-90,180,90&styles=&Format=image/png&width=80&height=40&srs=EPSG:4326"
                + "&layers=" + getLayerId(V_TIME_ELEVATION) + "&elevation=1.0,3.0", "image/png");

        // we should get second and third
        assertPixel(image, 20, 10, Color.WHITE);
        assertPixel(image, 60, 10, Color.BLACK);
        assertPixel(image, 20, 30, Color.WHITE);
        assertPixel(image, 60, 30, Color.BLACK);
    }

    public void testElevationListExtra() throws Exception {
        // adding a extra elevation that is simply not there, should not break
        setupVectorDimension(ResourceInfo.ELEVATION, "elevation", DimensionPresentation.LIST, null);
        BufferedImage image = getAsImage("wms?service=WMS&version=1.1.1&request=GetMap"
                + "&bbox=-180,-90,180,90&styles=&Format=image/png&width=80&height=40&srs=EPSG:4326"
                + "&layers=" + getLayerId(V_TIME_ELEVATION) + "&elevation=1.0,3.0,5.0", "image/png");

        // we should get only second and third
        assertPixel(image, 20, 10, Color.WHITE);
        assertPixel(image, 60, 10, Color.BLACK);
        assertPixel(image, 20, 30, Color.WHITE);
        assertPixel(image, 60, 30, Color.BLACK);
    }

    public void testElevationInterval() throws Exception {
        // adding a extra elevation that is simply not there, should not break
        setupVectorDimension(ResourceInfo.ELEVATION, "elevation", DimensionPresentation.LIST, null);
        BufferedImage image = getAsImage("wms?service=WMS&version=1.1.1&request=GetMap"
                + "&bbox=-180,-90,180,90&styles=&Format=image/png&width=80&height=40&srs=EPSG:4326"
                + "&layers=" + getLayerId(V_TIME_ELEVATION) + "&elevation=1.0/3.0", "image/png");

        // we should get last three
        assertPixel(image, 20, 10, Color.WHITE);
        assertPixel(image, 60, 10, Color.BLACK);
        assertPixel(image, 20, 30, Color.BLACK);
        assertPixel(image, 60, 30, Color.BLACK);
    }

    public void testElevationIntervalResolution() throws Exception {
        // adding a extra elevation that is simply not there, should not break
        setupVectorDimension(ResourceInfo.ELEVATION, "elevation", DimensionPresentation.LIST, null);
        BufferedImage image = getAsImage("wms?service=WMS&version=1.1.1&request=GetMap"
                + "&bbox=-180,-90,180,90&styles=&Format=image/png&width=80&height=40&srs=EPSG:4326"
                + "&layers=" + getLayerId(V_TIME_ELEVATION) + "&elevation=0.0/4.0/2.0", "image/png");

        // we should get second and fourth
        assertPixel(image, 20, 10, Color.BLACK);
        assertPixel(image, 60, 10, Color.WHITE);
        assertPixel(image, 20, 30, Color.BLACK);
        assertPixel(image, 60, 30, Color.WHITE);
    }

    public void testTimeDefault() throws Exception {
        setupVectorDimension(ResourceInfo.TIME, "time", DimensionPresentation.LIST, null);
        BufferedImage image = getAsImage("wms?service=WMS&version=1.1.1&request=GetMap"
                + "&bbox=-180,-90,180,90&styles=&Format=image/png&width=80&height=40&srs=EPSG:4326"
                + "&layers=" + getLayerId(V_TIME_ELEVATION), "image/png");

        // we should get only the last one (current)
        assertPixel(image, 20, 10, Color.WHITE);
        assertPixel(image, 60, 10, Color.WHITE);
        assertPixel(image, 20, 30, Color.WHITE);
        assertPixel(image, 60, 30, Color.BLACK);
    }

    public void testTimeCurrent() throws Exception {
        setupVectorDimension(ResourceInfo.TIME, "time", DimensionPresentation.LIST, null);
        BufferedImage image = getAsImage("wms?service=WMS&version=1.1.1&request=GetMap"
                + "&bbox=-180,-90,180,90&styles=&Format=image/png&width=80&height=40&srs=EPSG:4326"
                + "&layers=" + getLayerId(V_TIME_ELEVATION) + "&time=CURRENT", "image/png");

        // we should get only the last one (current)
        assertPixel(image, 20, 10, Color.WHITE);
        assertPixel(image, 60, 10, Color.WHITE);
        assertPixel(image, 20, 30, Color.WHITE);
        assertPixel(image, 60, 30, Color.BLACK);
    }

    public void testTimeSingle() throws Exception {
        setupVectorDimension(ResourceInfo.TIME, "time", DimensionPresentation.LIST, null);
        BufferedImage image = getAsImage("wms?service=WMS&version=1.1.1&request=GetMap"
                + "&bbox=-180,-90,180,90&styles=&Format=image/png&width=80&height=40&srs=EPSG:4326"
                + "&layers=" + getLayerId(V_TIME_ELEVATION) + "&time=2011-05-02", "image/png");

        // we should get only the second
        assertPixel(image, 20, 10, Color.WHITE);
        assertPixel(image, 60, 10, Color.BLACK);
        assertPixel(image, 20, 30, Color.WHITE);
        assertPixel(image, 60, 30, Color.WHITE);
    }

    public void testTimeListMulti() throws Exception {
        setupVectorDimension(ResourceInfo.TIME, "time", DimensionPresentation.LIST, null);
        BufferedImage image = getAsImage("wms?service=WMS&version=1.1.1&request=GetMap"
                + "&bbox=-180,-90,180,90&styles=&Format=image/png&width=80&height=40&srs=EPSG:4326"
                + "&layers=" + getLayerId(V_TIME_ELEVATION) + "&time=2011-05-02,2011-05-04",
                "image/png");

        // we should get only second and fourth
        assertPixel(image, 20, 10, Color.WHITE);
        assertPixel(image, 60, 10, Color.BLACK);
        assertPixel(image, 20, 30, Color.WHITE);
        assertPixel(image, 60, 30, Color.BLACK);
    }

    public void testTimeListExtra() throws Exception {
        // adding a extra elevation that is simply not there, should not break
        setupVectorDimension(ResourceInfo.TIME, "time", DimensionPresentation.LIST, null);
        BufferedImage image = getAsImage("wms?service=WMS&version=1.1.1&request=GetMap"
                + "&bbox=-180,-90,180,90&styles=&Format=image/png&width=80&height=40&srs=EPSG:4326"
                + "&layers=" + getLayerId(V_TIME_ELEVATION)
                + "&time=2011-05-02,2011-05-04,2011-05-10", "image/png");

        // we should get only second and fourth
        assertPixel(image, 20, 10, Color.WHITE);
        assertPixel(image, 60, 10, Color.BLACK);
        assertPixel(image, 20, 30, Color.WHITE);
        assertPixel(image, 60, 30, Color.BLACK);
    }

    public void testTimeInterval() throws Exception {
        // adding a extra elevation that is simply not there, should not break
        setupVectorDimension(ResourceInfo.TIME, "time", DimensionPresentation.LIST, null);
        BufferedImage image = getAsImage("wms?service=WMS&version=1.1.1&request=GetMap"
                + "&bbox=-180,-90,180,90&styles=&Format=image/png&width=80&height=40&srs=EPSG:4326"
                + "&layers=" + getLayerId(V_TIME_ELEVATION) + "&time=2011-05-02/2011-05-05",
                "image/png");

        // last three
        assertPixel(image, 20, 10, Color.WHITE);
        assertPixel(image, 60, 10, Color.BLACK);
        assertPixel(image, 20, 30, Color.BLACK);
        assertPixel(image, 60, 30, Color.BLACK);
    }

    public void testTimeIntervalResolution() throws Exception {
        // adding a extra elevation that is simply not there, should not break
        setupVectorDimension(ResourceInfo.TIME, "time", DimensionPresentation.LIST, null);
        BufferedImage image = getAsImage("wms?service=WMS&version=1.1.1&request=GetMap"
                + "&bbox=-180,-90,180,90&styles=&Format=image/png&width=80&height=40&srs=EPSG:4326"
                + "&layers=" + getLayerId(V_TIME_ELEVATION) + "&time=2011-05-01/2011-05-04/P2D",
                "image/png");

        // first and third
        assertPixel(image, 20, 10, Color.BLACK);
        assertPixel(image, 60, 10, Color.WHITE);
        assertPixel(image, 20, 30, Color.BLACK);
        assertPixel(image, 60, 30, Color.WHITE);
    }

}
