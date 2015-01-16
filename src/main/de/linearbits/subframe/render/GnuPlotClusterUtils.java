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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
        Map<String, Map<String, String>> map = getTransformedData(plot);

        // Obtain first entry and build header
        List<String> header = new ArrayList<String>();
        Map<String, Integer> indexes = new HashMap<String, Integer>();
        Entry<String, Map<String, String>> first = map.entrySet().iterator().next();
        header.add(plot.getLabels().x);
        int index = 0;
        for (String key : first.getValue().keySet()) {
            indexes.put(key, index++);
            header.add(key);
        }

        // Build data
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < header.size(); i++) {
            buffer.append("\"").append(header.get(i)).append("\"");
            if (i < header.size() - 1) buffer.append(" ");
            else buffer.append("\n");
        }
        for (Entry<String, Map<String, String>> entry : map.entrySet()) {
            buffer.append(entry.getKey()).append(" ");
            String[] data = new String[indexes.size()];
            for (Entry<String, String> value : entry.getValue().entrySet()) {
                data[indexes.get(value.getKey())] = value.getValue();
            }
            for (int i = 0; i < data.length; i++) {
                buffer.append(data[i] != null ? data[i] : "-");
                buffer.append(i < data.length - 1 ? " " : "\n");
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

        return getTransformedData(plot).entrySet().iterator().next().getValue().keySet().size();
    }

    /**
     * Creates a clustered data representation for the given plot
     * @param plot
     * @return
     */
    protected static Map<String, Map<String, String>> getTransformedData(Plot<Series3D> plot) {

        Map<String, Map<String, String>> map = new LinkedHashMap<String, Map<String, String>>();
        for (Point3D point : plot.getSeries3D().getData()) {
            if (!map.containsKey(point.x)) {
                map.put(point.x, new LinkedHashMap<String, String>());
            }
            if (map.get(point.x).containsKey(point.y)) { throw new RuntimeException("Duplicate value for (" + point.x +
                                                                                    ", " + point.y + ")"); }
            map.get(point.x).put(point.y, point.z);
        }

        return map;
    }
}
