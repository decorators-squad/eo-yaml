package com.amihaiemil.camel;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Salavat.Yalalov on 24.01.2017.
 */
public final class StrictYamlSequenceTest {
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

        MatcherAssert.assertThat(sequence.children().size(), Matchers.equalTo(3));
    }

    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullMapping() {
    }

    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullSequence() {
    }

    @Test
    public void returnsSize() {
        List<YamlNode> elements = new LinkedList<>();
        elements.add(new Scalar("key1"));
        elements.add(new Scalar("key2"));
        YamlSequence sequence = new StrictYamlSequence(new RtYamlSequence(elements));

        MatcherAssert.assertThat(sequence.children().size(), Matchers.equalTo(2));
    }

    @Test
    public void returnsString() {
    }

    @Test
    public void returnsSequence() {
    }

    @Test
    public void returnsMapping() {
    }

}
