package com.amihaiemil.eoyaml;

/**
 * Builder of Yaml Scalar. Implementations should be immutable and thread-safe.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 3.2.0
 */
public interface YamlScalarBuilder {

    /**
     * Add a line of text this Scalar. You can use this multiple
     * times or just once, if your String already contains NEW_LINE
     * chars.
     * @param value String
     * @return An instance of this builder.
     */
    YamlScalarBuilder addLine(final String value);

    /**
     * Build a plain Scalar. Ideally, you should use this when
     * your scalar is short, a single line of text.<br><br>
     * If you added more lines of text, all of them will be put together,
     * separated by spaces.
     * @return The built Scalar.
     */
    Scalar buildPlainScalar();

    /**
     * Build a Folded Block Scalar. Use this when your scalar has multiple
     * lines of text, but you don't care about the newlines, you want them
     * all separated by spaces. <br><br>
     *
     * The difference from buildPlainScalar() comes when you are printing
     * the created YAML:
     * <pre>
     *     plain: a very long scalar which should have been built as Folded
     *     folded:>
     *       a very long scalar which
     *       has been folded for readability
     * </pre>
     *
     * @return The built Scalar.
     */
    Scalar buildFoldedBlockScalar();

    /**
     * Build a Literal Block Scalar. Use this when your scalar has multiple
     * lines and you want these lines to be separated.
     *
     * @return The built Scalar.
     */
    Scalar buildLiteralBlockScalar();

}
