#!/bin/bash

DATETIME=$(date +%F--%H-%M);
echo $DATETIME
mvn clean package -f ../modules/activiti-webapp-explorer2/
cp -f ../modules/activiti-webapp-explorer2/target/activiti-webapp-explorer2-5.16.4.war .
cp -f ../engine.properties.prod ./engine.properties
cp -f ../gera-folders-db-reset.jar ./gera-folders-db-reset.jar
zip ../target/"KVS_AP_"$DATETIME"_bin.zip" ./activiti-webapp-explorer2-5.16.4.war ./engine.properties ./gera-folders-db-reset.jar
rm ./activiti-webapp-explorer2-5.16.4.war
rm ./engine.properties
rm ./gera-folders-db-reset.jar
cp -f ../doc/diegimo_instrukcija.odt .
cp -f ../doc/keitimo_turinys.odt .
cp -f ../doc/sistemos_atstatymo_instrukcija.odt .
zip ../target/"KVS_AP_"$DATETIME"_doc.zip" ./diegimo_instrukcija.odt ./keitimo_turinys.odt ./sistemos_atstatymo_instrukcija.odt
rm ./diegimo_instrukcija.odt
rm ./keitimo_turinys.odt
rm ./sistemos_atstatymo_instrukcija.odt
mvn -Pupdate package -f ..
mv ../target/activiti-root-5.16.4-update-src.zip ../target/"KVS_AP_"$DATETIME"_src.zip"