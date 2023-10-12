# data-upload

## Setup

1. Install all of the following software
        * Mongo Database with Version (>= 2.6) and (<= 4.0) - recommended to use MongoDB with version 4.0. You can use the docker image with tag: 4.0-xenial 
        * Java version >= 8
        * Git >= 2.17
  
2. Clone the directory and enter it
    
        git@gitlab.fokus.fraunhofer.de:viaduct/hub/data-upload.git
        
3. Edit the environment variables in the `Dockerfile` according to your local mongodb server. Variables and their purpose are listed below:
   
    |Key|Description|Example|
    |:--- |:---|:---|
    |MONGO_DB_URI| The URL for the mongodb used by this service| mongodb://localhost:27017 |
    |MONGO_DB| The name for the database the files will be stored in  | fileDB |
    |MONGO_USER| The User for the database |  |
    |MONGO_PW | The password for the database |  |
    |HTTP_PORT| Port used to communicate with the API | 8080 | 
    |API_KEY| The API_key used to secure the connection in certain requests | 22921c47-0c78-424b-bac5-ccf4a9393123 |
    
## Run

Build the project by using the provided Maven wrapper. This ensures everyone this software is provided to can use the exact same version of the maven build tool.
The generated _fat-jar_ can then be found in the `target` directory.

* Linux
    
        ./mvn clean package
        java -jar target/data-upload-fat.jar

* Windows

        mvnw.cmd clean package
        java -jar target/data-upload-fat.jar
      
* Docker
    
        1. Start your docker daemon 
        2. Make sure that MongDB is running
        3. Build the application as described in Windows or Linux
        4. Adjust the port number (`EXPOSE` in the `Dockerfile`)
        5. Build the image: `docker build -t data-upload .`
        6. Run the image, adjusting the port number as set in step 3: `docker run -i -p 8081:8081 data-upload`
        7. Configuration can be changed without rebuilding the image by overriding variables: `-e PORT=8086`


## Methods

* Upload files

First the user needs to prepare an entry request by sending a PUT to /v1/data/ . The body must contain a Json Array with one or multiple JSON Objects that have the id and the token of the file.
After that, you need to send a POST request with the file as form-data in the body to this endpoint /v1/data/ + [uuid of the file] +  ?token=[token of the file]. As result, the file is accessible under the upload url without the token query.

* Get files

In Order to request a file, you need to send a GET request to  v1/data/ + UUID of the file.

## API

A formal OpenAPI 3 specification can be found in the `src/main/resources/webroot/openapi.yaml` file.
A visually more appealing version is available at `{url}:{port}` once the application has been started.