FROM eclipse-temurin:17-jre

WORKDIR /app
COPY target/*.jar flight-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "flight-service.jar"]
