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
package de.linearbits.subframe.analyzer.stream;

import de.linearbits.subframe.analyzer.Analyzer;

/**
 * Base class for analyzers that compute the result incrementally, based on incoming values
 * @author Fabian Prasser
 */
public abstract class StreamAnalyzer extends Analyzer{

    protected double value = 0xDEADBEEF;
    
    /**
     * Creates a new instance
     * @param label
     */
    protected StreamAnalyzer(String label) {
        super(label);
    }

    @Override
    public double getValue() {
        if (value == 0XDEADBEEF) {
            throw new RuntimeException("No values specified!");
        }
        return value;
    }
}
