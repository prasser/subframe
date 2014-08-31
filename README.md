Java Benchmarking Library
====

Introduction
------
SUBFRAME is a Java framework for performing microbenchmarks, analyzing the results and plotting them graphically. 
It supports the following features: 

1. Measuring several parameters, including: wall-clock time, system time, user time and different methods for measuring memory consumption in
single- and multithreaded environments

2. Analyzing the results, including: minimum, maximum, sums, geometric and arithmetic mean, standard deviation, percentiles

3. Writing data to and reading data from csv-files

4. Extracting and transforming data series from files

5. Clustering and plotting results: line charts, histograms, stacked histograms, heatmaps

For rendering, the library uses Gnuplot and LaTeX, which need to be installed separately.

Examples
------

* First, we perform a benchmark. In the example, we measure the wall-clock time required to
sort an array with up to one million integers with merge sort: 

```Java
  
// Create benchmark
Benchmark benchmark = new Benchmark(new String[] { "Size", "Method" });
  
// Register measurements
int time = benchmark.addMeasure("Time");
  
// Register analyzers
benchmark.addAnalyzer(time, new BufferedArithmetricMeanAnalyzer());
benchmark.addAnalyzer(time, new BufferedStandardDeviationAnalyzer());
  
// Run benchmark for different sizes
int repetitions = 5;
for (int size=100000; size<=1000000; size+=100000) {
  
  // Prepare benchmark
  int index = 0;
  int[][] arrays = new int[repetitions + 1];
  for (int i=0; i<=arrays.length; i++) arrays[i] = getRandomArray(size);
  
  // Warmup
  Sorting.mergeSort(arrays[index++], 0, size);
  
  // Run benchmark
  benchmark.addRun(size, "MergeSort");
  for (int i = 0; i < REPETITIONS; i++) {
     benchmark.startTimer(time);
     Sorting.mergeSort(arrays[index++], 0, size);
     benchmark.addStopTimer(time);
  }
}
  
// Store results
benchmark.getResults().write(new File("sort.csv"));
  
```

* This results in a csv file looking like this: 

<table>
<tr><td>		</td><td>			</td><td>Time			</td><td>Time				</td></tr>
<tr><td>Size	</td><td>Method		</td><td>Arithmetic Mean</td><td>Standard Deviation	</td></tr>
<tr><td>100000	</td><td>MergeSort	</td><td>16871734.40	</td><td>1996647.6291153480	</td></tr>
<tr><td>200000	</td><td>MergeSort	</td><td>32456968.25	</td><td>3176739.1862106794	</td></tr>
<tr><td>300000	</td><td>MergeSort	</td><td>49534981.35	</td><td>1265261.8748274317	</td></tr>
<tr><td>400000	</td><td>MergeSort	</td><td>72037158.90	</td><td>13137915.799871740 </td></tr>
</table>

* Second, we plot the results. In this example we use a lines chart:

```Java
  
// Open the file
CSVFile file = new CSVFile(new File("sort.csv"));

// Select rows for the series  
Selector<String[]> selector = file.getSelectorBuilder()
                                  .field("Method").equals("MergeSort")
                                  .build();
        
// Build the series
Series3D series = new Series3D(file, selector, 
                               new Field("Size"),
                               new Field("Method"),
                               new Field("Time", Analyzer.ARITHMETIC_MEAN));
                           
// Create a plot            
Plot<?> plot = new PlotLinesClustered("Sorting Arrays", 
                                      new Labels("Size", "Execution time [ns]"),
                                      series);

// Render the plot
GnuPlotParams params = new GnuPlotParams();
params.xticsrotate = -90;
params.keypos = KeyPos.TOP_LEFT;
params.size = 0.6d;
GnuPlot.plot(plot, params, "sort");
  
```

* The following image shows the results for different sorting methods:

![Image](https://raw.github.com/prasser/subframe/master/doc/sorting1.png)

* We can also plot a histogram with error bars for individual methods:

```Java
  
// Open the file
CSVFile file = new CSVFile(new File("sort.csv"));

// Select rows for the series  
Selector<String[]> selector = file.getSelectorBuilder()
                                  .field("Method").equals("MergeSort")
                                  .build();
        
// Build the series
Series3D series = new Series3D(file, selector, 
                               new Field("Size"),
                               new Field("Time", Analyzer.ARITHMETIC_MEAN),
                               new Field("Time", Analyzer.STANDARD_DEVIATION));
                           
// Create a plot            
Plot<?> plot = new PlotHistogram("Sorting arrays with merge sort", 
                                 new Labels("Size", "Execution time [ns]"),
                                 series);

// Render the plot
GnuPlotParams params = new GnuPlotParams();
params.xticsrotate = -90;
params.keypos = KeyPos.TOP_LEFT;
params.size = 0.6d;
GnuPlot.plot(plot, params, "sort");
  
```

* The following image shows the result:

![Image](https://raw.github.com/prasser/subframe/master/doc/sorting2.png)

* Further methods exist, e.g., for measuring memory consumption:

```Java
benchmark.startUsedBytesGC(SIZE_JVM);
byte[] array = new byte[size];
for (int i=0; i<size; i++) array[i] = (byte)i;
benchmark.addStopUsedBytesGC(SIZE_JVM);
```

* This results in plots similar to this:

![Image](https://raw.github.com/prasser/subframe/master/doc/size1.png)

Documentation
------
More examples are available in the [repository](https://github.com/prasser/subframe/tree/master/src/example).

To see SUBFRAME in action you might also want to have a look at [anonbench](https://github.com/arx-deidentifier/anonbench).

Javadoc documentation is available [here](https://rawgithub.com/prasser/subframe/master/doc/index.html).

Downloads
------
[Library (Version 0.1)](https://raw.github.com/prasser/subframe/master/jars/subframe-0.2-lib.jar)

[API documentation (Version 0.1)](https://raw.github.com/prasser/subframe/master/jars/subframe-0.2-doc.jar)

[Source (Version 0.1)](https://raw.github.com/prasser/subframe/master/jars/subframe-0.2-src.jar)