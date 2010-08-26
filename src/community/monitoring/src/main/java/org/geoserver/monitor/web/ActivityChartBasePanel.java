package org.geoserver.monitor.web;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.markup.html.panel.Panel;
import org.geoserver.monitor.Monitor;
import org.geoserver.monitor.MonitorQuery;
import org.geoserver.monitor.RequestData;
import org.geoserver.monitor.RequestDataVisitor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

public abstract class ActivityChartBasePanel extends Panel {
    
    protected static long PAGE_OFFSET = 1000;
    protected static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public ActivityChartBasePanel(String id, Monitor monitor) {
        super(id);
        
        Date[] range = getDateRange();
        
        MonitorQuery q = new MonitorQuery();
        q.between(range[0], range[1]);
        
        DataGatherer gatherer = new DataGatherer();
        monitor.query(q, gatherer);
        
        HashMap<RegularTimePeriod,Integer> data = gatherer.getData();
        
        Class timeUnitClass = getTimePeriod(range[0]).getClass();
        TimeSeries series = new TimeSeries("foo", timeUnitClass);
        for (Map.Entry<RegularTimePeriod, Integer> d : data.entrySet()) {
            series.add(new TimeSeriesDataItem(d.getKey(), d.getValue()));
        }
        
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);
        
        final JFreeChart chart = ChartFactory.createTimeSeriesChart(getChartTitle(range), 
            timeUnitClass.getSimpleName(), "Requests", dataset, false, false, false);
        chart.setBackgroundPaint(Color.WHITE);
        chart.setAntiAlias(true);
        
        BufferedDynamicImageResource resource = new BufferedDynamicImageResource();
        resource.setImage(chart.createBufferedImage(650,500));
        add(new NonCachingImage("chart", resource));
    }
    
    class DataGatherer implements RequestDataVisitor {

        HashMap<RegularTimePeriod,Integer> data = new HashMap<RegularTimePeriod, Integer>();
        
        public void visit(RequestData r) {
            RegularTimePeriod period = getTimePeriod(r.getStartTime());
            Integer count = data.get(period);
       
            if (count == null) {
                count = new Integer(1);
            }
            else {
                count = new Integer(count.intValue()+1);
            }
            
            data.put(period,count);
        }
        
        public HashMap<RegularTimePeriod, Integer> getData() {
            return data;
        }
    }
    
    protected String getChartTitle(Date[] range) {
        return "Activity " + FORMAT.format(range[0]) + " - " + FORMAT.format(range[1]);
    }
    
    protected abstract Date[] getDateRange();
    
    protected abstract RegularTimePeriod getTimePeriod(Date time);
    
    

}
