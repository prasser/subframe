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
package de.linearbits.subframe.graph;

/**
 * Abstract base class for plots
 * 
 * @author Fabian Prasser
 *
 * @param <T>
 */
public abstract class Plot<T extends Series<?>> {

	/** Title*/
    private String title;
    /** Underlying series*/
    private T      series;
    /** Labels*/
    private Labels labels;

    /**
     * Constructs a new instance
     * @param title
     * @param labels
     * @param series
     */
    public Plot(String title, Labels labels, T series) {
        this.title = title;
        this.series = series;
        this.labels = labels;
    }

    /**
     * Returns the labels
     * @return
     */
    public Labels getLabels() {
        return labels;
    }

    /**
     * Returns the series as a Series2D. Null if its not an instance of Series2D
     * @return
     */
    public Series2D getSeries2D() {
        if (series instanceof Series2D) {
            return (Series2D) series;
        } else {
            return null;
        }
    }

    /**
     * Returns the series as a Series3D. Null if its not an instance of Series3D
     * @return
     */
    public Series3D getSeries3D() {
        if (series instanceof Series3D) {
            return (Series3D) series;
        } else {
            return null;
        }
    }

    /**
     * Returns the title
     * @return
     */
    public String getTitle() {
        return title;
    }
}
