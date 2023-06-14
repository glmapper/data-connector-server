package com.idata.web;

import com.idata.common.ConnectorSourceConfigs;
import com.idata.common.enums.SourceType;
import com.idata.plugin.jmlt.JmltConvertor;
import com.idata.plugin.jmlt.JmltSyncer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 * DataSyncExecutorTest
 * </p>
 *
 * @author mailto:glmapper_2018@163.com glmapper
 * @date 2023/6/13 4:22 PM
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class DataSyncExecutorTest {

    protected static String url = "jdbc:mysql://localhost:3306/baseline?createDatabaseIfNotExist=true&useSSL=false&allowMultiQueries=true";

    @Test
    public void test_DataSyncExecutor() {

        ConnectorSourceConfigs configs = new ConnectorSourceConfigs();
        ConnectorSourceConfigs.InnerSource origin = new ConnectorSourceConfigs.InnerSource();
        origin.setDatabase("glmapper");
        origin.setUrl(url);
        origin.setUsername("root");
        origin.setPassword("glmapper@2018");
        ConnectorSourceConfigs.InnerSource target = new ConnectorSourceConfigs.InnerSource();
        target.setDatabase("glmapper");
        target.setUrl(url);
        target.setUsername("root");
        target.setPassword("glmapper@2018");

        configs.setBizType("jmlt");
        configs.setSourceType(SourceType.MYSQL57);
        configs.setOriginSource(origin);
        configs.setTargetSource(target);
        JmltConvertor convertor = new JmltConvertor();
        JmltSyncer syncer = new JmltSyncer(convertor, configs);
        syncer.sync();

        /*DataSyncExecutor.start(configs);*/
    }
}
