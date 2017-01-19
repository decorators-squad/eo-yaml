package com.amihaiemil.camel;

/**
 * A Yaml sequence.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public interface YamlSequence extends YamlNode {

    /**
     * Returns the number of elements in this Yaml sequence.
     * @return Integer size >= 0
     */
    int size();

    /**
     * Get the Yaml mapping  from the given index.
     * @param index Integer index.
     * @return Yaml mapping
     */
    YamlMapping getYamlMapping(final int index);

    /**
     * Get the Yaml sequence  from the given index.
     * @param index Integer index.
     * @return Yaml sequence
     */
    YamlSequence getYamlSequence(final int index);

    /**
     * Get the String from the given index.
     * @param index Integer index.
     * @return String
     */
    String getString(final int index);
}
