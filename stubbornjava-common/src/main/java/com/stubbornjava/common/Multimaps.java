package com.stubbornjava.common;

import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Multimaps {
    private Multimaps() {}

    public static <K, V> Multimap<K, V> newListMultimap(Map<K, ? extends Iterable<V>> input) {
        Multimap<K, V> multimap = ArrayListMultimap.create();
        for (Map.Entry<K, ? extends Iterable<V>> entry : input.entrySet()) {
          multimap.putAll(entry.getKey(), entry.getValue());
        }
        return multimap;
      }
}
