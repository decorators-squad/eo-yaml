package com.amihaiemil.eoyaml;

import javax.json.JsonArray;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Representation of a {@link javax.json.JsonArray} as YAML Sequence.
 * @author criske
 * @version $Id$
 * @since 5.1.7
 */
final class JsonYamlSequence extends BaseYamlSequence {

    /**
     * Json array being mapped.
     */
    private final JsonArray array;

    /**
     * Ctor.
     * @param array Json array being mapped.
     */
    JsonYamlSequence(final JsonArray array) {
        this.array = array;
    }

    @Override
    public Collection<YamlNode> values() {
        return this.array.stream()
            .map(value -> new JsonYamlDump(value).dump())
            .collect(Collectors.toList());
    }

    @Override
    public Comment comment() {
        return new Comment() {
            @Override
            public YamlNode yamlNode() {
                return JsonYamlSequence.this;
            }

            @Override
            public String value() {
                return "";
            }
        };
    }
}