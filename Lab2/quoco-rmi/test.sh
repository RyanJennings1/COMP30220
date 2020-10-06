#!/usr/bin/env bash
mvn install
mvn package
mvn test

# Run project
mvn exec:java -pl client
