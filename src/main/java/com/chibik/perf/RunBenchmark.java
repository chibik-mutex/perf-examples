package com.chibik.perf;

import org.openjdk.jmh.profile.LinuxPerfAsmProfiler;
import org.openjdk.jmh.profile.WinPerfAsmProfiler;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class RunBenchmark {

    public static void runSimple(Class<?> clazz) {
        runSimple(clazz, TimeUnit.NANOSECONDS);
    }

    public static void runSimple(Class<?> clazz, TimeUnit timeUnit) {
        runSimple(clazz, timeUnit, new String[]{});
    }

    public static void runSimple(Class<?> clazz, TimeUnit timeUnit, String... jvmArgs) {
        try {
            Options opt = new OptionsBuilder()
                    .include(clazz.getSimpleName())
                    .jvmArgsAppend(
                            "-Xmx4G",
                            "-XX:BiasedLockingStartupDelay=0",
                            "-server",
                            "-XX:-TieredCompilation",
                            "-ea",
                            jvmArgs.length > 0 ? jvmArgs[0] : "-ea"
                    )
                    .addProfiler(LinuxPerfAsmProfiler.class)
                    .timeUnit(timeUnit)
                    .forks(1)
                    .build();

            Collection<RunResult> runResults = new Runner(opt).run();

            printResults(clazz, runResults);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void printResults(Class<?> clazz, Collection<RunResult> runResults) {

    }
}
