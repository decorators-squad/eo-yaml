package com.amihaiemil.eoyaml;

/**
 * A YAML comment.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 5.0.0
 */
interface Comment {
    /**
     * Yaml node to which this comment applies.
     * @return YamlNode.
     */
    YamlNode yamlNode();

    /**
     * This comment as String.
     * @return String comment.
     */
    String toString();
}
