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
import de.linearbits.subframe.graph.PlotHistogram;
import de.linearbits.subframe.graph.PlotLinesClustered;
import de.linearbits.subframe.graph.Series3D;
import de.linearbits.subframe.io.CSVFile;
import de.linearbits.subframe.render.GnuPlotParams;
import de.linearbits.subframe.render.GnuPlotParams.KeyPos;
import de.linearbits.subframe.render.LaTeX;
import de.linearbits.subframe.render.PlotGroup;

/**
 * Example benchmark
 * @author Fabian Prasser
 */
public class SortEvaluation {

	/**
	 * Main
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
    public static void main(String[] args) throws IOException, ParseException {
        
        CSVFile file = new CSVFile(new File("src/example/example1/sort.csv"));

        List<PlotGroup> groups = new ArrayList<PlotGroup>();
        groups.add(getGroup1(file));
        groups.add(getGroup2(file));

        LaTeX.plot(groups, "src/example/example1/sort");
        
    }
    
    /**
     * Returns the first plot group
     * @param file
     * @return
     * @throws ParseException
     */
    private static PlotGroup getGroup1(CSVFile file) throws ParseException{

        Series3D series = getSeriesForLinesPlot(file, "JavaQuickSort");
        series.append(getSeriesForLinesPlot(file, "ColtQuickSort"));
        series.append(getSeriesForLinesPlot(file, "ColtMergeSort"));
        
        List<Plot<?>> plots = new ArrayList<Plot<?>>();
        plots.add(new PlotLinesClustered("Sorting Arrays", 
                                         new Labels("Size", "Execution time [ns]"),
                                         series));
        
        GnuPlotParams params = new GnuPlotParams();
        params.rotateXTicks = -90;
        params.keypos = KeyPos.TOP_LEFT;
        params.size = 0.6d;
        return new PlotGroup("Comparison of algorithms", plots, params, 1.0d);
    }
    
    /**
     * Returns the second plot group
     * @param file
     * @return
     * @throws ParseException
     */
    private static PlotGroup getGroup2(CSVFile file) throws ParseException{

        List<Plot<?>> plots = new ArrayList<Plot<?>>();
        Series3D series;
        
        series = getSeriesForHistogram(file, "JavaQuickSort");
        plots.add(new PlotHistogram("JavaQuickSort", 
                                    new Labels("Size", "Execution time [ns]"),
                                    series));
        
        series = getSeriesForHistogram(file, "ColtQuickSort");
        plots.add(new PlotHistogram("ColtQuickSort", 
                                    new Labels("Size", "Execution time [ns]"),
                                    series));
        
        series = getSeriesForHistogram(file, "ColtMergeSort");
        plots.add(new PlotHistogram("ColtMergeSort", 
                                    new Labels("Size", "Execution time [ns]"),
                                    series));
        
        GnuPlotParams params = new GnuPlotParams();
        params.rotateXTicks = -90;
        params.keypos = KeyPos.TOP_LEFT;
        params.size = 0.6d;
        params.minY = 0d;
        return new PlotGroup("Individual execution times", plots, params, 1.0d);
    }

    /**
     * Returns a series that can be clustered by size
     * @param file
     * @param method
     * @return
     * @throws ParseException
     */
    private static Series3D getSeriesForHistogram(CSVFile file, String method) throws ParseException {

        Selector<String[]> selector = file.getSelectorBuilder()
                                          .field("Method").equals(method)
                                          .build();
        
        Series3D series = new Series3D(file, selector, 
                                       new Field("Size"),
                                       new Field("Time", Analyzer.ARITHMETIC_MEAN),
                                       new Field("Time", Analyzer.STANDARD_DEVIATION));
        
        return series;
    }

    /**
     * Returns a series that can be clustered by size
     * @param file
     * @param method
     * @return
     * @throws ParseException
     */
    private static Series3D getSeriesForLinesPlot(CSVFile file, String method) throws ParseException {

        Selector<String[]> selector = file.getSelectorBuilder()
                                          .field("Method").equals(method)
                                          .build();
        
        Series3D series = new Series3D(file, selector, 
                                       new Field("Size"),
                                       new Field("Method"),
                                       new Field("Time", Analyzer.ARITHMETIC_MEAN));
        
        return series;
    }
}
