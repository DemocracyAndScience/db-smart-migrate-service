  
# **db-version-control-server**

- 当前工程是同步任意两个环境之间的数据库表信息的改动。<br/>

- 改动包括数据库表，表属性，列名，列属性，索引名，索引属性。<br/>
- 目前不支持外键和表空间，因为一般现代项目好像不太需要。
- 目前仅支持Mysql。
- 提供有界面，支持自定义改动某些字段。

--- 
## 配置
### 1.如果要配置单个数据库 schema
规则: sync_source_【数据库名】:  sync_target_【数据库名】source_1_url。。。 
```springdataql
sync_source_shuangshi: 
  datasource:
    url: ${source_1_url:jdbc:mysql://127.0.0.1:3306/shuangshi?characterEncoding=utf-8&amp;zeroDateTimeBehavior=convertToNull}
    username: ${source_1_username:root}
    password: ${source_1_password:tengyue360}
    driver-class-name: com.mysql.jdbc.Driver
sync_target_shuangshi: 
  datasource:
    url: ${target_1_url:jdbc:mysql://172.16.16.128:3306/shuangshi?characterEncoding=utf-8&amp;zeroDateTimeBehavior=convertToNull}
    username: ${target_1_username:root}
    password: ${target_1_password:tengyue360}
    driver-class-name: com.mysql.jdbc.Driver

#sync_source_shuangshi-assistant: 
#  datasource:
#    url: ${source_2_url:jdbc:mysql://127.0.0.1:3306/shuangshi-assistant?characterEncoding=utf-8&amp;zeroDateTimeBehavior=convertToNull}
#    username: ${source_2_username:root}
#    password: ${source_2_password:tengyue360}
#    driver-class-name: com.mysql.jdbc.Driver
#sync_target_shuangshi-assistant: 
#  datasource:
#    url: ${target_2_url:jdbc:mysql://172.16.16.128:3306/shuangshi-assistant?characterEncoding=utf-8&amp;zeroDateTimeBehavior=convertToNull}
#    username: ${target_2_username:root}
#    password: ${target_2_password:tengyue360}
#    driver-class-name: com.mysql.jdbc.Driver

```

 ### 【2】并且 databases 也要配置。
 ```springdataql
 databases: 
   names：$ {database-names：shuangshi} 
 # names：shuangshi,shuangshi-assistant

```
###【3】Dockerfile 或本地 创建目录  
- 创建目录为生成将要执行的SQL文件路径。
RUN mkdir -p /var/logs/db_locations/dev/
RUN mkdir -p /var/logs/db_locations/local/
RUN mkdir -p /var/logs/db_locations/staging/
RUN mkdir -p /var/logs/db_locations/online/
RUN mkdir -p /var/logs/db_locations/test/


### 2.如果想配置多个数据库 schema  
       则放开注释即可
