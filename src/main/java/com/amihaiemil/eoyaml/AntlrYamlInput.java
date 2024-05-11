package com.amihaiemil.eoyaml;

import com.amihaiemil.eoyaml.generated.antlr4.YamlLexer;
import com.amihaiemil.eoyaml.generated.antlr4.YamlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.Reader;

final class AntlrYamlInput implements YamlInput {

    /**
     * Yaml Parser from ANTLR.
     */
    private final YamlParser parser;

    /**
     * Ctor.
     * @param source Given source.
     * @throws IOException If something goes wrong while using the Reader.
     */
    AntlrYamlInput(final Reader source) throws IOException {
        this.parser = new YamlParser(
            new CommonTokenStream(
                new YamlLexer(
                    CharStreams.fromReader(source)
                )
            )
        );
    }

    @Override
    public YamlMapping readYamlMapping() throws IOException {
        final YamlParseTreeVisitor visitor = new YamlParseTreeVisitor();
        return visitor.visitMapping(this.parser.mapping()).asMapping();
    }

    @Override
    public YamlSequence readYamlSequence() throws IOException {
        final YamlParseTreeVisitor visitor = new YamlParseTreeVisitor();
        return visitor.visitSequence(this.parser.sequence()).asSequence();
    }

    @Override
    public YamlStream readYamlStream() throws IOException {
        return null;
    }

    @Override
    public Scalar readPlainScalar() throws IOException {
        return null;
    }

    @Override
    public Scalar readFoldedBlockScalar() throws IOException {
        return null;
    }

    @Override
    public Scalar readLiteralBlockScalar() throws IOException {
        return null;
    }
}
