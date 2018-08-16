/*
 * This file is generated by jOOQ.
*/
package com.stubbornjava.cms.server.generated.tables;


import com.stubbornjava.cms.server.generated.Indexes;
import com.stubbornjava.cms.server.generated.Keys;
import com.stubbornjava.cms.server.generated.SjCms;
import com.stubbornjava.cms.server.generated.tables.records.PostTagRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
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
        "jOOQ version:3.10.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PostTagTable extends TableImpl<PostTagRecord> {

    private static final long serialVersionUID = 1272857439;

    /**
     * The reference instance of <code>sj_cms.post_tag</code>
     */
    public static final PostTagTable POST_TAG = new PostTagTable();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PostTagRecord> getRecordType() {
        return PostTagRecord.class;
    }

    /**
     * The column <code>sj_cms.post_tag.post_tag_id</code>.
     */
    public final TableField<PostTagRecord, Integer> POST_TAG_ID = createField("post_tag_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>sj_cms.post_tag.app_id</code>.
     */
    public final TableField<PostTagRecord, Integer> APP_ID = createField("app_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>sj_cms.post_tag.name</code>.
     */
    public final TableField<PostTagRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>sj_cms.post_tag.last_update_ts</code>.
     */
    public final TableField<PostTagRecord, LocalDateTime> LAST_UPDATE_TS = createField("last_update_ts", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "");

    /**
     * Create a <code>sj_cms.post_tag</code> table reference
     */
    public PostTagTable() {
        this(DSL.name("post_tag"), null);
    }

    /**
     * Create an aliased <code>sj_cms.post_tag</code> table reference
     */
    public PostTagTable(String alias) {
        this(DSL.name(alias), POST_TAG);
    }

    /**
     * Create an aliased <code>sj_cms.post_tag</code> table reference
     */
    public PostTagTable(Name alias) {
        this(alias, POST_TAG);
    }

    private PostTagTable(Name alias, Table<PostTagRecord> aliased) {
        this(alias, aliased, null);
    }

    private PostTagTable(Name alias, Table<PostTagRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
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
        return Arrays.<Index>asList(Indexes.POST_TAG_APP_ID_NAME_UNIQUE, Indexes.POST_TAG_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<PostTagRecord, Integer> getIdentity() {
        return Keys.IDENTITY_POST_TAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<PostTagRecord> getPrimaryKey() {
        return Keys.KEY_POST_TAG_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PostTagRecord>> getKeys() {
        return Arrays.<UniqueKey<PostTagRecord>>asList(Keys.KEY_POST_TAG_PRIMARY, Keys.KEY_POST_TAG_APP_ID_NAME_UNIQUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<PostTagRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<PostTagRecord, ?>>asList(Keys.POST_TAG_APP_ID_FK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PostTagTable as(String alias) {
        return new PostTagTable(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PostTagTable as(Name alias) {
        return new PostTagTable(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PostTagTable rename(String name) {
        return new PostTagTable(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PostTagTable rename(Name name) {
        return new PostTagTable(name, null);
    }
}