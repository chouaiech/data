# httpd ngnix 

## Project information

This project is to configure the label used into the link. 

### Default
The default is including all the projects, setting up until now.

In the command line you can perform the following command 

'docker-compose -f docker-compose-local.yml up --build -d'
The default includes the files:
1. docker-compose-local.yml 
2. Dockerfile
3. ngnix.conf

###Scenario 1
This scenario is excluding the geovierwer projects.

In the command line you can perform the following command 

'docker-compose -f docker-compose-local-scen1.yml up --build -d'

It includes the files:
1. docker-compose-local-scen1.yml
2. Dockerfile_scenario1
2. nginx_scenario1.conf

###Scenario 2
This scenario is excluding the geovierwer projects and mqa.

In the command line you can perform the following command 

'docker-compose -f docker-compose-local-scen2.yml up --build -d'

It includes the files:
1. docker-compose-local-scen2.yml
2. Dockerfile_scenario2
3. nginx_scenario2.conf