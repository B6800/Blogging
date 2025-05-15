FROM maven:3.9.9-eclipse-temurin-21 AS build
COPY pom.xml /build/pom.xml
COPY .mvn /build/.mvn
COPY src /build/src
WORKDIR /build/
RUN mvn package

FROM eclipse-temurin:21-jre
COPY --from=build /build/target/streamvailtbackend-application-1.jar /app/streamvaultbackend-application-1.jar
WORKDIR /app/
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.datasource.url=jdbc:mariadb://db:3306/3306/streamvault?createDatabaseIfNotExist=true", "-jar", "streamvaultbackend-application-1.jar"]