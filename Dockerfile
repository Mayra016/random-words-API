FROM ubuntu:latest AS build

ENV MYSQLHOST=${MYSQLHOST} \
    MYSQLPORT=${MYSQLPORT} \
	MYSQLDATABASE=${MYSQLDATABASE} \
	MYSQLUSER=${MYSQLUSER} \
	MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
	
RUN echo "MYSQLHOST=${MYSQLHOST}"
RUN echo "MYSQLPORT=${MYSQLPORT}"
RUN echo "MYSQLDATABASE=${MYSQLDATABASE}"
RUN echo "MYSQLUSER=${MYSQLUSER}"
RUN echo "MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}"

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /target/API-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]