package org.geoserver.geosync;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.Writer;
import java.io.PrintWriter;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.io.SyndFeedOutput;

public class GeoSyncController extends AbstractController {

    static final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");

    private RecordingTransactionListener myListener;

    public RecordingTransactionListener getListener(){
        return myListener;
    }

    public void setListener(RecordingTransactionListener rtl){
        myListener = rtl;
    }

    public ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse resp){
        resp.setContentType("text/plain");
        try{
            generateFeed(resp);
        } catch (Exception e){
            // what to do? output is borked?
            resp.setStatus(500);
            e.printStackTrace();
        }

        return null;
    }

    public void generateFeed(HttpServletResponse response) throws Exception{
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("atom_1.0");

        feed.setTitle("Geoserver History Feed");
        feed.setLink("http://geoserver.org/"); //TODO: get the local url and use that
        feed.setDescription("Changes for feature type "); // TODO: get the feature type name and use that

        List history = myListener.getHistoryList(); 

        feed.setEntries(encodeHistory(history));

        SyndFeedOutput out = new SyndFeedOutput();
        Writer writer = response.getWriter();
        out.output(feed, writer);
    }

    public List encodeHistory(List history) throws Exception{
        List entries = new ArrayList();

        Iterator it = history.iterator();
        while(it.hasNext()){
            String thingy = (String)it.next();
            System.out.println("Found thingy: " + thingy);
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle("Feature A");
            entry.setLink("http://geoserver.org/a");
            entry.setPublishedDate(DATE_PARSER.parse("2004-06-08"));
            SyndContent description = new SyndContentImpl();
            description.setType("text/plain");
            description.setValue(thingy);
            entry.setDescription(description);
            entries.add(entry);
        }

        return entries;
    }
}
