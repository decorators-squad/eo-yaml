package com.amihaiemil.eoyaml;

/**
 * A Yaml sequence.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public interface YamlSequence extends YamlNode {

    /**
     * The number of Yaml elements (scalars, mappings and sequences) found in
     * this sequence.
     * @return Integer.
     */
    int size();

    /**
     * Get the Yaml mapping  from the given index.
     * @param index Integer index.
     * @return Yaml mapping
     */
    YamlMapping yamlMapping(final int index);

    /**
     * Get the Yaml sequence  from the given index.
     * @param index Integer index.
     * @return Yaml sequence
     */
    YamlSequence yamlSequence(final int index);

    /**
     * Get the String from the given index.
     * @param index Integer index.
     * @return String
     */
    String string(final int index);
}
