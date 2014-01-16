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
 * A 3D Point
 * 
 * @author Fabian Prasser
 */
public class Point3D {
	
	/** Coordinate*/
    public final String x;
    /** Coordinate*/
    public final String y;
    /** Coordinate*/
    public final String z;

    /**
     * Creates a new instance
     * @param x
     * @param y
     * @param z
     */
    protected Point3D(String x, String y, String z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Point3D other = (Point3D) obj;
        if (x == null) {
            if (other.x != null) return false;
        } else if (!x.equals(other.x)) return false;
        if (y == null) {
            if (other.y != null) return false;
        } else if (!y.equals(other.y)) return false;
        if (z == null) {
            if (other.z != null) return false;
        } else if (!z.equals(other.z)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
        result = prime * result + ((z == null) ? 0 : z.hashCode());
        return result;
    }

    @Override
    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append("Point3D[").append(x).append(", ").append(y).append(", ").append(z).append("]");
        return b.toString();
    }
}
