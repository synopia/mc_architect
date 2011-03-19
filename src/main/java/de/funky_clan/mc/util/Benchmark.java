package de.funky_clan.mc.util;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author synopia
 */
public class Benchmark {
    private HashMap<Object, Long> runningBenchmarks = new HashMap<Object, Long>();
    private HashMap<Object, Long> usage = new HashMap<Object, Long>();
    private long startTime;

    public synchronized void startBenchmark( Object obj ) {
        runningBenchmarks.put(obj, System.nanoTime() );
    }

    public synchronized void endBenchmark( Object obj ) {
        long startTime = runningBenchmarks.get(obj);
        long time = System.nanoTime() - startTime;
        runningBenchmarks.remove(obj);

        if( usage.containsKey(obj) ) {
            usage.put(obj, usage.get(obj)+time );
        } else {
            usage.put(obj, time );
        }
    }

    public synchronized HashMap<Object, Double> getResults() {
        HashMap<Object, Double> result = new HashMap<Object, Double>();
        long now = System.nanoTime();
        long totalTime = now - startTime;
        Iterator<Object> it = usage.keySet().iterator();
        while (it.hasNext()) {
            Object object =  it.next();

            long time = usage.get(object);
            it.remove();

            if( runningBenchmarks.containsKey(object) ) {
                time += now-runningBenchmarks.get(object);
            }
            result.put(object, (double )time/(double) totalTime );
        }
        startTime = now;
        return result;
    }
}
