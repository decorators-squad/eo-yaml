package com.amihaiemil.camel;

/**
 * A Yaml sequence.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 * @todo #6:30m/DEV Implement and unit test decorator StrictYamlSequence
 *  which should throw YamlNodeNotFoundException if any of the
 *  methods of the decorated YamlSequence returns null (if the given index
 *  points to a YamlNode that is not a YamlMapping, for instance).
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
