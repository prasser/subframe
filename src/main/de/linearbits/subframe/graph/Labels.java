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
 * Represents labels for a plot
 * 
 * @author Fabian Prasser
 *
 */
public class Labels {

	/** Label*/
    public final String x;
    /** Label*/
    public final String y;
    /** Label*/
    public final String z;

    /**
     * Creates a new instance
     * @param x
     */
    public Labels(String x) {
        this.x = x;
        this.y = "";
        this.z = "";
    }

    /**
     * Creates a new instance
     * @param x
     * @param y
     */
    public Labels(String x, String y) {
        this.x = x;
        this.y = y;
        this.z = "";
    }

    /**
     * Creates a new instance
     * @param x
     * @param y
     * @param z
     */
    public Labels(String x, String y, String z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
