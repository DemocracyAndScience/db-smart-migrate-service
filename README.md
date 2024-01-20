# db-smart-migrate-server
数据库智能迁移服务

### 使用场景
- 对MySQL表结构的多人协同的变更时，想将dev环境的库中的结构变动同步迁移到test环境， 进行智能化的迁移。并将变更的SQL记录归档需求。下载以便检查后线上执行。
- 提供按照表、字段、索引 的对比步骤，进行展示哪些是新增的，哪些是删除的，哪些是需要更名的手工选择的页面需求。类似git合并冲突代码。
- 固定发版日想要将SQL变更统一管理，减少多人开发多人记录SQL的工作量。
### 痛点：
- mysql各个客户端进行表同步时，会丢失左库中比右库少的表和列；
- 不能解决表更名、字段更名、索引更名的使用场景；
- 不能手动选择自行决定如何处理。
- 不能直观的感受到哪些变更，不能将变更的内容产生SQL文件，用于备份和手动编辑执行。
- 行数据也可能会被覆盖，造成环境不同，串数据的问题。
### 特点
- 支持迁移的内容项有： 数据库表，表属性，列名，列属性，索引名，索引属性。目前不支持外键。
- 字段的属性 如 not null ，自动迁移不提示冲突。
- 提供类似Git的冲突检测功能，对表、列、索引，进行冲突提示，自行决定如何处理。
- 处理选项包含：
    * 新增【将该变更新增到右库】
    * 被更名为【将右库的变更变更名为左库的表名、字段名或索引名】 
    * 删除【删除右库中比左库多的表、字段、或索引】
- 变更内容生成每次点击生成版本的SQL文件。可供下载。
- 不对表数据做变动，保证数据安全。
- 目前仅支持Mysql。
<br/>![avatar](./src/desc-images/a.png)
#### 使用示例图
- 使用SpringBoot 1.5.9.RELEASE
- 使用 Flyway 控制版本

### 用例图
### 活动图
### 安装教程
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


### 注意事项

### 问题反馈
 Email ： 757761927@qq.com 
 微信 ：qiuqiu757761927