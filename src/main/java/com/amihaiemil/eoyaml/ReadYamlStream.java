package com.amihaiemil.eoyaml;

import java.util.Collection;

/**
 * Read YAML Stream of documents.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 3.1.4
 */
final class ReadYamlStream extends ComparableYamlStream {

    /**
     * Read lines of this YAML Stream.
     */
    private final YamlLines lines;

    /**
     * Constructor.
     * @param lines All YAML lines as they are read from the input.
     */
    ReadYamlStream(final AllYamlLines lines) {
        this.lines = lines;
    }

    @Override
    public Collection<YamlNode> values() {
        return null;
    }

}
