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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.linearbits.subframe.graph.Plot;
import de.linearbits.subframe.graph.Point3D;
import de.linearbits.subframe.graph.Series3D;

/**
 * Utility functions for clustered plots
 * @author Fabian Prasser
 */
class GnuPlotClusterUtils {

    /**
     * Returns a color from a grayscaled palette
     * @param current
     * @param max
     * @return
     */
    protected static String getColor(int current, int max) {
        int val = (int) ((double) current / (double) max * 255d);
        String sval = Integer.toHexString(val);
        if (sval.length() == 1) sval = "0" + sval;
        return "#" + sval + sval + sval;
    }

    /**
     * Creates clustered data for the given plot
     * @param plot
     * @return
     */
    protected static String getData(Plot<Series3D> plot) {

        // Transform
        // y->x->list(z)
        Map<String, Map<String, List<String>>> map = getTransformedData(plot);

        // Obtain first entry and build header
        List<String> header = new ArrayList<String>();
        List<String> bars = getBars(plot);
        header.add(plot.getLabels().x);
        header.addAll(bars);
        Map<String, Integer> indexes = new HashMap<String, Integer>();
        int index = 0;
        for (String bar : bars) {
            indexes.put(bar, index++);
        }

        // Build data
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < header.size(); i++) {
            buffer.append("\"").append(header.get(i)).append("\"");
            if (i < header.size() - 1) buffer.append(" ");
            else buffer.append("\n");
        }
        
        // For each bar
        for (String bar : bars) {
            
            // Obtain data
            Map<String, List<String>> data = map.get(bar);
            index = indexes.get(bar);
            
            // For each point
            for (Entry<String, List<String>> points : data.entrySet()) {
                
                // Print
                for (String z : points.getValue()) {
                    String[] row = new String[indexes.size()];
                    Arrays.fill(row, "NaN");
                    row[index] = z;
                    buffer.append(points.getKey()).append(" ");
                    for (int i = 0; i < row.length; i++) {
                      buffer.append(row[i]);
                      if (i < row.length - 1) buffer.append(" ");
                      else buffer.append("\n");
                  }
                }
            }
        }

        return buffer.toString();
    }

    /**
     * Returns the number of values per cluster
     * @param plot
     * @return
     */
    protected static int getNumBars(Plot<Series3D> plot) {
        return getBars(plot).size();
    }

    /**
     * Returns all bars
     * @param plot
     * @return
     */
    private static List<String> getBars(Plot<Series3D> plot) {
        Set<String> bars = new HashSet<>();
        List<String> list = new ArrayList<String>();
        for (Point3D point : plot.getSeries3D().getData()) {
            if (!bars.contains(point.y)) {
                bars.add(point.y);
                list.add(point.y);
            }
        }
        return list;
    }

    /**
     * Creates a clustered data representation for the given plot: y->x->list(z)
     * @param plot
     * @return
     */
    private static Map<String, Map<String, List<String>>> getTransformedData(Plot<Series3D> plot) {
        Map<String, Map<String, List<String>>> map = new HashMap<String, Map<String, List<String>>>();
        for (Point3D point : plot.getSeries3D().getData()) {
            
            // Obtain series
            Map<String, List<String>> points = map.get(point.y);
            if (points == null) {
                points = new LinkedHashMap<String, List<String>>();
                map.put(point.y, points);
            }
            // Obtain z-values for x
            List<String> values = points.get(point.x);
            if (values == null) {
                values = new ArrayList<String>();
                points.put(point.x, values);
            }
            values.add(point.z);
            if (plot.getSeries3D().isStrict() && values.size() > 1) { 
                throw new RuntimeException("Duplicate value for (" + point.x + ", " + point.y + ")"); 
            }
        }
        return map;
    }
}
