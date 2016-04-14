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

import java.io.File;
import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import de.linearbits.subframe.Benchmark;
import de.linearbits.subframe.analyzer.ValueBuffer;

/**
 * Class with JUnit tests
 * @author Fabian Prasser
 */
public class TestBenchmark2 extends TestBase {

    private static final Benchmark benchmark = new Benchmark("Run", "Size", "Phase");
    private static final int       EXECTIME  = benchmark.addMeasure("Execution time");

    @Test
    public void testBenchmark(){

        try {
            benchmark.addAnalyzer(EXECTIME, new ValueBuffer());
            run("Warmup", 1,1000000);
            
            run("Test", 10, 100);
            run("Test", 10, 1000);
            run("Test", 10, 10000);
            run("Test", 10, 50000);
            run("Test", 10, 100000);
            run("Test", 10, 250000);
            run("Test", 10, 500000);
            run("Test", 10, 1000000);
            
            String file = "src/test/temp2.csv";
            benchmark.getResults().write(new File(file));
            checkAndDelete(file);
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private int run(String label, int repetitions, int size) {
        int k = 0;
        for (int i=0; i<repetitions; i++){
            

            benchmark.addRun(label, String.valueOf(size), "Init");
            benchmark.startTimer(EXECTIME);
            Random r = new Random();
            int[] data = new int[size];
            for (int j=0; j<data.length; j++){
                data[j] = r.nextInt();
            }
            benchmark.addStopTimer(EXECTIME);
            
            benchmark.addRun(label, String.valueOf(size), "Sort");
            benchmark.startTimer(EXECTIME);
            Arrays.sort(data);
            benchmark.addStopTimer(EXECTIME);
            
            benchmark.addRun(label, String.valueOf(size), "Sum");
            benchmark.startTimer(EXECTIME);
            for (int j=0; j<data.length; j++){
                k+=data[j];
            }
            benchmark.addStopTimer(EXECTIME);
        }
        return k;
    }
}
