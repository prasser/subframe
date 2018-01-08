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

import java.io.IOException;
import java.util.Iterator;

import de.linearbits.objectselector.Selector;
import de.linearbits.subframe.analyzer.Analyzer;
import de.linearbits.subframe.io.CSVFile;
import de.linearbits.subframe.io.CSVLine;

/**
 * A series for a box and whisker plot
 * 
 * @author Fabian Prasser
 */
public class SeriesBoxAndWhisker extends Series<PointBoxAndWhisker> {

    /**
     * Creates an empty series
     */
    public SeriesBoxAndWhisker() {
        // Empty by design
    }
    
    /**
     * Creates a series by selecting rows and taking two values
     * 
     * @param file
     * @param xField
     * @param yField
     * @param interval
     * @param precision
     * @param omitEmptyBuckets
     */
    public SeriesBoxAndWhisker(CSVFile file, 
                               Field xField, 
                               Field yField,
                               double interval,
                               int precision,
                               boolean omitEmptyBuckets){
        this(file, getDefaultSelector(), xField, yField, interval, precision, omitEmptyBuckets);
    }

    /**
     * Creates a series by selecting rows and taking two values
     * 
     * @param file
     * @param xField
     * @param yField
     * @param interval
     * @param precision
     */
    public SeriesBoxAndWhisker(CSVFile file, 
                               Field xField, 
                               Field yField,
                               double interval,
                               int precision){
        this(file, getDefaultSelector(), xField, yField, interval, precision);
    }
    
    /**
     * Creates a series by selecting rows and taking two values
     * 
     * @param file
     * @param selector
     * @param xField
     * @param yField
     * @param interval
     * @param precision
     * @param omitEmptyBuckets
     * @throws IOException 
     */
    public SeriesBoxAndWhisker(CSVFile file, 
                               Selector<String[]> selector, 
                               Field xField, 
                               Field yField,
                               double interval,
                               int precision,
                               boolean omitEmptyBuckets) {
        
        CSVFile input = file.getBucketizedFile(selector, xField.category, xField.measure, yField.category, yField.measure, interval, precision, omitEmptyBuckets);
        Iterator<CSVLine> iter = input.iterator();
        int id = 1;
        while (iter.hasNext()) {
            CSVLine csvline = iter.next();
            data.add(new PointBoxAndWhisker(String.valueOf(id++),
                                            csvline.get(yField.category, Analyzer.MINIMUM),
                                            csvline.get(yField.category, Analyzer.PERCENTILE(0.25d)),
                                            csvline.get(yField.category, Analyzer.MEDIAN),
                                            csvline.get(yField.category, Analyzer.PERCENTILE(0.75d)),
                                            csvline.get(yField.category, Analyzer.MAXIMUM),
                                            csvline.get(xField.category, Analyzer.VALUE)));
        }
    }
    
    /**
     * Creates a series by selecting rows and taking two values
     * 
     * @param file
     * @param selector
     * @param xField
     * @param yField
     * @param interval
     * @param precision
     * @throws IOException 
     */
    public SeriesBoxAndWhisker(CSVFile file, 
                    Selector<String[]> selector, 
                    Field xField, 
                    Field yField,
                    double interval,
                    int precision) {
        
       this(file, selector, xField, yField, interval, precision, false);
    }
}
