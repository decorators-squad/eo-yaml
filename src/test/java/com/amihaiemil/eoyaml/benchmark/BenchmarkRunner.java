package com.amihaiemil.eoyaml.benchmark;

import com.amihaiemil.eoyaml.Yaml;
import java.io.*;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class BenchmarkRunner {

    private final org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();

    public static void main(final String[] args) throws IOException {
        Main.main(args);
    }

    @Setup
    public void setup() {
    }

    @Benchmark
    public void eoyaml() throws IOException {
        Yaml.createYamlInput(new FileInputStream(
            new File("target/test-classes/benchmark", "benchmark.yml")), true)
            .readYamlMapping();
    }

    @Benchmark
    public void snakeyaml() throws FileNotFoundException {
        this.yaml.load(new FileInputStream(
            new File("target/test-classes/benchmark", "benchmark.yml")));
    }

}
