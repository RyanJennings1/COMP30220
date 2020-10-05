# Lab 2 build script
cd core
mvn install
cd ..
mvn package
docker-compose up
