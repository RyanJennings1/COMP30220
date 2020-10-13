FROM openjdk:8-jre-alpine
COPY target/broker-0.0.1-jar-with-dependencies.jar /broker-0.0.1.jar
CMD ["/usr/bin/java", "-cp", "/broker-0.0.1.jar", "service.core.Broker", "broker"]
