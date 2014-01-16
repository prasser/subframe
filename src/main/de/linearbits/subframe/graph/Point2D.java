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
 * A 2D point
 * 
 * @author Fabian Prasser
 */
public class Point2D {
	
	/** Coordinate*/
    public final String x;
    /** Coordinate*/
    public final String y;

    /**
     * Creates a new instance
     * @param x
     * @param y
     */
    public Point2D(String x, String y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Point2D other = (Point2D) obj;
        if (x == null) {
            if (other.x != null) return false;
        } else if (!x.equals(other.x)) return false;
        if (y == null) {
            if (other.y != null) return false;
        } else if (!y.equals(other.y)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
        return result;
    }
    
    @Override
    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append("Point2D[").append(x).append(", ").append(y).append("]");
        return b.toString();
    }
}
