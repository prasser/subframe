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

import de.linearbits.subframe.graph.PlotBoxAndWhisker;
import de.linearbits.subframe.graph.PointBoxAndWhisker;

/**
 * GnuPlot implementation of a box and whisker plot
 * 
 * @author Fabian Prasser
 */
class GnuPlotBoxAndWhisker extends GnuPlot<PlotBoxAndWhisker> {

    /**
     * Creates a new plot
     * 
     * @param plot
     * @param params
     */
    protected GnuPlotBoxAndWhisker(PlotBoxAndWhisker plot, GnuPlotParams params) {
        super(plot, params);
    }

    @Override
    protected String getData() {

        StringBuffer buffer = new StringBuffer();
        for (PointBoxAndWhisker point : plot.getSeriesBoxAndWhisker().getData()) {
                buffer.append(point.id);
                buffer.append(" ");
                buffer.append(point.min);
                buffer.append(" ");
                buffer.append(point.quartile1);
                buffer.append(" ");
                buffer.append(point.median);
                buffer.append(" ");
                buffer.append(point.quartile3);
                buffer.append(" ");
                buffer.append(point.max);
                buffer.append(" ");
                buffer.append(point.boxwidth);
                buffer.append(" ");
                buffer.append(point.title);
                buffer.append(" ");
                buffer.append("\n");
        }
        return buffer.toString();
    }

    @Override
    protected String getSource(String filename) {

        List<String> gpCommands = getGenericCommands(filename, plot);

        if (params.boxwidth != null) {
            gpCommands.add("set boxwidth " + params.boxwidth + " absolute");
        } else {
            gpCommands.add("set boxwidth 0.2 absolute");
        }

        gpCommands.add("set style fill empty");
        gpCommands.add("plot '" + filename + ".dat' using 1:3:2:6:5:7:xticlabels(8) with candlesticks title 'Quartiles' whiskerbars, \\");
        gpCommands.add("     '' using 1:4:4:4:4:7 with candlesticks lt -1 notitle");
        
        StringBuffer buffer = new StringBuffer();
        for (String line : gpCommands) {
            buffer.append(line).append("\n");
        }
        return buffer.toString();
    }
}
