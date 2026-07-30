FROM maven:3.9.16-amazoncorretto-25-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn clean package -DskipTests -q

FROM amazoncorretto:25-alpine-jdk AS jre-builder
WORKDIR /app
RUN apk add --no-cache binutils
COPY --from=build /app/target/user-service-0.0.1-SNAPSHOT.jar /app.jar
RUN jlink --add-modules java.base,java.logging,java.management,java.sql,java.scripting,java.xml,java.prefs,java.naming,java.net.http,java.desktop,java.security.jgss,java.instrument,java.compiler,jdk.crypto.ec \
    --strip-debug \
    --compress=2 \
    --no-header-files \
    --no-man-pages \
    --output /custom-jre
FROM alpine:3.24
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
ENV JAVA_HOME=/opt/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-builder --chown=appuser:appgroup /custom-jre /opt/jre
COPY --from=build --chown=appuser:appgroup /app/target/user-service-0.0.1-SNAPSHOT.jar /app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
