cd core
call mvn install
cd ..
call mvn package
call docker-compose up
pause
