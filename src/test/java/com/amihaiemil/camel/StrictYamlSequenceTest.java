package com.amihaiemil.camel;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for {@link StrictYamlSequence}.
 * @author Salavat.Yalalov
 * @since 1.0.0
 */
public final class StrictYamlSequenceTest {
    /**
     * StrictYamlSequence can fetch its children.
     */
    @Test
    public void fetchesChildren() {
        List<YamlNode> elements = new LinkedList<>();
        elements.add(new Scalar("key1"));
        elements.add(new Scalar("key2"));
        elements.add(new Scalar("key3"));
        YamlSequence sequence = new StrictYamlSequence(new RtYamlSequence(elements));

        MatcherAssert.assertThat(
            sequence.children(), Matchers.not(Matchers.emptyIterable())
        );
    }

    /**
     * StringYamlSequence can throw YamlNodeNotFoundException
     * when the demanded YamlMapping is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullMapping() {
        YamlMapping origin = Mockito.mock(YamlMapping.class);
        Mockito.when(origin.yamlMapping("key")).thenReturn(null);
        YamlMapping strict = new StrictYamlMapping(origin);
        strict.yamlMapping("key");
    }

    /**
     * StringYamlSequence can throw YamlNodeNotFoundException
     * when the demanded YamlSequence is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullSequence() {
        YamlMapping origin = Mockito.mock(YamlMapping.class);
        Mockito.when(origin.yamlSequence("key")).thenReturn(null);
        YamlMapping strict = new StrictYamlMapping(origin);
        strict.yamlSequence("key");
    }

    /**
     * StringYamlSequence can throw YamlNodeNotFoundException
     * when the demanded String is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullString() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        Mockito.when(origin.string(1)).thenReturn(null);
        YamlSequence strict = new StrictYamlSequence(origin);
        strict.string(1);
    }

    /**
     * StrictYamlSequence can return the number of elements in a sequence.
     */
    @Test
    public void returnsSize() {
        List<YamlNode> elements = new LinkedList<>();
        elements.add(new Scalar("key1"));
        elements.add(new Scalar("key2"));
        YamlSequence sequence = new StrictYamlSequence(new RtYamlSequence(elements));

        MatcherAssert.assertThat(sequence.children().size(), Matchers.equalTo(2));
    }

//    @Test
//    public void returnsString() {
//    }
//
//    @Test
//    public void returnsSequence() {
//    }
//
//    @Test
//    public void returnsMapping() {
//    }

}
