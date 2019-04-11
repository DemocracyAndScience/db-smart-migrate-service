# 基于哪个镜像
FROM java:8
# 拷贝文件到容器，也可以直接写成ADD microservice-discovery-eureka-0.0.1-SNAPSHOT.jar /app.jar
ADD target/*.jar app.jar
RUN mkdir -p /var/logs/db-service/

# 如果要管理多个库请创建目录
RUN mkdir -p /var/logs/db_locations/dev/shuangshi
RUN mkdir -p /var/logs/db_locations/local/shuangshi
RUN mkdir -p /var/logs/db_locations/staging/shuangshi
RUN mkdir -p /var/logs/db_locations/online/shuangshi
RUN mkdir -p /var/logs/db_locations/test/shuangshi


RUN bash -c 'touch /app.jar'
# 开放8080端口
EXPOSE 8080
# 配置容器启动后执行的命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dfile.encoding=UTF8","-Duser.timezone=GMT+08","-Xmx600m","-jar","/app.jar"]
