FROM openjdk:8-jdk-alpine
COPY target/springgc1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]