package com.amihaiemil.eoyaml.benchmark;

import com.amihaiemil.eoyaml.Yaml;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class BenchmarkRunner {

    public static void main(final String[] args) throws IOException {
        Main.main(args);
    }

    @Setup
    public void setup() {
    }

    @Benchmark
    public void eoyamlLoadAndGetString() throws IOException {
        try (final InputStream stream =
                 new FileInputStream(new File("target/test-classes/benchmark", "benchmark.yml"))) {
            Yaml.createYamlInput(stream, true)
                .readYamlMapping()
                .yamlMapping("run")
                .string("benchmarkName");
        }
    }

    @Benchmark
    public void snakeyamlLoadAndGetString() throws IOException {
        try (final Reader reader =
                 new FileReader(new File("target/test-classes/benchmark", "benchmark.yml"))) {
            final Map<Object, Object> map = new org.yaml.snakeyaml.Yaml().load(reader);
            final Map<Object, Object> run = (Map<Object, Object>) map.get("run");
            run.get("benchmarkName");
        }
    }

    @Benchmark
    public void yamlbeansLoadAndGetString() throws IOException {
        try (final Reader reader =
                 new FileReader(new File("target/test-classes/benchmark", "benchmark.yml"))) {
            final Map<Object, Object> map = new YamlReader(reader).read(Map.class);
            final Map<Object, Object> run = (Map<Object, Object>) map.get("run");
            run.get("benchmarkName");
        }
    }

    @Benchmark
    public void jacksonyamlLoadAndGetString() throws IOException {
        try (final Reader reader =
                 new FileReader(new File("target/test-classes/benchmark", "benchmark.yml"))) {
            final TreeNode node = new ObjectMapper(new YAMLFactory())
                .createParser(reader)
                .readValueAsTree();
            final TreeNode run = node.get("run");
            run.get("benchmarkName");
        }
    }

}
