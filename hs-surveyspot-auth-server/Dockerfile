# FROM gradle:6.6.1-jdk8 as builder
#
# COPY build.gradle.kts settings.gradle.kts /home/gradle/source/
# COPY src /home/gradle/source/src
# WORKDIR /home/gradle/source
#
# RUN gradle build --no-daemon

FROM openjdk:8-jre-slim
#COPY --from=builder /home/gradle/source/build/libs/*-all.jar /app/surveyspot-auth-server.jar
COPY build/libs/hs-surveyspot-auth-server-*-all.jar /app/surveyspot-auth-server.jar
WORKDIR /app
EXPOSE 8080 50051
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "surveyspot-auth-server.jar"]