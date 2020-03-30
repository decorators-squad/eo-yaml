/**
 * Copyright (c) 2016-2020, Mihai Emil Andronache
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.amihaiemil.eoyaml;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Merge two YAML Mappings into a single one. Very useful in implementing
 * Updating of an existing YamlMapping. Use it like this:
 * <pre>
 *     final YamlMapping original = ...;
 *     final YamlMapping changed = ...;
 *     final YamlMapping merged = new MergedYamlMapping(
 *         original, changed, true|false
 *     );
 *     //or, via the Supplier constructor:
 *     final YamlMapping merged = new MergedYamlMapping(
 *         original,
 *         () -> {
 *             //create the changed version on-the-fly, right here.
 *         },
 *         true|false
 *     );
 * </pre>
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 4.1.0
 */
public final class MergedYamlMapping extends BaseYamlMapping {

    /**
     * The original YamlMapping into which the changes
     * will be merged.
     */
    private YamlMapping original;

    /**
     * The YamlMapping containing the changes.
     */
    private YamlMapping changed;

    /**
     * Override the conflicting keys or complain?
     */
    private boolean overrideConflicts;

    /**
     * Constructor. By default, conflicting keys will not be overriden.
     * @param original YamlMapping in which the changes will be merged.
     * @param changed YamlMapping containing the differences.
     */
    public MergedYamlMapping(
        final YamlMapping original,
        final YamlMapping changed
    ) {
        this(original, changed, false);
    }

    /**
     * Constructor. By default, conflicting keys will not be overriden.
     * @param original YamlMapping in which the changes will be merged.
     * @param changed Supplier of YamlMapping containing the differences.
     */
    public MergedYamlMapping(
        final YamlMapping original,
        final Supplier<YamlMapping> changed
    ) {
        this(original, changed.get(), false);
    }

    /**
     * Constructor. You can choose whether conflicting keys will be
     * overriden or not.
     * @param original YamlMapping in which the changes will be merged.
     * @param changed YamlMapping containing the differences.
     * @param overrideConflicts Override key conflicts or not?
     */
    public MergedYamlMapping(
        final YamlMapping original,
        final Supplier<YamlMapping> changed,
        final boolean overrideConflicts
    ) {
        this(original, changed.get(), overrideConflicts);
    }

    /**
     * Constructor. You can choose whether conflicting keys will be
     * overriden or not.
     * @param original YamlMapping in which the changes will be merged.
     * @param changed YamlMapping containing the differences.
     * @param overrideConflicts Override key conflicts or not?
     */
    public MergedYamlMapping(
        final YamlMapping original,
        final YamlMapping changed,
        final boolean overrideConflicts
    ) {
        this.original = original;
        this.changed = changed;
        this.overrideConflicts = overrideConflicts;
    }

    @Override
    public Set<YamlNode> keys() {
        final Set<YamlNode> merged = new LinkedHashSet<>();
        merged.addAll(this.original.keys());
        merged.addAll(this.changed.keys());
        return merged;
    }

    @Override
    public Collection<YamlNode> values() {
        return null;
    }

    @Override
    public YamlNode value(final YamlNode key) {
        return null;
    }

    @Override
    public YamlMapping yamlMapping(final YamlNode key) {
        return null;
    }

    @Override
    public YamlSequence yamlSequence(final YamlNode key) {
        return null;
    }

    @Override
    public String string(final YamlNode key) {
        return null;
    }

    @Override
    public String foldedBlockScalar(final YamlNode key) {
        return null;
    }

    @Override
    public Collection<String> literalBlockScalar(final YamlNode key) {
        return null;
    }

}
