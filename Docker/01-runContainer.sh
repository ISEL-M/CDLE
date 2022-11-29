@echo off
echo off

set HostName=hadoop
set DockerName=HaddopWithOpenCV

set MountDirectoryExamples="/home/mastetsam/Documents/ISEL/CDLE/examples"

set MountPointExamples=/home/usermr/examples

set MountOptionsExamples=type=bind,source="/home/mastetsam/Documents/ISEL/CDLE/examples",target=/home/usermr/examples

set PortOptions=-p 222:22 -p 8042:8042 -p 8088:8088 -p 9864:9864 -p 9868:9868 -p 9870:9870 -p 19888:19888

set ImageName=hadoop.ubuntu.cdle

echo docker run --hostname %HostName% --name %DockerName% --mount %MountOptionsExamples% --detach %PortOptions% %ImageName%

docker run --hostname hadoop --name HaddopWithOpenCV --mount type=bind,source="/home/mastetsam/Documents/ISEL/CDLE/examples",target=/home/usermr/examples --detach -p 222:22 -p 8042:8042 -p 8088:8088 -p 9864:9864 -p 9868:9868 -p 9870:9870 -p 19888:19888 hadoop.ubuntu.cdle
