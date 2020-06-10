package com.amihaiemil.eoyaml;

public enum Node {

    /**
     * Type of {@link Scalar}
     */
    SCALAR,
    /**
     * Type of {@link YamlMapping}
     */
    MAPPING,
    /**
     * Type of {@link YamlStream}
     */
    STREAM,
    /**
     * Type of {@link YamlSequence}
     */
    SEQUENCE

}
