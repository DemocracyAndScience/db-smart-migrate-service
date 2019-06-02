# db-control-version-server
#### 介绍
数据库结构同步版本控制
- 取代 Navicat结构同步功能，解决如果源表的列比目标表少，Navicat结构同步功能会造成数据被删除的问题。<br/>
- 同步任意两个环境之间的两个schema 的表，列，索引信息,但两个环境原本的数据记录不变。<br/>
- 解决同一个开发周期，由于产品需求，开发环境表信息变动多次，环境切换时，需要手工记录变动的问题。<br/>
- 解决不同环境之间数据库表结构差异问题。<br/>
- 以source schema 为基准，target schema 为目标，自由选择要同步的变动，提供有页面交互按钮(如下图)。<br/>
- 提供每次同步的差异结果，存为SQL文件，做版本管理。<br/>
- 同步的内容项有： 数据库表，表属性，列名，列属性，索引名，索引属性。<br/>
- 不同步的内容项： 数据表的数据。<br/>
- 目前不支持同步外键和表空间，因为一般现代项目好像不太需要。
- 目前仅支持Mysql。
<br/>![avatar](./src/desc-images/a.png)
#### 软件架构
- 使用SpringBoot 1.5.9.RELEASE
- 使用 Flyway 控制版本


#### 安装教程

##### &emsp;A. 配置
   1. 如果要配置单个数据库 schema: <br/>
       规则: 
    
        ```  name_spaces:
               schema_name: 
                 source:
                   datasource:
                     url: ${source_1_url:jdbc:mysql://test:3306/database_name?characterEncoding=utf-8&useSSL=false}
                     username: ${source_1_username:root}
                     password: ${source_1_password:123456}
                     driver-class-name: com.mysql.jdbc.Driver
                 target:
                   datasource:
                     url: ${target_1_url:jdbc:mysql://dev:3306/database_name?characterEncoding=utf-8&useSSL=false}
                     username: ${target_1_username:root}
                     password: ${target_1_password:123456}
                     driver-class-name: com.mysql.jdbc.Driver
           #   schema_name-mysql:
           #     source:
           #       datasource:
           #         url: ${source_1_url:jdbc:mysql://test:3306/database_name-mysql?characterEncoding=utf-8&useSSL=false}
           #         username: ${source_1_username:root}
           #         password: ${source_1_password:123456}
           #         driver-class-name: com.mysql.jdbc.Driver
           #     target:
           #       datasource:
           #         url: ${target_1_url:jdbc:mysql://dev:3306/database_name-mysql?characterEncoding=utf-8&useSSL=false}
           #         username: ${target_1_username:root}
           #         password: ${target_1_password:123456}
           #         driver-class-name: com.mysql.jdbc.Driver
        
        ```
        
   2. Dockerfile 或本地 创建目录  
   
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

1. 使用IDEA 或 Eclipse 运行 src/main/java/com/system/DBVersionControlServerApplication <br/>
   
2. 本地访问 http://localhost:8081/<br/>
    登陆 用户名密码为 admin/123456
   第一步: 点击 <font color=#008000 >结构同步</font>   ，点击 <font color=#008000 >开始</font>  弹出页面，根据自己需求进行操作，完成后，点击 <font color=#008000 >迁移</font>
   此时会生成新的版本，和新的SQL文件可供下载查看 。
3.<br/>![avatar](./src/desc-images/a.png)
4.<br/>![avatar](./src/desc-images/b.png)
5.<br/>![avatar](./src/desc-images/c.png)
6.<br/>![avatar](./src/desc-images/d.png)
7.<br/>![avatar](./src/desc-images/e.png)
8.<br/>![avatar](./src/desc-images/f.png)
9.<br/>![avatar](./src/desc-images/g.png)


#### 注意事项

#### 问题反馈
 Email ： 757761927@qq.com 
