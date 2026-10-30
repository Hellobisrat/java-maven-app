FROM amazoncorretto:17-alpine-jdk

EXPOSE 8080

WORKDIR /usr/app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

