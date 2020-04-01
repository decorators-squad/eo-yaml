package com.amihaiemil.eoyaml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Decorator over some YamlLines which makes sure that lines
 * which satisfy one of the given conditions are ignored from
 * iteration.
 */
final class Skip implements YamlLines {

    /**
     * YamlLines.
     */
    private final YamlLines yamlLines;

    /**
     * If a YamlLine meets any of these conditions, it is skipped.
     */
    private final Condition[] conditions;

    /**
     * Ctor.
     * @param yamlLines The Yaml lines.
     * @param conditions Conditions.
     */
    Skip(final YamlLines yamlLines, final Condition... conditions) {
        this.yamlLines = yamlLines;
        this.conditions = conditions;
    }

    @Override
    public Iterator<YamlLine> iterator() {
        Iterator<YamlLine> iterator = this.yamlLines.iterator();
        if (iterator.hasNext()) {
            final List<YamlLine> notSkipped = new ArrayList<>();
            while (iterator.hasNext()) {
                boolean skip = false;
                final YamlLine current = iterator.next();
                for(int idx = 0; idx < this.conditions.length; idx++) {
                    if(this.conditions[idx].isMet(current)) {
                        skip = true;
                        break;
                    }
                }
                if(!skip) {
                    notSkipped.add(current);
                }
            }
            iterator = notSkipped.iterator();
        }
        return iterator;
    }

    @Override
    public Collection<YamlLine> lines() {
        return this.yamlLines.lines();
    }

    @Override
    public AllYamlLines nested(final int after) {
        return this.yamlLines.nested(after);
    }

    @Override
    public YamlNode toYamlNode(final YamlLine prev) {
        return this.yamlLines.toYamlNode(prev);
    }

    /**
     * Condition that a YamlLine has to satisfy in order to be
     * skipped/ignored from iteration.
     */
    interface Condition {

        /**
         * Is this condition met or not?
         * @param line YamlLine.
         * @return True or false.
         */
        boolean isMet(final YamlLine line);

    }
}
