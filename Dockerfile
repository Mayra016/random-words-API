#https://www.youtube.com/watch?v=p3AIecyvok4
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
#RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "API-0.0.1-SNAPSHOT.jar"]



