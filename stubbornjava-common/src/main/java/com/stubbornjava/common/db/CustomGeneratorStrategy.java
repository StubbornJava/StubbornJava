package com.stubbornjava.common.db;

import org.jooq.util.DefaultGeneratorStrategy;
import org.jooq.util.Definition;

public class CustomGeneratorStrategy extends DefaultGeneratorStrategy {

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        // Append Tables to the end of the Table classes
        if (getJavaPackageName(definition, mode).endsWith("tables")) {
            return super.getJavaClassName(definition, mode) + "Table";
        }
        return super.getJavaClassName(definition, mode);
    }
}
