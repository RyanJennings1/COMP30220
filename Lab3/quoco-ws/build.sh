# Lab 3 build script
mvn compile install
mvn package
sudo docker-compose down
sudo docker-compose up
