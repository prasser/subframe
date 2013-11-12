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
import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import de.linearbits.subframe.Benchmark;
import de.linearbits.subframe.analyzer.buffered.BufferedArithmetricMeanAnalyzer;
import de.linearbits.subframe.analyzer.buffered.BufferedStandardDeviationAnalyzer;

/**
 * Class with JUnit tests
 * @author Fabian Prasser
 */
public class TestBenchmark1 extends TestBase{

    private static final Benchmark benchmark = new Benchmark();
    private static final int       INIT      = benchmark.addMeasure("Init");
    private static final int       SORT      = benchmark.addMeasure("Sort");
    private static final int       SUM       = benchmark.addMeasure("Sum");

    @Test
    public void testBenchmark1(){
        
        try {
            benchmark.addAnalyzer(INIT, new BufferedArithmetricMeanAnalyzer());
            benchmark.addAnalyzer(INIT, new BufferedStandardDeviationAnalyzer());
            benchmark.addAnalyzer(SORT, new BufferedArithmetricMeanAnalyzer());
            benchmark.addAnalyzer(SORT, new BufferedStandardDeviationAnalyzer());
            benchmark.addAnalyzer(SUM, new BufferedArithmetricMeanAnalyzer());
            benchmark.addAnalyzer(SUM, new BufferedStandardDeviationAnalyzer());

            benchmark.addRun("Warmup");
            run(1);
            
            benchmark.addRun("Test");
            run(10);
            
            String file = "src/test/temp1.csv";
            benchmark.getResults().write(new File(file));
            checkAndDelete(file);
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private int run(int repetitions) {

        int k = 0;
        for (int i=0; i<repetitions; i++){
            
            benchmark.startTimer(INIT);
            Random r = new Random();
            int[] data = new int[1000000];
            for (int j=0; j<data.length; j++){
                data[j] = r.nextInt();
            }
            benchmark.addStopTimer(INIT);
            
            benchmark.startTimer(SORT);
            Arrays.sort(data);
            benchmark.addStopTimer(SORT);
            
            benchmark.startTimer(SUM);
            for (int j=0; j<data.length; j++){
                k+=data[j];
            }
            benchmark.addStopTimer(SUM);
        }
        return k;
    }
}
