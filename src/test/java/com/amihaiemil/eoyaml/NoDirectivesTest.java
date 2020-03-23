package com.amihaiemil.eoyaml;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Unit tests for {@link NoDirectives}.
 * @checkstyle ExecutableStatementCount (200 lines)
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 3.1.4
 */
public final class NoDirectivesTest {

    /**
     * NoDirectives can return the encapsulated lines.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void returnsLines() {
        final YamlLines lines = Mockito.mock(YamlLines.class);
        final Collection<YamlLine> collection = Mockito.mock(Collection.class);
        Mockito.when(lines.lines()).thenReturn(collection);
        MatcherAssert.assertThat(
            new NoDirectives(lines).lines(),
            Matchers.is(collection)
        );
    }

    /**
     * NoDirectives can turn itself into YamlNode.
     */
    @Test
    public void turnsIntoYamlNode() {
        final YamlLines lines = Mockito.mock(YamlLines.class);
        final YamlLine prev = Mockito.mock(YamlLine.class);
        final YamlNode result = Mockito.mock(YamlNode.class);
        Mockito.when(lines.toYamlNode(prev)).thenReturn(result);
        MatcherAssert.assertThat(
            new NoDirectives(lines).toYamlNode(prev),
            Matchers.is(result)
        );
    }

    /**
     * NoDirectives should ignore YAML directives
     * and markers from iteration.
     */
    @Test
    public void ignoresDirectives() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("%YAML 1.2", 0));
        lines.add(new RtYamlLine("---", 1));
        lines.add(new RtYamlLine("first: something", 2));
        lines.add(new RtYamlLine("second: ", 3));
        lines.add(new RtYamlLine("  fourth: some", 4));
        lines.add(new RtYamlLine("  fifth: values", 5));
        lines.add(new RtYamlLine("third:", 6));
        lines.add(new RtYamlLine("  - seq", 7));
        lines.add(new RtYamlLine("...", 8));
        lines.add(new RtYamlLine("%YAML 1.2", 9));
        lines.add(new RtYamlLine("---", 10));
        lines.add(new RtYamlLine("...", 11));

        final YamlLines ndmLines = new NoDirectives(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            ndmLines,
            Matchers.iterableWithSize(10)
        );
        final Iterator<YamlLine> iterator = ndmLines.iterator();
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("first: something")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("second:")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("fourth: some")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("fifth: values")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("third:")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("- seq")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("...")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("---")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("...")
        );
    }

    /**
     * NoDirectives iterates over all the lines, since
     * there are actually no directives to ignore.
     */
    @Test
    public void noDirectivesAndMarkersToIgnore() {
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  fourth: some", 2));
        lines.add(new RtYamlLine("  fifth: values", 3));
        final YamlLines ndmLines = new NoDirectives(
            new AllYamlLines(lines)
        );
        MatcherAssert.assertThat(
            ndmLines,
            Matchers.iterableWithSize(4)
        );
        final Iterator<YamlLine> iterator = ndmLines.iterator();
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("first: something")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("second:")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("fourth: some")
        );
        MatcherAssert.assertThat(
            iterator.next().trimmed(),
            Matchers.equalTo("fifth: values")
        );
    }

    /**
     * NoDirectivesOrMarkers works with no lines.
     */
    @Test
    public void iteratesEmptyLines() {
        final YamlLines ndmLines = new NoDirectives(
            new AllYamlLines(new ArrayList<YamlLine>())
        );
        MatcherAssert.assertThat(
            ndmLines,
            Matchers.iterableWithSize(0)
        );
    }
}
