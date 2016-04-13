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
package de.linearbits.subframe.analyzer;

/**
 * An abstract base class for Analyzers
 * 
 * @author Fabian Prasser
 */
public abstract class Analyzer<T> {
    
    /** Label for analyzed values */
    public static final String VALUE              = "Value";
    /** Label for analyzed values */
    public static final String SUM                = "Sum";
    /** Label for analyzed values */
    public static final String COUNT              = "Count";
    /** Label for analyzed values */
    public static final String MAXIMUM            = "Maximum";
    /** Label for analyzed values */
    public static final String MINIMUM            = "Minimum";
    /** Label for analyzed values */
    public static final String GEOMETRIC_MEAN     = "Geometric Mean";
    /** Label for analyzed values */
    public static final String STANDARD_DEVIATION = "Standard Deviation";
    /** Label for analyzed values */
    public static final String ARITHMETIC_MEAN    = "Arithmetic Mean";
    /** Label for analyzed values */
    public static final String MEDIAN             = "Median";
    /** Label for analyzed values */
    public static final String PERCENTILE(double percentile) {
        return percentile + " - Percentile";
    }
    /** Label for analyzed values */
    public static final String ARITHMETIC_MEAN_WITHOUT_OUTLIERS(int numOutliers) {
        return "Arithmetic Mean ("+numOutliers+")";
    }
    /** Label for analyzed values */
    public static final String STANDARD_DEVIATION_WITHOUT_OUTLIERS(int numOutliers) {
        return "Standard Deviation ("+numOutliers+")";
    }
    
    /** The label*/
    private String label;
    
    /**
     * Creates a new analyzer with the given label
     * @param label
     */
    public Analyzer(String label){
        this.label = label;
    }

    /**
     * Adds a value
     * @param val
     */
    public abstract void add(T val);
    
    /**
     * Returns the label
     * @return
     */
    public String getLabel(){
        return label;
    }
    
    /**
     * Returns the computed value
     * @return
     */
    public abstract String getValue();
    
    /** 
     * Creates a new instance
     * @return
     */
    public abstract Analyzer<T> newInstance();
}
