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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.linearbits.subframe.graph.Plot;

/**
 * Renders groups of plots into a common PDF file with LaTeX
 * 
 * @author Fabian Prasser
 */
public class LaTeX {

    /**
     * Render the given list of plots
     * 
     * @param groups
     * @param filename
     * @throws IOException
     */
    public static void plot(List<PlotGroup> groups, String filename) throws IOException {
        plot(groups, filename, false);
    }

    /**
     * Render the given list of plots. Allows keeping the source files.
     * 
     * @param groups
     * @param filename
     * @param keepFiles
     * @throws IOException
     */
    public static void plot(List<PlotGroup> groups, String filename, boolean keepFiles) throws IOException {
        plot(groups, filename, new LaTeXParams(), keepFiles);
    }
    
    /**
     * Render the given list of plots. Allows keeping the source files.
     * 
     * @param groups
     * @param filename
     * @param keepFiles
     * @throws IOException
     */
    public static void plot(List<PlotGroup> groups, String filename, LaTeXParams params) throws IOException {
        plot(groups, filename, params, false);
    }
    
    /**
     * Render the given list of plots. Allows keeping the source files.
     * 
     * @param groups
     * @param filename
     * @param params
     * @param keepFiles
     * @throws IOException
     */
    public static void plot(List<PlotGroup> groups, String filename, LaTeXParams params, boolean keepFiles) throws IOException {

        String outputFolder = new File(new File(".").getAbsolutePath() + "/" +
                                       new File("./" + filename + ".tex").getParent()).getAbsolutePath();
        
        Map<Plot<?>, String> filenames = new HashMap<Plot<?>, String>();

        try {
            plot(groups, filename, keepFiles, params, filenames, outputFolder);
        } catch (IOException e) {
            if (!keepFiles) {
                deleteFile(filename + ".tex");
                for (String name : filenames.values()) {
                    deleteFile(name + ".pdf");
                }
                deleteFile(outputFolder + "/img/");
            }
            deleteFile(filename + ".log");
            deleteFile(filename + ".aux");
            deleteFile(outputFolder + "/texput.log");
            throw (e);
        }

        // delete temp files
        if (!keepFiles) {
            deleteFile(filename + ".tex");
            for (String name : filenames.values()) {
                deleteFile(name + ".pdf");
            }
            deleteFile(outputFolder + "/img/");
        }
        deleteFile(filename + ".log");
        deleteFile(filename + ".aux");
        deleteFile(outputFolder + "/texput.log");

    }

    /**
     * Executes pdflatex
     * 
     * @param groups
     * @param filename
     * @param keepFiles
     * @param params
     * @param filenames
     * @param outputFolder
     * @throws IOException
     */
    private static void plot(List<PlotGroup> groups,
                             String filename,
                             boolean keepFiles,
                             LaTeXParams params,
                             Map<Plot<?>, String> filenames,
                             String outputFolder) throws IOException {

        new File(outputFolder + "/img/").mkdir();

        int index = 0;
        for (int i = 0; i < groups.size(); i++) {
            PlotGroup group = groups.get(i);
            for (Plot<?> plot : group.getPlots()) {
                String name = new File(outputFolder + "/img/" + "image_" + (index++)).getAbsolutePath();
                GnuPlot.plot(plot, group.getParams(), name, keepFiles);
                filenames.put(plot, name);
            }
        }

        BufferedWriter w = new BufferedWriter(new FileWriter(new File(filename + ".tex")));

        w.write("\\documentclass{article}\n");
        w.write("\\usepackage{graphicx}\n");
        w.write("\\pagestyle{empty}\n");
        w.write("\\usepackage{geometry}\n");
        w.write("\\geometry{");
        switch (params.pageFormat) {
        case A4:
            w.write("a4paper");
            break;
        case LETTER:
            w.write("letterpaper");
            break;
        default:
            w.close();
            throw new IllegalArgumentException("Unknown page format: " + params.pageFormat);
        }
        
        w.write(", margin=");
        w.write(String.valueOf(params.margin));
        w.write("in}\n");
        w.write("\\begin{document}\n");

        for (int i = 0; i < groups.size(); i++) {
            PlotGroup group = groups.get(i);
            w.write("\t\\begin{figure*}[!htbp]\n");
            w.write("\t\t\\centering\n");
            for (Plot<?> plot : group.getPlots()) {
                w.write("\t\t\\includegraphics[width=");
                w.write(String.valueOf(group.getSize() - 0.01d));
                w.write("\\textwidth]");
                w.write("{");
                String imgFilename = filenames.get(plot);
                if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
                    imgFilename = imgFilename.replaceAll("\\\\", "/");
                }
                w.write(imgFilename);
                w.write(".pdf");
                w.write("}\n");

            }
            w.write("\t\t\\caption{");
            w.write(group.getCaption());
            w.write("}\n");
            w.write("\t\\end{figure*}\n");
            
            if (i % 18 == 0) {
                w.write("\t\\clearpage\n");
            }

        }

        w.write("\\end{document}\n");

        w.flush();
        w.close();

        ProcessBuilder b = new ProcessBuilder();
        Process p;

        if (new File(filename + ".tex").exists()) {
            b.command("pdflatex", "-interaction=errorstopmode", "-quiet", "-output-directory=" + outputFolder, filename + ".tex");
            p = b.start();
            StreamReader output = new StreamReader(p.getInputStream());
            StreamReader error = new StreamReader(p.getErrorStream());
            new Thread(output).start();
            new Thread(error).start();
            try {
                p.waitFor();
            } catch (final InterruptedException e) {
                throw new IOException(e);
            }

            if (p.exitValue() != 0) {
                throw new IOException("Error executing pdflatex: " + error.getString() + System.lineSeparator() + output.getString());
            }
        }
    }

    /**
     * Deletes a file
     * 
     * @param path
     */
    private static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }
}
