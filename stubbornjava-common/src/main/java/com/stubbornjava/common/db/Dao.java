package com.stubbornjava.common.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordUnmapper;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;
import org.jooq.impl.TableImpl;

public class Dao<Rec extends UpdatableRecord<Rec>, T, Table extends TableImpl<Rec>> {
    private final Table table;
    private final RecordMapper<Rec, T> mapper;
    private final RecordUnmapper<T, Rec> unmapper;
    public Dao(Table table,
               RecordMapper<Rec, T> mapper,
               RecordUnmapper<T, Rec> unmapper) {
        super();
        this.table = table;
        this.mapper = mapper;
        this.unmapper = unmapper;
    }

    public T insertReturning(DSLContext ctx, T obj) {
        Rec rec = records(ctx, Collections.singletonList(obj), false).get(0);
        rec.insert();
        return mapper.map(rec);
    }

    public void insert(DSLContext ctx, T obj) {
        insert(ctx, Collections.singletonList(obj));
    }

    @SuppressWarnings("unchecked")
    public void insert(DSLContext ctx, T... objects) {
        insert(ctx, Arrays.asList(objects));
    }

    public void insert(DSLContext ctx, Collection<T> objects) {
        // Execute a batch INSERT
        if (objects.size() > 1) {
            ctx.batchInsert(records(ctx, objects, false)).execute();
        }

        // Execute a regular INSERT
        else if (objects.size() == 1) {
            records(ctx, objects, false).get(0).insert();
        }
    }

    public void update(DSLContext ctx, T obj) {
        update(ctx, Collections.singletonList(obj));
    }

    @SuppressWarnings("unchecked")
    public void update(DSLContext ctx, T... objects) {
        update(ctx, Arrays.asList(objects));
    }

    public void update(DSLContext ctx, Collection<T> objects) {
        // Execute a batch UPDATE
        if (objects.size() > 1) {
            ctx.batchUpdate(records(ctx, objects, false)).execute();
        }

        // Execute a regular UPDATE
        else if (objects.size() == 1) {
            records(ctx, objects, false).get(0).update();
        }
    }

    public void delete(DSLContext ctx, T obj) {
        delete(ctx, Collections.singletonList(obj));
    }

    @SuppressWarnings("unchecked")
    public void delete(DSLContext ctx, T... objects) {
        delete(ctx, Arrays.asList(objects));
    }

    public void delete(DSLContext ctx, Collection<T> objects) {
        // Execute a batch DELETE
        if (objects.size() > 1) {
            ctx.batchDelete(records(ctx, objects, false)).execute();
        }

        // Execute a regular DELETE
        else if (objects.size() == 1) {
            records(ctx, objects, false).get(0).delete();
        }
    }

    public T fetchOne(DSLContext ctx, Function<Table, Condition> func) {
        return mapper.map(ctx.fetchOne(table, func.apply(table)));
    }

    public List<T> fetch(DSLContext ctx, Function<Table, Condition> func) {
        return ctx.fetch(table, func.apply(table)).map(mapper);
    }

    public List<T> fetchAll(DSLContext ctx) {
        return ctx.fetch(table).map(mapper);
    }

    public int deleteWhere(DSLContext ctx, Function<TableImpl<Rec>, Condition> func) {
        return ctx.deleteFrom(table).where(func.apply(table)).execute();
    }

    // Copy pasted from jOOQ's DAOImpl.java
    private /* non-final */ Field<?>[] pk() {
        UniqueKey<?> key = table.getPrimaryKey();
        return key == null ? null : key.getFieldsArray();
    }

    // Copy pasted from jOOQ's DAOImpl.java
    private /* non-final */ List<Rec> records(DSLContext ctx, Collection<T> objects, boolean forUpdate) {
        List<Rec> result = new ArrayList<>();
        Field<?>[] pk = pk();

        for (T object : objects) {
            Rec record = unmapper.unmap(object);
            record.attach(ctx.configuration());

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
