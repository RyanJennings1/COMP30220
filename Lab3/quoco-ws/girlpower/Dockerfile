FROM openjdk:8-jre-alpine
COPY target/girlpower-0.0.1-jar-with-dependencies.jar /girlpower-0.0.1.jar
CMD ["/usr/bin/java", "-cp", "/girlpower-0.0.1.jar", "service.core.Quoter", "girlpower"]
