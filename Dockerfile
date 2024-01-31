FROM maven:3-openjdk-17 AS build
RUN mkdir -p .
WORKDIR .
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:17-jdk
COPY --from=build target/drone-task-1.0.jar /drone-task-1.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/drone-task-1.0.jar"]