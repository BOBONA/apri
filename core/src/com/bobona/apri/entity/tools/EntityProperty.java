package com.bobona.apri.entity.tools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EntityProperty<T> {

    private Map<String, EntityProperty<?>> properties;
    private T value;
    private Class<T> type;

    public EntityProperty(Class<T> type) {
        properties = new HashMap<>();
        this.type = type;
    }

    public T get() {
        return value;
    }

    public <C> EntityProperty<C> getPath(String path, Class<C> type) {
        String[] pathList = path.split("\\.");
        if (!properties.containsKey(pathList[0])) {
            if (pathList.length == 1) {
                properties.put(pathList[0], new EntityProperty<>(type));
            } else {
                properties.put(pathList[0], new EntityProperty<>(Object.class));
            }
        }
        if (pathList.length > 1) {
            return properties.get(pathList[0]).getPath(String.join(".",
                    Arrays.copyOfRange(pathList, 1, pathList.length)),
                    type);
        } else if (properties.get(pathList[0]).type.equals(type)) {
            return (EntityProperty<C>) properties.get(pathList[0]);
        } else {
            throw new ClassCastException("Expected '" + path + "' to be of type '" + type + "' instead was of type '" + properties.get(pathList[0]).type + "'");
        }
    }

    // todo add listeners to properties for whenever they are changed
    public void set(T value) {
        this.value = value;
    }
}
