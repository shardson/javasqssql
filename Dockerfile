FROM amazoncorretto:17.0.7-alpine
ADD target/apisqssql-0.0.1-SNAPSHOT.jar apisqssql-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "apisqssql-0.0.1-SNAPSHOT.jar"]