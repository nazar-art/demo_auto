package net.lelyak.core.utils;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapLiteral {

    // Creates a map from a list of entries
    @SafeVarargs
    public static <K, V> Map<K, V> mapOf(Map.Entry<K, V>... entries) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();

        for (Map.Entry<K, V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    // Creates a map entry
    public static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    public static void main(String[] args) {
        Map<String, Integer> map = mapOf(entry("a", 1), entry("b", 2), entry("c", 3));
        System.out.println(map);
    }
}


