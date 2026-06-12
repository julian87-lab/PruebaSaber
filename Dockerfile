FROM eclipse-temurin:21-jdk

COPY ./target/pruebasaber-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8101

ENTRYPOINT ["java", "-jar", "app.jar"]