package com.amihaiemil.camel;

import java.util.Collection;
import java.util.Map;

/**
 * A Yaml serializer.
 * @author sherif
 * @version $Id$
 * @param <YamlNode>
 * @since 1.0.0
 */
public interface YamlSerializer<YamlNode> {
    
    /**
     * Serialize a Map<Object, Object> to YamlNode.
     * @param map Map<Object, Object>
     * @return Yaml node
     * @TODO #30 Implement the serialize method with Map input and unit-test it.
     */
    YamlNode serialize(final Map<Object, Object> map);
    
    /**
     * Serialize a Map<Object, Object> to YamlNode.
     * @param object Object
     * @return Yaml node
     * @TODO #30 Implement the serialize method with Object input
     * and unit-test it.
     */
    YamlNode serialize(final Object object);
    
    /**
     * Serialize a Map<Object, Object> to YamlNode.
     * @param collection Collection<Object>
     * @return Yaml node
     * @TODO #30 Implement the serialize method with Collection input 
     * and unit-test it.
     */
    YamlNode serialize(final Collection<Object> collection);
}
