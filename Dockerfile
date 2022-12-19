FROM maven:3.8.6-eclipse-temurin-17-alpine AS MAVEN_BUILD
WORKDIR /app
COPY ./ ./
RUN mvn -Dmaven.test.skip clean package

FROM openjdk:17-alpine
VOLUME /tmp
ARG JAR_FILE=/app/users-spring-boot-exercice-application/target/*.jar
COPY --from=MAVEN_BUILD ${JAR_FILE} app.jar
ENV PORT 8083
EXPOSE $PORT
ENTRYPOINT ["java", "-jar", "/app.jar"]
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar dbcsservidoralfernajosborg.jar
