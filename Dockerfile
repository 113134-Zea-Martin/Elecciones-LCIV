FROM openjdk:17-jdk-alpine
COPY target/parcial-2-elecciones-2023-0.0.1.jar java-app.jar
ENTRYPOINT ["java", "-jar","java-app.jar"]