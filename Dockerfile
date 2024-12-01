FROM gradle:8-jdk17-alpine AS build
WORKDIR /app
COPY src ./src
COPY *.gradle .
RUN gradle build

FROM bellsoft/liberica-openjre-alpine:17.0.13
COPY --from=build /app/build/libs/bookstore-backend-*-SNAPSHOT.jar /app.jar
ENTRYPOINT [ "java", "-jar", "app.jar" ]