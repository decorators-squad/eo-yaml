package com.amihaiemil.eoyaml.benchmark;

import com.amihaiemil.eoyaml.Yaml;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.*;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 1)
@Measurement(iterations = 1)
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
        try (final InputStream stream =
                 new FileInputStream(new File("target/test-classes/benchmark", "benchmark.yml"))) {
            Yaml.createYamlInput(stream, true)
                .readYamlMapping();
        }
    }

    @Benchmark
    public void snakeyaml() throws IOException {
        try (final Reader reader =
                 new FileReader(new File("target/test-classes/benchmark", "benchmark.yml"))) {
            this.yaml.load(reader);
        }
    }

    @Benchmark
    public void yamlbeans() throws IOException {
        try (final Reader reader =
                 new FileReader(new File("target/test-classes/benchmark", "benchmark.yml"))) {
            new YamlReader(reader).read();
        }
    }

    @Benchmark
    public void jacksonyaml() throws IOException {
        try (final Reader reader =
                 new FileReader(new File("target/test-classes/benchmark", "benchmark.yml"))) {
            YAMLFactory.builder()
                .build()
                .createParser(reader)
                .readValueAsTree();
        }
    }

}
