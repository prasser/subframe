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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.linearbits.subframe.graph.Plot;
import de.linearbits.subframe.graph.PlotBoxAndWhisker;
import de.linearbits.subframe.graph.PlotHistogram;
import de.linearbits.subframe.graph.PlotHistogramClustered;
import de.linearbits.subframe.graph.PlotHistogramStacked;
import de.linearbits.subframe.graph.PlotLines;
import de.linearbits.subframe.graph.PlotLinesClustered;
import de.linearbits.subframe.render.GnuPlotParams.KeyPos;

/**
 * Static class for rendering plots with GnuPlot
 * 
 * @author Prasser, Kohlmayer
 */
public abstract class GnuPlot<T extends Plot<?>> {
    
    /** Counter for random plot names */
    private static int counter = 0;
    
    /**
     * Renders the given plot
     * @param plot
     * @throws IOException
     */
    public static void plot(Plot<?> plot) throws IOException {
        plot(plot, "plot" + (counter++), false);
    }
    
    /**
     * Renders the given plot, taking into account the given parameters
     * @param plot
     * @param params
     * @throws IOException
     */
    public static void plot(Plot<?> plot, GnuPlotParams params) throws IOException {
        plot(plot, params, "plot" + (counter++), false);
    }
    
    /**
     * Renders the given plot, taking into account the given parameters,
     * writing to the given file
     * @param plot
     * @param params
     * @param filename
     * @throws IOException
     */
    public static void plot(Plot<?> plot, GnuPlotParams params, String filename) throws IOException {
        plot(plot, params, filename, false);
    }
    
    /**
     * Renders the given plot, taking into account the given parameters,
     * writing to the given file. Allows keeping the GnuPlot sources.
     * @param plot
     * @param params
     * @param filename
     * @param keepSources
     * @throws IOException
     */
    public static void
            plot(Plot<?> plot, GnuPlotParams params, String filename, boolean keepSources) throws IOException {
        
        // Create gnuplot
        GnuPlot<?> gPlot = null;
        if (plot instanceof PlotHistogram) {
            gPlot = new GnuPlotHistogram((PlotHistogram) plot, params);
        } else if (plot instanceof PlotLines) {
            gPlot = new GnuPlotLines((PlotLines) plot, params);
        } else if (plot instanceof PlotHistogramClustered) {
            gPlot = new GnuPlotHistogramClustered((PlotHistogramClustered) plot, params);
        } else if (plot instanceof PlotLinesClustered) {
            gPlot = new GnuPlotLinesClustered((PlotLinesClustered) plot, params);
        } else if (plot instanceof PlotHistogramStacked) {
            gPlot = new GnuPlotHistogramStacked((PlotHistogramStacked) plot, params);
        } else if (plot instanceof PlotBoxAndWhisker) {
            gPlot = new GnuPlotBoxAndWhisker((PlotBoxAndWhisker) plot, params);
        } else {
            throw new RuntimeException("Invalid type of plot");
        }
        
        // Write gnuplot file
        String gpFilename = "." + System.getProperty("file.separator") + new File(filename).getName();
        if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
            gpFilename = gpFilename.replaceAll("\\\\", "\\\\\\\\");
        }
        File file = new File(filename + ".gp");
        FileWriter writer = new FileWriter(file);
        writer.write(gPlot.getSource(gpFilename));
        writer.close();
        
        // Write data file
        file = new File(filename + ".dat");
        writer = new FileWriter(file);
        writer.write(gPlot.getData());
        writer.close();
        
        // Execute
        try {
            plot(filename);
        } catch (IOException e) {
            if (!keepSources) {
                new File(filename + ".gp").delete();
                new File(filename + ".dat").delete();
            }
            new File(filename + ".eps").delete();
            new File(filename + ".pdf").delete();
            throw (e);
        }
        
        // Delete files
        if (!keepSources) {
            new File(filename + ".gp").delete();
            new File(filename + ".dat").delete();
            new File(filename + ".eps").delete();
        }
    }
    
    /**
     * Renders the given plot, writing to the given file
     * @param plot
     * @param filename
     * @throws IOException
     */
    public static void plot(Plot<?> plot, String filename) throws IOException {
        plot(plot, filename, false);
    }
    
    /**
     * Renders the given plot, writing to the given file. Allows keeping the GnuPlot sources.
     * @param plot
     * @param filename
     * @param keepSources
     * @throws IOException
     */
    public static void plot(Plot<?> plot, String filename, boolean keepSources) throws IOException {
        plot(plot, new GnuPlotParams(), filename, keepSources);
    }
    
    /**
     * Runs gnuplot.
     * 
     * @param file the file
     * @param preserveGnuplotFiles the preserve gnuplot files
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void plot(final String file) throws IOException {
        
        // Run gnuplot
        ProcessBuilder b = new ProcessBuilder();
        b.directory(new File(file).getParentFile());
        b.command("gnuplot", file + ".gp");
        Process p = b.start();
        StreamReader in = new StreamReader(p.getInputStream());
        StreamReader error = new StreamReader(p.getErrorStream());
        new Thread(in).start();
        new Thread(error).start();
        try {
            p.waitFor();
        } catch (final InterruptedException e) {
            throw new IOException(e);
        }
        
        // Check messages
        File eps = new File(file + ".eps");
        String errorMsg = error.getString();
        if (p.exitValue() != 0 || (errorMsg != null && !errorMsg.equals("") && !errorMsg.startsWith("Warning:"))) {
            if (eps.exists()) eps.delete();
            throw new IOException("Error executing gnuplot. Please check the provided series. Error: " + errorMsg);
        }
        
        // Check file
        if (!eps.exists() || eps.length() == 0) {
            if (eps.exists()) eps.delete();
            throw new IOException("Error executing gnuplot. Please check the provided series. Error: " + errorMsg);
        }
        
        // Run ps2pdf
        if (new File(file + ".eps").exists()) {
            
            b = new ProcessBuilder();
            if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
                b.command("ps2pdf",
                          "-dPDFSETTINGS#/prepress",
                          "-dEmbedAllFonts#true",
                          "-dUseFlateCompression#true",
                          file + ".eps",
                          file + ".pdf");
            } else {
                b.command("pstopdf", file + ".eps", file + ".pdf");
            }
            
            p = b.start();
            in = new StreamReader(p.getInputStream());
            error = new StreamReader(p.getErrorStream());
            new Thread(in).start();
            new Thread(error).start();
            try {
                p.waitFor();
            } catch (final InterruptedException e) {
                throw new IOException(e);
            }
            
            if (p.exitValue() != 0) {
                new File(file + ".eps").delete();
                throw new IOException("Error running psd2pdf: " + error.getString());
            }
        }
        
        // Run pdfcrop
        if (new File(file + ".pdf").exists()) {
            
            b = new ProcessBuilder();
            b.command("pdfcrop", file + ".pdf", file + ".pdf");
            
            p = b.start();
            
            in = new StreamReader(p.getInputStream());
            error = new StreamReader(p.getErrorStream());
            new Thread(in).start();
            new Thread(error).start();
            try {
                p.waitFor();
            } catch (final InterruptedException e) {
                throw new IOException(e);
            }
            
            if (p.exitValue() != 0) {
                new File(file + ".pdf").delete();
                throw new IOException("Error running pdfcrop: " + error.getString());
            }
        }
    }
    
    /** The plot to render */
    protected T             plot;
    /** The parameters to use */
    protected GnuPlotParams params;
    
    /**
     * Constructs a new instance
     * @param plot
     * @param params
     */
    protected GnuPlot(T plot, GnuPlotParams params) {
        this.plot = plot;
        this.params = params;
    }
    
    /** Returns the data */
    protected abstract String getData();
    
    /** Returns the GnuPlot source code */
    protected abstract String getSource(String filename);
    
    /**
     * Returns a list of generic gnuplot commands, which are similar for all plots
     * @param filename
     * @param plot
     * @return
     */
    protected List<String> getGenericCommands(String filename, Plot<?> plot) {
        
        List<String> gpCommands = new ArrayList<String>();
        
        gpCommands.add("set terminal postscript eps enhanced " +
                       (params.colorize ? "color" : "monochrome") +
                       " size " + params.width + "," + params.height +
                       (params.font != null ? " font '" + params.font + "'" : ""));
        gpCommands.add("set output \"" + filename + ".eps\"");
        
        if (params.size != null) {
            gpCommands.add("set size " + params.size);
        }
        
        if (params.ratio != null) {
            gpCommands.add("set size ratio " + params.ratio);
        }
        
        gpCommands.add("set offsets " + params.offsetLeft + " , " + params.offsetRight + ", " + params.offsetTop + ", " + params.offsetBottom + " ");
        
        gpCommands.add("set title \"" + plot.getTitle() + "\"");
        gpCommands.add("set xlabel \"" + plot.getLabels().x + "\"" + params.offsetXlabel);
        gpCommands.add("set ylabel \"" + plot.getLabels().y + "\"" + params.offsetYlabel);
        
        if (params.keypos == KeyPos.NONE) {
            gpCommands.add("unset key");
        } else {
            gpCommands.add("set key " + params.keypos.toString());
        }
        
        if (params.minY != null && params.maxY != null) {
            gpCommands.add("set yrange[" + params.minY + ":" + params.maxY + "]");
        }
        if (params.minX != null && params.maxX != null) {
            gpCommands.add("set xrange[" + params.minX + ":" + params.maxX + "]");
        }
        if (params.minZ != null && params.maxZ != null) {
            gpCommands.add("set zrange[" + params.minZ + ":" + params.maxZ + "]");
        }
        
        if (params.minY != null && params.maxY == null) {
            gpCommands.add("set yrange[" + params.minY + ":]");
        }
        if (params.minX != null && params.maxX == null) {
            gpCommands.add("set xrange[" + params.minX + ":]");
        }
        if (params.minZ != null && params.maxZ == null) {
            gpCommands.add("set zrange[" + params.minZ + ":]");
        }
        
        if (params.logX) {
            gpCommands.add("set logscale x");
        }
        if (params.logY) {
            gpCommands.add("set logscale y");
        }
        if (params.logZ) {
            gpCommands.add("set logscale z");
        }
        
        if (params.grid) {
            gpCommands.add("set grid");
        }
        
        if (params.rotateXTicks != null) {
            gpCommands.add("set xtics rotate by " + params.rotateXTicks);
        }
        gpCommands.add("set xtic scale 0");
        
        return gpCommands;
    }
}
