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

import de.linearbits.subframe.graph.PlotLinesClustered;

/**
 * GnuPlot implementation of a clustered lines plot
 * 
 * @author Fabian Prasser
 */
class GnuPlotLinesClustered extends GnuPlot<PlotLinesClustered> {

    /**
     * Creates a new plot
     * 
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

        List<String> gpCommands = getGenericCommands(filename, plot);

        gpCommands.add("set style fill solid border -1");

        int size = GnuPlotClusterUtils.getNumBars(this.plot);
        for (int i = 0; i < size; i++) {
            String command = null;
            String color = params.colorize ? "linecolor rgb \"#" + params.colors[i % params.colors.length] + "\" " : "";
            if (i == 0) {
                if (params.categorialX) {
                    command = "plot '" + filename + ".dat' using 2:xtic(1) with linespoints "+color+"title col";
                } else {
                    command = "plot '" + filename + ".dat' using 1:2 with linespoints "+color+"title col";
                }
            } else {
                if (params.categorialX) {
                    command = "     '' using " + (i + 2) + ":xtic(1) with linespoints "+color+"title col";
                } else {
                    command = "     '' using 1:" + (i + 2) + " with linespoints "+color+"title col";
                }
            }

            if (i < size - 1) command += ",\\";
            gpCommands.add(command);
        }

        StringBuffer buffer = new StringBuffer();
        for (String line : gpCommands) {
            buffer.append(line).append("\n");
        }
        return buffer.toString();
    }
}
