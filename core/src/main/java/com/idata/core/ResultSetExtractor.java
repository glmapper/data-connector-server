package com.idata.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetExtractor<T> {

    private final RowMapper<T> rowMapper;

    public ResultSetExtractor(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    public List<T> extractData(ResultSet resultSet) throws SQLException {
        List<T> resultList = new ArrayList<>();
        while (resultSet.next()) {
            T mappedObject = rowMapper.mapRow(resultSet);
            resultList.add(mappedObject);
        }
        return resultList;
    }
}
