FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} writerpad-0.0.1-SNAPSHOT.jar
ARG DEPENDENCY=src/main/resources/*.txt
COPY ${DEPENDENCY} spamwords.txt
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/writerpad-0.0.1-SNAPSHOT.jar"]