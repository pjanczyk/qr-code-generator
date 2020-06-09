FROM openjdk:11-jdk AS builder

WORKDIR /app

ENV GRADLE_OPTS -Dorg.gradle.daemon=false
COPY gradle ./gradle
COPY gradlew build.gradle settings.gradle ./
RUN ./gradlew build -x check || true
RUN ./gradlew generateProto || true
COPY . ./
RUN ./gradlew build -x check

FROM openjdk:11-jre
COPY --from=builder /app/build/libs/web-1.0.0.jar /app/web.jar
EXPOSE 8000
CMD java -server -jar /app/web.jar
