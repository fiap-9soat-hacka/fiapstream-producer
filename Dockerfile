FROM maven:3.9.8-eclipse-temurin-21 AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean install -U -Dmaven.test.skip=true

FROM openjdk:21
COPY --from=build /app/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build /app/target/quarkus-app/*.jar /deployments/
COPY --from=build /app/target/quarkus-app/app/ /deployments/app/
COPY --from=build /app/target/quarkus-app/quarkus/ /deployments/quarkus/
COPY --from=build /app/target/quarkus-app/* /deployments/

ENTRYPOINT ["java", "-jar", "/deployments/quarkus-run.jar"]
