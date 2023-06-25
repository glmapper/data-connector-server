# data-connector-server

* api-web 提供对外的 API 访问
* common 公共模型和组件工具
* connectors 连接器
* executors 执行器
* core 核心接口
* plugins 业务包

# 快速开始

> 本组件目前仅支持 jdbc 方式同步，快速开始使用 mysql 为例、本案例是同一个库的表-表同步示例，实际上使用了同一个表；建议您在测试中可以使用不同的表来进行区分

## 准备

* 目标数据库、表；schema 见：com.idata.plugin.jmlt.model.JmltModel
* 原始数据库、表；schema 见：com.idata.plugin.jmlt.model.StandardMdjfModel

## 用例
api-web 模块下有个 DataSyncExecutorTest 测试用例，以此为入口；在完成配置之后执行用例即可。



