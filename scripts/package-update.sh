#!/bin/bash

DATETIME=$(date +%F--%H-%M);
echo $DATETIME
mvn clean package -f ../modules/activiti-webapp-explorer2/
cp -f ../modules/activiti-webapp-explorer2/target/activiti-webapp-explorer2-5.16.4.war .
cp -f ../engine.properties.prod ./engine.properties
zip ../target/"KVS_AP_"$DATETIME"_bin.zip" ./activiti-webapp-explorer2-5.16.4.war ./engine.properties
rm ./activiti-webapp-explorer2-5.16.4.war
rm ./engine.properties
cp -f ../doc/diegimo_instrukcija.odt .
zip ../target/"KVS_AP_"$DATETIME"_doc.zip" ./diegimo_instrukcija.odt
rm ./diegimo_instrukcija.odt
mvn -Pupdate package -f ..
mv ../target/activiti-root-5.16.4-update-src.zip ../target/"KVS_AP_"$DATETIME"_src.zip"