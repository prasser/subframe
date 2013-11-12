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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.linearbits.objectselector.SelectorBuilder;
import de.linearbits.objectselector.datatypes.DataType;
import de.linearbits.objectselector.util.ArrayAccessor;

/**
 * This class represents the output of a benchmark as a csv file.
 * 
 * @author Fabian Prasser, Florian Kohlmayer
 */
public class CSVFile {

    /** The separatpr*/
    private static final char   SEPERATOR = ';';
    /** The newline*/
    private static final String NEWLINE   = "\n";

    /**
     * Checks a field for valid syntax
     * @param label
     */
    public static void checkFieldValue(String label) {
        if (label.contains(";") || label.contains(".") || label.contains("\n")) { throw new IllegalArgumentException("Label/value '" +
                label +
                "' must not contain ';' or '.' or linebreaks"); }
    }

    /**
     * Writes the file to disk
     * @param writer
     * @param line
     * @throws IOException
     */
    private static void write(Writer writer, String[] line) throws IOException {
        for (int i = 0; i < line.length; i++) {
            writer.write(line[i]);
            if (i < line.length - 1) {
                writer.write(String.valueOf(SEPERATOR));
            } else {
                writer.write(NEWLINE);
            }
        }
    }

    /** The lines in the file*/
    private List<CSVLine>                     lines     = new ArrayList<CSVLine>();
    /** Mapping labels to indices*/
    private Map<String, Map<String, Integer>> headermap = new HashMap<String, Map<String, Integer>>();
    /** The first header*/
    private String[]                          header1   = null;
    /** The second header*/
    private String[]                          header2   = null;

    /**
     * Creates a new csv file containing the content of the given file
     * @param file
     * @throws IOException
     */
    public CSVFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        this.header1 = reader.readLine().split(String.valueOf(SEPERATOR));
        this.header2 = reader.readLine().split(String.valueOf(SEPERATOR));
        this.buildHeaderMap();
        String line = reader.readLine();
        while (line != null) {
            this.addLine(line.split(String.valueOf(SEPERATOR)));
            line = reader.readLine();
        }
        reader.close();
    }

    /**
     * Creates a new empty file with the given headers
     * @param header1
     * @param header2
     */
    public CSVFile(String[] header1, String[] header2) {
        this.header1 = header1;
        this.header2 = header2;
        this.buildHeaderMap();
    }

    /**
     * Adds a line
     * @param line
     */
    public void addLine(String[] line) {
        this.lines.add(new CSVLine(line, headermap));
    }

    /**
     * Returns a new selector builder
     * 
     * @return
     */
    public SelectorBuilder<String[]> getSelectorBuilder() {
        return getSelectorBuilder(null);
    }

    /**
     * Returns a new selector builder
     * 
     * @param datatypes
     *            Data types for run data
     * @return
     */
    public SelectorBuilder<String[]> getSelectorBuilder(DataType<?>[] datatypes) {

        List<String> labels = new ArrayList<String>();
        List<DataType<?>> types = new ArrayList<DataType<?>>();

        for (int i = 0; i < header1.length; i++) {

            // Run data
            if (header1[i].equals("")) {
                labels.add(header2[i]);
                if (datatypes != null) {
                    types.add(datatypes[i]);
                } else {
                    types.add(DataType.STRING);
                }
            }
            // Benchmark data
            else {
                labels.add(header1[i] + "." + header2[i]);
                types.add(DataType.NUMERIC);
            }
        }

        String[] aLabels = labels.toArray(new String[labels.size()]);
        DataType<?>[] aTypes = types.toArray(new DataType<?>[types.size()]);
        return new SelectorBuilder<String[]>(new ArrayAccessor<String>(aLabels, aTypes));
    }

    /**
     * Returns an iterator
     * @return
     */
    public Iterator<CSVLine> iterator() {
        return new CSVIterator(lines);
    }

    /**
     * Writes the file to the given location
     * @param file
     * @throws IOException
     */
    public void write(File file) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        write(writer, header1);
        write(writer, header2);
        for (CSVLine line : lines) {
            write(writer, line.getData());
        }
        writer.close();
    }

    /**
     * Builds the map linking labels with indices
     */
    private void buildHeaderMap() {
        for (int i = 0; i < header1.length; i++) {
            if (!headermap.containsKey(header1[i])) {
                headermap.put(header1[i], new HashMap<String, Integer>());
            }
            headermap.get(header1[i]).put(header2[i], i);
        }
    }
}
