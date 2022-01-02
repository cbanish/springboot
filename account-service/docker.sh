#!/bin/bash
echo "Docker steps to create account service image and run container"

#move to the current project directory where Dockerfile is present
cd STS_Workspace/account-service/

#Build image
 docker build -f Dockerfile -t docker-acct-service .
 
 docker images
 
 docker run -p8085:8085 docker-acct-service