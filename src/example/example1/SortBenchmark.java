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
import java.util.Arrays;
import java.util.Random;

import cern.colt.Sorting;
import cern.colt.function.IntComparator;
import de.linearbits.subframe.Benchmark;
import de.linearbits.subframe.analyzer.buffered.BufferedArithmeticMeanAnalyzer;
import de.linearbits.subframe.analyzer.buffered.BufferedStandardDeviationAnalyzer;

/**
 * Example benchmark
 * 
 * @author Fabian Prasser
 */
public class SortBenchmark {

    private static final Benchmark BENCHMARK   = new Benchmark(new String[] { "Size", "Method" });
    private static final int       TIME        = BENCHMARK.addMeasure("Time");
    private static final int       REPETITIONS = 20;

    static {
        BENCHMARK.addAnalyzer(TIME, new BufferedArithmeticMeanAnalyzer(REPETITIONS));
        BENCHMARK.addAnalyzer(TIME, new BufferedStandardDeviationAnalyzer(REPETITIONS));
    }

    /**
     * Run the benchmark
     * @param size
     */
    private static void run(int size) {

        // Prepare
        int[][] arrays = new int[REPETITIONS*3+3][];
        for (int j=0; j<arrays.length; j++){
            arrays[j] = new int[size];
        }
        Random random = new Random();
        for (int j=0; j<arrays.length; j++){
            for (int i=0; i<size; i++){
                arrays[j][i] = random.nextInt();
            }
        }
        
        int index = 0;

        // Run colt merge sort
        BENCHMARK.addRun(size, "ColtMergeSort");
        Sorting.mergeSort(arrays[index++], 0, size); // Warm up
        for (int i = 0; i < REPETITIONS; i++) {
            BENCHMARK.startTimer(TIME);
            Sorting.mergeSort(arrays[index++], 0, size);
            BENCHMARK.addStopTimer(TIME);
        }

        // Run colt quick sort
        IntComparator c = new IntComparator(){
            public int compare(int arg0, int arg1) {
                int result = arg0 < arg1 ? -1 : 0;
                result = arg0 > arg1 ? +1 : 0;
                return result;
            }
        };
        BENCHMARK.addRun(size, "ColtQuickSort");
        Sorting.quickSort(arrays[index++], 0, size, c); // Warm up
        for (int i = 0; i < REPETITIONS; i++) {
            BENCHMARK.startTimer(TIME);
            Sorting.quickSort(arrays[index++], 0, size, c);
            BENCHMARK.addStopTimer(TIME);
        }

        // Run java quick sort
        BENCHMARK.addRun(size, "JavaQuickSort");
        Sorting.mergeSort(arrays[index++], 0, size); // Warm up
        for (int i = 0; i < REPETITIONS; i++) {
            BENCHMARK.startTimer(TIME);
            Arrays.sort(arrays[index++]);
            BENCHMARK.addStopTimer(TIME);
        }

    }

    /**
     * Main
     * 
     * @param args
     */
    public static void main(String[] args) throws Exception {
        
        int[] sizes = new int[20];
        for (int i=1; i<21; i++){
            sizes[i-1] = 100000 * i;
        }
        
        for (int size : sizes){
            run(size);
        }
        
        BENCHMARK.getResults().write(new File("src/example/example1/sort.csv"));
    }
}
