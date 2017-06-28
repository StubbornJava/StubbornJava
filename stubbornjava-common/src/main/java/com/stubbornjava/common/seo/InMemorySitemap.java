package com.stubbornjava.common.seo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.jooq.lambda.Seq;

import com.google.common.base.Suppliers;
import com.google.common.collect.Maps;

// {{start:inMemorySitemap}}
public class InMemorySitemap {
    private final Supplier<Map<String, String>> indexSupplier;
    private InMemorySitemap(Supplier<Map<String, String>> indexSupplier) {
        this.indexSupplier = indexSupplier;
    }

    public String getIndex(String sitemapName) {
        return indexSupplier.get().get(sitemapName);
    }

    public List<String> getIndexNames() {
        return Seq.seq(indexSupplier.get().keySet())
                  .sorted()
                  .toList();
    }

    // Cache the sitemap for the lifetime of the JVM
    public static InMemorySitemap fromSupplier(Supplier<Map<String, List<String>>> supplier) {
        Supplier<Map<String, String>> sup = mapSupplier(supplier);
        Supplier<Map<String, String>> memoized = Suppliers.memoize(sup::get);
        return new InMemorySitemap(memoized);
    }

    // Cache the sitemap but refresh after the given duration.
    public static InMemorySitemap fromSupplierWithExpiration(
            Supplier<Map<String, List<String>>> supplier,
            long duration,
            TimeUnit unit) {
        Supplier<Map<String, String>> sup = mapSupplier(supplier);
        Supplier<Map<String, String>> memoized = Suppliers.memoizeWithExpiration(sup::get, duration, unit);
        return new InMemorySitemap(memoized);
    }

    private static Supplier<Map<String, String>> mapSupplier(Supplier<Map<String, List<String>>> supplier) {
        return () -> {
            Map<String, List<String>> originalMap = supplier.get();
            Map<String, String> newIndex = Maps.newHashMap();
            for (Entry<String, List<String>> entry : originalMap.entrySet()) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    newIndex.put(entry.getKey() + "-" + i + ".xml", entry.getValue().get(i));
                }
            }
            return newIndex;
        };
    }
}
// {{end:inMemorySitemap}}

