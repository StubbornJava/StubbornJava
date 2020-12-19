# Use adoptopenjdk/openjdk15:alpine-jre because its ~60MB
# and the openjdk alpine container is ~190MB
FROM adoptopenjdk/openjdk15:alpine-jre AS builder

# We will eventually need more things here
RUN apk add --no-cache curl tar bash

RUN mkdir -p /app
WORKDIR /app

COPY build/libs/ /app/libs

# Using multi build steps here to keep container as small as possible
# Eventually we may add more tooling above
FROM adoptopenjdk/openjdk15:alpine-jre
RUN mkdir -p /app
WORKDIR /app
COPY --from=builder /app/libs /app/libs
COPY docker/entrypoint.sh /app/entrypoint.sh
CMD ["java", "-cp", "libs/*", "com.stubbornjava.webapp.StubbornJavaWebApp"]
