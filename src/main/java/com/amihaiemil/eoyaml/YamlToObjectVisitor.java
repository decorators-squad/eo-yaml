package com.amihaiemil.eoyaml;

import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class YamlToObjectVisitor implements YamlVisitor<Object>{
    private final Class clazz;

    YamlToObjectVisitor(final Class clazz) {
        if(clazz.isInterface()) {
            if(clazz.getName().equals("java.util.List") || clazz.getName().equals("java.util.Collection")) {
                this.clazz = ArrayList.class;
            } else if(clazz.getName().equals("java.util.Map")) {
                this.clazz = HashMap.class;
            } else {
                throw new RuntimeException("Type " + clazz.getSimpleName() + ".class is an interface, it must be a class!");
            }
        } else {
            this.clazz = clazz;
        }
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
                    final String withoutSet = method.getName().substring(3);
                    final YamlNode value = node.value(
                        withoutSet.substring(0, 1).toLowerCase() +
                        withoutSet.substring(1)
                    );
                    if(value != null) {
                        try {
                            method.invoke(
                                loaded,
                                value.accept(
                                    new YamlToObjectVisitor(method.getParameters()[0].getType())
                                )
                            );
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
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
        final List<Class> primitives = Arrays.asList(
            Integer.class, Long.class, Float.class, Double.class,
            Boolean.class, Character.class, Byte.class, Short.class,
            int.class, long.class, float.class, double.class,
            short.class, char.class, byte.class, boolean.class
        );
        final String scalar = node.value();
        if(primitives.contains(this.clazz) && !this.clazz.isArray()) {
            try {
                final Method factory = MethodType.methodType(clazz).wrap().returnType().getMethod("valueOf", String.class);
                return factory.invoke(factory, scalar);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } else if(this.clazz.getSimpleName().equalsIgnoreCase("char") && this.clazz.isArray()) {
            return scalar.toCharArray();
        } else {
            return scalar;
        }
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
