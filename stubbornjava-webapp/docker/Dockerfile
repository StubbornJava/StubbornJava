FROM openjdk:8-jdk-alpine AS builder

# ----
# Install Maven
RUN apk add --no-cache curl tar bash

RUN mkdir -p /app
WORKDIR /app

COPY build/libs/ /app/libs

# Using multi build steps here to keep container as small as possible
# Eventually we may add more tooling above
FROM openjdk:8-jre-alpine
RUN mkdir -p /app
WORKDIR /app
COPY --from=builder /app/libs /app/libs
COPY docker/entrypoint.sh /app/entrypoint.sh
CMD ["/usr/bin/java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-cp", "libs/*", "com.stubbornjava.webapp.StubbornJavaWebApp"]
