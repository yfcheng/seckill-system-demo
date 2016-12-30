# seckill-system-demo
> 测试连接github

> 创建项目流程

* 用maven命令创建项目
    * mvn archetype:create -DgroupId=com.tanhao -DartifactId=seckill -DarchetypeArtifactId=maven-archetype-webapp
* 创建数据库表
* 新建实体类和DAO接口
* mybatis实现dao接口以及与spring整合
* service层的实现与测试
* web层实现，秒杀交互与计时
    * bootstrap与jquery的使用
* 高并发的优化
    * 前端控制：暴露接口，按钮防重复
    * 动静态数据分离：CDN缓存，后端缓存（redis）
        * 使用redis优化地址暴露接口
    * 事务竞争：减少事务锁时间
    