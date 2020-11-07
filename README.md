### Redis单机和集群下数据的查询，添加，修改，删除；支持自定义key，value的序列化方式。
#### v1.0.0

1、支持查询，添加，修改，删除redis连接配置信息

2、支持redis中数据的查询，删除，修改key和key的过期时间

#### v1.1.0

1、优化查询redis中的数据时将数据封装成树形结构的方法

2、优化页面

#### v1.2.0

1、支持对redis连接配置信息中的密码进行加密存储到数据库

2、支持对redis连接配置信息进行测试连接的功能
 
3、支持对redis中的string，set，zset，hash，list等类型的数据进行添加和修改的功能
 
4、优化页面

#### v1.3.0

1、系统支持登录和退出功能

2、支持用户注册，增加个人页菜单，支持查看基本设置和修改密码功能

3、对系统菜单进行权限控制

4、优化页面

#### v1.4.0

1、优化日志追踪-TraceLoggerFactory

2、增加用户管理-用户列表菜单

3、增加角色管理-角色列表菜单

4、优化页面

备注：可以使用superadmin，admin，develop，test等几个用户登录系统，登录密码888888；
其中superadmin用户拥有所有权限

#### v1.5.0

1、项目调整为多模块结构

2、Spring Boot从1.5.4.RELEASE升级到2.3.4.RELEASE版本<br/>

备注：RedisConnectionFactory使用LettuceConnectionFactory替换JedisConnectionFactory

3、增加Dashboard-分析页菜单