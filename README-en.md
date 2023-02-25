# db-smart-migrate-server
#### INTRODUCE
##### Synchronized database structure, version control.
- Instead of the synchronization function of Navicat structure, it solves the problem that if the source table has fewer columns than the target table, the synchronization function of Navicat structure will cause the data to be deleted.<br/>
- Synchronize the table, column, and index information of two schemas between any two environments, but the original data records of the two environments remain unchanged.<br/>
- In the same development cycle, due to product requirements, the development environment table information changes many times, when the environment changes, the need to manually record changes..<br/>
- Solve the problem of database table structure differences between different environments.<br/>
- With source schema as the benchmark and target schema as the goal, we can freely choose the changes to be synchronized and provide page interaction buttons (as shown below).<br/>
- Provide different results of each synchronization, save them as SQL files, and do version management.<br/>
- Synchronized content items are: database tables, table attributes, column names, column attributes, index names, index attributes.<br/>
- Asynchronous Content Items: Data from Datasheets.<br/>
- Synchronization of foreign keys and table spaces is not currently supported because modern projects generally do not seem to need them.<br/>
- Synchronization of foreign keys and table spaces is not currently supported because modern projects generally do not seem to need them.<br/>
- Currently only Mysql is supported.<br/>

<br/>![avatar](./src/desc-images/a.png)
#### SOFTWARE ARCHITECTURE
- Used SpringBoot 1.5.9.RELEASE
- Used Flyway for control version . 


#### INSTALLATION TUTORIAL

##### &emsp;A. CONFIGURE
   1. If you want to configure a single database schema: <br/>
       rule:  
    
       ```  name_spaces:
               database_name:
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
           #   database_name-mysql:
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
    
   2. Create directories in Dockerfile or locally 
   
        - Create directories to generate the path of the SQL file to be executed.
        ```springdataql
          RUN mkdir -p ~/logs/db_locations/dev/
          RUN mkdir -p ~/logs/db_locations/local/
          RUN mkdir -p ~/logs/db_locations/staging/
          RUN mkdir -p ~/logs/db_locations/online/
          RUN mkdir -p ~/logs/db_locations/test/
        ```
        
2. If you want to configure multiple database schemas, just leave the notes open.


#### INSTRUCTIONS

1. Running with IDEA or Eclipse 'src/main/java/com/system/DBVersionControlServerApplication' <br/>
   
2. Local access http://localhost:8081/<br/>
   Login username and password : admin/123456
   First: Click <font color=#008000 >结构同步[Structural synchronization]</font>   ，click <font color=#008000 >开始[start]</font>  Pop-up window ，According to their own needs to operate, after completion, Click <font color=#008000 >迁移【migrate】</font>
   New versions are generated and new SQL files are available for download and viewing. 
3.<br/>![avatar](./src/desc-images/a.png)
4.<br/>![avatar](./src/desc-images/b.png)
5.<br/>![avatar](./src/desc-images/c.png)
6.<br/>![avatar](./src/desc-images/d.png)
7.<br/>![avatar](./src/desc-images/e.png)
8.<br/>![avatar](./src/desc-images/f.png)
9.<br/>![avatar](./src/desc-images/g.png)

#### NOTE

#### PROBLEM FEEDBACK
 Email ： 757761927@qq.com 
