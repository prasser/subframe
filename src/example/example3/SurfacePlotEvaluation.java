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
package example3;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import de.linearbits.subframe.analyzer.Analyzer;
import de.linearbits.subframe.graph.Field;
import de.linearbits.subframe.graph.Labels;
import de.linearbits.subframe.graph.Plot;
import de.linearbits.subframe.graph.Plot3D;
import de.linearbits.subframe.graph.Series3D;
import de.linearbits.subframe.io.CSVFile;
import de.linearbits.subframe.render.GnuPlotParams;
import de.linearbits.subframe.render.LaTeX;
import de.linearbits.subframe.render.LaTeXParams;
import de.linearbits.subframe.render.LaTeXParams.PageFormat;
import de.linearbits.subframe.render.PlotGroup;

/**
 * Example benchmark
 * 
 * @author Fabian Prasser
 */
public class SurfacePlotEvaluation {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
    public static void main(String[] args) throws IOException, ParseException {
        
        CSVFile file = new CSVFile(new File("src/example/example3/input.csv"));

        List<PlotGroup> groups = new ArrayList<PlotGroup>();
        groups.add(getGroup1(file));

        LaTeXParams params = new LaTeXParams();
        params.pageFormat = PageFormat.LETTER;
        params.margin = 0.5;
        LaTeX.plot(groups, "src/example/example3/output", params, false);
    }
    
    /**
     * Build the first plot group
     * @param file
     * @return
     * @throws ParseException
     */
    private static PlotGroup getGroup1(CSVFile file) throws ParseException{

        List<Plot<?>> plots = new ArrayList<Plot<?>>();
        
        Series3D series = new Series3D(file,  new Field("A", Analyzer.VALUE),
                                              new Field("B", Analyzer.VALUE),
                                              new Field("C", Analyzer.VALUE));
        
        plots.add(new Plot3D("Example 3D surface plot", new Labels("X", "Y", "Z"), series));
        
        GnuPlotParams params = new GnuPlotParams();
        params.size = 0.6d;
        params.colorize = true;
        params.grid3dResolution = 32;
        params.grid3dInterpolation = 1;
        params.xyPlaneAt = 0;
        params.xTics = 0.05;
        params.yTics = 0.01;
        params.zTics = 0.2;
        return new PlotGroup("Example 3D surface plot", plots, params, 1.0d);
    }
}
