package com.idata.core;

import com.idata.core.mapper.ResultSetMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResultSetExtractor<T> {

    Class<T> t;

//    private final RowMapper<T> rowMapper;

    public ResultSetExtractor(Class<T> t) {
        this.t = t;
    }

    public List<T> extractData(ResultSet resultSet, Class<T> objectType) throws Exception {
        List<T> resultList = new ArrayList<>();
        ResultSetMapper<T> resultSetMapper = new ResultSetMapper();
        while (resultSet.next()) {
            T mappedObject = resultSetMapper.resultSetToJavaObject(resultSet, objectType);
            //T mappedObject = rowMapper.mapRow(resultSet);
            resultList.add(mappedObject);
        }
        return resultList;
    }

    public Class getClassType() {
        return t;
    }
}
