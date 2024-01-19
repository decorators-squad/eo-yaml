/**
 * Copyright (c) 2016-2024, Mihai Emil Andronache
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
package com.amihaiemil.eoyaml.extensions;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;

/**
 * Unit tests for {@link MergedYamlMapping}.
 * @author Mihai Andronache (amihaiemil.com)
 * @version $Id$
 * @since 4.1.0
 */
public final class MergedYamlMappingTest {

    /**
     * It should complain when both arguments are null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void complainsOnTwoNullMappings() {
        final YamlMapping original = null;
        final YamlMapping changed = null;
        final YamlMapping boom = new MergedYamlMapping(original, changed);
    }

    /**
     * It should represent the Original mapping if the changed one
     * is null.
     */
    @Test
    public void representsOriginalIfChangedIsNullOrEmpty() {
        final YamlMapping original = Yaml
            .createYamlMappingBuilder().add("key", "value").build();
        final YamlMapping changed = null;
        MatcherAssert.assertThat(
            new MergedYamlMapping(original, changed),
            Matchers.equalTo(original)
        );
        MatcherAssert.assertThat(
            new MergedYamlMapping(
                original,
                Yaml.createYamlMappingBuilder().build()
             ),
            Matchers.equalTo(original)
        );
    }

    /**
     * It should represent the Changed mapping if the original one
     * is null.
     */
    @Test
    public void representsChangeIfOriginalIsNullOrEmpty() {
        final YamlMapping original = null;
        final YamlMapping changed = Yaml
            .createYamlMappingBuilder().add("key", "value").build();
        MatcherAssert.assertThat(
            new MergedYamlMapping(original, changed),
            Matchers.equalTo(changed)
        );
        MatcherAssert.assertThat(
            new MergedYamlMapping(
                Yaml.createYamlMappingBuilder().build(),
                changed
            ),
            Matchers.equalTo(changed)
        );
    }

    /**
     * It should contain all the keys regardless of the override flag,
     * since there are no conflicts.
     */
    @Test
    public void mergesWithNoConflicts() {
        final YamlMapping expected = Yaml.createYamlMappingBuilder()
            .add("key1", "value1")
            .add("key2", "value2")
            .add("key3", "value3")
            .add("key4", "value4")
            .build();
        final YamlMapping original = Yaml.createYamlMappingBuilder()
            .add("key1", "value1")
            .add("key2", "value2")
            .build();
        final YamlMapping changed = Yaml.createYamlMappingBuilder()
            .add("key3", "value3")
            .add("key4", "value4")
            .build();
        MatcherAssert.assertThat(
            new MergedYamlMapping(original, changed, false),
            Matchers.equalTo(expected)
        );
        MatcherAssert.assertThat(
            new MergedYamlMapping(original, changed, true),
            Matchers.equalTo(expected)
        );
    }

    /**
     * It should merge without overriding conflicting keys.
     */
    @Test
    public void mergesWithConflictsNoOverride() {
        final YamlMapping expected = Yaml.createYamlMappingBuilder()
            .add("key1", "value1")
            .add("key2", "original")
            .add("key3", "value3")
            .build();
        final YamlMapping original = Yaml.createYamlMappingBuilder()
                .add("key1", "value1")
                .add("key2", "original")
                .build();
        final YamlMapping changed = Yaml.createYamlMappingBuilder()
                .add("key2", "changed")
                .add("key3", "value3")
                .build();
        MatcherAssert.assertThat(
            new MergedYamlMapping(original, changed, false),
            Matchers.equalTo(expected)
        );
        MatcherAssert.assertThat(
            new MergedYamlMapping(original, changed),
            Matchers.equalTo(expected)
        );
    }

    /**
     * It should merge by overriding conflicting keys.
     */
    @Test
    public void mergesWithConflictsDoOverride() {
        final YamlMapping expected = Yaml.createYamlMappingBuilder()
            .add("key1", "value1")
            .add("key2", "changed!")
            .add("key3", "value3")
            .build();
        final YamlMapping original = Yaml.createYamlMappingBuilder()
            .add("key1", "value1")
            .add("key2", "original")
            .build();
        final YamlMapping changed = Yaml.createYamlMappingBuilder()
            .add("key2", "changed!")
            .add("key3", "value3")
            .build();
        MatcherAssert.assertThat(
            new MergedYamlMapping(original, changed, true),
            Matchers.equalTo(expected)
        );
    }

    /**
     * It should merge the original one with the changed one provided
     * by a Supplier.
     */
    @Test
    public void mergesFromSupplier() {
        final YamlMapping expected = Yaml.createYamlMappingBuilder()
                .add("key1", "value1")
                .add("key2", "value2")
                .add("key3", "value3")
                .add("key4", "value4")
                .build();
        final YamlMapping original = Yaml.createYamlMappingBuilder()
                .add("key1", "value1")
                .add("key2", "value2")
                .build();
        MatcherAssert.assertThat(
            new MergedYamlMapping(
                original,
                () -> Yaml.createYamlMappingBuilder()
                    .add("key3", "value3")
                    .add("key4", "value4")
                    .build()
            ),
            Matchers.equalTo(expected)
        );
    }

    /**
     * It should override a conflicting key even if the new value is null.
     */
    @Test
    public void overridesWithNullValue() {
        final String nullValue = null;
        final YamlMapping expected = Yaml.createYamlMappingBuilder()
            .add("key1", "value1")
            .add("key2", nullValue)
            .add("key3", "value3")
            .build();
        final YamlMapping original = Yaml.createYamlMappingBuilder()
            .add("key1", "value1")
            .add("key2", "original")
            .build();
        final YamlMapping changed = Yaml.createYamlMappingBuilder()
            .add("key2", nullValue)
            .add("key3", "value3")
            .build();
        MatcherAssert.assertThat(
            new MergedYamlMapping(original, changed, true),
            Matchers.equalTo(expected)
        );
    }

    /**
     * MergedYamlMapping can merge 2 build YamlMapping, first one
     * with a comment on top of its first element.
     */
    @Test
    public void mergesBuiltWithCommentOnFirstElement() {
        final YamlMapping first = Yaml.createYamlMappingBuilder()
            .add(
                "mapping1",
                Yaml.createYamlMappingBuilder()
                    .add("key1", "value1")
                    .build("A comment")
            ).build();
        final YamlMapping second = Yaml.createYamlMappingBuilder()
            .add(
                "mapping2",
                Yaml.createYamlMappingBuilder()
                    .add("key2", "value2")
                    .build()
            ).build();
        final YamlMapping merged = new MergedYamlMapping(
            first, second
        );
        final StringBuilder expected = new StringBuilder();
        expected
            .append("# A comment").append(System.lineSeparator())
            .append("mapping1:").append(System.lineSeparator())
            .append("  key1: value1").append(System.lineSeparator())
            .append("mapping2:").append(System.lineSeparator())
            .append("  key2: value2");
        MatcherAssert.assertThat(
            merged.toString(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * MergedYamlMapping can merge 2 read YamlMapping, first one
     * with a comment on top of its first element.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void mergesReadWithCommentOnFirstElement() throws Exception {
        final YamlMapping first = Yaml.createYamlInput(
            new StringBuilder()
                .append("# A comment").append(System.lineSeparator())
                .append("mapping1:").append(System.lineSeparator())
                .append("  key1: value1")
                .toString()
        ).readYamlMapping();
        System.out.println(first);
        final YamlMapping second = Yaml.createYamlInput(
            new StringBuilder()
                .append("mapping2:").append(System.lineSeparator())
                .append("  key2: value2")
                .toString()
        ).readYamlMapping();
        final YamlMapping merged = new MergedYamlMapping(
            first, second
        );
        final StringBuilder expected = new StringBuilder();
        expected
            .append("# A comment").append(System.lineSeparator())
            .append("mapping1:").append(System.lineSeparator())
            .append("  key1: value1").append(System.lineSeparator())
            .append("mapping2:").append(System.lineSeparator())
            .append("  key2: value2");
        MatcherAssert.assertThat(
            merged.toString(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * Merges two simple mappings. Uses the request issue
     * <a href="https://github.com/decorators-squad/eo-yaml/issues/420#issuecomment-731776176">example</a>
     * as test case.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void mergesSimpleMappings() throws IOException {
        final YamlMapping original =  Yaml.createYamlInput(
            new StringBuilder()
                .append("Parameters:").append(System.lineSeparator())
                .append("  Param1:").append(System.lineSeparator())
                .append("    Type: x")
                .toString()
        ).readYamlMapping();
        final YamlMapping changed =Yaml.createYamlInput(
            new StringBuilder()
                .append("Parameters:").append(System.lineSeparator())
                .append("  Param2:").append(System.lineSeparator())
                .append("    Type: y")
                .toString()
        ).readYamlMapping();
        final YamlMapping merged = new MergedYamlMapping(
            original, changed
        );
        final StringBuilder expected = new StringBuilder();
        expected
            .append("Parameters:").append(System.lineSeparator())
            .append("  Param1:").append(System.lineSeparator())
            .append("    Type: x").append(System.lineSeparator())
            .append("  Param2:").append(System.lineSeparator())
            .append("    Type: y");
        MatcherAssert.assertThat(
            merged.toString(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * Merges two complex mappings with conflict override.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void mergesComplexMappingsWithOverride() throws IOException {
        final YamlMapping original = Yaml.createYamlInput(
            new StringBuilder()
                .append("Parameters:").append(System.lineSeparator())
                .append("  Param1:").append(System.lineSeparator())
                .append("    Type:").append(System.lineSeparator())
                .append("      Prop1: x").append(System.lineSeparator())
                .append("      Prop2:").append(System.lineSeparator())
                .append("        -").append(System.lineSeparator())
                .append("          foo: 1").append(System.lineSeparator())
                .append("          bar: 2").append(System.lineSeparator())
                .append("        - x").append(System.lineSeparator())
                .append("        - y").append(System.lineSeparator())
                .toString()
        ).readYamlMapping();
        final YamlMapping changed = Yaml.createYamlInput(
            new StringBuilder()
                .append("Parameters:").append(System.lineSeparator())
                .append("  Param1:").append(System.lineSeparator())
                .append("    Type:").append(System.lineSeparator())
                .append("      Prop1: changedX").append(System.lineSeparator())
                .append("      Prop2:").append(System.lineSeparator())
                .append("        - x").append(System.lineSeparator())
                .append("        - addedZ").append(System.lineSeparator())
                .append("  Param2:").append(System.lineSeparator())
                .append("    Type: y")
                .toString()
        ).readYamlMapping();
        final YamlMapping merged = new MergedYamlMapping(
            original, changed, true
        );
        final StringBuilder expected = new StringBuilder();
        expected
            .append("Parameters:").append(System.lineSeparator())
            .append("  Param1:").append(System.lineSeparator())
            .append("    Type:").append(System.lineSeparator())
            .append("      Prop1: changedX").append(System.lineSeparator())
            .append("      Prop2:").append(System.lineSeparator())
            .append("        -").append(System.lineSeparator())
            .append("          foo: 1").append(System.lineSeparator())
            .append("          bar: 2").append(System.lineSeparator())
            .append("        - x").append(System.lineSeparator())
            .append("        - y").append(System.lineSeparator())
            .append("        - addedZ").append(System.lineSeparator())
            .append("  Param2:").append(System.lineSeparator())
            .append("    Type: y");
        MatcherAssert.assertThat(
            merged.toString(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * Merges two complex mappings with no conflict override.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void mergesComplexMappingsWithNoOverride() throws IOException {
        final YamlMapping original = Yaml.createYamlInput(
            new StringBuilder()
                .append("Parameters:").append(System.lineSeparator())
                .append("  Param1:").append(System.lineSeparator())
                .append("    Type:").append(System.lineSeparator())
                .append("      Prop1: x").append(System.lineSeparator())
                .append("      Prop2:").append(System.lineSeparator())
                .append("        -").append(System.lineSeparator())
                .append("          foo: 1").append(System.lineSeparator())
                .append("          bar: 2").append(System.lineSeparator())
                .append("        - x").append(System.lineSeparator())
                .append("        - y").append(System.lineSeparator())
                .toString()
        ).readYamlMapping();
        final YamlMapping changed = Yaml.createYamlInput(
            new StringBuilder()
                .append("Parameters:").append(System.lineSeparator())
                .append("  Param1:").append(System.lineSeparator())
                .append("    Type:").append(System.lineSeparator())
                .append("      Prop1: changedX").append(System.lineSeparator())
                .append("      Prop2:").append(System.lineSeparator())
                .append("        - x").append(System.lineSeparator())
                .append("        - addedZ").append(System.lineSeparator())
                .append("  Param2:").append(System.lineSeparator())
                .append("    Type: y")
                .toString()
        ).readYamlMapping();
        final YamlMapping merged = new MergedYamlMapping(
            original, changed
        );
        final StringBuilder expected = new StringBuilder();
        expected
            .append("Parameters:").append(System.lineSeparator())
            .append("  Param1:").append(System.lineSeparator())
            .append("    Type:").append(System.lineSeparator())
            .append("      Prop1: x").append(System.lineSeparator())
            .append("      Prop2:").append(System.lineSeparator())
            .append("        -").append(System.lineSeparator())
            .append("          foo: 1").append(System.lineSeparator())
            .append("          bar: 2").append(System.lineSeparator())
            .append("        - x").append(System.lineSeparator())
            .append("        - y").append(System.lineSeparator())
            .append("  Param2:").append(System.lineSeparator())
            .append("    Type: y");
        MatcherAssert.assertThat(
            merged.toString(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * It should remove key "Prop3" from Mapping by replacing it with null.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldRemoveKeyFromComplexMapping() throws IOException{
        final YamlMapping original = Yaml.createYamlInput(
            new StringBuilder()
                .append("Parameters:").append(System.lineSeparator())
                .append("  Param1:").append(System.lineSeparator())
                .append("    Type:").append(System.lineSeparator())
                .append("      Prop1: x").append(System.lineSeparator())
                .append("      Prop2:").append(System.lineSeparator())
                .append("        -").append(System.lineSeparator())
                .append("          foo: 1").append(System.lineSeparator())
                .append("          bar: 2").append(System.lineSeparator())
                .append("        - x").append(System.lineSeparator())
                .append("        - y").append(System.lineSeparator())
                .append("      Prop3:").append(System.lineSeparator())
                .append("        foo: 1").append(System.lineSeparator())
                .append("        bar: 2").append(System.lineSeparator())
                .append("  Param2:").append(System.lineSeparator())
                .append("    Type: y")
                .toString()
        ).readYamlMapping();

        final YamlMapping keyRemover = Yaml
            .createYamlMappingBuilder()
            .add("Parameters", Yaml
                .createYamlMappingBuilder()
                .add("Param1", Yaml
                    .createYamlMappingBuilder()
                    .add("Type", Yaml
                        .createYamlMappingBuilder()
                        .add("Prop3", (String) null)
                        .build())
                    .build())
                .build()
            ).build();

        final YamlMapping merged = new MergedYamlMapping(
            original, keyRemover, true
        );

        final StringBuilder expected = new StringBuilder();
        expected
            .append("Parameters:").append(System.lineSeparator())
            .append("  Param1:").append(System.lineSeparator())
            .append("    Type:").append(System.lineSeparator())
            .append("      Prop1: x").append(System.lineSeparator())
            .append("      Prop2:").append(System.lineSeparator())
            .append("        -").append(System.lineSeparator())
            .append("          foo: 1").append(System.lineSeparator())
            .append("          bar: 2").append(System.lineSeparator())
            .append("        - x").append(System.lineSeparator())
            .append("        - y").append(System.lineSeparator())
            .append("      Prop3: null").append(System.lineSeparator())
            .append("  Param2:").append(System.lineSeparator())
            .append("    Type: y");
        MatcherAssert.assertThat(
            merged.toString(),
            Matchers.equalTo(expected.toString())
        );
    }

    /**
     * It should merge comments.
     * @see <a href="https://github.com/decorators-squad/eo-yaml/issues/469">Bug: #469</a>
     */
    @Test
    public void mergesByOverridingComment() {
        YamlMapping original = Yaml.createYamlMappingBuilder()
            .add("Key", "Value")
            .build("Old Comment");
        YamlMapping updater = Yaml.createYamlMappingBuilder()
            .add("Key", "Value")
            .build("New Comment");
        YamlMapping merged = new MergedYamlMapping(original,
            updater, Boolean.TRUE
        );
        MatcherAssert.assertThat(
            merged.comment().value(),
            Matchers.equalTo("New Comment")
        );
    }

    /**
     * It should merge comments.
     * @see <a href="https://github.com/decorators-squad/eo-yaml/issues/469">Bug: #469</a>
     */
    @Test
    public void mergesByOverridingCommentOfSequence() {
        YamlMapping original = Yaml.createYamlMappingBuilder()
            .add("Key", Yaml.createYamlSequenceBuilder().add("Value")
                .build("Old Comment"))
            .build();
        YamlMapping updater =  Yaml.createYamlMappingBuilder()
            .add("Key", Yaml.createYamlSequenceBuilder().add("Value")
                .build("New Comment"))
            .build();
        YamlMapping merged = new MergedYamlMapping(original,
            updater, Boolean.TRUE
        );
        MatcherAssert.assertThat(
            merged.yamlSequence("Key").comment().value(),
            Matchers.equalTo("New Comment")
        );
    }

    /**
     * It should merge inner comments.
     * @see <a href="https://github.com/decorators-squad/eo-yaml/issues/469">Bug: #469</a>
     */
    @Test
    public void mergesByOverridingInnerComment() {
        YamlMapping original = Yaml.createYamlMappingBuilder()
            .add("Key", Yaml.createYamlMappingBuilder()
                .add("InnerKey", "InnerValue")
                .build("Old Comment"))
            .build("Top Comment");
        YamlMapping updater = Yaml.createYamlMappingBuilder()
            .add("Key", Yaml.createYamlMappingBuilder()
                .add("InnerKey", "InnerValue")
                .build("New Comment"))
            .build();
        YamlMapping merged = new MergedYamlMapping(original,
            updater, Boolean.TRUE
        );
        MatcherAssert.assertThat(
            merged
                .value("Key")
                .asMapping()
                .comment()
                .value(),
            Matchers.equalTo("New Comment")
        );
    }

    /**
     * It should keep original comment when merge mapping has not overriding
     * flag set.
     * @see <a href="https://github.com/decorators-squad/eo-yaml/issues/469">Bug: #469</a>
     */
    @Test
    public void mergesByNotOverridingCommentWhenOverridingFlagNotSet() {
        YamlMapping original = Yaml.createYamlMappingBuilder()
            .add("Key", "Value")
            .build("Old Comment");
        YamlMapping updater = Yaml.createYamlMappingBuilder()
            .add("Key", "Value")
            .build("New Comment");
        YamlMapping merged = new MergedYamlMapping(original,
            updater, Boolean.FALSE
        );
        MatcherAssert.assertThat(
            merged.comment().value(),
            Matchers.equalTo("Old Comment")
        );
    }

    /**
     * It should keep original comment if the updater has no comment attached.
     * @see <a href="https://github.com/decorators-squad/eo-yaml/issues/469">Bug: #469</a>
     */
    @Test
    public void mergesByNotOverridingCommentWhenCommentNotProvided() {
        YamlMapping original = Yaml.createYamlMappingBuilder()
            .add("Key", "Value")
            .build("Old Comment");
        YamlMapping updater = Yaml.createYamlMappingBuilder()
            .add("Key", "Value")
            .build();
        YamlMapping merged = new MergedYamlMapping(original,
            updater, Boolean.TRUE
        );
        MatcherAssert.assertThat(
            merged.comment().value(),
            Matchers.equalTo("Old Comment")
        );
    }

    /**
     * It should keep original comment when updater merge sequence has no
     * comment attached.
     * @see <a href="https://github.com/decorators-squad/eo-yaml/issues/469">Bug: #469</a>
     */
    @Test
    public void mergesByNotOverridingCommentOfSequenceWhenCommentNotProvided() {
        YamlMapping original = Yaml.createYamlMappingBuilder()
            .add("Key", Yaml.createYamlSequenceBuilder().add("Value")
                .build("Old Comment"))
            .build();
        YamlMapping updater =  Yaml.createYamlMappingBuilder()
            .add("Key", Yaml.createYamlSequenceBuilder().add("Value")
                .build())
            .build();
        YamlMapping merged = new MergedYamlMapping(original,
            updater, Boolean.TRUE
        );
        MatcherAssert.assertThat(
            merged.yamlSequence("Key").comment().value(),
            Matchers.equalTo("Old Comment")
        );
    }
}
