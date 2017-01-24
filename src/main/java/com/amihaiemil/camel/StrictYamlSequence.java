package com.amihaiemil.camel;

import java.util.Collection;

/**
 * Decorator for a YamlSequence which throws YamlNodeNotFoundException
 *  if any of the methods of the decorated YamlSequence returns null
 *  (if the given index points to a YamlNode that is not a YamlMapping, for instance).
 *  @author Salavat.Yalalov
 *  @version
 *  @since 1.0.0
 */
public final class StrictYamlSequence implements YamlSequence {
    /**
     * Original YamlSequence
     */
    private YamlSequence decorated;

    /**
     * Ctor.
     * @param decorated Original YamlSequence
     */
    public StrictYamlSequence(YamlSequence decorated) {
        this.decorated = decorated;
    }

    @Override
    public Collection<YamlNode> children() {
        return this.decorated.children();
    }

    @Override
    public int compareTo(final YamlNode other) {
        return this.decorated.compareTo(other);
    }

    /**
     * Returns the number of elements in this Yaml sequence.
     * @return Integer size >= 0
     */
    @Override
    public int size() {
        return this.decorated.size();
    }

    /**
     * Get the Yaml mapping  from the given index.
     * @param index Integer index.
     * @return Yaml mapping
     */
    @Override
    public YamlMapping yamlMapping(final int index) {
        return this.decorated.yamlMapping(index);
    }

    /**
     * Get the Yaml sequence  from the given index.
     * @param index Integer index.
     * @return Yaml sequence
     */
    @Override
    public YamlSequence yamlSequence(final int index) {
        return this.decorated.yamlSequence(index);
    }

    /**
     * Get the String from the given index.
     * @param index Integer index.
     * @return String
     */
    @Override
    public String string(final int index) {
        return this.decorated.string(index);
    }
}
