/*
 * SUBFRAME - Simple Java Benchmarking Framework
 * Copyright (C) 2012 - 2013 Fabian Prasser
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.linearbits.subframe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.linearbits.subframe.Measures.Visibility;
import de.linearbits.subframe.analyzer.Analyzer;
import de.linearbits.subframe.io.CSVFile;

/**
 * The central benchmark class. Allows collecting measurements and returning the
 * results as a csv file
 * @author Fabian Prasser
 */
public class Benchmark {

    /** Id to label */
    private Map<Integer, String>            measureToString = new HashMap<Integer, String>();
    /** Label to id */
    private Map<String, Integer>            stringToMeasure = new HashMap<String, Integer>();
    /** Analyzer<?>s for each measurement */
    private Map<Integer, List<Analyzer<?>>> analyzers       = new HashMap<Integer, List<Analyzer<?>>>();
    /** All analyzers for each run */
    private List<Analyzer<?>[][]>           runs            = new ArrayList<Analyzer<?>[][]>();
    /** Data for each run */
    private List<String[]>                  runData         = new ArrayList<String[]>();
    /** The current run */
    private Analyzer<?>[][]                 currentRun      = null;
    /** The measures */
    protected Measures                      measures;
    /** Labels for run data */
    private final String[]                  runHeader;

    /**
     * Creates a new benchmark, with an additional column called "Run" for run data
     */
    public Benchmark() {
        this.runHeader = new String[] { "Run" };
    }

    /**
     * Creates a new benchmark, with the given additional columns for run data
     * @param runHeader
     */
    public Benchmark(String... runHeader) {
        this.runHeader = runHeader;
    }

    /**
     * Adds a new analyzer to the given measure
     * @param measure
     * @param analyzer
     */
    public synchronized void addAnalyzer(int measure, Analyzer<?> analyzer) {
        List<Analyzer<?>> list = analyzers.get(measure);
        if (list == null) {
            list = new ArrayList<Analyzer<?>>();
            analyzers.put(measure, list);
        }
        list.add(analyzer);
    }

    /**
     * Adds the CPU time of all threads
     * @see Measures#getCpuTime()
     * @param measure
     */
    public void addCpuTime(int measure) {
        this.addValue(measure, measures.getCpuTime());
    }

    /**
     * Adds the CPU time of the current thread
     * @see Measures#getCurrentThreadCpuTime()
     * @param measure
     */
    public void addCurrentThreadCpuTime(int measure) {
        this.addValue(measure, measures.getCurrentThreadCpuTime());
    }

    /**
     * Adds the system time of the current thread
     * @see Measures#getCurrentThreadSystemTime()
     * @param measure
     */
    public void addCurrentThreadSystemTime(int measure) {
        this.addValue(measure, measures.getCurrentThreadSystemTime());
    }

    /**
     * Adds the user time of the current thread
     * @see Measures#getCurrentThreadUserTime()
     * @param measure
     */
    public void addCurrentThreadUserTime(int measure) {
        this.addValue(measure, measures.getCurrentThreadUserTime());
    }

    /**
     * Adds the deep size of the given object
     * @see Measures#getDeepSize()
     * @param measure
     * @param object
     * @param filter
     * @param estimateArrays
     */
    public void addDeepSize(int measure, Object object, Visibility filter, boolean estimateArrays) {
        this.addValue(measure, measures.getDeepSize(object, filter, estimateArrays));
    }

    /**
     * Adds the deep size of the given object
     * @see Measures#getDeepSize()
     * @param measure
     * @param object
     */
    public void addDeepSize(int measure, Object object) {
        this.addValue(measure, measures.getDeepSize(object));
    }

    /**
     * Adds the free bytes as reported by MX management after a light gc
     * @see Measures#getUsedBytesGC()
     * @param measure
     */
    public void addFreeBytesGCMX(int measure) {
        this.addValue(measure, measures.getUsedBytesGCMX());
    }

    /**
     * Adds the free bytes after a gc
     * @see Measures#getUsedBytesGC()
     * @param measure
     */
    public void addFreeBytesGC(int measure) {
        this.addValue(measure, measures.getUsedBytesGC());
    }

    /**
     * Adds the free bytes as reported by MX management
     * @see Measures#getUsedBytesMX()
     * @param measure
     */
    public void addFreeBytesMX(int measure) {
        this.addValue(measure, measures.getUsedBytesMX());
    }

    /**
     * Adds the cpu time of the jvm
     * @see Measures#getJVMCpuTime()
     * @param measure
     */
    public void addJVMCpuTime(int measure) {
        this.addValue(measure, measures.getJVMCpuTime());
    }

    /**
     * Adds a measure to this benchmark. Returns its id.
     * @param label
     * @return
     */
    public synchronized int addMeasure(String label) {
        CSVFile.checkFieldValue(label);
        Integer id = stringToMeasure.get(label);
        if (id == null) {
            id = stringToMeasure.size();
            stringToMeasure.put(label, id);
            measureToString.put(id, label);
        }
        measures = new Measures(this.measureToString.size());
        return id;
    }
    
    /**
     * Creates a new run. The provided data becomes the run data.
     * @param data
     */
    public synchronized void addRun(Object... data) {
    	String[] strings = new String[data.length];
    	for (int i=0; i<data.length; i++){
    		strings[i] = String.valueOf(data[i]);
    	}
    	addRun(strings);
    }

    /**
     * Creates a new run. The provided data becomes the run data.
     * @param data
     */
    public synchronized void addRun(String... data) {
        for (String d : data) {
            CSVFile.checkFieldValue(d);
        }
        runData.add(data);
        if (data.length != runHeader.length) { throw new RuntimeException("Invalid run data"); }
        Analyzer<?>[][] run = new Analyzer<?>[analyzers.size()][];
        for (int i : analyzers.keySet()) {
            List<Analyzer<?>> list = analyzers.get(i);
            run[i] = new Analyzer<?>[list.size()];
            int index = 0;
            for (Analyzer<?> a : list) {
                run[i][index++] = a.newInstance();
            }
        }
        runs.add(run);
        currentRun = run;
    }

    /**
     * Adds the size of the object
     * @see Measures#getSize()
     * @param measure
     * @param obj
     */
    public void addSize(int measure, Object obj) {
        this.addValue(measure, measures.getSize(obj));
    }

    /**
     * Adds the system time of all threads
     * @see Measures#getSystemTime()
     * @param measure
     */
    public void addSystemTime(int measure) {
        this.addValue(measure, measures.getSystemTime());
    }

    /**
     * Stops the timer and adds its value
     * @see Measures#timerStop()
     * @param measure
     */
    public void addStopTimer(int measure) {
        this.addValue(measure, measures.stopTimer(measure));
    }

    /**
     * Stops the timer, adds its value and restarts it
     * @see Measures#timerStopAndStart()
     * @param measure
     */
    public void addStopAndStartTimer(int measure) {
        this.addValue(measure, measures.stopAndStartTimer(measure));
    }

    /**
     * Stops the measurement and adds its value
     * @see Measures#stopCurrentThreadCpuTime()
     * @param measure
     */
    public void addStopCurrentThreadCpuTime(int measure) {
        this.addValue(measure, measures.stopCurrentThreadCpuTime(measure));
    }

    /**
     * Stops the measurement and adds its value
     * @see Measures#stopCurrentThreadUserTime()
     * @param measure
     */
    public void addStopCurrentThreadUserTime(int measure) {
        this.addValue(measure, measures.stopCurrentThreadUserTime(measure));
    }

    /**
     * Stops the measurement and adds its value
     * @see Measures#stopCurrentThreadSystemTime()
     * @param measure
     */
    public void addStopCurrentThreadSystemTime(int measure) {
        this.addValue(measure, measures.stopCurrentThreadSystemTime(measure));
    }
    

    /**
     * Stops the measurement and adds its value
     * @see Measures#stopCurrentThreadCpuTime()
     * @param measure
     */
    public void addStopAndStartCurrentThreadCpuTime(int measure) {
        this.addValue(measure, measures.stopCurrentThreadCpuTime(measure));
        measures.startCurrentThreadCpuTime(measure);
    }

    /**
     * Stops the measurement and adds its value
     * @see Measures#stopCurrentThreadUserTime()
     * @param measure
     */
    public void addStopAndStartCurrentThreadUserTime(int measure) {
        this.addValue(measure, measures.stopCurrentThreadUserTime(measure));
        measures.startCurrentThreadUserTime(measure);
    }

    /**
     * Stops the measurement and adds its value
     * @see Measures#stopCurrentThreadSystemTime()
     * @param measure
     */
    public void addStopAndStartCurrentThreadSystemTime(int measure) {
        this.addValue(measure, measures.stopCurrentThreadSystemTime(measure));
        measures.startCurrentThreadSystemTime(measure);
    }

    /**
     * Stops the measurement and adds its value
     * @see Measures#stopUsedBytesGCMX()
     * @param measure
     */
    public void addStopUsedBytesGCMX(int measure) {
        this.addValue(measure, measures.stopUsedBytesGCMX(measure));
    }
    
    /**
     * Stops the measurement and adds its value
     * @see Measures#stopUsedBytesGC()
     * @param measure
     */
    public void addStopUsedBytesGC(int measure) {
        this.addValue(measure, measures.stopUsedBytesGC(measure));
    }
    

    /**
     * Stops the measurement and adds its value
     * @see Measures#stopUsedBytesMX()
     * @param measure
     */
    public void addStopUsedBytesMX(int measure) {
        this.addValue(measure, measures.stopUsedBytesMX(measure));
    }

    /**
     * Stops the measurement and adds its value
     * @see Measures#stopUsedBytesGCMX()
     * @param measure
     */
    public void addStopAndStartUsedBytesGCMX(int measure) {
        this.addValue(measure, measures.stopUsedBytesGCMX(measure));
        measures.startUsedBytesGCMX(measure);
    }
    
    /**
     * Stops the measurement and adds its value
     * @see Measures#stopUsedBytesGC()
     * @param measure
     */
    public void addStopAndStartUsedBytesGC(int measure) {
        this.addValue(measure, measures.stopUsedBytesGC(measure));
        measures.startUsedBytesGC(measure);
    }
    

    /**
     * Stops the measurement and adds its value
     * @see Measures#stopUsedBytesMX()
     * @param measure
     */
    public void addStopAndStartUsedBytesMX(int measure) {
        this.addValue(measure, measures.stopUsedBytesMX(measure));
        measures.startUsedBytesMX(measure);
    }
    
    /**
     * Adds the user time of all threads
     * @see Measures#getUserTime()
     * @param measure
     */
    public void addUserTime(int measure) {
        this.addValue(measure, measures.getUserTime());
    }
    
    /**
     * Adds the given value
     * @param measure
     * @param value
     */
    @SuppressWarnings("unchecked")
    public void addValue(int measure, Object value) {
        
            Analyzer<?>[] analyzers = currentRun[measure];
            for (int i = 0; i < analyzers.length; i++) {
                try {
                    ((Analyzer<Object>)analyzers[i]).add(value);
                } catch (ClassCastException e) {
                    throw new RuntimeException("Incompatible analyzer for value of type 'object'");
                }
            }
    }
    /**
     * Adds the given value
     * @param measure
     * @param value
     */
    public void addValue(int measure, Float value) {
        addValue(measure, Double.valueOf(value));
    }
    /**
     * Adds the given value
     * @param measure
     * @param value
     */
    public void addValue(int measure, float value) {
        addValue(measure, Double.valueOf(value));
    }

    /**
     * Adds the given value
     * @param measure
     * @param value
     */
    public void addValue(int measure, Long value) {
        addValue(measure, Double.valueOf(value));
    }
    
    /**
     * Adds the given value
     * @param measure
     * @param value
     */
    public void addValue(int measure, long value) {
        addValue(measure, Double.valueOf(value));
    }

    /**
     * Adds the given value
     * @param measure
     * @param value
     */
    public void addValue(int measure, Character value) {
        addValue(measure, Double.valueOf(value));
    }
    
    /**
     * Adds the given value
     * @param measure
     * @param value
     */
    public void addValue(int measure, char value) {
        addValue(measure, Double.valueOf(value));
    }
    
    /**
     * Adds the given value
     * @param measure
     * @param value
     */
    public void addValue(int measure, Integer value) {
        addValue(measure, Double.valueOf(value));
    }
    
    /**
     * Adds the given value
     * @param measure
     * @param value
     */
    public void addValue(int measure, int value) {
        addValue(measure, Double.valueOf(value));
    }
    
    /**
     * Adds the given value
     * @param measure
     * @param value
     */
    public void addValue(int measure, double value) {
        addValue(measure, Double.valueOf(value));
    }
    
    /**
     * Adds the given value
     * @param measure
     * @param value
     */
    @SuppressWarnings("unchecked")
    public void addValue(int measure, Double value) {
        
        Analyzer<?>[] analyzers = currentRun[measure];
        for (int i = 0; i < analyzers.length; i++) {
            try {
                ((Analyzer<Double>)analyzers[i]).add(value);
            } catch (ClassCastException e) {
                throw new RuntimeException("Incompatible analyzer for value of type 'double'");
            }
        }
    }
    
    /**
     * Is the benchmark instrumented
     * @return
     */
    public boolean isInstrumented(){
        return measures.isInstrumented();
    }

    /**
     * Returns the label of the measure with the given id
     * @param measure
     * @return
     */
    public synchronized String getMeasure(int measure) {
        return measureToString.get(measure);
    }

    /**
     * Returns the instance of the measurement class
     * @return
     */
    public synchronized Measures getMeasures() {
        return measures;
    }

    /**
     * Returns the benchmark results as a csv file
     * @return
     */
    public synchronized CSVFile getResults() {

        Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();

        // Init header
        List<String> header1 = new ArrayList<String>();
        List<String> header2 = new ArrayList<String>();

        for (int i = 0; i < runHeader.length; i++) {
            header1.add("");
            header2.add(runHeader[i]);
        }

        // Create header
        for (int i = 0; i < runs.size(); i++) {
            Analyzer<?>[][] analyzers = runs.get(i);
            for (int j = 0; j < measureToString.size(); j++) {
                String measure = measureToString.get(j);
                for (Analyzer<?> a : analyzers[j]) {
                    String label = a.getLabel();
                    int index = getIndex(map, measure, label);
                    if (index == -1) {
                        setIndex(map, measure, label, header1.size());
                        header1.add(measure);
                        header2.add(label);
                    }
                }
            }
        }

        // Create csv
        CSVFile csv = new CSVFile(header1.toArray(new String[0]), header2.toArray(new String[0]));

        // Write csv
        for (int i = 0; i < runs.size(); i++) {
            String[] line = new String[header1.size()];
            for (int j = 0; j < runData.get(i).length; j++) {
                line[j] = runData.get(i)[j];
            }
            Analyzer<?>[][] analyzers = runs.get(i);
            for (int j = 0; j < measureToString.size(); j++) {
                String measure = measureToString.get(j);
                for (Analyzer<?> a : analyzers[j]) {
                    String label = a.getLabel();
                    int index = getIndex(map, measure, label);
                    if (index == -1) { throw new RuntimeException("Invalid index"); }
                    line[index] = a.getValue();
                }
            }
            csv.addLine(line);
        }

        return csv;
    }

    /**
     * Returns a thread-safe synchronized instance of this class
     * @return
     */
    public synchronized Benchmark getSynchronized() {
        return new BenchmarkSynchronized(this);
    }
    
    /**
     * Runs a "heavy" GC
     */
    public void heavyGC() {
        measures.heavyGC();
    }
    
    /**
     * Starts the given timer
     * @param measure
     */
    public void startTimer(int measure) {
        measures.startTimer(measure);
    }

    public void startCurrentThreadCpuTime(int measure) {
        measures.startCurrentThreadCpuTime(measure);
    }

    public void startCurrentThreadSystemTime(int measure) {
        measures.startCurrentThreadSystemTime(measure);
    }

    public void startCurrentThreadUserTime(int measure) {
        measures.startCurrentThreadUserTime(measure);
    }

    public void startUsedBytesGCMX(int measure) {
        measures.startUsedBytesGCMX(measure);
    }

    public void startUsedBytesGC(int measure) {
        measures.startUsedBytesGC(measure);
    }

    public void startUsedBytesMX(int measure) {
        measures.startUsedBytesMX(measure);
    }

    /**
     * Returns a string representation
     */
    public synchronized String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < runs.size(); i++) {
            int rdLength = runData.get(i).length;
            for (int j = 0; j < rdLength; j++) {
                b.append(runData.get(i)[j]);
                if (j < rdLength - 1) {
                    b.append(", ");
                } else {
                    b.append("\n");
                }
            }

            Analyzer<?>[][] analyzers = runs.get(i);
            for (int j = 0; j < measureToString.size(); j++) {
                b.append(" ").append(measureToString.get(j)).append("\n");
                for (Analyzer<?> a : analyzers[j]) {
                    b.append("  ").append(a.getLabel()).append(": ").append(a.getValue()).append("\n");
                }
            }
        }
        return b.toString();
    }

    /**
     * Internal helper for building csv files
     * @param map
     * @param key1
     * @param key2
     * @return
     */
    private int getIndex(Map<String, Map<String, Integer>> map, String key1, String key2) {
        if (!map.containsKey(key1)) return -1;
        else if (!map.get(key1).containsKey(key2)) return -1;
        else return map.get(key1).get(key2);
    }

    /**
     * Internal helper for building csv files
     * @param map
     * @param key1
     * @param key2
     * @param index
     */
    private void setIndex(Map<String, Map<String, Integer>> map, String key1, String key2, int index) {
        if (!map.containsKey(key1)) {
            map.put(key1, new HashMap<String, Integer>());
        }
        map.get(key1).put(key2, index);
    }
    
    /**
     * @return map containing Analyzer<?>s for each measurement
     */
    public Map<Integer, List<Analyzer<?>>> getAnalyzers() {
        return analyzers;
    }
}
