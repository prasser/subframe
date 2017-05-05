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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.linearbits.subframe.graph.Plot3D;
import de.linearbits.subframe.graph.Point3D;

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
        
        List<Point3D> data = new ArrayList<Point3D>(plot.getSeries3D().getData());
        
        // Order by x
        Collections.sort(data, new Comparator<Point3D>() {
            @Override
            public int compare(Point3D o1, Point3D o2) {
                return Double.valueOf(o1.x).compareTo(Double.valueOf(o2.x));
            }
        });
        // Insert blank line when x changes
        List<Integer> insertion = new ArrayList<Integer>();
        for (int i = 1; i < data.size(); i++) {
            if (!data.get(i).x.equals(data.get(i - 1).x)) {
                insertion.add(i);
            }
        }
        for (int i = insertion.size() - 1; i >= 0; i--) {
            data.add(insertion.get(i), null);
        }

        StringBuffer buffer = new StringBuffer();
        for (Point3D point : data) {
            if (point == null) {
                buffer.append("\n");
            } else {
                buffer.append(point.x);
                buffer.append(" ");
                buffer.append(point.y);
                buffer.append(" ");
                buffer.append(point.z);
                buffer.append(" ");
                buffer.append("\n");
            }
        }
        return buffer.toString();
    }

    @Override
    protected String getSource(String filename) {

        final List<String> gpCommands = getGenericCommands(filename, plot);
        
        gpCommands.add("set boxwidth " +params.boxwidth + " absolute");
        if (params.colorize) {
            gpCommands.add("set palette rgbformulae 7,5,15");
            gpCommands.add("set palette model RGB rgbformulae 7,5,15");
        }
        if (params.grid3dResolution != null) {
            gpCommands.add("set dgrid3d "+params.grid3dResolution+","+params.grid3dResolution);
            if (params.grid3dInterpolation != null) {
                gpCommands.add("set pm3d interpolate "+params.grid3dInterpolation+","+params.grid3dInterpolation);
            }
        }
        if (params.xyPlaneAt != null) {
            gpCommands.add("set xyplane at " + params.xyPlaneAt);
        }
        
        gpCommands.add("set title \"" + plot.getTitle() + "\"");
        gpCommands.add("set surface");
        gpCommands.add("set grid back");
        gpCommands.add("set pm3d depthorder hidden3d");
        gpCommands.add("set pm3d implicit");
        gpCommands.add("unset key");
        gpCommands.add("splot '" + filename + ".dat' w l ls 1");

        StringBuffer buffer = new StringBuffer();
        for (String line : gpCommands) {
            buffer.append(line).append("\n");
        }
        return buffer.toString();
    }
}
