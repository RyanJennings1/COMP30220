FROM openjdk:8-jdk-alpine
EXPOSE 2551
COPY target/broker-0.0.1.jar /broker-0.0.1.jar
RUN sleep 5
CMD usr/bin/java -jar /broker-0.0.1.jar
