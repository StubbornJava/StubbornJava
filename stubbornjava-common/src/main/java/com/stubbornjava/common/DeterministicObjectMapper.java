package com.stubbornjava.common;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Objects;

import org.jooq.lambda.Seq;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

// {{start:mapper}}
public class DeterministicObjectMapper {

    private DeterministicObjectMapper() { }

    public static ObjectMapper create(ObjectMapper original, CustomComparators customComparators) {
        ObjectMapper mapper = original.copy()
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
            .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);

        /*
         *  Get the original instance of the SerializerProvider before we add our custom module.
         *  Our Collection Delegating code does not call itself.
         */
        SerializerProvider serializers = mapper.getSerializerProviderInstance();

        // This module is responsible for replacing non-deterministic objects
        // with deterministic ones. Example convert Set to a sorted List.
        SimpleModule module = new SimpleModule();
        module.addSerializer(Collection.class,
             new CustomDelegatingSerializerProvider(serializers, new CollectionToSortedListConverter(customComparators))
        );
        mapper.registerModule(module);
        return mapper;
    }

    /*
     * We need this class to delegate to the original SerializerProvider
     * before we added our module to it. If we have a Collection -> Collection converter
     * it delegates to itself and infinite loops until the stack overflows.
     */
    @SuppressWarnings("serial")
	private static class CustomDelegatingSerializerProvider extends StdDelegatingSerializer
    {
        private final SerializerProvider serializerProvider;

        private CustomDelegatingSerializerProvider(SerializerProvider serializerProvider,
                                                   Converter<?, ?> converter)
        {
            super(converter);
            this.serializerProvider = serializerProvider;
        }

        @Override
        protected StdDelegatingSerializer withDelegate(Converter<Object,?> converter,
                                                       JavaType delegateType, JsonSerializer<?> delegateSerializer)
        {
            return new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        }

        /*
         *  If we do not override this method to delegate to the original
         *  serializerProvider we get a stack overflow exception because it recursively
         *  calls itself. Basically we are hijacking the Collection serializer to first
         *  sort the list then delegate it back to the original serializer.
         */
        @Override
        public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
                throws JsonMappingException
        {
            return super.createContextual(serializerProvider, property);
        }
    }

    private static class CollectionToSortedListConverter extends StdConverter<Collection<?>, Collection<?>>
    {
        private final CustomComparators customComparators;

        public CollectionToSortedListConverter(CustomComparators customComparators) {
            this.customComparators = customComparators;
        }
        @Override
        public Collection<? extends Object> convert(Collection<?> value)
        {
            if (value == null || value.isEmpty())
            {
                return Collections.emptyList();
            }

            /**
             * Sort all elements by class first, then by our custom comparator.
             * If the collection is heterogeneous or has anonymous classes its useful
             * to first sort by the class name then by the comparator. We don't care
             * about that actual sort order, just that it is deterministic.
             */
            Comparator<Object> comparator = Comparator.comparing(x -> x.getClass().getName())
                                                      .thenComparing(customComparators::compare);
            Collection<? extends Object> filtered = Seq.seq(value)
                                                       .filter(Objects::nonNull)
                                                       .sorted(comparator)
                                                       .toList();
            if (filtered.isEmpty())
            {
                return Collections.emptyList();
            }

            return filtered;
        }
    }

    public static class CustomComparators {
        private final LinkedHashMap<Class<?>, Comparator<? extends Object>> customComparators;

        public CustomComparators() {
            customComparators = new LinkedHashMap<>();
        }

        public <T> void addConverter(Class<T> clazz, Comparator<?> comparator) {
            customComparators.put(clazz, comparator);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public int compare(Object first, Object second) {
            // If the object is comparable use its comparator
            if (first instanceof Comparable) {
                return ((Comparable) first).compareTo(second);
            }

            // If the object is not comparable try a custom supplied comparator
            for (Entry<Class<?>, Comparator<?>> entry : customComparators.entrySet()) {
                Class<?> clazz = entry.getKey();
                if (first.getClass().isAssignableFrom(clazz)) {
                    Comparator<Object> comparator = (Comparator<Object>) entry.getValue();
                    return comparator.compare(first, second);
                }
            }

            // we have no way to order the collection so fail hard
            String message = String.format("Cannot compare object of type %s without a custom comparator", first.getClass().getName());
            throw new UnsupportedOperationException(message);
        }
    }
}
// {{end:mapper}}

