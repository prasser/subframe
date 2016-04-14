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
 * A point for a box and whisker plot
 * 
 * @author Fabian Prasser
 */
public class PointBoxAndWhisker {
	
	/** Coordinate*/
    public final String id;
    /** Coordinate*/
    public final String min;
    /** Coordinate*/
    public final String quartile1;
    /** Coordinate*/
    public final String median;
    /** Coordinate*/
    public final String quartile3;
    /** Coordinate*/
    public final String max;
    /** Coordinate*/
    public final String boxwidth = "0.5";
    /** Coordinate*/
    public final String title;

    /**
     * Creates a new instance
     * @param id
     * @param min
     * @param quartile1
     * @param median
     * @param quartile3
     * @param max
     * @param title
     */
    public PointBoxAndWhisker(String id,
                       String min,
                       String quartile1,
                       String median,
                       String quartile3,
                       String max,
                       String title) {
        this.id = id;
        this.min = min;
        this.quartile1 = quartile1;
        this.median = median;
        this.quartile3 = quartile3;
        this.max = max;
        this.title = title;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((boxwidth == null) ? 0 : boxwidth.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((max == null) ? 0 : max.hashCode());
        result = prime * result + ((median == null) ? 0 : median.hashCode());
        result = prime * result + ((min == null) ? 0 : min.hashCode());
        result = prime * result + ((quartile1 == null) ? 0 : quartile1.hashCode());
        result = prime * result + ((quartile3 == null) ? 0 : quartile3.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PointBoxAndWhisker other = (PointBoxAndWhisker) obj;
        if (boxwidth == null) {
            if (other.boxwidth != null) return false;
        } else if (!boxwidth.equals(other.boxwidth)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (max == null) {
            if (other.max != null) return false;
        } else if (!max.equals(other.max)) return false;
        if (median == null) {
            if (other.median != null) return false;
        } else if (!median.equals(other.median)) return false;
        if (min == null) {
            if (other.min != null) return false;
        } else if (!min.equals(other.min)) return false;
        if (quartile1 == null) {
            if (other.quartile1 != null) return false;
        } else if (!quartile1.equals(other.quartile1)) return false;
        if (quartile3 == null) {
            if (other.quartile3 != null) return false;
        } else if (!quartile3.equals(other.quartile3)) return false;
        if (title == null) {
            if (other.title != null) return false;
        } else if (!title.equals(other.title)) return false;
        return true;
    }

    @Override
    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append("PointBoxAndWhisker[").append(id).append(", ").append(min).append(", ").append(quartile1)
                                       .append(median).append(", ").append(quartile3).append(", ").append(max)
                                       .append(title).append("]");
        return b.toString();
    }
}
