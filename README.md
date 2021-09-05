# redismanager

[![](https://img.shields.io/badge/Base%20up%20by-SpringBoot%202.3.4%20RELEASE-06?logo=SpringBoot&labelColor=02303A)](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter/2.3.4.RELEASE)
[![](https://img.shields.io/badge/Use%20up%20by-JDK%201.8+-important?logo=java&labelColor=02303A)](https://www.oracle.com/cn/java/technologies/javase/javase-jdk8-downloads.html)
[![](https://img.shields.io/badge/Use%20up%20by-MySQL%205.x.x%20<%208.x.x-9cf?logo=MySQL&labelColor=02303A)](https://dev.mysql.com/doc/relnotes/mysql/5.7/en/)
[![](https://img.shields.io/badge/Use%20up%20by-Redis%203.0.5+%20-06A0CE?logo=Redis&labelColor=02303A)](https://redis.io)
[![](https://img.shields.io/badge/Use%20up%20by-Zookeeper%203.6.0+%20-06A0CE?logo=apache&labelColor=02303A)](https://zookeeper.apache.org/doc/r3.6.0/index.html)

**redismanager**用于管理Redis的数据，支持Redis**单机**和**集群**下数据的查询，添加，修改，删除；支持自定义 key，value 的序列化方式。

## 模块说明

```text
redismanager/
    ├── redismanager-cacher/        缓存模块
    ├── redismanager-common/        公共模块
    ├── redismanager-config/        配置模块
    ├── redismanager-config-sdk/    配置SDK模块
    ├── redismanager-core/          核心模块
    ├── redismanager-dao/           DAO模块
    ├── redismanager-limiter/       限流模块
    ├── redismanager-model/         模型模块
    ├── redismanager-token/         Token模块
    ├── redismanager-uid-generator/ UID模块
    ├── redismanager-web/           WEB模块
    ├── ……
    └── ……
```

### redismanager 模块依赖关系

![](https://img-blog.csdnimg.cn/20210828151924889.png)

### redismanager-core 模块依赖

![](https://img-blog.csdnimg.cn/20210828151931785.png)

## 构建使用

由于项目使用了 **zookeeper**（<font color="red">版本：3.6.0+
</font>），**redis**（<font color="red">版本：3.0.5+</font>），**mysql**（<font color="red">版本：5.x.x，不支持 8.x.x</font>），因此你需要提前准备好这些环境，这里不再赘述

1. 执行 redismanager/doc/sql/ 路径 `redisadmin-create-db.sql` 脚本，创建一个名称为redisadmin的数据库(相关的表会在应用启动时自动创建)<br/>
**备注**：应用首次启动时把application.yml中的spring.datasource.initializationMode设置为ALWAYS，spring.datasource.continueOnError设置为false<br/>
应用后续启动时把application.yml中的中的spring.datasource.initializationMode设置为NEVER或者把
spring.datasource.initializationMode设置为ALWAYS，spring.datasource.continueOnError设置为true
2. 修改 redismanager-web 模板 `config/config-local.properties` 文件中数据库，redis，zookeeper 的连接和配置
3. 启动 redismanager-web 模块中的 RedisManagerWebApplication 类
4. 正常启动后，访问 http://127.0.0.1

> 用户（权限从大到小）: superadmin, admin, develop, test  
> 密码: 888888

## 功能预览

| 仪表盘 | 个人页 |
|:-----------:|:-----------:|
|![](https://img-blog.csdnimg.cn/2021082815191339.png)|![](https://img-blog.csdnimg.cn/20210828151918149.png)|
| 用户管理 | 角色管理 |
|![](https://img-blog.csdnimg.cn/20210828151943384.png)|![](https://img-blog.csdnimg.cn/20210828151939730.png)|
| 配置管理 | Redis连接管理 |
|![](https://img-blog.csdnimg.cn/20210828151903389.png)|![](https://img-blog.csdnimg.cn/20210828151908667.png)|
| Redis-Operational |  |
|![](https://img-blog.csdnimg.cn/20210828151936100.png)| |

## 贡献指引

## 路线图

## 更新记录

见 [CHANGELOG](CHANGELOG.md) 文件

## 使用协议

```textmate
MIT License

Copyright (c) 2021 tuanzuo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
