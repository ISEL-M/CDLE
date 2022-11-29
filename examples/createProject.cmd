@echo off
echo off

rem Default values

set DefaultProjetNumber=Ex
set DefaultProjetDescription=Desc
set DefaultProjetVersion=V

rem Don't need to modify from this point

set ProjetNumber=
set ProjetDescription=
set ProjetVersion=

setlocal enableextensions enabledelayedexpansion

if [%1%]==[] (
	set /p ProjetNumber="Type project number [%DefaultProjetNumber%]: "
	if [!ProjetNumber!]==[] (
		set ProjetNumber=%DefaultProjetNumber%
	)
) else (
	set ProjetNumber=%1%
)

if [%2%]==[] (
	set /p ProjetDescription="Type project description [%DefaultProjetDescription%]: "
	if [!ProjetDescription!]==[] (
		set ProjetDescription=%DefaultProjetDescription%
	)
) else (
	set ProjetDescription=%2%
)

if [%3%]==[] (
	set /p ProjetVersion="Type project version [%DefaultProjetVersion%]: "
	if [!ProjetVersion!]==[] (
		set ProjetVersion=%DefaultProjetVersion%
	)
) else (
	set ProjetVersion=%3%
)

set groupID=cdle.mr
set artifactID=%ProjetNumber%-%ProjetDescription%-%ProjetVersion%
set version=2020.2021.SemInv

if exist %artifactID% (
	echo Project folder already exists!
	echo Aborting creation of project.
	pause
	exit 1
)

echo Creating project structure using maven...

set OptsArcheTypeGroupID=-DarchetypeGroupId=org.apache.maven.archetypes
set OptsArcheTypeArtifactId=-DarchetypeArtifactId=maven-archetype-simple
set OptsArchetypeVersion=-DarchetypeVersion=RELEASE
set OptsGroupID=-DgroupId=%groupID%
set OptsArtifactID=-DartifactId=%artifactID%
set OptsVersion=-Dversion=%version%

set Command=mvn ^
archetype:generate ^
%OptsArcheTypeGroupID% ^
%OptsArcheTypeArtifactId% ^
%OptsArchetypeVersion% ^
%OptsGroupID% ^
%OptsArtifactID% ^
%OptsVersion% -DinteractiveMode=false

echo %Command%
%Command%
