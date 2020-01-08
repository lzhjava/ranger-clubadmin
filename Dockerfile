# --- build
FROM maven:3-jdk-8 AS build

RUN mkdir /app
WORKDIR /app
COPY pom.xml pom.xml
COPY src src

RUN mvn -f ./pom.xml package && mv ./target/ranger-clubadmin-0.0.1-SNAPSHOT.jar app.jar && mvn clean

# --- run
FROM java:8-jre-alpine

COPY --from=build /app/app.jar app.jar

CMD java -jar -Dspring.profiles.active=production app.jar