/*
 * SUBFRAME - Simple Java Benchmarking Framework
 * Copyright (C) 2012 - 2016 Fabian Prasser and contributors
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
package example1;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import de.linearbits.objectselector.Selector;
import de.linearbits.subframe.analyzer.Analyzer;
import de.linearbits.subframe.graph.Field;
import de.linearbits.subframe.graph.Labels;
import de.linearbits.subframe.graph.Plot;
import de.linearbits.subframe.graph.PlotLinesClustered;
import de.linearbits.subframe.graph.Series3D;
import de.linearbits.subframe.io.CSVFile;
import de.linearbits.subframe.render.GnuPlotParams;
import de.linearbits.subframe.render.GnuPlotParams.KeyPos;
import de.linearbits.subframe.render.LaTeX;
import de.linearbits.subframe.render.PlotGroup;

/**
 * Example benchmark
 * 
 * @author Fabian Prasser
 */
public class BoxingEvaluation {

	/**
	 * Main. Run with -javaagent:/lib/javassist.jar
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
    public static void main(String[] args) throws IOException, ParseException {
        
        CSVFile file = new CSVFile(new File("src/example/example1/boxing.csv"));

        List<PlotGroup> groups = new ArrayList<PlotGroup>();
        groups.add(getGroup1(file));
        groups.add(getGroup2(file));

        LaTeX.plot(groups, "src/example/example1/boxing");
        
    }
    
    /**
     * Build the first plot group
     * @param file
     * @return
     * @throws ParseException
     */
    private static PlotGroup getGroup1(CSVFile file) throws ParseException{

        List<Plot<?>> plots = new ArrayList<Plot<?>>();
        
        Series3D series = getSeriesForGroup1(file, "SizeMX");
        plots.add(new PlotLinesClustered("Memory consumption of arrays (MX)", 
                                         new Labels("Array size", "Size [bytes]"),
                                         series));
        series = getSeriesForGroup1(file, "SizeJVM");
        plots.add(new PlotLinesClustered("Memory consumption of arrays (JVM)", 
                                         new Labels("Array size", "Size [bytes]"),
                                         series));
        series = getSeriesForGroup1(file, "SizeInstr");
        plots.add(new PlotLinesClustered("Memory consumption of arrays (Instrumentation)", 
                                         new Labels("Array size", "Size [bytes]"),
                                         series));
             
        GnuPlotParams params = new GnuPlotParams();
        params.rotateXTicks = -90;
        params.keypos = KeyPos.TOP_LEFT;
        params.size = 0.6d;
        params.minY = 0d;
        return new PlotGroup("Memory consumption of arrays", plots, params, 1.0d);
    }

    /**
     * Build the second plot group
     * @param file
     * @return
     * @throws ParseException
     */
    private static PlotGroup getGroup2(CSVFile file) throws ParseException{

        List<Plot<?>> plots = new ArrayList<Plot<?>>();
        
        Series3D series = getSeriesForGroup2(file, "byte[]");
        plots.add(new PlotLinesClustered("Memory consumption of arrays (byte[])", 
                                         new Labels("Array size", "Size [bytes]"),
                                         series));

        series = getSeriesForGroup2(file, "Byte[]");
        plots.add(new PlotLinesClustered("Memory consumption of arrays (Byte[])", 
                                         new Labels("Array size", "Size [bytes]"),
                                         series));
        
        series = getSeriesForGroup2(file, "int[]");
        plots.add(new PlotLinesClustered("Memory consumption of arrays (int[])", 
                                         new Labels("Array size", "Size [bytes]"),
                                         series));
        
        series = getSeriesForGroup2(file, "Integer[]");
        plots.add(new PlotLinesClustered("Memory consumption of arrays (Integer[])", 
                                         new Labels("Array size", "Size [bytes]"),
                                         series));

        series = getSeriesForGroup2(file, "long[]");
        plots.add(new PlotLinesClustered("Memory consumption of arrays (long[])", 
                                         new Labels("Array size", "Size [bytes]"),
                                         series));
        
        series = getSeriesForGroup2(file, "Long[]");
        plots.add(new PlotLinesClustered("Memory consumption of arrays (Long[])", 
                                         new Labels("Array size", "Size [bytes]"),
                                         series));
        
        GnuPlotParams params = new GnuPlotParams();
        params.rotateXTicks = -90;
        params.keypos = KeyPos.TOP_LEFT;
        params.size = 0.6d;
        params.minY = 0d;
        return new PlotGroup("Memory consumption of arrays", plots, params, .5d);
    }
    
    /**
     * Returns a series that can be clustered by size
     * @param file
     * @param method
     * @return
     * @throws ParseException
     */
    private static Series3D getSeriesForGroup1(CSVFile file, String method) throws ParseException {
        Series3D series = getSeriesForGroup1(file, "byte[]", method);
        series.append(getSeriesForGroup1(file, "Byte[]", method));
        series.append(getSeriesForGroup1(file, "int[]", method));
        series.append(getSeriesForGroup1(file, "Integer[]", method));
        series.append(getSeriesForGroup1(file, "long[]", method));
        series.append(getSeriesForGroup1(file, "Long[]", method));
        return series;
    }

    /**
     * Returns a series that can be clustered by size
     * @param file
     * @param type
     * @param method
     * @return
     * @throws ParseException
     */
    private static Series3D getSeriesForGroup1(CSVFile file, String type, String method) throws ParseException {

        Selector<String[]> selector = file.getSelectorBuilder()
                                          .field("Type").equals(type)
                                          .build();
        
        Series3D series = new Series3D(file, selector, 
                                       new Field("Size"),
                                       new Field("Type"),
                                       new Field(method, Analyzer.VALUE));
        
        return series;
    }

    /**
     * Returns a series that can be clustered by size
     * @param file
     * @param type
     * @param method
     * @return
     * @throws ParseException
     */
    private static Series3D getSeriesForGroup2(CSVFile file, String type, String method) throws ParseException {

        Selector<String[]> selector = file.getSelectorBuilder()
                                          .field("Type").equals(type)
                                          .build();
        
        Series3D series = new Series3D(file, selector, 
                                       new Field("Size"),
                                       method,
                                       new Field(method, Analyzer.VALUE));
        
        return series;
    }

    /**
     * Returns a series that can be clustered by size
     * @param file
     * @param type
     * @return
     * @throws ParseException
     */
    private static Series3D getSeriesForGroup2(CSVFile file, String type) throws ParseException {
        Series3D series = getSeriesForGroup2(file, type, "SizeJVM");
        series.append(getSeriesForGroup2(file, type, "SizeMX"));
        series.append(getSeriesForGroup2(file, type, "SizeInstr"));
        return series;
    }
}
