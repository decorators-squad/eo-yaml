package com.amihaiemil.eoyaml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation destined for the accessor methods (public non-void methods)
 * of Java Beans to be converted to YAML. The specified value will be the
 * comment of the corresponding YAML Node.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 7.1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface YamlComment {

    /**
     * String value of the comment.
     * @return String, never null.
     */
    String value();
}
