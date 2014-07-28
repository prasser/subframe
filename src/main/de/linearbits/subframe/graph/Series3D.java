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
 * A 3D series. TODO: Implement constructors with multiple selectors
 * 
 * @author Fabian Prasser
 */
public class Series3D extends Series<Point3D>{

    /**
     * Creates a series by selecting rows, 
     * grouping by x and applying two analyzers to y,
     * the results of which will become y and z values 
     * 
     * @param file
     * @param selector
     * @param xField
     * @param yField
     * @param yAnalyzer
     * @param zAnalyzer
     */
    public Series3D(CSVFile file, 
                    Selector<String[]> selector, 
                    Field xField, 
                    Field yField, 
                    Analyzer<Double> yAnalyzer, 
                    Analyzer<Double> zAnalyzer){

        Map<String, Analyzer<Double>> yAnalyzers = new LinkedHashMap<String, Analyzer<Double>>();
        Map<String, Analyzer<Double>> zAnalyzers = new LinkedHashMap<String, Analyzer<Double>>();

        Iterator<CSVLine> iter = file.iterator();
        while (iter.hasNext()) {
            CSVLine csvline = iter.next();
            String[] line = csvline.getData();
            if (selector.isSelected(line)) {
                String x = csvline.get(xField.category, xField.measure);
                String y = csvline.get(yField.category, yField.measure);
                if (!yAnalyzers.containsKey(x)) {
                    yAnalyzers.put(x, yAnalyzer.newInstance());
                }
                if (!zAnalyzers.containsKey(x)) {
                    zAnalyzers.put(x, zAnalyzer.newInstance());
                }
                yAnalyzers.get(x).add(Double.valueOf(y));
                zAnalyzers.get(x).add(Double.valueOf(y));
            }
        }
        for (String x : yAnalyzers.keySet()) {
            Analyzer<?> y = yAnalyzers.get(x);
            Analyzer<?> z = zAnalyzers.get(x);
            data.add(new Point3D(x, 
                                 String.valueOf(y.getValue()), 
                                 String.valueOf(z.getValue())));
        }
    }
    
    /**
     * Creates a series by selecting rows and taking three values
     * 
     * @param file
     * @param selector
     * @param xField
     * @param yField
     * @param zField
     */
    public Series3D(CSVFile file, 
                    Selector<String[]> selector, 
                    Field xField, 
                    Field yField, 
                    Field zField){

        Iterator<CSVLine> iter = file.iterator();
        while (iter.hasNext()) {
            CSVLine csvline = iter.next();
            String[] line = csvline.getData();
            if (selector.isSelected(line)) {
                String x = csvline.get(xField.category, xField.measure);
                String y = csvline.get(yField.category, yField.measure);
                String z = csvline.get(zField.category, zField.measure);
                data.add(new Point3D(x, y, z));
            }
        }
    }
    

    /**
     * Creates a series by selecting rows and taking two values plus a constant y-value
     * 
     * @param file
     * @param selector
     * @param xField
     * @param yLabel
     * @param zField
     */
    public Series3D(CSVFile file, 
                    Selector<String[]> selector, 
                    Field xField, 
                    String yLabel, 
                    Field zField){

        Iterator<CSVLine> iter = file.iterator();
        while (iter.hasNext()) {
            CSVLine csvline = iter.next();
            String[] line = csvline.getData();
            if (selector.isSelected(line)) {
                String x = csvline.get(xField.category, xField.measure);
                String y = yLabel;
                String z = csvline.get(zField.category, zField.measure);
                data.add(new Point3D(x, y, z));
            }
        }
    }
    

    /**
     * Creates a series by selecting rows, 
     * grouping by x and y and applying the analyzer to z 
     * 
     * @param file
     * @param selector
     * @param xField
     * @param yField
     * @param zField
     * @param analyzer
     */
    public Series3D(CSVFile file, 
                    Selector<String[]> selector, 
                    Field xField, 
                    Field yField, 
                    Field zField, 
                    Analyzer<Double> analyzer){

        Map<Point2D, Analyzer<Double>> analyzers = new LinkedHashMap<Point2D, Analyzer<Double>>();

        Iterator<CSVLine> iter = file.iterator();
        while (iter.hasNext()) {
            CSVLine csvline = iter.next();
            String[] line = csvline.getData();
            if (selector.isSelected(line)) {
                String x = csvline.get(xField.category, xField.measure);
                String y = csvline.get(yField.category, yField.measure);
                String z = csvline.get(zField.category, zField.measure);
                Point2D point = new Point2D(x, y);
                if (!analyzers.containsKey(point)) {
                    analyzers.put(point, analyzer.newInstance());
                }
                analyzers.get(point).add(Double.valueOf(z));
            }
        }

        for (Entry<Point2D, Analyzer<Double>> entry : analyzers.entrySet()){
            data.add(new Point3D(entry.getKey().x, 
                                 entry.getKey().y, 
                                 String.valueOf(entry.getValue().getValue())));
        }
    }
    

    /**
     * Creates a series by selecting rows, 
     * grouping by a constant x and applying two analyzers to y,
     * the results of which will become y and z values 
     * 
     * @param file
     * @param selector
     * @param xLabel
     * @param yField
     * @param yAnalyzer
     * @param zAnalyzer
     */
    public Series3D(CSVFile file, 
                    Selector<String[]> selector, 
                    String xLabel, 
                    Field yField, 
                    Analyzer<Double> yAnalyzer, 
                    Analyzer<Double> zAnalyzer){

        Map<String, Analyzer<Double>> yAnalyzers = new LinkedHashMap<String, Analyzer<Double>>();
        Map<String, Analyzer<Double>> zAnalyzers = new LinkedHashMap<String, Analyzer<Double>>();

        Iterator<CSVLine> iter = file.iterator();
        while (iter.hasNext()) {
            CSVLine csvline = iter.next();
            String[] line = csvline.getData();
            if (selector.isSelected(line)) {
                String x = xLabel;
                String y = csvline.get(yField.category, yField.measure);
                if (!yAnalyzers.containsKey(x)) {
                    yAnalyzers.put(x, yAnalyzer.newInstance());
                }
                if (!zAnalyzers.containsKey(x)) {
                    zAnalyzers.put(x, zAnalyzer.newInstance());
                }
                yAnalyzers.get(x).add(Double.valueOf(y));
                zAnalyzers.get(x).add(Double.valueOf(y));
            }
        }
        for (String x : yAnalyzers.keySet()) {
            Analyzer<?> y = yAnalyzers.get(x);
            Analyzer<?> z = zAnalyzers.get(x);
            data.add(new Point3D(x, 
                                 String.valueOf(y.getValue()), 
                                 String.valueOf(z.getValue())));
        }
    }
    
    /**
     * Creates a series by selecting rows and taking a constant and two values
     * 
     * @param file
     * @param selector
     * @param xLabel
     * @param yField
     * @param zField
     */
    public Series3D(CSVFile file, 
                    Selector<String[]> selector, 
                    String xLabel, 
                    Field yField, 
                    Field zField){

        Iterator<CSVLine> iter = file.iterator();
        while (iter.hasNext()) {
            CSVLine csvline = iter.next();
            String[] line = csvline.getData();
            if (selector.isSelected(line)) {
                String y = csvline.get(yField.category, yField.measure);
                String z = csvline.get(zField.category, zField.measure);
                data.add(new Point3D(xLabel, y, z));
            }
        }
    }
    

    /**
     * Creates a series by selecting rows, taking a constant x 
     * grouping by y and applying the analyzer to z 
     * 
     * @param file
     * @param selector
     * @param xLabel
     * @param yField
     * @param zField
     * @param analyzer
     */
    public Series3D(CSVFile file, 
                    Selector<String[]> selector, 
                    String xLabel, 
                    Field yField, 
                    Field zField, 
                    Analyzer<Double> analyzer){

        Map<Point2D, Analyzer<Double>> analyzers = new LinkedHashMap<Point2D, Analyzer<Double>>();

        Iterator<CSVLine> iter = file.iterator();
        while (iter.hasNext()) {
            CSVLine csvline = iter.next();
            String[] line = csvline.getData();
            if (selector.isSelected(line)) {
                String x = xLabel;
                String y = csvline.get(yField.category, yField.measure);
                String z = csvline.get(zField.category, zField.measure);
                Point2D point = new Point2D(x, y);
                if (!analyzers.containsKey(point)) {
                    analyzers.put(point, analyzer.newInstance());
                }
                analyzers.get(point).add(Double.valueOf(z));
            }
        }

        for (Entry<Point2D, Analyzer<Double>> entry : analyzers.entrySet()){
            data.add(new Point3D(entry.getKey().x, 
                                 entry.getKey().y, 
                                 String.valueOf(entry.getValue().getValue())));
        }
    }
}
