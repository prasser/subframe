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

import java.util.Iterator;
import java.util.List;

/**
 * An iterator for csv files
 * 
 * @author Fabian Prasser, Florian Kohlmayer
 */
public class CSVIterator implements Iterator<CSVLine> {

    /** The csv file */
    private final List<CSVLine> lines;

    /** The current line index */
    private int                 index;

    /**
     * Instantiates a new line iterator.
     * 
     * @param lines the csvfile
     */
    public CSVIterator(final List<CSVLine> lines) {
        this.lines = lines;
        this.index = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return index < lines.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#next()
     */
    @Override
    public CSVLine next() {
        final CSVLine nextLine = lines.get(index);
        index++;
        return nextLine;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported");
    }
}
