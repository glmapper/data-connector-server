package com.idata.web.controller;

import com.idata.common.ConnectorSourceConfigs;
import com.idata.executors.DataSyncExecutor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class TaskController {


    @RequestMapping("start")
    public void start(@RequestBody ConnectorSourceConfigs connectorSourceConfigs) {
        // 调用 executor 触发执行
        DataSyncExecutor.start(connectorSourceConfigs);
    }
}
