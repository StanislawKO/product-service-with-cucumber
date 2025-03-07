FROM maven:3.9.8-amazoncorretto-21-al2023 AS build

WORKDIR /build

COPY product-service-app/src product-service-app/src
COPY product-service-app/pom.xml product-service-app/pom.xml
COPY openapi openapi

COPY e2e/pom.xml e2e/pom.xml
COPY pom.xml .

RUN mvn -B -pl product-service-app dependency:resolve
RUN mvn -B -pl product-service-app dependency:resolve-plugins

RUN mvn -B -pl product-service-app clean package -Dcheckstyle.skip && \
    VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout) && \
    cp product-service-app/target/product-service-app-${VERSION}.jar product-service-app.jar

FROM openjdk:21-jdk-slim

RUN adduser --system product-service

USER product-service

WORKDIR /product-service-app

COPY --from=build build/product-service-app.jar ./product-service-app.jar

EXPOSE 8080
EXPOSE 8043

ENTRYPOINT ["java", "-jar", "product-service-app.jar"]
