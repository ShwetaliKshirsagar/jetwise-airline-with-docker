FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/*.jar booking-service.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "booking-service.jar"]
