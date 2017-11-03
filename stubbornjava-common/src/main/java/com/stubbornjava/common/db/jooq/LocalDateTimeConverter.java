package com.stubbornjava.common.db.jooq;

import org.jooq.Converter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeConverter implements Converter<Timestamp, LocalDateTime> {

    @Override
    public LocalDateTime from(Timestamp date) {
        return null == date ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    @Override
    public Timestamp to(LocalDateTime localDate) {
        return null == localDate ? null : Timestamp.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public Class<Timestamp> fromType() {
        return Timestamp.class;
    }

    @Override
    public Class<LocalDateTime> toType() {
        return LocalDateTime.class;
    }

}
