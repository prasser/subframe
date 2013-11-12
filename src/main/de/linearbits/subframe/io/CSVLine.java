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
package de.linearbits.subframe.io;

import java.util.Map;

/**
 * A line in the csv file.
 * 
 * @author Prasser, Kohlmayer
 */
public class CSVLine {

    /** The header. */
    private Map<String, Map<String, Integer>> header;

    /** The line. */
    private String[]                          line;

    /**
     * Instantiates a new CSV line.
     * 
     * @param line   the line
     * @param header the header
     */
    public CSVLine(final String[] line, final Map<String, Map<String, Integer>> header) {
        this.line = line;
        this.header = header;
    }

    /**
     * Gets the value
     * 
     * @param header the header
     * @return the string
     */
    public String get(final String header1, final String header2) {
        if (!this.header.containsKey(header1)) {
            String message = header1.equals("") ? header2 : header1 + "." + header2;
            throw new IllegalArgumentException("Invalid field: " + message);
        }
        if (!this.header.get(header1).containsKey(header2)) {
            String message = header1.equals("") ? header2 : header1 + "." + header2;
            throw new IllegalArgumentException("Invalid field: " + message);
        }
        return line[this.header.get(header1).get(header2)];
    }

    /**
     * Gets the line.
     * 
     * @return the line
     */
    public String[] getData() {
        return line;
    }
}
