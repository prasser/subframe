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

import de.linearbits.subframe.graph.Plot3D;
import de.linearbits.subframe.graph.Point3D;
import de.linearbits.subframe.render.GnuPlotParams.KeyPos;

/**
 * GnuPlot implementation of a 3D plot
 * @author Fabian Prasser
 */
class GnuPlot3D extends GnuPlot<Plot3D> {

    /**
     * Creates a new plot
     * @param plot
     * @param params
     */
    protected GnuPlot3D(Plot3D plot, GnuPlotParams params) {
        super(plot, params);
    }

    @Override
    protected String getData() {

        StringBuffer buffer = new StringBuffer();
        for (Point3D point : plot.getSeries3D().getData()) {

            buffer.append(point.x);
            buffer.append(" ");
            buffer.append(point.y);
            buffer.append(" ");
            buffer.append(point.z);
            buffer.append(" ");
            buffer.append("\n");
        }
        return buffer.toString();
    }

    @Override
    protected String getSource(String filename) {

        final List<String> gpCommands = getGenericCommands(filename, plot);
        
        gpCommands.add("set boxwidth " +params.boxwidth + " absolute");
        gpCommands.add("set title \"" + plot.getTitle() + "\"");
        gpCommands.add("set data style lines");
        gpCommands.add("set surface");
        gpCommands.add("set grid back");
        gpCommands.add("set hidden3d");
        gpCommands.add("set pm3d ");

        gpCommands.add("set zlabel \"" + plot.getLabels().z + "\" rotate by 90");

        if (params.keypos == KeyPos.NONE) {
            gpCommands.add("unset key");
        } else {
            gpCommands.add("set key "+params.keypos.toString());
        }

        gpCommands.add("splot '" + filename + ".dat'");

        StringBuffer buffer = new StringBuffer();
        for (String line : gpCommands) {
            buffer.append(line).append("\n");
        }
        return buffer.toString();
    }
}
