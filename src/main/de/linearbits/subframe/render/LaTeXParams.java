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
 * Parameters for LaTeX
 * 
 * @author Fabian Prasser
 */
public class LaTeXParams {

    /**
     * Page format
     * 
     * @author Fabian Prasser
     */
    public static enum PageFormat {
        A4,
        LETTER
    }

    /** Margin in inches */
    public double     margin     = 0.5d;

    /** Page format */
    public PageFormat pageFormat = PageFormat.LETTER;
}
