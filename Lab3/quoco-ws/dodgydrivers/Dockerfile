FROM openjdk:8-jre-alpine
COPY target/dodgydrivers-0.0.1-jar-with-dependencies.jar /dodgydrivers-0.0.1.jar
CMD ["/usr/bin/java", "-cp", "/dodgydrivers-0.0.1.jar", "service.core.Quoter", "dodgydrivers"]
