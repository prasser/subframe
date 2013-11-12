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

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.linearbits.objectselector.Selector;
import de.linearbits.subframe.analyzer.Analyzer;
import de.linearbits.subframe.analyzer.buffered.BufferedArithmetricMeanAnalyzer;
import de.linearbits.subframe.analyzer.buffered.BufferedStandardDeviationAnalyzer;
import de.linearbits.subframe.graph.Field;
import de.linearbits.subframe.graph.Labels;
import de.linearbits.subframe.graph.Plot;
import de.linearbits.subframe.graph.PlotHistogram;
import de.linearbits.subframe.graph.PlotHistogramClustered;
import de.linearbits.subframe.graph.PlotHistogramStacked;
import de.linearbits.subframe.graph.PlotLines;
import de.linearbits.subframe.graph.PlotLinesClustered;
import de.linearbits.subframe.graph.Series2D;
import de.linearbits.subframe.graph.Series3D;
import de.linearbits.subframe.io.CSVFile;
import de.linearbits.subframe.render.GnuPlot;
import de.linearbits.subframe.render.GnuPlotParams;
import de.linearbits.subframe.render.LaTeX;
import de.linearbits.subframe.render.PlotGroup;
import de.linearbits.subframe.render.GnuPlotParams.KeyPos;

/**
 * Class with JUnit tests
 * @author Fabian Prasser
 */
public class TestEvaluations extends TestBase {

    @Test
    public void testAppendSeries2D(){
        
        try {
            CSVFile file = new CSVFile(new File("src/test/test1.csv"));
            
            Selector<String[]> selector = file.getSelectorBuilder()
                                              .field("Run").equals("Test")
                                              .build();
            
            Series2D series = new Series2D(file, selector, "Init", 
                                           new Field("Init", Analyzer.ARITHMETIC_MEAN));
            
            series.append(new Series2D(file, selector, "Sort", 
                                    new Field("Sort", Analyzer.ARITHMETIC_MEAN)));
      

            series.append(new Series2D(file, selector, "Sum", 
                                    new Field("Sum", Analyzer.ARITHMETIC_MEAN)));
            
            PlotHistogram plot = new PlotHistogram("Initializing, sorting and summing up an array", 
                                                   new Labels("Phase", "Execution time [ns]"),
                                                   series);
            
            GnuPlot.plot(plot, "src/test/test1_1");
            checkAndDelete("src/test/test1_1.pdf");
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    

    @Test
    public void testAppendSeries3D(){
        
        try {
            CSVFile file = new CSVFile(new File("src/test/test1.csv"));
            
            Selector<String[]> selector = file.getSelectorBuilder()
                                              .field("Run").equals("Test")
                                              .build();
            
            Series3D series = new Series3D(file, selector, "Init", 
                                           new Field("Init", Analyzer.ARITHMETIC_MEAN),
                                           new Field("Init", Analyzer.STANDARD_DEVIATION));
            
            series.append(new Series3D(file, selector, "Sort", 
                                    new Field("Sort", Analyzer.ARITHMETIC_MEAN),
                                    new Field("Sort", Analyzer.STANDARD_DEVIATION)));
      

            series.append(new Series3D(file, selector, "Sum", 
                                    new Field("Sum", Analyzer.ARITHMETIC_MEAN),
                                    new Field("Sum", Analyzer.STANDARD_DEVIATION)));
            
            PlotHistogram plot = new PlotHistogram("Initializing, sorting and summing up an array", 
                                                   new Labels("Phase", "Execution time [ns]"),
                                                   series);
            
            GnuPlot.plot(plot, "src/test/test1_2");
            checkAndDelete("src/test/test1_2.pdf");
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testHistogram(){
        
        try {
            CSVFile file = new CSVFile(new File("src/test/test2.csv"));
            
            Selector<String[]> selector = file.getSelectorBuilder()
                                              .field("Run").equals("Test")
                                              .and()
                                              .field("Phase").equals("Sort")
                                              .build();
            
            Series2D series = new Series2D(file, selector, 
                                           new Field("Size"), 
                                           new Field("Execution time", Analyzer.VALUE),
                                           new BufferedArithmetricMeanAnalyzer());
            
            PlotHistogram plot = new PlotHistogram("Sorting arrays of different sizes", 
                                                   new Labels("Size", "Execution time [ns]"),
                                                   series);
            
            GnuPlotParams params = new GnuPlotParams();
            params.logY = true;
            params.xticsrotate = -90;
            GnuPlot.plot(plot, params, "src/test/test2_1");
            checkAndDelete("src/test/test2_1.pdf");
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    

    @Test
    public void testSeriesWithTwoAnalyzers(){
        
        try {
            CSVFile file = new CSVFile(new File("src/test/test2.csv"));
            
            Selector<String[]> selector = file.getSelectorBuilder()
                                              .field("Run").equals("Test")
                                              .and()
                                              .field("Phase").equals("Sort")
                                              .build();
            
            Series3D series = new Series3D(file, selector, 
                                           new Field("Size"), 
                                           new Field("Execution time", Analyzer.VALUE),
                                           new BufferedArithmetricMeanAnalyzer(),
                                           new BufferedStandardDeviationAnalyzer());
            
            PlotHistogram plot = new PlotHistogram("Sorting arrays of different sizes", 
                                                   new Labels("Size", "Execution time [ns]"),
                                                   series);
            
            GnuPlotParams params = new GnuPlotParams();
            params.logY = true;
            params.xticsrotate = -90;
            GnuPlot.plot(plot, params, "src/test/test2_2");
            checkAndDelete("src/test/test2_2.pdf");
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    

    @Test
    public void testLinesSeries2D(){
        
        try {
            CSVFile file = new CSVFile(new File("src/test/test2.csv"));
            
            Selector<String[]> selector = file.getSelectorBuilder()
                                              .field("Run").equals("Test")
                                              .and()
                                              .field("Phase").equals("Sort")
                                              .build();
            
            Series2D series = new Series2D(file, selector, 
                                           new Field("Size"), 
                                           new Field("Execution time", Analyzer.VALUE),
                                           new BufferedArithmetricMeanAnalyzer());
            
            PlotLines plot = new PlotLines("Sorting arrays of different sizes", 
                                           new Labels("Size", "Execution time [ns]"),
                                           series);

            GnuPlotParams params = new GnuPlotParams();
            params.xticsrotate = -90;
            GnuPlot.plot(plot, params, "src/test/test2_3");
            checkAndDelete("src/test/test2_3.pdf");
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    

    @Test
    public void testLinesSeries3D(){
        
        try {
            CSVFile file = new CSVFile(new File("src/test/test2.csv"));
            
            Selector<String[]> selector = file.getSelectorBuilder()
                                              .field("Run").equals("Test")
                                              .and()
                                              .field("Phase").equals("Sort")
                                              .build();
            
            Series3D series = new Series3D(file, selector, 
                                           new Field("Size"), 
                                           new Field("Execution time", Analyzer.VALUE),
                                           new BufferedArithmetricMeanAnalyzer(),
                                           new BufferedStandardDeviationAnalyzer());
            
            PlotLines plot = new PlotLines("Sorting arrays of different sizes", 
                                           new Labels("Size", "Execution time [ns]"),
                                           series);
            
            GnuPlotParams params = new GnuPlotParams();
            params.xticsrotate = -90;
            GnuPlot.plot(plot, params, "src/test/test2_4");
            checkAndDelete("src/test/test2_4.pdf");
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    

    @Test
    public void testClusteredHistogram(){
        
        try {
            CSVFile file = new CSVFile(new File("src/test/test2.csv"));
            
            Selector<String[]> selector = file.getSelectorBuilder()
                                              .field("Run").equals("Test")
                                              .build();
            
            Series3D series = new Series3D(file, selector, 
                                           new Field("Size"),
                                           new Field("Phase"), 
                                           new Field("Execution time", Analyzer.VALUE),
                                           new BufferedArithmetricMeanAnalyzer());
            
            PlotHistogramClustered plot = new PlotHistogramClustered("Creating, sorting and summing up arrays of different sizes", 
                                                                     new Labels("Size", "Execution time [ns]"),
                                                                     series);
            
            GnuPlotParams params = new GnuPlotParams();
            params.xticsrotate = -90;
            params.keypos = KeyPos.TOP_LEFT;
            GnuPlot.plot(plot, params, "src/test/test2_5");
            checkAndDelete("src/test/test2_5.pdf");
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    

    @Test
    public void testClusteredLines() {
        
        try {
            CSVFile file = new CSVFile(new File("src/test/test2.csv"));
            
            Selector<String[]> selector = file.getSelectorBuilder()
                                              .field("Run").equals("Test")
                                              .build();
            
            Series3D series = new Series3D(file, selector, 
                                           new Field("Size"),
                                           new Field("Phase"), 
                                           new Field("Execution time", Analyzer.VALUE),
                                           new BufferedArithmetricMeanAnalyzer());
            
            PlotLinesClustered plot = new PlotLinesClustered("Creating, sorting and summing up arrays of different sizes", 
                                                             new Labels("Size", "Execution time [ns]"),
                                                             series);
            
            GnuPlotParams params = new GnuPlotParams();
            params.xticsrotate = -90;
            params.keypos = KeyPos.TOP_LEFT;
            GnuPlot.plot(plot, params, "src/test/test2_6");
            checkAndDelete("src/test/test2_6.pdf");
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    

    @Test
    public void testStackedHistogram() {
        
        try {
            CSVFile file = new CSVFile(new File("src/test/test2.csv"));
            
            Selector<String[]> selector = file.getSelectorBuilder()
                                              .field("Run").equals("Test")
                                              .build();
            
            Series3D series = new Series3D(file, selector, 
                                           new Field("Size"),
                                           new Field("Phase"), 
                                           new Field("Execution time", Analyzer.VALUE),
                                           new BufferedArithmetricMeanAnalyzer());
            
            PlotHistogramStacked plot = new PlotHistogramStacked("Creating, sorting and summing up arrays of different sizes", 
                                                             new Labels("Size", "Execution time [ns]"),
                                                             series);
            
            GnuPlotParams params = new GnuPlotParams();
            params.xticsrotate = -90;
            params.keypos = KeyPos.TOP_LEFT;
            GnuPlot.plot(plot, params, "src/test/test2_7");
            checkAndDelete("src/test/test2_7.pdf");
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    

    @Test
    public void testLaTeX() {
        
        try {
            CSVFile file = new CSVFile(new File("src/test/test2.csv"));
            
            List<PlotGroup> groups = new ArrayList<PlotGroup>();
            groups.add(getGroup1(file));
            groups.add(getGroup2(file));

            LaTeX.plot(groups, "src/test/test2_8");
            checkAndDelete("src/test/test2_8.pdf");
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Returns the first plot group
     * @param file
     * @return
     * @throws ParseException
     */
    private PlotGroup getGroup1(CSVFile file) throws ParseException{

        Selector<String[]> selector = file.getSelectorBuilder()
                                          .field("Run").equals("Test")
                                          .build();
        
        Series3D series = new Series3D(file, selector, 
                                       new Field("Size"),
                                       new Field("Phase"), 
                                       new Field("Execution time", Analyzer.VALUE),
                                       new BufferedArithmetricMeanAnalyzer());

        List<Plot<?>> list = new ArrayList<Plot<?>>();
        list.add(new PlotHistogramClustered("Creating, sorting and summing up arrays of different sizes",
                                             new Labels("Size", "Execution time [ns]"),
                                             series));
        
        list.add(new PlotHistogramStacked("Creating, sorting and summing up arrays of different sizes",
                                           new Labels("Size", "Execution time [ns]"),
                                           series));

        list.add(new PlotLinesClustered("Creating, sorting and summing up arrays of different sizes",
                                         new Labels("Size", "Execution time [ns]"),
                                         series));
        
        GnuPlotParams params = new GnuPlotParams();
        params.xticsrotate = -90;
        params.keypos = KeyPos.TOP_LEFT;
        params.size = 0.6d;
        return new PlotGroup("Clustered plots", list, params, 0.5d);
    }
    
    /**
     * Returns the second plot group
     * @param file
     * @return
     * @throws ParseException
     */
    private PlotGroup getGroup2(CSVFile file) throws ParseException{

        Selector<String[]> selector = file.getSelectorBuilder()
                                          .field("Run").equals("Test")
                                          .and()
                                          .field("Phase").equals("Sort")
                                          .build();
        
        Series2D series2D = new Series2D(file, selector, 
                                       new Field("Size"), 
                                       new Field("Execution time", Analyzer.VALUE),
                                       new BufferedArithmetricMeanAnalyzer());

        Series3D series3D = new Series3D(file, selector, 
                                       new Field("Size"), 
                                       new Field("Execution time", Analyzer.VALUE),
                                       new BufferedArithmetricMeanAnalyzer(),
                                       new BufferedStandardDeviationAnalyzer());

        List<Plot<?>> list = new ArrayList<Plot<?>>();
        list.add(new PlotHistogram("Sorting arrays of different sizes",
                                    new Labels("Size", "Execution time [ns]"),
                                    series2D));

        list.add(new PlotHistogram("Sorting arrays of different sizes",
                                    new Labels("Size", "Execution time [ns]"),
                                    series3D));

        list.add(new PlotLines("Sorting arrays of different sizes",
                                new Labels("Size", "Execution time [ns]"),
                                series2D));

        list.add(new PlotLines("Sorting arrays of different sizes",
                                new Labels("Size", "Execution time [ns]"),
                                series3D));

        GnuPlotParams params = new GnuPlotParams();
        params.xticsrotate = -90;
        params.keypos = KeyPos.TOP_LEFT;
        params.size = 0.6d;
        return new PlotGroup("Non-clustered plots", list, params, 0.5d);
    }
}
