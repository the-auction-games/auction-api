# Builder to compile code
FROM openjdk:17-alpine AS builder
RUN apk add --no-cache maven bash
COPY . .
RUN mvn clean install -D skipTests

# Image with jar file
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder target/*.jar /app/app.jar
EXPOSE 8080
CMD [ "java", "-jar", "app.jar" ]