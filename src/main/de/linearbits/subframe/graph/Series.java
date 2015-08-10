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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.linearbits.objectselector.Selector;

/**
 * An abstract series
 * 
 * @author Fabian Prasser
 *
 * @param <T>
 */
public abstract class Series<T> {

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

    /** The data*/
    protected List<T> data = new ArrayList<T>();

    /** 
     * Appends the given series to this series
     * 
     * @param series
     */
    public void append(Series<T> series) {
        this.data.addAll(series.data);
    }
    
    /**
     * Returns the data
     * @return
     */
    public List<T> getData() {
        return data;
    }
    
    /**
     * Sorts the series according to the given comperator
     * 
     * @param comparator
     */
    public void sort(Comparator<T> comparator) {
        Collections.sort(data, comparator);
    }

    @Override
    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append("Series[\n");
        for (T t : data){
            b.append(t.toString()).append("\n");
        }
        b.append("]");
        return b.toString();
    }

    /**
     * Applies the given function to all data points
     * @param function
     */
    public void transform(Function<T> function){
        for (int i=0; i<data.size(); i++){
            data.set(i, function.apply(data.get(i)));
        }
    }

}
