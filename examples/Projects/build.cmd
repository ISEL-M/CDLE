@echo off
echo off

call mvn clean package install -DskipTests
rem call mvn clean package install

pause
