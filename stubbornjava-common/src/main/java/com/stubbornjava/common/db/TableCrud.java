package com.stubbornjava.common.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordUnmapper;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;
import org.jooq.impl.TableImpl;

public class TableCrud<Rec extends UpdatableRecord<Rec>, T> {
    private final TableImpl<Rec> table;
    private final RecordMapper<Record, T> mapper;
    private final RecordUnmapper<T, Rec> unmapper;
    private final Supplier<DSLContext> configSupplier;
    public TableCrud(TableImpl<Rec> table,
                     // Ideally this would be RecordMapper<Rec, T> mapper but hitting generic issues
                     RecordMapper<Record, T> mapper,
                     RecordUnmapper<T, Rec> unmapper,
                     Supplier<DSLContext> configSupplier) {
        super();
        this.table = table;
        this.mapper = mapper;
        this.unmapper = unmapper;
        this.configSupplier = configSupplier;
    }

    public T insertReturning(T obj) {
        Rec rec = records(Collections.singletonList(obj), false).get(0);
        rec.insert();
        return rec.map(mapper);
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
            configSupplier.get().batchInsert(records(objects, false)).execute();
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
            configSupplier.get().batchUpdate(records(objects, false)).execute();
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
            configSupplier.get().batchDelete(records(objects, false)).execute();
        }

        // Execute a regular DELETE
        else if (objects.size() == 1) {
            records(objects, false).get(0).delete();
        }
    }

    public T findOne(Function<TableImpl<Rec>, Condition> func) {
        return configSupplier.get().fetchOne(table, func.apply(table)).map(mapper);
    }

    public List<T> find(Function<TableImpl<Rec>, Condition> func) {
        return configSupplier.get().fetch(table, func.apply(table)).map(mapper);
    }

    public int deleteWhere(Function<TableImpl<Rec>, Condition> func) {
        return configSupplier.get().deleteFrom(table).where(func.apply(table)).execute();
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
            record.attach(configSupplier.get().configuration());

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
