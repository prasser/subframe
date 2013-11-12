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
 * Base class for analyzers that buffer all values and compute the result afterwards
 * @author Fabian Prasser
 */
public abstract class BufferedAnalyzer extends Analyzer{

	/** Current number of values*/
    protected int count = 0;
    /** Growth rate for the underlying array list*/
    protected double growthRate = 0d;
    /** Values*/
    protected double[] values = null;
    
    /**
     * Constructs a default instance. Backed by an array list with size 10 and a 1.5 growth rate
     * @param label
     */
    protected BufferedAnalyzer(String label){
        this(label, 10, 1.5d);
    }
    
    /**
     * Constructs an instance backed by an array list with given initial size and a 1.5 growth rate
     * @param label
     * @param size
     */
    protected BufferedAnalyzer(String label, int size){
        super(label);
        this.values = new double[size];
    }
    
    /**
     * Constructs an instance backed by an array list with given initial size and given growth rate
     * @param label
     * @param initialSize
     * @param growthRate
     */
    protected BufferedAnalyzer(String label, int initialSize, double growthRate){
        super(label);
        this.growthRate = growthRate;
        this.values = new double[initialSize];
    }
    
    /**
     * Clone constructor
     * @param label
     * @param size
     * @param count
     * @param growthRate
     */
    protected BufferedAnalyzer(String label, int size, int count, double growthRate){
        super(label);
        this.values = new double[size];
        this.count = count;
        this.growthRate = growthRate;
    }
    
    @Override
    public void add(double val) {
        values[count++] = val;
        if (count == values.length + 1) {
            if (growthRate != 0) {
                grow();
            } else {
                throw new RuntimeException("Maximum number of values exceeded: "+count);
            }
        }
    }

    /**
     * Grows the array list
     */
    private void grow() {
        double[] nValues = new double[(int)((double)values.length * growthRate)];
        System.arraycopy(values, 0, nValues, 0, values.length);
        this.values = nValues;
    }
}
