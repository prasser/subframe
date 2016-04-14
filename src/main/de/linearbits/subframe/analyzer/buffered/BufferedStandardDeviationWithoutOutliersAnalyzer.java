/*
 * SUBFRAME - Simple Java Benchmarking Framework
 * Copyright (C) 2012 - 2016 Fabian Prasser and contributors
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
 * A buffered analyzer that computes the standard deviation after removing outliers
 * @author Fabian Prasser
 */
public class BufferedStandardDeviationWithoutOutliersAnalyzer extends BufferedAnalyzer{

    private final int numOutliers;
    
    /**
     * Constructs a default instance. Backed by an array list with size 10 and a 1.5 growth rate
     */
    public BufferedStandardDeviationWithoutOutliersAnalyzer(int numOutliers){
        super(Analyzer.STANDARD_DEVIATION_WITHOUT_OUTLIERS(numOutliers));
        this.numOutliers = numOutliers;
    }

    /**
     * Constructs an instance backed by an array list with given initial size and a 1.5 growth rate
     * @param size
     */
    public BufferedStandardDeviationWithoutOutliersAnalyzer(int size, int numOutliers){
        super(Analyzer.STANDARD_DEVIATION_WITHOUT_OUTLIERS(numOutliers), size);
        this.numOutliers = numOutliers;
    }

    /**
     * Constructs an instance backed by an array list with given initial size and given growth rate
     * @param initialSize
     * @param growthRate
     */
    public BufferedStandardDeviationWithoutOutliersAnalyzer(int initialSize, double growthRate, int numOutliers){
        super(Analyzer.STANDARD_DEVIATION_WITHOUT_OUTLIERS(numOutliers), initialSize, growthRate);
        this.numOutliers = numOutliers;
    }
    
    /**
     * Clone constructor
     * @param label
     * @param size
     * @param count
     * @param growthRate
     */
    public BufferedStandardDeviationWithoutOutliersAnalyzer(String label, int size, int count, double growthRate, int numOutliers) {
        super(label, size, count, growthRate);
        this.numOutliers = numOutliers;
    }
    
    @Override
    public String getValue() {
        if (count==0) throw new RuntimeException("No values specified");
        if (count<=numOutliers*2) throw new RuntimeException("Need to specify more than ("+numOutliers*2+") values");
        if (count==1) return String.valueOf(0d);
        
        Arrays.sort(values, 0, count);
        BufferedStandardDeviationAnalyzer analyzer = new BufferedStandardDeviationAnalyzer();
        for (int i=numOutliers; i<count-numOutliers; i++) {
           analyzer.add(values[i]);
        }
        return analyzer.getValue();
    }

    @Override
    public Analyzer<Double> newInstance() {
        return new BufferedStandardDeviationWithoutOutliersAnalyzer(super.getLabel(), super.values.length, 0, super.growthRate, numOutliers);
    }
}
