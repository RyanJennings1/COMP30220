FROM openjdk:8-jre-alpine
COPY target/client-0.0.1-jar-with-dependencies.jar /client-0.0.1.jar
CMD sleep 10; /usr/bin/java -cp /client-0.0.1.jar client.Main
