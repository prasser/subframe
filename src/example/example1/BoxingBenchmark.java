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

import de.linearbits.subframe.Benchmark;
import de.linearbits.subframe.analyzer.ValueBuffer;

/**
 * Example benchmark
 * @author Fabian Prasser
 */
public class BoxingBenchmark {

    private static final Benchmark BENCHMARK   = new Benchmark(new String[] { "Size", "Type"});
    private static final int       SIZE_MX     = BENCHMARK.addMeasure("SizeMX");
    private static final int       SIZE_JVM    = BENCHMARK.addMeasure("SizeJVM");
    private static final int       SIZE_INSTR  = BENCHMARK.addMeasure("SizeInstr");

    static {
        BENCHMARK.addAnalyzer(SIZE_MX, new ValueBuffer());
        BENCHMARK.addAnalyzer(SIZE_JVM, new ValueBuffer());
        BENCHMARK.addAnalyzer(SIZE_INSTR, new ValueBuffer());
    }

    /**
     * Run the benchmark
     * @param size
     */
    private static void run(int size) {

        BENCHMARK.addRun(size, "byte[]");
        BENCHMARK.startUsedBytesGC(SIZE_JVM);
        BENCHMARK.startUsedBytesMX(SIZE_MX);
        byte[] array = new byte[size];
        for (int i=0; i<size; i++) array[i] = (byte)i;
        BENCHMARK.addStopUsedBytesGC(SIZE_JVM);
        BENCHMARK.addStopUsedBytesMX(SIZE_MX);
        BENCHMARK.addValue(SIZE_INSTR, BENCHMARK.getMeasures().getSize(array));
        array = null;
        
        BENCHMARK.addRun(size, "Byte[]");
        BENCHMARK.startUsedBytesGC(SIZE_JVM);
        BENCHMARK.startUsedBytesMX(SIZE_MX);
        Byte[] array2 = new Byte[size];
        for (int i=0; i<size; i++) array2[i] = (byte)i;
        BENCHMARK.addStopUsedBytesGC(SIZE_JVM);
        BENCHMARK.addStopUsedBytesMX(SIZE_MX);
        BENCHMARK.addValue(SIZE_INSTR, BENCHMARK.getMeasures().getSize(array2));
        array2 = null;
        
        BENCHMARK.addRun(size, "int[]");
        BENCHMARK.startUsedBytesGC(SIZE_JVM);
        BENCHMARK.startUsedBytesMX(SIZE_MX);
        int[] array3 = new int[size];
        for (int i=0; i<size; i++) array3[i] = (int)i;
        BENCHMARK.addStopUsedBytesGC(SIZE_JVM);
        BENCHMARK.addStopUsedBytesMX(SIZE_MX);
        BENCHMARK.addValue(SIZE_INSTR, BENCHMARK.getMeasures().getSize(array3));
        array3 = null;

        BENCHMARK.addRun(size, "Integer[]");
        BENCHMARK.startUsedBytesGC(SIZE_JVM);
        BENCHMARK.startUsedBytesMX(SIZE_MX);
        Integer[] array4 = new Integer[size];
        for (int i=0; i<size; i++) array4[i] = (int)i;
        BENCHMARK.addStopUsedBytesGC(SIZE_JVM);
        BENCHMARK.addStopUsedBytesMX(SIZE_MX);
        BENCHMARK.addValue(SIZE_INSTR, BENCHMARK.getMeasures().getSize(array4));
        array4 = null;

        BENCHMARK.addRun(size, "long[]");
        BENCHMARK.startUsedBytesGC(SIZE_JVM);
        BENCHMARK.startUsedBytesMX(SIZE_MX);
        long[] array5 = new long[size];
        for (int i=0; i<size; i++) array5[i] = (long)i;
        BENCHMARK.addStopUsedBytesGC(SIZE_JVM);
        BENCHMARK.addStopUsedBytesMX(SIZE_MX);
        BENCHMARK.addValue(SIZE_INSTR, BENCHMARK.getMeasures().getSize(array5));
        array5 = null;

        BENCHMARK.addRun(size, "Long[]");
        BENCHMARK.startUsedBytesGC(SIZE_JVM);
        BENCHMARK.startUsedBytesMX(SIZE_MX);
        Long[] array6 = new Long[size];
        for (int i=0; i<size; i++) array6[i] = (long)i;
        BENCHMARK.addStopUsedBytesGC(SIZE_JVM);
        BENCHMARK.addStopUsedBytesMX(SIZE_MX);
        BENCHMARK.addValue(SIZE_INSTR, BENCHMARK.getMeasures().getSize(array6));
        array6 = null;
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
        
        BENCHMARK.getResults().write(new File("src/example/example1/boxing.csv"));
    }
}
