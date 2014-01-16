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

/**
 * Parameters for GnuPlot
 * @author Fabian Prasser
 */
public class GnuPlotParams {
    
    /**
     * The position of the key
     * @author Fabian Prasser
     */
    public static enum KeyPos {
        TOP_LEFT {
            public String toString(){ return "top left"; }
        },
        TOP_RIGHT {
            public String toString(){ return "top right"; }
        },
        BOTTOM_LEFT {
            public String toString(){ return "bottom left"; }
        }, 
        BOTTOM_RIGHT {
            public String toString(){ return "bottom right"; }
        }, 
        OUTSIDE_TOP {
            public String toString(){ return "out horiz center top"; }
        }, 
        NONE {
            public String toString(){ return "none"; }
        }
    }

    /** Logarithmic x axis*/
    public Boolean logX = false;
    /** Logarithmic y axis*/
    public Boolean logY = false;
    /** Logarithmic z axis*/
    public Boolean logZ = false;
    /** Minimum value on the x axis*/
    public Double minX = null;
    /** Minimum value on the y axis*/
    public Double minY = null;
    /** Minimum value on the z axis*/
    public Double minZ = null;
    /** Maximum value on the x axis*/
    public Double maxX = null;
    /** Maximum value on the y axis*/
    public Double maxY = null;
    /** Maximum value on the z axis*/
    public Double maxZ = null;
    /** The size*/
    public Double size = 0.6d;
    /** The width/height ratio of the plot*/
    public Double ratio = null;
    /** The width of the box*/
    public Double boxwidth = 0.75d;
    /** Rotation of the xtics in degrees*/
    public Integer xticsrotate = null;
    /** Grid*/
    public Boolean grid = true;
    /** The keypos*/
    public KeyPos keypos = KeyPos.TOP_RIGHT;
}
