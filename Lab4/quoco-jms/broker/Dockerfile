FROM openjdk:8-jre-alpine
COPY target/broker-0.0.1-jar-with-dependencies.jar /broker-0.0.1.jar
CMD sleep 3; /usr/bin/java -cp /broker-0.0.1.jar service.Broker.Broker
