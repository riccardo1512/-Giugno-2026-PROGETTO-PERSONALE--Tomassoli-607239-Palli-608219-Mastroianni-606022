FROM amazoncorretto:17-alpine
WORKDIR /app
COPY application.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
