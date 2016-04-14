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
package example2;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import de.linearbits.subframe.analyzer.Analyzer;
import de.linearbits.subframe.graph.Field;
import de.linearbits.subframe.graph.Labels;
import de.linearbits.subframe.graph.Plot;
import de.linearbits.subframe.graph.PlotBoxAndWhisker;
import de.linearbits.subframe.graph.SeriesBoxAndWhisker;
import de.linearbits.subframe.io.CSVFile;
import de.linearbits.subframe.render.GnuPlotParams;
import de.linearbits.subframe.render.GnuPlotParams.KeyPos;
import de.linearbits.subframe.render.LaTeXParams.PageFormat;
import de.linearbits.subframe.render.LaTeX;
import de.linearbits.subframe.render.LaTeXParams;
import de.linearbits.subframe.render.PlotGroup;

/**
 * Example benchmark
 * 
 * @author Fabian Prasser
 */
public class BoxAndWhiskerEvaluation {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
    public static void main(String[] args) throws IOException, ParseException {
        
        CSVFile file = new CSVFile(new File("src/example/example2/input.csv"));

        List<PlotGroup> groups = new ArrayList<PlotGroup>();
        groups.add(getGroup1(file));

        LaTeXParams params = new LaTeXParams();
        params.pageFormat = PageFormat.LETTER;
        params.margin = 1;
        LaTeX.plot(groups, "src/example/example2/output", params);
    }
    
    /**
     * Build the first plot group
     * @param file
     * @return
     * @throws ParseException
     */
    private static PlotGroup getGroup1(CSVFile file) throws ParseException{

        List<Plot<?>> plots = new ArrayList<Plot<?>>();
        
        SeriesBoxAndWhisker series = new SeriesBoxAndWhisker(file,  new Field("A", Analyzer.VALUE),
                                                                    new Field("B", Analyzer.VALUE),
                                                                    0.05d,
                                                                    100);
        
        plots.add(new PlotBoxAndWhisker("Bucketized A vs. V", 
                                         new Labels("A", "B"),
                                         series));
        
        GnuPlotParams params = new GnuPlotParams();
        params.rotateXTicks = -90;
        params.keypos = KeyPos.TOP_RIGHT;
        params.size = 0.6d;
        params.minY = 0d;
        params.maxY = 1d;
        return new PlotGroup("Bucketized A vs. V", plots, params, 1.0d);
    }
}
