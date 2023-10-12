piveau-hub-statistics
=====================

There are two ways to start this application. 

**1. Using Docker**

First step, build the docker image.

    cd <path_to>/piveau-hub-statistics
    
    docker build -t piveau-hub-statistics .
    
Second step, run this image.

    docker run -p 9090:9090 --name statistics -d piveau-hub-statistics
    

**2. As a Python Application**

To start the service as a Python application, a **Python 3.5 or higher** installation is required. 
It is recommended to use *virtualenv* ([User Guide](https://virtualenv.pypa.io/en/latest/userguide/)). 
You can download it via *PyPi* ([Installation Guide](https://virtualenv.pypa.io/en/latest/installation/)):

    pip install virtualenv
    
Change in the programm directory.

    cd piveau-hub-statistics
    
Run virtualenv with the name `env`.

    virtualenv env

Activate the virtual environment.

    source env/bin/activate
    
Now use the `requirements.txt` to install all necessary dependencies via _PyPi_.
The `*` stands for `dev` or `prod`.

    pip install -r requirements_*.txt
    
After that, leave the virtual environment.

    deactivate
    
You can now use the script `start_server.sh` to start a local server, 
as it will use exactly this environment `env`. Use a parameter named `DEV` 
to start this server in _Debug-Mode_. 

    sh start_server.sh DEV 

Dependencies can also be found in the `./requirements_dev.txt` file.
These can be downloaded and installed using the following command: 

`pip install <dependencyname>==<version>`

necessary Dependencies :

| Dependency | Version |
| :--------- | :------ |
| Flask      | 1.0.2   |
| openpyxl   | 2.5.3   |
| celery     | 4.2.0   |
| requests   | 2.19.1  |
| schedule   | 0.5.0   |
| SQLAlchemy | 1.3.6   |
| pandas     | 0.25.1  |
| psycopg2-binary | 2.8.3|


