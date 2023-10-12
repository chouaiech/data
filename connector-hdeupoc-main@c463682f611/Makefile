.DEFAULT_GOAL := local

# ==============================================================================
# API Key

apikey: #generate an API Key
	@go run ./services/gateway/tooling/apikey/main.go

# ==============================================================================

local:
	@go build -o ./nc_ms_gateway ./services/gateway
	@./nc_ms_gateway

kubernetes_nc:
	go build -o ./nc_ms_gateway ./services/gateway/
	# The go build process will automatically build everything referenced in gateway
	./nc_ms_datadiscovery &
	./nc_ms_datapermit &
	./nc_ms_messagechecker &

kubernetes_cs:
	go build -o ./cs_ms_centralservice ./services/centralservice
	./cs_ms_centralservice

kubernetes:
	kubernetes_nc
	kubernetes_cs

kill_nc:
	pkill nc_ms 

kill_cs:
	pkill cs_ms

kill_all: kill_nc kill_cs

createdirs:
	mkdir -p build/centralservice/messagechecker
	mkdir -p build/centralservice/datadiscovery
	mkdir -p build/centralservice/datapermit
	mkdir -p build/nationalconnector/gateway
	mkdir -p build/nationalconnector/messagechecker
	mkdir -p build/nationalconnector/datapermit

createbuilds:
	go build -o ./nc_ms_gateway ./services/gateway/
	go build -o ./nc_ms_datadiscoverycentralservice ./services/datadiscoverycentralservice/
	go build -o ./nc_ms_messagechecker ./services/messagechecker/
	go build -o ./nc_ms_datapermit ./services/datapermit/
	go build -o ./nc_ms_datapermitcentralservice ./services/datapermitcentralservice/

copybuilds:
	cp ./nc_ms_datadiscoverycentralservice build/centralservice/datadiscovery
	cp ./nc_ms_messagechecker build/centralservice/messagechecker
	cp ./nc_ms_datapermitcentralservice build/centralservice/datapermit
	cp ./nc_ms_gateway build/nationalconnector/gateway
	cp ./nc_ms_messagechecker build/nationalconnector/messagechecker
	cp ./nc_ms_datapermit build/nationalconnector/datapermit

copyconfig:
	cp ./services/datadiscoverycentralservice/config.yml build/centralservice/datadiscovery/config.yml
	cp ./services/datapermitcentralservice/config.yml build/centralservice/datapermit/config.yml
	cp ./services/messagechecker/cs.config.yml build/centralservice/messagechecker/config.yml
	cp ./services/messagechecker/national.config.yml build/nationalconnector/messagechecker/config.yml
	cp ./services/gateway/config.yml build/nationalconnector/gateway/config.yml
	cp ./services/datapermit/config.yml build/nationalconnector/datapermit/config.yml

copyjson:
	cp -R ./keys/ build/nationalconnector
	cp ./ap_catalog.json build/centralservice/ap_catalog.json

rmdirs:
	rm -r build

createinstances: createdirs createbuilds copybuilds copyconfig copyjson

startinstances:
	(cd build/centralservice/messagechecker && ./nc_ms_messagechecker &)
	(cd build/centralservice/datadiscovery && ./nc_ms_datadiscoverycentralservice &)
	(cd build/centralservice/datapermit && ./nc_ms_datapermitcentralservice &)
	(cd build/nationalconnector/messagechecker && ./nc_ms_messagechecker &)
	(cd build/nationalconnector/gateway && ./nc_ms_gateway &)
	(cd build/nationalconnector/datapermit && ./nc_ms_datapermit &)

# docker
copydocker:
	cp ./services/datadiscoverycentralservice/Dockerfile build/centralservice/datadiscovery/Dockerfile
	cp ./services/datapermitcentralservice/Dockerfile build/centralservice/datapermit/Dockerfile
	cp ./services/messagechecker/Dockerfile build/centralservice/messagechecker/Dockerfile
	cp ./services/messagechecker/Dockerfile build/nationalconnector/messagechecker/Dockerfile
	cp ./services/gateway/Dockerfile build/nationalconnector/gateway/Dockerfile
	cp ./services/datapermit/Dockerfile build/nationalconnector/datapermit/Dockerfile

copydockerconfig:
	cp ./central.config.docker.yml build/centralservice/config.yml
	cp ./national.config.docker.yml build/nationalconnector/config.yml

dockercomposecopy:
	cp docker-compose.yml build/docker-compose.yml

dockercompose_central_copy:
	cp docker-compose-central.yml build/centralservice/docker-compose.yml

dockercompose_national_copy:
	cp docker-compose-national.yml build/nationalconnector/docker-compose.yml

dockercomposebuild:
	(cd build && docker compose build &)

dockercompose_central_build:
	(cd build/centralservice && docker compose build &)

dockercompose_national_build:
	(cd build/nationalconnector && docker compose build &)

dockercomposestart:
	(cd build && docker-compose up -d &)

dockercompose_central_start:
	(cd build/centralservice && docker compose up -d &)

dockercompose_national_start:
	(cd build/nationalconnector && docker compose up -d &)
	
docker: createdirs createbuilds copybuilds copydocker copydockerconfig copyjson dockercompose_central_copy dockercompose_national_copy dockercompose_central_build dockercompose_national_build