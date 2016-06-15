###Running Locally
    mvn spring-boot:run

    http://localhost:8080/hawtio/

###Building and Running The Docker Container
    mvn clean install
    docker build -t camel-app .
    docker run -d -p 8080:8080 camel-app

**URL of app**

`http://<docker-machine-ip>/hawtio/ e.g. http://192.168.99.100:8080/hawtio/`

**To find out your docker machine ip**

    docker-machine ip `docker-machine active`