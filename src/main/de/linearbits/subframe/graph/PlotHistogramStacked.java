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
 * A stacked histogram
 * 
 * @author Fabian Prasser
 */
public class PlotHistogramStacked extends Plot<Series3D> {

    /**
     * Will plot a clustered histogram
     * 
     * @param title
     * @param labels
     * @param series
     */
    public PlotHistogramStacked(String title, Labels labels, Series3D series) {
        super(title, labels, series);
    }
}
