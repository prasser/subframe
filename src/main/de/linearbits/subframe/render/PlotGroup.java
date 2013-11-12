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
package de.linearbits.subframe.render;

import java.util.List;

import de.linearbits.subframe.graph.Plot;

/**
 * This class represents a group of plots
 * @author Fabian Prasser
 */
public class PlotGroup {

    /** The caption of the plot group*/
    private String caption;
    /** The plots*/
    private List<Plot<?>> plots;
    /** The size of each plot relative to the page width*/
    private double size;
    /** The gnuplot parameters*/
    private GnuPlotParams params;

    /**
     * Defines a plot group
     * @param caption The caption
     * @param plots The plots
     * @param size The size of each plot relative to the page width
     */
    public PlotGroup(String caption, List<Plot<?>> plots, double size) {
        this.caption = caption;
        this.plots = plots;
        this.size = size;
        this.params = new GnuPlotParams();
    }
    /**
     * Defines a plot group
     * @param caption The caption
     * @param plots The plots
     * @param size The size of each plot relative to the page width
     */
    public PlotGroup(String caption, List<Plot<?>> plots, GnuPlotParams params, double size) {
        this.caption = caption;
        this.plots = plots;
        this.size = size;
        this.params = params;
    }

    /**
     * Returns the caption
     * @return
     */
    protected String getCaption() {
        return caption;
    }

    /**
     * Returns the parameters
     * @return
     */
    protected GnuPlotParams getParams() {
        return params;
    }

    /**
     * Returns the plots
     * @return
     */
    protected List<Plot<?>> getPlots() {
        return plots;
    }
    
    /**
     * Returns the size
     * @return
     */
    protected double getSize() {
        return size;
    }
}
