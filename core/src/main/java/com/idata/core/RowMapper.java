package com.idata.core;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class RowMapper<T> {

    public abstract T mapRow(ResultSet resultSet) throws SQLException;

    public abstract Class<?> rowType();

    public RowMapper() {
        RowMapperFactory.register(rowType(), this);
    }
}
