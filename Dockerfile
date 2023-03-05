# Builder to compile code
FROM openjdk:17-alpine AS builder
# Install maven
RUN apk add --no-cache maven bash
# Copy code and compile
WORKDIR /build
COPY . .
RUN mvn clean install -D skipTests

# Image with jar file
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder ./build/target/*.jar /app/app.jar

# Environment variables
ENV APP_PORT=8080
ENV SIDECAR_PORT=3500
ENV STATE_STORE_NAME="auction-statestore"

# Expose port
EXPOSE ${APP_PORT}

# Run application
CMD [ "java", "-jar", "app.jar" ]

