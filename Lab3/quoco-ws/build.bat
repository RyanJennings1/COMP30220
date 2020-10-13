call mvn compile install
call mvn package
call docker-compose down
call docker-compose up
pause
