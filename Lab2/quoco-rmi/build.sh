# Lab 2 build script
mvn install
mvn package
sudo docker-compose down
sudo docker-compose up
