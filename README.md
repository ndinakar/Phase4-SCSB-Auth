## Phase4-SCSB-Auth
Auth module for SCSB

The SCSB Middleware codebase and components are all licensed under the Apache 2.0 license, with the exception of a set of API design components (JSF, JQuery, and Angular JS), which are licensed under MIT X11.

[Apache Shiro](https://shiro.apache.org/): Apache Shiro is an open-source software security framework that performs authentication, authorization, cryptography and session management. Shiro has been designed to be an intuitive and easy-to-use framework while still providing robust security features.

PHASE4-SCSB-AUTH is a microservice application that uses APACHE-SHIRO to provide authentication, authorization, cryptography and session management. All the user’s authentication and authorization are done here before they can enter the application. 

## Software Required

  - Java 11
  - Docker 19.03.13   

## Prerequisite

1.**Cloud Config Server**

Dspring.cloud.config.uri=http://phase4-scsb-config-server:8888

## Build

Download the Project , navigate inside project folder and build the project using below command

**./gradlew clean build -x test**

## Docker Image Creation

Naviagte Inside project folder where Dockerfile is present and Execute the below command

**sudo docker build -t phase4-scsb-auth .**

## Docker Run

User the below command to Run the Docker

**sudo docker  run --name phase4-scsb-auth -v /data:/recap-vol  --label collect_logs_with_filebeat="true" --label decode_log_event_to_json_object="true"  -p 9092:9092 -e "ENV= -Dorg.apache.activemq.SERIALIZABLE_PACKAGES="*"  -Dspring.cloud.config.uri=http://phase4-scsb-config-server:8888 "  --network=scsb  -d phase4-scsb-auth**
