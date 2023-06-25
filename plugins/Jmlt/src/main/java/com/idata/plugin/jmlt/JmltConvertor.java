package com.idata.plugin.jmlt;

import com.idata.plugin.jmlt.model.StandardMdjfModel;
import com.idata.core.Convertor;
import com.idata.plugin.jmlt.model.JmltModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这里的 Object是原始数据的对象模型，JmltModel 是警民联调的模型
 */
public class JmltConvertor implements Convertor<StandardMdjfModel, JmltModel> {

    @Override
    public StandardMdjfModel convertFrom(JmltModel origin) throws SQLException {
        StandardMdjfModel standardMdjfModel = new StandardMdjfModel();
        standardMdjfModel.setEmail(origin.getEmail());
        standardMdjfModel.setId(origin.getId());
        standardMdjfModel.setName(origin.getName());
        return standardMdjfModel;
    }

    @Override
    public JmltModel convertTo(StandardMdjfModel target) {
        return null;
    }

    @Override
    public List<StandardMdjfModel> batchConvertFrom(List<JmltModel> origin) throws SQLException {
        List<StandardMdjfModel> result = new ArrayList<>();
        for (JmltModel jmltModel : origin) {
            result.add(convertFrom(jmltModel));
        }
        return result;
    }

    @Override
    public List<JmltModel> batchConvertTo(List<StandardMdjfModel> target) {
        List<JmltModel> result = new ArrayList<>();
        for (StandardMdjfModel item : target) {
            result.add(convertTo(item));
        }
        return result;
    }

    @Override
    public String bizType() {
        return "jmlt";
    }
}
