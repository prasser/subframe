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
package de.linearbits.subframe;

import java.util.concurrent.atomic.AtomicLong;

import de.linearbits.subframe.Measures.Visibility;
import de.linearbits.subframe.io.CSVFile;

/**
 * A synchronized instance of the benchmark class. Uses spin locks.
 * @author Fabian Prasser
 */
public class BenchmarkSynchronized extends Benchmark {

    /**
     * A spin lock
     * @author Fabian Prasser
     */
    private static class SpinLock {

        /** Mutex*/
        private AtomicLong mutex = new AtomicLong(-1);

        /** Take the lock*/
        public void take() {
            long id = Thread.currentThread().getId();
            while (!mutex.compareAndSet(-1, id)) { /* Spin */}
        }

        /** Release the lock*/
        public void release() {
            mutex.set(-1);
        }
    }

    /** The benchmark that calls are delegated to*/
    private final Benchmark  benchmark;
    /** One lock for each measure*/
    private final SpinLock[] locks;

    /**
     * Creates a new instance
     * @param benchmark
     */
    protected BenchmarkSynchronized(Benchmark benchmark) {
        this.benchmark = benchmark;
        this.locks = new SpinLock[benchmark.measures.time.length];
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new SpinLock();
        }
    }

    @Override
    public void addCpuTime(int measure) {
        locks[measure].take();
        benchmark.addCpuTime(measure);
        locks[measure].release();
    }

    @Override
    public void addCurrentThreadCpuTime(int measure) {
        locks[measure].take();
        benchmark.addCurrentThreadCpuTime(measure);
        locks[measure].release();
    }

    @Override
    public void addCurrentThreadSystemTime(int measure) {
        locks[measure].take();
        benchmark.addCurrentThreadSystemTime(measure);
        locks[measure].release();
    }

    @Override
    public void addCurrentThreadUserTime(int measure) {
        locks[measure].take();
        benchmark.addCurrentThreadUserTime(measure);
        locks[measure].release();
    }

    @Override
    public void addDeepSize(int measure, Object object, Visibility filter, boolean estimateArrays) {
        locks[measure].take();
        benchmark.addDeepSize(measure, object, filter, estimateArrays);
        locks[measure].release();
    }

    @Override
    public void addFreeBytesGCMX(int measure) {
        locks[measure].take();
        benchmark.addFreeBytesGCMX(measure);
        locks[measure].release();
    }
    @Override
    public void addFreeBytesGC(int measure) {
        locks[measure].take();
        benchmark.addFreeBytesGC(measure);
        locks[measure].release();
    }

    @Override
    public void addFreeBytesMX(int measure) {
        locks[measure].take();
        benchmark.addFreeBytesMX(measure);
        locks[measure].release();
    }

    @Override
    public void addJVMCpuTime(int measure) {
        locks[measure].take();
        benchmark.addJVMCpuTime(measure);
        locks[measure].release();
    }

    @Override
    public int addMeasure(String label) {
        return benchmark.addMeasure(label);
    }

    @Override
    public void addRun(String... data) {
        benchmark.addRun(data);
    }

    @Override
    public void addSize(int measure, Object obj) {
        locks[measure].take();
        benchmark.addSize(measure, obj);
        locks[measure].release();
    }

    @Override
    public void addSystemTime(int measure) {
        locks[measure].take();
        benchmark.addSystemTime(measure);
        locks[measure].release();
    }

    @Override
    public void addStopTimer(int measure) {
        locks[measure].take();
        benchmark.addStopTimer(measure);
        locks[measure].release();
    }

    @Override
    public void addStopAndStartTimer(int measure) {
        locks[measure].take();
        benchmark.addStopAndStartTimer(measure);
        locks[measure].release();
    }

    @Override
    public void addUserTime(int measure) {
        locks[measure].take();
        benchmark.addUserTime(measure);
        locks[measure].release();
    }

    @Override
    public void addValue(int measure, double value) {
        locks[measure].take();
        benchmark.addValue(measure, value);
        locks[measure].release();
    }

    @Override
    public boolean equals(Object obj) {
        return benchmark.equals(obj);
    }

    @Override
    public String getMeasure(int measure) {
        return benchmark.getMeasure(measure);
    }

    @Override
    public CSVFile getResults() {
        return benchmark.getResults();
    }

    @Override
    public Benchmark getSynchronized() {
        return this;
    }

    @Override
    public int hashCode() {
        return benchmark.hashCode();
    }

    @Override
    public void startTimer(int measure) {
        locks[measure].take();
        benchmark.startTimer(measure);
        locks[measure].release();
    }

    @Override
    public String toString() {
        return benchmark.toString();
    }
}
