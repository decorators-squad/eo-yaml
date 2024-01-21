package com.amihaiemil.eoyaml;

public interface YamlVisitor<T>{
    default T visitYamlNode(final YamlNode node) {
        return this.visitChildren(node);
    }

    default T visitScalar(final Scalar node) {
        return this.visitChildren(node);
    }

    default T visitYamlMapping(final YamlMapping node) {
        return this.visitChildren(node);
    }

    default T visitYamlSequence(final YamlSequence node) {
        return this.visitChildren(node);
    }

    default T visitYamlStream(final YamlStream node) {
        return this.visitChildren(node);
    }

    default T visitChildren(final YamlNode node) {
        T result = defaultResult();
        if(node != null) {
            for (final YamlNode child : node.children()) {
                if(child != null) {
                    T childResult = child.accept(this);
                    result = aggregateResult(result, childResult);
                }
            }
        }
        return result;
    }

    T defaultResult();
    T aggregateResult(T aggregate, T nextResult);
}
