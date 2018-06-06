package com.stubbornjava.common.db;

import static org.jooq.impl.DSL.using;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordUnmapper;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;
import org.jooq.impl.TableImpl;

public class Dao<Rec extends UpdatableRecord<Rec>, T> {
    private final TableImpl<Rec> table;
    private final RecordMapper<Rec, T> mapper;
    private final RecordUnmapper<T, Rec> unmapper;
    private final Configuration configuration;
    public Dao(TableImpl<Rec> table,
                     RecordMapper<Rec, T> mapper,
                     RecordUnmapper<T, Rec> unmapper,
                     Configuration configuration) {
        super();
        this.table = table;
        this.mapper = mapper;
        this.unmapper = unmapper;
        this.configuration = configuration;
    }

    public T insertReturning(T obj) {
        Rec rec = records(Collections.singletonList(obj), false).get(0);
        rec.insert();
        return mapper.map(rec);
    }

    public void insert(T obj) {
        insert(Collections.singletonList(obj));
    }

    @SuppressWarnings("unchecked")
    public void insert(T... objects) {
        insert(Arrays.asList(objects));
    }

    public void insert(Collection<T> objects) {
        // Execute a batch INSERT
        if (objects.size() > 1) {
            using(configuration).batchInsert(records(objects, false)).execute();
        }

        // Execute a regular INSERT
        else if (objects.size() == 1) {
            records(objects, false).get(0).insert();
        }
    }

    public void update(T obj) {
        update(Collections.singletonList(obj));
    }

    @SuppressWarnings("unchecked")
    public void update(T... objects) {
        update(Arrays.asList(objects));
    }

    public void update(Collection<T> objects) {
        // Execute a batch UPDATE
        if (objects.size() > 1) {
            using(configuration).batchUpdate(records(objects, false)).execute();
        }

        // Execute a regular UPDATE
        else if (objects.size() == 1) {
            records(objects, false).get(0).update();
        }
    }

    public void delete(T obj) {
        delete(Collections.singletonList(obj));
    }

    @SuppressWarnings("unchecked")
    public void delete(T... objects) {
        delete(Arrays.asList(objects));
    }

    public void delete(Collection<T> objects) {
        // Execute a batch DELETE
        if (objects.size() > 1) {
            using(configuration).batchDelete(records(objects, false)).execute();
        }

        // Execute a regular DELETE
        else if (objects.size() == 1) {
            records(objects, false).get(0).delete();
        }
    }

    public T fetchOne(Function<TableImpl<Rec>, Condition> func) {
        return mapper.map(using(configuration).fetchOne(table, func.apply(table)));
    }

    public List<T> fetch(Function<TableImpl<Rec>, Condition> func) {
        return using(configuration).fetch(table, func.apply(table)).map(mapper);
    }

    public List<T> fetchAll() {
        return using(configuration).fetch(table).map(mapper);
    }

    public int deleteWhere(Function<TableImpl<Rec>, Condition> func) {
        return using(configuration).deleteFrom(table).where(func.apply(table)).execute();
    }

    // Copy pasted from jOOQ's DAOImpl.java
    private /* non-final */ Field<?>[] pk() {
        UniqueKey<?> key = table.getPrimaryKey();
        return key == null ? null : key.getFieldsArray();
    }

    // Copy pasted from jOOQ's DAOImpl.java
    private /* non-final */ List<Rec> records(Collection<T> objects, boolean forUpdate) {
        List<Rec> result = new ArrayList<>();
        Field<?>[] pk = pk();

        for (T object : objects) {
            Rec record = unmapper.unmap(object);
            record.attach(using(configuration).configuration());

            if (forUpdate && pk != null)
                for (Field<?> field : pk)
                    record.changed(field, false);

            resetChangedOnNotNull(record);
            result.add(record);
        }

        return result;
    }

    // Copy pasted from jOOQ's Tools.java
    /**
     * [#2700] [#3582] If a POJO attribute is NULL, but the column is NOT NULL
     * then we should let the database apply DEFAULT values
     */
    private static final void resetChangedOnNotNull(Record record) {
        int size = record.size();

        for (int i = 0; i < size; i++)
            if (record.get(i) == null)
                if (!record.field(i).getDataType().nullable())
                    record.changed(i, false);
    }
}
