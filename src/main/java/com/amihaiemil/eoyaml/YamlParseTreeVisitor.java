package com.amihaiemil.eoyaml;

import com.amihaiemil.eoyaml.generated.antlr4.YamlParser;
import com.amihaiemil.eoyaml.generated.antlr4.YamlParserBaseVisitor;

final class YamlParseTreeVisitor extends YamlParserBaseVisitor<YamlNode> {

    @Override
    public YamlNode visitYamlDocument(final YamlParser.YamlDocumentContext ctx) {
        YamlStreamBuilder builder = Yaml.createYamlStreamBuilder();
        for(final YamlParser.YamlContext yaml : ctx.yaml()) {
            builder = builder.add(this.visitYaml(yaml));
        }
        return builder.build();
    }

    @Override
    public YamlNode visitYaml(final YamlParser.YamlContext ctx) {
        if(ctx.mapping() != null) {
            return this.visitMapping(ctx.mapping());
        } else if(ctx.sequence() != null) {
            return this.visitSequence(ctx.sequence());
        } else {
            return this.visitScalar(ctx.scalar());
        }
    }

    @Override
    public YamlNode visitMapping(final YamlParser.MappingContext ctx) {
        YamlMappingBuilder builder = Yaml.createYamlMappingBuilder();
        for(int i=0;i<ctx.scalar().size();i++) {
            YamlNode key = null;
            YamlNode value = null;
            if(i%2 == 0) {
                key = this.visitScalar(ctx.scalar(i));
            } else {
                value = this.visitScalar(ctx.scalar(i));
            }
            builder = builder.add(key, value);
        }
        return builder.build();
    }

    @Override
    public YamlNode visitSequence(final YamlParser.SequenceContext ctx) {
        YamlSequenceBuilder builder = Yaml.createYamlSequenceBuilder();
        for(final YamlParser.YamlContext yaml : ctx.yaml()) {
            builder = builder.add(this.visitYaml(yaml));
        }
        return builder.build();
    }

    @Override
    public YamlNode visitScalar(final YamlParser.ScalarContext ctx) {
        return new PlainStringScalar(ctx.getText());
    }
}
