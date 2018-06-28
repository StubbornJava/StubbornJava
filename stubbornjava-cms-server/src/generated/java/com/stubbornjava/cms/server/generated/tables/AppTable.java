/*
 * This file is generated by jOOQ.
 */
package com.stubbornjava.cms.server.generated.tables;


import com.stubbornjava.cms.server.generated.Indexes;
import com.stubbornjava.cms.server.generated.Keys;
import com.stubbornjava.cms.server.generated.SjCms;
import com.stubbornjava.cms.server.generated.tables.records.AppRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AppTable extends TableImpl<AppRecord> {

    private static final long serialVersionUID = 1835385312;

    /**
     * The reference instance of <code>sj_cms.app</code>
     */
    public static final AppTable APP = new AppTable();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AppRecord> getRecordType() {
        return AppRecord.class;
    }

    /**
     * The column <code>sj_cms.app.app_id</code>.
     */
    public final TableField<AppRecord, Integer> APP_ID = createField("app_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>sj_cms.app.name</code>.
     */
    public final TableField<AppRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>sj_cms.app.date_created_ts</code>.
     */
    public final TableField<AppRecord, LocalDateTime> DATE_CREATED_TS = createField("date_created_ts", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "");

    /**
     * Create a <code>sj_cms.app</code> table reference
     */
    public AppTable() {
        this(DSL.name("app"), null);
    }

    /**
     * Create an aliased <code>sj_cms.app</code> table reference
     */
    public AppTable(String alias) {
        this(DSL.name(alias), APP);
    }

    /**
     * Create an aliased <code>sj_cms.app</code> table reference
     */
    public AppTable(Name alias) {
        this(alias, APP);
    }

    private AppTable(Name alias, Table<AppRecord> aliased) {
        this(alias, aliased, null);
    }

    private AppTable(Name alias, Table<AppRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> AppTable(Table<O> child, ForeignKey<O, AppRecord> key) {
        super(child, key, APP);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return SjCms.SJ_CMS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.APP_NAME_IDX, Indexes.APP_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<AppRecord, Integer> getIdentity() {
        return Keys.IDENTITY_APP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<AppRecord> getPrimaryKey() {
        return Keys.KEY_APP_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<AppRecord>> getKeys() {
        return Arrays.<UniqueKey<AppRecord>>asList(Keys.KEY_APP_PRIMARY, Keys.KEY_APP_NAME_IDX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AppTable as(String alias) {
        return new AppTable(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AppTable as(Name alias) {
        return new AppTable(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public AppTable rename(String name) {
        return new AppTable(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public AppTable rename(Name name) {
        return new AppTable(name, null);
    }
}
