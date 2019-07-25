FROM maven:3-jdk-8

MAINTAINER chaiwei<chaiwei@acegear.com>

COPY pom.xml pom.xml

RUN mvn install

COPY src src

RUN mvn package && mv ./target/ranger-clubadmin-0.0.1-SNAPSHOT.jar clubadmin.jar && mvn clean

CMD java -jar -Dspring.profiles.active=production clubadmin.jar