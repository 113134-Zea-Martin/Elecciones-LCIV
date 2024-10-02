FROM openjdk:17-jdk-alpine
COPY ./target/parcial-2-elecciones-2023-0.0.1.jar elecciones-app.jar
ENTRYPOINT ["java","-jar","elecciones-app.jar"]