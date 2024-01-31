# Drone Delivery Service

### Requirements

For building and running the application, you need:

* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* [Maven 3](https://maven.apache.org/download.cgi)
* [MySQL 8](https://dev.mysql.com/downloads/mysql/)

### How to Run

This application is packaged as a jar which has Tomcat embedded. No Tomcat or JBoss installation is necessary. You run it using the ```java -jar``` command.

* Clone this repository
* Make sure you are using JDK 17 and Maven 3.x
* You can build the project by running ```mvn clean package -DskipTests```
* Once successfully built, you can run the service by running ```java -jar target/drone-task-1.0.jar```
* Check the log file to make sure no exceptions are thrown
* Once the application runs you should see something like this
```
2022-11-14 04:22:31.450  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2022-11-14 04:22:31.490  INFO 1 --- [           main] com.nero.dronetask.DroneTaskApplication  : Started DroneTaskApplication in 14.981 seconds (JVM running for 18.133)
```

### Deploying the Application using Docker
The easiest way to deploy the application is to use the docker
* [Install docker-compose](https://docs.docker.com/compose/install/)
* Start the application by running ```docker-compose up```

### About the Service
The service is just a simple Drone delivery REST service. It uses mysql database to store the data. If you start the service successfully, you can call some REST endpoints.<br />
Please refer to the swagger documentation on: ```/swagger-ui/index.html```
