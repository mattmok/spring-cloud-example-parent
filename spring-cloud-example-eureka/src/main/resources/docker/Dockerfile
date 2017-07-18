# Dckerizing spring boot: Dockerfile for building Discovery images

FROM      registry.docker.tiamaes.com:5000/tiamaes/java:latest
MAINTAINER chenlili <chenlili@tiamaes.com>

ARG JAR
ADD $JAR /usr/local/apps/app.jar

EXPOSE 8761

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Duser.timezone=Asia/Shanghai", "-jar", "/usr/local/apps/app.jar"]

