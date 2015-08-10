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
package de.linearbits.subframe.graph;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.linearbits.objectselector.Selector;
import de.linearbits.subframe.analyzer.Analyzer;
import de.linearbits.subframe.io.CSVFile;
import de.linearbits.subframe.io.CSVLine;

/**
 * A 2D series
 * 
 * @author Fabian Prasser
 */
public class Series2D extends Series<Point2D> {

    /**
     * Creates an empty series
     */
    public Series2D() {
        // Empty by design
    }
    
    /**
     * Creates a series by selecting rows and taking two values
     * 
     * @param file
     * @param xField
     * @param yField
     */
    public Series2D(CSVFile file, 
                    Field xField, 
                    Field yField){
        this(file, getDefaultSelector(), xField, yField);
    }
    
    /**
     * Creates a series by selecting rows, 
     * grouping by x and applying the analyzer to y 
     * 
     * @param file
     * @param xLabel
     * @param yLabel
     * @param analyzer
     */
    public Series2D(CSVFile file, 
                    Field xField, 
                    Field yField, 
                    Analyzer<Double> analyzer){
        this(file, getDefaultSelector(), xField, yField, analyzer);
    }
    

    /**
     * Creates a series by selecting rows and taking two values
     * 
     * @param file
     * @param selector
     * @param xField
     * @param yField
     */
    public Series2D(CSVFile file, 
                    Selector<String[]> selector, 
                    Field xField, 
                    Field yField){
        
        Iterator<CSVLine> iter = file.iterator();
        while (iter.hasNext()) {
            CSVLine csvline = iter.next();
            String[] line = csvline.getData();
            if (selector.isSelected(line)) {
                String x = csvline.get(xField.category, xField.measure);
                String y = csvline.get(yField.category, yField.measure);
                data.add(new Point2D(x, y));
            }
        }
    }
    

    /**
     * Creates a series by selecting rows, 
     * grouping by x and applying the analyzer to y 
     * 
     * @param file
     * @param selector
     * @param xLabel
     * @param yLabel
     * @param analyzer
     */
    public Series2D(CSVFile file, 
                    Selector<String[]> selector, 
                    Field xField, 
                    Field yField, 
                    Analyzer<Double> analyzer){
        
        Map<String, Analyzer<Double>> analyzers = new LinkedHashMap<String, Analyzer<Double>>();

        Iterator<CSVLine> iter = file.iterator();
        while (iter.hasNext()) {
            CSVLine csvline = iter.next();
            String[] line = csvline.getData();
            if (selector.isSelected(line)) {
                String x = csvline.get(xField.category, xField.measure);
                if (!analyzers.containsKey(x)) {
                    analyzers.put(x, analyzer.newInstance());
                }
                analyzers.get(x).add(Double.valueOf(csvline.get(yField.category, yField.measure)));
            }
        }

        for (Entry<String, Analyzer<Double>> entry : analyzers.entrySet()){
            data.add(new Point2D(entry.getKey(), String.valueOf(entry.getValue().getValue())));
        }
    }
    
    
    /**
     * Creates a series by selecting different rows and taking different values
     * 
     * @param file
     * @param xSelector
     * @param xLabel
     * @param ySelector
     * @param yLabel
     */
    public Series2D(CSVFile file, 
                    Selector<String[]> xSelector, 
                    Field xField, 
                    Selector<String[]> ySelector, 
                    Field yField){

        String x = null;
        String y = null;
        Iterator<CSVLine> iter = file.iterator();
        while (iter.hasNext()) {
            CSVLine csvline = iter.next();
            String[] line = csvline.getData();
            if (x == null){
                if (xSelector.isSelected(line)) {
                    x = csvline.get(xField.category, xField.measure);
                }
            } else if (y==null){
                if (ySelector.isSelected(line)) {
                    y = csvline.get(yField.category, yField.measure);
                    data.add(new Point2D(x, y));
                    x = null;
                    y = null;
                }
            } else {
                throw new RuntimeException("Invalid match");
            }
        }
    }
    
    /**
     * Creates a series by selecting different rows and taking different values, 
     * grouping by x and applying the analyzer to y 
     * 
     * @param file
     * @param xSelector
     * @param xLabel
     * @param ySelector
     * @param yLabel
     * @param analyzer
     */
    public Series2D(CSVFile file, 
                    Selector<String[]> xSelector, 
                    Field xField,
                    Selector<String[]> ySelector,
                    Field yField, 
                    Analyzer<Double> analyzer){

        Map<String, Analyzer<Double>> analyzers = new LinkedHashMap<String, Analyzer<Double>>();

        String x = null;
        String y = null;
        Iterator<CSVLine> iter = file.iterator();
        while (iter.hasNext()) {
            CSVLine csvline = iter.next();
            String[] line = csvline.getData();
            if (x == null){
                if (xSelector.isSelected(line)) {
                    x = csvline.get(xField.category, xField.measure);
                    if (!analyzers.containsKey(x)) {
                        analyzers.put(x, analyzer.newInstance());
                    }
                }
            } else if (y==null){
                if (ySelector.isSelected(line)) {
                    y = csvline.get(yField.category, yField.measure);
                    analyzers.get(x).add(Double.valueOf(y));
                    x = null;
                    y = null;
                }
            } else {
                throw new RuntimeException("Invalid match");
            }
        }
        
        for (Entry<String, Analyzer<Double>> entry : analyzers.entrySet()){
            data.add(new Point2D(entry.getKey(), String.valueOf(entry.getValue().getValue())));
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    /**
     * Creates a series by selecting rows and taking a constant and one value
     * 
     * @param file
     * @param selector
     * @param xLabel
     * @param yField
     */
    public Series2D(CSVFile file, 
                    Selector<String[]> selector, 
                    String xLabel, 
                    Field yField){

        Iterator<CSVLine> iter = file.iterator();
        while (iter.hasNext()) {
            CSVLine csvline = iter.next();
            String[] line = csvline.getData();
            if (selector.isSelected(line)) {
                String x = xLabel;
                String y = csvline.get(yField.category, yField.measure);
                data.add(new Point2D(x, y));
            }
        }
    }
    
    /**
     * Creates a series by selecting rows, 
     * grouping by constant x and applying the analyzer to y 
     * 
     * @param file
     * @param selector
     * @param xLabel
     * @param yField
     * @param analyzer
     */
    public Series2D(CSVFile file, 
                    Selector<String[]> selector, 
                    String xLabel, 
                    Field yField, 
                    Analyzer<Double> analyzer){

        Map<String, Analyzer<Double>> analyzers = new LinkedHashMap<String, Analyzer<Double>>();

        Iterator<CSVLine> iter = file.iterator();
        while (iter.hasNext()) {
            CSVLine csvline = iter.next();
            String[] line = csvline.getData();
            if (selector.isSelected(line)) {
                String x = xLabel;
                if (!analyzers.containsKey(x)) {
                    analyzers.put(x, analyzer.newInstance());
                }
                analyzers.get(x).add(Double.valueOf(csvline.get(yField.category, yField.measure)));
            }
        }

        for (Entry<String, Analyzer<Double>> entry : analyzers.entrySet()){
            data.add(new Point2D(entry.getKey(), String.valueOf(entry.getValue().getValue())));
        }
    }
    
    /**
     * Creates a series by selecting rows and taking a constant and one value
     * 
     * @param file
     * @param xLabel
     * @param yField
     */
    public Series2D(CSVFile file, 
                    String xLabel, 
                    Field yField){
        
        this(file, getDefaultSelector(), xLabel, yField);
    }
    
    /**
     * Creates a series by selecting rows, 
     * grouping by constant x and applying the analyzer to y 
     * 
     * @param file
     * @param xLabel
     * @param yField
     * @param analyzer
     */
    public Series2D(CSVFile file, 
                    String xLabel, 
                    Field yField, 
                    Analyzer<Double> analyzer){

        this(file, getDefaultSelector(), xLabel, yField, analyzer);
    }
}
