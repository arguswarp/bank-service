FROM maven:3.9.4-eclipse-temurin-17-alpine as build
WORKDIR /build
COPY src src
COPY pom.xml pom.xml
RUN --mount=type=cache,target=/root/.m2 mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-alpine

RUN addgroup spring-boot-group && adduser --ingroup spring-boot-group --disabled-password spring-boot
USER spring-boot

VOLUME /tmp

ARG JAR_FILE=bank-service-0.0.1-SNAPSHOT.jar

WORKDIR /application

COPY --from=build /build/target/${JAR_FILE} application.jar

ENTRYPOINT exec java ${JAVA_OPTS} -jar application.jar ${0} ${@}