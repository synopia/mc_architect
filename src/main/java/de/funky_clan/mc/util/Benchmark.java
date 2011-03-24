package de.funky_clan.mc.util;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author synopia
 */
@Singleton
public class Benchmark {
    private final Logger logger = LoggerFactory.getLogger(Benchmark.class);
    private final HashMap<Long, Long> timeData = new HashMap<Long, Long>();
    private final HashMap<String, List<Long>> threadGroups = new HashMap<String, List<Long>>();
    private long startTime;

    public synchronized void addThreadId( String name, long id ) {
        logger.info("ThreadId "+id+" added to group "+name+" (EDT="+ SwingUtilities.isEventDispatchThread() +")");
        List<Long> ids;
        if( !threadGroups.containsKey(name) ) {
            ids = new ArrayList<Long>();
            threadGroups.put(name, ids);
        } else {
            ids = threadGroups.get(name);
        }
        ids.add(id);
    }

    public synchronized void addThreadIds( String name, long... ids) {
        for (long id : ids) {
            addThreadId(name, id);
        }
    }

    public synchronized HashMap<String, Double> getResults() {
        HashMap<String, Double> result = null;
        long now = System.nanoTime();
        if( startTime>0 ) {
            long totalTime = now - startTime;
            result = captureThreadInfo(totalTime);
        }
        startTime = now;
        return result;
    }

    protected HashMap<String, Double> captureThreadInfo( long totalTime ) {
        HashMap<String, Double> result = new HashMap<String, Double>();
        final ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        for (String name : threadGroups.keySet()) {
            List<Long> ids = threadGroups.get(name);
            long groupTime = 0;
            for (Long id : ids) {
               final long c = bean.getThreadUserTime(id);
                if( c==-1 ) {
                    continue;
                }
                Long startTime= timeData.get(id);
                if( startTime!=null ) {
                    groupTime += c-startTime;
                }
                timeData.put(id, c);
            }
            result.put(name, (double )groupTime/totalTime );
        }
        return result;
    }
}
