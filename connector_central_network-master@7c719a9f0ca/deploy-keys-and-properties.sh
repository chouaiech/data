#!/bin/bash

####### Central Domibus#####################
# Remove default keys
docker exec central-domibus rm /data/tomcat/conf/domibus/keystores/gateway_keystore.jks
docker exec central-domibus rm /data/tomcat/conf/domibus/keystores/gateway_truststore.jks
docker exec central-domibus rm /data/tomcat/conf/domibus/domibus.properties

#copy the new ones 
docker cp ./key-central/gateway_keystore.jks central-domibus:/data/tomcat/conf/domibus/keystores/gateway_keystore.jks
docker cp ./key-central/gateway_truststore.jks central-domibus:/data/tomcat/conf/domibus/keystores/gateway_truststore.jks
docker cp ./key-central/central-domibus.cert central-domibus:/data/tomcat/conf/domibus/keystores/central-domibus.cert
docker cp ./domibus.properties central-domibus:/data/tomcat/conf/domibus/domibus.properties

docker restart central-domibus

# End of the script

