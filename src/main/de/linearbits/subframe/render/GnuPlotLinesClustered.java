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

import java.util.ArrayList;
import java.util.List;

import de.linearbits.subframe.graph.PlotLinesClustered;

/**
 * GnuPlot implementation of a clustered lines plot
 * @author Fabian Prasser
 */
class GnuPlotLinesClustered extends GnuPlot<PlotLinesClustered> {

    /**
     * Creates a new plot
     * @param plot
     * @param params
     */
    protected GnuPlotLinesClustered(PlotLinesClustered plot, GnuPlotParams params) {
        super(plot, params);
    }

    @Override
    protected String getData() {
        return GnuPlotClusterUtils.getData(this.plot);
    }

    @Override
    protected String getSource(String filename) {

        final List<String> gpCommands = new ArrayList<String>();
        gpCommands.add("set terminal postscript eps enhanced monochrome");
        gpCommands.add("set output \"" + filename + ".eps\"");
        
        gpCommands.add("set size " + params.size);
        if (params.ratio != null){
            gpCommands.add("set size ratio " + params.ratio);
        }
        
        gpCommands.add("set boxwidth " +params.boxwidth + " absolute");
        gpCommands.add("set title \"" + plot.getTitle() + "\"");
        gpCommands.add("set xlabel \"" + plot.getLabels().x + "\"");
        gpCommands.add("set ylabel \"" + plot.getLabels().y + "\"");
        gpCommands.add("set key "+params.keypos.toString());
        gpCommands.add("set xtic scale 0");
        gpCommands.add("set style fill solid border -1");
        
        if (params.minY != null && params.maxY != null) {
            gpCommands.add("set yrange[" + params.minY + ":" + params.maxY + "]");
        }

        if (params.minY != null && params.maxY == null) {
            gpCommands.add("set yrange[" + params.minY + ":]");
        }
        if (params.minX != null && params.maxX == null) {
            gpCommands.add("set xrange[" + params.minX + ":]");
        }

        if (params.logY) {
            gpCommands.add("set logscale y");
        }
        
        if (params.xticsrotate != null) {
            gpCommands.add("set xtics rotate by " + params.xticsrotate);
        }
        
        if (params.grid) {
            gpCommands.add("set grid");
        }
        
        int size = GnuPlotClusterUtils.getNumBars(this.plot);
        for (int i=0; i<size; i++){
            String command = null;
            if (i==0){
                command = "plot '" + filename + ".dat' using 1:2 with linespoints title col";
            }  
            else {
                command = "     '' using 1:"+(i+2)+" with linespoints title col";
            }
            
            if (i<size-1) command += ",\\";
            gpCommands.add(command);
        }
        
        StringBuffer buffer = new StringBuffer();
        for (String line : gpCommands) {
            buffer.append(line).append("\n");
        }
        return buffer.toString();
    }
}
