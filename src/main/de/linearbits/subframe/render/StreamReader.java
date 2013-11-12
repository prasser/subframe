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

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for reading the output and error streams of child processes
 * @author Fabian Prasser
 */
class StreamReader implements Runnable {

    /** A buffer for output*/
    private StringBuffer buffer = new StringBuffer();
    /** The wrapped stream*/
    private InputStream in = null;

    /**
     * Creates a new stream reader
     * @param in
     */
    protected StreamReader(final InputStream in) {
        this.in = in;
    }

    /**
     * Returns the buffered content
     * @return
     */
    public String getString() {
        return buffer.toString();
    }
    
    @Override
    public void run() {
        try {
            int i = this.in.read();
            while (i != -1) {
                buffer.append((char) i);
                i = this.in.read();
            }
        } catch (final IOException e) {
            // Die silently
        }
    }
}