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
 * 
 * @author Fabian Prasser
 */
public class GnuPlotParams {
    
    /**
     * The position of the key
     * 
     * @author Fabian Prasser
     */
    public static class KeyPos {
        
        /** The position string */
        private final String position;
        
        /**
         * Creates a new instance
         * @param position
         */
        private KeyPos(String position) {
            this.position = position;
        }
        
        @Override
        public String toString() {
            return this.position;
        }
        
        public static final KeyPos TOP_LEFT     = new KeyPos("top left");
        public static final KeyPos TOP_RIGHT    = new KeyPos("top right");
        public static final KeyPos BOTTOM_LEFT  = new KeyPos("bottom left");
        public static final KeyPos BOTTOM_RIGHT = new KeyPos("bottom right");
        public static final KeyPos OUTSIDE_TOP  = new KeyPos("out horiz center top");
        public static final KeyPos NONE         = new KeyPos("none");
        
        public static final KeyPos AT(double x, double y) {
            return new KeyPos("at " + x + "," + y);
        }
        
        public static final KeyPos AT(double x, double y, String anchor) {
            return new KeyPos("at " + x + "," + y + " " + anchor);
        }
    }
    
    /**
     * Class for line types
     * @author Johanna Eicher
     * @author Fabian Prasser
     */
    public static class LineType {

        /** The position string */
        private final String label;
        
        /**
         * Creates a new instance
         * @param label
         */
        private LineType(String label) {
            this.label = label;
        }
        
        @Override
        public String toString() {
            return this.label;
        }
        
        public static final LineType LINESPOINTS = new LineType("linespoints");
        public static final LineType LINES       = new LineType("lines");
        public static final LineType STEPS       = new LineType("steps");
    }
    
    /** colourValues. */
    public String[] colors                  = new String[] { "1D4599", "11AD34", "E62B17",
                                            "E69F17", "2F3F60", "2F6C3D", "8F463F", "8F743F", "031A49",
                                            "025214", "6D0D03", "6D4903", "7297E6", "67EB84", "F97A6D",
                                            "F9C96D", "A9BDE6", "A6EBB5", "F9B7B0", "F9E0B0" };
    
    /** Should the plot be colorized */
    public boolean  colorize                = false;
    
    /** Logarithmic x axis */
    public Boolean  logX                    = false;
    /** Logarithmic y axis */
    public Boolean  logY                    = false;
    /** Logarithmic z axis */
    public Boolean  logZ                    = false;
    
    /** Minimum value on the x axis */
    public Double   minX                    = null;
    /** Minimum value on the y axis */
    public Double   minY                    = null;
    /** Minimum value on the z axis */
    public Double   minZ                    = null;
    
    /** Maximum value on the x axis */
    public Double   maxX                    = null;
    /** Maximum value on the y axis */
    public Double   maxY                    = null;
    /** Maximum value on the z axis */
    public Double   maxZ                    = null;
    
    /** X-Tics */
    public Double   xTics                    = null;
    /** Y-Tics */
    public Double   yTics                    = null;
    /** T-Tics */
    public Double   zTics                    = null;
    
    /** The size */
    public Double   size                    = 0.6d;
    /** The width/height ratio of the plot */
    public Double   ratio                   = null;
    /** The width of the output (inches) */
    public double   width                   = 5d;
    /** The height of the output (inches) */
    public double   height                  = 3.5d;
    
    /** Offset */
    public double   offsetLeft              = 0;
    /** Offset */
    public double   offsetRight             = 0;
    /** Offset */
    public double   offsetBottom            = 0;
    /** Offset */
    public double   offsetTop               = 0;
    /** Offset xlabel */
    public double   offsetXlabel            = 0;
    /** Offset ylabel */
    public double   offsetYlabel            = 0;
    /** Offset zlabel */
    public double   offsetZlabel            = 0;
    
    /** The width of the box */
    public Double   boxwidth                = 0.75d;
    /** Rotation of the xtics in degrees */
    public Integer  rotateXTicks            = null;
    /** Rotation of the ytics in degrees */
    public Integer  rotateYTicks            = null;
    /** Rotation of the ztics in degrees */
    public Integer  rotateZTicks            = null;
    /** X-ticks are categorical */
    public Boolean  categorialX             = false;
    /** Grid */
    public Boolean  grid                    = true;
    /** The keypos */
    public KeyPos   keypos                  = KeyPos.TOP_RIGHT;
    /** Print the actual values into the plot */
    public Boolean  printValues             = false;
    /** Format string for values in the plot */
    public String   printValuesFormatString = "%.2f";
    /** Offset for labels in the plot */
    public double   printValuesOffset       = +0.5d;

    /** Make the data a grid */
    public Integer  grid3dResolution        = null;
    /** Make the data a grid */
    public Integer  grid3dInterpolation     = null;
    /** Make the data a grid */
    public Integer  xyPlaneAt               = null;

    /** Font specification */
    public String   font                    = null;

    /** The line type */
    public LineType lineType                = LineType.LINESPOINTS;
    /** The line width */
    public double   lineWidth               = 1d;
}
