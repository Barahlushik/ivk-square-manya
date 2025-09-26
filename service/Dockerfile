FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
FROM jetty:11-jre17
RUN rm -rf /var/lib/jetty/webapps/*
COPY --from=build /app/service/target/service-1.0-SNAPSHOT.war /var/lib/jetty/webapps/ROOT.war
EXPOSE 8080