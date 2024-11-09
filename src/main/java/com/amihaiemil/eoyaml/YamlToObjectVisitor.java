package com.amihaiemil.eoyaml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

final class YamlToObjectVisitor implements YamlVisitor<Object>{
    private final Class clazz;

    YamlToObjectVisitor(final Class clazz) {
        if(clazz.isInterface()) {
            throw new RuntimeException("Type " + clazz.getSimpleName() + ".class is an interface, it must be a class!");
        }
        this.clazz = clazz;
    }

    @Override
    public Object visitYamlMapping(final YamlMapping node) {
        final Object loaded;
        try {
            loaded = this.clazz.newInstance();
        } catch (final InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if(loaded instanceof Map) {
            final Map<Object, Object> loadedMap = (Map<Object, Object>) loaded;
            node.keys().forEach(
                k -> loadedMap.put(
                    this.visitYamlNode(k),
                    this.visitYamlNode(node.value(k))
                )
            );
        } else if(loaded instanceof Collection) {
            throw new IllegalArgumentException("Cannot load a YamlMapping into a Collection!");
        } else {
            final Method[] methods = loaded.getClass().getDeclaredMethods();
            for (final Method method : methods) {
                if (Modifier.isPublic(method.getModifiers())
                    && method.getParameterCount() == 1
                    && method.getName().startsWith("set")
                ) {
                    final String keyName = method.getName().substring(3).toLowerCase();
                    try {
                        final Object param =  node.value(keyName).accept(
                            new YamlToObjectVisitor(method.getParameters()[0].getType())
                        );
                        method.invoke(param);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return loaded;
    }

    @Override
    public Object visitYamlSequence(final YamlSequence node) {
        final List<Object> list = new ArrayList<>();
        node.values().forEach(
            v -> list.add(this.visitYamlNode(v))
        );
        return list;
    }

    @Override
    public Object visitYamlStream(final YamlStream node) {
        final List<Object> list = new ArrayList<>();
        node.values().forEach(
            v -> list.add(this.visitYamlNode(v))
        );
        return list;
    }

    @Override
    public Object visitScalar(final Scalar node) {
        try {
            System.out.println(
                "Clazz " + clazz.newInstance().getClass() + " contains " +
                    Arrays.asList(
                        Integer.class, Long.class, Float.class, Double.class, Short.class,
                        String.class, Boolean.class, Character.class, Byte.class, int.class, long.class, float.class, double.class, short.class, char.class
                    ).contains(this.clazz)
            );
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return node.value();
    }

    @Override
    public Object defaultResult() {
        return this.clazz.isInterface();
    }

    @Override
    public Object aggregateResult(Object aggregate, Object nextResult) {
        return null;
    }
}
