package com.amihaiemil.camel;

import java.io.File;
import java.io.FileInputStream;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit tests for {@link RtYamlInput}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.1
 *
 */
public final class RtYamlInputTest {

    /**
     * YamlMapping can be read without its comments.
     * @throws Exception If something goes wrong
     * @todo #98:30m/DEV There is a bug in ReadYamlMapping.children(), the
     *  maps' values are not returned properly. Because of that,
     *  YamlMapping.equals does not work fine. Fix the bug and activate this
     *  unit test.
     */
    @Test
    @Ignore
    public void readsMappingWithoutComments() throws Exception {
        YamlMapping expected = Yaml.createYamlMappingBuilder()
            .add("name", "camel")
            .add("architect", "mihai")
            .add("developers",
                Yaml.createYamlSequenceBuilder()
                    .add("rultor")
                    .add("sherif")
                    .add("salikjan")
                    .build()
            ).build();
        YamlMapping read = new RtYamlInput(
            new FileInputStream(
                new File("src/test/resources/commentedMapping.yml")
            )
        ).readYamlMapping();
        MatcherAssert.assertThat(
            read.string("name"),
            Matchers.equalTo(expected.string("name"))
        );
        MatcherAssert.assertThat(
            read.string("architect"),
            Matchers.equalTo(expected.string("architect"))
        );
        MatcherAssert.assertThat(
            read.yamlSequence("developers").size(),
            Matchers.equalTo(expected.yamlSequence("developers").size())
        );
        MatcherAssert.assertThat(
            read.yamlSequence("developers").string(0),
            Matchers.equalTo(expected.yamlSequence("developers").string(0))
        );
        MatcherAssert.assertThat(
            read.yamlSequence("developers").string(1),
            Matchers.equalTo(expected.yamlSequence("developers").string(1))
        );
        MatcherAssert.assertThat(
            read.yamlSequence("developers").string(2),
            Matchers.equalTo(expected.yamlSequence("developers").string(2))
        );
        MatcherAssert.assertThat(
            read, Matchers.equalTo(expected)
        );
    }
    
}
