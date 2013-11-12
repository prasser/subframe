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

import de.linearbits.subframe.io.CSVFile;

/**
 * Represents a field in the resulting csv file
 * 
 * @author Fabian Prasser
 */
public class Field {

	/** Category*/
    protected String category;
    
    /** Measure label*/
    protected String measure;

    /**
     * Constructs a new field with empty category (for run data)
     * @param measure
     */
    public Field(String measure) {
        CSVFile.checkFieldValue(measure);
        this.category = "";
        this.measure = measure;
    }

    /**
     * Constructs a field consisting of category and measure label
     * @param category
     * @param measure
     */
    public Field(String category, String measure) {
        CSVFile.checkFieldValue(measure);
        CSVFile.checkFieldValue(category);
        this.category = category;
        this.measure = measure;
    }

    @Override
    public String toString() {
        if (!category.equals("")) return category + "." + measure;
        else return measure;
    }
}
