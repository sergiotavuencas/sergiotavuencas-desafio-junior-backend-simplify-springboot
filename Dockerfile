FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/desafio-simplify-0.0.1-SNAPSHOT.jar /app/desafio-simplify.jar

ENTRYPOINT ["java", "-jar", "/app/desafio-simplify.jar"]
