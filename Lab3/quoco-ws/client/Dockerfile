FROM openjdk:8-jre-alpine
ENV HOST localhost
ENV PORT 9000
COPY target/client-1.0-jar-with-dependencies.jar /client-1.0.jar
CMD /usr/bin/java -cp /client-1.0.jar service.core.Client -h ${HOST} -p ${PORT}
