FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/testscheduler-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]