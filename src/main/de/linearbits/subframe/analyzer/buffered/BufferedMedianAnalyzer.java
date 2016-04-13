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
package de.linearbits.subframe.analyzer.buffered;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.linearbits.subframe.analyzer.Analyzer;

/**
 * A buffered analyzer that computes the median
 * @author Johanna Eicher
 */
public class BufferedMedianAnalyzer extends BufferedAnalyzer {
    
    /**
     * Constructs a default instance. Backed by an array list with size 10 and a 1.5 growth rate
     */
    public BufferedMedianAnalyzer() {
        super(Analyzer.MEDIAN);
    }
    
    /**
     * Constructs an instance backed by an array list with given initial size and a 1.5 growth rate
     * @param size
     */
    public BufferedMedianAnalyzer(int size) {
        super(Analyzer.MEDIAN, size);
    }
    
    /**
     * Constructs an instance backed by an array list with given initial size and given growth rate
     * @param initialSize
     * @param growthRate
     */
    public BufferedMedianAnalyzer(int initialSize, double growthRate) {
        super(Analyzer.MEDIAN, initialSize, growthRate);
    }
    
    /**
     * Clone constructor
     * @param label
     * @param size
     * @param count
     * @param growthRate
     */
    public BufferedMedianAnalyzer(String label, int size, int count, double growthRate) {
        super(label, size, count, growthRate);
    }
    
    @Override
    public String getValue() {
        if (count == 0) throw new RuntimeException("No values specified!");
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(values[i]);
        }
        Collections.sort(list);
        double result = 0d;
        if (list.size() % 2 == 1) {
            result = list.get((list.size() + 1) / 2 - 1);
        }
        else {
            double low = list.get(list.size() / 2 - 1);
            double high = list.get(list.size() / 2);
            result = (low + high) / 2.0;
        }
        
        return String.valueOf(result);
    }
    
    @Override
    public Analyzer<Double> newInstance() {
        return new BufferedMedianAnalyzer(super.getLabel(), super.values.length, 0, super.growthRate);
    }
}
