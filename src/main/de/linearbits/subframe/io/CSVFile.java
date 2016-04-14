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
package de.linearbits.subframe.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.linearbits.objectselector.Selector;
import de.linearbits.objectselector.SelectorBuilder;
import de.linearbits.objectselector.datatypes.DataType;
import de.linearbits.objectselector.util.ArrayAccessor;
import de.linearbits.subframe.analyzer.Analyzer;
import de.linearbits.subframe.analyzer.buffered.BufferedArithmeticMeanAnalyzer;
import de.linearbits.subframe.analyzer.buffered.BufferedCountAnalyzer;
import de.linearbits.subframe.analyzer.buffered.BufferedGeometricMeanAnalyzer;
import de.linearbits.subframe.analyzer.buffered.BufferedMaxAnalyzer;
import de.linearbits.subframe.analyzer.buffered.BufferedMedianAnalyzer;
import de.linearbits.subframe.analyzer.buffered.BufferedMinAnalyzer;
import de.linearbits.subframe.analyzer.buffered.BufferedPercentileAnalyzer;
import de.linearbits.subframe.analyzer.buffered.BufferedStandardDeviationAnalyzer;
import de.linearbits.subframe.analyzer.buffered.BufferedSumAnalyzer;

/**
 * This class represents the output of a benchmark as a csv file.
 * 
 * @author Fabian Prasser, Florian Kohlmayer
 */
public class CSVFile {
    
    /** The separator */
    private static final char   SEPERATOR = ';';
    /** The newline */
    private static final String NEWLINE   = "\n";
    
    /**
     * Checks a field for valid syntax
     * @param label
     */
    public static void checkFieldValue(String label) {
        if (label.contains(String.valueOf(SEPERATOR)) || label.contains(NEWLINE)) {
            throw new IllegalArgumentException("Label/value '" +
                                               label + "' must not contain ';' or linebreaks");
        }
    }
    
    /**
     * Writes the file to disk
     * @param writer
     * @param line
     * @throws IOException
     */
    private static void write(Writer writer, String[] line) throws IOException {
        for (int i = 0; i < line.length; i++) {
            writer.write(line[i]);
            if (i < line.length - 1) {
                writer.write(String.valueOf(SEPERATOR));
            } else {
                writer.write(NEWLINE);
            }
        }
    }
    
    /**
     * Returns a default selector
     * @return
     */
    static Selector<String[]> getDefaultSelector() {
        return new Selector<String[]> (null){
            @Override
            public boolean isSelected(String[] arg0) {
                return true;
            }
        };
    }
    /** The lines in the file */
    private List<CSVLine>                     lines     = new ArrayList<CSVLine>();
    /** Mapping labels to indices */
    private Map<String, Map<String, Integer>> headermap = new HashMap<String, Map<String, Integer>>();
    /** The first header */
    private String[]                          header1   = null;
    
    /** The second header */
    private String[]                          header2   = null;
    
    /**
     * Creates a new csv file containing the content of the given file
     * @param file
     * @throws IOException
     */
    public CSVFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        this.header1 = reader.readLine().split(String.valueOf(SEPERATOR));
        this.header2 = reader.readLine().split(String.valueOf(SEPERATOR));
        this.buildHeaderMap();
        String line = reader.readLine();
        while (line != null) {
            this.addLine(line.split(String.valueOf(SEPERATOR)));
            line = reader.readLine();
        }
        reader.close();
    }
    
    /**
     * Creates a new empty file with the given headers
     * @param header1
     * @param header2
     */
    public CSVFile(String[] header1, String[] header2) {
        this.header1 = header1;
        this.header2 = header2;
        this.buildHeaderMap();
    }
    
    /**
     * Adds a line
     * @param line
     */
    public void addLine(String[] line) {
        this.lines.add(new CSVLine(line, headermap));
    }

    /**
     * Appends a second file
     * @param other
     */
    public void append(CSVFile other) {
        if (!Arrays.equals(this.header1, other.header1) || !Arrays.equals(this.header2, other.header2)) {
            throw new IllegalArgumentException("Incompatible headers. Please make sure that the files have the same format.");
        }
        for (CSVLine line : other.lines) {
            this.lines.add(line);
        }
    }
        
    /**
     * Creates a new csv file containing bucketized values of this file.
     * 
     * @param selector a selector
     * @param x1 header 1 of x-values
     * @param x2 header 2 of x-values
     * @param y1 header 1 of y-values
     * @param y2 header 2 of y-values
     * @param interval size of buckets
     * @param precision
     * @return
     * @throws IOException
     */
    public CSVFile getBucketizedFile(Selector<String[]> selector,
                                     String x1, String x2, 
                                     String y1, String y2, double interval, int precision) throws IOException {
        
        // Collect values for buckets
        Map<String, Set<Double>> bucket2Values = new HashMap<String, Set<Double>>();
        Iterator<CSVLine> iter = this.iterator();
        List<String> buckets = new ArrayList<>();
        final Map<String, Double> position = new HashMap<>();
        while (iter.hasNext()) {
            
            CSVLine line = iter.next();
            if (selector.isSelected(line.getData())) {
                double x = Double.valueOf(line.get(x1, x2));
                double y = Double.valueOf(line.get(y1, y2));
                double low = x - (x % interval);
                double high = low + interval;
                low = Math.floor(low * precision) / precision;
                high = Math.floor(high * precision) / precision;
                String bucket = "[" + low + "," + high + "[";
                if (!bucket2Values.containsKey(bucket)) {
                    buckets.add(bucket);
                    bucket2Values.put(bucket, new HashSet<Double>());
                    position.put(bucket, low);
                }
                bucket2Values.get(bucket).add(y);
            }
        }
        
        String[] header1 = new String[] { x1, y1, y1, y1, y1, y1, y1, y1, y1 };
        String[] header2 = new String[] { Analyzer.VALUE, 
                                          Analyzer.MINIMUM, 
                                          Analyzer.MAXIMUM, 
                                          Analyzer.COUNT, 
                                          Analyzer.SUM, 
                                          Analyzer.GEOMETRIC_MEAN, 
                                          Analyzer.ARITHMETIC_MEAN, 
                                          Analyzer.MEDIAN, 
                                          Analyzer.STANDARD_DEVIATION,
                                          Analyzer.PERCENTILE(0.25d),
                                          Analyzer.PERCENTILE(0.50d),
                                          Analyzer.PERCENTILE(0.75d),
                                          Analyzer.PERCENTILE(0.80d),
                                          Analyzer.PERCENTILE(0.90d),
                                          Analyzer.PERCENTILE(0.95d),
                                          Analyzer.PERCENTILE(0.97d),
                                          Analyzer.PERCENTILE(0.99d)};
        CSVFile result = new CSVFile(header1, header2);
        
        // Calculate aggregates for each bucket
        Collections.sort(buckets, new Comparator<String>(){
            @Override
            public int compare(String o1, String o2) {
                return position.get(o1).compareTo(position.get(o2));
            }
        });
        for (String bucket : buckets) {
            
            BufferedMinAnalyzer min = new BufferedMinAnalyzer();
            BufferedMaxAnalyzer max = new BufferedMaxAnalyzer();
            BufferedCountAnalyzer count = new BufferedCountAnalyzer();
            BufferedSumAnalyzer sum = new BufferedSumAnalyzer();
            BufferedGeometricMeanAnalyzer gm = new BufferedGeometricMeanAnalyzer();
            BufferedArithmeticMeanAnalyzer am = new BufferedArithmeticMeanAnalyzer();
            BufferedMedianAnalyzer me = new BufferedMedianAnalyzer();
            BufferedStandardDeviationAnalyzer dev = new BufferedStandardDeviationAnalyzer();
            BufferedPercentileAnalyzer perc25 = new BufferedPercentileAnalyzer(0.25d);
            BufferedPercentileAnalyzer perc50 = new BufferedPercentileAnalyzer(0.50d);
            BufferedPercentileAnalyzer perc75 = new BufferedPercentileAnalyzer(0.75d);
            BufferedPercentileAnalyzer perc80 = new BufferedPercentileAnalyzer(0.80d);
            BufferedPercentileAnalyzer perc90 = new BufferedPercentileAnalyzer(0.90d);
            BufferedPercentileAnalyzer perc95 = new BufferedPercentileAnalyzer(0.95d);
            BufferedPercentileAnalyzer perc97 = new BufferedPercentileAnalyzer(0.97d);
            BufferedPercentileAnalyzer perc99 = new BufferedPercentileAnalyzer(0.99d);
            
            for (double value : bucket2Values.get(bucket)) {
                min.add(value);
                max.add(value);
                count.add(value);
                sum.add(value);
                gm.add(value);
                am.add(value);
                me.add(value);
                dev.add(value);
                perc25.add(value);
                perc50.add(value);
                perc75.add(value);
                perc80.add(value);
                perc90.add(value);
                perc95.add(value);
                perc97.add(value);
                perc99.add(value);
            }
            result.addLine(new String[] { bucket, 
                                          min.getValue(), 
                                          max.getValue(), 
                                          count.getValue(), 
                                          sum.getValue(), 
                                          gm.getValue(), 
                                          am.getValue(), 
                                          me.getValue(), 
                                          dev.getValue(),
                                          perc25.getValue(),
                                          perc50.getValue(),
                                          perc75.getValue(),
                                          perc80.getValue(),
                                          perc90.getValue(),
                                          perc95.getValue(),
                                          perc97.getValue(),
                                          perc99.getValue(),
            });
        }
        
        return result;
    }

    /**
     * Creates a new csv file containing bucketized values of this file.
     * 
     * @param x1 header 1 of x-values
     * @param x2 header 2 of x-values
     * @param y1 header 1 of y-values
     * @param y2 header 2 of y-values
     * @param interval size of buckets
     * @param precision
     * @return
     * @throws IOException
     */
    public CSVFile getBucketizedFile(String x1, String x2, 
                                     String y1, String y2, double interval, int precision) throws IOException {
        return getBucketizedFile(getDefaultSelector(), x1, x2, y1, y2, interval, precision);
    }
    /**
     * Returns a new selector builder
     * 
     * @return
     */
    public SelectorBuilder<String[]> getSelectorBuilder() {
        return getSelectorBuilder(null);
    }
    
    /**
     * Returns a new selector builder
     * 
     * @param datatypes
     *            Data types for run data
     * @return
     */
    public SelectorBuilder<String[]> getSelectorBuilder(DataType<?>[] datatypes) {
        
        List<String> labels = new ArrayList<String>();
        List<DataType<?>> types = new ArrayList<DataType<?>>();
        
        for (int i = 0; i < header1.length; i++) {
            
            // Run data
            if (header1[i].equals("")) {
                labels.add(header2[i]);
                if (datatypes != null) {
                    types.add(datatypes[i]);
                } else {
                    types.add(DataType.STRING);
                }
            }
            // Benchmark data
            else {
                labels.add(header1[i] + "." + header2[i]);
                types.add(DataType.NUMERIC);
            }
        }
        
        String[] aLabels = labels.toArray(new String[labels.size()]);
        DataType<?>[] aTypes = types.toArray(new DataType<?>[types.size()]);
        return new SelectorBuilder<String[]>(new ArrayAccessor<String>(aLabels, aTypes));
    }
    
    /**
     * Returns an iterator
     * @return
     */
    public Iterator<CSVLine> iterator() {
        return new CSVIterator(lines);
    }
    
    /**
     * Writes the file to the given location
     * @param file
     * @throws IOException
     */
    public void write(File file) throws IOException {
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        write(writer, header1);
        write(writer, header2);
        for (CSVLine line : lines) {
            write(writer, line.getData());
        }
        writer.close();
    }
    
    /**
     * Builds the map linking labels with indices
     */
    private void buildHeaderMap() {
        for (int i = 0; i < header1.length; i++) {
            if (!headermap.containsKey(header1[i])) {
                headermap.put(header1[i], new HashMap<String, Integer>());
            }
            headermap.get(header1[i]).put(header2[i], i);
        }
    }
}
