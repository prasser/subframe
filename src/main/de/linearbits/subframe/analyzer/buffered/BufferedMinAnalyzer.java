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

import de.linearbits.subframe.analyzer.Analyzer;

/**
 * A buffered analyzer that computes the minimum
 * @author Fabian Prasser
 */
public class BufferedMinAnalyzer extends BufferedAnalyzer{

    /**
     * Constructs a default instance. Backed by an array list with size 10 and a 1.5 growth rate
     */
    public BufferedMinAnalyzer(){
        super(Analyzer.MINIMUM);
    }

    /**
     * Constructs an instance backed by an array list with given initial size and a 1.5 growth rate
     * @param size
     */
    public BufferedMinAnalyzer(int size){
        super(Analyzer.MINIMUM, size);
    }

    /**
     * Constructs an instance backed by an array list with given initial size and given growth rate
     * @param initialSize
     * @param growthRate
     */
    public BufferedMinAnalyzer(int initialSize, double growthRate){
        super(Analyzer.MINIMUM, initialSize, growthRate);
    }
    
    /**
     * Clone constructor

     * @param size
     * @param count
     * @param growthRate
     */
    public BufferedMinAnalyzer(String label, int size, int count, double growthRate) {
        super(label, size, count, growthRate);
    }
    
    @Override
    public String getValue() {
        if (count==0) throw new RuntimeException("No values specified!");
        double result = Double.MAX_VALUE;
        for (int i=0; i<count; i++){
            result = Math.min(result, values[i]);
        }
        return String.valueOf(result);
    }

    @Override
    public Analyzer<Double> newInstance() {
        return new BufferedMinAnalyzer(super.getLabel(), super.values.length, super.count, super.growthRate);
    }
}
