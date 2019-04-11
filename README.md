# db-control-version-server

#### 介绍
数据库版本同步控制服务
- 当前工程功能同步任意两个环境之间的数据库表信息的改动。<br/>

- 改动包括： 数据库表，表属性，列名，列属性，索引名，索引属性。<br/>
- 目前不支持外键和表空间，因为一般现代项目好像不太需要。
- 目前仅支持Mysql。
- 提供有界面，支持自定义改动某些字段。
#### 软件架构
- 使用SpringBoot 1.5.9.RELEASE
- 使用 Flyway 控制版本


#### 安装教程

##### &emsp;A. 配置
   1. 如果要配置单个数据库 schema: <br/>
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
    
   2. 并且 databases 也要配置。
     
        ```springdataql
        databases: 
          names：$ {database-names：shuangshi} 
        # names：shuangshi,shuangshi-assistant
           
        ```
     
   3. Dockerfile 或本地 创建目录  
   
        - 创建目录为生成将要执行的SQL文件路径。
        ```springdataql
          RUN mkdir -p /var/logs/db_locations/dev/
          RUN mkdir -p /var/logs/db_locations/local/
          RUN mkdir -p /var/logs/db_locations/staging/
          RUN mkdir -p /var/logs/db_locations/online/
          RUN mkdir -p /var/logs/db_locations/test/
        ```
        
2. 如果想配置多个数据库 schema   则放开注释即可


#### 使用说明

1. 登陆 用户名密码为 admin/123456 
2. 本地访问 http://localhost:8080/<br/>
   第一步: 点击 <font color=#008000 >结构同步</font>   ，点击 <font color=#008000 >开始</font>  弹出页面，根据自己需求进行操作，完成后，点击 <font color=#008000 >迁移</font>
   此时会生成新的版本，和新的SQL文件可供下载查看 。
3. <br/>![avatar](./src/desc-images/a.png)
4. <br/>![avatar](./src/desc-images/b.png)
5. <br/>![avatar](./src/desc-images/c.png)
6. <br/>![avatar](./src/desc-images/d.png)
7. <br/>![avatar](./src/desc-images/e.png)
8. <br/>![avatar](./src/desc-images/f.png)
9. <br/>![avatar](./src/desc-images/g.png)
10. <br/>![avatar](./src/desc-images/h.png)
11. <br/>![avatar](./src/desc-images/i.png)


#### 问题反馈
 Email ： 757761927@qq.com 
