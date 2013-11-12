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

/**
 * A histogram with or without error bars
 * @author Fabian Prasser
 */
public class PlotHistogram extends Plot<Series<?>> {

    /**
     * Will plot a standard histogram
     * 
     * @param title
     * @param labels
     * @param series
     */
    public PlotHistogram(String title, Labels labels, Series2D series) {
        super(title, labels, series);
    }

    /**
     * Will plot a histogram with error bars
     * 
     * @param title
     * @param labels
     * @param series
     */
    public PlotHistogram(String title, Labels labels, Series3D series) {
        super(title, labels, series);
    }

    /**
     * Does the plot contain error bars
     * 
     * @return
     */
    public boolean isErrorBars() {
        return getSeries3D() != null;
    }
}
