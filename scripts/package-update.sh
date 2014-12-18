#!/bin/bash

DATETIME=$(date +%F--%H-%M);
echo $DATETIME
mvn clean package -f ../modules/activiti-webapp-explorer2/
#cp ../modules/activiti-webapp-explorer2/target/activiti-webapp-explorer2-5.16.4.war 
zip ../target/"KVS_AP_"$DATETIME"_bin.zip" ../modules/activiti-webapp-explorer2/target/activiti-webapp-explorer2-5.16.4.war
mvn -Pupdate package -f ..
mv ../target/activiti-root-5.16.4-update-src.zip ../target/"KVS_AP_"$DATETIME"_src.zip"