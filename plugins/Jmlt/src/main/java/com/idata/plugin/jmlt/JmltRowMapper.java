package com.idata.plugin.jmlt;

import com.idata.core.RowMapper;
import com.idata.plugin.jmlt.model.JmltModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JmltRowMapper extends RowMapper<JmltModel> {

    @Override
    public JmltModel mapRow(ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    public Class<?> rowType() {
        return JmltModel.class;
    }
}
