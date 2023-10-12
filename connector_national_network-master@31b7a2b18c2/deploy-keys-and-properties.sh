#!/bin/bash

###### PT node ##############################
# Remove default keys
docker exec pt-node-domibus rm /data/tomcat/conf/domibus/keystores/gateway_keystore.jks
docker exec pt-node-domibus rm /data/tomcat/conf/domibus/keystores/gateway_truststore.jks
docker exec pt-node-domibus rm /data/tomcat/conf/domibus/domibus.properties

#copy the new ones 
docker cp ./key-pt-node/gateway_keystore.jks pt-node-domibus:/data/tomcat/conf/domibus/keystores/gateway_keystore.jks
docker cp ./key-pt-node/gateway_truststore.jks pt-node-domibus:/data/tomcat/conf/domibus/keystores/gateway_truststore.jks
docker cp ./key-pt-node/pt-node-domibus.cert pt-node-domibus:/data/tomcat/conf/domibus/keystores/pt-node-domibus.cert
docker cp ./domibus.properties pt-node-domibus:/data/tomcat/conf/domibus/domibus.properties

docker restart pt-node-domibus

# End of the script

