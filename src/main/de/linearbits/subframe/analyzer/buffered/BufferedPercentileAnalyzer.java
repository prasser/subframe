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

import java.util.Arrays;

import de.linearbits.subframe.analyzer.Analyzer;

/**
 * A buffered analyzer that computes a percentile
 * @author Fabian Prasser
 */
public class BufferedPercentileAnalyzer extends BufferedAnalyzer{

    private double percentile = 0d;

    /**
     * Constructs a default instance. Backed by an array list with size 10 and a 1.5 growth rate
     * @param percentile
     */
    public BufferedPercentileAnalyzer(double percentile){
        super(Analyzer.PERCENTILE(percentile));
        this.percentile = percentile;
        check();
    }

    /**
     * Constructs an instance backed by an array list with given initial size and a 1.5 growth rate
     * @param percentile
     * @param size
     */
    public BufferedPercentileAnalyzer(double percentile, int size){
        super(Analyzer.PERCENTILE(percentile), size);
        this.percentile = percentile;
        if (percentile <= 0d || percentile >= 1d) {
            throw new RuntimeException("Invalid percentile: "+percentile);
        }
    }

    /**
     * Constructs an instance backed by an array list with given initial size and given growth rate
     * @param percentile
     * @param initialSize
     * @param growthRate
     */
    public BufferedPercentileAnalyzer(double percentile, int initialSize, double growthRate){
        super(Analyzer.PERCENTILE(percentile), initialSize, growthRate);
        this.percentile = percentile;
        if (percentile <= 0d || percentile >= 1d) {
            throw new RuntimeException("Invalid percentile: "+percentile);
        }
    }
    
    /**
     * Clone constructor
     * @param label
     * @param size
     * @param count
     * @param growthRate
     */
    public BufferedPercentileAnalyzer(String label, double percentile, int size, int count, double growthRate) {
        super(label, size, count, growthRate);
        this.percentile = percentile;
        check();
    }

    @Override
    public String getValue() {
        if (count==0) throw new RuntimeException("No values specified!");
        Arrays.sort(values, 0, count);
        int offset = (int)Math.ceil(percentile * (double)count);
        if (offset>count-1) offset = count-1;
        return String.valueOf(values[offset]);
    }
    
    @Override
    public Analyzer<Double> newInstance() {
        return new BufferedPercentileAnalyzer(super.getLabel(), this.percentile, super.values.length, 0, super.growthRate);
    }

    private void check(){
        if (percentile <= 0d || percentile >= 1d) {
            throw new IllegalArgumentException("Invalid percentile: "+percentile);
        }
    }
}
