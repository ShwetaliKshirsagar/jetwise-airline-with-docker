FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/*.jar payment-service.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "payment-service.jar"]
