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

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Stack;

/**
 * This class provides access to a number of methods for measuring execution times,
 * memory consumption etc. Parts of the code are inspired by
 * http://nadeausoftware.com/articles/2008/03/java_tip_how_get_cpu_and_user_time_benchmarking
 * 
 * All reported times are in nanoseconds. All reported sizes are in bytes.
 * 
 * @author Fabian Prasser, Florian Kohlmayer
 */
public class Measures {

    /**
     * Visibility filter for members
     * 
     * @author Prasser, Kohlmayer
     */
    public static enum Visibility {
        ALL,
        NON_PUBLIC,
        NONE,
        PRIVATE_ONLY
    }

    /** Instrumentation, if any */
    private static Instrumentation instrumentation = null;

    /**
     * Premain for setting the instrumentation instance
     * 
     * @param args
     * @param instr
     */
    public static void premain(final String args, final Instrumentation instr) {
        if (instr == null) { throw new IllegalStateException("Use -javaagent:lib/JARFILE.jar to initialize agent"); }
        instrumentation = instr;
    }

    /** Timestamps for different measures */
    protected long[] time;
    protected long[] threadCpuTime;
    protected long[] threadSystemTime;
    protected long[] threadUserTime;
    protected long[] bytesGC;
    protected long[] bytesMX;
    

    /**
     * Construct a new instance
     * @param size
     */
    public Measures(int size) {
        this.time = new long[size];
        threadCpuTime = new long[size];
        threadSystemTime = new long[size];
        threadUserTime = new long[size];
        bytesGC = new long[size];
        bytesMX = new long[size];
    }

    /** 
     * Get CPU time of all threads
     * 
     * @return time in nanoseconds
     */
    public long getCpuTime() {
        return getCpuTime(ManagementFactory.getThreadMXBean().getAllThreadIds());
    }

    /** 
     * Get CPU time of the current thread in nanoseconds. 
     * @return time in nanoseconds 
     */
    public long getCurrentThreadCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadCpuTime() : 0L;
    }

    /** 
     * Get system time of the current thread in nanoseconds.
     * @return time in nanoseconds 
     */
    public long getCurrentThreadSystemTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ? (bean.getCurrentThreadCpuTime() - bean.getCurrentThreadUserTime())
                : 0L;
    }

    /** 
     * Get user time of the current thread in nanoseconds.
     * @return time in nanoseconds 
     */
    public long getCurrentThreadUserTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadUserTime() : 0L;
    }

    /** 
     * Stores a baseline for CPU time of the current thread in nanoseconds.  
     */
    public void startCurrentThreadCpuTime(int measure) {
        threadCpuTime[measure] = getCurrentThreadCpuTime();
    }

    /** 
     * Stores a baseline for system time of the current thread in nanoseconds.
     */
    public void startCurrentThreadSystemTime(int measure) {
        threadSystemTime[measure] = getCurrentThreadSystemTime();
    }

    /** 
     * Stores a baseline for user time of the current thread in nanoseconds.
     */
    public void startCurrentThreadUserTime(int measure) {
        threadUserTime[measure] = getCurrentThreadUserTime();
    }

    /**
     * Returns the deep memory usage of the given object. Uses instrumentation.
     * 
     * @param object
     * @return size in bytes
     */
    public long getDeepSize(final Object object) {
        return getDeepSize(object, Visibility.ALL, false);
    }

    /**
     * Returns the deep memory usage of the given object, applying the
     * given visibility filter. Uses instrumentation.
     * 
     * @param object
     * @param filter the reference filter
     * @param estimateArrays should array sizes be estimated
     * @return size in bytes
     */
    public long getDeepSize(final Object object, final Visibility filter, final boolean estimateArrays) {

        if (instrumentation == null) { throw new IllegalStateException("Use -javaagent:lib/JARFILE.jar to initialize agent"); }
        return getDeepSize(new HashSet<Object>(), object, filter, estimateArrays);
    }

    /** 
     * Returns the currently used memory as reported after a GC
     * @return size in bytes
     */
    public long getUsedBytesGC() {
        heavyGC();
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    /** 
     * Returns the currently used memory as reported by MX Management
     * @return size in bytes 
     */
    public long getUsedBytesMX() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() +
               ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
    }
    
    /** 
     * Stores a baseline for the currently used memory as reported after a GC
     */
    public void startUsedBytesGC(int measure) {
        bytesGC[measure] = getUsedBytesGC();
    }

    /** 
     * Stores a baseline for the currently used memory as reported by MX Management
     */
    public void startUsedBytesMX(int measure) {
        bytesMX[measure] = getUsedBytesMX();
    }

    /**
     * Runs a "heavy" GC.
     * http://lewisleo.blogspot.jp/2012/08/java-collections-performance.html
     */
    public void heavyGC() {
        try {
            System.gc();
            Thread.sleep(200);
            System.runFinalization();
            Thread.sleep(200);
            System.gc();
            Thread.sleep(200);
            System.runFinalization();
            Thread.sleep(1000);
            System.gc();
            Thread.sleep(200);
            System.runFinalization();
            Thread.sleep(200);
            System.gc();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /** 
     * Get JVM CPU time with MX Management
     * @return time in nanoseconds
     */
    @SuppressWarnings("restriction")
    public long getJVMCpuTime() {
        OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
        if (!(bean instanceof com.sun.management.OperatingSystemMXBean)) return 0L;
        return ((com.sun.management.OperatingSystemMXBean) bean).getProcessCpuTime() * 1000000l;
    }

    /**
     * Returns the size of the given object using instrumentation. Does not include referenced
     * objects.
     * 
     * @param obj
     * @return size in bytes
     */
    public long getSize(final Object obj) {
        if (instrumentation == null) { throw new IllegalStateException("Use -javaagent:lib/JARFILE.jar to initialize agent"); }
        return instrumentation.getObjectSize(obj);
    }

    /** 
     * Get system time of all threads
     * @return time in nanoseconds 
     */
    public long getSystemTime() {
        return getSystemTime(ManagementFactory.getThreadMXBean().getAllThreadIds());
    }

    /** 
     * Get user time of all threads
     * @return time in nanoseconds 
     */
    public long getUserTime() {
        return getUserTime(ManagementFactory.getThreadMXBean().getAllThreadIds());
    }

    /**
     * Start a timer
     * 
     * @param measure
     */
    public void startTimer(int measure) {
        time[measure] = System.nanoTime();
    }

    /**
     * Return the wall-clock time since the last call to start()
     * 
     * @param measure
     * @return time in nanoseconds
     */
    public long stopTimer(int measure) {
        return System.nanoTime() - time[measure];
    }

    /**
     * Return the wall-clock time since the last call to start and re-starts the
     * timer
     * 
     * @param measure
     * @return time in nanoseconds
     */
    public long stopAndStartTimer(int measure) {
        long time = System.nanoTime() - this.time[measure];
        startTimer(measure);
        return time;
    }

    /** 
     * Returns CPU time of the current thread in nanoseconds compared to baseline  
     */
    public long stopCurrentThreadCpuTime(int measure) {
        return getCurrentThreadCpuTime() - threadCpuTime[measure];
    }

    /** 
     * Returns system time of the current thread in nanoseconds compared to baseline
     */
    public long stopCurrentThreadSystemTime(int measure) {
        return getCurrentThreadSystemTime() - threadSystemTime[measure];
    }

    /** 
     * Returns user time of the current thread in nanoseconds compared to baseline
     */
    public long stopCurrentThreadUserTime(int measure) {
        return getCurrentThreadUserTime() - threadUserTime[measure];
    }
    
    /** 
     * Returns the currently used memory as reported after a GC compared to baseline
     * @return size in bytes
     */
    public long stopUsedBytesGC(int measure) {
        return getUsedBytesGC() - bytesGC[measure];
    }

    /** 
     * Returns the currently used memory as reported by MX Management compared to baseline
     * @return size in bytes 
     */
    public long stopUsedBytesMX(int measure) {
        return getUsedBytesMX() - bytesMX[measure];
    }

    
    /** 
     * Get CPU time in nanoseconds.
     * @return time in nanoseconds 
     */
    private long getCpuTime(long[] ids) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        if (!bean.isThreadCpuTimeSupported()) return 0L;
        long time = 0L;
        for (long i : ids) {
            long t = bean.getThreadCpuTime(i);
            if (t != -1) time += t;
        }
        return time;
    }

    /**
     * Deep memory usage of the given object.
     * 
     * @param instrumentation the instrumentation
     * @param counted the counted
     * @param obj the obj
     * @param filter the filter
     * @param estimateArraySize the estimate array size
     * @return size in bytes
     * @throws SecurityException the security exception
     */
    private long getDeepSize(final HashSet<Object> counted,
                             final Object obj,
                             final Visibility filter,
                             final boolean estimateArraySize) throws SecurityException {
        final Stack<Object> st = new Stack<Object>();
        st.push(obj);
        long total = 0L;
        while (!st.isEmpty()) {
            final Object o = st.pop();
            if (counted.add(Integer.valueOf(System.identityHashCode(o)))) {

                final long sz = instrumentation.getObjectSize(o);
                total += sz;
                Class<?> clz = o.getClass();
                final Class<?> compType = clz.getComponentType();
                if ((compType != null) && !compType.isPrimitive()) {
                    final Object arr[] = (Object[]) o;
                    final Object aobj[] = arr;
                    int j = 0;
                    if (estimateArraySize) {
                        long sizeOfObject = -1;

                        for (final int l = aobj.length; j < l; j++) {
                            final Object el = aobj[j];
                            if (el != null) {
                                // if (sizeOfObject == -1) sizeOfObject =
                                // deepMemoryUsageOf0(instrumentation, counted,
                                // aobj[j], filter, estimateArraySize);
                                if (sizeOfObject == -1) {
                                    sizeOfObject = instrumentation.getObjectSize(aobj[j]);
                                }

                                total += sizeOfObject;
                            }
                        }
                    } else {
                        for (final int l = aobj.length; j < l; j++) {
                            final Object el = aobj[j];
                            if (el != null) {
                                st.push(el);
                            }
                        }
                    }

                }
                for (; clz != null; clz = clz.getSuperclass()) {
                    final Field afield[] = clz.getDeclaredFields();
                    int i = 0;
                    for (final int k = afield.length; i < k; i++) {
                        final Field fld = afield[i];
                        final int mod = fld.getModifiers();
                        if (((mod & 8) == 0) && matches(filter, mod)) {
                            final Class<?> fieldClass = fld.getType();
                            if (!fieldClass.isPrimitive()) {
                                if (!fld.isAccessible()) {
                                    fld.setAccessible(true);
                                }
                                try {
                                    final Object subObj = fld.get(o);
                                    if (subObj != null) {
                                        st.push(subObj);
                                    }
                                } catch (final IllegalAccessException illAcc) {
                                    throw new InternalError((new StringBuilder("Couldn't read ")).append(fld)
                                                                                                 .toString());
                                }
                            }
                        }
                    }
                }
            }
        }
        return total;
    }

    /** 
     * Get system time in nanoseconds.
     * @return time in nanoseconds 
     */
    private long getSystemTime(long[] ids) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        if (!bean.isThreadCpuTimeSupported()) return 0L;
        long time = 0L;
        for (long i : ids) {
            long tc = bean.getThreadCpuTime(i);
            long tu = bean.getThreadUserTime(i);
            if (tc != -1 && tu != -1) time += (tc - tu);
        }
        return time;
    }

    /** 
     * Get user time in nanoseconds.
     * @return time in nanoseconds 
     */
    private long getUserTime(long[] ids) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        if (!bean.isThreadCpuTimeSupported()) return 0L;
        long time = 0L;
        for (long i : ids) {
            long t = bean.getThreadUserTime(i);
            if (t != -1) time += t;
        }
        return time;
    }

    /**
     * Checks if the modifier is matched by the visibility filter
     * 
     * @param filter 
     * @param modifier
     * @return true
     */
    private boolean matches(final Visibility filter, final int modifier) {
        switch (filter.ordinal()) {
        case 0:
            return true;
        case 3:
            return false;
        case 1:
            return (modifier & 2) != 0;
        case 2:
            return (modifier & 1) == 0;
        }
        throw new IllegalArgumentException((new StringBuilder("Illegal filter ")).append(modifier).toString());
    }

    /**
     * Is instrumentation active
     * @return
     */
    public boolean isInstrumented() {
        return instrumentation != null;
    }
}
